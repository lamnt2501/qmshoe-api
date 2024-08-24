package com.lamdangfixbug.qmshoe.payment.service;

import com.lamdangfixbug.qmshoe.configurations.VNPayConfig;
import com.lamdangfixbug.qmshoe.order.entity.Order;
import com.lamdangfixbug.qmshoe.order.entity.OrderStatus;
import com.lamdangfixbug.qmshoe.order.payload.request.OrderRequest;
import com.lamdangfixbug.qmshoe.order.payload.request.UpdateOrderStatusRequest;
import com.lamdangfixbug.qmshoe.order.service.OrderService;
import com.lamdangfixbug.qmshoe.payment.entity.PaymentDetails;
import com.lamdangfixbug.qmshoe.payment.entity.PaymentStatus;
import com.lamdangfixbug.qmshoe.payment.payload.request.UpdatePaymentRequest;
import com.lamdangfixbug.qmshoe.payment.repository.PaymentDetailsRepository;
import com.lamdangfixbug.qmshoe.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class VNPayService {
    private final VNPayConfig vnPayConfig;
    private final OrderService orderService;
    private final PaymentService paymentService;
    private final PaymentDetailsRepository paymentDetailsRepository;

    public VNPayService(VNPayConfig vnPayConfig, OrderService orderService, PaymentService paymentService, PaymentDetailsRepository paymentDetailsRepository) {
        this.vnPayConfig = vnPayConfig;
        this.orderService = orderService;
        this.paymentService = paymentService;
        this.paymentDetailsRepository = paymentDetailsRepository;
    }

    public String getPaymentUrl(Order order, String ipAddress) {
        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String vnp_TxnRef = String.valueOf(order.getId());
//                vnPayConfig.getRandomNumber(8);
        String vnp_IpAddr = "127.0.0.1";
//                ipAddress;
        String vnp_TmnCode = vnPayConfig.getVnp_TmnCode();
        String orderType = "order-type";

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf((int) order.getTotal() * 100));
        vnp_Params.put("vnp_CurrCode", "VND");

        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", Utils.deAccent(order.getPaymentDetails().getDescription()));
        vnp_Params.put("vnp_OrderType", orderType);

        String locale = "vn";
        vnp_Params.put("vnp_Locale", locale);

        vnp_Params.put("vnp_ReturnUrl", vnPayConfig.getReturnUrl());
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator<String> itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = itr.next();
            String fieldValue = vnp_Params.get(fieldName);
            if ((fieldValue != null) && (!fieldValue.isEmpty())) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                //Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = vnPayConfig.hmacSHA512(vnPayConfig.getVnp_HashSecret(), hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        return vnPayConfig.getVnp_PayUrl() + "?" + queryUrl;

    }

//    @Transactional()
    public Map<String, String> ipnVNPay(HttpServletRequest request) {
        Map<String, String> response = new HashMap<>();
        String rspCode = "";
        String message = "";
        try {
            Map<String, String> fields = new HashMap<>();
            for (Enumeration<String> params = request.getParameterNames(); params.hasMoreElements(); ) {
                String fieldName = URLEncoder.encode((String) params.nextElement(), StandardCharsets.US_ASCII);
                String fieldValue = URLEncoder.encode(request.getParameter(fieldName), StandardCharsets.US_ASCII);
                if ((fieldValue != null) && (!fieldValue.isEmpty())) {
                    fields.put(fieldName, fieldValue);
                }
            }

            String vnp_SecureHash = request.getParameter("vnp_SecureHash");
            fields.remove("vnp_SecureHashType");
            fields.remove("vnp_SecureHash");
            String signValue = vnPayConfig.hashAllFields(fields);


            if (signValue.equals(vnp_SecureHash)) {
                boolean checkOrderId = orderService.orderExists(Integer.parseInt(request.getParameter("vnp_TxnRef")));
                if (checkOrderId) {
                    Order order = orderService.getOrderAdmin(Integer.parseInt(request.getParameter("vnp_TxnRef")));
                    boolean checkAmount = (Integer.parseInt(request.getParameter("vnp_Amount")) / 100) == (int) order.getTotal();
                    if (checkAmount) {
                        PaymentDetails paymentDetails = order.getPaymentDetails();
                        boolean paymentStatus = paymentDetails.getStatus() == PaymentStatus.PROCESSING;
                        System.out.println(paymentStatus);
                        if (paymentStatus) {
                            paymentDetails.setTransactionIdRef(request.getParameter("vnp_TransactionNo"));
                            if ("00".equals(request.getParameter("vnp_ResponseCode"))) {
                                paymentDetails.setStatus(PaymentStatus.PAID);
                            } else {
                                paymentDetails.setStatus(PaymentStatus.CANCELLED);
                                orderService.updateOrderAdmin(UpdateOrderStatusRequest.builder()
                                        .id(paymentDetails.getOrderId())
                                        .status(OrderStatus.CANCEL)
                                        .message("Payment failed")
                                        .build());
                            }
                            paymentService.updatePayment(UpdatePaymentRequest.builder().id(paymentDetails.getId()).status(paymentDetails.getStatus()).build());
                            paymentDetailsRepository.save(paymentDetails);
                            rspCode = "00";
                            message = "Confirm Success";
                        } else {
                            rspCode = "02";
                            message = "Order already confirmed";
                        }
                    } else {
                        rspCode = "04";
                        message = "Invalid Amount";
                    }
                } else {
                    rspCode = "01";
                    message = "Order not Found";
                }
            } else {
                rspCode = "97";
                message = "Invalid Checksum";
            }
        } catch (Exception e) {
            rspCode = "99";
            message = "Unknown error";
        }
        response.put("RspCode", rspCode);
        response.put("Message", message);
        return response;
    }
}

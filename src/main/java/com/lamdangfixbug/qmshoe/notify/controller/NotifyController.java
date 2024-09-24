package com.lamdangfixbug.qmshoe.notify.controller;

import com.lamdangfixbug.qmshoe.notify.service.NotifyService;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class NotifyController {

    private NotifyService notifyService;


}

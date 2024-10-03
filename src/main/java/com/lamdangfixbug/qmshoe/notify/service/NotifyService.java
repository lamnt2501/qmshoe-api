package com.lamdangfixbug.qmshoe.notify.service;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotifyService {
    private final SimpMessagingTemplate template;

    public NotifyService(SimpMessagingTemplate template) {
        this.template = template;
    }

    public void notify(String message,String destination) {
        template.convertAndSend("/topic/notify" + destination, message);
    }
}

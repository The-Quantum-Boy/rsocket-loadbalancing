package com.sumit.controller;

import com.sumit.dto.Request;
import com.sumit.service.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.annotation.ConnectMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

@Controller
public class RequestController {

    @Autowired
    private RequestService service;

    @ConnectMapping
    public Mono<Void> connectionSetup(RSocketRequester rSocketRequester){
        System.out.println("connection setup");
        return Mono.empty();
    }


    @MessageMapping("request.print")
    public Mono<Void> print(Mono<Request> requestMono){
        return service.fireAndForget(requestMono);
    }


}

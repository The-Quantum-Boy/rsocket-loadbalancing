package com.sumit.controller;


import com.sumit.dto.Request;
import com.sumit.service.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class RequestController {

    @Autowired
    private RequestService requestService;



    @GetMapping("ss")
    public Mono<Void> testServersideLB(Mono<Request> requestMono)  {
        return requestService
                .initServerSideLB()
                .then();
    }

    @GetMapping("cs")
    public Mono<Void> testClientSideLB(Mono<Request> requestMono)  {
        return requestService
                .initClientSideLB()
                .then();
    }


}

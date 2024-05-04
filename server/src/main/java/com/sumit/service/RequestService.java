package com.sumit.service;


import com.sumit.dto.Request;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class RequestService {

    public Mono<Void> fireAndForget(Mono<Request> requestMono){
        return requestMono
                .doOnNext(System.out::println)
                .then();
    }

}

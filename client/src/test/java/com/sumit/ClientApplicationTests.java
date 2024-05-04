package com.sumit;

import com.sumit.controller.RequestController;
import com.sumit.dto.Request;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest
class ClientApplicationTests {

	@Autowired
	RequestController requestController;

	@Test
	void contextLoads() {

	}

	@Test
	void testLoadBalancing(){
//		Request request=new Request(2);
//		Mono<Request> requestMono=Mono.just(request);
//
//		Mono<Void> voidMono = requestController.testLoadBalancing(requestMono);
//
//		StepVerifier.create(voidMono)
//				.expectComplete()
//				.verify();

	}

}

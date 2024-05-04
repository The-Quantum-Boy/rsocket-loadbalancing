package com.sumit.service;


import com.sumit.config.ServiceRegistry;
import com.sumit.dto.Request;
import io.rsocket.loadbalance.RoundRobinLoadbalanceStrategy;
import io.rsocket.loadbalance.WeightedLoadbalanceStrategy;
import io.rsocket.transport.netty.client.TcpClientTransport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.RSocketStrategies;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
public class RequestService {

    private static final RSocketStrategies STRATEGIES = RSocketStrategies.builder()
            .encoders(encoders -> encoders.add(new Jackson2JsonEncoder()))
            .decoders(decoders -> decoders.add(new Jackson2JsonDecoder()))
            .build();

    @Autowired
    public ServiceRegistry serviceRegistry;


    public Mono<Void> initServerSideLB()  {

        RSocketRequester requester1 = RSocketRequester
                .builder()
                .rsocketStrategies(STRATEGIES)
                .transport(TcpClientTransport.create("localhost", 6566));




        RSocketRequester requester2 = RSocketRequester
                .builder()
                .rsocketStrategies(STRATEGIES)
                .transport(TcpClientTransport.create("localhost", 6566));


        Flux.range(0, 50)
                .delayElements(Duration.ofSeconds(2))
                .map(s->{
                    System.out.println("Sending ->"+s);
                    return s;
                })
                .flatMap(i -> Flux.concat(
                        requester1.route("request.print").data(new Request(i)).send(),
                        requester2.route("request.print").data(new Request(i)).send()
                ))
                .subscribe();

        return Mono.empty();

    }

    public Mono<Void> initClientSideLB()  {

        RSocketRequester requester = RSocketRequester
                .builder()
                .rsocketStrategies(STRATEGIES)
//                .transports(serviceRegistry.targetsFlux(),new RoundRobinLoadbalanceStrategy())
                .transports(serviceRegistry.targetsFlux(), WeightedLoadbalanceStrategy.create())
                ;

        Flux.range(0, 50)
                .delayElements(Duration.ofSeconds(2))
                .map(s->{
                    System.out.println("Sending ->"+s);
                    return s;
                })
                .flatMap(i -> requester
                        .route("request.print")
                        .data(new Request(i)).send()
                )
                .subscribe();

        return Mono.empty();

    }
}

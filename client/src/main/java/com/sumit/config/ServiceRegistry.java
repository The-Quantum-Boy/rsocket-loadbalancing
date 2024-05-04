package com.sumit.config;


import io.rsocket.loadbalance.LoadbalanceTarget;
import io.rsocket.transport.ClientTransport;
import io.rsocket.transport.netty.client.TcpClientTransport;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.ReactiveDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class ServiceRegistry {

    @Autowired
    public DiscoveryClient discoveryClient;


    public List<ServiceInstance> getServers() {
        List<String> services = this.discoveryClient.getServices();
        List<ServiceInstance> instances = new ArrayList<>();
        services.forEach(serviceName -> {
            instances.addAll(this.discoveryClient.getInstances(serviceName));
        });
        System.out.println("Registered eureka instances -> "+instances);
        return instances;
    }


    @Bean
    public Flux<List<LoadbalanceTarget>> targetsFlux(){

        return Flux.from(targetedServer()); //simple publishing
//        return Flux.interval(Duration.ofSeconds(5)) // this will get server instances at every 5 second and
//                .flatMap(i->targets())
//                .doOnNext(l->l.remove(ThreadLocalRandom.current().nextInt(0,3))); //we randomly removes the any one instace for loadbalancing

    }

    private Mono<List<LoadbalanceTarget>> targetedServer(){
        return Mono.fromSupplier(()->
                this.getServers()
                        .stream()
                        .map(server->LoadbalanceTarget.from(key(server),transport(server)))
                        .collect(Collectors.toList())
        );
    }

    private String key(ServiceInstance instance) {
        System.out.println("host -> "+instance.getHost()+" port->"+instance.getPort());
        return instance.getHost()+instance.getPort();
    }

    private ClientTransport transport(ServiceInstance instance) {
        return TcpClientTransport.create(instance.getHost(), instance.getPort());

    }



}

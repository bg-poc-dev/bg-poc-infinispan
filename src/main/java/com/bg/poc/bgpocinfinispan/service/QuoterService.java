package com.bg.poc.bgpocinfinispan.service;

import com.bg.poc.bgpocinfinispan.cache.DataCache;
import com.bg.poc.bgpocinfinispan.domain.Quoter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class QuoterService {

    @Autowired
    private DataCache dataCache;

    @Value("${qouters.uri}")
    private String qoutersUri;

    private WebClient webClient = WebClient.create();

    public Flux<Quoter> fetchQuoters() {
        return webClient.get().uri(qoutersUri).retrieve()
                .onStatus(HttpStatus::is4xxClientError, resp -> Mono.just(new RuntimeException(resp.statusCode().value() + " : " + resp.statusCode().getReasonPhrase())))
                .bodyToFlux(Quoter.class).filter(q -> dataCache.quoter(q.getValue().getId()) == null)
                .concatWith(newQuoter())
                .doOnNext(q -> dataCache.quoterPut(q.getValue().getId(), q));
    }

    private Flux<Quoter> newQuoter() {
        Long now = System.currentTimeMillis();
        return Flux.just(new Quoter("success", new Quoter.Value(now.toString(),
                String.format("Quote added at %s", now))));
    }

}

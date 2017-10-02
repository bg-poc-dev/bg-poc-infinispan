package com.bg.poc.bgpocinfinispan.endpoint;

import com.bg.poc.bgpocinfinispan.domain.Quot;
import lombok.extern.java.Log;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.time.Duration;
import java.util.Random;

@RestController
@Log
public class QuotEndpoint {

    private static final Random random = new Random();

    @PostMapping(path = "/quot", consumes = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Mono<ResponseEntity<Void>> quot(@RequestBody Mono<Quot> quot) {
        return quot.delaySubscription(Duration.ofSeconds(random.nextInt(5 - 2) + 2))
                .map(q -> {
                    log.info(String.format("POST quot: %s with id [%s]", q.getQuote(), q.getId()));
                    return ResponseEntity.created(quotUrl(q.getId())).build();
                });
    }

    private static URI quotUrl(String id) {
        return URI.create("/quot/" + id);
    }
}

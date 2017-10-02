package com.bg.poc.bgpocinfinispan.endpoint;

import com.bg.poc.bgpocinfinispan.domain.Quot;
import com.bg.poc.bgpocinfinispan.service.QuoterService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;

@RestController
@Log
public class QuotImportEndpoint {

    private WebClient quoterwebClient = WebClient.create();

    @Value("${server.port}")
    private String serverPort;

    @Autowired
    private QuoterService quoterService;

    @PostMapping(path = "/quotimport")
    public Mono<ResponseEntity<String>> importQuot() {
        return
                quoterService.fetchQuoters()
                        .flatMap(q -> quoterwebClient.post().uri(quotURI())
                                .contentType(MediaType.APPLICATION_STREAM_JSON)
                                .body(BodyInserters.fromObject(new Quot(q.getValue().getId(), q.getValue().getQuote())))
                                .exchange()
                                .map(this::quotUri)
                        )
                        .count()
                        .map(v -> ResponseEntity.ok(String.format("%d quotes were imported", v)));
    }

    private URI quotURI() {
        return URI.create("http://localhost:" + serverPort + "/quot");
    }

    private ResponseEntity<Void> quotUri(ClientResponse response) {
        URI location = response.headers().asHttpHeaders().getLocation();
        return ResponseEntity.created(location).build();
    }

}

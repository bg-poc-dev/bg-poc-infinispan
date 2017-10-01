package com.bg.poc.bgpocinfinispan.endpoint;

import com.bg.poc.bgpocinfinispan.domain.Quot;
import com.bg.poc.bgpocinfinispan.domain.Quoter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URI;

@RestController
public class QuotImportEndpoint {

    private WebClient quotwebClient = WebClient.create();
    private WebClient quoterwebClient = WebClient.create();

    @Value("${server.port}")
    private String serverPort;

    @PostMapping(path = "/quotimport")
    public ResponseEntity<Void> importQuot() {
        return quotwebClient.get().uri(quotersURI()).retrieve().bodyToFlux(Quoter.class)
                .flatMap(q ->
                        quoterwebClient.post().uri(quotURI())
                                .body(BodyInserters.fromObject(new Quot(q.getValue().getId(), q.getValue().getQuote())))
                                .exchange().map(this::quotUri)
                )
                .blockLast();
    }

    private URI quotersURI() {
        return URI.create("http://localhost:" + serverPort + "/quoters");
    }

    private URI quotURI() {
        return URI.create("http://localhost:" + serverPort + "/quot");
    }

    private ResponseEntity<Void> quotUri(ClientResponse response) {
        URI location = response.headers().asHttpHeaders().getLocation();
        return ResponseEntity.created(location).build();
    }

}

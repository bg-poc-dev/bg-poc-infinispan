package com.bg.poc.bgpocinfinispan.endpoint;

import com.bg.poc.bgpocinfinispan.domain.Quoter;
import com.bg.poc.bgpocinfinispan.service.QuoterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class QuotersEndpoint {

    @Autowired
    private QuoterService quoterService;

    @GetMapping(path = "/quoters")
    public Flux<Quoter> quoters() {
        return quoterService.fetchQuoters();
    }

}

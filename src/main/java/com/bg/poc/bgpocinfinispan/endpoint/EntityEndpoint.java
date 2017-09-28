package com.bg.poc.bgpocinfinispan.endpoint;

import com.bg.poc.bgpocinfinispan.cache.DataCache;
import com.bg.poc.bgpocinfinispan.domain.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/data")
public class EntityEndpoint {

    @Autowired
    private DataCache cache;

    @GetMapping(path = "/{key}")
    public ResponseEntity<Data> data(@PathVariable("key") String key) {
        return ResponseEntity.ok(cache.findData(key));
    }

}

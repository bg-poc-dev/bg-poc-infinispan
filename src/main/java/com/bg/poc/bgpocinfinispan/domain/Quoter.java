package com.bg.poc.bgpocinfinispan.domain;

import lombok.Value;

import java.io.Serializable;

@Value
public class Quoter implements Serializable {

    private String type;
    private Value value;

    @lombok.Value
    public static class Value implements Serializable {
        private String id;
        private String quote;
    }

}

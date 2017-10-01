package com.bg.poc.bgpocinfinispan.domain;

import lombok.Value;

import java.io.Serializable;

@Value
public class Quot implements Serializable {

    private String id;
    private String quote;
}

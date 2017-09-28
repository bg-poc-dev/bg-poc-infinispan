package com.bg.poc.bgpocinfinispan.domain;

import lombok.Value;

import java.io.Serializable;

/**
 * Created by belikov on 28.09.17.
 */
@Value
public class Data implements Serializable {

    String key;
    String value;
}

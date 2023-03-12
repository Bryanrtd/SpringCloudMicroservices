package com.microservice.shoppingservice.controller;

import java.util.*;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @Builder
public class ErrorMessage {
    private String code;
    private List<Map<String, String>> messages;
}

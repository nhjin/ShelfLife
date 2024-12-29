package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class AuthVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String  id;
    private String  key;
    private String  tokenType;
    private String  tokenValidate;
}

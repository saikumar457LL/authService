package org.ocean.authservice.dao;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class TokenDto {
    private String token;
    private Date iAt;
    private Date eAt;
    private String scope;
    private String tokenType;
}

package com.authentication.jwtauth.customResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@AllArgsConstructor
@Component
@ComponentScan("com.authentication.jwtauth.customResponse")
@NoArgsConstructor
public class CustomResponse {
    private Boolean success;
    private String message;
    private String token;
    private List<Object> details;
}

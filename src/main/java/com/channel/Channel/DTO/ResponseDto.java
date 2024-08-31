package com.channel.Channel.DTO;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ResponseDto {
    HttpStatus statusCode;
    Integer code;
    String msg;
    Object body;
}

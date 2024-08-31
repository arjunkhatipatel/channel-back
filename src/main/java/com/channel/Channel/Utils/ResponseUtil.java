package com.channel.Channel.Utils;

import com.channel.Channel.DTO.ResponseDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class ResponseUtil {
    public ResponseEntity<ResponseDto> createResponse(Object body, HttpStatus statusCode, Integer code, String msg) {
        ResponseDto responseDto = new ResponseDto();

        responseDto.setBody(body);
        responseDto.setStatusCode(statusCode);
        responseDto.setCode(code);
        responseDto.setMsg(msg);

        return new ResponseEntity<>(responseDto, new HttpHeaders(), statusCode);
    }
}

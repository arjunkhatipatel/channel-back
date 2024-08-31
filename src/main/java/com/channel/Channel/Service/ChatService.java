package com.channel.Channel.Service;


import com.channel.Channel.DTO.ChatDto;
import com.channel.Channel.DTO.ResponseDto;
import org.springframework.http.ResponseEntity;

public interface ChatService {
    
    public ResponseEntity<ResponseDto> loginUser(ChatDto chatDto);
}

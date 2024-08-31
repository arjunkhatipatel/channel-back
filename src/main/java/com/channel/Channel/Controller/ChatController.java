package com.channel.Channel.Controller;

import com.channel.Channel.DTO.ChatDto;
import com.channel.Channel.DTO.ResponseDto;
import com.channel.Channel.Service.Impl.ChatServiceImpl;
import com.channel.Channel.Utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class ChatController {
    @Autowired
    ResponseUtil responseUtil;

    @Autowired
    ChatServiceImpl chatService;

    // wakup endpoint to up the service
    @GetMapping("/wakeup")
    public ResponseEntity<ResponseDto> wakeUp() {
        return responseUtil.createResponse(null, HttpStatus.OK, 200, "Response from wakeup");
    }

    // Login endpoint
    @PostMapping("/login")
    public ResponseEntity<ResponseDto> login(@RequestBody ChatDto chatDto) {
        System.out.println("Login request received: " + chatDto);
        return chatService.loginUser(chatDto);
    }
}

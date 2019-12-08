package com.myproject.myproject.controller;

import com.myproject.myproject.model.FbUsers;
import com.myproject.myproject.model.ChatfuelMessages;
import com.myproject.myproject.model.ChatfuelText;
import com.myproject.myproject.model.ClearCartPostBody;
import com.myproject.myproject.service.FbUsersService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
//@CrossOrigin(origins = "http://192.168.0.8")
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1")
public class FbUsersController {

    private static final Logger LOGGER = LoggerFactory.getLogger(FbUsersController.class);

    @Autowired
    private FbUsersService fbUsersService;

    @PostMapping("/chatfuel/fb-users")
    public ResponseEntity<ChatfuelMessages> create(@RequestBody FbUsers fbUsers) {
        List<ChatfuelText> chatfuelTexts = new ArrayList<>();

        try {
            fbUsersService.addFbUsers(fbUsers);
            chatfuelTexts.add(new ChatfuelText("加入成功"));
        } catch (Exception ignored) {

        }

        chatfuelTexts.add(new ChatfuelText("希望您購物愉快"));

        return ResponseEntity.ok(new ChatfuelMessages(chatfuelTexts));
    }
}

package com.myproject.myproject.controller;

import com.myproject.myproject.model.Carts;
import com.myproject.myproject.model.ChatfuelMessages;
import com.myproject.myproject.model.ChatfuelText;
import com.myproject.myproject.model.ClearCartPostBody;
import com.myproject.myproject.service.CartsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

@RestController
//@CrossOrigin(origins = "http://192.168.0.8")
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1")
public class ChatfuelCartsController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChatfuelCartsController.class);

    @Autowired
    private CartsService cartsService;

    @GetMapping("/chatfuel/carts/messenger-user/{messengerUserId}")
    public ResponseEntity<List<Carts>> showByMessengerUserId(@PathVariable(value = "messengerUserId") String messengerUserId) {
        return new ResponseEntity<>(cartsService.findByMessengerUserId(messengerUserId), HttpStatus.OK);
    }

    @PostMapping("/chatfuel/carts")
    public ResponseEntity<ChatfuelMessages> create(@RequestBody Carts carts) {
        List<ChatfuelText> chatfuelTexts = new ArrayList<>();
        chatfuelTexts.add(new ChatfuelText("加入購物車成功"));
        return ResponseEntity.ok(new ChatfuelMessages(chatfuelTexts));
    }

    @PostMapping("/chatfuel/carts/checkout")
    public ResponseEntity<ChatfuelMessages> checkout(@RequestBody ClearCartPostBody clearCartPostBody) {
        List<Carts> cartsList = cartsService.findByMessengerUserId(clearCartPostBody.getMessengerUserId());
        List<ChatfuelText> chatfuelTexts = new ArrayList<>();

        if (cartsList.isEmpty()) {
            chatfuelTexts.add(new ChatfuelText("購物車是空的"));
            return ResponseEntity.ok(new ChatfuelMessages(chatfuelTexts));
        }

        int totalPrice = 0;
//        StringBuilder detailStringBuilder = new StringBuilder();
        for (Carts carts : cartsList) {
            String addText = String.format(
                    "品名: %s, 數量: %d NT$: %d",
                    carts.getProductName(), carts.getQty(), carts.getPrice());
            totalPrice += carts.getPrice();
//            detailStringBuilder.append(String.format("%s, ", carts.getProductName()));
            chatfuelTexts.add(new ChatfuelText(addText));
        }

        chatfuelTexts.add(new ChatfuelText(String.format("合計 NT$: %d", totalPrice)));
        chatfuelTexts.add(new ChatfuelText("結帳完成，感謝您的購買。"));

        return ResponseEntity.ok(new ChatfuelMessages(chatfuelTexts));
    }

    @PostMapping("/chatfuel/carts/clear")
    public ResponseEntity<ChatfuelMessages> clear(@RequestBody ClearCartPostBody clearCartPostBody) {
        List<ChatfuelText> chatfuelTexts = new ArrayList<>();
        cartsService.deleteByMessengerUserId(clearCartPostBody.getMessengerUserId());
        chatfuelTexts.add(new ChatfuelText("清除購物車成功"));
        return ResponseEntity.ok(new ChatfuelMessages(chatfuelTexts));
    }
}

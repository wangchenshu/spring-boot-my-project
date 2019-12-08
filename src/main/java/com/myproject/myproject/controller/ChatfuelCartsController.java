package com.myproject.myproject.controller;

import com.myproject.myproject.model.*;
import com.myproject.myproject.service.CartsService;
import com.myproject.myproject.service.OrdersService;
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

    @Autowired
    private OrdersService ordersService;

    private static final String CARTS_EMPTY = "購物車是空的";
    private static final String CARTS_CLEAR_SUCCESS = "清除購物車成功";
    private static final String CARTS_ADD_SUCCESS = "加入購物車成功";
    private static final String ORDER_FINISH = "結帳完成，感謝您的購買";

    private List<ChatfuelText> listCarts(List<Carts> cartsList) {
        List<ChatfuelText> chatfuelTexts = new ArrayList<>();

        if (cartsList.isEmpty()) {
            return chatfuelTexts;
        }

        StringBuilder detailStringBuilder = new StringBuilder();
        for (Carts carts : cartsList) {
            String addText = String.format(
                    "%s %d 個 %d元",
                    carts.getProductName(), carts.getQty(), carts.getPrice());
            detailStringBuilder.append(addText);
            detailStringBuilder.append("\n");
        }

        chatfuelTexts.add(new ChatfuelText(detailStringBuilder.toString()));

        return chatfuelTexts;
    }

    @GetMapping("/chatfuel/carts/messenger-user/{messengerUserId}")
    public ResponseEntity<ChatfuelMessages> showByMessengerUserId(@PathVariable(value = "messengerUserId") String messengerUserId) {
        List<Carts> cartsList = cartsService.findByMessengerUserId(messengerUserId);
        List<ChatfuelText> chatfuelTexts = listCarts(cartsList);

        if (chatfuelTexts.isEmpty()) {
            chatfuelTexts.add(new ChatfuelText(CARTS_EMPTY));
            return ResponseEntity.ok(new ChatfuelMessages(chatfuelTexts));
        }

        int totalPrice = cartsList
                .stream()
                .mapToInt(carts -> carts.getQty() * carts.getPrice())
                .sum();
        chatfuelTexts.add(new ChatfuelText(String.format("合計: %d元", totalPrice)));

        return ResponseEntity.ok(new ChatfuelMessages(chatfuelTexts));
    }

    @PostMapping("/chatfuel/carts")
    public ResponseEntity<ChatfuelMessages> create(@RequestBody Carts carts) {
        List<ChatfuelText> chatfuelTexts = new ArrayList<>();
        cartsService.addCarts(carts);
        chatfuelTexts.add(new ChatfuelText(CARTS_ADD_SUCCESS));
        return ResponseEntity.ok(new ChatfuelMessages(chatfuelTexts));
    }

    @PostMapping("/chatfuel/carts/checkout")
    public ResponseEntity<ChatfuelMessages> checkout(@RequestBody ClearCartPostBody clearCartPostBody) {
        String messengerUserId = clearCartPostBody.getMessengerUserId();
        String firstName = clearCartPostBody.getFirstName();
        List<Carts> cartsList = cartsService.findByMessengerUserId(messengerUserId);

        List<ChatfuelText> chatfuelTexts = listCarts(cartsList);

        if (chatfuelTexts.isEmpty()) {
            chatfuelTexts.add(new ChatfuelText(CARTS_EMPTY));
            return ResponseEntity.ok(new ChatfuelMessages(chatfuelTexts));
        }

        int totalPrice = cartsList
                .stream()
                .mapToInt(carts -> carts.getQty() * carts.getPrice())
                .sum();

        chatfuelTexts.add(new ChatfuelText(String.format("合計 NT$: %d", totalPrice)));
        chatfuelTexts.add(new ChatfuelText(ORDER_FINISH));

        // save orders
        Orders orders = new Orders();
        orders.setMessengerUserId(messengerUserId);
        orders.setFirstName(firstName);
        orders.setDetail(chatfuelTexts.toString());
        orders.setTotalPrice(totalPrice);
        ordersService.addOrders(orders);

        // remove carts
        cartsService.deleteByMessengerUserId(clearCartPostBody.getMessengerUserId());

        return ResponseEntity.ok(new ChatfuelMessages(chatfuelTexts));
    }

    @PostMapping("/chatfuel/carts/clear")
    public ResponseEntity<ChatfuelMessages> clear(@RequestBody ClearCartPostBody clearCartPostBody) {
        List<ChatfuelText> chatfuelTexts = new ArrayList<>();
        cartsService.deleteByMessengerUserId(clearCartPostBody.getMessengerUserId());
        chatfuelTexts.add(new ChatfuelText(CARTS_CLEAR_SUCCESS));

        return ResponseEntity.ok(new ChatfuelMessages(chatfuelTexts));
    }
}

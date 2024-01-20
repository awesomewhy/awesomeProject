package com.dark.online.service.impl.order;

import com.dark.online.dto.product.CreateOrderForChatShowDto;
import com.dark.online.dto.product.CreateOrderForSellDto;
import com.dark.online.entity.Order;
import com.dark.online.entity.User;
import com.dark.online.exception.ErrorResponse;
import com.dark.online.mapper.OrderMapper;
import com.dark.online.repository.OrderRepository;
import com.dark.online.service.OrderService;
import com.dark.online.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final UserService userService;
    private final OrderMapper orderMapper;
    @Override
    public ResponseEntity<?> addItem(@RequestBody CreateOrderForSellDto createOrderForSellDto) {
        Optional<User> userOptional = userService.getAuthenticationPrincipalUserByNickname();

        if(userOptional.isEmpty()) {
            return ResponseEntity.ok().body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), "user in spring context not found / user not auth"));
        }
        User user = userOptional.get();
        Order order = orderMapper.mapCreateOrderForSellDtoToOrderEntity(createOrderForSellDto, user);

        orderRepository.save(order);

        return ResponseEntity.ok().body(new ErrorResponse(HttpStatus.OK.value(), "order added"));
    }

    @Override
    public ResponseEntity<?> createOrderForChat(@RequestBody CreateOrderForChatShowDto createOrderForChatShowDto) {
        Optional<User> userOptional = userService.getAuthenticationPrincipalUserByNickname();

        if(userOptional.isEmpty()) {
            return ResponseEntity.ok().body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), "user in spring context not found / user not auth"));
        }
        User user = userOptional.get();
        Order order = orderMapper.mapCreateOrderForChatShowDtoToEntity(createOrderForChatShowDto, user);

        orderRepository.save(order);

        return ResponseEntity.ok().body(new ErrorResponse(HttpStatus.OK.value(), "order added"));
    }
    @Override
    public ResponseEntity<?> getAllOrders() {
        return ResponseEntity.ok().body(orderRepository.findAll().stream()
                .map(orderMapper::mapOrderToOrderForShowDto));
    }

}

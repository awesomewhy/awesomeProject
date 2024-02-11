package com.dark.online.service.impl.order;

import com.dark.online.dto.order.CreateOrderForChatShowDto;
import com.dark.online.entity.Order;
import com.dark.online.entity.User;
import com.dark.online.exception.ErrorResponse;
import com.dark.online.mapper.OrderMapper;
import com.dark.online.mapper.ProductMapper;
import com.dark.online.repository.OrderRepository;
import com.dark.online.repository.UserRepository;
import com.dark.online.service.OrderService;
import com.dark.online.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final UserService userService;
    private final UserRepository userRepository;
    private final OrderMapper orderMapper;

    @Override
    public ResponseEntity<?> createOrderForChat(@PathVariable String nickname, @RequestBody CreateOrderForChatShowDto createOrderForChatShowDto) {
        Optional<User> userOptional = userService.getAuthenticationPrincipalUserByNickname();
        Optional<User> userOptional2 = userRepository.findByNickname(nickname);

        if(userOptional.isEmpty() || userOptional2.isEmpty()) {
            return ResponseEntity.ok().body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), "user not auth"));
        }
        User user = userOptional.get();
        User user2 = userOptional2.get();
        Order order = orderMapper.mapCreateOrderForChatShowDtoToEntity(user2, createOrderForChatShowDto, user);

        orderRepository.save(order);

        return ResponseEntity.ok().body(new ErrorResponse(HttpStatus.OK.value(), "order added"));
    }

}

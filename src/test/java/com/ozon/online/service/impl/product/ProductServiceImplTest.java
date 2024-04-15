package com.ozon.online.service.impl.product;

import com.ozon.online.dto.product.CategoryDto;
import com.ozon.online.dto.product.ProductForShowDto;
import com.ozon.online.dto.product.SortDto;
import com.ozon.online.entity.Product;
import com.ozon.online.entity.User;
import com.ozon.online.enums.OrderTypeEnum;
import com.ozon.online.enums.PaymentTypeEnum;
import com.ozon.online.mapper.ProductMapper;
import com.ozon.online.repository.ProductRepository;
import com.ozon.online.service.UserService;
import okhttp3.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {
    @Mock
    private UserService userService;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductServiceImpl productService;


    private User mockUser;
    private Product product;
    private CategoryDto categoryDto;
    private SortDto sortDto;

    @BeforeEach
    public void setUp() {
        productRepository = mock(ProductRepository.class);
        userService = mock(UserService.class);
        productMapper = mock(ProductMapper.class);
        productService = new ProductServiceImpl(userService, productRepository, productMapper);
    }

//    @BeforeEach
//    void setUp() {
//        mockUser = User.builder()
//                .id(UUID.randomUUID())
//                .username("testUsername")
//                .build();
//
//        product = Product.builder()
//                .id(1L)
//                .name("testNameProduct")
//                .orderType(OrderTypeEnum.CAR)
//                .price(BigDecimal.TEN)
//                .paymentType(PaymentTypeEnum.VISA)
//                .build();
//
//        categoryDto = new CategoryDto();
//        categoryDto.setOrderTypeEnum(OrderTypeEnum.CAR);
//
//        sortDto = new SortDto();
//        sortDto.setCategories(List.of(categoryDto));
//        sortDto.setStartPrice(BigDecimal.ZERO);
//        sortDto.setEndPrice(BigDecimal.TEN);
//        sortDto.setPaymentTypeEnum(PaymentTypeEnum.VISA);
//
//    }

    @Test
    void addProduct() {
    }

    @Test
    public void testSort() {
        SortDto sortDto = new SortDto();
        sortDto.setCategories(List.of(new CategoryDto()));
        sortDto.setPaymentTypeEnum(PaymentTypeEnum.VISA);
        sortDto.setStartPrice(new BigDecimal("100"));
        sortDto.setEndPrice(new BigDecimal("200"));

        List<Product> mockProducts = new ArrayList<>();
        mockProducts.add(new Product());

        when(productRepository.findAll()).thenReturn(mockProducts);
        when(userService.getAuthenticationPrincipalUserByNickname()).thenReturn(Optional.of(new User()));
        when(productMapper.mapProductToProductForShowDto(any())).thenReturn(new ProductForShowDto());

        ResponseEntity<?> responseEntity = productService.sort(sortDto);

        assertEquals(200, ((List<ProductForShowDto>) responseEntity.getBody()).size());
    }

    @Test
    void searchProduct() {
        when(userService.getAuthenticationPrincipalUserByNickname()).thenReturn(Optional.of(mockUser));
    }

    @Test
    void getAllProducts() {
    }

    @Test
    void getCorrectProduct() {
    }

    @Test
    void getMyProducts() {
    }
}
package com.order.process.system.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.order.process.system.demo.controller.OrdersController;
import com.order.process.system.demo.entity.Item;
import com.order.process.system.demo.entity.Order;
import com.order.process.system.demo.model.Status;
import com.order.process.system.demo.model.*;
import com.order.process.system.demo.service.OrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class OrdersControllerTest {

    private MockMvc mockMvc;

    @Mock
    private OrderServiceImpl orderService;

    @InjectMocks
    private OrdersController ordersController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(ordersController).build();
        objectMapper = new ObjectMapper();
    }

    // ==================== CREATE ORDER TESTS ====================

    @Test
    void createOrder_Success_ReturnsOk() throws Exception {
        // Arrange
        CreateOrderRequest request = new CreateOrderRequest();
        request.setCustomerId(123);
        List<ItemRequest> items = Arrays.asList(
                new ItemRequest(1L, 5),
                new ItemRequest(2L, 3)
        );
        request.setItems(items);

        Order order = new Order();
        order.setId(1L);
        order.setCustomerId(123);
        order.setStatus(Status.PENDING);

        OrderDto orderDto = new OrderDto(1L, 123, Status.PENDING, new ArrayList<>());
        OrderCreatedResponse successResponse = new OrderCreatedResponse(orderDto.getId(), Status.PENDING);

        when(orderService.createOrder(any(CreateOrderRequest.class))).thenReturn(successResponse);

        // Act & Assert
        mockMvc.perform(post("/order/createOrder")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").value(1))
                .andExpect(jsonPath("$.status").value("PENDING"));


        verify(orderService, times(1)).createOrder(any(CreateOrderRequest.class));
    }

    @Test
    void createOrder_InsufficientInventory_ReturnsConflict() throws Exception {
        // Arrange
        CreateOrderRequest request = new CreateOrderRequest();
        request.setCustomerId(123);
        request.setItems(Arrays.asList(new ItemRequest(1L, 1000)));

        HashMap<Item, String> insufficientItems = new HashMap<>();
        OrderErrorResponse errorResponse = new OrderErrorResponse(
                insufficientItems,
                "One or more items do not have enough stock"
        );

        when(orderService.createOrder(any(CreateOrderRequest.class))).thenReturn(errorResponse);

        // Act & Assert
        mockMvc.perform(post("/order/createOrder")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.errorMessage").value("One or more items do not have enough stock"));

        verify(orderService, times(1)).createOrder(any(CreateOrderRequest.class));
    }

    @Test
    void createOrder_WithUnitTest_Success() {
        // Arrange
        CreateOrderRequest request = new CreateOrderRequest();
        request.setCustomerId(456);
        request.setItems(Arrays.asList(new ItemRequest(1L, 2)));

        OrderDto orderDto = new OrderDto(2L, 456, Status.PENDING, new ArrayList<>());
        OrderCreatedResponse successResponse = new OrderCreatedResponse(orderDto.getId(), Status.PENDING);

        when(orderService.createOrder(any(CreateOrderRequest.class))).thenReturn(successResponse);

        // Act
        ResponseEntity<OrderResponse> response = ordersController.createOrder(request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof OrderCreatedResponse);
        OrderCreatedResponse createdResponse = (OrderCreatedResponse) response.getBody();
        assertEquals(2L, createdResponse.getOrderId());

        verify(orderService, times(1)).createOrder(any(CreateOrderRequest.class));
    }

    // ==================== GET ORDER STATUS TESTS ====================

    @Test
    void getOrderStatus_OrderExists_ReturnsOk() throws Exception {
        // Arrange
        Long orderId = 1L;
        OrderDto orderDto = new OrderDto(1L, 123, Status.PENDING, new ArrayList<>());
        OrderStatusResponse response = new OrderStatusResponse(orderDto, "Order found");

        when(orderService.findOrderById(orderId)).thenReturn(response);

        // Act & Assert
        mockMvc.perform(get("/order/get/OrderStatus/{id}", orderId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.order.id").value(1))
                .andExpect(jsonPath("$.order.customerId").value(123))
                .andExpect(jsonPath("$.order.status").value("PENDING"))
                .andExpect(jsonPath("$.message").value("Order found"));

        verify(orderService, times(1)).findOrderById(orderId);
    }

    @Test
    void getOrderStatus_OrderNotFound_ReturnsNotFound() throws Exception {
        // Arrange
        Long orderId = 999L;
        OrderStatusResponse response = new OrderStatusResponse(null, "No order with id: 999 was found.");

        when(orderService.findOrderById(orderId)).thenReturn(response);

        // Act & Assert
        mockMvc.perform(get("/order/get/OrderStatus/{id}", orderId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.order").doesNotExist())
                .andExpect(jsonPath("$.message").value("No order with id: 999 was found."));

        verify(orderService, times(1)).findOrderById(orderId);
    }

    @Test
    void getOrderStatus_WithUnitTest_Success() {
        // Arrange
        Long orderId = 5L;
        OrderDto orderDto = new OrderDto(5L, 789, Status.PENDING, new ArrayList<>());
        OrderStatusResponse mockResponse = new OrderStatusResponse(orderDto, "Order retrieved");

        when(orderService.findOrderById(orderId)).thenReturn(mockResponse);

        // Act
        ResponseEntity<OrderStatusResponse> response = ordersController.getOrderStatus(orderId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getOrder());
        assertEquals(5L, response.getBody().getOrder().getId());
        assertEquals(Status.PENDING, response.getBody().getOrder().getStatus());

        verify(orderService, times(1)).findOrderById(orderId);
    }

    @Test
    void getOrderStatus_WithUnitTest_NotFound() {
        // Arrange
        Long orderId = 999L;
        OrderStatusResponse mockResponse = new OrderStatusResponse(null, "Order not found");

        when(orderService.findOrderById(orderId)).thenReturn(mockResponse);

        // Act
        ResponseEntity<OrderStatusResponse> response = ordersController.getOrderStatus(orderId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNull(response.getBody().getOrder());

        verify(orderService, times(1)).findOrderById(orderId);
    }

    // ==================== UPDATE ORDER STATUS TESTS ====================

    @Test
    void updateOrderStatus_Success_ReturnsOk() throws Exception {
        // Arrange
        Long orderId = 1L;
        UpdateStatusResponse response = new UpdateStatusResponse(1, "Order status updated to PROCESSING");

        when(orderService.updateOrderStatus(orderId)).thenReturn(response);

        // Act & Assert
        mockMvc.perform(put("/order/updateStatus/{orderId}", orderId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1))
                .andExpect(jsonPath("$.message").value("Order status updated to PROCESSING"));

        verify(orderService, times(1)).updateOrderStatus(orderId);
    }

    @Test
    void updateOrderStatus_OrderNotFound_ReturnsNotFound() throws Exception {
        // Arrange
        Long orderId = 999L;
        UpdateStatusResponse response = new UpdateStatusResponse(2, "Order with id: 999 was not found");

        when(orderService.updateOrderStatus(orderId)).thenReturn(response);

        // Act & Assert
        mockMvc.perform(put("/order/updateStatus/{orderId}", orderId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(2))
                .andExpect(jsonPath("$.message").value("Order with id: 999 was not found"));

        verify(orderService, times(1)).updateOrderStatus(orderId);
    }

    @Test
    void updateOrderStatus_OrderAlreadyDelivered_ReturnsConflict() throws Exception {
        // Arrange
        Long orderId = 1L;
        UpdateStatusResponse response = new UpdateStatusResponse(
                3,
                "Order has already been completed for order with ID: 1"
        );

        when(orderService.updateOrderStatus(orderId)).thenReturn(response);

        // Act & Assert
        mockMvc.perform(put("/order/updateStatus/{orderId}", orderId))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value(3))
                .andExpect(jsonPath("$.message").value("Order has already been completed for order with ID: 1"));

        verify(orderService, times(1)).updateOrderStatus(orderId);
    }

    @Test
    void updateOrderStatus_WithUnitTest_Success() {
        // Arrange
        Long orderId = 3L;
        UpdateStatusResponse mockResponse = new UpdateStatusResponse(1, "Status updated to SHIPPED");

        when(orderService.updateOrderStatus(orderId)).thenReturn(mockResponse);

        // Act
        ResponseEntity<UpdateStatusResponse> response = ordersController.updateOrderStatus(orderId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getCode());
        assertEquals("Status updated to SHIPPED", response.getBody().getMessage());

        verify(orderService, times(1)).updateOrderStatus(orderId);
    }

    @Test
    void updateOrderStatus_WithUnitTest_NotFound() {
        // Arrange
        Long orderId = 888L;
        UpdateStatusResponse mockResponse = new UpdateStatusResponse(2, "Order not found");

        when(orderService.updateOrderStatus(orderId)).thenReturn(mockResponse);

        // Act
        ResponseEntity<UpdateStatusResponse> response = ordersController.updateOrderStatus(orderId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(2, response.getBody().getCode());

        verify(orderService, times(1)).updateOrderStatus(orderId);
    }

    @Test
    void updateOrderStatus_WithUnitTest_Conflict() {
        // Arrange
        Long orderId = 7L;
        UpdateStatusResponse mockResponse = new UpdateStatusResponse(3, "Already delivered");

        when(orderService.updateOrderStatus(orderId)).thenReturn(mockResponse);

        // Act
        ResponseEntity<UpdateStatusResponse> response = ordersController.updateOrderStatus(orderId);

        // Assert
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals(3, response.getBody().getCode());

        verify(orderService, times(1)).updateOrderStatus(orderId);
    }
}

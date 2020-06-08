package com.ewolff.microservice.order.logic;

import com.ewolff.microservice.order.clients.CatalogClient;
import com.ewolff.microservice.order.clients.CustomerClient;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * OrderServicde test class.
 *
 * @author chakrav
 * @since 2019-08-20
 */
public class OrderServiceTest {

  @InjectMocks
  private OrderService orderService;

  @Mock
  private OrderRepository orderRepository;
  @Mock
  private CustomerClient customerClient;
  @Mock
  private CatalogClient itemClient;

  @Mock
  private Order mockOrder;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);

    orderService = new OrderService(orderRepository, customerClient, itemClient);

  }

  @Test(expected = IllegalArgumentException.class)
  public void orderException() {
    when(orderRepository.findById(anyString())).thenReturn(Optional.of(dummyOrder(true)));
    orderService.order(dummyOrder(true));
  }

  @Test(expected = IllegalArgumentException.class)
  public void orderInvalidLine() {
    when(orderRepository.findById(anyString())).thenReturn(Optional.of(dummyOrder(true)));
    when(orderRepository.findById(anyString())).thenReturn(Optional.of(dummyOrder(false)));
    orderService.order(dummyOrder(true));
  }

  @Test
  public void order() {
    when(orderRepository.findById(anyString())).thenReturn(Optional.of(dummyOrder(true)));
    when(customerClient.isValidCustomerId(anyString())).thenReturn(true);

    orderService.order(dummyOrder(true));
    verify(orderRepository,  times(1)).save(any(Order.class));
  }

  @Test
  public void getPrice() {
    when(orderRepository.findById(anyString())).thenReturn(Optional.of(mockOrder));
    when(mockOrder.totalPrice(any(CatalogClient.class))).thenReturn(100.00);

    assertEquals(100.00, orderService.getPrice("1"), 0);
  }

  private Order dummyOrder(final boolean flag) {
    Order order =  new Order();
    order.setId("1");
    order.setCustomer("test customer");
    order.setCustomerId("testCustId");
    OrderLine orderLine = new OrderLine();
    orderLine.setCount(1);
    orderLine.setItemId("1");

    if (flag) {
      order.setOrderLine(Arrays.asList(orderLine));
    }

    return order;
  }
}
package com.ewolff.microservice.order.clients;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.anyObject;
import static org.mockito.Mockito.doReturn;

/**
 * CustomerClientTest.
 *
 * @author chakrav
 * @since 2019-08-20
 */
public class CustomerClientTest {
  private CustomerClient customerClient;

  @Mock
  private RestTemplate restTemplate;
  private String customerServiceHost = "host";
  private long customerServicePort = 8080;

  @Mock
  private PagedResources<Item> pagedResources;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);

    customerClient = new CustomerClient(customerServiceHost, customerServicePort);

    ReflectionTestUtils.setField(customerClient, "restTemplate", restTemplate);
  }

  //@Test
  public void isValidCustomerId() {
    ResponseEntity<String> responseEntity = ResponseEntity.ok("success");
    doReturn(responseEntity).when(restTemplate).getForEntity(anyString(), any());

    assertEquals(true, customerClient.isValidCustomerId("1"));
  }

  @Test
  public void getRestTemplate() {
    assertNotNull(customerClient.getRestTemplate());
  }

  @Test
  public void findAll() {
    Customer cust = new Customer();

    doReturn(pagedResources).when(restTemplate).getForObject(anyString(), any());
    doReturn(Arrays.asList(cust)).when(pagedResources).getContent();

    assertEquals(1, customerClient.findAll().size(), 0);
  }

}
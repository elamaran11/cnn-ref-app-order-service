package com.ewolff.microservice.order.clients;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.hateoas.PagedResources;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;

/**
 * CatalogClientTest.
 *
 * @author chakrav
 * @since 2019-08-20
 */
public class CatalogClientTest {
  private CatalogClient catalogClient;

  @Mock
  private RestTemplate restTemplate;
  private String catalogServiceHost = "host";
  private long catalogServicePort = 8080;

  @Mock
  private PagedResources<Item> pagedResources;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);

    catalogClient = new CatalogClient(catalogServiceHost, catalogServicePort);

    ReflectionTestUtils.setField(catalogClient, "restTemplate", restTemplate);
  }

  @Test
  public void getRestTemplate() {
    assertNotNull(catalogClient.getRestTemplate());
  }

  @Test
  public void price() {
    Item item = new Item();
    item.setPrice(200.00d);
    doReturn(item).when(restTemplate).getForObject(anyString(), any());

    assertEquals(200.00, catalogClient.price("1"), 0);
  }

  @Test
  public void findAll() {
    Item item = new Item();
    item.setPrice(200.00d);

    doReturn(pagedResources).when(restTemplate).getForObject(anyString(), any());
    doReturn(Arrays.asList(item)).when(pagedResources).getContent();

    assertEquals(1, catalogClient.findAll().size(), 0);
  }

}
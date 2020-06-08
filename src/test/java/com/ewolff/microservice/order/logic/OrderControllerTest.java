package com.ewolff.microservice.order.logic;

import com.ewolff.microservice.order.clients.CatalogClient;
import com.ewolff.microservice.order.clients.CustomerClient;
import com.ewolff.microservice.order.clients.Item;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.commons.lang.SerializationUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * Customer test class.
 *
 * @author chakrav
 * @since 2019-08-20
 */
@RunWith(SpringRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = SpringRestDataConfig.class, loader = AnnotationConfigContextLoader.class)
public class OrderControllerTest {
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderService orderService;
    @Mock
    private CustomerClient customerClient;
    @Mock
    private CatalogClient catalogClient;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(new OrderController(orderService, orderRepository, customerClient, catalogClient))
                .build();

        when(orderRepository.findById(anyString())).thenReturn(Optional.of(dummyOrder()));
        when(orderRepository.findAll()).thenReturn(Arrays.asList(dummyOrder()));
    }

    @Test
    public void testOrderList() throws Exception {
        mockMvc.perform(get("/")
                .accept(MediaType.TEXT_HTML_VALUE))
                .andExpect(status().isOk())
                .andExpect(view().name("orderlist"));
    }

    @Test
    public void testForm() throws Exception {
        mockMvc.perform(get("/form.html")
                .accept(MediaType.TEXT_HTML_VALUE))
                .andExpect(status().isOk())
                .andExpect(view().name("orderForm"));
    }

    @Test
    public void testOrderLine() throws Exception {
        final ObjectMapper mapper = new ObjectMapper();
        final String jsonContent = mapper.writeValueAsString(dummyOrder());

        Item item = new Item();
        item.setItemId("1");
        item.setName("testItem");
        item.setPrice(100);
        when(catalogClient.findAll()).thenReturn(Arrays.asList(item));

        mockMvc.perform(post("/line")
                .content(jsonContent)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(view().name("orderForm"));
    }

    @Test
    public void testFindById() throws Exception {
        mockMvc.perform(get("/1")
                .accept(MediaType.TEXT_HTML_VALUE))
                .andExpect(status().isOk())
                .andExpect(view().name("order"));
    }

    @Test
    public void testOrderPost() throws Exception {
        mockMvc.perform(post("/")
                .accept(MediaType.TEXT_HTML_VALUE))
                .andExpect(status().isOk())
                .andExpect(view().name("success"));
    }

    @Test
    public void testOrderDelete() throws Exception {
        mockMvc.perform(delete("/1")
                .accept(MediaType.TEXT_HTML_VALUE))
                .andExpect(status().isOk())
                .andExpect(view().name("success"));
    }

    private Order dummyOrder() {
        Order order =  new Order();
        order.setId("1");
        order.setCustomer("test customer");
        order.setCustomerId("testCustId");
        OrderLine orderLine = new OrderLine();
        orderLine.setCount(1);
        orderLine.setItemId("1");
        order.setOrderLine(Arrays.asList(orderLine));

        return order;
    }

}

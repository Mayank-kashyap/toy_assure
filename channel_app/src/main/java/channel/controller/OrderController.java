package channel.controller;

import channel.dto.OrderDto;
import channel.util.ApiException;
import common.model.OrderData;
import common.model.OrderItemData;
import common.model.OrderItemsForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api
public class OrderController extends ExceptionHandler{
    @Autowired
    private OrderDto orderDto;

    @ApiOperation(value = "Creates an order")
    @RequestMapping(path = "/api/order/{channelName}/{clientName}/{customerName}/{channelOrderId}", method = RequestMethod.POST)
    public void add(@PathVariable String channelName,@PathVariable String clientName,@PathVariable String customerName,@PathVariable String channelOrderId,@RequestBody List<OrderItemsForm> orderItemsForm) throws ApiException {
        orderDto.add(channelName,clientName,customerName,channelOrderId,orderItemsForm);
    }

    @ApiOperation("Get all orders")
    @RequestMapping(path = "api/order",method = RequestMethod.GET)
    public List<OrderData> get() throws ApiException {
        return orderDto.get();
    }

    @ApiOperation("Get single order")
    @RequestMapping(path = "api/order/{id}",method = RequestMethod.GET)
    public OrderData getOrder(@PathVariable Long id) throws ApiException {
        return orderDto.getOrder(id);
    }

    @ApiOperation("Get order items for an order")
    @RequestMapping(path = "api/orderItem/{orderId}",method = RequestMethod.GET)
    public List<OrderItemData> getOrderItems(@PathVariable Long orderId) throws ApiException {
        return orderDto.get(orderId);
    }
}

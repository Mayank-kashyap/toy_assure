package assure.controller;

import assure.dto.OrderDto;
import assure.service.ApiException;
import common.model.OrderData;
import common.model.OrderItemData;
import common.model.OrderItemForm;
import common.model.OrderItemsForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api
public class OrderController extends ExceptionHandler {

    @Autowired
    private OrderDto orderDto;

    @ApiOperation("Create an order")
    @RequestMapping(path = "api/order/{clientName}/{channelOrderId}/{customerName}",method = RequestMethod.POST)
    public void add(@PathVariable String clientName, @PathVariable String channelOrderId, @PathVariable String customerName, @RequestBody OrderItemForm orderItemForm) throws ApiException {
        orderDto.add(clientName,channelOrderId,customerName, orderItemForm);
    }

    @ApiOperation("Create an order")
    @RequestMapping(path = "api/order/list/{clientName}/{channelOrderId}/{customerName}",method = RequestMethod.POST)
    public void add(@PathVariable String clientName, @PathVariable String channelOrderId, @PathVariable String customerName, @RequestBody List<OrderItemForm> orderItemFormList) throws ApiException {
        orderDto.add(clientName, channelOrderId, customerName, orderItemFormList);
    }

    @ApiOperation(value = "Creates an order")
    @RequestMapping(path = "/api/order/{channelName}/{clientName}/{customerName}/{channelOrderId}", method = RequestMethod.POST)
    public void add(@PathVariable String channelName,@PathVariable String clientName,@PathVariable String customerName,@PathVariable String channelOrderId,@RequestBody List<OrderItemsForm> orderItemsForm) throws ApiException {
        orderDto.add(channelName,clientName,customerName,channelOrderId,orderItemsForm);
    }

    @ApiOperation("Get order details")
    @RequestMapping(path = "api/order/{id}",method = RequestMethod.GET)
    public OrderData get(@PathVariable Long id) throws ApiException {
        return orderDto.get(id);
    }

    @ApiOperation("Get all orders")
    @RequestMapping(path = "api/order",method = RequestMethod.GET)
    public List<OrderData> get() throws ApiException {
        return orderDto.get();
    }

    @ApiOperation("Get order items for an order")
    @RequestMapping(path = "api/orderItem/{orderId}",method = RequestMethod.GET)
    public List<OrderItemData> getOrderItems(@PathVariable Long orderId) throws ApiException {
        return orderDto.getOrderItems(orderId);
    }

    @ApiOperation("Allocate order")
    @RequestMapping(path = "api/order/update/{id}",method = RequestMethod.PUT)
    public void allocate(@PathVariable Long id) throws ApiException {
        orderDto.allocate(id);
    }
}

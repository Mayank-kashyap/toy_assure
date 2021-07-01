package channel.dto;

import channel.assure.AssureClient;
import channel.util.ApiException;
import channel.util.StringUtil;
import common.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderDto {
    @Autowired
    private AssureClient assureClient;

    @Transactional(rollbackFor = ApiException.class)
    public void add(String channelName,String clientName,String customerName,String channelOrderId,List<OrderItemsForm> orderItemsFormList) throws ApiException {
        for (OrderItemsForm orderItemsForm:orderItemsFormList){
            normalize(orderItemsForm);
            check(orderItemsForm);
        }
        assureClient.add(channelName,clientName,customerName,channelOrderId,orderItemsFormList);
    }

    @Transactional(rollbackFor = ApiException.class)
    public List<OrderData> get() throws ApiException {
        return assureClient.getOrderList();
    }

    @Transactional(rollbackFor = ApiException.class)
    public OrderData getOrder(Long id) throws ApiException {
        return assureClient.getOrder(id);
    }

    @Transactional(rollbackFor = ApiException.class)
    public List<OrderItemData> get(Long orderId) throws ApiException {
        return assureClient.get(orderId);
    }

    private void check(OrderItemsForm orderItemsForm) throws ApiException {
        if (StringUtil.isEmpty(orderItemsForm.getChannelSkuId()))
            throw new ApiException("Channel skuId cannot be empty");
        if (orderItemsForm.getQuantity()==null)
            throw new ApiException("Channel skuId cannot be empty");
        if (orderItemsForm.getSellingPricePerUnit()==null)
            throw new ApiException("Selling price cannot be empty");
        if (orderItemsForm.getQuantity()<=0)
            throw new ApiException("Quantity should be positive");
        if (orderItemsForm.getSellingPricePerUnit()<=0)
            throw new ApiException("Selling price should be positive");
    }

    public void normalize(OrderItemsForm orderItemsForm){
        orderItemsForm.setChannelSkuId(StringUtil.trim(orderItemsForm.getChannelSkuId()));
    }

}

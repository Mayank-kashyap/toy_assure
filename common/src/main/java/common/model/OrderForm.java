package common.model;

import lombok.Data;

import java.util.List;

@Data
public class OrderForm {
    private String channelName;
    private Long clientId;
    private Long customerId;
    private String channelOrderId;
    private List<OrderItemsForm> orderItemsFormList;
}

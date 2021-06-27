package common.model;

import lombok.Data;

@Data
public class OrderData {
    private Long id;
    private String clientName;
    private String customerName;
    private String channelName;
    private String channelOrderId;
    private Status status;
}

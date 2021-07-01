package common.model;

import lombok.Data;

@Data
public class OrderItemsForm {
    private String channelSkuId;
    private Long quantity;
    private Double sellingPricePerUnit;
}

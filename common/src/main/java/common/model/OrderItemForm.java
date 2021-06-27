package common.model;

import lombok.Data;

@Data
public class OrderItemForm {
    private String clientSkuId;
    private Long orderedQuantity;
    private Double sellingPricePerUnit;
}

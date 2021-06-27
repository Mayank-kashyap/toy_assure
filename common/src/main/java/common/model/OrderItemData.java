package common.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class OrderItemData extends OrderItemForm {
    private Long id;
    private Long orderId;
}

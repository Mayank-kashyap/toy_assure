package common.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ProductData extends ProductForm{
    private Long id;
    private Long clientId;
}

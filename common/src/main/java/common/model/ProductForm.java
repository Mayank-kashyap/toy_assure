package common.model;

import lombok.Data;

@Data
public class ProductForm {
    private String clientSkuId;
    private String name;
    private String brandId;
    private Double mrp;
    private String description;
}

package common.model;

import lombok.Data;

@Data
public class BinSkuForm {
    private Long binId;
    private String clientSkuId;
    private Long quantity;
}

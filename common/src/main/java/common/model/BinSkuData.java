package common.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BinSkuData extends BinSkuForm{
    private Long id;
}

package assure.pojo;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"globalSkuId"})})
public class InventoryPojo {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private Long globalSkuId;
    private Long availableQuantity;
    private Long allocatedQuantity;
    private Long fulfilledQuantity;
}

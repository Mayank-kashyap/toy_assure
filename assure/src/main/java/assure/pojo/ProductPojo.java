package assure.pojo;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"clientSkuId","clientId"}),@UniqueConstraint(columnNames = {"clientSkuId"})})
public class ProductPojo {
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE,generator = "globalSkuIdSequence")
    @SequenceGenerator(name = "globalSkuIdSequence",initialValue = 100000, allocationSize = 1, sequenceName = "globalSkuId")
    private Long id;
    private String clientSkuId;
    private Long clientId;
    private String name;
    private String brandId;
    private Double mrp;
    private String description;
}

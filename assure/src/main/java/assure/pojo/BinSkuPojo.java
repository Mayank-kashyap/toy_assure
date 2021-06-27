package assure.pojo;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"binId","globalSkuId"})})
public class BinSkuPojo {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private Long binId;
    private Long globalSkuId;
    private Long quantity;
}

package assure.pojo;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"channelSkuId"})})
public class ChannelListingPojo {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private Long channelId;
    private String channelSkuId;
    private Long clientId;
    private Long globalSkuId;
}

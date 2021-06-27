package assure.pojo;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class BinPojo {
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE,generator = "binIdSequence")
    @SequenceGenerator(name = "binIdSequence",initialValue = 10000, allocationSize = 1, sequenceName = "binId")
    private Long id;
}

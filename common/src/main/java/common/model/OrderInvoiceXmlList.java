package common.model;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@Data
@XmlRootElement(name = "items")
@XmlAccessorType(XmlAccessType.FIELD)
public class OrderInvoiceXmlList {
    @XmlElement(name="channelOrderId")
    private String channelOrderId;
    @XmlElement(name="total")
    private Double total;
    @XmlElement(name="item")
    private List<OrderInvoiceData> orderInvoiceData;
}

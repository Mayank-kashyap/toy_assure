package assure.service;

import assure.pojo.OrderItemPojo;
import assure.pojo.OrderPojo;
import assure.pojo.ProductPojo;
import assure.util.ConversionUtil;
import assure.util.PdfConversionUtil;
import common.model.OrderInvoiceData;
import common.model.OrderInvoiceXmlList;
import common.model.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.util.List;
import java.util.Map;

@Service
public class InvoiceService {

    @Autowired
    private OrderService orderService;

    public byte[] generatePdfResponse(Long id) throws Exception {
        OrderInvoiceXmlList orderInvoiceXmlList = generateInvoiceList(id);
        PdfConversionUtil.generateXml(new File("invoice.xml"), orderInvoiceXmlList, OrderInvoiceXmlList.class);
        return PdfConversionUtil.generatePDF(new File("invoice.xml"), new StreamSource("invoice.xsl"));
    }

    public OrderInvoiceXmlList generateInvoiceList(Long orderId) throws Exception {
        List<OrderItemPojo> orderItemPojoList = orderService.getOrderItems(orderId);
        Map<OrderItemPojo, ProductPojo> productPojoList=orderService.getProductPojoFromOrderItem(orderItemPojoList);
        OrderInvoiceXmlList orderInvoiceXmlList = ConversionUtil.convertToInvoiceDataList(orderItemPojoList,productPojoList);

        double total = calculateTotal(orderInvoiceXmlList);
        orderInvoiceXmlList.setTotal(total);
        OrderPojo orderPojo=orderService.get(orderId);
        if (!orderPojo.getStatus().equals(Status.FULFILLED))
        {
            orderPojo.setStatus(Status.FULFILLED);
            orderService.fulfill(orderId,orderPojo);
        }
        orderInvoiceXmlList.setChannelOrderId(orderPojo.getChannelOrderId());
        return orderInvoiceXmlList;
    }

    private static double calculateTotal(OrderInvoiceXmlList orderInvoiceXmlList) {
        double total = 0;
        for (OrderInvoiceData orderInvoiceData : orderInvoiceXmlList.getOrderInvoiceData()) {
            total += (orderInvoiceData.getSellingPricePerUnit() * orderInvoiceData.getQuantity());
        }
        return total;
    }
}

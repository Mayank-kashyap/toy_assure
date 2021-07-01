package channel.dto;

import channel.assure.AssureClient;
import channel.util.ApiException;
import channel.util.PdfConversionUtil;
import common.model.OrderInvoiceXmlList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.stream.StreamSource;
import java.io.File;

@Service
public class InvoiceDto {
    @Autowired
    private AssureClient assureClient;

    public void get(Long id, HttpServletResponse response) throws ApiException {
        System.out.println("channel dto "+id);
        assureClient.getInvoice(id,response);
    }

    public byte[] generatePdfResponse(Long id) throws Exception {
        OrderInvoiceXmlList orderInvoiceXmlList = assureClient.generateInvoiceList(id);
        PdfConversionUtil.generateXml(new File("invoice.xml"), orderInvoiceXmlList, OrderInvoiceXmlList.class);
        return PdfConversionUtil.generatePDF(new File("invoice.xml"), new StreamSource("invoice.xsl"));
    }
}

package assure.controller;

import assure.service.InvoiceService;
import common.model.OrderInvoiceXmlList;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Api
@RestController
public class InvoiceController extends ExceptionHandler{

    @Autowired
    private InvoiceService invoiceService;

    //Retrieves the invoice of an order from orderId
    @ApiOperation(value = "Gets Invoice PDF by id")
    @RequestMapping(path = "/api/invoice/{id}", method = RequestMethod.GET)
    public void get(@PathVariable Long id, HttpServletResponse response) throws Exception {
        System.out.println("assure controller "+id);
        byte[] bytes = invoiceService.generatePdfResponse(id);
        createPdfResponse(bytes, response);
    }

    @ApiOperation(value = "Gets Invoice PDF by id")
    @RequestMapping(path = "/api/generateInvoice/{id}", method = RequestMethod.GET)
    public OrderInvoiceXmlList get(@PathVariable Long id) throws Exception {
        return invoiceService.generateInvoiceList(id);
    }

    //Creates PDF
    public void createPdfResponse(byte[] bytes, HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        response.setContentLength(bytes.length);
        response.getOutputStream().write(bytes);
        response.getOutputStream().flush();
    }
}
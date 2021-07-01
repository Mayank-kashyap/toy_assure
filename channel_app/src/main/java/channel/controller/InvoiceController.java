package channel.controller;

import channel.dto.InvoiceDto;
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
    private InvoiceDto invoiceDto;

    @ApiOperation(value = "Gets Invoice PDF by id")
    @RequestMapping(path = "/api/channel/invoice/{id}", method = RequestMethod.GET)
    public void get(@PathVariable Long id, HttpServletResponse response) throws Exception {
        System.out.println("channel controller "+id);
        byte[] bytes = invoiceDto.generatePdfResponse(id);
        createPdfResponse(bytes, response);
    }

    public void createPdfResponse(byte[] bytes, HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        response.setContentLength(bytes.length);
        response.getOutputStream().write(bytes);
        response.getOutputStream().flush();
    }
}

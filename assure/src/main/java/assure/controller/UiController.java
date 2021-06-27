package assure.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
class UiController extends AbstractUiController{

    @Value("${app.baseUrl}")
    private String baseUrl;

    @RequestMapping(value = "")
    public ModelAndView index() {
        return mav("home.html");
    }

    @RequestMapping(value = "/site/user")
    public ModelAndView features() {
        return mav("user.html");
    }

    @RequestMapping(value = "/site/product")
    public ModelAndView products() {
        return mav("product.html");
    }

    @RequestMapping(value = "/site/bin")
    public ModelAndView bin() {
        return mav("bin.html");
    }

    @RequestMapping(value = "/site/order")
    public ModelAndView orders() {
        return mav("order.html");
    }
}

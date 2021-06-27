package channel.controller;

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
        System.out.println(baseUrl);
        return mav("home.html");
    }

    @RequestMapping(value = "/site/channel")
    public ModelAndView channels() {
        return mav("channels.html");
    }
    }

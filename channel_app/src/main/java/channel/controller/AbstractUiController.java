package channel.controller;

import common.model.InfoData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public abstract class AbstractUiController {
    @Value("${app.baseUrl}")
    private String baseUrl;

    protected ModelAndView mav(String page) {
        System.out.println("abstract: "+baseUrl);
        // Set info
        ModelAndView mav = new ModelAndView(page);
        mav.addObject("info", new InfoData());
        mav.addObject("baseUrl", baseUrl);
        return mav;
    }
}
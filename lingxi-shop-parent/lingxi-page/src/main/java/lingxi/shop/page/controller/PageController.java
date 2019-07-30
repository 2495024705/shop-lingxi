package lingxi.shop.page.controller;

import lingxi.shop.page.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;


@Controller
public class PageController {
    @Autowired
    private PageService pageService;

    @RequestMapping(value = "item/{id}.html", method = RequestMethod.GET)
    public String toItem(@PathVariable("id") Long id, Model model) {
        Map<String, Object> attributes = pageService.LoadModel(id);
        model.addAllAttributes(attributes);
        return "item";
    }

}
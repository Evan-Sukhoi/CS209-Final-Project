package cse.java2.project.controller;

import cse.java2.project.service.Services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;



@Controller
public class DemoController {
    private final Services Services;

    @Autowired
    public DemoController(Services Services) {
        this.Services = Services;
    }

    /**
     * This method is called when the user requests the root URL ("/") or "/demo".
     * In this demo, you can visit localhost:9090 or localhost:9090/demo to see the result.
     * @return the name of the view to be rendered
     * You can find the static HTML file in src/main/resources/templates/home.html
     */
    @GetMapping({"/","/Homepage"})
    public String login(Model model) {
        return "Homepage";
    }
    @GetMapping({ "/AnswersPage"})
    public String answers(Model model) {
        model.addAttribute("noAnswersPer", Services.getNoAnswersPercentage());
        model.addAttribute("avg", Services.getAnswerCountAverage());
        model.addAttribute("max", Services.getAnswerCountMax());
        model.addAttribute("acceptedAnswersPer", Services.getHavingAcceptedAnswerPercentage());
        model.addAttribute("answerNotWillPer", Services.getNotWillPercentage());
        return "AnswersPage";
    }
    @GetMapping({ "/TagsPage"})
    public String tags(Model model) {
        model.addAttribute("mostRelateJava", Services.getMostRelatedToJavaTop());
        model.addAttribute("mostQuvotes", Services.getBestTop());
        model.addAttribute("mostViews", Services.getMostFashionTop());
        return "TagsPage";
    }
    @GetMapping({ "/UsersPage"})
    public String users(Model model) {
        model.addAttribute("mostActiveUser", Services.getMostActiveUsersTop());
        return "UsersPage";
    }
    @GetMapping({"/ApisPage"})
    public String apis(Model model) {
        model.addAttribute("mostHotApi", Services.getMostHotApiTop());
        return "ApisPage";
    }

    @GetMapping({"/RESTfulAPI"})
    public String restful(Model model) {
        return "RESTfulAPI";
    }

    @GetMapping({"/demo"})
    public String demo(Model model) {
        return "demo";
    }
}

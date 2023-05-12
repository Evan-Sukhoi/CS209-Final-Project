package cse.java2.project.controller;

import cse.java2.project.model.Questions;
import cse.java2.project.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.List;


@Controller
public class DemoController {
    private final QuestionService questionService;

    static String[] colors = new String[]{"#4e73df", "#1cc88a", "#36b9cc","#c51e1e","#ddda1e","#dd8a18","#1fd632","#d023d3","#e2d0d0","#aeff00","#7e29c8","#007fff","#eb1592","#00f91a","#ffb700"};
    static String[] hoverColors = new String[]{"#2e59d9", "#17a673", "#2c9faf", "#971818", "#c1bf1e", "#bd7b20", "#24b233", "#ad23af", "#c4a9a9", "#9ecf34", "#5c207e", "#166dc4", "#ca2184", "#1dc12e", "#ce9d20"};
    @Autowired
    public DemoController(QuestionService questionService) {
        this.questionService = questionService;
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
        //The percentage of questions that has no answers
        model.addAttribute("noAnswersPer", questionService.getNoAnswersPercentage());
        //The average and maximum number of answers
        model.addAttribute("avg", questionService.getAnswerCountAverage());
        model.addAttribute("max", questionService.getAnswerCountMax());
        //The percentage of having an accepted answer
        model.addAttribute("acceptedAnswersPer", questionService.getHavingAcceptedAnswerPercentage());
        //The percentage of questions have non-accepted answers that have received more upvotes than the accepted answers
        model.addAttribute("answerNotWillPer", questionService.getNotWillPercentage());
        return "AnswersPage";
    }
    @GetMapping({ "/TagsPage"})
    public String tags(Model model) {
        model.addAttribute("mostRelateJava", questionService.getMostRelatedToJavaTop());
        model.addAttribute("mostQuvotes", questionService.getBestTop());
        model.addAttribute("mostViews", questionService.getMostFashionTop());
        return "TagsPage";
    }
    @GetMapping({ "/UsersPage"})
    public String users() {
        return "UsersPage";
    }
    @GetMapping({"/ApisPage"})
    public String apis(Model model) {
        model.addAttribute("mostHotApi", questionService.getMostHotApiTop());
        return "ApisPage";
    }
}

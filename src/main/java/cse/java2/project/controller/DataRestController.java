package cse.java2.project.controller;

import cse.java2.project.model.TagsJavaRelated;
import cse.java2.project.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class DataRestController {

    private final QuestionService questionService;
    @Autowired
    public DataRestController(QuestionService service) {
        this.questionService = service;
    }
    @GetMapping("/AnswersPage/distribution1")
    public Map<String, Long> getAnswersDistribution() {
        return questionService.getAnswersDistribution();
    }
    @GetMapping("/AnswersPage/distribution2")
    public Map<String, Long> getResolutionDurationDistribution() {
        return questionService.getResolutionDurationDistribution();
    }
    @GetMapping("/AnswersPage/Percentage1")
    public Map<String, Long> getNoAnswersPercentage() {
        return questionService.getNoAnswers();
    }
    @GetMapping("/AnswersPage/Percentage2")
    public Map<String, Long> getHavingAcceptedAnswersPercentage() {
        return questionService.getHavingAcceptedAnswers();
    }
    @GetMapping("/AnswersPage/Percentage3")
    public Map<String, Long> getHavingNotWillAcceptedAnswersPercentage() {
        return questionService.getNotWill();
    }
    @GetMapping("/TagsPage/chart1")
    public Map<String, Long> getMostRelatedToJava() {
        return questionService.getMostRelatedToJava();
    }
    @GetMapping("/TagsPage/chart2")
    public Map<String, Long> getMostUpvotes() {
        return questionService.getBest();
    }
    @GetMapping("/TagsPage/chart3")
    public Map<String, Long> getMostViews() {
        return questionService.getMostFashion();
    }
    @GetMapping("/ApisPage/chart1")
    public Map<String, Long> getMostHotApi() {
        return questionService.getMostHotApi();
    }
}

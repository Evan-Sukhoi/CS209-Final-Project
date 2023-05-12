package cse.java2.project.controller;

import cse.java2.project.model.TagsJavaRelated;
import cse.java2.project.service.Services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class DataRestController {

    private final Services Services;
    @Autowired
    public DataRestController(Services service) {
        this.Services = service;
    }
    @GetMapping("/AnswersPage/distribution1")
    public Map<String, Long> getAnswersDistribution() {
        return Services.getAnswersDistribution();
    }
    @GetMapping("/AnswersPage/distribution2")
    public Map<String, Long> getResolutionDurationDistribution() {
        return Services.getResolutionDurationDistribution();
    }
    @GetMapping("/AnswersPage/Percentage1")
    public Map<String, Long> getNoAnswersPercentage() {
        return Services.getNoAnswers();
    }
    @GetMapping("/AnswersPage/Percentage2")
    public Map<String, Long> getHavingAcceptedAnswersPercentage() {
        return Services.getHavingAcceptedAnswers();
    }
    @GetMapping("/AnswersPage/Percentage3")
    public Map<String, Long> getHavingNotWillAcceptedAnswersPercentage() {
        return Services.getNotWill();
    }
    @GetMapping("/TagsPage/chart1")
    public Map<String, Long> getMostRelatedToJava() {
        return Services.getMostRelatedToJava();
    }
    @GetMapping("/TagsPage/chart2")
    public Map<String, Long> getMostUpvotes() {
        return Services.getBest();
    }
    @GetMapping("/TagsPage/chart3")
    public Map<String, Long> getMostViews() {
        return Services.getMostFashion();
    }
    @GetMapping("/ApisPage/chart1")
    public Map<String, Long> getMostHotApi() {
        return Services.getMostHotApi();
    }
}

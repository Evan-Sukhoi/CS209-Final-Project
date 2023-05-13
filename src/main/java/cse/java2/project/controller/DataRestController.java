package cse.java2.project.controller;

import cse.java2.project.model.Answers;
import cse.java2.project.model.Comments;
import cse.java2.project.model.Questions;
import cse.java2.project.model.TagsJavaRelated;
import cse.java2.project.service.Services;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


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

  @GetMapping("/UsersPage/chart1")
  public Map<String, Long> getUsersDistribution() {
    return Services.getUsersDistribution();
  }

  @GetMapping("/UsersPage/chart2")
  public Map<String, Long> getUsersDistributionAnswer() {
    return Services.getUsersDistributionAnswer();
  }

  @GetMapping("/UsersPage/chart3")
  public Map<String, Long> getUsersDistributionComment() {
    return Services.getUsersDistributionComment();
  }

  @GetMapping("/UsersPage/chart4")
  public Map<String, Long> getMostActiveUser() {
    return Services.getMostActiveUsers();
  }

  @GetMapping("/ApisPage/chart1")
  public Map<String, Long> getMostHotApi() {
    return Services.getMostHotApi();
  }

  /**
   * The following methods are for RESTful API
   */


  @GetMapping("/java/answers")
  public List<Answers> getAllAnswers(@RequestParam(required = false) Integer id) {
    if (id == null) {
      return Services.getAllAnswers();
    } else {
      return Services.getAnswersByQuestion_id(id);
    }
  }


  @GetMapping("/java/answers/accepted")
  public List<Answers> getAllAcceptedAnswers(@RequestParam(required = false) Integer id) {
    if (id == null) {
      return Services.getAllAcceptedAnswers();
    } else {
      return Services.getAcceptedAnswersByQuestionId(id);
    }
  }

  @GetMapping("/java/comments")
  public List<Comments> getAllComments(@RequestParam(required = false) Integer id) {
    if (id == null) {
      return Services.getAllComments();
    } else {
      return Services.getCommentsByQuestion_id(id);
    }
  }

  @GetMapping("/java/questions")
  public List<Questions> getAllQuestions(@RequestParam(required = false) Integer id) {
    if (id == null) {
      return Services.getAllQuestions();
    } else {
      return Services.getQuestionsByQuestion_id(id);
    }
  }

  @GetMapping("/java/tags")
  public List<TagsJavaRelated> getAllTagsJavaRelated() {
    return Services.getAllTagsJavaRelated();
  }

}

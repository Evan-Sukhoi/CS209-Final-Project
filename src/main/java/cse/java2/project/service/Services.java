package cse.java2.project.service;

import cse.java2.project.model.Answers;
import cse.java2.project.model.Comments;
import cse.java2.project.model.HotApi;
import cse.java2.project.model.Questions;
import cse.java2.project.model.Tags;
import cse.java2.project.model.TagsJavaRelated;
import cse.java2.project.model.Users;
import cse.java2.project.repository.Repositories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Evan
 */
@Service
public class Services {
  private final Repositories Repositories;

  @Autowired
  public Services(Repositories Repositories) {
    this.Repositories = Repositories;
  }

  public Repositories getRepositories() {
    return Repositories;
  }


  public Map<String, Long> getAnswersDistribution() {
    Map<String, Long> map = new HashMap<>();
    map.put("0-9", getRepositories().findByAnswer_countRange(0, 9));
    map.put("10-19", getRepositories().findByAnswer_countRange(10, 19));
    map.put("20-29", getRepositories().findByAnswer_countRange(20, 29));
    map.put("30-39", getRepositories().findByAnswer_countRange(30, 39));
    map.put("40-49", getRepositories().findByAnswer_countRange(40, 49));
    map.put("50-59", getRepositories().findByAnswer_countRange(50, 59));
    map.put(">=60", getRepositories().findByAnswer_countRange(60));
    return map;
  }
  public double getAnswerCountAverage() {
    return getRepositories().findByAnswer_countAverage();
  }
  public double getAnswerCountMax() {
    return getRepositories().findByAnswer_countMax();
  }
  public String getNoAnswersPercentage() {
    NumberFormat percentFormat = NumberFormat.getPercentInstance();
    percentFormat.setMaximumFractionDigits(4);
    return percentFormat.format((double)
            getRepositories().findByAnswer_countRange(0, 0) / getRepositories().findTheQuestionsSize());
  }
  public Map<String, Long> getNoAnswers() {
    Map<String, Long> map = new HashMap<>();
    Long total = getRepositories().findTheQuestionsSize();
    Long a = getRepositories().findByAnswer_countRange(0, 0);
    map.put("Having", total - a);
    map.put("Not having", a);
    return map;
  }
  public String getHavingAcceptedAnswerPercentage() {
    NumberFormat percentFormat = NumberFormat.getPercentInstance();
    percentFormat.setMaximumFractionDigits(4);
    return percentFormat.format( (double)
            getRepositories().findByHavingAcceptedAnswers() / getRepositories().findTheQuestionsSize());
  }
  public Map<String, Long> getHavingAcceptedAnswers() {
    Map<String, Long> map = new HashMap<>();
    Long total = getRepositories().findTheQuestionsSize();
    Long a = getRepositories().findByHavingAcceptedAnswers();
    map.put("Having", a);
    map.put("Not having", total - a);
    return map;
  }
  public Map<String, Long> getResolutionDurationDistribution() {
    Map<String, Long> map = new HashMap<>();
    map.put("within 1 hours", getRepositories().findByResolutionTimeRange(0, 1));
    map.put("1-3 hours", getRepositories().findByResolutionTimeRange(1, 3));
    map.put("3-6 hours", getRepositories().findByResolutionTimeRange(3, 6));
    map.put("6-12 hours", getRepositories().findByResolutionTimeRange(6, 12));
    map.put("12-24 hours", getRepositories().findByResolutionTimeRange(12, 24));
    map.put("above 24 hours", getRepositories().findByResolutionTimeRange(24));
    map.put("Not solved", getRepositories().findTheQuestionsSize() - getRepositories().findByHavingAcceptedAnswers());
    return map;
  }
  public String getNotWillPercentage() {
    NumberFormat percentFormat = NumberFormat.getPercentInstance();
    percentFormat.setMaximumFractionDigits(4);
    return percentFormat.format( (double)
            getRepositories().findByNot_public_will() / getRepositories().findTheQuestionsSize());
  }

  public Map<String, Long> getNotWill() {
    Map<String, Long> map = new HashMap<>();
    Long total = getRepositories().findTheQuestionsSize();
    Long a = getRepositories().findByNot_public_will();
    map.put("Having", a);
    map.put("Not having", total - a);
    return map;
  }

  public Map<String, Long> getMostRelatedToJava() {
    List<TagsJavaRelated> a = getRepositories().findByCount();
    Map<String, Long> map = new HashMap<>();
    for (int i = 0; i < 50; i++) {
      map.put(a.get(i).name, (long) a.get(i).count);
    }
    return map;
  }
  public String getMostRelatedToJavaTop() {
    return getRepositories().findByCount().get(0).name;
  }
  public Map<String, Long> getBest() {
    List<Tags> a = getRepositories().findByUpvotes();
    Map<String, Long> map = new HashMap<>();
    for (int i = 0; i < 14; i++) {
      map.put(a.get(i).name, (long) a.get(i).score);
    }
    return map;
  }
  public String getBestTop() {
    return "{" + getRepositories().findByUpvotes().get(0).name + "}";
  }

  public Map<String, Long> getMostFashion() {
    List<Tags> a = getRepositories().findByViews();
    Map<String, Long> map = new HashMap<>();
    for (int i = 0; i < 14; i++) {
      map.put(a.get(i).name, (long) a.get(i).view_count);
    }
    return map;
  }
  public String getMostFashionTop() {
    return "{" + getRepositories().findByViews().get(0).name + "}";
  }
  public Map<String, Long> getMostActiveUsers() {
    List<Users> a = getRepositories().findByUsersCount();
    Map<String, Long> map = new HashMap<>();
    for (int i = 0; i < 14; i++) {
      map.put(a.get(i).display_name, (long) a.get(i).count);
    }
    return map;
  }
  public String getMostActiveUsersTop() {
    return getRepositories().findByUsersCount().get(0).display_name;
  }
  public Map<String, Long> getUsersDistribution() {
    Map<String, Long> map = new HashMap<>();
    map.put("0-9", getRepositories().findByQuestionUsersCount(0, 9));
    map.put("10-19", getRepositories().findByQuestionUsersCount(10, 19));
    map.put("20-29", getRepositories().findByQuestionUsersCount(20, 29));
    map.put("30-39", getRepositories().findByQuestionUsersCount(30, 39));
    map.put("40-49", getRepositories().findByQuestionUsersCount(40, 49));
    map.put("50-59", getRepositories().findByQuestionUsersCount(50, 59));
    map.put("above 60", getRepositories().findByQuestionUsersCount(60));
    return map;
  }
  public Map<String, Long> getUsersDistributionAnswer() {
    Map<String, Long> map = new HashMap<>();
    map.put("0-9", getRepositories().findByQuestionAnswerUsersCount(0, 9));
    map.put("10-19", getRepositories().findByQuestionAnswerUsersCount(10, 19));
    map.put("20-29", getRepositories().findByQuestionAnswerUsersCount(20, 29));
    map.put("30-39", getRepositories().findByQuestionAnswerUsersCount(30, 39));
    map.put("40-49", getRepositories().findByQuestionAnswerUsersCount(40, 49));
    map.put("50-59", getRepositories().findByQuestionAnswerUsersCount(50, 59));
    map.put("above 60", getRepositories().findByQuestionAnswerUsersCount(60));
    return map;
  }
  public Map<String, Long> getUsersDistributionComment() {
    Map<String, Long> map = new HashMap<>();
    map.put("0-5", getRepositories().findByQuestionCommentUsersCount(0, 4));
    map.put("6-10", getRepositories().findByQuestionCommentUsersCount(5, 9));
    map.put("11-15", getRepositories().findByQuestionCommentUsersCount(10, 14));
    map.put("above 15", getRepositories().findByQuestionCommentUsersCount(15));
    return map;
  }
  public Map<String, Long> getMostHotApi() {
    List<HotApi> a = getRepositories().findByApiCount();
    Map<String, Long> map = new HashMap<>();
    for (int i = 0; i < 45; i++) {
      map.put(a.get(i).name, (long) a.get(i).count);
    }
    return map;
  }
  public String getMostHotApiTop() {
    return "{" + getRepositories().findByApiCount().get(0).name + "}";
  }

  /**
   * The following methods are for RESTful API
   */
  public List<Answers> getAllAnswers() {
    return getRepositories().findAllAnswers();
  }

  public List<Answers> getAnswersByQuestion_id(Integer question_id) {
    return getRepositories().findAnswersByQuestion_id(question_id);
  }

  public List<Answers> getAllAcceptedAnswers() {
    return getRepositories().findAllAcceptedAnswers();
  }

  public List<Answers> getAcceptedAnswersByQuestionId(Integer question_id) {
    return getRepositories().findAcceptedAnswersByQuestionId(question_id);
  }

  public List<Comments> getAllComments() {
    return getRepositories().findAllComments();
  }

  public List<Comments> getCommentsByQuestion_id(Integer question_id) {
    return getRepositories().findCommentsByQuestion_id(question_id);
  }

  public List<Questions> getAllQuestions() {
    return getRepositories().findAllQuestions();
  }

  public List<Questions> getQuestionsByQuestion_id(Integer question_id) {
    return getRepositories().findQuestionsByQuestion_id(question_id);
  }

  public List<TagsJavaRelated> getAllTagsJavaRelated() {
    return getRepositories().findAllTagsJavaRelated();
  }

}



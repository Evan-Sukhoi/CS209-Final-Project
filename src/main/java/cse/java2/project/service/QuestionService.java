package cse.java2.project.service;

import com.google.gson.JsonObject;
import cse.java2.project.model.*;
import cse.java2.project.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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
public class QuestionService {
  private final QuestionRepository questionRepository;
/*  private final TagsJavaRelatedRepository;*/

  @Autowired
  private JdbcTemplate jdbcTemplate;
  @Autowired
  public QuestionService(QuestionRepository questionRepository) {
    this.questionRepository = questionRepository;
/*    this.TagsJavaRelatedRepository = tagsJavaRelatedRepository;*/
  }

  public QuestionRepository getQuestionRepository() {
    return questionRepository;
  }

/*  public TagsJavaRelatedRepository getTags_java_relatedRepository() {
    return TagsJavaRelatedRepository;
  }*/

  public Map<String, Long> getAnswersDistribution() {
    Map<String, Long> map = new HashMap<>();
    map.put("0-9", getQuestionRepository().findByAnswer_countRange(0, 9));
    map.put("10-19", getQuestionRepository().findByAnswer_countRange(10, 19));
    map.put("20-29", getQuestionRepository().findByAnswer_countRange(20, 29));
    map.put("30-39", getQuestionRepository().findByAnswer_countRange(30, 39));
    map.put("40-49", getQuestionRepository().findByAnswer_countRange(40, 49));
    map.put("50-59", getQuestionRepository().findByAnswer_countRange(50, 59));
    map.put(">=60", getQuestionRepository().findByAnswer_countRange(60));
    return map;
  }
  public double getAnswerCountAverage() {
    return getQuestionRepository().findByAnswer_countAverage();
  }
  public double getAnswerCountMax() {
    return getQuestionRepository().findByAnswer_countMax();
  }
  public String getNoAnswersPercentage() {
    NumberFormat percentFormat = NumberFormat.getPercentInstance();
    percentFormat.setMaximumFractionDigits(4);
    return percentFormat.format((double)
            getQuestionRepository().findByAnswer_countRange(0, 0) / getQuestionRepository().findTheQuestionsSize());
  }
  public Map<String, Long> getNoAnswers() {
    Map<String, Long> map = new HashMap<>();
    Long total = getQuestionRepository().findTheQuestionsSize();
    Long a = getQuestionRepository().findByAnswer_countRange(0, 0);
    map.put("Having", total - a);
    map.put("Not having", a);
    return map;
  }
  public String getHavingAcceptedAnswerPercentage() {
    NumberFormat percentFormat = NumberFormat.getPercentInstance();
    percentFormat.setMaximumFractionDigits(4);
    return percentFormat.format( (double)
            getQuestionRepository().findByHavingAcceptedAnswers() / getQuestionRepository().findTheQuestionsSize());
  }
  public Map<String, Long> getHavingAcceptedAnswers() {
    Map<String, Long> map = new HashMap<>();
    Long total = getQuestionRepository().findTheQuestionsSize();
    Long a = getQuestionRepository().findByHavingAcceptedAnswers();
    map.put("Having", a);
    map.put("Not having", total - a);
    return map;
  }
  public Map<String, Long> getResolutionDurationDistribution() {
    Map<String, Long> map = new HashMap<>();
    map.put("within 1 hours", getQuestionRepository().findByResolutionTimeRange(0, 1));
    map.put("1-3 hours", getQuestionRepository().findByResolutionTimeRange(1, 3));
    map.put("3-6 hours", getQuestionRepository().findByResolutionTimeRange(3, 6));
    map.put("6-12 hours", getQuestionRepository().findByResolutionTimeRange(6, 12));
    map.put("12-24 hours", getQuestionRepository().findByResolutionTimeRange(12, 24));
    map.put("above 24 hours", getQuestionRepository().findByResolutionTimeRange(24));
    map.put("Not solved", getQuestionRepository().findTheQuestionsSize() - getQuestionRepository().findByHavingAcceptedAnswers());
    return map;
  }
  public String getNotWillPercentage() {
    NumberFormat percentFormat = NumberFormat.getPercentInstance();
    percentFormat.setMaximumFractionDigits(4);
    return percentFormat.format( (double)
            getQuestionRepository().findByNot_public_will() / getQuestionRepository().findTheQuestionsSize());
  }

  public Map<String, Long> getNotWill() {
    Map<String, Long> map = new HashMap<>();
    Long total = getQuestionRepository().findTheQuestionsSize();
    Long a = getQuestionRepository().findByNot_public_will();
    map.put("Having", a);
    map.put("Not having", total - a);
    return map;
  }

  public Map<String, Long> getMostRelatedToJava() {
    List<TagsJavaRelated> a = getQuestionRepository().findByCount();
    Map<String, Long> map = new HashMap<>();
    for (int i = 0; i < 14; i++) {
      map.put(a.get(i).name, (long) a.get(i).count);
    }
    return map;
  }
  public String getMostRelatedToJavaTop() {
    return getQuestionRepository().findByCount().get(0).name;
  }
  public Map<String, Long> getBest() {
    List<Tags> a = getQuestionRepository().findByUpvotes();
    Map<String, Long> map = new HashMap<>();
    for (int i = 0; i < 14; i++) {
      map.put(a.get(i).name, (long) a.get(i).score);
    }
    return map;
  }
  public String getBestTop() {
    return "{" + getQuestionRepository().findByUpvotes().get(0).name + "}";
  }
  public Map<String, Long> getMostFashion() {
    List<Tags> a = getQuestionRepository().findByViews();
    Map<String, Long> map = new HashMap<>();
    for (int i = 0; i < 14; i++) {
      map.put(a.get(i).name, (long) a.get(i).view_count);
    }
    return map;
  }
  public String getMostFashionTop() {
    return "{" + getQuestionRepository().findByViews().get(0).name + "}";
  }
  public Map<String, Long> getMostActiveUsers() {
    List<Users> a = getQuestionRepository().findByUsersCount();
    Map<String, Long> map = new HashMap<>();
    for (int i = 0; i < 14; i++) {
      map.put(a.get(i).display_name, (long) a.get(i).count);
    }
    return map;
  }
  public String getMostActiveUsersTop() {
    return getQuestionRepository().findByUsersCount().get(0).display_name;
  }
  public Map<String, Long> getUsersDistribution() {
    Map<String, Long> map = new HashMap<>();
    map.put("0-9", getQuestionRepository().findByQuestionUsersCount(0, 9));
    map.put("10-19", getQuestionRepository().findByQuestionUsersCount(10, 19));
    map.put("20-29", getQuestionRepository().findByQuestionUsersCount(20, 29));
    map.put("30-39", getQuestionRepository().findByQuestionUsersCount(30, 39));
    map.put("40-49", getQuestionRepository().findByQuestionUsersCount(40, 49));
    map.put("50-59", getQuestionRepository().findByQuestionUsersCount(50, 59));
    map.put("above 60", getQuestionRepository().findByQuestionUsersCount(60));
    return map;
  }
  public Map<String, Long> getUsersDistributionAnswer() {
    Map<String, Long> map = new HashMap<>();
    map.put("0-9", getQuestionRepository().findByQuestionAnswerUsersCount(0, 9));
    map.put("10-19", getQuestionRepository().findByQuestionAnswerUsersCount(10, 19));
    map.put("20-29", getQuestionRepository().findByQuestionAnswerUsersCount(20, 29));
    map.put("30-39", getQuestionRepository().findByQuestionAnswerUsersCount(30, 39));
    map.put("40-49", getQuestionRepository().findByQuestionAnswerUsersCount(40, 49));
    map.put("50-59", getQuestionRepository().findByQuestionAnswerUsersCount(50, 59));
    map.put("above 60", getQuestionRepository().findByQuestionAnswerUsersCount(60));
    return map;
  }
  public Map<String, Long> getUsersDistributionComment() {
    Map<String, Long> map = new HashMap<>();
    map.put("0-5", getQuestionRepository().findByQuestionCommentUsersCount(0, 4));
    map.put("6-10", getQuestionRepository().findByQuestionCommentUsersCount(5, 9));
    map.put("11-15", getQuestionRepository().findByQuestionCommentUsersCount(10, 14));
    map.put("above 15", getQuestionRepository().findByQuestionCommentUsersCount(15));
    return map;
  }
  public Map<String, Long> getMostHotApi() {
    List<HotApi> a = getQuestionRepository().findByApiCount();
    Map<String, Long> map = new HashMap<>();
    for (int i = 0; i < 14; i++) {
      map.put(a.get(i).name, (long) a.get(i).count);
    }
    return map;
  }
  public String getMostHotApiTop() {
    return "{" + getQuestionRepository().findByApiCount().get(0).name + "}";
  }

  public void saveJsonObject(JsonObject jsonObject) {
    String sql = "INSERT INTO your_table_name (name, age) VALUES (?, ?)";

    jdbcTemplate.update(sql,
        jsonObject.get("name").getAsString(),
        jsonObject.get("age").getAsInt());
  }

}



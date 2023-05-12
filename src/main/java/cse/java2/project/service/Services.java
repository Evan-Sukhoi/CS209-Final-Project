package cse.java2.project.service;

import cse.java2.project.model.HotApi;
import cse.java2.project.model.Tags;
import cse.java2.project.model.TagsJavaRelated;
import cse.java2.project.repository.Repositories;
import org.springframework.beans.factory.annotation.Autowired;
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
/*  private final TagsJavaRelatedRepository;*/

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Autowired
  public Services(Repositories Repositories) {
    this.Repositories = Repositories;
/*    this.TagsJavaRelatedRepository = tagsJavaRelatedRepository;*/
  }

  public Repositories getRepositories() {
    return Repositories;
  }

/*  public TagsJavaRelatedRepository getTags_java_relatedRepository() {
    return TagsJavaRelatedRepository;
  }*/

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
    for (int i = 0; i < 14; i++) {
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
  public Map<String, Long> getMostHotApi() {
    List<HotApi> a = getRepositories().findByApiCount();
    Map<String, Long> map = new HashMap<>();
    for (int i = 0; i < 14; i++) {
      map.put(a.get(i).name, (long) a.get(i).count);
    }
    return map;
  }
  public String getMostHotApiTop() {
    return "{" + getRepositories().findByApiCount().get(0).name + "}";
  }
}



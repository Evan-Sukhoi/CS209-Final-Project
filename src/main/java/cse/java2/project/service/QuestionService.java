package cse.java2.project.service;

import com.google.gson.JsonObject;
import cse.java2.project.model.Questions;
import cse.java2.project.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Evan
 */
@Service
public class QuestionService {
  private final QuestionRepository questionRepository;

  @Autowired
  private JdbcTemplate jdbcTemplate;

  public QuestionService(QuestionRepository questionRepository) {
    this.questionRepository = questionRepository;
  }

  public QuestionRepository getQuestionRepository() {
    return questionRepository;
  }

  public void saveJsonObject(JsonObject jsonObject) {
    String sql = "INSERT INTO your_table_name (name, age) VALUES (?, ?)";

    jdbcTemplate.update(sql,
        jsonObject.get("name").getAsString(),
        jsonObject.get("age").getAsInt());
  }
}



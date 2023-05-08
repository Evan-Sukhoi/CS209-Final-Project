package cse.java2.project.questions;

import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

/**
 * @author Evan
 */
@Service
public class QuestionService {

  @Autowired
  private JdbcTemplate jdbcTemplate;

  public void saveJsonObject(JsonObject jsonObject) {
    String sql = "INSERT INTO your_table_name (name, age) VALUES (?, ?)";

    jdbcTemplate.update(sql,
        jsonObject.get("name").getAsString(),
        jsonObject.get("age").getAsInt());
  }
}



package cse.java2.project.database;

import com.google.gson.JsonArray;
import cse.java2.project.StackOverflowApi;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.google.gson.JsonObject;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author Evan
 */

public class addData {

  private static final String DB_URL = "jdbc:mysql://localhost:3306/mydatabase";
  private static final String USER = "root";
  private static final String PASS = "010504";

  public static void main(String[] args) {
    addQuestion();
    updateQuestion();
    
    addUsers();
  }

  private static void addUsers() {
  }

  private static void updateQuestion() {
    StackOverflowApi api = new StackOverflowApi();

    // 获取数据库中的所有is_answered为真的数据
    List<JsonObject> dataList = getAllDataFromDatabase();

    // 对数据进行修改
    for (JsonObject data : dataList) {
      int acceptedAnswerId = data.get("accepted_answer_id").getAsInt();
      // 调用queryQuestion方法，获取accepted_answer_id对应的回答的信息
      JsonObject answer = queryQuestion(acceptedAnswerId, api);

      long acceptedDate = answer.get("creation_date").getAsLong() * 1000;
      data.addProperty("accepted_date", acceptedDate);
      System.out.println(acceptedAnswerId + " " + acceptedDate);

      System.out.println(answer.get("answer_id"));

      int acceptedScore = answer.get("score").getAsInt();
      // 调用queryTopAnswer方法，获取问题的最高票回答的信息
      JsonObject answerOfQuestion = queryTopAnswer(data.get("question_id").getAsInt(), api);
      int score = answerOfQuestion.get("score").getAsInt();

      data.addProperty("not_public_will", !(acceptedScore >= score));

      // 更新数据库中的数据
      updateDataInDatabase(data);

    }

  }

  static List<JsonObject> getAllDataFromDatabase() {
    List<JsonObject> dataList = new ArrayList<>();
    String sql = "SELECT * FROM questions WHERE is_answered = true";

    try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery()) {

      while (rs.next()) {
        JsonObject jsonObject = new JsonObject();
        // 获取每一行的数据
        jsonObject.addProperty("question_id", rs.getString("question_id"));
        jsonObject.addProperty("is_answered", rs.getString("is_answered"));
        jsonObject.addProperty("accepted_answer_id", rs.getString("accepted_answer_id"));
        dataList.add(jsonObject);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return dataList;
  }

  static void updateDataInDatabase(JsonObject data) {
    String sql = "UPDATE questions SET accepted_answer_id = ?, accepted_date = ?, not_public_will = ? WHERE question_id = ?";

    try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
        PreparedStatement stmt = conn.prepareStatement(sql)) {

      // 根据你的表结构，设置预处理语句中的参数值
      stmt.setInt(1, data.get("accepted_answer_id").getAsInt());
      stmt.setTimestamp(2, new Timestamp(data.get("accepted_date").getAsLong()));
      stmt.setBoolean(3, data.get("not_public_will").getAsBoolean());
      stmt.setInt(4, data.get("question_id").getAsInt());
      stmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  static void addQuestion() {
    // 使用StackOverflowApi类的fetchData方法，向StackExchange API发送请求
    StackOverflowApi api = new StackOverflowApi();
    Map<String, String> params = new HashMap<>(1);
    params.put("pagesize", "100");
    params.put("page", "2653");
//    params.put("sort", "activity");
    params.put("sort", "votes");
    params.put("order", "desc");
    // 获得返回的JsonObject
    CompletableFuture<JsonObject> future = api.fetchData("questions", params);

    future.thenAcceptAsync(jsonObject -> {
      JsonArray items = jsonObject.getAsJsonArray("items");
      List<CompletableFuture<Void>> tasks = new ArrayList<>();

      for (int i = 0; i < items.size(); i++) {
        JsonObject item = items.get(i).getAsJsonObject();
        tasks.add(CompletableFuture.runAsync(() -> insertJsonObjectIntoDatabase(item, api))
        );
      }
      CompletableFuture.allOf(tasks.toArray(new CompletableFuture<?>[0])).join();
    });

  }


  private static void insertJsonObjectIntoDatabase(JsonObject jsonObject, StackOverflowApi api) {
    String sql = "INSERT INTO questions (question_id, answer_count, is_answered, creation_date, accepted_answer_id, accepted_date, not_public_will) VALUES (?, ?, ?, ?, ?, ?, ?)";

    try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
      // 将信息填入sql语句
      PreparedStatement pstmt = conn.prepareStatement(sql);
      pstmt.setInt(1, jsonObject.get("question_id").getAsInt());
      pstmt.setInt(2, jsonObject.get("answer_count").getAsInt());
      boolean is_answered = jsonObject.get("is_answered").getAsBoolean();
      pstmt.setBoolean(3, is_answered);
      Date date = new Date(jsonObject.get("creation_date").getAsLong() * 1000);
      Timestamp timestamp = new Timestamp(date.getTime());
      pstmt.setTimestamp(4, timestamp);

      pstmt.setNull(5, java.sql.Types.INTEGER);
      if (is_answered) {
        int acceptedAnswerId = jsonObject.get("accepted_answer_id").getAsInt();
        pstmt.setInt(5, acceptedAnswerId);
      }

//       如果问题有被回答，将回答的信息也填入sql语句
//      if (is_answered) {
//        // 获取accepted_answer_id
//        int acceptedAnswerId = jsonObject.get("accepted_answer_id").getAsInt();
//        pstmt.setInt(5, acceptedAnswerId);
//        // 调用queryQuestion方法，获取accepted_answer_id对应的回答的信息
//        JsonObject answer = queryQuestion(acceptedAnswerId, api);
//        Date acceptedDate = new Date(answer.get("creation_date").getAsLong() * 1000);
//        Timestamp acceptedTimestamp = new Timestamp(acceptedDate.getTime());
//        pstmt.setTimestamp(6, acceptedTimestamp);
//
//        int acceptedScore = answer.get("score").getAsInt();
//
//        // 调用queryTopAnswer方法，获取问题的最高票回答的信息
//        JsonObject answerOfQuestion = queryTopAnswer(jsonObject.get("question_id").getAsInt(), api);
//        int score = answerOfQuestion.get("score").getAsInt();
//
//        pstmt.setBoolean(7, acceptedScore >= score);
//      }else {
//        pstmt.setNull(5, java.sql.Types.INTEGER);
//        pstmt.setNull(6, java.sql.Types.TIMESTAMP);
//        pstmt.setNull(7, java.sql.Types.BOOLEAN);
//      }
      pstmt.setNull(6, java.sql.Types.TIMESTAMP);
      pstmt.setNull(7, java.sql.Types.BOOLEAN);

      pstmt.executeUpdate();
      System.out.println("Record inserted successfully");
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  static JsonObject queryQuestion(int answer_id, StackOverflowApi api) {
    Map<String, String> params = new HashMap<>(1);
    params.put("ids", String.valueOf(answer_id));
    CompletableFuture<JsonObject> future = api.fetchData("answers", params);

    try {
      JsonObject jsonObject = future.get();
      JsonArray items = jsonObject.getAsJsonArray("items");
      return items.get(0).getAsJsonObject();
    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
      return null;
    }
  }

  static JsonObject queryTopAnswer(int question_id, StackOverflowApi api) {
    try {
      Map<String, String> params = new HashMap<>(1);
      params.put("ids", String.valueOf(question_id));
      params.put("sort", "votes");
      params.put("order", "desc");
      params.put("pagesize", "1");
      CompletableFuture<JsonObject> future = api.fetchData("answer_question", params);

      JsonArray items = future.get().getAsJsonArray("items");
      return items.get(0).getAsJsonObject();

    } catch (ExecutionException | InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

}


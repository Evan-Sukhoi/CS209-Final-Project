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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.persistence.criteria.CriteriaBuilder.In;

/**
 * @author Evan
 */

public class addData {

  private static final String DB_URL = "jdbc:mysql://localhost:3306/public";
  private static final String USER = "root";
  private static final String PASS = "LywMysql";

  public static void main(String[] args) {
    addQuestion();
//    updateQuestionAcptInfo();

//    updateQuestionTags();
//    addTags();

//    addTagsJavaRelated();

//    updateUsers();

//    addBodies();
  }

  private static void addBodies() {
    List<Integer> questionIds = getAllQuestionIds();

    String sql1 = "INSERT INTO answers (answer_id, question_id, body) VALUES (?, ?, ?) ";
    String sql2 = "INSERT INTO comments (comment_id, question_id, body) VALUES (?, ?, ?) ";
    String sql3 = "Update questions SET body = ? WHERE question_id = ?";

    try (
        Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
        PreparedStatement pstmt1 = conn.prepareStatement(sql1);
        PreparedStatement pstmt2 = conn.prepareStatement(sql2);
        PreparedStatement pstmt3 = conn.prepareStatement(sql3);
    ) {
      for (Integer questionId : questionIds) {
        StackOverflowApi api = new StackOverflowApi();
        Map<String, String> params = new HashMap<>();

        params.put("ids", questionId.toString());
        String filter = "!-KbmneoxTT(85c6ZIwXKXNeD**.IX4rPe";
//        filter = URLEncoder.encode(filter, "UTF-8"); // 对filter参数的值进行编码
        params.put("filter", filter);
        CompletableFuture<JsonObject> future = api.fetchData("questions", params);

        JsonObject question = future.get().getAsJsonArray("items").get(0).getAsJsonObject();
        String questionBody = question.get("body").getAsString();
        pstmt3.setString(1, questionBody);
        pstmt3.setInt(2, questionId);
        pstmt3.executeUpdate();

        if (question.has("answers")) {
          JsonArray answers = question.get("answers").getAsJsonArray();
          for (int i = 0; i < answers.size(); i++) {
            JsonObject answer = answers.get(i).getAsJsonObject();
            pstmt1.setInt(1, answer.get("answer_id").getAsInt());
            pstmt1.setInt(2, questionId);
            pstmt1.setString(3, answer.get("body").getAsString());
            pstmt1.executeUpdate();
          }
        }
        if (question.has("comments")) {
          JsonArray comments = question.get("comments").getAsJsonArray();
          for (int i = 0; i < comments.size(); i++) {
            JsonObject comment = comments.get(i).getAsJsonObject();
            pstmt2.setInt(1, comment.get("comment_id").getAsInt());
            pstmt2.setInt(2, questionId);
            pstmt2.setString(3, comment.get("body").getAsString());
            pstmt2.executeUpdate();
          }
        }
      }
    } catch (SQLException | InterruptedException | ExecutionException e) {
      throw new RuntimeException(e);
    }
  }

  private static void addTagsJavaRelated() {
    List<JsonObject> tags = getTagsForJavaRelated();

    String sql = "INSERT INTO tags_java_related (name, count) VALUES (?, ?) "
        + "ON DUPLICATE KEY UPDATE count = count + ?;";

    try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
        PreparedStatement pstmt = conn.prepareStatement(sql);) {

      for (JsonObject tag : tags) {
        String tagCombination = tag.get("name").getAsString();

        List<String> tagList = Arrays.asList(tagCombination.split(","));

        if (tagList.size() != 1 && tagList.contains("java")) {
          tagList.remove("java");
          for (String tagName : tagList) {
            pstmt.setString(1, tagName);
            pstmt.setInt(2, tag.get("count").getAsInt());
            pstmt.setInt(3, tag.get("count").getAsInt());
            pstmt.executeUpdate();
          }
        }

      }

    } catch (SQLException e) {
      e.printStackTrace();
    }

  }


  private static void addTags() {
    List<JsonObject> tags = getAllTags();

    String sql = "INSERT INTO tags (name, score, view_count, count) VALUES (?, ?, ?, 1) "
        + "ON DUPLICATE KEY UPDATE count = count + 1, score = score + ?, view_count = view_count + ?;";

    try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
        PreparedStatement pstmt = conn.prepareStatement(sql)) {

      for (JsonObject tag : tags) {
        String tagCombination = tag.get("tags").getAsString();
        String regex = "\\\"([^\"\\\\]*(\\\\.[^\"\\\\]*)*)\\\""; // 正则表达式，匹配引号内的内容，忽略转义引号

        // 创建 Pattern 对象

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(tagCombination);

        StringBuilder result = new StringBuilder();
        while (matcher.find()) {
          if (result.length() > 0) {
            result.append(",");
          }
          result.append(matcher.group(1));
        }

        pstmt.setString(1, result.toString());
        pstmt.setInt(2, tag.get("score").getAsInt());
        pstmt.setInt(3, tag.get("view_count").getAsInt());

        pstmt.setInt(4, tag.get("score").getAsInt());
        pstmt.setInt(5, tag.get("view_count").getAsInt());

        pstmt.executeUpdate();
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }


  private static void updateUsers() {
    StackOverflowApi api = new StackOverflowApi();

    // 获取数据库中的所有Question的id
    List<Integer> questionIds = getAllQuestionIds();

    for (int questionId : questionIds) {
      System.out.println(questionId);
      List<Integer> userIdsAnswer = new ArrayList<>();
      List<Integer> userIdsComment = new ArrayList<>();

      // 调用queryQuestion方法，获取questionId对应的问题的信息
      JsonObject question = queryQuestion(questionId, api);

      if (question == null) {
        continue;
      }

      // 调用queryQuestionOwner方法，获取questionId对应的问题的提问者的信息
//      JsonObject owner = question.get("owner").getAsJsonObject();
//
//      if (!owner.get("user_type").getAsString().equals("does_not_exist")) {
//        int ownerId = owner.get("user_id").getAsInt();
////        String ownerName = owner.get("display_name").getAsString();
////        insertUser(ownerId, ownerName);
//      }

      // 调用queryQuestionAnswers方法，获取questionId对应的问题的回答的信息
      if (question.has("answers")) {
        JsonArray answers = question.get("answers").getAsJsonArray();
        // 将回答者的信息插入到数据库中
        for (int i = 0; i < answers.size() - 3; i++) {
          JsonObject answer = answers.get(i).getAsJsonObject();
          JsonObject answerOwner = answer.get("owner").getAsJsonObject();
          if (answerOwner.get("user_type").getAsString().equals("does_not_exist")) {
            continue;
          }
          int answerOwnerId = answerOwner.get("user_id").getAsInt();
//        String answerOwnerName = answerOwner.get("display_name").getAsString();
//        insertUser(answerOwnerId, answerOwnerName);
          userIdsAnswer.add(answerOwnerId);
        }
      }

      // 调用queryQuestionComments方法，获取questionId对应的问题的评论的信息
      if (question.has("comments")) {
        JsonArray comments = question.get("comments").getAsJsonArray();
        // 将评论者的信息插入到数据库中
        for (int i = 0; i < comments.size() - 3; i++) {
          JsonObject comment = comments.get(i).getAsJsonObject();
          JsonObject commentOwner = comment.get("owner").getAsJsonObject();
          if (commentOwner.get("user_type").getAsString().equals("does_not_exist")) {
            continue;
          }
          int commentOwnerId = commentOwner.get("user_id").getAsInt();
//        String commentOwnerName = commentOwner.get("display_name").getAsString();
//        insertUser(commentOwnerId, commentOwnerName);
          userIdsComment.add(commentOwnerId);
        }
      }

      List<Integer> distinctIDs = userIdsAnswer.stream().distinct().toList(); // 去重后的List
      List<Integer> distinctIDs2 = userIdsComment.stream().distinct().toList(); // 去重后的List
      int user_count = distinctIDs.size() + distinctIDs2.size() + 1; // 加上提问者

      insertUserCount(questionId, user_count, distinctIDs.size(), distinctIDs2.size());
    }
  }

  private static void insertUserCount(int questionId, int userCount, int answerCount,
      int commentCount) {
    String sql = "UPDATE questions SET comment_user_count = ?, answer_user_count = ?, user_count = ? WHERE question_id = ?;";
    try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setInt(1, commentCount);
      pstmt.setInt(2, answerCount);
      pstmt.setInt(3, userCount);
      pstmt.setInt(4, questionId);
      pstmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }


  private static void insertUser(int ownerId, String ownerName) {
    String sql = "INSERT INTO users (account_id, display_name, count) VALUES (?, ?, 1) ON DUPLICATE KEY UPDATE count = count + 1;";

    try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setInt(1, ownerId);
      pstmt.setString(2, ownerName);
      pstmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  private static void updateQuestionAcptInfo() {
    StackOverflowApi api = new StackOverflowApi();

    // 获取数据库中的所有is_answered为真的数据
    List<JsonObject> dataList = getAllDataFromDatabaseForAcptInfo();

    // 对数据进行修改
    for (JsonObject data : dataList) {
      int acceptedAnswerId = data.get("accepted_answer_id").getAsInt();
      // 调用queryQuestion方法，获取accepted_answer_id对应的回答的信息
      JsonObject answer = queryQuestionByAnswer(acceptedAnswerId, api);

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

  private static void updateQuestionTags() {
    StackOverflowApi api = new StackOverflowApi();

    // 获取数据库中的所有Question的id
    List<Integer> questionIds = getAllQuestionIds();

    for (int questionId : questionIds) {
      // 调用queryQuestion方法，获取问题的信息
      JsonObject question;
      try {
        question = queryQuestion(questionId, api);
      } catch (Exception e) {
        e.printStackTrace();
        continue;
      }
      // 获取问题的标签
      JsonArray tags = question.get("tags").getAsJsonArray();
      // 将标签转换为字符串
      String tagsString = tags.toString();
      int score = question.get("score").getAsInt();
      int viewCount = question.get("view_count").getAsInt();

      // 更新数据库中的数据
      updateTagsInDatabase(questionId, tagsString, score, viewCount);
    }
  }

  private static void updateTagsInDatabase(int questionId, String tagsString, int score,
      int viewCount) {
    String sql = "UPDATE questions SET tags = ?, score = ?, view_count = ? WHERE question_id = ?";

    try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
        PreparedStatement stmt = conn.prepareStatement(sql)) {

      // 根据你的表结构，设置预处理语句中的参数值
      stmt.setString(1, tagsString);
      stmt.setInt(2, score);
      stmt.setInt(3, viewCount);
      stmt.setInt(4, questionId);
      stmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }


  static List<JsonObject> getAllDataFromDatabaseForAcptInfo() {
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

  private static List<Integer> getAllQuestionIds() {
    List<Integer> questionIds = new ArrayList<>();
    String sql = "SELECT question_id FROM questions where question_id >= 0 ORDER BY question_id ASC";

    try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery()) {

      while (rs.next()) {
        questionIds.add(rs.getInt("question_id"));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return questionIds;
  }

  private static List<JsonObject> getAllTags() {
    List<JsonObject> dataList = new ArrayList<>();
    String sql = "SELECT tags, score, view_count FROM questions";

    try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery()) {

      while (rs.next()) {
        JsonObject jsonObject = new JsonObject();
        // 获取每一行的数据
        jsonObject.addProperty("tags", rs.getString("tags"));
        jsonObject.addProperty("score", rs.getString("score"));
        jsonObject.addProperty("view_count", rs.getString("view_count"));
        dataList.add(jsonObject);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return dataList;
  }

  private static List<JsonObject> getTagsForJavaRelated() {
    List<JsonObject> tags = new ArrayList<>();

    String sql = "SELECT name, count FROM tags;";
    try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
        PreparedStatement pstmt = conn.prepareStatement(sql)) {

      ResultSet rs = pstmt.executeQuery();

      while (rs.next()) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", rs.getString("name"));
        jsonObject.addProperty("count", rs.getString("count"));
        tags.add(jsonObject);
      }

      return tags;

    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
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
    params.put("pagesize", "1");
    params.put("page", "1");
    params.put("sort", "activity");
//    params.put("sort", "votes");
//    params.put("sort", "hot");
    params.put("order", "desc");
    params.put("tagged", "java");

    // 获得返回的JsonObject
    CompletableFuture<JsonObject> future = api.fetchData("questions", params);

    future.thenAcceptAsync(jsonObject -> {
      JsonArray items = jsonObject.getAsJsonArray("items");
      List<CompletableFuture<Void>> tasks = new ArrayList<>();

      for (int i = 0; i < items.size(); i++) {
        JsonObject item = items.get(i).getAsJsonObject();
        tasks.add(CompletableFuture.runAsync(() -> insertQuestionIntoDatabase(item, api))
        );
      }
      CompletableFuture.allOf(tasks.toArray(new CompletableFuture<?>[0])).join();
    });
  }


  private static void insertQuestionIntoDatabase(JsonObject jsonObject, StackOverflowApi api) {
    String sql = "INSERT INTO questions (question_id, answer_count, is_answered, creation_date, accepted_answer_id, accepted_date, not_public_will, score, view_count, tags) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

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
      pstmt.setNull(6, java.sql.Types.TIMESTAMP);
      pstmt.setNull(7, java.sql.Types.BOOLEAN);

      pstmt.setInt(8, jsonObject.get("score").getAsInt());
      pstmt.setInt(9, jsonObject.get("view_count").getAsInt());

      // 获取问题的标签
      JsonArray tags = jsonObject.get("tags").getAsJsonArray();
      // 将标签转换为字符串
      String tagsString = tags.toString();
      pstmt.setString(10, tagsString);

      if (is_answered) {
        int acceptedAnswerId = jsonObject.get("accepted_answer_id").getAsInt();
        pstmt.setInt(5, acceptedAnswerId);
      }

      pstmt.executeUpdate();
      System.out.println("Record inserted successfully");
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  static JsonObject queryQuestionByAnswer(int answer_id, StackOverflowApi api) {
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

  static JsonObject queryQuestion(int question_id, StackOverflowApi api) {
    Map<String, String> params = new HashMap<>(1);
    params.put("ids", String.valueOf(question_id));
    params.put("filter", "!B9tEMyjamHuhqA_ixREf6pYm9fUBiL");
    CompletableFuture<JsonObject> future = api.fetchData("questions", params);

    try {
      JsonObject jsonObject = future.get();
      JsonArray items = jsonObject.getAsJsonArray("items");
      if (items.size() == 0) {
        System.out.println("Question not found: " + question_id);
        return null;
      }
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

  private static JsonArray queryQuestionAnswers(int questionId, StackOverflowApi api) {
    Map<String, String> params = new HashMap<>(1);
    params.put("ids", String.valueOf(questionId));
    params.put("pagesize", "100");
    CompletableFuture<JsonObject> future = api.fetchData("answer_question", params);
    try {
      JsonObject jsonObject = future.get();
      return jsonObject.getAsJsonArray("items");
    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
      return null;
    }
  }

  private static JsonArray queryQuestionComments(int questionId, StackOverflowApi api) {
    Map<String, String> params = new HashMap<>(1);
    params.put("ids", String.valueOf(questionId));
    params.put("pagesize", "100");
    CompletableFuture<JsonObject> future = api.fetchData("comment_question", params);
    try {
      JsonObject jsonObject = future.get();
      return jsonObject.getAsJsonArray("items");
    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
      return null;
    }
  }

}


package cse.java2.project;

import java.io.IOException;
import java.util.Map;
import okhttp3.*;
import com.google.gson.*;

/**
 * @author Evan
 * This class is used to fetch data from Stack Overflow API.
 */
public class StackOverflowApi {

  private static final String API_BASE_URL = "https://api.stackexchange.com/2.3";
  private static final String API_KEY = "kcM5s9XKgXkSQ38shvfDSQ((";

  public void fetchData(String op, Map<String, String> params) {
    OkHttpClient client = new OkHttpClient();
    Gson gson = new Gson();

    // 构建请求 URL
    HttpUrl url = HttpUrl.parse(API_BASE_URL + "/questions")
        .newBuilder()
        .addQueryParameter("tagged", "python")
        .addQueryParameter("site", "stackoverflow")
        .addQueryParameter("key", API_KEY)
        .build();

    // 创建请求
    Request request = new Request.Builder()
        .url(url)
        .build();

    // 发送请求并处理响应
    client.newCall(request).enqueue(new Callback() {
      @Override
      public void onFailure(Call call, IOException e) {
        e.printStackTrace();
      }

      @Override
      public void onResponse(Call call, Response response) throws IOException {
        if (!response.isSuccessful()) {
          throw new IOException("Unexpected code " + response);
        } else {
          String jsonData = response.body().string();
          // 使用 Gson 解析 JSON 数据
          // TODO: （定义一个实体类？）处理、存储、显示解析后的数据
          JsonObject jsonObject = gson.fromJson(jsonData, JsonObject.class);
          System.out.println(url);
          System.out.println(jsonObject);
        }
      }
    });
  }
}

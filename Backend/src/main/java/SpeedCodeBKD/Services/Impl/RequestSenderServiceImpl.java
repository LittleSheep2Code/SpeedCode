package SpeedCodeBKD.Services.Impl;

import SpeedCodeBKD.Services.RequestSenderService;
import lombok.SneakyThrows;
import okhttp3.*;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class RequestSenderServiceImpl implements RequestSenderService {
    private static final OkHttpClient HTTP_CLIENT = new OkHttpClient();
    public static final MediaType MEDIA_TYPE_JSON = MediaType.get("application/json; charset=utf-8");

    @Override
    @SneakyThrows
    public String getRequest(String url) {
        Request request = new Request.Builder().url(url).build();

        try(Response response = HTTP_CLIENT.newCall(request).execute()) {
            return response.body().string();
        }
    }

    @Override
    @SneakyThrows
    public JSONObject getRequestJSON(String url) {
        Request request = new Request.Builder().url(url).build();

        try(Response response = HTTP_CLIENT.newCall(request).execute()) {
            return new JSONObject(response.body().string());
        }
    }

    @Override
    @SneakyThrows
    public String postRequest(String url, String source) {
        RequestBody body = RequestBody.create(MEDIA_TYPE_JSON, source);
        Request request = new Request.Builder().url(url).post(body).build();

        try(Response response = HTTP_CLIENT.newCall(request).execute()) {
            return response.body().string();
        }
    }

    @Override
    @SneakyThrows
    public String postRequest(String url, JSONObject source) {
        RequestBody body = RequestBody.create(MEDIA_TYPE_JSON, source.toString());
        Request request = new Request.Builder().url(url).post(body).build();

        try(Response response = HTTP_CLIENT.newCall(request).execute()) {
            return response.body().string();
        }
    }

    @Override
    @SneakyThrows
    public JSONObject postRequestJSON(String url, String source) {
        RequestBody body = RequestBody.create(MEDIA_TYPE_JSON, source);
        Request request = new Request.Builder().url(url).post(body).build();

        try(Response response = HTTP_CLIENT.newCall(request).execute()) {
            return new JSONObject(response.body().string());
        }
    }

    @Override
    @SneakyThrows
    public JSONObject postRequestJSON(String url, JSONObject source) {
        RequestBody body = RequestBody.create(MEDIA_TYPE_JSON, source.toString());
        Request request = new Request.Builder().url(url).post(body).build();

        try(Response response = HTTP_CLIENT.newCall(request).execute()) {
            return new JSONObject(response.body().string());
        }
    }
}

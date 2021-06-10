package SpeedCodeBKD.Services;

import org.json.JSONObject;

public interface RequestSenderService {
    public String getRequest(String url);
    public JSONObject getRequestJSON(String url);

    public String postRequest(String url, String source);
    public String postRequest(String url, JSONObject source);
    public JSONObject postRequestJSON(String url, String source);
    public JSONObject postRequestJSON(String url, JSONObject source);
}

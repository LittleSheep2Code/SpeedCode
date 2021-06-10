package SpeedCodeBKD.Utils.Processor;

import io.micrometer.core.lang.Nullable;
import org.json.JSONObject;

public class ResultProcessor {

    // Static values
    public static final String[] StatusList = {"completed", "warning_failed", "error_failed", "failed", "unknown", "stop"};

    public static String compile(@Nullable JSONObject res, @Nullable String oper, @Nullable String reason, String status, int code) {
        JSONObject response = new JSONObject();

        if (reason != null) response.put("reason", reason);
        if (oper != null) response.put("operating", oper);
        if (res != null) response.put("result", res);

        response.put("status", status);
        response.put("status_code", code);

        return response.toString();
    }

    // Completed response function
    public static String completed_response(JSONObject result, String operating) {
        return compile(result, operating, null, StatusList[0], 200);
    }

    public static String completed_response(JSONObject result, String operating, int customize_sc) {
        return compile(result, operating, null, StatusList[0], customize_sc);
    }

    public static String completed_response(String info, String operating, int customize_sc) {
        return compile(new JSONObject().put("message", info), operating, null, StatusList[0], customize_sc);
    }

    public static String completed_response(String info, String operating) {
        return compile(new JSONObject().put("status", info), operating, null, StatusList[0], 200);
    }

    // Warning response function
    public static String warn_response(String reason, String operating) {
        return compile(null, operating, reason, StatusList[1], 200);
    }

    public static String warn_response(String reason, String operating, int customize_sc) {
        return compile(null, operating, reason, StatusList[1], customize_sc);
    }

    public static String warn_response(ReasonCode reason, String operating, int customize_sc) {
        return compile(null, operating, reason.getCode(), StatusList[1], customize_sc);
    }

    public static String warn_response(ReasonCode reason, String operating) {
        return compile(null, operating, reason.getCode(), StatusList[1], 200);
    }

    // Error response function
    public static String error_response(String reason, String operating) {
        return compile(null, operating, reason, StatusList[2], 400);
    }

    public static String error_response(String reason, String operating, int customize_sc) {
        return compile(null, operating, reason, StatusList[2], customize_sc);
    }

    public static String error_response(ReasonCode reason, String operating) {
        return compile(null, operating, reason.getCode(), StatusList[2], 400);
    }

    public static String error_response(ReasonCode reason, String operating, int customize_sc) {
        return compile(null, operating, reason.getCode(), StatusList[2], customize_sc);
    }

    public static int generateRandomNumber(int min, int max) {
        return min + (int) (Math.random() * (max - min + 1));
    }

    // ReasonCode enum
    public enum ReasonCode {

        permission_insufficient("PERNDF"),
        parameters_error("ARVERR"),
        duplicate_data("DATDUP"),
        email_error("EAMERR"),
        wrong_data("DATERR"),
        undefined("LGUNDF"),
        skip("SKIPOPR"),

        unknown_error("UNKNOWN");

        private final String code;

        private ReasonCode(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }
    }
}

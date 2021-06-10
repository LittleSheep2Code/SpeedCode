package SpeedCodeBKD.Services.Impl;

import SpeedCodeBKD.Services.CompileUtilsService;
import SpeedCodeBKD.Services.RequestSenderService;
import SpeedCodeBKD.Utils.Processor.IOReader;
import SpeedCodeBKD.Data.Service.ExecutorService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CompileUtilsServiceImpl implements CompileUtilsService {

    private static final String JUDGE0_HOST = "http://43.129.25.210:2358";

    @Autowired
    private RequestSenderService requestSenderService;

    @Autowired
    private ExecutorService codeCacheService;

    public short translateLanguage2Id(LanguageList language) {
        switch(language) {
            case NASM: return 45;
            case BASH: return 46;
            case BASIC: return 47;

            case C_CLANG7: return 75;
            case C_GCC7: return 48;
            case C_GCC8: return 49;
            case C_GCC9: return 50;
            case CSHARP: return 51;
            case CPP_CLANG7: return 76;
            case CPP_GPP7: return 52;
            case CPP_GPP8: return 53;
            case CPP_GPP9: return 54;

            case GOLANG: return 60;
            case JAVA_13: return 62;
            case NODEJS_12: return 63;
            case KOTLIN: return 78;
            case LUA: return 64;
            case OBJECTIVE_C: return 79;
            case PASCAL: return 67;
            case PERL: return 85;
            case PHP_7: return 68;

            case PYTHON_2: return 70;
            case PYTHON_3: return 71;

            case R: return 80;
            case RUBY: return 72;
            case RUST: return 73;
            case SCALA: return 81;
            case SWIFT: return 83;
            case TYPESCRIPT: return 74;
            case VB_NET: return 84;

            default: return -1;
        }
    }

    public LanguageList translateString2Language(String s) {
        try { return LanguageList.valueOf(s); }
        catch(IllegalArgumentException e) {
            log.warn("Couldn't translate string 2 language: " + s);
            return null;
        }
    }

    public short translateString2Id(String s) {
        try { return translateLanguage2Id(LanguageList.valueOf(s)); }
        catch(IllegalArgumentException e) {
            log.warn("Couldn't translate string 2 language id: " + s);
            return -1;
        }
    }

    @SneakyThrows
    private JSONObject queryResult(String submissionsToken) {
        int againCount = 0;
        JSONObject object;

        if(submissionsToken == null) return null;

        while(true) {
            object = requestSenderService.getRequestJSON(JUDGE0_HOST + "/submissions/" + submissionsToken);
            try {
                if(object.getJSONObject("status").getInt("id") == 1 || object.getJSONObject("status").getInt("id") == 2) againCount++;
                else { log.info(" * Running completed: " + object.toString()); return object; }
            }

            catch(JSONException e) { log.error(" * Cannot execute, response is: " + object.toString()); }
        }
    }

    @SneakyThrows
    private String createSubmissions(String source, JSONObject parma) {
        codeCacheService.automaticDelete_outdated();
        parma.put("source_code", source);
        JSONObject response = requestSenderService.postRequestJSON(JUDGE0_HOST + "/submissions", parma);

        try { return response.getString("token"); }
        catch(JSONException e) { log.error(" * Couldn't run code: " + response.toString()); return null; }
    }

    @Override
    @SneakyThrows
    public JSONObject read2executeProgram(String classpath, LanguageList language) {
        codeCacheService.automaticDelete_outdated();
        JSONObject object = new JSONObject(); object.put("language_id", translateLanguage2Id(language));
        return queryResult(createSubmissions(IOReader.read(new ClassPathResource(classpath).getInputStream()), object));
    }

    @Override
    public JSONObject executeProgram(String sourceCode, LanguageList language) {
        JSONObject object = new JSONObject(); object.put("language_id", translateLanguage2Id(language));
        return queryResult(createSubmissions(sourceCode, object));
    }

    @Override
    @SneakyThrows
    public JSONObject read2executeProgram(String classpath, short languageId) {
        codeCacheService.automaticDelete_outdated();
        JSONObject object = new JSONObject(); object.put("language_id", languageId);
        return queryResult(createSubmissions(IOReader.read(new ClassPathResource(classpath).getInputStream()), object));
    }

    @Override
    public JSONObject executeProgram(String sourceCode, short languageId) {
        codeCacheService.automaticDelete_outdated();
        JSONObject object = new JSONObject(); object.put("language_id", languageId);
        return queryResult(createSubmissions(sourceCode, object));
    }

    @Override
    @SneakyThrows
    public JSONObject read2executeProgram(String classpath, LanguageList language, String stdin) {
        codeCacheService.automaticDelete_outdated();
        JSONObject object = new JSONObject();
        object.put("language_id", translateLanguage2Id(language)); object.put("stdin", stdin);
        return queryResult(createSubmissions(IOReader.read(new ClassPathResource(classpath).getInputStream()), object));
    }

    @Override
    public JSONObject executeProgram(String sourceCode, LanguageList language, String stdin) {
        codeCacheService.automaticDelete_outdated();
        JSONObject object = new JSONObject();
        object.put("language_id", translateLanguage2Id(language)); object.put("stdin", stdin);
        return queryResult(createSubmissions(sourceCode, object));
    }

    @Override
    @SneakyThrows
    public JSONObject read2executeProgram(String classpath, short languageId, String stdin) {
        codeCacheService.automaticDelete_outdated();
        JSONObject object = new JSONObject();
        object.put("language_id", languageId); object.put("stdin", stdin);
        return queryResult(createSubmissions(IOReader.read(new ClassPathResource(classpath).getInputStream()), object));
    }

    @Override
    public JSONObject executeProgram(String sourceCode, short languageId, String stdin) {
        codeCacheService.automaticDelete_outdated();
        JSONObject object = new JSONObject();
        object.put("language_id", languageId); object.put("stdin", stdin);
        return queryResult(createSubmissions(sourceCode, object));
    }
}

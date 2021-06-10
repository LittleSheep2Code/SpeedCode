package SpeedCodeBKD.Services;

import org.json.JSONObject;

public interface CompileUtilsService {
    /*
     * This Service all function is based on Judge0 CE
     * We support all language of Judge0 CE
     * And we don't add any permission mode
     */

    enum LanguageList {
        NASM, BASH, BASIC,
        C_CLANG7, C_GCC7, C_GCC8, C_GCC9, CPP_CLANG7, CPP_GPP7, CPP_GPP8, CPP_GPP9, CSHARP,
        GOLANG, JAVA_13, NODEJS_12, KOTLIN, LUA, OBJECTIVE_C, PASCAL, PERL, PHP_7, PYTHON_2, PYTHON_3,
        R, RUBY, RUST, SCALA, SWIFT, TYPESCRIPT, VB_NET
    }

    short translateLanguage2Id(LanguageList language);
    short translateString2Id(String s);
    LanguageList translateString2Language(String s);

    JSONObject read2executeProgram(String classpath, LanguageList language);
    JSONObject executeProgram(String sourceCode, LanguageList language);
    JSONObject read2executeProgram(String classpath, short languageId);
    JSONObject executeProgram(String sourceCode, short languageId);

    JSONObject read2executeProgram(String classpath, LanguageList language, String stdin);
    JSONObject executeProgram(String sourceCode, LanguageList language, String stdin);
    JSONObject read2executeProgram(String classpath, short languageId, String stdin);
    JSONObject executeProgram(String sourceCode, short languageId, String stdin);
}

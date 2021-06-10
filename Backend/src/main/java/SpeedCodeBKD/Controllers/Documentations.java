package SpeedCodeBKD.Controllers;

import SpeedCodeBKD.Utils.Processor.IOReader;
import SpeedCodeBKD.Utils.Processor.ResultProcessor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
@RestController
public class Documentations {

    @ResponseBody
    @GetMapping(value = "/documentations/{uniqueIdentifier}", produces = "application/json; charset=UTF-8")
    private String AutoloadDocumentations(@PathVariable(value = "uniqueIdentifier") String uniqueIdentifier) {
        uniqueIdentifier = uniqueIdentifier.replace("@", "/");
        InputStream agreementIOStream;

        agreementIOStream = getClass().getClassLoader().getResourceAsStream("static/document/config/autoload/" + uniqueIdentifier + ".json");

        String agreementDocsString;
        JSONObject agreementObject;
        try {
            agreementObject = new JSONObject(IOReader.read(agreementIOStream));
            agreementDocsString = IOReader.read(getClass().getClassLoader().getResourceAsStream(agreementObject.getString("documentation")));
        }

        catch(IOException e) { return ResultProcessor.warn_response("Cannot read documentation", "DocumentationReader"); }

        JSONObject result = new JSONObject();
        result.put("contents", agreementDocsString);
        result.put("date", agreementObject.get("releasesDate"));
        result.put("ver", agreementObject.get("releaseVer"));

        return ResultProcessor.completed_response(result, "getUserAgreement");
    }
}

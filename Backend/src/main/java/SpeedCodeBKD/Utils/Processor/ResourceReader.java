package SpeedCodeBKD.Utils.Processor;

import org.json.JSONObject;

import java.io.InputStream;

public class ResourceReader {

    public static InputStream InputStreamReader(String path) {
        return ResourceReader.class.getClassLoader().getResourceAsStream(path);
    }

    public static String TemplateReader(String settings_path) {
        String path = "";

        try { IOReader.read(ResourceReader.class.getClassLoader().getResourceAsStream(settings_path)); }
        catch(Exception e) { throw new NullPointerException("(ResourceReader): Cannot load " + settings_path + " template configure"); }

        path = new JSONObject(path).getString("documentation");

        try { return IOReader.read(ResourceReader.class.getClassLoader().getResourceAsStream(path)); }
        catch(Exception e) { throw new NullPointerException("(ResourceReader): Cannot load " + path + " template content"); }
    }

    public static String StringReader(String path) {
        try { return IOReader.read(ResourceReader.class.getClassLoader().getResourceAsStream(path)); }
        catch(Exception e) { throw new NullPointerException("(ResourceReader): Cannot load " + path + " resource"); }
    }
}

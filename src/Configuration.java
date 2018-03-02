import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

public class Configuration {

    private final static String pathname = System.getProperty("user.dir") + "\\conf.txt";

    public void save(String config) throws IOException {
        if (!new File(pathname).exists())
            new File(pathname).createNewFile();
        PrintWriter printWriter = new PrintWriter(pathname);
        printWriter.println(config);
        printWriter.close();

    }

    public String get(String id) throws IOException {
        File file = new File(pathname);
        if (file.exists()) {
            String jsonConfiguration = readFile(pathname, Charset.defaultCharset());
            return (String) new Gson().fromJson(jsonConfiguration, Map.class).get(id);
        }
        return "";
    }



    static String readFile(String path, Charset encoding)
            throws IOException
    {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }

}

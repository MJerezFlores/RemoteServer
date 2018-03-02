import portmapper.*;
import address.*;
import spark.Request;
import spark.Response;
import spark.Spark;

import javax.servlet.ServletOutputStream;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;

import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;

public class main {

    static Configuration configuration = new Configuration();
    static int PORT = 9090;
    static String ADDRESS= "localhost";

    public static void main(String[] args) throws Exception{

        InetAddress local = new AddressManager().getExternalAddress();
        new UpnpPortMapper().openUpnpPorts(local, PORT);
        port(PORT);
        Spark.staticFiles.location("/public");
        Spark.staticFiles.externalLocation(configuration.get("path"));

        get("/hello", (req, res) ->{
            return "Hello";
        });


        post("set/configuration", (req, res) -> {
            configuration.save(req.body());
            return "";
        });

        get("*", main::streamFile);

        if(Desktop.isDesktopSupported())
        {
            Desktop.getDesktop().browse(new URI("http://"+ADDRESS+":"+PORT+"/index.html"));
        }


        new Thread(new IPSender(local.getHostAddress(), configuration)).run();


    }

    private static Object streamFile(Request req, Response res) throws IOException {
        File file = new File(configuration.get("path") + req.uri().replace("/", "\\").replace("%20", " "));
        FileInputStream fin = new FileInputStream(file);
        byte b[] = new byte[(int)file.length()];
        fin.read(b);
        ServletOutputStream fw = res.raw().getOutputStream();
        fw.write(b);
        fw.flush();
        fw.close();
        return res.raw();
    }
}

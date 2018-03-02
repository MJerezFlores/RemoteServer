import address.AddressManager;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.URL;

public class IPSender implements Runnable {
    private Configuration configuration;
    private String local;
    private String lastIp ="";
    public String EXTERNAL_ADDRESS = "localhost";
    public String EXTERNAL_PORT = "7070";

    public IPSender(String local, Configuration configuration) {
        this.configuration = configuration;
        this.local = local;
    }

    @Override
    public void run() {
        configurationWait();
        try {
            sendIP(makeIPObject(), 1500);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void sendIP(String ipRawObject, int delay) {
        while(true) {
            try {
                if(!lastIp.equals(ipRawObject)) {
                    Request.Post("http://"+EXTERNAL_ADDRESS+":"+EXTERNAL_PORT+"/api/add-ip/"+configuration.get("nickname"))
                            .bodyString(ipRawObject,ContentType.APPLICATION_JSON)
                            .execute().returnContent();
                    lastIp = ipRawObject;
                }
                Thread.sleep(delay);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }






    private void configurationWait() {
        try {
            while (configuration.get("nickname").equals("")) {
                Thread.sleep(2500);
            }
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }


    private String makeIPObject() throws IOException {
        URL whatismyip = new URL("http://checkip.amazonaws.com");
        BufferedReader in = new BufferedReader(new InputStreamReader(
                whatismyip.openStream()));

        return jsonIP(local, in.readLine());
    }

    private String jsonIP(String localIP, String remoteIP) {
        return "{" +
                "\"local\": \""+ localIP +"\"," +
                "\"remote\": \""+ remoteIP +"\"" +
                "}";
    }

}
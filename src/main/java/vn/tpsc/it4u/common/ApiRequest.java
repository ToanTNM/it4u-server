package vn.tpsc.it4u.common;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONObject;
import org.springframework.stereotype.Component;
@Component
public class ApiRequest {

    public String getRequestApi(String urlIt4u,String infoApi,String csrfToken,String unifises) {
        try {
            URL url = new URL( urlIt4u + infoApi);
            URLConnection conn = url.openConnection();
            conn.setRequestProperty("Cookie", "csrf_token=" + csrfToken + "; unifises=" + unifises);
            conn.connect();
            InputStream inputStream = conn.getInputStream();
            StringBuilder build = new StringBuilder();
                if (inputStream != null) {
                    InputStreamReader ISreader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
                    BufferedReader reader = new BufferedReader(ISreader);
                    String line = reader.readLine();
                    while (line != null) {
                        build.append(line);
                        line = reader.readLine();
                    }
                }
            String jsonString = build.toString();
            return jsonString;
            
        } catch (Exception e) {
            System.out.println("===============ERROR===============\n" + e.getMessage() + "\n\n\n"); 
            return e.getMessage();
        } 
        // return jo;
    }
    public String postRequestApi(String urlIt4u,String infoApi,String csrfToken,String unifises,String dataPost) {
        try {
            byte[] out = "{\"start\":\"1576515600000\",\"end\":\"1577206800000\"}" .getBytes(StandardCharsets.UTF_8);
            int length = out.length;
            URL url = new URL( urlIt4u + infoApi);
            JSONObject data = new JSONObject();
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            // conn.setFixedLengthStreamingMode(length);
            conn.setRequestProperty("Cookie", "csrf_token=" + csrfToken + "; unifises=" + unifises);
            conn.connect();
            // byte[] out = dataPost.getBytes(StandardCharsets.UTF_8);
            try(OutputStream os = conn.getOutputStream()) {
                os.write(out);
            }
            InputStream inputStream = conn.getInputStream();
            StringBuilder build = new StringBuilder();
                if (inputStream != null) {
                    InputStreamReader ISreader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
                    BufferedReader reader = new BufferedReader(ISreader);
                    String line = reader.readLine();
                    while (line != null) {
                        build.append(line);
                        line = reader.readLine();
                    }
                }
            String jsonString = build.toString();
            return jsonString;
            
        } catch (Exception e) {
            System.out.println("===============ERROR===============\n" + e.getMessage() + "\n\n\n"); 
            return e.getMessage();
        } 
    }
}
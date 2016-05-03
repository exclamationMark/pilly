package co.pilly.pilly;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Davide on 03/05/2016.
 * <p>
 * Let's tidy up the code by putting all Android's networking bullshit in here
 */
public class JessicaFetcher implements Runnable {
    public JessicaFetcher(String userId) {
        this.userId = userId;
    }

    @Override
    public void run() {
        try {
            URL url = new URL("http://api.pilly.co:5000/status/" + userId);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            try {
                InputStream inputStream = new BufferedInputStream(httpURLConnection.getInputStream());
                String JSON = readStream(inputStream);
                System.out.println(JSON);
                Data = new JSONObject(JSON);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                httpURLConnection.disconnect();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String readStream(InputStream is) {
        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            int i = is.read();
            while (i != -1) {
                bo.write(i);
                i = is.read();
            }
            return bo.toString();
        } catch (IOException e) {
            return "";
        }
    }

    JSONObject Data = null;
    final String userId;
}

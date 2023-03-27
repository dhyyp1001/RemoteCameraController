package network_resurce;

import android.util.Log;

import com.example.remotecameracontroller.UrlList;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class OnvifPTZControllerConnection {
    final String TAG = "OnvifPTZControllerConnection : ";

    public OnvifPTZControllerConnection(String uriString) {
        try {
            String requestUri = uriString;
            URL url = new URL(new UrlList().onvifControllerUrl());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);

            DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
            dataOutputStream.write(requestUri.getBytes());
            dataOutputStream.flush();
            dataOutputStream.close();

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                Log.i(TAG, "PTZ OK");
            } else {
                Log.i(TAG, "PTZ FAIL");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

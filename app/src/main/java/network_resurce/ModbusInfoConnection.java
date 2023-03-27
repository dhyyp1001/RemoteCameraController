package network_resurce;

import android.util.Log;

import com.example.remotecameracontroller.UrlList;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ModbusInfoConnection {
    final String TAG = "NetworkConnection : ";

    public ModbusInfoConnection(String uriString) {
        try {
            String requestUri = uriString; //uriString 으로 교체하기
            URL url = new URL(new UrlList().modbusConnectionUrl());
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
                Log.i(TAG, "HTTP OK");
            } else {
                Log.i(TAG, "HTTP FAIL");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

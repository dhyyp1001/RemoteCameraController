package network_resurce;

import android.util.Log;

import com.example.remotecameracontroller.UrlList;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class PostLoginData {
    private static final String TAG = "PostLoginData : ";
    public String response = "";
    public String okSign = "";
    public static String listValues = "";

    public PostLoginData(String id, String pw) {
        try {
            String[]responseArr;
            String userName = id;
            String password = pw;
            URL url = new URL(new UrlList().postLoginDataUrl());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);

            DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
            dataOutputStream.write(userName.getBytes());
            dataOutputStream.write(" ".getBytes());
            dataOutputStream.write(password.getBytes());
            dataOutputStream.flush();

            DataInputStream dataInputStream = new DataInputStream(connection.getInputStream());
            byte[] buffer = new byte[1024];
            int length;
            while ((length = dataInputStream.read(buffer)) > 0) {
                response += new String(buffer, 0, length);
            }
            dataOutputStream.close();
            dataInputStream.close();

            responseArr = response.split("//////");
            okSign = responseArr[0];
            listValues = responseArr[1];//리스트값

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                Log.i(TAG, "Login data OK");
            } else {
                Log.i(TAG, "Login data FAIL");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

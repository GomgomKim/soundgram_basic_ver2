package test.dahun.mobileplay.database;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

public class HttpRequest {

    private final String USER = "root";
    private final String PASSWORD = "1Howtobiz!";
    private  String song_list_url = "http://211.251.236.130:9000/api/song/getList/";
    private final String AUTHHEADER = USER + ":" + PASSWORD;

    HashMap<String, String> params;

    public HttpRequest() {

    }

    public void getSongList() {
        AsyncTask.execute(() -> {
            try {
                Authenticator.setDefault(new Authenticator(){
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(USER, PASSWORD.toCharArray());
                    }});

                URL api_url = new URL(song_list_url);
                HttpURLConnection httpCon = (HttpURLConnection) api_url.openConnection();
                httpCon.setReadTimeout(10000);
                httpCon.setConnectTimeout(15000);
                httpCon.setRequestProperty("User-Agent", "soundgram");
                httpCon.setRequestMethod("POST");
                httpCon.setDoInput(true);
                httpCon.setDoOutput(true);
                httpCon.setUseCaches(false);
                httpCon.setRequestProperty("authorization", AUTHHEADER);
                //starts the query
                httpCon.connect();
                int response = httpCon.getResponseCode();
                if(response == 200){
                    Log.i("gomgomKim", "success");
                    params.put("albumID", "3");

                    StringBuilder sbParams = new StringBuilder();
                    int i = 0;
                    for (String key : params.keySet()) {
                        try {
                            if (i != 0){
                                sbParams.append("&");
                            }
                            sbParams.append(key).append("=")
                                    .append(URLEncoder.encode(params.get(key), "UTF-8"));

                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        i++;
                    }

                    String paramsString = sbParams.toString();

                    DataOutputStream wr = new DataOutputStream(httpCon.getOutputStream());
                    wr.writeBytes(paramsString);
                    wr.flush();
                    wr.close();


                    try {
                        InputStream in = new BufferedInputStream(httpCon.getInputStream());
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                        StringBuilder result = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            result.append(line);
                        }

                        Log.i("gomgomKim", "result from server: " + result.toString());

                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (httpCon != null) {
                            httpCon.disconnect();
                        }
                    }

                } else{
                    Log.i("gomgomKim", "request error : "+response);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }





}

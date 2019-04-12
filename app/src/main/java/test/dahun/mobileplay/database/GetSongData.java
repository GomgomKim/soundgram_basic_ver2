package test.dahun.mobileplay.database;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

class GetSondData extends AsyncTask<String, Void, String>{

    ProgressDialog progressDialog;
    String errorString = null;

    String TAG = "gomgomKim";
    String mJsonString = "";

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        /*progressDialog = ProgressDialog.show(MainActivity.this,
                "Please Wait", null, true, true);*/
    }


    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

//        progressDialog.dismiss();
        Log.d(TAG, "response - " + result);

        if (result == null){
            Log.d(TAG, "null result");
        }
        else {
            mJsonString = result;
            showResult();
        }
    }


    @Override
    protected String doInBackground(String... params) {

        String serverURL = params[0];
        String postParameters = params[1];


        try {

            URL url = new URL(serverURL);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


            httpURLConnection.setReadTimeout(5000);
            httpURLConnection.setConnectTimeout(5000);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoInput(true);
            httpURLConnection.connect();


            OutputStream outputStream = httpURLConnection.getOutputStream();
            outputStream.write(postParameters.getBytes("UTF-8"));
            outputStream.flush();
            outputStream.close();


            int responseStatusCode = httpURLConnection.getResponseCode();
            Log.d(TAG, "response code - " + responseStatusCode);

            InputStream inputStream;
            if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                inputStream = httpURLConnection.getInputStream();
            }
            else{
                inputStream = httpURLConnection.getErrorStream();
            }


            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            StringBuilder sb = new StringBuilder();
            String line;

            while((line = bufferedReader.readLine()) != null){
                sb.append(line);
            }

            bufferedReader.close();

            return sb.toString().trim();


        } catch (Exception e) {

            Log.d(TAG, "GetData : Error ", e);
            errorString = e.toString();

            return null;
        }

    }

    private void showResult(){

        String TAG_JSON="webnautes";
        String TAG_ID = "id";
        String TAG_NAME = "name";
        String TAG_COUNTRY ="country";


        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for(int i=0;i<jsonArray.length();i++){

                JSONObject item = jsonArray.getJSONObject(i);

                String id = item.getString(TAG_ID);
                String name = item.getString(TAG_NAME);
                String country = item.getString(TAG_COUNTRY);
            }



        } catch (JSONException e) {

            Log.d(TAG, "showResult : ", e);
        }

    }
}

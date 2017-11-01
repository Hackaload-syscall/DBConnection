package com.example.leesangwook.dbconnection;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Main2Activity extends AppCompatActivity {

    private static String TAG = "PHPTEST_MAIN_ACTIVITY";

    private EditText EditTextID;
    private EditText EditTextName;
    private EditText EditTextAge;
    private TextView TextViewResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        EditTextID = (EditText) findViewById(R.id.edittext_id);
        EditTextName = (EditText) findViewById(R.id.edittext_name);
        EditTextAge = (EditText) findViewById(R.id.edittext_age);
        TextViewResult = (TextView) findViewById(R.id.textview_result);

//        Button ButtonInsert = (Button) findViewById(R.id.button_insert);
    }

    class InsertData extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;

        protected void onPreExecute() {

            super.onPreExecute();

            progressDialog = ProgressDialog.show(Main2Activity.this, "Please Wait", null, true, true);
        }

        protected void onPostExecute(String result) {

            super.onPostExecute(result);

            progressDialog.dismiss();
            TextViewResult.setText(result);
//            Log.d(TAG, "Post response - " + result);
        }


        @Override
        protected String doInBackground(String... params) {

            String id = (String) params[0];
            String name = (String) params[1];
            String age = (String) params[2];

            String serverURL = "http://52.79.165.228/insert.php";
            String postParameters = "id=" + id + "&name=" + name + "&age=" + age;

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

                InputStream inputStream;

                if (responseStatusCode == HttpURLConnection.HTTP_OK) {

                    inputStream = httpURLConnection.getInputStream();

                } else {

                    inputStream = httpURLConnection.getErrorStream();
                }

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line = null;

                while((line = bufferedReader.readLine()) != null) {

                    sb.append(line);
                }

                bufferedReader.close();

                Log.e("test", sb.toString());

                return sb.toString();

            } catch(Exception e) {

                return new String("Error: " + e.getMessage());
            }
        }
    }

    public void onClick2(View view) {

        String id = EditTextID.getText().toString();
        String name = EditTextName.getText().toString();
        String age = EditTextAge.getText().toString();

        InsertData task = new InsertData();
        task.execute(id, name, age);

        EditTextID.setText("");
        EditTextName.setText("");
        EditTextAge.setText("");
    }
}

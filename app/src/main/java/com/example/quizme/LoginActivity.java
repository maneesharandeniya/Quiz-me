package com.example.quizme;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText userName,password;
    private TextInputLayout user,pass;
    String userText,passText;

    Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //text field
        userName = findViewById(R.id.name);
        password = findViewById(R.id.pass);

        //layout
        user = findViewById(R.id.loginUsername);
        pass = findViewById(R.id.loginPassword);
        button = findViewById(R.id.loginBtn);
        
    }
    public void goReg(View v){

        Intent intent = new Intent(this,SignUpActivity.class);
        this.startActivity(intent);

    }

    private boolean validateFields() {

        userText = userName.getText().toString();
        passText = password.getText().toString();

        if (userText.isEmpty()) {
            user.setError("User Name can't be Empty");
            return false;
        }

        if (passText.isEmpty()) {
            pass.setError("Password can't be Empty");
            return false;
        }

        return true;
    }

    public void submitLogin(View view){

        if(!validateFields()){
            return;
        }

        final String userName = userText;
        final String password = passText;

        WebRequest webRequest = new WebRequest(this);
        webRequest.execute(userName,password);


    }

    private class WebRequest extends AsyncTask<String,String,String> {

        Context con;
        public WebRequest(Context con){
            this.con=con;
        }


        @Override
        protected String doInBackground(String... strings) {

            OkHttpClient client = new OkHttpClient();
            MediaType Json = MediaType.parse("application/json;charset=utf-8");
            JSONObject data = new JSONObject();
            String val = "";

            try {
                data.put("userName", strings[0]);
                data.put("password", strings[1]);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.i("data", data.toString());

            RequestBody body = RequestBody.create(data.toString(), Json);

            Request request = new Request.Builder().url(
                    "https://quizmeonline.herokuapp.com/all/login"
            ).post(body).build();

            Response response = null;
            String responseBody = null;
            JSONObject json = null;

            try {
                response = client.newCall(request).execute();
                responseBody = response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(responseBody.equals("user not found")){
                return "user not found";
            }
            else if(responseBody.equals("Incorrect userName or Password.")){
                return "Incorrect userName or Password.";
            }
            if(response.code()==200) {
                try {
                    json = new JSONObject(response.body().string());
                    val = json.getString("jwt");
                } catch ( Exception e) {
                    e.printStackTrace();
                }
            }else{
                return null;
            }


            return val;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if(s==null){
                Toast toast=Toast.makeText(con, "Something Went Wrong Try Again Later!", Toast.LENGTH_SHORT);
                toast.show();
            }
            else if(s.equals("Incorrect userName or Password.")){
                Toast toast=Toast.makeText(con, "Incorrect userName or Password.", Toast.LENGTH_SHORT);
                toast.show();
            }
            else if(s.equals("user not found")){
                Toast toast=Toast.makeText(con, "New User?Sign UP", Toast.LENGTH_SHORT);
                toast.show();
            }
            else {

                try {

                    SharedPreferences pref = con.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("jwt", s);
                    editor.commit();
                    Intent intent = new Intent(con, MainActivity.class);
                    con.startActivity(intent);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }
}






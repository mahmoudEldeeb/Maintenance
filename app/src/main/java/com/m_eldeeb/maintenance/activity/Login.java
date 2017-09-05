package com.m_eldeeb.maintenance.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.m_eldeeb.maintenance.MainActivity;
import com.m_eldeeb.maintenance.R;
import com.m_eldeeb.maintenance.interfaces.GetData;
import com.m_eldeeb.maintenance.services.FCMRegistrationService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class Login extends AppCompatActivity {

    @InjectView(R.id.loginEmail)
    EditText loginEmail;
    @InjectView(R.id.loginPassword)
    EditText loginPassword;
    @InjectView(R.id.registerText)
    TextView registerText;
    @InjectView(R.id.error)
    TextView error;
    @InjectView(R.id.logiBbutton)
    Button logiBbutton;

    @InjectView(R.id.progressBar3)
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        ButterKnife.inject(this);
        declareSharedPrefrenceValues();
        boolean logined = getBaseContext().getSharedPreferences("PREFERENCE", Context.MODE_PRIVATE)
                .getBoolean("logined", false);


        if (logined==true) {

            Intent intent = new Intent(Login.this, MainActivity.class);
            startActivity(intent);
        }

        logiBbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!loginEmail.getText().toString().isEmpty() && !loginPassword.getText().toString().isEmpty()) {
                    login();
                } else {
                    error.setText(R.string.fill_all_record);
                    error.setVisibility(View.VISIBLE);
                }

            }
        });
        registerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
            }
        });
    }

    public void login() {
        progressBar.setVisibility(View.VISIBLE);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.BAS_URL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final Call<ResponseBody> connection;
        GetData loginObgect = retrofit.create(GetData.class);
        connection = loginObgect.login(loginEmail.getText().toString(), loginPassword.getText().toString());
        connection.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressBar.setVisibility(View.INVISIBLE);

                String RegisterResult = null;

                try {
                    RegisterResult = response.body().string();

                    JSONObject jso = new JSONObject(RegisterResult);
                    String code = jso.getString("code");

                    if(code.equals("200")){
                        JSONObject data = new JSONObject(jso.getString("data"));
                        String id=data.getString("userId");
                        String name=data.getString("userName");
                        String date=data.getString("notificationDate");
                        String time=data.getString("time");

                        SharedPreferences.Editor editor = getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit();
                        editor.putString("id",id);
                        editor.putString("notificationDate", date);
                        editor.putString("userName",name);
                        editor.putBoolean("logined", true);
                        editor.commit();

                        Intent service_intent = new Intent(Login.this, FCMRegistrationService.class);

                        startService(service_intent);
                        Intent intent = new Intent(Login.this, MainActivity.class);
                        startActivity(intent);

                    } else {
                        error.setVisibility(View.VISIBLE);
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);

                Toast.makeText(getBaseContext(), getText(R.string.something_wrong), Toast.LENGTH_SHORT).show();


            }
        });

    }
    @Override
    public void onBackPressed()
    {
        this.finish();
        super.onBackPressed();  // optional depending on your needs
    }    public void declareSharedPrefrenceValues() {

        SharedPreferences  preferences = getApplicationContext().getSharedPreferences("PREFERENCE", 0);
        SharedPreferences.Editor editor = preferences.edit();


        preferences.getString("id", "-1");
        preferences.getString("notificationDate", "16-6-2017");
        preferences.getInt("time", 0);
        preferences.getString("userName","null");

        preferences.getString("lang","eng");
        preferences.getBoolean("logined", false);
        preferences.getInt("notifyId", 0);
        preferences.getBoolean("hour", false);
        preferences.getBoolean("finsh", false);

        editor.apply();
    }
}
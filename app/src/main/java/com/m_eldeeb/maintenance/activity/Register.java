package com.m_eldeeb.maintenance.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.iid.FirebaseInstanceId;
import com.m_eldeeb.maintenance.MainActivity;
import com.m_eldeeb.maintenance.R;
import com.m_eldeeb.maintenance.helper;
import com.m_eldeeb.maintenance.interfaces.GetData;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.ButterKnife;
import butterknife.InjectView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class Register extends AppCompatActivity {

    @InjectView(R.id.name)
    EditText name;
    @InjectView(R.id.email)
    EditText email;
    @InjectView(R.id.password)
    EditText password;
    @InjectView(R.id.phone)
    EditText phone;
    @InjectView(R.id.register)
    Button register;
    @InjectView(R.id.progressBar2)
    ProgressBar progressBar;

    @InjectView(R.id.emailerror)
    TextView error_email;
    @InjectView(R.id.passerror)
    TextView error_password;

    String spinner_item="0";
    String myFormat = "dd-MM-yyyy"; //In which you need put here
    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
String date;
    String token ="";

    String timeZone="" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.inject(this);
        timeZone= String.valueOf(TimeZone.getDefault().getID());

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(new helper(getBaseContext()).isOnline())
                {
                token=FirebaseInstanceId.getInstance().getToken();

                if(name.getText().toString().isEmpty()||
                        token==null
                        ||phone.getText().toString().isEmpty()
                        ||email.getText().toString().isEmpty()||timeZone.isEmpty()||
        password.getText().toString().isEmpty()||spinner_item.isEmpty()){
    Toast.makeText(getBaseContext(),getText(R.string.fill),Toast.LENGTH_SHORT).show();
}
else if(!isValidEmail(email.getText().toString())){
              error_email.setVisibility(View.VISIBLE);

                }
else if(!isValidPassword( password.getText().toString())){
                    error_password.setVisibility(View.VISIBLE);
                }
else   {
  register();
}


}

                else{

                    Toast.makeText(getBaseContext(), getText(R.string.something_wrong), Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
    public static boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=\\S+$).{8,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }
    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
    public void register() {
        progressBar.setVisibility(View.VISIBLE);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.BAS_URL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final Call<ResponseBody> connection;
        GetData registerFunction = retrofit.create(GetData.class);

        connection = registerFunction.register(name.getText().toString(),phone.getText().toString(), email.getText().toString(),
                password.getText().toString(),date,spinner_item,token,timeZone);
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

                        SharedPreferences.Editor editor = getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit();
                        editor.putString("id",id);
                        editor.putString("notificationDate", date);
                        editor.putInt("time", Integer.parseInt(spinner_item));
                        editor.putString("userName",name.getText().toString());
                        editor.putBoolean("logined", true);
                        editor.commit();

                       // Intent service_intent = new Intent(getBaseContext(), AlarmService.class);

                       // getBaseContext().startService(service_intent);
                        Intent intent = new Intent(Register.this, MainActivity.class);
                        startActivity(intent);
                    }
                    else {  String error = jso.getString("msg");
                        Toast.makeText(getBaseContext(), error, Toast.LENGTH_SHORT).show();

                    }
                    } catch (JSONException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                catch (NullPointerException e){
                    Toast.makeText(getBaseContext(), getText(R.string.something_wrong), Toast.LENGTH_SHORT).show();

                }




            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(getBaseContext(), getText(R.string.something_wrong), Toast.LENGTH_SHORT).show();
            }
        });


    }
}


package com.m_eldeeb.maintenance;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.iid.FirebaseInstanceId;
import com.m_eldeeb.maintenance.activity.ChangeParts;
import com.m_eldeeb.maintenance.activity.Login;
import com.m_eldeeb.maintenance.activity.Parts;
import com.m_eldeeb.maintenance.activity.Register;
import com.m_eldeeb.maintenance.activity.UpdateHourWork;
import com.m_eldeeb.maintenance.fragments.AddItem;
import com.m_eldeeb.maintenance.fragments.Mymachine;
import com.m_eldeeb.maintenance.fragments.UpdateWorkedHoure;
import com.m_eldeeb.maintenance.interfaces.Communication;
import com.m_eldeeb.maintenance.interfaces.GetData;
import com.m_eldeeb.maintenance.models.MachineModel;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener , Communication {

    InterstitialAd interstitial;
    int close=0;
    String spinner_item ="";
    int result ;
    String languageToLoad;
    String myFormat = "dd-MM-yyyy"; //In which you need put here
    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String lang = getBaseContext().getSharedPreferences("PREFERENCE", Context.MODE_PRIVATE)
                .getString("lang", "eng");
        if(lang.equals("ar")){
         languageToLoad  = "ar"; // your language
             }
            else{  languageToLoad  = "eng";
            }
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());


        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();


        boolean finish = getBaseContext().getSharedPreferences("PREFERENCE", Context.MODE_PRIVATE)
                .getBoolean("finish", false);

if(finish) {
    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
    UpdateWorkedHoure fragment = new UpdateWorkedHoure();
    ft.replace(R.id.fragment, fragment);

    ft.commit();
}
else {
    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
    AddItem fragment = new AddItem();
    ft.replace(R.id.fragment, fragment);

    ft.commit();
}
    }

    @Override
    public void onBackPressed() {



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.my_machine) {
            final FragmentTransaction ft = getSupportFragmentManager()
                    .beginTransaction();

            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            Mymachine fragment = new Mymachine();
            ft.replace(R.id.fragment, fragment);

            ft.commit();


        }
        else if (id == R.id.add_item) {

            final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            AddItem fragment = new AddItem();
            ft.replace(R.id.fragment, fragment);

            ft.commit();
        }

        else if (id == R.id.main) {

            final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            UpdateWorkedHoure fragment = new UpdateWorkedHoure();
            ft.replace(R.id.fragment, fragment);

            ft.commit();
        }
        else if (id == R.id.language) {
String lan=Locale.getDefault().getDisplayLanguage();
            if(lan.equals("English"))
            {
                SharedPreferences.Editor editor = getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit();
                editor.putString("lang","ar");
                editor.commit();

            }
            else{
                SharedPreferences.Editor editor = getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit();
                editor.putString("lang","eng");
                editor.commit();
            }
            Intent i = getBaseContext().getPackageManager()
                    .getLaunchIntentForPackage( getBaseContext().getPackageName() );
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            finish();
            startActivity(i);

        }

        else if (id == R.id.nav_need_fix) {
Intent intent=new Intent(MainActivity.this, ChangeParts.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_logout) {
          if(new helper(getBaseContext()).isOnline()){
            sendTokenToServer("0000");}
            else {    Toast.makeText(getBaseContext(), getText(R.string.something_wrong), Toast.LENGTH_SHORT).show();
          }

        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void sendTokenToServer(final String token) {

        final ProgressDialog progressDialog=new ProgressDialog(MainActivity.this);
        progressDialog.setTitle(R.string.log_out);
        progressDialog.show();
        String id = getBaseContext().getSharedPreferences("PREFERENCE", Context.MODE_PRIVATE)
                .getString("id", "-1");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.BAS_URL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final Call<ResponseBody> connection;
        GetData getData = retrofit.create(GetData.class);

        connection = getData.updateToken(id, token);
        connection.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                String RegisterResult = null;
                try {
                    RegisterResult = response.body().string();

                    JSONObject jso = new JSONObject(RegisterResult);
                    String code = jso.getString("code");
                    if(code.equals("200")){

                        progressDialog.dismiss();
                        SharedPreferences.Editor editor = getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit();
                        editor.putBoolean("logined", false);
                        editor.putString("id","-1");
                        editor.apply();

                        Intent i=new Intent(MainActivity.this,Login.class);
                        startActivity(i);
                        finish();
                    }
                    else {  String error = jso.getString("msg");

                        progressDialog.dismiss();
                        SharedPreferences.Editor editor = getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit();
                        editor.putBoolean("logined", false);
                        editor.putString("id","-1");
                        editor.apply();

                        Intent i=new Intent(MainActivity.this,Login.class);
                        startActivity(i);
                        finish();
                        Toast.makeText(getBaseContext(), error, Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();


                Toast.makeText(getBaseContext(), getText(R.string.something_wrong), Toast.LENGTH_SHORT).show();


            }
        });
    }
    @Override
    public void onclick(MachineModel m) {
Intent intent=new Intent(MainActivity.this,Parts.class);
        intent.putExtra("machine",m);
        startActivity(intent);
    }
}

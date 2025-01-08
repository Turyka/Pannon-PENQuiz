package bk.mobilprog.penquiz;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import java.io.File;
import java.util.ArrayList;
import androidx.activity.EdgeToEdge;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import bk.mobilprog.penquiz.Server.Server;
import bk.mobilprog.penquiz.Services.DataBaseHelper;
import bk.mobilprog.penquiz.SplashScreen.SplashScreen;

public class MainActivity extends AppCompatActivity {
    private DataBaseHelper myDataBase;
    View rootLayout;
    // TODO: Megcsinálni az info button-t
    ImageView infobutton;
    //Declaráljuk a Gombokat
    Button Buttontheme1;
    Button Buttontheme2;
    Button Buttontheme3;
    Button Buttontheme4;
    Button Buttontheme5;
    Button Buttontheme6;
    Button Buttontheme7;
    Button Buttontheme8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        final Context context = this.getApplicationContext();
        //Gondolom majd ez publicos ip lesz?
        final Server server = new Server("192.168.33.5");
        //szál
        final Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try  {
                    server.DownloadFile("http://"+server.getIpAddress()+"/database/penquizdb.db",context);
                    File file = new File(getFilesDir(),"penquizdb.db");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
/*
        try{
            thread.start();
            thread.join();
        }catch (InterruptedException e){
            e.printStackTrace();
        }*/


        myDataBase = new DataBaseHelper(this);

        rootLayout = findViewById(R.id.main_layout);
        //infobutton = findViewById(R.id.infobutton);
/*
        infobutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Thread thread1 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Looper.prepare();
                        //Log.d("External IP:",server.externalIP().toString());
                        //Log.d("Valid? ",Boolean.toString(server.isValidIPAddress(server.externalIP(), "185.220.1.1", "185.222.254.255")));
                        if(server.isValidIPAddress(server.externalIP(),"185.220.1.1", "185.222.254.255")){
                            Toast.makeText(MainActivity.this, "IP címed megfelelő", Toast.LENGTH_SHORT).show();
                        }
                        else
                            Toast.makeText(MainActivity.this, "IP címed nem megfelelő", Toast.LENGTH_SHORT).show();
                    }
                });
                thread1.start();
            }
        });
*/
        //QuizActivityOpener onClickBtnListener = new QuizActivityOpener(0);
        //Typeface face = Typeface.createFromAsset(getAssets(), "trajanpro_regular.OTF");
        setSizeofItems(context);

    }

    private void setSizeofItems(Context context){

        Typeface face = Typeface.createFromAsset(getAssets(), "trajan_pro_bold.ttf");



        //Azonosítás
        Buttontheme1 = (Button) findViewById(R.id.theme1);
        Buttontheme1.setTypeface(face);
        Buttontheme2 = (Button) findViewById(R.id.theme2);
        Buttontheme2.setTypeface(face);
        Buttontheme3 = (Button) findViewById(R.id.theme3);
        Buttontheme3.setTypeface(face);
        Buttontheme4 = (Button) findViewById(R.id.theme4);
        Buttontheme4.setTypeface(face);
        Buttontheme5 = (Button) findViewById(R.id.theme5);
        Buttontheme5.setTypeface(face);
        Buttontheme6 = (Button) findViewById(R.id.theme6);
        Buttontheme6.setTypeface(face);
        Buttontheme7 = (Button) findViewById(R.id.theme7);
        Buttontheme7.setTypeface(face);
        Buttontheme8 = (Button) findViewById(R.id.theme8);
        Buttontheme8.setTypeface(face);




        FrameLayout theme1layout = (FrameLayout) findViewById(R.id.theme1_layout);
        FrameLayout theme2layout = (FrameLayout) findViewById(R.id.theme2_layout);
        FrameLayout theme3layout = (FrameLayout) findViewById(R.id.theme3_layout);
        FrameLayout theme4layout = (FrameLayout) findViewById(R.id.theme4_layout);
        FrameLayout theme5layout = (FrameLayout) findViewById(R.id.theme5_layout);
        FrameLayout theme6layout = (FrameLayout) findViewById(R.id.theme6_layout);
        FrameLayout theme7layout = (FrameLayout) findViewById(R.id.theme7_layout);
        FrameLayout theme8layout = (FrameLayout) findViewById(R.id.theme8_layout);


        LinearLayout button_layout = (LinearLayout) findViewById(R.id.button_layout);


        ImageView pendroid_logo = (ImageView) findViewById(R.id.pendroid_logo);
        ImageView pend_logo = (ImageView) findViewById(R.id.pen_logo);

        ArrayList<String> TopicNames = myDataBase.getThemesforButton();
        if (!TopicNames.isEmpty()) {
            Buttontheme1.setText(TopicNames.get(0));
            Buttontheme2.setText(TopicNames.get(1));
            Buttontheme3.setText(TopicNames.get(2));
            Buttontheme4.setText(TopicNames.get(3));
            Buttontheme5.setText(TopicNames.get(4));
            Buttontheme6.setText(TopicNames.get(5));
            Buttontheme7.setText(TopicNames.get(6));
            Buttontheme8.setText(TopicNames.get(7));
        }

        //Gombok háttere:
        /*
        theme1layout.setBackgroundResource(R.drawable.theme1_pen_button_grey);
        theme2layout.setBackgroundResource(R.drawable.theme_pe_705x1128_button);
        theme3layout.setBackgroundResource(R.drawable.theme3_turizmus_button_grey);
        theme4layout.setBackgroundResource(R.drawable.theme4_informatika_button_grey);
        theme5layout.setBackgroundResource(R.drawable.theme5_smart_button_grey);
        theme6layout.setBackgroundResource(R.drawable.theme6_nagykanizsa_button_grey);
        theme7layout.setBackgroundResource(R.drawable.theme7_vizes_button_grey);
        theme8layout.setBackgroundResource(R.drawable.theme8_zold_button_grey);
        */

        rootLayout.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onGlobalLayout() {
                        // Layout has happened here.
                        setOnClick(Buttontheme1, 1);
                        setOnClick(Buttontheme2, 2);
                        setOnClick(Buttontheme3, 3);
                        setOnClick(Buttontheme4, 4);
                        setOnClick(Buttontheme5, 5);
                        setOnClick(Buttontheme6, 6);
                        setOnClick(Buttontheme7, 7);
                        setOnClick(Buttontheme8, 8);

                        // Don't forget to remove your listener when you are done with it.
                        rootLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });



    }


    private void setOnClick(final Button btn, final int topicID){
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //System.out.println("MainActivity gomb click");

                Buttontheme1.setClickable(false);
                Buttontheme2.setClickable(false);
                Buttontheme3.setClickable(false);
                Buttontheme4.setClickable(false);
                Buttontheme5.setClickable(false);
                Buttontheme6.setClickable(false);
                Buttontheme7.setClickable(false);
                Buttontheme8.setClickable(false);

                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation(MainActivity.this, view, "transition");
                int[] img_coordinates = new int[2];
                btn.getLocationOnScreen(img_coordinates);
                //int revealX = (int) (view.getX() + btn.getWidth() / 2);
                //int revealY = (int) (view.getY() + btn.getHeight() / 2);
                int revealX = img_coordinates[0] + btn.getWidth()/2;
                int revealY = img_coordinates[1] + btn.getHeight()/2;

                Intent quizActivity = new Intent(MainActivity.this, QuizActivity.class);
                quizActivity.putExtra(QuizActivity.EXTRA_CIRCULAR_REVEAL_X, revealX);
                quizActivity.putExtra(QuizActivity.EXTRA_CIRCULAR_REVEAL_Y, revealY);
                quizActivity.putExtra("topicID", topicID);

                ActivityCompat.startActivity(MainActivity.this, quizActivity, options.toBundle());

                /*Intent quizActivity = new Intent(MainActivity.this, QuizActivity.class);
                quizActivity.putExtra("topicID", topicID);
                startActivity(quizActivity);*/
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent splashScreen = new Intent(MainActivity.this, SplashScreen.class);
        startActivity(splashScreen);
    }

}
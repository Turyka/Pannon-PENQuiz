package bk.mobilprog.penquiz;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;

import java.lang.reflect.Field;

import androidx.activity.EdgeToEdge;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import bk.mobilprog.penquiz.CoustomTextView.CoustomTextView;

public class ResultActivity extends AppCompatActivity {

    public static final String EXTRA_CIRCULAR_REVEAL_X = "EXTRA_CIRCULAR_REVEAL_X";
    public static final String EXTRA_CIRCULAR_REVEAL_Y = "EXTRA_CIRCULAR_REVEAL_Y";
    View rootLayout;
    private int revealX;
    private int revealY;
    float dpHeight;
    float dpWidth;
    Button okButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_result);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.result_root_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Animáció lekezelése
        final Intent intent = getIntent();
        rootLayout = findViewById(R.id.result_root_layout);
        if (savedInstanceState == null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP &&
                intent.hasExtra(EXTRA_CIRCULAR_REVEAL_X) &&
                intent.hasExtra(EXTRA_CIRCULAR_REVEAL_Y)) {

            rootLayout.setVisibility(View.INVISIBLE);

            revealX = intent.getIntExtra(EXTRA_CIRCULAR_REVEAL_X, 0);
            revealY = intent.getIntExtra(EXTRA_CIRCULAR_REVEAL_Y, 0);


            ViewTreeObserver viewTreeObserver = rootLayout.getViewTreeObserver();
            if (viewTreeObserver.isAlive()) {
                viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onGlobalLayout() {
                        revealActivity(revealX, revealY);
                        rootLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });
            }
        } else {
            rootLayout.setVisibility(View.VISIBLE);
        }
        //________


        Bundle extras = getIntent().getExtras();
        int CorrectAnswerNumber = extras.getInt("result");
        int topicID = extras.getInt("topicID");

        Typeface face = Typeface.createFromAsset(getAssets(), "trajan_pro_bold.ttf");

        CoustomTextView congratTW = (CoustomTextView) findViewById(R.id.result_congrat_text);
        CoustomTextView resultTW = (CoustomTextView) findViewById(R.id.result_result_text);
        String result = String.valueOf(CorrectAnswerNumber*10) + " %";
        resultTW.setText(result);

        okButton = (Button) findViewById(R.id.result_ok_button);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        congratTW.setTypeface(face);
        resultTW.setTypeface(face);
        okButton.setTypeface(face);

        Bitmap bitmap;
        DisplayMetrics displayMetrics = getApplicationContext().getResources().getDisplayMetrics();
        dpHeight = displayMetrics.heightPixels / displayMetrics.density;
        dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        switch(topicID){
            case 1:
                bitmap = imageOpen(R.drawable.theme_pen_1013x1800_bg);
                rootLayout.setBackgroundDrawable(new BitmapDrawable(getResources(),bitmap));
                okButton.setBackgroundResource(R.drawable.theme_buttoncolor);
                congratTW.setBackgroundResource(R.drawable.theme_buttoncolor);
                resultTW.setBackgroundResource(R.drawable.theme_buttoncolor);
                break;
            case 2:
                bitmap = imageOpen(R.drawable.theme_pe_705x1128);
                rootLayout.setBackgroundDrawable(new BitmapDrawable(getResources(),bitmap));
                okButton.setBackgroundResource(R.drawable.theme2_buttoncolor);
                congratTW.setBackgroundResource(R.drawable.theme2_buttoncolor);
                resultTW.setBackgroundResource(R.drawable.theme2_buttoncolor);
                break;
            case 3:
                bitmap = imageOpen(R.drawable.theme_turizmus_400x640_bg);
                rootLayout.setBackgroundDrawable(new BitmapDrawable(getResources(),bitmap));
                okButton.setBackgroundResource(R.drawable.theme3_buttoncolor);
                congratTW.setBackgroundResource(R.drawable.theme3_buttoncolor);
                resultTW.setBackgroundResource(R.drawable.theme3_buttoncolor);
                okButton.setTextColor(getResources().getColor(R.color.black));
                congratTW.setTextColor(getResources().getColor(R.color.black));
                resultTW.setTextColor(getResources().getColor(R.color.black));
                break;
            case 4:
                bitmap = imageOpen(R.drawable.theme_informatika_750x1200);
                rootLayout.setBackgroundDrawable(new BitmapDrawable(getResources(),bitmap));
                okButton.setBackgroundResource(R.drawable.theme4_buttoncolor);
                congratTW.setBackgroundResource(R.drawable.theme4_buttoncolor);
                resultTW.setBackgroundResource(R.drawable.theme4_buttoncolor);
                break;
            case 5:
                bitmap = imageOpen(R.drawable.theme_smart_493x720);
                rootLayout.setBackgroundDrawable(new BitmapDrawable(getResources(),bitmap));
                okButton.setBackgroundResource(R.drawable.theme5_buttoncolor);
                congratTW.setBackgroundResource(R.drawable.theme5_buttoncolor);
                resultTW.setBackgroundResource(R.drawable.theme5_buttoncolor);
                break;
            case 6:
                bitmap = imageOpen(R.drawable.theme6_nagykanizsa_bg_grey);
                rootLayout.setBackgroundDrawable(new BitmapDrawable(getResources(),bitmap));
                okButton.setBackgroundResource(R.drawable.theme6_buttoncolor);
                congratTW.setBackgroundResource(R.drawable.theme6_buttoncolor);
                resultTW.setBackgroundResource(R.drawable.theme6_buttoncolor);
                break;
            case 7:
                bitmap = imageOpen(R.drawable.theme7_vizes_bg_grey);
                rootLayout.setBackgroundDrawable(new BitmapDrawable(getResources(),bitmap));
                okButton.setBackgroundResource(R.drawable.theme7_buttoncolor);
                congratTW.setBackgroundResource(R.drawable.theme7_buttoncolor);
                resultTW.setBackgroundResource(R.drawable.theme7_buttoncolor);
                okButton.setTextColor(getResources().getColor(R.color.black));
                congratTW.setTextColor(getResources().getColor(R.color.black));
                resultTW.setTextColor(getResources().getColor(R.color.black));
                break;
            case 8:
                bitmap = imageOpen(R.drawable.theme8_zold_bg_grey);
                rootLayout.setBackgroundDrawable(new BitmapDrawable(getResources(),bitmap));
                okButton.setBackgroundResource(R.drawable.theme8_buttoncolor);
                congratTW.setBackgroundResource(R.drawable.theme8_buttoncolor);
                resultTW.setBackgroundResource(R.drawable.theme8_buttoncolor);
                okButton.setTextColor(getResources().getColor(R.color.black));
                congratTW.setTextColor(getResources().getColor(R.color.black));
                resultTW.setTextColor(getResources().getColor(R.color.black));
                break;

        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        //unRevealActivity();
        super.onBackPressed();
        Intent mainActivity = new Intent(ResultActivity.this, MainActivity.class);
        startActivity(mainActivity);
    }

    //Animációk:
    protected void revealActivity(int x, int y) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            float finalRadius = (float) (Math.max(rootLayout.getWidth(), rootLayout.getHeight()) * 1.1);

            // create the animator for this view (the start radius is zero)
            Animator circularReveal = ViewAnimationUtils.createCircularReveal(rootLayout, x, y, 0, finalRadius);
            circularReveal.setDuration(400);
            circularReveal.setInterpolator(new AccelerateInterpolator());

            // make the view visible and start the animation
            rootLayout.setVisibility(View.VISIBLE);
            circularReveal.start();
        } else {
            finish();
        }
    }

    protected void unRevealActivity() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            finish();
        } else {
            float finalRadius = (float) (Math.max(rootLayout.getWidth(), rootLayout.getHeight()) * 1.1);
            Animator circularReveal = ViewAnimationUtils.createCircularReveal(
                    rootLayout, revealX, revealY, finalRadius, 0);

            circularReveal.setDuration(400);
            circularReveal.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    rootLayout.setVisibility(View.INVISIBLE);
                    finish();
                }
            });


            circularReveal.start();
        }
    }

    public Bitmap imageOpen(int imageName){
        //Log.d("ImageSwitchViewer", String.valueOf(currImage));


        //String file = Pentura_tour_directory + "/" + fileNameArray[currImage];
        //fileName = new File(Pentura_tour_directory + "/" + fileNameArray[currImage]);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        //BitmapFactory.decodeStream(new FileInputStream(fileName),null,options);
        BitmapFactory.decodeResource(this.getResources(), imageName,options);

        int scale=1;
        do{
            scale+=1;
        }while(options.outWidth/scale>=dpHeight && options.outHeight/scale>=dpWidth);
        //scale -=1;

        BitmapFactory.Options options2 = new BitmapFactory.Options();
        options2.inSampleSize=scale;
        //Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(fileName), null, options2);
        Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), imageName, options2);
        //imageSwitcher.setImageDrawable(new BitmapDrawable(getResources(),bitmap));
        return bitmap;

        //imageSwitcherCounterTW.setText(currImage+1 + "/" + fileNameArray.length );
    }

    public int getDrawableId(String name){
        try {
            Field fld = R.drawable.class.getField(name);
            return fld.getInt(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
}

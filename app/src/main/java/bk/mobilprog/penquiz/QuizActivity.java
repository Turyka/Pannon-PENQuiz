package bk.mobilprog.penquiz;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Random;

import androidx.activity.EdgeToEdge;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import bk.mobilprog.penquiz.CoustomTextView.CoustomTextView;
import bk.mobilprog.penquiz.Services.DataBaseHelper;

public class QuizActivity extends AppCompatActivity {

    //Animációkhoz
    public static final String EXTRA_CIRCULAR_REVEAL_X = "EXTRA_CIRCULAR_REVEAL_X";
    public static final String EXTRA_CIRCULAR_REVEAL_Y = "EXTRA_CIRCULAR_REVEAL_Y";
    View rootLayout;
    View buttonView;
    private int revealX;
    private int revealY;

    DataBaseHelper myDataBase;
    ArrayList<String> questionsSelected = new ArrayList<>();
    ArrayList<Integer> questionIDsSelected = new ArrayList<>();
    int quizSequence;
    int topicID;
    int countTimer;
    long startTime;
    //TextView question;
    CoustomTextView coustomTextView;
    Button answer1;
    Button answer2;
    Button answer3;
    Button answer4;
    ArrayList<ImageView> answerTaggerList = new ArrayList<>();
    ProgressBar progressBar;
    int CorrectAnswerNumber;
    //float dpHeight;
    //float dpWidth;

    Handler timerHandler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_quiz);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.quiz_root_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        myDataBase = new DataBaseHelper(this);
        Context context = this.getApplicationContext();

        final Intent intent = getIntent();
        rootLayout = findViewById(R.id.quiz_root_layout);
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

        Bundle extras = getIntent().getExtras();
        topicID = extras.getInt("topicID");
        CorrectAnswerNumber = 0;


        Typeface face = Typeface.createFromAsset(getAssets(), "trajan_pro_bold.ttf");
        Typeface face2 = Typeface.createFromAsset(getAssets(), "trajanpro_regular.OTF");

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        //question = (TextView) findViewById(R.id.askquestion);
        coustomTextView = (CoustomTextView) findViewById(R.id.askquestion);
        answer1 = (Button) findViewById(R.id.answer1);
        answer2 = (Button) findViewById(R.id.answer2);
        answer3 = (Button) findViewById(R.id.answer3);
        answer4 = (Button) findViewById(R.id.answer4);


        //question.setTypeface(face);
        coustomTextView.setTypeface(face);
        answer1.setTypeface(face);
        answer2.setTypeface(face);
        answer3.setTypeface(face);
        answer4.setTypeface(face);






        Bitmap bitmap;
        DisplayMetrics displayMetrics = getApplicationContext().getResources().getDisplayMetrics();
        float dpHeight = displayMetrics.heightPixels / displayMetrics.density;
        int dpWidth = (int) (displayMetrics.widthPixels / displayMetrics.density);

        switch(topicID){
            case 1:
                //bitmap = imageOpen(R.drawable.theme_pen_1013x1800_bg);
                //bitmap = imageOpen(R.drawable.theme1_pen_bg_grey);
                bitmap = ReScalePictures.getResizedBitmap(dpWidth, dpWidth, R.drawable.theme1_pen_bg_grey, this);
                rootLayout.setBackgroundDrawable(new BitmapDrawable(getResources(),bitmap));
                answer1.setBackgroundResource(R.drawable.theme_buttoncolor);
                answer2.setBackgroundResource(R.drawable.theme_buttoncolor);
                answer3.setBackgroundResource(R.drawable.theme_buttoncolor);
                answer4.setBackgroundResource(R.drawable.theme_buttoncolor);
                coustomTextView.setBackgroundResource(R.drawable.theme_buttoncolor);
                break;
            case 2:
                //bitmap = imageOpen(R.drawable.theme2_pe_bg_grey);
                bitmap = ReScalePictures.getResizedBitmap(dpWidth, dpWidth, R.drawable.theme2_pe_bg_grey, this);
                rootLayout.setBackgroundDrawable(new BitmapDrawable(getResources(),bitmap));
                answer1.setBackgroundResource(R.drawable.theme2_buttoncolor);
                answer2.setBackgroundResource(R.drawable.theme2_buttoncolor);
                answer3.setBackgroundResource(R.drawable.theme2_buttoncolor);
                answer4.setBackgroundResource(R.drawable.theme2_buttoncolor);
                coustomTextView.setBackgroundResource(R.drawable.theme2_buttoncolor);
                break;
            case 3:
                //bitmap = imageOpen(R.drawable.theme3_turizmus_bg_grey);
                bitmap = ReScalePictures.getResizedBitmap(dpWidth, dpWidth, R.drawable.theme3_turizmus_bg_grey, this);
                rootLayout.setBackgroundDrawable(new BitmapDrawable(getResources(),bitmap));
                answer1.setBackgroundResource(R.drawable.theme3_buttoncolor);
                answer2.setBackgroundResource(R.drawable.theme3_buttoncolor);
                answer3.setBackgroundResource(R.drawable.theme3_buttoncolor);
                answer4.setBackgroundResource(R.drawable.theme3_buttoncolor);
                coustomTextView.setBackgroundResource(R.drawable.theme3_buttoncolor);
                coustomTextView.setTextColor(getResources().getColor(R.color.black));
                answer1.setTextColor(getResources().getColor(R.color.black));
                answer2.setTextColor(getResources().getColor(R.color.black));
                answer3.setTextColor(getResources().getColor(R.color.black));
                answer4.setTextColor(getResources().getColor(R.color.black));
                break;
            case 4:
                //bitmap = imageOpen(R.drawable.theme4_informatika_bg_grey);
                bitmap = ReScalePictures.getResizedBitmap(dpWidth, dpWidth, R.drawable.theme4_informatika_bg_grey, this);
                rootLayout.setBackgroundDrawable(new BitmapDrawable(getResources(),bitmap));
                answer1.setBackgroundResource(R.drawable.theme4_buttoncolor);
                answer2.setBackgroundResource(R.drawable.theme4_buttoncolor);
                answer3.setBackgroundResource(R.drawable.theme4_buttoncolor);
                answer4.setBackgroundResource(R.drawable.theme4_buttoncolor);
                coustomTextView.setBackgroundResource(R.drawable.theme4_buttoncolor);
                break;
            case 5:
                //bitmap = imageOpen(R.drawable.theme5_smart_bg_grey);
                bitmap = ReScalePictures.getResizedBitmap(dpWidth, dpWidth, R.drawable.theme5_smart_bg_grey, this);
                rootLayout.setBackgroundDrawable(new BitmapDrawable(getResources(),bitmap));
                answer1.setBackgroundResource(R.drawable.theme5_buttoncolor);
                answer2.setBackgroundResource(R.drawable.theme5_buttoncolor);
                answer3.setBackgroundResource(R.drawable.theme5_buttoncolor);
                answer4.setBackgroundResource(R.drawable.theme5_buttoncolor);
                coustomTextView.setBackgroundResource(R.drawable.theme5_buttoncolor);
                break;
            case 6:
                //bitmap = imageOpen(R.drawable.theme6_nagykanizsa_bg_grey);
                bitmap = ReScalePictures.getResizedBitmap(dpWidth, dpWidth, R.drawable.theme1_pen_bg_grey, this);
                rootLayout.setBackgroundDrawable(new BitmapDrawable(getResources(),bitmap));
                answer1.setBackgroundResource(R.drawable.theme6_buttoncolor);
                answer2.setBackgroundResource(R.drawable.theme6_buttoncolor);
                answer3.setBackgroundResource(R.drawable.theme6_buttoncolor);
                answer4.setBackgroundResource(R.drawable.theme6_buttoncolor);
                coustomTextView.setBackgroundResource(R.drawable.theme6_buttoncolor);
                break;
            case 7:
                //bitmap = imageOpen(R.drawable.theme7_kemia_bg_grey);
                bitmap = ReScalePictures.getResizedBitmap(dpWidth, dpWidth, R.drawable.theme7_vizes_bg_grey, this);
                rootLayout.setBackgroundDrawable(new BitmapDrawable(getResources(),bitmap));
                answer1.setBackgroundResource(R.drawable.theme7_buttoncolor);
                answer2.setBackgroundResource(R.drawable.theme7_buttoncolor);
                answer3.setBackgroundResource(R.drawable.theme7_buttoncolor);
                answer4.setBackgroundResource(R.drawable.theme7_buttoncolor);
                coustomTextView.setBackgroundResource(R.drawable.theme7_buttoncolor);
                coustomTextView.setTextColor(getResources().getColor(R.color.black));
                answer1.setTextColor(getResources().getColor(R.color.black));
                answer2.setTextColor(getResources().getColor(R.color.black));
                answer3.setTextColor(getResources().getColor(R.color.black));
                answer4.setTextColor(getResources().getColor(R.color.black));

                //Vizesek csak 3 választ adtak meg
                //answer4.setVisibility(View.GONE);
                break;
            case 8:

                //bitmap = imageOpen(R.drawable.theme8_biologia_bg_grey);
                bitmap = ReScalePictures.getResizedBitmap(dpWidth, dpWidth, R.drawable.theme8_zold_bg_grey, this);
                rootLayout.setBackgroundDrawable(new BitmapDrawable(getResources(),bitmap));
                answer1.setBackgroundResource(R.drawable.theme8_buttoncolor);
                answer2.setBackgroundResource(R.drawable.theme8_buttoncolor);
                answer3.setBackgroundResource(R.drawable.theme8_buttoncolor);
                answer4.setBackgroundResource(R.drawable.theme8_buttoncolor);
                coustomTextView.setBackgroundResource(R.drawable.theme8_buttoncolor);
                coustomTextView.setTextColor(getResources().getColor(R.color.black));
                answer1.setTextColor(getResources().getColor(R.color.black));
                answer2.setTextColor(getResources().getColor(R.color.black));
                answer3.setTextColor(getResources().getColor(R.color.black));
                answer4.setTextColor(getResources().getColor(R.color.black));
                break;
        }


        int buttonheightdivider = 8;


        LinearLayout tagger_layout = (LinearLayout) findViewById(R.id.tagger_layout);

//        answer1.requestLayout();
//        answer1.getLayoutParams().width = Math.round(dpWidth) / 2;
//        answer1.getLayoutParams().height = Math.round(dpHeight) / buttonheightdivider;
//        answer2.requestLayout();
//        answer2.getLayoutParams().width = Math.round(dpWidth) / 2;
//        answer2.getLayoutParams().height = Math.round(dpHeight) / buttonheightdivider;
//        answer3.requestLayout();
//        answer3.getLayoutParams().width = Math.round(dpWidth) / 2;
//        answer3.getLayoutParams().height = Math.round(dpHeight) / buttonheightdivider;
//        answer4.requestLayout();
//        answer4.getLayoutParams().width = Math.round(dpWidth) / 2;
//        answer4.getLayoutParams().height = Math.round(dpHeight) / buttonheightdivider;

        answerTaggerList.add((ImageView) findViewById(R.id.answerTAG_1));
        answerTaggerList.add((ImageView) findViewById(R.id.answerTAG_2));
        answerTaggerList.add((ImageView) findViewById(R.id.answerTAG_3));
        answerTaggerList.add((ImageView) findViewById(R.id.answerTAG_4));
        answerTaggerList.add((ImageView) findViewById(R.id.answerTAG_5));
        answerTaggerList.add((ImageView) findViewById(R.id.answerTAG_6));
        answerTaggerList.add((ImageView) findViewById(R.id.answerTAG_7));
        answerTaggerList.add((ImageView) findViewById(R.id.answerTAG_8));
        answerTaggerList.add((ImageView) findViewById(R.id.answerTAG_9));
        answerTaggerList.add((ImageView) findViewById(R.id.answerTAG_10));


        if (topicID == 1 || topicID == 2 || topicID == 3 || topicID == 4 || topicID == 5 || topicID == 6 || topicID == 7 || topicID == 8){
            inicializeQuestions(topicID);   //Előkészítjük a random számokat, lekérjük a kérdéseket
            quizSequence=0;
            setQuestionsandAnswer();
        }

        startTime = System.currentTimeMillis();
        timerHandler.postDelayed(timerRunnable, 0);
        countTimer = 0;


    }

    protected void nextQuestion(){

        quizSequence += 1;
        if (quizSequence<10){
            setQuestionsandAnswer();
            startTime = System.currentTimeMillis();
            timerHandler.postDelayed(timerRunnable, 0);
            countTimer = 0;
        }else{

            answer1.setClickable(false);
            answer2.setClickable(false);
            answer3.setClickable(false);
            answer4.setClickable(false);

            ActivityOptionsCompat options = ActivityOptionsCompat.
                    makeSceneTransitionAnimation(QuizActivity.this, progressBar, "transition");
            int revealX = (int) (progressBar.getX() + progressBar.getWidth() / 2);
            int revealY = (int) (progressBar.getY() + progressBar.getHeight() / 2);

            Intent resultActivity = new Intent(QuizActivity.this, ResultActivity.class);
            resultActivity.putExtra(QuizActivity.EXTRA_CIRCULAR_REVEAL_X, revealX);
            resultActivity.putExtra(QuizActivity.EXTRA_CIRCULAR_REVEAL_Y, revealY);
            resultActivity.putExtra("result", CorrectAnswerNumber);
            resultActivity.putExtra("topicID", topicID);

            ActivityCompat.startActivity(QuizActivity.this, resultActivity, options.toBundle());
        }
    }



    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            long millis = System.currentTimeMillis() - startTime;
            int seconds = (int) (millis / 1000);
            seconds = seconds % 60;

            //System.out.println(String.format("%d:%02d", minutes, seconds));


            Drawable draw = getResources().getDrawable(R.drawable.b_quiz_progressbar);
            progressBar.setProgressDrawable(draw);

            progressBar.setMax(20000);
            countTimer = (int)millis;
            progressBar.setProgress(countTimer);


            //timerTextView.setText(String.format("%d:%02d", minutes, seconds));

            if (seconds <20){
                timerHandler.postDelayed(this, 50);
            }else{
                answerTaggerList.get(quizSequence).setImageResource(R.drawable.answertagger_false);
                timerHandler.removeCallbacks(timerRunnable);
                nextQuestion();
            }

        }
    };


    private void inicializeQuestions(int topicID){
        Random randomgenerator = new Random();
        ArrayList<Integer> selectedRandoms = new ArrayList<>();
        questionsSelected = new ArrayList<>();
        questionIDsSelected = new ArrayList<>();

        ArrayList<Integer> questionIDs = myDataBase.getQuestionsIDs(topicID);
        int selector;
        if (questionIDs != null){
            for (int i = 0; i<10; i++){
                selector = randomgenerator.nextInt(questionIDs.size());
                if(!selectedRandoms.contains(selector)){
                    selectedRandoms.add(selector);
                }else{
                    i--;
                }
            }
            for (int i = 0; i < selectedRandoms.size(); i++) {
//                System.out.println("selectedRandoms: " + selectedRandoms.get(i));
//                System.out.println("questionIDs: " + questionIDs.get(selectedRandoms.get(i)));

                questionIDsSelected.add(questionIDs.get(selectedRandoms.get(i)));
                questionsSelected.add(myDataBase.getQuestionofID(questionIDs.get(selectedRandoms.get(i))));
            }
        }
    }

    //Beállítjuk a gombokat, mit írjanak és milyen TAG-et kapjanak (true/false)
    private void setQuestionsandAnswer(){


        //question.setText(questionsSelected.get(quizSequence));
        coustomTextView.setText(questionsSelected.get(quizSequence));
        ArrayList<String[]> answerpack = myDataBase.getAnswersofQuestion(questionIDsSelected.get(quizSequence));


        Random r = new Random();
        ArrayList<Integer> randomAnswerque = new ArrayList<>();
        for (int i = 0; i<4; i++){
            int randomAnswers = r.nextInt(4);
            if (!randomAnswerque.contains(randomAnswers)){
                randomAnswerque.add(randomAnswers);
            }else{
                i--;
            }
        }

        //Szövegel feltöltés
        answer1.setText(answerpack.get(randomAnswerque.get(0))[0]);
        answer2.setText(answerpack.get(randomAnswerque.get(1))[0]);
        answer3.setText(answerpack.get(randomAnswerque.get(2))[0]);
        answer4.setText(answerpack.get(randomAnswerque.get(3))[0]);
        //Berakjuk a true és false szavakat TAG-nek
        answer1.setTag(answerpack.get(randomAnswerque.get(0))[1]);
        answer2.setTag(answerpack.get(randomAnswerque.get(1))[1]);
        answer3.setTag(answerpack.get(randomAnswerque.get(2))[1]);
        answer4.setTag(answerpack.get(randomAnswerque.get(3))[1]);

        setOnClick(answer1);
        setOnClick(answer2);
        setOnClick(answer3);
        setOnClick(answer4);
    }

    //Melyik gombra kattintunk, annak TAG-jét lekéri (true/false)
    private void setOnClick(final Button btn){
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String TAG = btn.getTag().toString();
                //buttonView = view;

                //timerHandler.removeCallbacks(timerRunnable);

                if (timerHandler != null){
                    timerHandler.removeCallbacks(timerRunnable);
                }
                //Változtatjuk a TAGjelölő ikonját
                switch (TAG){
                    case "true":
                        answerTaggerList.get(quizSequence).setImageResource(R.drawable.answertagger_true);
                        CorrectAnswerNumber++;
                        break;
                    case "false":
                        answerTaggerList.get(quizSequence).setImageResource(R.drawable.answertagger_false);
                        break;
                }

                nextQuestion();

                //System.out.println(btn.getTag());

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (timerHandler != null){
            timerHandler.postDelayed(timerRunnable, 0);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (timerHandler != null){
            timerHandler.removeCallbacks(timerRunnable);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (timerHandler != null){
            timerHandler.removeCallbacks(timerRunnable);
        }
        unRevealActivity();
        finish();
        Intent mainAcivity = new Intent(QuizActivity.this, MainActivity.class);
        startActivity(mainAcivity);

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
        //do{
        //    scale+=1;
        //}while(options.outWidth/scale>=dpHeight && options.outHeight/scale>=dpWidth);
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
            super.onBackPressed();
        }
    }
}



package bk.mobilprog.penquiz;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import bk.mobilprog.penquiz.Services.DataBaseHelper;

public class QuizActivityCDT extends AppCompatActivity {

    CountDownTimer cdt;
    CountDownTimer count;
    int countTimer;
    int progress;
    int backtime;
    int oldbacktime;
    DataBaseHelper myDataBase;
    ArrayList<String> questionsSelected = new ArrayList<>();
    ArrayList<Integer> questionIDsSelected = new ArrayList<>();
    int quizSequence;
    TextView question;
    Button answer1;
    Button answer2;
    Button answer3;
    Button answer4;
    ArrayList<ImageView> answerTaggerList = new ArrayList<>();
    private long mPauseTimeRemaining;

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


        Bundle extras = getIntent().getExtras();
        int topicID = extras.getInt("topicID");

        question = (TextView) findViewById(R.id.askquestion);
        answer1 = (Button) findViewById(R.id.answer1);
        answer2 = (Button) findViewById(R.id.answer2);
        answer3 = (Button) findViewById(R.id.answer3);
        answer4 = (Button) findViewById(R.id.answer4);

        int buttonheightdivider = 8;

        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpHeight = displayMetrics.heightPixels / displayMetrics.density;
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;

        LinearLayout tagger_layout = (LinearLayout) findViewById(R.id.tagger_layout);

        answer1.requestLayout();
        answer1.getLayoutParams().width = Math.round(dpWidth) / 2;
        answer1.getLayoutParams().height = Math.round(dpHeight) / buttonheightdivider;
        answer2.requestLayout();
        answer2.getLayoutParams().width = Math.round(dpWidth) / 2;
        answer2.getLayoutParams().height = Math.round(dpHeight) / buttonheightdivider;
        answer3.requestLayout();
        answer3.getLayoutParams().width = Math.round(dpWidth) / 2;
        answer3.getLayoutParams().height = Math.round(dpHeight) / buttonheightdivider;
        answer4.requestLayout();
        answer4.getLayoutParams().width = Math.round(dpWidth) / 2;
        answer4.getLayoutParams().height = Math.round(dpHeight) / buttonheightdivider;

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

        for (int i=0; i<10; i++){
            answerTaggerList.get(i).setImageResource(R.drawable.answertagger_empty);
            /*answerTaggerList.get(i).requestLayout();
            answerTaggerList.get(i).getLayoutParams().width = Math.round(dpWidth) / 12;*/
        }
        /*tagger_layout.requestLayout();
        tagger_layout.getLayoutParams().height = Math.round(dpHeight)/10;*/


        setprogressBar_progress();
        inicializeQuestions(topicID);   //Előkészítjük a random számokat, lekérjük a kérdéseket
        quizSequence=0;
        setQuestionsandAnswer();


    }

    protected void nextQuestion(){

        quizSequence += 1;
        if (quizSequence<10){
            setQuestionsandAnswer();
            setprogressBar_progress();
        }else{
            if (cdt != null){
                Intent quizActivity = new Intent(QuizActivityCDT.this, ResultActivity.class);
                startActivity(quizActivity);
            }
            //onBackPressed();
        }

    }

    private void setprogressBar_progress(){

        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        Drawable draw = getResources().getDrawable(R.drawable.b_quiz_progressbar);
        progressBar.setProgressDrawable(draw);

        countTimer = 0;
        progressBar.setProgress(countTimer);

        final int timeSetter = 20 * 1000+200;
        progressBar.setMax(20000);//10000

        cdt = new CountDownTimer(timeSetter, 20){ //mennyi ideig, mekkora léptékben lépdessen //100

            @Override
            public void onTick(long millisUntilFinished) {
                //progress++;
                //System.out.println("Thick: " + progress);
                //progress = (int)countTimer*1000/(timeSetter/10);

                countTimer += 20;
                progressBar.setProgress(countTimer);
            }

            @Override
            public void onFinish() {
                answerTaggerList.get(quizSequence).setImageResource(R.drawable.answertagger_false);
                cdt.cancel();
                nextQuestion();

                //System.out.println("LETELT AZ IDŐ!");

            }
        }.start();

    }

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
                System.out.println("Selector: " +  selector);
                if(!selectedRandoms.contains(selector)){
                    selectedRandoms.add(selector);
                }else{
                    i--;
                }
            }
            for (int i = 0; i < selectedRandoms.size(); i++) {
                //System.out.println("selectedRandoms: " + selectedRandoms.get(i));
                //System.out.println("questionIDs: " + questionIDs.get(selectedRandoms.get(i)));

                questionIDsSelected.add(questionIDs.get(selectedRandoms.get(i)));
                questionsSelected.add(myDataBase.getQuestionofID(questionIDs.get(selectedRandoms.get(i))));
            }
        }
    }

    //Beállítjuk a gombokat, mit írjanak és milyen TAG-et kapjanak (true/false)
    private void setQuestionsandAnswer(){


        question.setText(questionsSelected.get(quizSequence));
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
        //System.out.println("Random 1: " + randomAnswerque.get(0));
        //System.out.println("Random 2: " + randomAnswerque.get(1));
        ///System.out.println("Random 3: " + randomAnswerque.get(2));
        //System.out.println("Random 4: " + randomAnswerque.get(3));
        //System.out.println("Random count: " + randomAnswerque.size());




        //Szövegel feltöltés
        answer1.setText(answerpack.get(0)[0]);
        answer2.setText(answerpack.get(1)[0]);
        answer3.setText(answerpack.get(2)[0]);
        answer4.setText(answerpack.get(3)[0]);
        //Berakjuk a true és false szavakat TAG-nek
        answer1.setTag(answerpack.get(0)[1]);
        answer2.setTag(answerpack.get(1)[1]);
        answer3.setTag(answerpack.get(2)[1]);
        answer4.setTag(answerpack.get(3)[1]);

        setOnClick(answer1);
        setOnClick(answer2);
        setOnClick(answer3);
        setOnClick(answer4);
    }

    //Melyik gombra kattintunk, annak TAG-jét lekéri (true/false)
    private void setOnClick(final Button btn){
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String TAG = btn.getTag().toString();
                if (cdt != null){
                    cdt.cancel();
                }
                //Változtatjuk a TAGjelölő ikonját
                switch (TAG){
                    case "true":
                        answerTaggerList.get(quizSequence).setImageResource(R.drawable.answertagger_true);
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
        if (cdt != null){
            cdt.start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (cdt != null){
            cdt.cancel();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (cdt != null){
            cdt.cancel();
        }
    }
}

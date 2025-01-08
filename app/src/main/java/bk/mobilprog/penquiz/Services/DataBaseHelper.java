package bk.mobilprog.penquiz.Services;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Created by PEN0001 on 2017. 08. 08..
 */

public class DataBaseHelper extends SQLiteOpenHelper {

    private static final String ANSWERS_TABLE_NAME = "Answers";
    private static final String QUESTIONS_TABLE_NAME = "Questions";
    private static final String TOPICS_TABLE_NAME = "Topics";

    private static final String ANSWERS_ID = "_id";
    private static final String ANSWERS_ANSWER = "Answer";
    private static final String ANSWERS_CORRECT = "Correct";
    private static final String ANSWERS_QUESTION_ID = "Questions_idQuestions";

    private static final String QUESTIONS_ID = "_id";
    private static final String QUESTIONS_QUESTION = "Question";
    private static final String QUESTIONS_TOPIC_ID = "Topics_idTopics";

    private static final String TOPICS_ID = "_id";
    private static final String TOPICS_TOPIC = "Topic";



    private static String DB_PATH;
    private static String DB_NAME = "penquizdb.db";
    private final Context myContext;
    private SQLiteDatabase myDataBase;



    public DataBaseHelper(Context context) {
        super(context, DB_NAME, null, 1);
        this.myContext = context;
        DB_PATH = context.getFilesDir()+"/penquizdb.db";
    }


    public void createDataBase() throws IOException {
        //boolean dbExist = checkDataBase();
        SQLiteDatabase db_read = null;
        //if(dbExist){
            //do nothing - database already exist
        //}else{
            //By calling this method and empty database will be created into the default system path
            //of your application so we are gonna be able to overwrite that database with our database.
            //this.getReadableDatabase();
        db_read = this.getReadableDatabase();
        db_read.close();
        try {
            copyDataBase();
        } catch (IOException e) {
            throw new Error("Error copying database");
        }
        //}

    }

    //Ellenőrzi, létezik-e már az adatbázis a system mappában, hogy ne másolja át fölöslegesen.
    //true értéket ad vissza, ha létezik és false-t ha még nem
    private boolean checkDataBase(){
        SQLiteDatabase checkDB = null;
        try{
            //String myPath = DB_PATH + DB_NAME;
            String myPath = DB_PATH+DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
            this.close();

        }catch(SQLiteException e){
            //database does't exist yet.
            e.printStackTrace();
        }
        if(checkDB != null){
            checkDB.close();
        }
        return checkDB != null ? true : false;
    }

    //Az előre elkészített adatbázist a helyi assets mappából átmásolja a system mappába, ahol ezentúl eléri és használja a program
    private void copyDataBase() throws IOException{
        //Open your local db as the input stream
        InputStream myInput = myContext.getAssets().open(DB_NAME);
        // Path to the just created empty db
        // String outFileName = DB_PATH + DB_NAME;
        String outFileName = DB_PATH+DB_NAME;
        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);
        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        /*while ((length = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, length);
        }*/
        while((length = myInput.read(buffer))!=-1){
            if(length > 0){
                myOutput.write(buffer, 0, length);
            }
        }
        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }


    //Adatbázis megnyitása
    private DataBaseHelper open () throws  SQLException{
        String myPath = DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
        return this;
    }


    public DataBaseHelper openDataBaseWrite() throws SQLException {
        String myPath = DB_PATH + DB_NAME;
        //myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
        return this;
    }

    /**Lekérdezések kezdete*/

    //Lekérjük az összes Topic nevét
    public ArrayList<String> getThemesforButton(){
        //adatbázis megnyitása
        open();
        //string lista
        ArrayList<String> TopicList = new ArrayList<String>();

        Cursor TopicNameCursor = myDataBase.query(TOPICS_TABLE_NAME, new String[]
                {TOPICS_ID, TOPICS_TOPIC},
                null,
                null,
                null,
                null,
                null);

        if(TopicNameCursor.moveToFirst() || TopicNameCursor.getCount() != 0){
            TopicNameCursor.moveToFirst();
        }else{
            return null;
        }

        while(!TopicNameCursor.isAfterLast()){
            TopicList.add(TopicNameCursor.getString(TopicNameCursor.getColumnIndexOrThrow(TOPICS_TOPIC)));
            TopicNameCursor.moveToNext();
        }

        TopicNameCursor.close();
        close();
        return TopicList;
    }

    public ArrayList<Integer> getQuestionsIDs(int topicID){
        open();
        ArrayList<Integer> questionIDs = new ArrayList<>();
        Cursor questionCursor = myDataBase.query(QUESTIONS_TABLE_NAME, new String[]
                        {QUESTIONS_ID, QUESTIONS_QUESTION, QUESTIONS_TOPIC_ID},
                QUESTIONS_TOPIC_ID + " = ?",
                new String[]{String.valueOf(topicID)},
                null,
                null,
                null);

        if (questionCursor.moveToFirst() || questionCursor.getCount() != 0){
            questionCursor.moveToFirst();
        }else{
            return null;
        }

        while(!questionCursor.isAfterLast()){
            questionIDs.add(questionCursor.getInt(questionCursor.getColumnIndexOrThrow(QUESTIONS_ID)));
            questionCursor.moveToNext();
        }
        questionCursor.close();
        close();
        return questionIDs;
    }

    public String getQuestionofID(int questionID){
        open();
        Cursor questionCursor = myDataBase.query(QUESTIONS_TABLE_NAME, new String[]
                {QUESTIONS_ID, QUESTIONS_QUESTION},
                QUESTIONS_ID + " = ?",
                new String[]{String.valueOf(questionID)},
                null,
                null,
                null);

        if (questionCursor.moveToFirst() || questionCursor.getCount() != 0){
            questionCursor.moveToFirst();
        }else{
            return null;
        }

        close();
        String question = questionCursor.getString(questionCursor.getColumnIndexOrThrow(QUESTIONS_QUESTION));
        questionCursor.close();
        return question;
    }

    public ArrayList<String[]> getAnswersofQuestion(int questionID){
        open();

        ArrayList<String[]> answersPack = new ArrayList<>();
        Cursor answerCursor = myDataBase.query(ANSWERS_TABLE_NAME, new String[]
                {ANSWERS_ID, ANSWERS_ANSWER, ANSWERS_CORRECT, ANSWERS_QUESTION_ID},
                ANSWERS_QUESTION_ID + " = ?",
                new String[]{String.valueOf(questionID)},
                null,
                null,
                null);
        if (answerCursor.moveToFirst() || answerCursor.getCount() != 0){
            answerCursor.moveToFirst();
        }else{
            return null;
        }
        close();

        while (!answerCursor.isAfterLast()){
            String[] answer = new String[2];
            answer[0] = answerCursor.getString(answerCursor.getColumnIndexOrThrow(ANSWERS_ANSWER));
            answer[1] = answerCursor.getString(answerCursor.getColumnIndexOrThrow(ANSWERS_CORRECT));
            answersPack.add(answer);
            answerCursor.moveToNext();
        }
        answerCursor.close();
        return answersPack;
    }






    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


}

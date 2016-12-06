package finalproject.mobileappclass.com.captura.DatabaseHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import finalproject.mobileappclass.com.captura.Models.QuizScore;
import finalproject.mobileappclass.com.captura.Models.TranslationRequest;

/**
 * Created by Nikhil on 12/4/16.
 */

public class CapturaDatabaseHelper extends SQLiteOpenHelper
{
    // Database Info
    private static final String DATABASE_NAME = "capturaDatabase";
    private static final int DATABASE_VERSION = 1;

    // Table Names
    private static final String TABLE_TRANSLATION_REQUESTS = "translationRequests";
    private static final String TABLE_QUIZ_SCORES = "quizScores";


    //Translation Request Table Columns
    private static final String COLUMN_REQUEST_ID = "requestId";
    private static final String COLUMN_INPUT_WORD = "inputWord";
    private static final String COLUMN_TRANSLATED_WORD = "translatedWord";
    private static final String COLUMN_LAGUAGE_OF_INTEREST = "languageOfInterest";

    //Quiz Scores Table Columns
    private static final String COLUMN_QUIZ_ID = "quizId";
    private static final String COLUMN_QUIZ_SCORES = "quizScores";
    private static final String COLUMN_TIME_STAMP = "timeStamp";
    private static final String COLUMN_QUIZ_LANGUAGE = "languageOfInterest";

    //Singleton instance
    private static CapturaDatabaseHelper mInstance;

    private CapturaDatabaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized CapturaDatabaseHelper getInstance(Context context)
    {
        if(mInstance == null)
        {
            mInstance = new CapturaDatabaseHelper(context.getApplicationContext());
        }

        return mInstance;
    }

    @Override
    public void onConfigure(SQLiteDatabase db)
    {
        super.onConfigure(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String CREATE_TABLE_TRANSLATION_REQUESTS = "CREATE TABLE " + TABLE_TRANSLATION_REQUESTS +
                "(" +
                    COLUMN_REQUEST_ID + " INTEGER PRIMARY KEY," +
                    COLUMN_INPUT_WORD + " TEXT," +
                    COLUMN_TRANSLATED_WORD + " TEXT," +
                    COLUMN_LAGUAGE_OF_INTEREST + " TEXT" +
                ")";

        String CREATE_TABLE_QUIZ_SCORES = "CREATE TABLE " + TABLE_QUIZ_SCORES +
                "(" +
                    COLUMN_QUIZ_ID + " INTEGER PRIMARY KEY," +
                    COLUMN_QUIZ_SCORES + " INTEGER," +
                    COLUMN_QUIZ_LANGUAGE + " TEXT," +
                    COLUMN_TIME_STAMP + " TEXT" +
                ")";

        db.execSQL(CREATE_TABLE_TRANSLATION_REQUESTS);
        db.execSQL(CREATE_TABLE_QUIZ_SCORES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        if(oldVersion != newVersion)
        {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSLATION_REQUESTS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUIZ_SCORES);
            onCreate(db);
        }
    }

    public ArrayList<TranslationRequest> getEntireHistory()
    {
        ArrayList<TranslationRequest> translationRequests = new ArrayList<TranslationRequest>();
        String GET_ALL_ENTRIES_QUERY = String.format("SELECT * FROM %s ORDER BY %s DESC", TABLE_TRANSLATION_REQUESTS, COLUMN_REQUEST_ID);
        SQLiteDatabase db = getReadableDatabase();
        db.beginTransaction();
        Cursor cursor = db.rawQuery(GET_ALL_ENTRIES_QUERY, null);
        try
        {
            if(cursor.moveToFirst())
            {
                do
                {
                    TranslationRequest translationRequest = new TranslationRequest();
                    translationRequest.setInputWord(cursor.getString(cursor.getColumnIndex(COLUMN_INPUT_WORD)));
                    translationRequest.setTranslatedWord(cursor.getString(cursor.getColumnIndex(COLUMN_TRANSLATED_WORD)));
                    translationRequest.setLanguageOfInterest(cursor.getString(cursor.getColumnIndex(COLUMN_LAGUAGE_OF_INTEREST)));
                    translationRequests.add(translationRequest);
                }while(cursor.moveToNext());
            }
            db.setTransactionSuccessful();
        }
        catch(Exception e)
        {
            Log.e("AndroidCaptura", e.getMessage());
        }
        finally
        {
            if(cursor != null && !(cursor.isClosed()))
            {
                cursor.close();
            }
            db.endTransaction();
        }
        return translationRequests;
    }

    public ArrayList<QuizScore> getAllScores()
    {
        ArrayList<QuizScore> quizScores = new ArrayList<QuizScore>();
        String GET_ALL_SCORES_QUERY = String.format("SELECT * FROM %s", TABLE_QUIZ_SCORES);
        SQLiteDatabase db = getReadableDatabase();
        db.beginTransaction();
        Cursor cursor = db.rawQuery(GET_ALL_SCORES_QUERY, null);
        try
        {
            if(cursor.moveToFirst())
            {
                do
                {
                    QuizScore quizScore = new QuizScore();
                    quizScore.setQuizScore(cursor.getInt(cursor.getColumnIndex(COLUMN_QUIZ_SCORES)));
                    quizScore.setLanguageOfInterest(cursor.getString(cursor.getColumnIndex(COLUMN_QUIZ_LANGUAGE)));
                    quizScore.setTimeStamp(cursor.getString(cursor.getColumnIndex(COLUMN_TIME_STAMP)));
                    quizScores.add(quizScore);
                }while(cursor.moveToNext());
            }
            db.setTransactionSuccessful();
        }
        catch(Exception e)
        {
            Log.e("AndroidCaptura", e.getMessage());
        }
        finally
        {
            if(cursor != null && !(cursor.isClosed()))
            {
                cursor.close();
            }
            db.endTransaction();
        }
        return quizScores;
    }

    public void insertTranslationRequest(TranslationRequest translationRequest)
    {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try
        {
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_INPUT_WORD, translationRequest.getInputWord());
            contentValues.put(COLUMN_TRANSLATED_WORD, translationRequest.getTranslatedWord());
            contentValues.put(COLUMN_LAGUAGE_OF_INTEREST, translationRequest.getLanguageOfInterest());

            Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_TRANSLATION_REQUESTS + " WHERE " +
                    COLUMN_INPUT_WORD + "='" + translationRequest.getInputWord() + "' AND " + COLUMN_LAGUAGE_OF_INTEREST
                    + "='" + translationRequest.getLanguageOfInterest()+"'", null);

            if(cursor.getCount() == 0)
            {
                db.insertOrThrow(TABLE_TRANSLATION_REQUESTS, null, contentValues);
            }
            db.setTransactionSuccessful();
        }
        catch(Exception e)
        {
            Log.e("AndroidCaptura", e.getMessage());
        }
        finally
        {
            db.endTransaction();
        }
    }

    public void insertQuizScore(QuizScore quizScore)
    {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try
        {
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_QUIZ_SCORES, quizScore.getQuizScore());
            contentValues.put(COLUMN_QUIZ_LANGUAGE, quizScore.getLanguageOfInterest());
            contentValues.put(COLUMN_TIME_STAMP, quizScore.getTimeStamp());

            db.insertOrThrow(TABLE_QUIZ_SCORES, null, contentValues);
            db.setTransactionSuccessful();
        }
        catch(Exception e)
        {
            Log.e("AndroidCaptura", e.getMessage());
        }
        finally
        {
            db.endTransaction();
        }
    }

    public ArrayList<TranslationRequest> findTranslationRequestsByLanguage(String language)
    {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<TranslationRequest> resultList = new ArrayList<TranslationRequest>();
        Cursor cursor = db.rawQuery("SELECT * FROM "+TABLE_TRANSLATION_REQUESTS+" WHERE " + COLUMN_LAGUAGE_OF_INTEREST
                + "= ? ORDER BY " + COLUMN_REQUEST_ID + " DESC" , new String[]{language});
        db.beginTransaction();
        try
        {
            if(cursor.moveToFirst())
            {
                do
                {
                    TranslationRequest translationRequest = new TranslationRequest();
                    translationRequest.setInputWord(cursor.getString(cursor.getColumnIndex(COLUMN_INPUT_WORD)));
                    translationRequest.setTranslatedWord(cursor.getString(cursor.getColumnIndex(COLUMN_TRANSLATED_WORD)));
                    translationRequest.setLanguageOfInterest(cursor.getString(cursor.getColumnIndex(COLUMN_LAGUAGE_OF_INTEREST)));
                    resultList.add(translationRequest);
                } while(cursor.moveToNext());
            }
            db.setTransactionSuccessful();
        }
        catch(Exception e)
        {
            Log.e("AndroidCaptura", e.getMessage());
        }
        finally
        {
            if(cursor != null && !(cursor.isClosed()))
            {
                cursor.close();
            }
            db.endTransaction();
        }
        return resultList;
    }

    public ArrayList<QuizScore> findQuizScoresByLanguage(String language)
    {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<QuizScore> resultList = new ArrayList<QuizScore>();
        Cursor cursor = db.rawQuery("SELECT * FROM "+TABLE_QUIZ_SCORES+" WHERE " + COLUMN_QUIZ_LANGUAGE
            + "= ?", new String[]{language});
        db.beginTransaction();
        try
        {
            if(cursor.moveToFirst())
            {
                do
                {
                    QuizScore quizScore = new QuizScore();
                    quizScore.setQuizScore(cursor.getInt(cursor.getColumnIndex(COLUMN_QUIZ_SCORES)));
                    quizScore.setLanguageOfInterest(cursor.getString(cursor.getColumnIndex(COLUMN_QUIZ_LANGUAGE)));
                    quizScore.setTimeStamp(cursor.getString(cursor.getColumnIndex(COLUMN_TIME_STAMP)));
                    resultList.add(quizScore);
                }while(cursor.moveToNext());
            }
            db.setTransactionSuccessful();
        }
        catch(Exception e)
        {
            Log.e("AndroidCaptura", e.getMessage());
        }
        finally
        {
            if(cursor != null && !(cursor.isClosed()))
            {
                cursor.close();
            }
            db.endTransaction();
        }
        return resultList;
    }
}

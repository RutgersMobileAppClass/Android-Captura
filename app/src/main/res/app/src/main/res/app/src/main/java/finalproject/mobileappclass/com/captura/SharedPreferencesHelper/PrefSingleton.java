package finalproject.mobileappclass.com.captura.SharedPreferencesHelper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


public class PrefSingleton {
    private static PrefSingleton mInstance;
    private Context mContext;
    //
    private SharedPreferences mMyPreferences;

    private PrefSingleton(){ }

    public static PrefSingleton getInstance(){
        if (mInstance == null) mInstance = new PrefSingleton();
        return mInstance;
    }

    public void init(Context ctx){
        mContext = ctx;

        mMyPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
    }

    public SharedPreferences getSharedPreferences()
    {
        return mMyPreferences;
    }

    public void writePreference(String key, String value)
    {
        SharedPreferences.Editor editor = mMyPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public String readPreference(String key)
    {
        return mMyPreferences.getString(key, null);
    }
}

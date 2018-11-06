package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class RestoFireBaseInstanceIdService extends FirebaseInstanceIdService {
    public void onTokenRefresh(){
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        guardarToken(refreshedToken);
    }

    public void guardarToken(String token){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("registration_id",  token);
        editor.apply();
    }

    public String leerToken(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        return preferences.getString("registration_id",null);
    }
}

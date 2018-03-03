package com.example.cesar.dbfz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void menuAction(View v){
        switch (v.getId()){
            case R.id.btnCharacters :
                Intent intent = new Intent(MainActivity.this, CharactersActivity.class);
                startActivity(intent);
                break;
            case R.id.btnBasicAtacks :

                break;
            case R.id.btnCombatMechanics :

                break;
            case R.id.btnScenarios :

                break;
        }
    }
}

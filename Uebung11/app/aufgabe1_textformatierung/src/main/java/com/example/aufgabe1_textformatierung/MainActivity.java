package com.example.aufgabe1_textformatierung;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.widget.TextView;
import android.graphics.Typeface;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Beispiel: Eigene Schriftart verwenden
        TextView customFontText = findViewById(R.id.customFontText);
        Typeface schriftart = Typeface.createFromAsset(getAssets(), "fonts/deineschrift.ttf");
        customFontText.setTypeface(schriftart);
    }
}

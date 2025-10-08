package com.example.aufgabe3_temperaturumrechnung;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

public class MainActivity extends AppCompatActivity {

    EditText inputValue;
    TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputValue = findViewById(R.id.inputValue);
        result = findViewById(R.id.result);
        Button btnCtoF = findViewById(R.id.btnCtoF);
        Button btnFtoC = findViewById(R.id.btnFtoC);

        btnCtoF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double c = Double.parseDouble(inputValue.getText().toString());
                double f = c * 9 / 5 + 32;
                result.setText("Ergebnis: " + f + " °F");
            }
        });

        btnFtoC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double f = Double.parseDouble(inputValue.getText().toString());
                double c = (f - 32) * 5 / 9;
                result.setText("Ergebnis: " + c + " °C");
            }
        });
    }
}

package com.example.aufgabe3_temperaturumrechnung;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

public class MainActivity extends AppCompatActivity {

    EditText inputValue;
    TextView result;
    Button btnCtoF, btnFtoC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputValue = findViewById(R.id.inputValue);
        result = findViewById(R.id.result);
        btnCtoF = findViewById(R.id.btnCtoF);
        btnFtoC = findViewById(R.id.btnFtoC);

        //Celsius in Fahrenheit
        btnCtoF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input = inputValue.getText().toString().trim();
                if (input.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Bitte geben Sie einen Wert", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    double c = Double.parseDouble(input);
                    double f = c * 9 / 5 + 32;
                    result.setText("Ergebnis: " + f + " °F");
                } catch (NumberFormatException e) {
                    Toast.makeText(MainActivity.this, "Dieser Wert ist leider ungültig", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Fahrenheit → Celsius
        btnFtoC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input = inputValue.getText().toString().trim();
                if (input.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Bitte geben Sie einen Wert ein", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    double f = Double.parseDouble(input);
                    double c = (f - 32) * 5 / 9;
                    result.setText("Ergebnis: " + c + " °C");
                } catch (NumberFormatException e) {
                    Toast.makeText(MainActivity.this, "Ungültiger Wert", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

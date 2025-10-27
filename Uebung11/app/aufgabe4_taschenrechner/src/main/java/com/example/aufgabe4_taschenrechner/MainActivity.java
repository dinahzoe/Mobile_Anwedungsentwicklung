package com.example.aufgabe4_taschenrechner;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

public class MainActivity extends AppCompatActivity {

    TextView display;
    String current = "";
    double firstNumber = 0;
    String operator = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        display = findViewById(R.id.display);

        View.OnClickListener numberClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button b = (Button) v;
                current += b.getText().toString();
                display.setText(current);
            }
        };

        int[] numberIds = {R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
                R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9};
        for (int id : numberIds) {
            findViewById(id).setOnClickListener(numberClick);
        }

        // Operatoren
        findViewById(R.id.btnAdd).setOnClickListener(v -> setOperator("+"));
        findViewById(R.id.btnSub).setOnClickListener(v -> setOperator("-"));
        findViewById(R.id.btnMul).setOnClickListener(v -> setOperator("*"));
        findViewById(R.id.btnDiv).setOnClickListener(v -> setOperator("/"));

        // Clear
        findViewById(R.id.btnC).setOnClickListener(v -> clear());

        // Gleich
        findViewById(R.id.btnEq).setOnClickListener(v -> calculate());

        //CE
        findViewById(R.id.btnCE).setOnClickListener(v -> {
            current = "";
            display.setText("0");
        });

    }

    void setOperator(String op) {
        if (current.isEmpty()) return; // nichts tun, wenn keine Zahl eingegeben
        try {
            firstNumber = Double.parseDouble(current);
            operator = op;
            current = "";
        } catch (NumberFormatException e) {
            display.setText("Error");
            current = "";
        }
    }

    void clear() {
        current = "";
        firstNumber = 0;
        operator = "";
        display.setText("0");
    }

    void calculate() {
        if (current.isEmpty() || operator.isEmpty()) return;

        try {
            double second = Double.parseDouble(current);
            double result = 0;

            switch (operator) {
                case "+": result = firstNumber + second; break;
                case "-": result = firstNumber - second; break;
                case "*": result = firstNumber * second; break;
                case "/":
                    if (second == 0) {
                        display.setText("Error");
                        current = "";
                        operator = "";
                        return;
                    }
                    result = firstNumber / second;
                    break;
            }

            if (result == (long) result)
                display.setText(String.format("%d", (long) result));
            else
                display.setText(String.valueOf(result));

            current = String.valueOf(result);
            operator = "";
        } catch (NumberFormatException e) {
            display.setText("Error");
            current = "";
        }
    }
}

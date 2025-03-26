package com.example.calculator;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Stack;

public class MainActivity extends AppCompatActivity {
    Button btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btn0, btnDiv, btnMul, btnSub, btnAdd, btnEqual,
            btnAC, btnC, btnDot, btnMod, btnLeftParen, btnRightParen;
    StringBuilder sbuff = new StringBuilder("");
    TextView txtResult, txtIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

        // Clear the screen when 'AC' is clicked
        btnAC.setOnClickListener(v -> {
            sbuff = new StringBuilder();
            txtIndicator.setText("");
            txtResult.setText("0");
        });

        // Clear one character when 'C' is clicked
        btnC.setOnClickListener(v -> {
            if (sbuff.length() > 0) {
                sbuff.deleteCharAt(sbuff.length() - 1);
                txtIndicator.setText(sbuff.toString());
            }
        });

        // Button listeners for numbers and operators
        setButtonListeners();
    }

    public void init() {
        // Initialize buttons
        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.btn3);
        btn4 = findViewById(R.id.btn4);
        btn5 = findViewById(R.id.btn5);
        btn6 = findViewById(R.id.btn6);
        btn7 = findViewById(R.id.btn7);
        btn8 = findViewById(R.id.btn8);
        btn9 = findViewById(R.id.btn9);
        btn0 = findViewById(R.id.btn0);
        btnDiv = findViewById(R.id.btnDiv);
        btnMul = findViewById(R.id.btnMul);
        btnSub = findViewById(R.id.btnSub);
        btnAdd = findViewById(R.id.btnAdd);
        btnEqual = findViewById(R.id.btnEqual);
        btnAC = findViewById(R.id.btnAC);
        btnC = findViewById(R.id.btnC);
        btnDot = findViewById(R.id.btnDot);
        btnMod = findViewById(R.id.btnMod);
        btnLeftParen = findViewById(R.id.btnOpen_brace);
        btnRightParen = findViewById(R.id.btnClose_brace);
        txtResult = findViewById(R.id.txtResult);
        txtIndicator = findViewById(R.id.txtIndicator);
    }

    // Set listeners for number and operator buttons
    public void setButtonListeners() {
        btn1.setOnClickListener(this::check);
        btn2.setOnClickListener(this::check);
        btn3.setOnClickListener(this::check);
        btn4.setOnClickListener(this::check);
        btn5.setOnClickListener(this::check);
        btn6.setOnClickListener(this::check);
        btn7.setOnClickListener(this::check);
        btn8.setOnClickListener(this::check);
        btn9.setOnClickListener(this::check);
        btn0.setOnClickListener(this::check);
        btnDiv.setOnClickListener(this::check);
        btnMul.setOnClickListener(this::check);
        btnSub.setOnClickListener(this::check);
        btnAdd.setOnClickListener(this::check);
        btnMod.setOnClickListener(this::check);
        btnDot.setOnClickListener(this::check);
        btnEqual.setOnClickListener(this::check);
        btnLeftParen.setOnClickListener(this::check);
        btnRightParen.setOnClickListener(this::check);
    }

    // Check which button was pressed
    public void check(View v) {
        Button currbtn = (Button) v;
        String s = currbtn.getText().toString();
        char ch = s.charAt(0);

        if (ch == '=') {
            // Evaluate the expression when '=' is pressed
            if (sbuff.length() != 0) {
                try {
                    int result = evaluate(sbuff.toString());
                    txtResult.setText(String.valueOf(result));
                } catch (Exception e) {
                    txtResult.setText("Error");
                }
            } else {
                txtResult.setText("");
            }
            txtIndicator.setText("");
        } else {
            // Append the character to the current expression
            sbuff.append(ch);
            txtIndicator.setText(sbuff.toString());
        }
    }

    // Evaluate the expression
    public int evaluate(String expression) {
        char[] tokens = expression.toCharArray();
        Stack<Integer> values = new Stack<>();
        Stack<Character> ops = new Stack<>();

        for (int i = 0; i < tokens.length; i++) {
            if (tokens[i] == ' ') continue;

            if (tokens[i] >= '0' && tokens[i] <= '9') {
                StringBuilder sbuf = new StringBuilder();
                while (i < tokens.length && tokens[i] >= '0' && tokens[i] <= '9') {
                    sbuf.append(tokens[i++]);
                }
                values.push(Integer.parseInt(sbuf.toString()));
                i--;
            } else if (tokens[i] == '(') {
                ops.push(tokens[i]);
            } else if (tokens[i] == ')') {
                while (ops.peek() != '(') {
                    values.push(applyOp(ops.pop(), values.pop(), values.pop()));
                }
                ops.pop();
            } else if (tokens[i] == '+' || tokens[i] == '-' || tokens[i] == '*' || tokens[i] == '/' || tokens[i] == '%') {
                while (!ops.isEmpty() && hasPrecedence(tokens[i], ops.peek())) {
                    values.push(applyOp(ops.pop(), values.pop(), values.pop()));
                }
                ops.push(tokens[i]);
            }
        }

        while (!ops.isEmpty()) {
            values.push(applyOp(ops.pop(), values.pop(), values.pop()));
        }

        return values.pop();
    }

    // Determine if an operator has precedence over another
    public static boolean hasPrecedence(char op1, char op2) {
        if (op2 == '(' || op2 == ')') return false;
        if ((op1 == '*' || op1 == '/' || op1 == '%') && (op2 == '+' || op2 == '-')) return false;
        return true;
    }

    // Apply an operator to two operands
    public static int applyOp(char op, int b, int a) {
        switch (op) {
            case '+': return a + b;
            case '-': return a - b;
            case '*': return a * b;
            case '/':
                if (b == 0) throw new UnsupportedOperationException("Cannot divide by zero");
                return a / b;
            case '%': return a % b;
        }
        return 0;
    }
}

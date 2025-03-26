package com.example.calculator;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.w3c.dom.Text;

import java.util.Stack;

public class MainActivity extends AppCompatActivity {
  Button btn1 ,btn2,btn3,btn4,btn5,btn6,btn7,btn8,btn9,btn0,btnDiv,btnMul,btnSub,btnAdd,btnEqual
          ,btnAC,btnC,btnDot,btnMod;
  StringBuffer sbuff = new StringBuffer("");
TextView txtResult, txtIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        init();
        btnAC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sbuff = new StringBuffer();
                txtIndicator.setText(sbuff.toString());
                txtResult.setText("");
            }
        });
        btnC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sbuff.length()>0){
                    sbuff.deleteCharAt(sbuff.length()-1);
                    txtIndicator.setText(sbuff.toString());
                }
            }
        });
    }



public void  init(){
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
    btnSub = findViewById(R.id.btnSub);
    btnMul = findViewById(R.id.btnMul);
    btnAdd = findViewById(R.id.btnAdd);
    btnEqual = findViewById(R.id.btnEqual);
    btnAC = findViewById(R.id.btnAC);
    btnC = findViewById(R.id.btnC);
    btnDot = findViewById(R.id.btnDot);
    btnMod = findViewById(R.id.btnMod);
    txtResult = findViewById(R.id.txtResult);
    txtIndicator = findViewById(R.id.txtIndicator);

    }

public  void check(View v) {

    Button currbtn = (Button) v;

    String s = currbtn.getText().toString();
    char ch = s.charAt(0);
    if (ch == '=') {
        if(sbuff.length() != 0) {
            int result = evaluate(sbuff.toString());
            txtResult.setText(String.valueOf(result));
        }
        else txtResult.setText("");
        txtIndicator.setText("");
    }else{
        sbuff.append(ch);
        txtIndicator.setText(sbuff.toString());
    }
}




    public  int evaluate(String expression)
    {
        char[] tokens = expression.toCharArray();

        // Stack for numbers: 'values'
        Stack<Integer> values = new
                Stack<Integer>();

        // Stack for Operators: 'ops'
        Stack<Character> ops = new
                Stack<Character>();

        for (int i = 0; i < tokens.length; i++)
        {

            // Current token is a
            // whitespace, skip it
            if (tokens[i] == ' ')
                continue;

            // Current token is a number,
            // push it to stack for numbers
            if (tokens[i] >= '0' &&
                    tokens[i] <= '9')
            {
                StringBuffer sbuf = new
                        StringBuffer();

                // There may be more than one
                // digits in number
                while (i < tokens.length &&
                        tokens[i] >= '0' &&
                        tokens[i] <= '9')
                    sbuf.append(tokens[i++]);
                values.push(Integer.parseInt(sbuf.
                        toString()));

                // right now the i points to
                // the character next to the digit,
                // since the for loop also increases
                // the i, we would skip one
                // token position; we need to
                // decrease the value of i by 1 to
                // correct the offset.
                i--;
            }

            // Current token is an opening brace,
            // push it to 'ops'
            else if (tokens[i] == '(')
                ops.push(tokens[i]);

                // Closing brace encountered,
                // solve entire brace
            else if (tokens[i] == ')')
            {
                while (ops.peek() != '(')
                    values.push(applyOp(ops.pop(),
                            values.pop(),
                            values.pop()));
                ops.pop();
            }

            // Current token is an operator.
            else if (tokens[i] == '+' ||
                    tokens[i] == '-' ||
                    tokens[i] == '*' ||
                    tokens[i] == '/')
            {
                // While top of 'ops' has same
                // or greater precedence to current
                // token, which is an operator.
                // Apply operator on top of 'ops'
                // to top two elements in values stack
                while (!ops.empty() &&
                        hasPrecedence(tokens[i],
                                ops.peek()))
                    values.push(applyOp(ops.pop(),
                            values.pop(),
                            values.pop()));

                // Push current token to 'ops'.
                ops.push(tokens[i]);
            }
        }

        // Entire expression has been
        // parsed at this point, apply remaining
        // ops to remaining values
        while (!ops.empty())
            values.push(applyOp(ops.pop(),
                    values.pop(),
                    values.pop()));

        // Top of 'values' contains
        // result, return it
        return values.pop();
    }

    // Returns true if 'op2' has higher
    // or same precedence as 'op1',
    // otherwise returns false.
    public static boolean hasPrecedence(
            char op1, char op2)
    {
        if (op2 == '(' || op2 == ')')
            return false;
        if ((op1 == '*' || op1 == '/') &&
                (op2 == '+' || op2 == '-'))
            return false;
        else
            return true;
    }

    // A utility method to apply an
    // operator 'op' on operands 'a'
    // and 'b'. Return the result.
    public static int applyOp(char op,
                              int b, int a)
    {
        switch (op)
        {
            case '+':
                return a + b;
            case '-':
                return a - b;
            case '*':
                return a * b;
            case '/':
                if (b == 0)
                    throw new
                            UnsupportedOperationException(
                            "Cannot divide by zero");
                return a / b;
        }
        return 0;
    }



}
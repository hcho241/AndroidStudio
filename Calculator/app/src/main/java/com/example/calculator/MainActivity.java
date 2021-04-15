package com.example.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    // initialize variables
    EditText value;
    TextView calculation;
    Button one, two, three, four, five, six, seven, eight, nine, zero,
            addition, subtraction, multiplication, division, percentage,
            intConverter, decimal, execute, backspace, clear;
    String num1, num2, strResult, input, showCalculation;  // to implement +, -, *, and strResult is to removE .0
    double result;
    int removeChar;
    boolean add, subtract, multiply, divide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // set variables
        value = (EditText)findViewById(R.id.value);
        calculation = (TextView)findViewById(R.id.calculation);
        // numbers
        one = (Button)findViewById(R.id.one);
        two = (Button)findViewById(R.id.two);
        three = (Button)findViewById(R.id.three);
        four = (Button)findViewById(R.id.four);
        five = (Button)findViewById(R.id.five);
        six = (Button)findViewById(R.id.six);
        seven = (Button)findViewById(R.id.seven);
        eight = (Button)findViewById(R.id.eight);
        nine = (Button)findViewById(R.id.nine);
        zero = (Button)findViewById(R.id.zero);
        // operators
        addition = (Button)findViewById(R.id.addition);
        subtraction = (Button)findViewById(R.id.subtraction);
        multiplication = (Button)findViewById(R.id.multiplication);
        division = (Button)findViewById(R.id.division);
        percentage = (Button)findViewById(R.id.percentage);
        // +/-, =, clear, result
        intConverter = (Button)findViewById(R.id.intConverter);
        decimal = (Button)findViewById(R.id.decimal);
        execute = (Button)findViewById(R.id.execute);
        backspace = (Button)findViewById(R.id.backspace);
        clear = (Button)findViewById(R.id.clear);
        // error message
        Toast error = Toast.makeText(getApplicationContext(), "Enter number first", Toast.LENGTH_SHORT);
        // when user click num button, show it as a text
        one.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                value.setText(value.getText() + "1");
            }
        });
        two.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                value.setText(value.getText() + "2");
            }
        });
        three.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                value.setText(value.getText() + "3");
            }
        });
        four.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                value.setText(value.getText() + "4");
            }
        });
        five.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                value.setText(value.getText() + "5");
            }
        });
        six.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                value.setText(value.getText() + "6");
            }
        });
        seven.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                value.setText(value.getText() + "7");
            }
        });
        eight.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                value.setText(value.getText() + "8");
            }
        });
        nine.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                value.setText(value.getText() + "9");
            }
        });
        zero.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                value.setText(value.getText() + "0");
            }
        });
        // decimal (.)
        decimal.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                value.setText(value.getText() + ".");
            }
        });
        // addition (+)
        addition.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                num1 = value.getText().toString() + "";
                if (num1 == ""){
                    error.show();
                }
                else{
                    value.setText("");
                    add = true;
                }
            }
        });
        // subtraction (-)
        subtraction.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                num1 = value.getText().toString() + "";
                if (num1 == ""){
                    error.show();
                }
                else{
                    value.setText("");
                    subtract = true;
                }
            }
        });
        // multiplication (*)
        multiplication.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                num1 = value.getText().toString() + "";
                if (num1 == ""){
                    error.show();
                }
                else {
                    value.setText("");
                    multiply = true;
                }
            }
        });
        // division (/)
        division.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                num1 = value.getText().toString() + "";
                if (num1 == ""){
                    error.show();
                }
                else{
                    value.setText("");
                    divide = true;
                }
            }
        });
        // percentage (%)
        percentage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                num1 = value.getText().toString() + "";
                if (num1 == ""){
                    error.show();
                }
                else{
                    result = Double.parseDouble(num1) / 100;
                    value.setText(String.valueOf(result));
                    // update
                    calculation.setText(String.valueOf(result));
                }
            }
        });
        // intConverter (+/-)
        intConverter.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                num1 = value.getText().toString() + "";
                if (num1 == ""){
                    error.show();
                }
                else{
                    result = Double.parseDouble(num1) * -1;
                    value.setText(String.valueOf(result));
                    // update
                    calculation.setText(String.valueOf(result));
                }
            }
        });
        // update
        // backspace
        backspace.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                input = value.getText().toString();
                if(input.length() == 1){
                    value.setText("");
                }
                else if (input.length() > 1) {
                    input = input.substring(0, input.length()-1);
                    value.setText(input);
                }
            }
        });
        // execute (=)
        execute.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                num2 = value.getText().toString() + "";
                if (num2 == "")
                {
                    error.show();
                }
                else{
                    if(add){
                        result = Double.parseDouble(num1) + Double.parseDouble(num2);
                        // update
                        value.setText(num1 + "+" + num2);
                        showCalculation = num1 + "+" + num2 + "=";
                        add = false;
                    }
                    if(subtract){
                        result = Double.parseDouble(num1) - Double.parseDouble(num2);
                        value.setText(num1 + "-" + num2);
                        showCalculation = num1 + "-" + num2 + "=";
                        subtract = false;
                    }
                    if(multiply){
                        result = Double.parseDouble(num1) * Double.parseDouble(num2);
                        value.setText(num1 + "*" + num2);
                        showCalculation = num1 + "*" + num2 + "=";
                        multiply = false;
                    }
                    if(divide) {
                        result = Double.parseDouble(num1) / Double.parseDouble(num2);
                        value.setText(num1 + "/" + num2);
                        showCalculation = num1 + "/" + num2 + "=";
                        divide = false;
                    }
                    removeChar = String.valueOf(result).indexOf(".0");
                    if(removeChar != -1 && !num2.equals("0")) {
                        strResult = String.valueOf(result).substring(0, removeChar);
                        value.setText(strResult);
                        // update
                        showCalculation += strResult;
                        calculation.setText(showCalculation);
                    }
                    else if (num2.equals("0")){
                        value.setText("ERROR");
                        calculation.setText("ERROR");
                    }
                    else{
                        value.setText(String.valueOf(result));
                    }
                }
            }
        });
        // clear
        clear.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                num1 = "";
                num2 = "";
                result = 0;
                value.setText("");
                calculation.setText("");
            }
        });
    }
}
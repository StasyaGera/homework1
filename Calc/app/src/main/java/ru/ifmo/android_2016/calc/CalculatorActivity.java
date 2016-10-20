package ru.ifmo.android_2016.calc;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public final class CalculatorActivity extends Activity {
    public CalcData calcData = new CalcData();
    public TextView input, result;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("input", input.getText().toString());
        outState.putString("result", result.getText().toString());
        outState.putSerializable("calcData", calcData);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        input.setText(savedInstanceState.getString("input"));
        result.setText(savedInstanceState.getString("result"));
        calcData = (CalcData) savedInstanceState.getSerializable("calcData");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        input = (TextView) findViewById(R.id.result);
        input.addTextChangedListener(new CalculatorTextWatcher((HorizontalScrollView) findViewById(R.id.inputScroll)));

        result = (TextView) findViewById(R.id.temporary);
        result.addTextChangedListener(new CalculatorTextWatcher((HorizontalScrollView) findViewById(R.id.resultScroll)));

        setNumericKeys((TableLayout) findViewById(R.id.numeric), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (calcData.number.toString().equals("0")) {
                    calcData.number.setLength(0);
                }
                calcData.number.append(view.getTag());
        
                if (!calcData.operation.equals(Operation.NONE)) {
                    try {
                        result.setText(calcData.format(calcData.operation.calc(calcData.a, Double.parseDouble(calcData.number.toString()))));
                    } catch (DivByZeroException e) {
                        result.setText(getString(R.string.error));
                    }
                }
        
                input.setText(calcData.updateInput());
            }
        });

        setOperationBtn(R.id.add, Operation.ADD);
        setOperationBtn(R.id.sub, Operation.SUB);
        setOperationBtn(R.id.mul, Operation.MUL);
        setOperationBtn(R.id.div, Operation.DIV);

        Button clear = (Button) findViewById(R.id.clear);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!calcData.number.toString().isEmpty()) {
                    if (calcData.number.charAt(calcData.number.length() - 1) == '.') {
                        calcData.dot = false;
                    }
                    calcData.number.deleteCharAt(calcData.number.length() - 1);

                    if (!calcData.operation.equals(Operation.NONE)) {
                        if (calcData.number.length() == 0) {
                            result.setText("");
                        } else {
                            try {
                                result.setText(calcData.format(calcData.operation.calc(calcData.a, Double.parseDouble(calcData.number.toString()))));
                            } catch (DivByZeroException e) {
                                result.setText(getString(R.string.error));
                            }
                        }
                    }
                } else if (!calcData.operation.equals(Operation.NONE)) {
                    calcData.operation = Operation.NONE;
                    calcData.number.append(calcData.format(calcData.a));
                    calcData.a = 0;
                } else {
                    result.setText("");
                }

                input.setText(calcData.updateInput());
            }
        });
        clear.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                calcData.reset(0);

                input.setText("");
                result.setText("");
                return true;
            }
        });
        
        Button eqv = (Button) findViewById(R.id.eqv);
        eqv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (calcData.number.length() == 0)
                    return;

                try {
                    calcData.a = calcData.operation.calc(calcData.a, Double.parseDouble(calcData.number.toString()));
                    input.setText(calcData.format(calcData.a));
                    result.setText("");
                    calcData.reset(calcData.a);
                } catch (DivByZeroException e) {
                    input.setText(getString(R.string.error));
                    result.setText("");
                    calcData.reset(0);
                }
            }
        });
    }

    protected void setOperationBtn(int id, final Operation math) {
        Button btn = (Button) findViewById(id);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (calcData.number.length() != 0) {
                    result.setText("");
                    try {
                        calcData.a = calcData.operation.calc(calcData.a, Double.parseDouble(calcData.number.toString()));

                        if (!calcData.operation.equals(Operation.NONE)) {
                            result.setText(calcData.format(calcData.a));
                        }
                    } catch (DivByZeroException e) {
                        input.setText(R.string.error);
                        calcData.reset(0);
                    }
                } else if (result.getText().length() != 0) {
                    calcData.a = Double.parseDouble(result.getText().toString());
                    result.setText("");
                }

                calcData.number.setLength(0);
                calcData.operation = math;
                calcData.dot = false;

                input.setText(calcData.updateInput());
            }
        });
    }

    protected void setNumericKeys(TableLayout table, View.OnClickListener listener) {
        for (int i = 7; i > 0; i -= 3) {
            TableRow tableRow = new TableRow(table.getContext());
            tableRow.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 1));
            tableRow.setWeightSum(3);
            for (int j = i; j < i + 3; j++) {
                setBtn(tableRow, listener, "" + j, j, getResources().getIntArray(R.array.numeric_buttons)[j], 40, 1);
            }
            table.addView(tableRow);
        }

        TableRow tableRow = new TableRow(table.getContext());
        tableRow.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT, 1));
        tableRow.setWeightSum(3);

        setBtn(tableRow, listener, getString(R.string.d0), 0, getResources().getIntArray(R.array.numeric_buttons)[0], 40, 2);
        setBtn(tableRow, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (calcData.dot)
                    return;

                if (calcData.number.length() == 0){
                    calcData.number.append("0.");
                    input.append("0.");
                } else {
                    calcData.number.append(".");
                    input.append(".");
                }
                calcData.dot = true;
            }
        }, getString(R.string.dot), -1, -1, 40, 1);

        table.addView(tableRow);
    }

    void setBtn(TableRow tableRow, View.OnClickListener listener, String text, int tag, int id, int textSize, int weight) {
        Button button = new Button(tableRow.getContext());
        button.setTag(tag);
        button.setId(id);

        button.setOnClickListener(listener);

        button.setText(text);
        button.setTextSize(textSize);
        button.setTextColor(getResources().getColorStateList(R.color.numeric_text));

        button.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, weight));
        button.setBackgroundResource(R.drawable.numeric_button);

        tableRow.addView(button);
    }
}
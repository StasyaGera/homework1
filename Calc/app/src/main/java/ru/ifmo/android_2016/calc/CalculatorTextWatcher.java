package ru.ifmo.android_2016.calc;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;

/**
 * Created by penguinni on 19.10.16.
 */

class CalculatorTextWatcher implements TextWatcher {
    private HorizontalScrollView scrollView;

    CalculatorTextWatcher(HorizontalScrollView scrollView) {
        this.scrollView = scrollView;
    }

    @Override
    public void afterTextChanged(Editable arg0) {
        scrollView.fullScroll(ScrollView.FOCUS_RIGHT);
    }

    @Override
    public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}

    @Override
    public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
        scrollView.fullScroll(ScrollView.FOCUS_RIGHT);
    }
}

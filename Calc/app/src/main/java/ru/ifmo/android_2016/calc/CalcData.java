package ru.ifmo.android_2016.calc;

import java.io.Serializable;
import java.text.DecimalFormat;

/**
 * Created by penguinni on 19.10.16.
 */

class CalcData implements Serializable {
    double a = 0;
    StringBuilder number = new StringBuilder("");
    Operation operation = Operation.NONE;
    boolean dot = false;

    String updateInput() {
        if (operation.equals(Operation.NONE)) {
            return number.toString();
        } else {
            return format(a) + operation.print() + number.toString();
        }
    }

    void reset(double num) {
        a = num;
        operation = Operation.NONE;
        dot = false;
        number.setLength(0);
    }

    String format(double num) {
        if ((long) num == num) {
            return Long.valueOf((long) num).toString();
        } else {
            return new DecimalFormat("#.######").format(num);
        }
    }
}
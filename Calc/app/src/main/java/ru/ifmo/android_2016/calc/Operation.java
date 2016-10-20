package ru.ifmo.android_2016.calc;

import java.io.Serializable;

/**
 * Created by penguinni on 19.10.16.
 */

abstract class Operation implements Serializable {
    public static final Operation NONE = new Operation() {
        @Override
        public String print() { return ""; }

        @Override
        public double calc(double a, double b) { return b; }
    };
    public static final Operation ADD  = new Operation() {
        @Override
        public String print() {
            return "+";
        }

        @Override
        public double calc(double a, double b) {
            return a + b;
        }
    };
    public static final Operation SUB  = new Operation() {
        @Override
        public String print() {
            return "-";
        }
        @Override
        public double calc(double a, double b) {
            return a - b;
        }
    };
    public static final Operation MUL  = new Operation() {
        @Override
        public String print() {
            return "*";
        }

        @Override
        public double calc(double a, double b) {
            return a * b;
        }
    };
    public static final Operation DIV  = new Operation() {
        @Override
        public String print() {
            return "/";
        }

        @Override
        public double calc(double a, double b) {
            if (b == 0) {
                throw new DivByZeroException();
            }
            return a / b;
        }
    };

    public abstract String print();
    public abstract double calc(double a, double b);
    boolean equals(Operation other) { return this.print().equals(other.print()); }
}
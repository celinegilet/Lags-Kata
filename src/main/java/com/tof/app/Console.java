package com.tof.app;

public class Console {

    public void println(String data) {
        System.out.println(data);
    }

    public void print(String data) {
        System.out.print(data);
    }

    public void format(String format, Object ... args) {
        System.out.format(format, args);
    }

    public String readLine() {
        return System.console().readLine().toUpperCase();
    }

}

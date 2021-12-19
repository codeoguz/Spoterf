package com.example.freespotify;

import java.util.ArrayList;
import java.util.Scanner;

public class generateCode {

    private static String allSymbols = "qwertyuiopASDFGHJKLZXCVBNMqwertyuıopasdfghjklizxcvbnm";
    private static ArrayList<Character> charSequence;
    private static String result;
    private static int hardness;
    private static String firstExpression;
    private static String secondExpression;

    public static String createEncryptedCode(String firstExpression, String secondExpression, int hardness) {
        result = String.valueOf(getSymbol());
        for(int i = 1; i < hardness; i++){
            result += getSymbol();
        }
        if(hardness < 35){
            result += firstExpression;
        } else {
            result += secondExpression;
        }
        for(int i = 1; i < hardness; i++){
            result += getSymbol();
        }
        if(hardness > 35){
            result += firstExpression;
        } else {
            result += secondExpression;
        }
        for(int i = 1; i < hardness; i++){
            result += getSymbol();
        }
        return result;
    }
    private static char getSymbol(){
        int randomNumber = getRandomNumber(1, allSymbols.length());
        return allSymbols.charAt(randomNumber);
    }
    public static int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
}

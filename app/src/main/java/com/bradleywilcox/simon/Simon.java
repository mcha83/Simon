package com.bradleywilcox.simon;

import java.util.ArrayList;
import java.util.Random;

/**
 * Bradley Wilcox / Michael Cha
 * CSCI 4020
 * Assignment 2
 */

public class Simon {

    public enum GameMode{
        normal,
        backwards,
        turbo
    }

    public enum Buttons{
        green,
        red,
        yellow,
        blue
    }

    private ArrayList<Integer> pattern;
    private Random rand;

    private int playerPressIndex;
    private boolean isPlayersTurnOver;
    private GameMode mode;

    public Simon(GameMode mode){
        pattern = new ArrayList<>();
        rand = new Random();
        playerPressIndex = 0;
        this.mode = mode;

        // initial first value
        addToPattern();
    }

    public ArrayList<Integer> getPattern(){
        return this.pattern;
    }

    public void addToPattern(){
        pattern.add(rand.nextInt(4));
    }

    public boolean isPressCorrect(Buttons button){
        if(mode == GameMode.normal || mode == GameMode.turbo)
            return isPressCorrectNormal(button);

        if(mode == GameMode.backwards)
            return isPressCorrectBackward(button);

        return false;
    }

    private boolean isPressCorrectNormal(Buttons button){
        int buttonNum = button.ordinal();
        boolean isCorrect = pattern.get(playerPressIndex) == buttonNum;
        playerPressIndex++;

        if(playerPressIndex == pattern.size()) {
            playerPressIndex = 0;
            isPlayersTurnOver = true;
        }else{
            isPlayersTurnOver = false;
        }

        return isCorrect;
    }

    private boolean isPressCorrectBackward(Buttons button){
        int buttonNum = button.ordinal();
        playerPressIndex++;

        boolean isCorrect = pattern.get(pattern.size() - playerPressIndex) == buttonNum;
        if(playerPressIndex == pattern.size()){
            playerPressIndex = 0;
            isPlayersTurnOver = true;
        }else{
            isPlayersTurnOver = false;
        }

        return isCorrect;
    }

    public boolean isPlayerTurnOver(){
        return isPlayersTurnOver;
    }

    public GameMode getGameMode(){
        return this.mode;
    }

}

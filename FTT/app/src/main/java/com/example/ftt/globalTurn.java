package com.example.ftt;

import android.app.Application;

public class globalTurn extends Application {
    int turn, end, nbTeams, type, timer;
    int [] points = new int[6];
    int [] read = new int[19];
    boolean gameCanceled, btnClicked;

    void setTimer(){
        this.timer = 1;
    }

    int getTimer(){
        return this.timer;
    }

    void startTimer(){
        this.timer = 0;
    }

    int getTurn(){
        return turn;
    }

    void setTurn(int turn){
        this.turn = turn;
    }

    void initPoints(int team){
        for (int i=1; i<team; i++){
            this.points[i] = 0;
        }
    }

    int getPoints(int team){
        return points[team];
    }

    void addPoints(int team){
        this.points[team]+=1;
    }

    int [] getWinner(){
        int winner = 0, max = 0, j=0;
        int [] winners = new int[6];
        for (int i=1; i<this.points.length; i++){
            if (max <= this.points[i]){
                winner = i;
                max = this.points[i];
            }
        }
        for (int i=1; i<this.points.length; i++){
            if (i == winner){
                winners[j] = this.points[i];
                j++;
            }
        }
        return winners;
    }

    void setEnd(){
        this.end = 0;
    }

    int getEnd(){
        return end;
    }

    void addEnd(){
        this.end+=1;
    }

    void initRead(){
        for (int i=1; i<19; i++){
            this.read[i] = 0;
        }
    }

    void setRead(int story){
        this.read[story] = 1;
    }

    int checkRead(int story){
        if (this.read[story] == 1){
            return 1;
        }
        else {
            return 0;
        }
    }

    void setNbTeams(int teams){
        this.nbTeams = teams;
    }

    int getNbTeams(){
        return nbTeams;
    }

    void setType(int type){
        this.type = type;
    }

    int getType(){
        return this.type;
    }

    void setGameCanceled(){
        this.gameCanceled = false;
    }

    void gameIsCanceled(){
        this.gameCanceled = true;
    }

    boolean getGameCanceled(){
        return this.gameCanceled;
    }

    void setBtnClicked (){
        this.btnClicked = false;
    }

    void btnIsClicked(){
        this.btnClicked = true;
    }

    boolean getBtnClicked(){
        return this.btnClicked;
    }
}

package com.example.ftt;

import java.util.Random;

public class randomCards {
    // Returns a random card to display
    public static int randomC (int max){
        Random random = new Random();
        int res = random.nextInt(max);
        return res+1;
    }
}

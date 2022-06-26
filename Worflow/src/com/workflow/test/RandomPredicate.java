package com.workflow.test;

import com.workflow.test.framework.Predicate;

import java.util.Random;

public class RandomPredicate implements Predicate<Void> {
    Random rd = new Random(); // creating Random object
    @Override
    public boolean test(Void input) {
        boolean randomBoolean = rd.nextBoolean();
        System.out.println( "randomBoolean" + randomBoolean) ;
        return randomBoolean;
    }
}

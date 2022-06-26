package com.workflow.test;

import com.workflow.test.reusable.TimeStamp;

import java.util.Random;

public class Simulation{
    int num_tick_duration;
    int tick = 0;

    public Simulation()
    {
        num_tick_duration = genRandomTicksDuration();
        //SS System.out.println("genRandgenRandomTicksDurationomSecTime " + num_tick_duration);
    }

    public void update()
    {
        tick++;
    }

    public boolean isTerminate()
    {
        return tick > num_tick_duration;
    }

    //SS generà un numero casuale da 1 a 10
    private int genRandomTicksDuration()
    {
        int min = 1;
        int max = 10;

        Random random = new Random();

        int value = random.nextInt(max + min) + min;
        return value;
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package markovbw;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Piotr
 */
public class MarkovBW {

    public static void main(String[] args) {
        HMMBW<Double> container = new HMMBW<Double>(10, 10);
        List states = new ArrayList<Double>();
        states.add(0.95);
        states.add(0.9);
        states.add(0.1);
        states.add(0.05);
        
        List probA = new ArrayList<Double>();
        for(int i = 0; i < 6; i++)
            probA.add(1/6);
        
        List probE = new ArrayList<Double>();
        for(int i = 0; i < 5; i++)
            probE.add(1/10);
        probE.add(1/2);
        
        int[][] sequences = new int[][] 
        {
            new int[] { 0,1,1,1,1,0,1,1,1,1 },
            new int[] { 0,1,1,1,0,1,1,1,1,1 },
            new int[] { 0,1,1,1,1,1,1,1,1,1 },
            new int[] { 0,1,1,1,1,1         },
            new int[] { 0,1,1,1,1,1,1       },
            new int[] { 0,1,1,1,1,1,1,1,1,1 },
            new int[] { 0,1,1,1,1,1,1,1,1,1 },
        };
        
        
        container.setAllCollections(states, probA, probE);
        container.learnBW(sequences);
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package markovbw;

import java.io.Console;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author Piotr
 */
public class HMMBW<T> {

    private List<T> stateCollection;
    private List<Double> probACollection;
    private List<Double> probECollection;
    private int [][] sequence;
    private Integer iterations;
    private Integer maxDeadline;
    
    public HMMBW(int endOfIteration, int deadline) {
        this.iterations = endOfIteration;
        this.maxDeadline = deadline;
    }
    
    public void setAllCollections(List<T> col, List<Double> col2, List<Double> col3)
    {
        this.stateCollection = col;
        this.probACollection = col2;
        this.probECollection = col3;
    }
    
    private List<T> prefixProcedure() {
        List prefixValues;
        prefixValues = new ArrayList();
        
        for(int i = 0; i < this.stateCollection.size(); i++) {
            prefixValues.add(0.0);
        }
        
        prefixAlgorithm(prefixValues);
        
        return prefixValues;
    }
    
    private void prefixAlgorithm(List<Double> prefixValues) {
        for(int k = 0; k < sequence.length; k++) {
            for(int i = 0; i < this.stateCollection.size(); i++) {
                double sum = 0.0;
                for(int j = 0; j < this.stateCollection.size(); j++) {
                    sum += prefixValues.get(j) * this.probACollection.get(j); 
                }
                prefixValues.set(i, sum * this.probECollection.get(i));
            }
        }
    }
    
    private double sufixProcedure() {
        double fk = 0 ;
        
        return fk;
    }
    
    private void updateAandE() {
        
    }
    
    
    private void calculateAandEb() {
        
    }
    
    public void learnBW(int [][] seq) {
        if(seq.length == 0) { return; }
        
        int j = 0;
        int l = 0;
        do {
            for(int i = 0; i < this.iterations; i++) {
                prefixProcedure();
                sufixProcedure();
                updateAandE();
            }
            calculateAandEb();
            l = this.maxDeadline;
            j++;
        }while(j < this.iterations || l < maxDeadline);
    }
    
    public void print(String string) {
        System.out.println(string);
    }
}

/*
 * Main.java
 *
 * Created on 21 avril 2006, 17:02
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package DynamicHmm;

/**
 *
 * @author AL
 */
import java.lang.*;
import java.util.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.io.*;

import DynamicHmm.BaumWelchLearner;

import be.ac.ulg.montefiore.run.jahmm.*;
import be.ac.ulg.montefiore.run.jahmm.Hmm;
import be.ac.ulg.montefiore.run.jahmm.ViterbiCalculator;
import be.ac.ulg.montefiore.run.jahmm.ObservationVector;
import be.ac.ulg.montefiore.run.jahmm.OpdfMultiGaussian;
import be.ac.ulg.montefiore.run.jahmm.draw.GenericHmmDrawerDot;

import be.ac.ulg.montefiore.run.jahmm.toolbox.KullbackLeiblerDistanceCalculator;
import be.ac.ulg.montefiore.run.jahmm.toolbox.MarkovGenerator;

import weka.core.*;

public class Dynamic {
    
    /** Creates a new instance of Main */
    public Dynamic() {
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] argv) {
        
        Instances instances;//, secondInstances, train, test, transformed, empty;
        //Instance instance;
        //Random random = new Random(2);
        Reader reader;
        //int start, num;
        //double newWeight;
        //FastVector testAtts, testVals;
        int i,j,k;
        
        try{
            
//            testVals = new FastVector(2);
//            testVals.addElement("first_value");
//            testVals.addElement("second_value");
//            testAtts = new FastVector(2);
//            testAtts.addElement(new Attribute("nominal_attribute", testVals));
//            testAtts.addElement(new Attribute("numeric_attribute"));
//            instances = new Instances("test_set", testAtts, 10);
//            instances.add(new Instance(instances.numAttributes()));
//            instances.add(new Instance(instances.numAttributes()));
//            instances.add(new Instance(instances.numAttributes()));
//            instances.setClassIndex(0);
//            System.out.println("\nSet of instances created from scratch:\n");
//            System.out.println(instances);
            
            String filename = argv[0];
	    reader = new FileReader(filename);
            
            // Read all the instances in the file
            reader = new FileReader(filename);
            instances = new Instances(reader);
            //System.out.println("Now it becomes serious");
            j = 0;
            k = 0;
            ArrayList seqOfSeq = new ArrayList();
            //System.out.println("Now it becomes serious");
            double [] vec = new double[8];
            //System.out.println("Now it becomes serious");
            for (i = 1; i < (int)instances.lastInstance().value(1); i++) {
                //System.out.println("Now it becomes serious: " + i);
                while ( instances.instance(j).value(1) == i ) {
                    vec[k] = instances.instance(j).value(0);
                    j++;
                    k++;
                }
                ObservationVector sequence = new ObservationVector(vec);
                //System.out.println("Observation vector: " + vec);
                seqOfSeq.add(sequence);
                //System.out.println("Enumeration of Sequence: " + seqOfSeq);
                k = 0;
            }
            
            //System.out.println("Now it becomes serious");
        
            
            System.out.println("Enumeration of Sequence: " + seqOfSeq);
            System.out.println("Print first sequence in seqOfSeq: " 
                                                            + seqOfSeq.get(0));
           
            
            int arbitrary = 8;
            
            ObservationVector obs = new ObservationVector(8);
 
            
            System.out.println("Observation vector dimension: " + obs.dimension());
            System.out.println("Observation value at 2: " + obs.value(1));
            System.out.println("Observation vector: " + seqOfSeq);
                
            Hmm<ObservationVector, OpdfMultiGaussian> wekaHmm = 
                        new Hmm<ObservationVector, OpdfMultiGaussian>(arbitrary,
                            new OpdfMultiGaussianFactory(8));
            
            
             Hmm<ObservationVector,  ?> learntHmm = 
                        new Hmm<ObservationVector, OpdfMultiGaussian>(arbitrary,
                            new OpdfMultiGaussianFactory(8));
             
               
            
            BaumWelchLearner<ObservationVector,OpdfMultiGaussian> bwl =
			new BaumWelchLearner<ObservationVector,
                            OpdfMultiGaussian>(new OpdfMultiGaussianFactory(8),seqOfSeq);
            
            System.out.println("BaumWelch is initialized");
            
            
            for (i = 0; i < 5; i++) { 
                learntHmm = bwl.iterate(learntHmm);
            }
            System.out.println("Learning Done");
            
            System.out.println();
            System.out.println("State transition probability distribution : ");
            for (int m=0; m<learntHmm.nbStates(); m++) {
                for (int n=0; n<learntHmm.nbStates(); n++) {
                    if (n==learntHmm.nbStates()-1)
                        System.out.println("(" + learntHmm.getAij(m,n) + ")");
                    else
                        System.out.print("(" + learntHmm.getAij(m,n) + ")");
                }
            }
            
            System.out.println("Printing Done");
            
            ViterbiCalculator<ObservationVector> viterbi = 
                    new ViterbiCalculator<ObservationVector>(seqOfSeq,wekaHmm); 
            
           
      
            
        }catch (Exception e) {
            e.printStackTrace(); 
    }
        
  }
} 


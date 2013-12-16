/* jahmm package - v0.5.0 */

/*
  *  Copyright (c) 2004, Jean-Marc Francois.
 *
 *  This file is part of Jahmm.
 *  Jahmm is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  Jahmm is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Jahmm; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

//package be.ac.ulg.montefiore.run.jahmm.learn;
package DynamicHmm;

import java.util.*;

import be.ac.ulg.montefiore.run.jahmm.*;
import DynamicHmm.KMeansLearner;


/**
 * An implementation of the Baum-Welch learning algorithm.
 */
public class BaumWelchLearner<O extends Observation, D extends Opdf<O>> {
	
	// Number of iterations ; must be >= 1
	static private final int NB_ITERATIONS = 9;
	
	//protected List<? extends List<? extends O>> sequences;
        protected ArrayList sequences;
	protected OpdfFactory<? extends D> opdfFactory;
	
	
	/**
	 * Initializes a Baum-Welch algorithm implementation.  This algorithm
	 * finds a HMM that models a set of observation sequences.
	 *
	 * @param opdfFactory A class that builds the observation probability
	 *                    distributions associated to the states of the HMM.
	 * @param sequences A vector of observation sequences.  Each observation
	 *                sequences is a vector of
	 *                {@link be.ac.ulg.montefiore.run.jahmm.Observation
	 *                observations}.
	 */
	//public BaumWelchLearner(OpdfFactory<? extends D> opdfFactory,
	//		List<? extends List<? extends O>> sequences) {
	public BaumWelchLearner(OpdfFactory<? extends D> opdfFactory,
			ArrayList sequences) {
		
		if (opdfFactory == null || sequences.isEmpty())
			throw new IllegalArgumentException();
		
		this.sequences = sequences;
		this.opdfFactory = opdfFactory;
	}
	
	
	/**
	 * Performs one iteration of the Baum-Welch algorithm.
	 * In one iteration, a new HMM is computed using a previously estimated
	 * HMM.
	 *
	 * @param hmm A previously estimated HMM.
	 * @return A new, updated HMM.
	 */
	public Hmm<O,D> iterate(Hmm<O,?> hmm) {
		
		Hmm<O,D> nhmm = new Hmm<O,D>(hmm.nbStates(), opdfFactory);
		
		/* gamma and xi arrays are those defined by Rabiner and Juang */
		/* allGamma[n] = gamma array associated to observation sequence n */
		double allGamma[][][] = new double[sequences.size()][][];
		
		/* a[i][j] = aijNum[i][j] / aijDen[i] */
		double aijNum[][] = new double[hmm.nbStates()][hmm.nbStates()];
		double aijDen[] = new double[hmm.nbStates()];
		
		Arrays.fill(aijDen, 0.);
		for (int i = 0; i < hmm.nbStates(); i++)
			Arrays.fill(aijNum[i], 0.);
		
		int g = 0;
                System.out.println("Sequence at this point:" + sequences); 
                System.out.println("Sequence Size dans bwl:" + sequences.size());
		//for (List<? extends O> obsSeq : sequences) {
                // should a sequence contain into a sequence
                for (int m=0; m<sequences.size(); m++) {
			ForwardBackwardCalculator fbc = 
				generateForwardBackwardCalculator(sequences, hmm);
			System.out.println("Sti jpense que ca marche");
			double xi[][][] = estimation_xi(sequences, fbc, hmm);
			double gamma[][] = allGamma[g++] = estimation_gamma(xi, fbc);
			
			for (int i = 0; i < hmm.nbStates(); i++)
				for (int t = 0; t < sequences.size() - 1; t++) {
					aijDen[i] += gamma[t][i];
					
					for (int j = 0; j < hmm.nbStates(); j++)
						aijNum[i][j] += xi[t][i][j];
				}
		}
		
		for (int i = 0; i < hmm.nbStates(); i++) 
			for (int j = 0; j < hmm.nbStates(); j++)
				nhmm.setAij(i, j, aijNum[i][j] / aijDen[i]);
		
		
		/* pi computation */
		for (int i = 0; i < hmm.nbStates(); i++)
			nhmm.setPi(i, 0.);
		
                
		for (int o = 0; o < sequences.size(); o++) {
                    
                        System.out.println("hmm.nbStates:" + hmm.nbStates());
     
			for (int i = 0; i < hmm.nbStates(); i++) {
                                
                                System.out.print("o:" + o);
                                System.out.println("i:" + i);
                                System.out.println("allGamma:" + allGamma[o][0][i]);
                                System.out.println("Pi:" + nhmm.getPi(i));
				nhmm.setPi(i,nhmm.getPi(i) + allGamma[o][0][i] / sequences.size());
                        }
                }
		
		
		/* pdfs computation */
		for (int i = 0; i < hmm.nbStates(); i++) {
			//List<O> observations = KMeansLearner.flat(sequences);
                        ArrayList observations = KMeansLearner.flat(sequences);
			double[] weights = new double[observations.size()];
			double sum = 0.;
			int j = 0;
			
			int o = 0;
                        
                        //for (List<? extends O> obsSeq : sequences) {
                        // should be a sequences contained into the sequence
			for (int m=0; m<sequences.size()-1; m++) {
				for (int t = 0; t < sequences.size(); t++, j++)
					sum += weights[j] = allGamma[o][t][i];
				o++;
			}
			
			for (j--; j >= 0; j--)
				weights[j] /= sum;
			
			D opdf = opdfFactory.factor();
			opdf.fit(observations, weights);
			nhmm.setOpdf(i, opdf);
		}
		
		return nhmm;
	}
	
	
	protected ForwardBackwardCalculator
	//generateForwardBackwardCalculator(List<? extends O> sequence,
        generateForwardBackwardCalculator(ArrayList sequence,
			Hmm<O,?> hmm) {
		
		return new ForwardBackwardCalculator(sequence, hmm, 
				EnumSet.allOf(ForwardBackwardCalculator.Computation.class));
	}
	
	
	/**
	 * Does a fixed number of iterations of the Baum-Welch algorithm.
	 * 
	 * @param initialHmm An initial estimation of the expected HMM.  This
	 *                   estimate is critical as the Baum-Welch algorithm
	 *                   only find local minima of its likelihood function.
	 * @return The HMM that best matches the set of observation sequences given
	 *         (according to the Baum-Welch algorithm).
	 */
	public Hmm<O,D> learn(Hmm<O,?> initialHmm) {
		Hmm<O,D> hmm = iterate(initialHmm);
		
		for (int i = 0; i < NB_ITERATIONS - 1; i++)
			hmm = iterate(hmm);
		
		return hmm;
	}
	
	
	protected double[][][] estimation_xi(List<? extends O> sequence, 
			ForwardBackwardCalculator fbc, Hmm<O,?> hmm) {
		
		if (sequence.size() <= 1)
			throw new IllegalArgumentException("Observation sequence too " + 
			"short");
		
		double xi[][][] = 
			new double[sequence.size() - 1][hmm.nbStates()][hmm.nbStates()];
		double probability = fbc.probability();
		
		Iterator<? extends O> seqIterator = sequence.iterator();
		seqIterator.next();
		
		for (int t = 0; t < sequence.size() - 1; t++) {
			O observation = seqIterator.next();
			
			for (int i = 0; i < hmm.nbStates(); i++)
				for (int j = 0; j < hmm.nbStates(); j++)
					xi[t][i][j] = fbc.alphaElement(t, i) *
					hmm.getAij(i, j) * 
					hmm.getOpdf(j).probability(observation) *
					fbc.betaElement(t + 1, j) / probability;
		}
		
		return xi;
	}
	
	
	/* gamma[][] could be computed directly using the alpha and beta
	 arrays, but this (slower) method is prefered because it doesn't
	 change if the xi array has been scaled (and should be changed with
	 the scaled alpha and beta arrays).
	 */
	protected double[][] estimation_gamma(double[][][] xi, 
			ForwardBackwardCalculator fbc) {
		double[][] gamma = new double[xi.length + 1][xi[0].length];
		
		for (int t = 0; t < xi.length + 1; t++)
			Arrays.fill(gamma[t], 0.);
		
		for (int t = 0; t < xi.length; t++)
			for (int i = 0; i < xi[0].length; i++)
				for (int j = 0; j < xi[0].length; j++)
					gamma[t][i] += xi[t][i][j];
		
		for (int j = 0; j < xi[0].length; j++)
			for (int i = 0; i < xi[0].length; i++)
				gamma[xi.length][j] += xi[xi.length - 1][i][j];
		
		return gamma;
	}
}

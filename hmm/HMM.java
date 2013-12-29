package javaapplication2;

import java.text.*;

public class HMM {
    
  private int numberOfStates;
  private int emisionSize;
  private double likehoodSetByUser;
  private double likehoodCalculated;
  
  private double initialStateProb[];
  private double transitionProb[][];
  private double emissionProb[][];

  /* initializes an HMM.
      @param numberOfStates number of states
      @param numberOfIterationsTrain size of output vocabulary 
  */
  public HMM(int numberOfStates, int emisionSize, double likehood) {
    this.numberOfStates = numberOfStates;
    this.emisionSize = emisionSize;
    this.likehoodSetByUser = likehood;
    this.likehoodCalculated = likehood;
    
    this.initialStateProb = new double[numberOfStates];
    this.transitionProb = new double[numberOfStates][numberOfStates];
    this.emissionProb = new double[numberOfStates][emisionSize];
  }

  /** implementation of the Baum-Welch Algorithm for HMMs.
      @param o the training set
      @param steps the number of steps
  */
  public void train(int[] o, int steps) {
    int T = o.length;
    double[][] fwd;
    double[][] bwd;

    double newInitialStateProb[] = new double[numberOfStates];
    double newTransitionProb[][] = new double[numberOfStates][numberOfStates];
    double newEmissionProb[][] = new double[numberOfStates][emisionSize];

    for (int s = 0; s < steps; s++) {
      /* calculation of Forward- und Backward Variables from the
	 current model */
      fwd = forwardProc(o);
      bwd = backwardProc(o);

      /* re-estimation of initial state probabilities */
      for (int i = 0; i < numberOfStates; i++)
	newInitialStateProb[i] = gamma(i, 0, o, fwd, bwd);

      /* re-estimation of transition probabilities */ 
      for (int i = 0; i < numberOfStates; i++) {
	for (int j = 0; j < numberOfStates; j++) {
	  double num = 0;
	  double denom = 0;
	  for (int t = 0; t <= T - 1; t++) {
	    num += p(t, i, j, o, fwd, bwd);
	    denom += gamma(i, t, o, fwd, bwd);
	  }
	  newTransitionProb[i][j] = divide(num, denom);
	}
      }
      
      /* re-estimation of emission probabilities */
      for (int i = 0; i < numberOfStates; i++) {
	for (int k = 0; k < emisionSize; k++) {
	  double num = 0;
	  double denom = 0;
	  
	  for (int t = 0; t <= T - 1; t++) {
	    double g = gamma(i, t, o, fwd, bwd);
	    num += g * (k == o[t] ? 1 : 0);
	    denom += g;
	  }
	  newEmissionProb[i][k] = divide(num, denom);
	}
      }
      this.initialStateProb = newInitialStateProb;
      this.transitionProb = newTransitionProb;
      this.emissionProb = newEmissionProb;
      
      calculateLikehood();
    }
  }
  

  /** calculation of Forward-Variables f(i,t) for state i at time
      t for output sequence O with the current HMM parameters
      @param o the output sequence O
      @return an array f(i,t) over states and times, containing
              the Forward-variables. 
  */
  public double[][] forwardProc(int[] observateSequence) {
    int T = observateSequence.length;
    double[] pi = this.initialStateProb;
    double[][] a = this.transitionProb;
    double[][] b = this.emissionProb;
    double[][] fwd = new double[numberOfStates][T];
        
    /* initialization (time 0) */
    for (int i = 0; i < numberOfStates; i++)
      fwd[i][0] = pi[i] * b[i][observateSequence[0]];

    /* induction */
    for (int t = 0; t <= T-2; t++) {
      for (int j = 0; j < numberOfStates; j++) {
	fwd[j][t+1] = 0;
	for (int i = 0; i < numberOfStates; i++)
	  fwd[j][t+1] += (fwd[i][t] * a[i][j]);

	fwd[j][t+1] *= b[j][observateSequence[t+1]];
      }
    }

    return fwd;
  }
  
  /** calculation of  Backward-Variables b(i,t) for state i at time
      t for output sequence O with the current HMM parameters
      @param o the output sequence O
      @return an array b(i,t) over states and times, containing
              the Backward-Variables. 
  */
  public double[][] backwardProc(int[] observateSequence) {
    int T = observateSequence.length;
    double[] pi = this.initialStateProb;
    double[][] a = this.transitionProb;
    double[][] b = this.emissionProb;
    double[][] bwd = new double[numberOfStates][T];
        
    /* initialization (time 0) */
    for (int i = 0; i < numberOfStates; i++)
      bwd[i][T-1] = 1;

    /* induction */
    for (int t = T - 2; t >= 0; t--) {
      for (int i = 0; i < numberOfStates; i++) {
	bwd[i][t] = 0;
	for (int j = 0; j < numberOfStates; j++)
	  bwd[i][t] += (bwd[j][t+1] * a[i][j] * b[j][observateSequence[t+1]]);
      }
    }

    return bwd;
  }

  /** calculation of probability P(X_t = s_i, X_t+1 = s_j | O, m).
      @param t time t
      @param i the number of state s_i
      @param j the number of state s_j
      @param o an output sequence o
      @param fwd the Forward-Variables for o
      @param bwd the Backward-Variables for o
      @return P
  */
  public double p(int t, int i, int j, int[] o, double[][] fwd, double[][] bwd) {
    double num;
    if (t == o.length - 1)
      num = fwd[i][t] * this.transitionProb[i][j];
    else
      num = fwd[i][t] * this.transitionProb[i][j] * this.emissionProb[j][o[t+1]] * bwd[j][t+1];

    double denom = 0;

    for (int k = 0; k < numberOfStates; k++)
      denom += (fwd[k][t] * bwd[k][t]);

    return divide(num, denom);
  }

  /** computes gamma(i, t) */
  public double gamma(int i, int t, int[] o, double[][] fwd, double[][] bwd) {
    double num = fwd[i][t] * bwd[i][t];
    double denom = 0;

    for (int j = 0; j < numberOfStates; j++)
      denom += fwd[j][t] * bwd[j][t];

    return divide(num, denom);
  }

  public void print() {
    DecimalFormat fmt = new DecimalFormat();
    fmt.setMinimumFractionDigits(5);
    fmt.setMaximumFractionDigits(5);
    
    for (int i = 0; i < numberOfStates; i++)
      System.out.println("pi(" + i + ") = " + fmt.format(this.initialStateProb[i]));
    System.out.println();

    for (int i = 0; i < numberOfStates; i++) {
      for (int j = 0; j < numberOfStates; j++)
	System.out.print("a(" + i + "," + j + ") = " + 
			 fmt.format(this.transitionProb[i][j]) + "  ");
      System.out.println();
    }

    System.out.println();
    for (int i = 0; i < numberOfStates; i++) {
      for (int k = 0; k < emisionSize; k++)
	System.out.print("b(" + i + "," + k + ") = " + 
			 fmt.format(this.emissionProb[i][k]) + "  ");
      System.out.println();
    }
  }
  
  public void initUnfairPokerModel() {
    initialStateProb[0] = 1.0;
    initialStateProb[1] = 0.0;

    this.transitionProb[0][0] = 0.95;
    this.transitionProb[0][1] = 0.05;
    this.transitionProb[1][1] = 0.9;
    this.transitionProb[1][0] = 0.1;
    
    for(int i = 0; i < 6; i++) 
        this.emissionProb[0][i] = 1.0/6.0;
    
    for(int i = 0; i < 5; i++) 
        this.emissionProb[1][i] = 1.0/10.0;

    this.emissionProb[1][5] = 1.0/2.0;
  }
  /** divides two doubles. 0 / 0 = 0! */
  public double divide(double n, double d) {
    if (n == 0)
      return 0;
    else
      return n / d;
  }

  private void calculateLikehood() {
    
  }
}

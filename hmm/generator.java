package javaapplication2;

import java.util.*;
import java.io.*;

public class generator {
  /** initial state probabilities */
  private int pi_min[];
  private int pi_max[];
  /** transition probabilities */
  private int a_min[][];
  private int a_max[][];

  /** output probabilities */
  private int b_min[][];
  private int b_max[][];

  /** output symbols */
  private static final int cola = 0;
  private static final int ice_t = 1;

  /** states */
  private static final int cola_pref = 0;
  private static final int ice_t_pref = 1;

  /** initialization of the HMM as in M&S, page 321 */
  private void init() {
    pi_min = new int[2];
    pi_max = new int[2];

    pi_min[cola_pref] = 1;
    pi_max[cola_pref] = 950;
    pi_min[ice_t_pref] = 951;
    pi_max[ice_t_pref] = 1000;

    a_min = new int[2][2];
    a_max = new int[2][2];

    a_min[cola_pref][cola_pref] = 1;
    a_max[cola_pref][cola_pref] = 950;
    a_min[cola_pref][ice_t_pref] = 951;
    a_max[cola_pref][ice_t_pref] = 1000;
    a_min[ice_t_pref][cola_pref] = 1;
    a_max[ice_t_pref][cola_pref] = 900;
    a_min[ice_t_pref][ice_t_pref] = 901;
    a_max[ice_t_pref][ice_t_pref] = 1000;

    b_min = new int[2][6];
    b_max = new int[2][6];
    
    for(int j = 0, i = 1;j < 6; i += 166, j++) {
        b_min[cola_pref][j] = i;
        b_max[cola_pref][j] = i+165;
    }
    b_max[cola_pref][5] = 1000;
    
    for(int j = 0, i = 1;j < 5; i += 100, j++) {
        b_min[ice_t_pref][j] = i;
        b_max[ice_t_pref][j] = i+99;
    }
    b_min[ice_t_pref][5] = 500;
    b_max[ice_t_pref][5] = 1000;
  }

  /** generation of output sequence */
  private void gen_output(int t) {
    try {
      PrintWriter pw = new PrintWriter(new FileWriter("dane4.out"));
      
      pw.println(t);
      
      int rnd_number = ((int) (Math.random() * 1000)) + 1;
      int state;
      
      for (state = 0; state < 1; state++) {
	if ((pi_min[state] <= rnd_number) && (pi_max[state] >= rnd_number))
	  break;
      }
      
      for (int i = 0; i < t; i++) {
	rnd_number = ((int) (Math.random() * 1000)) + 1;
	for (int symb = 0; symb < 6; symb++) {
	  if ((b_min[state][symb] <= rnd_number) && (b_max[state][symb]) >= rnd_number) {
	    printSymbol(symb+1, pw);
	    break;
	  }
	}
	
	rnd_number = ((int) (Math.random() * 1000)) + 1;
	for (int newState = 0; newState < 1; newState++) {
	  if ((a_min[state][newState] <= rnd_number) && (a_max[state][newState] >= rnd_number)) {
	    state = newState;
	    break;
	  }
	}
      }
      pw.println();
      pw.close();
    }
    catch (IOException e) {
      System.out.println("dane1.out cannot be loaded.");
      System.exit(0);
    }
  }

  /** output of a Symbol */
  private void printSymbol(int s, PrintWriter pw) {
    pw.print(s);
  }

  /** Main program. Invoke with java soda <t>, where t is the length of
      the sequence to be generated. */
  public static void main(String argv[]) {
    generator s = new generator();
    s.init();
    s.gen_output(Integer.parseInt("100"));
  }
}

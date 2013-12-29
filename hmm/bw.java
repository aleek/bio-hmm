package javaapplication2;

import java.io.*;


public class bw {
  public static void main(String argv[]) {
    String fileName = "dane4.out";
            
    HMM hmm = new HMM(2, 6, 0.001);

    hmm.initUnfairPokerModel();

    try {
      BufferedReader br = new BufferedReader(new FileReader("d:\\eti\\" + fileName));
      int definedLength = Integer.parseInt(br.readLine());
      int[] sequenceToObserver = new int[definedLength];
      String sequenceAsString = br.readLine();
    
      for (int i = 0; i < definedLength; i++) {
	sequenceToObserver[i] = Integer.parseInt(sequenceAsString.substring(i, i + 1));
        sequenceToObserver[i]--;
      }

      System.out.println("Initial Parameters:");
      hmm.print();
      
      hmm.train(sequenceToObserver, Integer.parseInt(argv[0]));
      
      System.out.println();
      
      System.out.println("Trained Model:");
      hmm.print();
    }
    catch (FileNotFoundException e) {
      System.out.println(fileName+ " file not found. Create using 'java soda'");
      System.exit(0);
    }
    catch (IOException e) {
      System.out.println("Problem reading " + fileName);
      System.exit(0);
    }
  }
}
  

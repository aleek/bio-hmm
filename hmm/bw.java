import java.io.*;


public class bw {
  public static void main(String argv[]) {
    HMM hmm = new HMM(2, 6);

    hmm.pi[0] = 1.0;
    hmm.pi[1] = 0.0;

    hmm.a[0][0] = 0.95;
    hmm.a[0][1] = 0.05;
    hmm.a[1][1] = 0.9;
    hmm.a[1][0] = 0.1;

    hmm.b[0][0] = 1.0/6.0;
    hmm.b[0][1] = 1.0/6.0;
    hmm.b[0][2] = 1.0/6.0;
    hmm.b[0][3] = 1.0/6.0;
    hmm.b[0][4] = 1.0/6.0;
    hmm.b[0][5] = 1.0/6.0;

    hmm.b[1][0] = 1.0/10.0;
    hmm.b[1][1] = 1.0/10.0;
    hmm.b[1][2] = 1.0/10.0;
    hmm.b[1][3] = 1.0/10.0;
    hmm.b[1][4] = 1.0/10.0;
    hmm.b[1][5] = 1.0/2.0;

    try {
      BufferedReader br = new BufferedReader(new FileReader("crazysoda.seq"));
      int olen = Integer.parseInt(br.readLine());
      int[] o = new int[olen];
      String os = br.readLine();
    
      for (int i = 0; i < olen; i++)
	o[i] = Integer.parseInt(os.substring(i, i + 1));

      System.out.println("Initial Parameters:");
      hmm.print();
      
      hmm.train(o, Integer.parseInt(argv[0]));
      
      System.out.println();
      
      System.out.println("Trained Model:");
      hmm.print();
    }
    catch (FileNotFoundException e) {
      System.out.println("crazysoda.seq file not found. Create using 'java soda'");
      System.exit(0);
    }
    catch (IOException e) {
      System.out.println("Problem reading crazysoda.seq");
      System.exit(0);
    }
  }
}
  

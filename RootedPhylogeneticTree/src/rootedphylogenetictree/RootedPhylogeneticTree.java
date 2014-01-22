/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rootedphylogenetictree;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author Piotr
 */
public class RootedPhylogeneticTree {
    private static int i;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)  {
        
        String fileName = "test2.txt";
        try {
            BufferedReader br = new BufferedReader(new FileReader("/home/aleek/src/bio/RootedPhylogeneticTree/" + fileName));
            CharSequence sequenceToObserver;
            String sequenceAsString = br.readLine();
            sequenceToObserver = sequenceAsString.subSequence(0, sequenceAsString.length());
            System.out.println("Sekwencja z pliku wygląda: " + sequenceToObserver.toString());
            
            NewickParser parsedTree = new NewickParser(sequenceToObserver.toString());
            parsedTree.parseStringToTreeStructure();
            String resultTree = parsedTree.showTree();
            System.out.println("Drzewo wygląda: \n" + resultTree.toString());
            ManageOptions.getInstance().initMenu(parsedTree.getRootTree());
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(RootedPhylogeneticTree.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            
        }
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rootedphylogenetictree;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Scanner;

/**
 *
 * @author Piotr
 */
public class ManageOptions {
    
    private static ManageOptions manager = null;
    
    protected ManageOptions() {
        
    }
 
    public static ManageOptions getInstance() {
        if(manager == null) {
            manager = new ManageOptions();
        }
        return manager;
    }
    
    public void initMenu(CustomTreeNode root) {
        
        String userInput = null;
        do {
            System.out.println("Co chcesz zrobić?:");
            System.out.println("1) Wizualizuj drzewo");
            System.out.println("2) Wyznacz odległość między dwoma drzewami metodą RF?");
            System.out.println("3) Drzewo konsensusu");
            System.out.println("4) Rodzina klastrów");
            Scanner scann = new Scanner(System.in);
            userInput = scann.nextLine();
            if(isInputValid(userInput)) {
                int i = Integer.parseInt(userInput);
                RootedTreeTools treeTools = new RootedTreeTools(root);
                switch(i) {
                    case 4: {
                        treeTools.createClusterFamily();
                        treeTools.printClusterFamily();
                        treeTools.printClusterFamilyValidationResult();
                    }
                    default: break;
                        
                }
            }
        }while(userInput.compareTo("0") != 0);
    }
    
    private boolean isInputValid(String input) {
        return true;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rootedphylogenetictree;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import sun.java2d.loops.CustomComponent;
/**
 *
 * @author Piotr
 */
public class RootedTreeTools {
    private CustomTreeNode rootedTree;
    private CustomTreeNode[] splitedTreeNodes;
    private ArrayList<Cluster> clusterFamilly;
    
    public RootedTreeTools(CustomTreeNode rootedTree) {
        this.rootedTree = rootedTree;
    }

    public double calculateDistanceRF(CustomTreeNode root1, CustomTreeNode root2) {
        double result = 0.0d;
        
        return result;
    }
    
    public void createClusterFamily() {
        clusterFamilly = new ArrayList<>();

        CustomTreeNode child;
        try {
            child = (CustomTreeNode) rootedTree.clone();
            searchTreeForAllClusters(child);
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(RootedTreeTools.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void searchTreeForAllClusters(CustomTreeNode node) {
        
            Cluster c = new Cluster(node);
            c.getAllLeavesOfNode(node);
            addClusterToFamily(c);
        
        for(int i = 0; i < node.getChildCount(); i++) {
                searchTreeForAllClusters(node.getChildAt(i));
            }            

    }
 
    private void addClusterToFamily(Cluster c) {
        clusterFamilly.add(c);
    }
    
    public void printClusterFamily() {
        for(Cluster c : clusterFamilly) {
            c.printCluster();
        }
    }
    
    private boolean isClusterFamilyValidForTree() {
        for (Cluster tmpCluster : clusterFamilly) {
            for (Cluster secTmpCluster : clusterFamilly) {
                if(!(tmpCluster.seperateFrom(secTmpCluster) || secTmpCluster.isSubsetOf(tmpCluster) || tmpCluster.isSubsetOf(secTmpCluster))) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public void printClusterFamilyValidationResult() {
        if(isClusterFamilyValidForTree()) {
            System.out.println("Rodzina jest zgodna :D");
        } else {
            System.out.println("Rodzina nie jest zgodna ;(");
        }
    }
}

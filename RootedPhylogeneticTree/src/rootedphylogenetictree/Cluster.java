/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rootedphylogenetictree;

import java.util.*;

/**
 *
 * @author Piotr
 */
public class Cluster implements Comparable<Cluster>{

    private CustomTreeNode parent;
    private Set<CustomTreeNode> childNodes;
    private List<CustomTreeNode> leaves;

    public CustomTreeNode getParent() {
        return parent;
    }

    public Set<CustomTreeNode> getChildNodes() {
        return childNodes;
    }

    public List<CustomTreeNode> getLeaves() {
        return leaves;
    }

    public Cluster(CustomTreeNode parent) {
        this.parent = parent;
        leaves = new ArrayList<CustomTreeNode>();
    }
    
    public void getAllLeavesOfNode(CustomTreeNode node) {
        searchForLeaves(node);
    }
    
    private void searchForLeaves(CustomTreeNode node) {
		if( node.isLeaf() ) {
			String name = node.getNodeName() == null ? "null" : node.getNodeName();
			System.out.println( "Checking leaf " + name );
			if( !leaves.contains(node ) ) {
			System.out.println( "Adding " + name );
            	leaves.add(node);
			}
		}
		else
		{
        	for(int i = 0; i < node.getChildCount(); i++) {
        	    searchForLeaves(node.getChildAt(i));
        	}
		}


        //for(int i = 0; i < node.getChildCount(); i++) {
//            searchForLeaves(node.getChildAt(i));
  //      }            
//
    //    if(node.isLeaf() && !leaves.contains(node)) 
//        { 
  //          leaves.add(node);
 //       }

    }
    
    public boolean isSubsetOf(Cluster cluster) {
        return cluster.getLeaves().containsAll(leaves);
    }
    
    public boolean seperateFrom(Cluster cluster) {
        return !isSubsetOf(cluster);
    }
    
    public void printCluster() {
        System.out.print("{"+this.getParent().getNodeName() + " ");
        for (CustomTreeNode tmpNode : leaves) {
            System.out.print(tmpNode.getNodeName() + " ");
        }
        System.out.println("}");
    }

    @Override
    public int compareTo(Cluster o) {
        if(parent.getNodeName() == o.parent.getNodeName()) { return 0; }
        else { return 1; }
    }
}

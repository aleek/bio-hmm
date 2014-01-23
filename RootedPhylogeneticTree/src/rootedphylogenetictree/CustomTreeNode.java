/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rootedphylogenetictree;

import java.util.Enumeration;
import java.util.Vector;
import javax.swing.tree.TreeNode;

/**
 *1
 * @author Piotr
 */
public class CustomTreeNode implements TreeNode, Cloneable{

    private String nodeName;
    private double nodeDistance;
    public int depthLevel = 0;
    
    private Vector childVec = new Vector();
    private CustomTreeNode parentNode;

	// for painting
	public int painting_shift;
    
    
    public CustomTreeNode(double distance) {
        nodeDistance = distance;
    }
    
    public void setNodeName(String s) {
        if(s.length() != 0)
            nodeName = s;
        else 
            nodeName = "NoName";
    }
    
    public void setDepthLevel(int depth) {
        depthLevel = depth;
    }
    
    public String getNodeName() {
        return nodeName;
    }

    public double getNodeDistance() {
        return nodeDistance;
    }

    public int getDepthLevel() {
        return depthLevel;
    }
    
    
    public void addChild(CustomTreeNode n) {
        childVec.add(n);
    }  

    @Override
    public CustomTreeNode getChildAt(int childIndex) {
        return (CustomTreeNode) childVec.get(childIndex);
    }

    @Override
    public int getChildCount() {
        return childVec.size();
    }

    @Override
    public TreeNode getParent() {
        return parentNode;
    }

    @Override
    public int getIndex(TreeNode node) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean getAllowsChildren() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isLeaf() {
        if(this.getChildCount() == 0 ) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean equals(Object obj) {
		return ( System.identityHashCode(this) == System.identityHashCode(obj) );
    /*    CustomTreeNode node = (CustomTreeNode) obj;
        if(this.getNodeName().equals(node.getNodeName())) {
            return true;
        } else if(this.getNodeDistance() == node.getNodeDistance()) {
            return true;
        } else {
            return false;
        }*/
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Enumeration children() {
        return childVec.elements();
    }
    
    public String toString() {
       StringBuffer sb = new StringBuffer();
       toString(this, sb);
       return sb.toString();
    }
    
     public static void toString(CustomTreeNode n, StringBuffer sb) {
       if (n.getChildCount() == 0) {
            sb.append(n.nodeName);
            sb.append(":");
            sb.append(n.nodeDistance);
       }
       else {
            sb.append("(");
            toString(n.getChildAt(0), sb);
            for (int i = 1; i < n.getChildCount(); i++) {
                 sb.append(",");
                 toString(n.getChildAt(i), sb);
            }
            sb.append("):");
            sb.append(n.nodeDistance);
       }
  }
}

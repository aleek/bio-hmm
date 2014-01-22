/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rootedphylogenetictree;

import java.util.Enumeration;
import javax.swing.JTree;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import sun.java2d.loops.CustomComponent;

/**
 *
 * @author Piotr
 */
public class NewickParser {
    
    private String treeData;
    private CustomTreeNode root;
    private String treeView; 
           
    
    public NewickParser(String sequenceTree) {
        treeData = sequenceTree;
               
    }
    
    public CustomTreeNode getRootTree() {
        return root;
    }
    
    public String showTree() {
        StringBuffer buffer = new StringBuffer();

        getStructure(root, buffer, 1);
        return buffer.toString();
    }
   
    public void getStructure(CustomTreeNode node,  StringBuffer buffer, int level) {
        for(int i = 1; i < level; i++) {
            buffer.append("     ");
        }
        if(node.getNodeName() != null)
            buffer.append("-"+node.getNodeName());
        else 
            buffer.append("-+");
        buffer.append("\n");
        for(int i = 0; i < node.getChildCount(); i++)
            getStructure(node.getChildAt(i), buffer, level+1);
    }
    
    public CustomTreeNode parseStringToTreeStructure() {
        int x = treeData.lastIndexOf(':');
        root = new CustomTreeNode(Double.parseDouble(treeData.substring(x+1)));
        root.setNodeName("ROOT");
        
        return build(treeData, root, 0, x);  
    }
    
    public CustomTreeNode build(String s, CustomTreeNode parent, int from, int to) {
          if (s.charAt(from) != '(') {
               parent.setNodeName(s.substring(from, to));
               return parent;
          }

          int b = 0; // bracket counter
          int colon = 0; // colon marker
          int x = from; // position marker
          
          CustomTreeNode leaf = parent;
          
          for (int i = from; i < to; i++) {
               char c = s.charAt(i);

               if (c == '(')
                    b++;
               else if (c == ')')
                    b--;
               else if (c == ':')
                    colon = i;

               if (b == 0 || b == 1 && c == ',') {
                    
                    leaf.addChild(build(s, new CustomTreeNode(Double.parseDouble(s.substring(colon+1, i))), x + 1, colon));
                    x = i;
               }
          }
          return parent;
     }

}

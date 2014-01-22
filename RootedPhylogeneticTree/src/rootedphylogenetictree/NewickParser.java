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
import java.util.ArrayList;

/**
 *
 * @author Piotr
 */
public class NewickParser {
    
    private String treeData;
    private CustomTreeNode root;
    private String treeView; 
	public ArrayList<StringBuffer> painted_tree = new ArrayList<StringBuffer>();
           
    
    public NewickParser(String sequenceTree) {
        treeData = sequenceTree;
               
    }
    
    public CustomTreeNode getRootTree() {
        return root;
    }
    
    public String showTree() {
        StringBuffer buffer = new StringBuffer();

		paintTree(root);

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

	public int getTreeDepth( CustomTreeNode node, int depth ) {

		int[] depth_arr = new int[node.getChildCount()];
		int max=0;
		int children_cnt = node.getChildCount();


		depth++;
		node.setDepthLevel( depth );

		if( children_cnt == 0 ) {
			return depth;
		}

		// get depths from all children
		for( int i=0; i< children_cnt; ++i ) {
			depth_arr[i] = getTreeDepth( node.getChildAt(i), depth );
		}

		// get max from the array
		for( int i=0; i<children_cnt; ++i ) {
			if( depth_arr[i] > max ) {
				max = depth_arr[i];
			}
		}
		return depth + max;
	}

	public void paintTree( CustomTreeNode node ) {
		int tree_depth = getTreeDepth( node, 0 );

		for( int i =0; i<tree_depth; ++i ) {
			painted_tree.add( new StringBuffer() );
		}
		if( painted_tree == null ) {
			System.out.println("smuteczek" );
		}

		paintElement( node );

		for( int i=0; i< tree_depth; ++i ) {
			System.out.println( painted_tree.get(i) );
		}
	}

	public void paintElement( CustomTreeNode node ) {
		//first, paint its children
		int children_cnt = node.getChildCount();
		int depth = node.depthLevel;
		String name;

		if( node.getNodeName() == null ) {
			name = "+";
		}
		else
		{
			name = node.getNodeName();
		}

		if( children_cnt == 0 ) {
			node.painting_shift = painted_tree.get(depth).length()+1;
			if( painted_tree.get(depth+1) != null ) {
				int a = painted_tree.get(depth+1).length();
				if( a > node.painting_shift ) {
					node.painting_shift = a;
				}
			}
			for( int i=0; i<node.painting_shift; ++i ) {
				painted_tree.get(depth).append( " " );
			}
			painted_tree.get(depth).append( name );
			return;
		}
		for( int i=0; i< children_cnt; ++i ) {
			paintElement( node.getChildAt (i) );
		}
		
		CustomTreeNode last_child = node.getChildAt(children_cnt-1);
		if( last_child == null ) {
			System.out.println("AAAAAAAA");
		}

		int first_child_shift = node.getChildAt(0).painting_shift;
		
		int last_child_shift = last_child.painting_shift;
		if(last_child.getNodeName() == null ) {
			last_child_shift++;
		} else {
			last_child_shift += last_child.getNodeName().length();
		}

		int my_shift = first_child_shift + (last_child_shift - first_child_shift)/2 + (name.length())/2;
		int cur_shift = painted_tree.get(depth).length();
		int shift = (my_shift - cur_shift) <0 ? 0 : my_shift - cur_shift;

		painted_tree.get(depth).append( " " );
		for( int i = 0; i< shift; ++i ) {
			painted_tree.get(depth).append( " " );
		}
		painted_tree.get(depth).append( name );
		node.painting_shift = shift;
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

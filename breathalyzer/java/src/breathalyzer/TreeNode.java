package breathalyzer;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class TreeNode {

   private final Map<Character, TreeNode> my_children;
   private String my_value;
   private boolean my_isLeaf;
   private int my_maxBranchDepth;

   public TreeNode() {
      my_children = new HashMap<Character, TreeNode>();
      my_value = null;
      my_isLeaf = false;
      my_maxBranchDepth = 0;
   }
   
   public TreeNode(String value) {
      my_children = new HashMap<Character, TreeNode>();
      my_value = value;
      my_isLeaf = (value != null);
      my_maxBranchDepth = 0;
   }
   
   public void insert(String value) {
      insert(value.toCharArray(), 0);
   }

   private void insert(char[] value, int level) {
      final char branch = value[level];
      TreeNode child = getChild(branch);
      if (child == null) {
         child = new TreeNode();
         addChild(branch, child);
      }
      if (level < value.length-1) {
         child.insert(value, level+1);
      } else {
         child.my_value = new String(value);
         child.my_isLeaf = true;
      }
   }
   
   public void addChild(char branch, TreeNode child) {
      my_children.put(branch, child);
   }
   
   public boolean isLeaf() {
      return my_isLeaf;
   }
   
   public String getValue() {
      return my_value;
   }
   
   public TreeNode getChild(char branch) {
      return my_children.get(branch);
   }
   
   public boolean hasChildren() {
      return !my_children.isEmpty();
   }

   public Collection<TreeNode> getChildren() {
      return my_children.values();
   }
   
   public int size() {
      int s = isLeaf() ? 1 : 0;
      for (TreeNode child : my_children.values()) {
         s += child.size();
      }
      return s;
   }
   
   public int getBranchDepth() {
      return my_maxBranchDepth;
   }

   public int calcDepths() {
      int maxDepth = 0;
      for (TreeNode child : my_children.values()) {
         int d = child.calcDepths();
         if (d > maxDepth) {
            maxDepth = d;
         }
      }
      my_maxBranchDepth = maxDepth;
      return maxDepth+1;
   }
   
   public String toString() {
      StringBuffer buffer = new StringBuffer();
      buffer.append("(" + my_maxBranchDepth + "[");
      buffer.append(isLeaf() ? my_value : "-");
      buffer.append("]");
      for (Map.Entry<Character, TreeNode> entry : my_children.entrySet()) {
         buffer.append(", {" + entry.getKey() + "->" + entry.getValue() + "}");
      }
      buffer.append(")");
      return buffer.toString();
   }
   
}

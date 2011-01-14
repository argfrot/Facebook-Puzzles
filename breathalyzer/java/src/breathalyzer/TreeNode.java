package breathalyzer;

import java.util.HashMap;
import java.util.Map;

public class TreeNode {

   private final Map<Character, TreeNode> my_children;
   private String my_value;
   private boolean my_isLeaf;

   public TreeNode() {
      my_children = new HashMap<Character, TreeNode>();
      my_value = null;
      my_isLeaf = false;
   }
   
   public TreeNode(String value) {
      my_children = new HashMap<Character, TreeNode>();
      my_value = value;
      my_isLeaf = (value != null);
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
   
   public int size() {
      int s = isLeaf() ? 1 : 0;
      for (TreeNode child : my_children.values()) {
         s += child.size();
      }
      return s;
   }
   
   public String toString() {
      StringBuffer buffer = new StringBuffer();
      buffer.append("([");
      buffer.append(isLeaf() ? my_value : "-");
      buffer.append("]");
      for (Map.Entry<Character, TreeNode> entry : my_children.entrySet()) {
         buffer.append(", {" + entry.getKey() + "->" + entry.getValue() + "}");
      }
      buffer.append(")");
      return buffer.toString();
   }
   
}

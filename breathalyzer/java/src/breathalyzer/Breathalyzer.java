package breathalyzer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;

/**
 * Solve the 'breathalyzer' Facebook Engineering puzzle.
 * 
 * @author Graham Williamson
 */
public class Breathalyzer {

   private static final String join(final Collection<Object> list, final String delimiter) {
      StringBuffer buffer = new StringBuffer();
      String delim = "";
      for (Object p : list) {
          buffer.append(delim).append(p);
          delim = delimiter;
      }
      return buffer.toString();
   }

   private static TreeNode loadDictionary(FileReader input) throws IOException {
      TreeNode top = new TreeNode(null);
      BufferedReader in = new BufferedReader(input);
      String line;
      while ((line = in.readLine()) != null) {
         line = line.trim();
         if (line.equals("")) continue;
         top.insert(line.toUpperCase());
      }
      return top;
   }
   
   public static boolean match(TreeNode dict, String toMatch) {
      return match(dict, toMatch.toUpperCase().toCharArray(), 0);
   }
   
   private static boolean match(TreeNode node, char[] toMatch, int level) {
      if (level == toMatch.length) {
         if (node.isLeaf()) {
            return node.getValue().equals(new String(toMatch));
         } else {
            return false;
         }
      } else {
         TreeNode child = node.getChild(toMatch[level]);
         if (child != null) {
            return match(child, toMatch, level+1);
         } else {
            return false;
         }
      }
   }
   
   public static void main(String[] args) throws Exception {
      TreeNode dict = loadDictionary(new FileReader(args[0]));
      System.out.println(dict.size());
      System.out.println(match(dict,args[1]));
   }

}

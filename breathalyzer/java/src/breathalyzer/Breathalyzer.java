package breathalyzer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import com.merriampark.CombinationGenerator;

/**
 * Solve the 'breathalyzer' Facebook Engineering puzzle.
 * 
 * @author Graham Williamson
 */
public class Breathalyzer {

   private static final char MATCH_ANY = '.';

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
//            return node.getValue().equals(new String(toMatch));
            System.out.println("Matched: " + node.getValue());
            return true;
         } else {
            return false;
         }
      } else {
         char next = toMatch[level];
         if (next == MATCH_ANY) {
            if (!node.hasChildren()) {
               return false;
            } else {
               for (TreeNode child : node.getChildren()) {
                  if (match(child,toMatch,level+1)) {
                     return true;
                  }
               }
               return false;
            }
         } else {
            TreeNode child = node.getChild(next);
            if (child != null) {
               return match(child, toMatch, level+1);
            } else {
               return false;
            }
         }
      }
   }

   public static boolean tryReplace(TreeNode dictionary, String toMatch, int edits) {
      CombinationGenerator possibilities = new CombinationGenerator(toMatch.length(), edits);
      while (possibilities.hasMore()) {
         int[] next = possibilities.getNext();
         char[] replaced = toMatch.toCharArray();
         for(int n : next) {
            replaced[n] = MATCH_ANY;
         }
         System.out.println("Trying to match with: " + String.valueOf(replaced));
         if (match(dictionary,replaced,0)) {
            return true;
         }
      }
      return false;
   }
   
   public static boolean tryRemove(TreeNode dictionary, String toMatch, int edits) {
      CombinationGenerator possibilities = new CombinationGenerator(toMatch.length(), edits);
      while (possibilities.hasMore()) {
         int[] next = possibilities.getNext();
         char[] replaced = delete(toMatch, next);
         System.out.println("Trying to match with: " + String.valueOf(replaced));
         if (match(dictionary,replaced,0)) {
            return true;
         }
      }
      return false;
   }
   
   private static char[] delete(String toMatch, int[] next) {
      char[] str = toMatch.toCharArray();
      char[] out = new char[str.length - next.length];
      int pointer = 0;
      for (int i=0; i<str.length; i++) {
         if (pointer < next.length && i == next[pointer]) {
            pointer++;
         } else {
            out[i-pointer] = str[i];
         }
      }
      return out;
   }

   public static boolean tryInsert(TreeNode dictionary, String toMatch, int edits) {
      CombinationGenerator possibilities = new CombinationGenerator(toMatch.length(), edits);
      while (possibilities.hasMore()) {
         int[] next = possibilities.getNext();
         char[] replaced = insert(toMatch, next);
         System.out.println("Trying to match with: " + String.valueOf(replaced));
         if (match(dictionary,replaced,0)) {
            return true;
         }
      }
      return false;
   }

   private static char[] insert(String toMatch, int[] next) {
      char[] str = toMatch.toCharArray();
      char[] out = new char[str.length + next.length];
      int pointer = 0;
      for (int i=0; i<out.length; i++) {
         if (pointer < next.length && i == next[pointer]) {
            out[i] = MATCH_ANY;
            pointer++;
         } else {
            out[i] = str[i-pointer];
         }
      }
      return out;
   }
   
   public static int doEdits(TreeNode dictionary, String toMatch) {
      for(int i=1; i<toMatch.length(); i++) {
         if (tryRemove(dictionary, toMatch, i)
               || tryReplace(dictionary, toMatch, i)
               || tryInsert(dictionary, toMatch, i)) {
            return i;
         }
      }
      return -1;
   }
   
   public static void main(String[] args) throws Exception {
      TreeNode dict = loadDictionary(new FileReader(args[0]));
      System.out.println(dict.size());
      System.out.println(match(dict,args[1]));
      System.out.println(tryReplace(dict, args[1], 1));
      System.out.println(doEdits(dict, args[1]));
   }

}

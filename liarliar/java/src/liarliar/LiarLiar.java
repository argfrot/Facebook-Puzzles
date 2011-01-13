package liarliar;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Solve the 'liarliar' Facebook Engineering puzzle.
 * 
 * This is essentially a graph colouring problem with two colours --- determine the
 * sets in a bipartite graph. Followed by counting the numbers of nodes in each.
 * 
 * @author Graham Williamson
 */
public class LiarLiar {

   public static Collection<Person> loadTerms(Reader input) throws Exception {
      Map<String, Person> people = new HashMap<String, Person>();
      
      BufferedReader in = new BufferedReader(input);
      /*int numberPeople = */Integer.parseInt(in.readLine().trim());
      
      String line;
      Person current = null;
      while ((line = in.readLine()) != null) {
         String[] split = line.split("\\s+");
         String name = split[0];
         if (split.length == 2) {
            current = people.get(name);
            if (current == null) {
               current = new Person(name);
               people.put(name, current);
            }
         } else {
            Person accused = people.get(name);
            if (accused == null) {
               accused = new Person(name);
               people.put(name, accused);
            }
            current.accuse(accused);
         }
      }
      
      return people.values();
   }

   private static final String join(final Collection<Person> list, final String delimiter) {
      StringBuffer buffer = new StringBuffer();
      String delim = "";
      for (Person p : list) {
          buffer.append(delim).append(p);
          delim = delimiter;
      }
      return buffer.toString();
   }

   /**
    * Keep trying to infer the labels for each node until no further changes
    * occur and therefore no new inferences are possible.
    * @param people The list of nodes in the graph.
    * @throws Exception If we encounter a contradiction during the current colouring (not possible though).
    */
   private static void solve(Collection<Person> people) throws Exception {
      boolean changed = true;
      while (changed) {
         changed = false;
         for (Person p : people) {
            if (p.inferLabel()) changed = true;
         }
      }
   }
   
   public static void main(String[] args) throws Exception {
      Collection<Person> people = loadTerms(new FileReader(args[0]));
      Person person = people.iterator().next();
      person.setLiar(false);
      solve(people);
      // there should be no contradictions as each colouring is equivalent and there is guaranteed to be a solution.
//      System.out.println(join(people, "\n"));
      int count = 0;
      for (Person p : people) {
         if (p.isLiar()) count++;
      }
      count = Math.max(count, people.size()-count);
      System.out.println(count + " " + (people.size()-count));
   }
}
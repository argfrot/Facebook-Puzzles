package liarliar;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

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
            accused.accuse(current); // make graph undirected
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
    * Rather than having the nodes propagate their values down through the
    * graph, use a breadth first traversal to avoid stack overflow in large
    * graphs.
    * @param head The node to start the propagation of values from.
    * @throws Exception If a contradiction occurs.
    */
   public static void bfs(Person head) throws Exception {
      Queue<Person> outstanding = new LinkedList<Person>();
      outstanding.add(head);
      while (!outstanding.isEmpty()) {
         Person current = outstanding.poll();
         for (Person p : current.getAccused()) {
            if (!p.isLabelled()) {
               p.setLiar(!current.isLiar());
               outstanding.add(p);
            }
         }
      }
   }
   
   public static void main(String[] args) throws Exception {
      Collection<Person> people = loadTerms(new FileReader(args[0]));
      if (people.size() > 0) {
         Person person = people.iterator().next();
         person.setLiar(false);
         bfs(person);
      }
//      System.out.println(join(people, "\n"));
      int count = 0;
      for (Person p : people) {
         if (!p.isLabelled()) throw new Exception("Oh dear. Unable to find a solution!");
         if (p.isLiar()) count++;
      }
      count = Math.max(count, people.size()-count);
      System.out.println(count + " " + (people.size()-count));
   }
}

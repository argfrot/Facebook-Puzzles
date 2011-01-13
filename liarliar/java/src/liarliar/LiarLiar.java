package liarliar;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

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
      System.out.println(join(people, "\n"));
      Person person = people.iterator().next();
      try {
         person.setLiar(true);
         solve(people);
      } catch (Exception e) {
         System.out.println(e);
         System.out.println();
         System.out.println(join(people, "\n"));
         person.setLiar(false);
         solve(people);
      }
      System.out.println();
      System.out.println("\nSolved.");
      System.out.println(join(people, "\n"));
   }
}

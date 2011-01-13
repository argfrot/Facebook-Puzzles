package liarliar;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class Person {

   private final String my_name;
   
   private final List<Person> my_accused;
   private boolean my_isLabelled;
   private boolean my_isLiar;
   
   public Person(String name) {
      my_name = name;
      my_accused = new LinkedList<Person>();
      my_isLabelled = false;
   }
   
   public String getName() {
      return my_name;
   }
   
   public void accuse(Person p) {
      my_accused.add(p);
   }

   public void clear() {
      my_isLabelled = false;
   }

   public boolean isLabelled() {
      return my_isLabelled;
   }
   
   public boolean isLiar() {
      return my_isLiar;
   }
   
   public void setLiar(boolean label) throws Exception {
      if (isLabelled()) {
         if (label == my_isLiar) return; // consistent
         else throw new Exception("Contradiction at person: " + this);
      }
      my_isLiar = label;
      my_isLabelled = true;
      for (Person accused : my_accused) {
         accused.setLiar(!my_isLiar);
      }
   }

   public boolean inferLabel() throws Exception {
      if (isLabelled()) return false;

      boolean accusedAreLiars = false;
      boolean accusedLabelled = false;
      for (Person p : my_accused) {
         if (p.isLabelled()) {
            accusedAreLiars = p.isLiar();
            accusedLabelled = true;
            break;
         }
      }
      
      if (accusedLabelled) setLiar(!accusedAreLiars);
      return accusedLabelled;
   }

   private static final String join(final Collection<Person> list, final String delimiter) {
      StringBuffer buffer = new StringBuffer();
      String delim = "";
      for (Person p : list) {
          buffer.append(delim).append(p.summary());
          delim = delimiter;
      }
      return buffer.toString();
   }
   
   private final String summary() {
      return my_name + "[" + (isLabelled() ? my_isLiar : "-") + "]";
   }
   
   public String toString() {
      return summary() + " === !" + join(my_accused," ^ !");
   }
   
}

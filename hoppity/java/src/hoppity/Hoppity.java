package hoppity;

import java.io.BufferedReader;
import java.io.FileReader;

public class Hoppity {
   public static void main(String[] args) throws Exception {
      final BufferedReader in = new BufferedReader(new FileReader(args[0]));
      final int n = Integer.parseInt(in.readLine().trim());
      for (int i=1; i<=n; i++) {
         if (i%3 == 0 && i%5 == 0) {
            System.out.println("Hop");
         } else if (i%3==0) {
            System.out.println("Hoppity");
         } else if (i%5==0) {
            System.out.println("Hophop");
         }
      }
   }
}

package filter;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DataFilter {
   protected List criteria = null;

   public void reset() {
      this.criteria = null;
   }

   protected void addCriteria(String var1, Criteria var2, boolean var3) {
      if (this.criteria == null) {
         this.criteria = new LinkedList();
      }

      DataFilter._A var4 = new DataFilter._A();
      var4.A = (Criteria)(var3 ? new NegateCriteria(var2) : var2);
      var4.B = var1;
      this.criteria.add(var4);
   }

   public void checkWildcard(String var1, String var2) {
      this.addCriteria(var1, new WildcardCriteria(var2), false);
   }

   public void checkWildcard(String var1, String var2, boolean var3) {
      this.addCriteria(var1, new WildcardCriteria(var2), var3);
   }

   public void checkLiteral(String var1, String var2) {
      this.addCriteria(var1, new LiteralCriteria(var2), false);
   }

   public void checkNTLMHash(String var1, boolean var2) {
      this.addCriteria(var1, new NTLMHashCriteria(), var2);
   }

   public void checkNetwork(String var1, String var2, boolean var3) {
      this.addCriteria(var1, new NetworkCriteria(var2), var3);
   }

   public void checkNumber(String var1, String var2, boolean var3) {
      this.addCriteria(var1, new RangeCriteria(var2), var3);
   }

   public void checkBeacon(String var1, boolean var2) {
      this.addCriteria(var1, new BeaconCriteria(), var2);
   }

   public List apply(List var1) {
      if (this.criteria == null) {
         return var1;
      } else {
         LinkedList var2 = new LinkedList(var1);
         Iterator var3 = var2.iterator();

         while(var3.hasNext()) {
            Map var4 = (Map)var3.next();
            if (!this.test(var4)) {
               var3.remove();
            }
         }

         return var2;
      }
   }

   public boolean test(Map var1) {
      if (this.criteria == null) {
         return true;
      } else {
         Iterator var2 = this.criteria.iterator();

         DataFilter._A var3;
         Object var4;
         do {
            if (!var2.hasNext()) {
               return true;
            }

            var3 = (DataFilter._A)var2.next();
            if (!"*".equals(var3.B)) {
               var4 = var1.get(var3.B);
            } else {
               StringBuffer var5 = new StringBuffer();
               Iterator var6 = var1.values().iterator();

               while(var6.hasNext()) {
                  var5.append(var6.next() + "");
                  var5.append("\t");
               }

               var4 = var5.toString();
            }
         } while(var3.A.test(var4));

         return false;
      }
   }

   public String toString() {
      return this.criteria.size() == 1 ? "1 filter" : this.criteria.size() + " filters";
   }

   private static class _A {
      public Criteria A;
      public String B;

      private _A() {
      }

      // $FF: synthetic method
      _A(Object var1) {
         this();
      }
   }
}

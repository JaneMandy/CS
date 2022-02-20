package sleep.bridges;

import sleep.runtime.Scalar;

public class KeyValuePair {
   protected Scalar key;
   protected Scalar value;

   public KeyValuePair(Scalar var1, Scalar var2) {
      this.key = var1;
      this.value = var2;
   }

   public Scalar getKey() {
      return this.key;
   }

   public Scalar getValue() {
      return this.value;
   }

   public String toString() {
      String var10000 = this.key.toString();
      return var10000 + "=" + this.value.toString();
   }
}

package common;

import java.io.Serializable;

public class Accent implements Serializable, Transcript {
   protected String id;
   protected String value;

   public Accent(String var1, String var2) {
      this.id = var1;
      this.value = var2;
   }

   public String getKey() {
      return this.id;
   }

   public String getValue() {
      return this.value;
   }

   public String toString() {
      return this.id + "=" + this.value;
   }
}

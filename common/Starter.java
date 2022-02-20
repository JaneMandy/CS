package common;

public abstract class Starter {
   public static final boolean S1MODE = true;

   protected final void initializeStarter(Class var1) {
      if (!this.A(var1)) {
         System.exit(0);
      }

   }

   public final boolean isStartable(Class var1) {
      return this.A(var1);
   }

   private final boolean A(Class var1) {
      boolean var2 = true;
      return var2;
   }
}

package org.apache.batik.ext.awt;

import java.awt.Shape;
import java.awt.RenderingHints.Key;

final class AreaOfInterestHintKey extends Key {
   AreaOfInterestHintKey(int var1) {
      super(var1);
   }

   public boolean isCompatibleValue(Object var1) {
      boolean var2 = true;
      if (var1 != null && !(var1 instanceof Shape)) {
         var2 = false;
      }

      return var2;
   }
}

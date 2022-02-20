package org.apache.fop.text.linebreak;

public class LineBreakStatus {
   public static final byte DIRECT_BREAK = 0;
   public static final byte INDIRECT_BREAK = 1;
   public static final byte COMBINING_INDIRECT_BREAK = 2;
   public static final byte COMBINING_PROHIBITED_BREAK = 3;
   public static final byte PROHIBITED_BREAK = 4;
   public static final byte EXPLICIT_BREAK = 5;
   private byte leftClass;
   private boolean hadSpace;
   // $FF: synthetic field
   static final boolean $assertionsDisabled;

   public LineBreakStatus() {
      this.reset();
   }

   public void reset() {
      this.leftClass = -1;
      this.hadSpace = false;
   }

   public byte nextChar(char c) {
      byte currentClass = LineBreakUtils.getLineBreakProperty(c);
      switch(currentClass) {
      case 1:
      case 31:
      case 35:
         currentClass = 2;
         break;
      case 30:
         switch(Character.getType(c)) {
         case 6:
         case 8:
            currentClass = 9;
            break;
         default:
            currentClass = 2;
         }
      }

      switch(this.leftClass) {
      case -1:
         this.leftClass = currentClass;
         if (this.leftClass == 9) {
            this.leftClass = 2;
         }

         return 4;
      case 6:
      case 22:
      case 23:
         this.reset();
         this.leftClass = currentClass;
         return 5;
      case 10:
         if (currentClass != 22) {
            this.reset();
            this.leftClass = currentClass;
            return 5;
         }
      default:
         switch(currentClass) {
         case 6:
         case 10:
         case 22:
         case 23:
            this.leftClass = currentClass;
            return 4;
         case 32:
            this.hadSpace = true;
            return 4;
         default:
            boolean savedHadSpace = this.hadSpace;
            this.hadSpace = false;
            byte breakAction = LineBreakUtils.getLineBreakPairProperty(this.leftClass, currentClass);
            switch(breakAction) {
            case 0:
            case 4:
               this.leftClass = currentClass;
               return breakAction;
            case 1:
               this.leftClass = currentClass;
               if (savedHadSpace) {
                  return 1;
               }

               return 4;
            case 2:
               if (savedHadSpace) {
                  this.leftClass = currentClass;
                  return 2;
               }

               return 4;
            case 3:
               if (savedHadSpace) {
                  this.leftClass = currentClass;
               }

               return 3;
            default:
               if (!$assertionsDisabled) {
                  throw new AssertionError();
               } else {
                  return breakAction;
               }
            }
         }
      }
   }

   static {
      $assertionsDisabled = !LineBreakStatus.class.desiredAssertionStatus();
   }
}

package org.apache.batik.gvt.text;

import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.text.AttributedCharacterIterator.Attribute;
import java.util.Map;

public class ArabicTextHandler {
   private static final int arabicStart = 1536;
   private static final int arabicEnd = 1791;
   private static final Attribute ARABIC_FORM;
   private static final Integer ARABIC_NONE;
   private static final Integer ARABIC_ISOLATED;
   private static final Integer ARABIC_TERMINAL;
   private static final Integer ARABIC_INITIAL;
   private static final Integer ARABIC_MEDIAL;
   static int singleCharFirst;
   static int singleCharLast;
   static int[][] singleCharRemappings;
   static int doubleCharFirst;
   static int doubleCharLast;
   static int[][][] doubleCharRemappings;

   private ArabicTextHandler() {
   }

   public static AttributedString assignArabicForms(AttributedString var0) {
      if (!containsArabic(var0)) {
         return var0;
      } else {
         AttributedCharacterIterator var1 = var0.getIterator();
         int var2 = var1.getEndIndex() - var1.getBeginIndex();
         int[] var3 = null;
         char var5;
         int var6;
         int var8;
         if (var2 >= 3) {
            char var4 = var1.first();
            var5 = var1.next();
            var6 = 1;

            for(char var7 = var1.next(); var7 != '\uffff'; ++var6) {
               if (arabicCharTransparent(var5) && hasSubstitute(var4, var7)) {
                  if (var3 == null) {
                     var3 = new int[var2];

                     for(var8 = 0; var8 < var2; ++var8) {
                        var3[var8] = var8 + var1.getBeginIndex();
                     }
                  }

                  var8 = var3[var6];
                  var3[var6] = var3[var6 - 1];
                  var3[var6 - 1] = var8;
               }

               var4 = var5;
               var5 = var7;
               var7 = var1.next();
            }
         }

         if (var3 != null) {
            StringBuffer var14 = new StringBuffer(var2);

            for(var6 = 0; var6 < var2; ++var6) {
               var5 = var1.setIndex(var3[var6]);
               var14.append(var5);
            }

            AttributedString var17 = new AttributedString(var14.toString());

            for(int var19 = 0; var19 < var2; ++var19) {
               var1.setIndex(var3[var19]);
               Map var22 = var1.getAttributes();
               var17.addAttributes(var22, var19, var19 + 1);
            }

            if (var3[0] != var1.getBeginIndex()) {
               var1.setIndex(var3[0]);
               Float var20 = (Float)var1.getAttribute(GVTAttributedCharacterIterator.TextAttribute.X);
               Float var23 = (Float)var1.getAttribute(GVTAttributedCharacterIterator.TextAttribute.Y);
               if (var20 != null && !var20.isNaN()) {
                  var17.addAttribute(GVTAttributedCharacterIterator.TextAttribute.X, new Float(Float.NaN), var3[0], var3[0] + 1);
                  var17.addAttribute(GVTAttributedCharacterIterator.TextAttribute.X, var20, 0, 1);
               }

               if (var23 != null && !var23.isNaN()) {
                  var17.addAttribute(GVTAttributedCharacterIterator.TextAttribute.Y, new Float(Float.NaN), var3[0], var3[0] + 1);
                  var17.addAttribute(GVTAttributedCharacterIterator.TextAttribute.Y, var23, 0, 1);
               }
            }

            var0 = var17;
         }

         var1 = var0.getIterator();
         int var15 = -1;
         int var16 = var1.getBeginIndex();

         for(char var18 = var1.first(); var18 != '\uffff'; ++var16) {
            if (var18 >= 1536 && var18 <= 1791) {
               if (var15 == -1) {
                  var15 = var16;
               }
            } else if (var15 != -1) {
               var0.addAttribute(ARABIC_FORM, ARABIC_NONE, var15, var16);
               var15 = -1;
            }

            var18 = var1.next();
         }

         if (var15 != -1) {
            var0.addAttribute(ARABIC_FORM, ARABIC_NONE, var15, var16);
         }

         var1 = var0.getIterator();
         var6 = var1.getBeginIndex();
         Integer var21 = ARABIC_NONE;

         while(true) {
            char var9;
            do {
               if (var1.setIndex(var6) == '\uffff') {
                  return var0;
               }

               var8 = var1.getRunStart(ARABIC_FORM);
               var6 = var1.getRunLimit(ARABIC_FORM);
               var9 = var1.setIndex(var8);
               var21 = (Integer)var1.getAttribute(ARABIC_FORM);
            } while(var21 == null);

            int var10 = var8;

            for(int var11 = var8 - 1; var10 < var6; var11 = var10++) {
               char var12 = var9;

               for(var9 = var1.setIndex(var10); arabicCharTransparent(var9) && var10 < var6; var9 = var1.setIndex(var10)) {
                  ++var10;
               }

               if (var10 >= var6) {
                  break;
               }

               Integer var13 = var21;
               var21 = ARABIC_NONE;
               if (var11 >= var8) {
                  if (arabicCharShapesRight(var12) && arabicCharShapesLeft(var9)) {
                     var13 = new Integer(var13 + 1);
                     var0.addAttribute(ARABIC_FORM, var13, var11, var11 + 1);
                     var21 = ARABIC_INITIAL;
                  } else if (arabicCharShaped(var9)) {
                     var21 = ARABIC_ISOLATED;
                  }
               } else if (arabicCharShaped(var9)) {
                  var21 = ARABIC_ISOLATED;
               }

               if (var21 != ARABIC_NONE) {
                  var0.addAttribute(ARABIC_FORM, var21, var10, var10 + 1);
               }
            }
         }
      }
   }

   public static boolean arabicChar(char var0) {
      return var0 >= 1536 && var0 <= 1791;
   }

   public static boolean containsArabic(AttributedString var0) {
      return containsArabic(var0.getIterator());
   }

   public static boolean containsArabic(AttributedCharacterIterator var0) {
      for(char var1 = var0.first(); var1 != '\uffff'; var1 = var0.next()) {
         if (arabicChar(var1)) {
            return true;
         }
      }

      return false;
   }

   public static boolean arabicCharTransparent(char var0) {
      if (var0 >= 1611 && var0 <= 1773) {
         return var0 <= 1621 || var0 == 1648 || var0 >= 1750 && var0 <= 1764 || var0 >= 1767 && var0 <= 1768 || var0 >= 1770;
      } else {
         return false;
      }
   }

   private static boolean arabicCharShapesRight(char var0) {
      return var0 >= 1570 && var0 <= 1573 || var0 == 1575 || var0 == 1577 || var0 >= 1583 && var0 <= 1586 || var0 == 1608 || var0 >= 1649 && var0 <= 1651 || var0 >= 1653 && var0 <= 1655 || var0 >= 1672 && var0 <= 1689 || var0 == 1728 || var0 >= 1730 && var0 <= 1739 || var0 == 1741 || var0 == 1743 || var0 >= 1746 && var0 <= 1747 || arabicCharShapesDuel(var0);
   }

   private static boolean arabicCharShapesDuel(char var0) {
      return var0 == 1574 || var0 == 1576 || var0 >= 1578 && var0 <= 1582 || var0 >= 1587 && var0 <= 1594 || var0 >= 1601 && var0 <= 1607 || var0 >= 1609 && var0 <= 1610 || var0 >= 1656 && var0 <= 1671 || var0 >= 1690 && var0 <= 1727 || var0 == 1729 || var0 == 1740 || var0 == 1742 || var0 >= 1744 && var0 <= 1745 || var0 >= 1786 && var0 <= 1788;
   }

   private static boolean arabicCharShapesLeft(char var0) {
      return arabicCharShapesDuel(var0);
   }

   private static boolean arabicCharShaped(char var0) {
      return arabicCharShapesRight(var0);
   }

   public static boolean hasSubstitute(char var0, char var1) {
      if (var0 >= doubleCharFirst && var0 <= doubleCharLast) {
         int[][] var2 = doubleCharRemappings[var0 - doubleCharFirst];
         if (var2 == null) {
            return false;
         } else {
            for(int var3 = 0; var3 < var2.length; ++var3) {
               if (var2[var3][0] == var1) {
                  return true;
               }
            }

            return false;
         }
      } else {
         return false;
      }
   }

   public static int getSubstituteChar(char var0, char var1, int var2) {
      if (var2 == 0) {
         return -1;
      } else if (var0 >= doubleCharFirst && var0 <= doubleCharLast) {
         int[][] var3 = doubleCharRemappings[var0 - doubleCharFirst];
         if (var3 == null) {
            return -1;
         } else {
            for(int var4 = 0; var4 < var3.length; ++var4) {
               if (var3[var4][0] == var1) {
                  return var3[var4][var2];
               }
            }

            return -1;
         }
      } else {
         return -1;
      }
   }

   public static int getSubstituteChar(char var0, int var1) {
      if (var1 == 0) {
         return -1;
      } else if (var0 >= singleCharFirst && var0 <= singleCharLast) {
         int[] var2 = singleCharRemappings[var0 - singleCharFirst];
         return var2 == null ? -1 : var2[var1 - 1];
      } else {
         return -1;
      }
   }

   public static String createSubstituteString(AttributedCharacterIterator var0) {
      int var1 = var0.getBeginIndex();
      int var2 = var0.getEndIndex();
      int var3 = var2 - var1;
      StringBuffer var4 = new StringBuffer(var3);

      for(int var5 = var1; var5 < var2; ++var5) {
         char var6 = var0.setIndex(var5);
         if (!arabicChar(var6)) {
            var4.append(var6);
         } else {
            Integer var7 = (Integer)var0.getAttribute(ARABIC_FORM);
            if (charStartsLigature(var6) && var5 + 1 < var2) {
               char var8 = var0.setIndex(var5 + 1);
               Integer var9 = (Integer)var0.getAttribute(ARABIC_FORM);
               if (var7 != null && var9 != null) {
                  int var10;
                  if (var7.equals(ARABIC_TERMINAL) && var9.equals(ARABIC_INITIAL)) {
                     var10 = getSubstituteChar(var6, var8, ARABIC_ISOLATED);
                     if (var10 > -1) {
                        var4.append((char)var10);
                        ++var5;
                        continue;
                     }
                  } else if (var7.equals(ARABIC_TERMINAL)) {
                     var10 = getSubstituteChar(var6, var8, ARABIC_TERMINAL);
                     if (var10 > -1) {
                        var4.append((char)var10);
                        ++var5;
                        continue;
                     }
                  } else if (var7.equals(ARABIC_MEDIAL) && var9.equals(ARABIC_MEDIAL)) {
                     var10 = getSubstituteChar(var6, var8, ARABIC_MEDIAL);
                     if (var10 > -1) {
                        var4.append((char)var10);
                        ++var5;
                        continue;
                     }
                  }
               }
            }

            if (var7 != null && var7 > 0) {
               int var11 = getSubstituteChar(var6, var7);
               if (var11 > -1) {
                  var6 = (char)var11;
               }
            }

            var4.append(var6);
         }
      }

      return var4.toString();
   }

   public static boolean charStartsLigature(char var0) {
      return var0 == 1611 || var0 == 1612 || var0 == 1613 || var0 == 1614 || var0 == 1615 || var0 == 1616 || var0 == 1617 || var0 == 1618 || var0 == 1570 || var0 == 1571 || var0 == 1573 || var0 == 1575;
   }

   public static int getNumChars(char var0) {
      return isLigature(var0) ? 2 : 1;
   }

   public static boolean isLigature(char var0) {
      if (var0 >= 'ﹰ' && var0 <= 'ﻼ') {
         return var0 <= 'ﹲ' || var0 == 'ﹴ' || var0 >= 'ﹶ' && var0 <= 'ﹿ' || var0 >= 'ﻵ';
      } else {
         return false;
      }
   }

   static {
      ARABIC_FORM = GVTAttributedCharacterIterator.TextAttribute.ARABIC_FORM;
      ARABIC_NONE = GVTAttributedCharacterIterator.TextAttribute.ARABIC_NONE;
      ARABIC_ISOLATED = GVTAttributedCharacterIterator.TextAttribute.ARABIC_ISOLATED;
      ARABIC_TERMINAL = GVTAttributedCharacterIterator.TextAttribute.ARABIC_TERMINAL;
      ARABIC_INITIAL = GVTAttributedCharacterIterator.TextAttribute.ARABIC_INITIAL;
      ARABIC_MEDIAL = GVTAttributedCharacterIterator.TextAttribute.ARABIC_MEDIAL;
      singleCharFirst = 1569;
      singleCharLast = 1610;
      singleCharRemappings = new int[][]{{65152, -1, -1, -1}, {65153, 65154, -1, -1}, {65155, 65156, -1, -1}, {65157, 65158, -1, -1}, {65159, 65160, -1, -1}, {65161, 65162, 65163, 65164}, {65165, 65166, -1, -1}, {65167, 65168, 65169, 65170}, {65171, 65172, -1, -1}, {65173, 65174, 65175, 65176}, {65177, 65178, 65179, 65180}, {65181, 65182, 65183, 65184}, {65185, 65186, 65187, 65188}, {65189, 65190, 65191, 65192}, {65193, 65194, -1, -1}, {65195, 65196, -1, -1}, {65197, 65198, -1, -1}, {65199, 65200, -1, -1}, {65201, 65202, 65203, 65204}, {65205, 65206, 65207, 65208}, {65209, 65210, 65211, 65212}, {65213, 65214, 65215, 65216}, {65217, 65218, 65219, 65220}, {65221, 65222, 65223, 65224}, {65225, 65226, 65227, 65228}, {65229, 65230, 65231, 65232}, null, null, null, null, null, null, {65233, 65234, 65235, 65236}, {65237, 65238, 65239, 65240}, {65241, 65242, 65243, 65244}, {65245, 65246, 65247, 65248}, {65249, 65250, 65251, 65252}, {65253, 65254, 65255, 65256}, {65257, 65258, 65259, 65260}, {65261, 65262, -1, -1}, {65263, 65264, -1, -1}, {65265, 65266, 65267, 65268}};
      doubleCharFirst = 1570;
      doubleCharLast = 1618;
      doubleCharRemappings = new int[][][]{{{1604, 65269, 65270, -1, -1}}, {{1604, 65271, 65272, -1, -1}}, (int[][])null, {{1604, 65273, 65274, -1, -1}}, (int[][])null, {{1604, 65275, 65276, -1, -1}}, (int[][])null, (int[][])null, (int[][])null, (int[][])null, (int[][])null, (int[][])null, (int[][])null, (int[][])null, (int[][])null, (int[][])null, (int[][])null, (int[][])null, (int[][])null, (int[][])null, (int[][])null, (int[][])null, (int[][])null, (int[][])null, (int[][])null, (int[][])null, (int[][])null, (int[][])null, (int[][])null, (int[][])null, (int[][])null, (int[][])null, (int[][])null, (int[][])null, (int[][])null, {{32, 65136, -1, -1, -1}, {1600, -1, -1, -1, 65137}}, {{32, 65138, -1, -1, -1}}, {{32, 65140, -1, -1, -1}}, {{32, 65142, -1, -1, -1}, {1600, -1, -1, -1, 65143}}, {{32, 65144, -1, -1, -1}, {1600, -1, -1, -1, 65145}}, {{32, 65146, -1, -1, -1}, {1600, -1, -1, -1, 65147}}, {{32, 65148, -1, -1, -1}, {1600, -1, -1, -1, 65149}}, {{32, 65150, -1, -1, -1}, {1600, -1, -1, -1, 65151}}};
   }
}

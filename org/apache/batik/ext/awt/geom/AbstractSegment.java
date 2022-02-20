package org.apache.batik.ext.awt.geom;

import java.awt.geom.Point2D.Double;
import java.util.Arrays;

public abstract class AbstractSegment implements Segment {
   static final double eps = 3.552713678800501E-15D;
   static final double tol = 1.4210854715202004E-14D;

   protected abstract int findRoots(double var1, double[] var3);

   public Segment.SplitResults split(double var1) {
      double[] var3 = new double[]{0.0D, 0.0D, 0.0D};
      int var4 = this.findRoots(var1, var3);
      if (var4 == 0) {
         return null;
      } else {
         Arrays.sort(var3, 0, var4);
         double[] var5 = new double[var4 + 2];
         byte var6 = 0;
         int var18 = var6 + 1;
         var5[var6] = 0.0D;

         double var8;
         for(int var7 = 0; var7 < var4; ++var7) {
            var8 = var3[var7];
            if (!(var8 <= 0.0D)) {
               if (var8 >= 1.0D) {
                  break;
               }

               if (var5[var18 - 1] != var8) {
                  var5[var18++] = var8;
               }
            }
         }

         var5[var18++] = 1.0D;
         if (var18 == 2) {
            return null;
         } else {
            Segment[] var19 = new Segment[var18];
            var8 = 0.0D;
            int var10 = 0;
            boolean var11 = false;
            boolean var12 = false;

            for(int var13 = 1; var13 < var18; ++var13) {
               var19[var10] = this.getSegment(var5[var13 - 1], var5[var13]);
               Double var14 = var19[var10].eval(0.5D);
               if (var10 == 0) {
                  ++var10;
                  var11 = var12 = var14.y < var1;
               } else {
                  boolean var15 = var14.y < var1;
                  if (var12 == var15) {
                     var19[var10 - 1] = this.getSegment(var8, var5[var13]);
                  } else {
                     ++var10;
                     var8 = var5[var13 - 1];
                     var12 = var15;
                  }
               }
            }

            if (var10 == 1) {
               return null;
            } else {
               Segment[] var20;
               Segment[] var21;
               if (var11) {
                  var21 = new Segment[(var10 + 1) / 2];
                  var20 = new Segment[var10 / 2];
               } else {
                  var21 = new Segment[var10 / 2];
                  var20 = new Segment[(var10 + 1) / 2];
               }

               int var22 = 0;
               int var16 = 0;

               for(int var17 = 0; var17 < var10; ++var17) {
                  if (var11) {
                     var21[var22++] = var19[var17];
                  } else {
                     var20[var16++] = var19[var17];
                  }

                  var11 = !var11;
               }

               return new Segment.SplitResults(var20, var21);
            }
         }
      }
   }

   public Segment splitBefore(double var1) {
      return this.getSegment(0.0D, var1);
   }

   public Segment splitAfter(double var1) {
      return this.getSegment(var1, 1.0D);
   }

   public static int solveLine(double var0, double var2, double[] var4) {
      if (var0 == 0.0D) {
         if (var2 != 0.0D) {
            return 0;
         } else {
            var4[0] = 0.0D;
            return 1;
         }
      } else {
         var4[0] = -var2 / var0;
         return 1;
      }
   }

   public static int solveQuad(double var0, double var2, double var4, double[] var6) {
      if (var0 == 0.0D) {
         return solveLine(var2, var4, var6);
      } else {
         double var7 = var2 * var2 - 4.0D * var0 * var4;
         if (Math.abs(var7) <= 1.4210854715202004E-14D * var2 * var2) {
            var6[0] = -var2 / (2.0D * var0);
            return 1;
         } else if (var7 < 0.0D) {
            return 0;
         } else {
            var7 = Math.sqrt(var7);
            double var9 = -(var2 + matchSign(var7, var2));
            var6[0] = 2.0D * var4 / var9;
            var6[1] = var9 / (2.0D * var0);
            return 2;
         }
      }
   }

   public static double matchSign(double var0, double var2) {
      if (var2 < 0.0D) {
         return var0 < 0.0D ? var0 : -var0;
      } else {
         return var0 > 0.0D ? var0 : -var0;
      }
   }

   public static int solveCubic(double var0, double var2, double var4, double var6, double[] var8) {
      double[] var9 = new double[]{0.0D, 0.0D};
      int var10 = solveQuad(3.0D * var0, 2.0D * var2, var4, var9);
      double[] var11 = new double[]{0.0D, 0.0D, 0.0D, 0.0D};
      double[] var12 = new double[]{0.0D, 0.0D, 0.0D, 0.0D};
      byte var13 = 0;
      var11[var13] = var6;
      int var37 = var13 + 1;
      var12[var13] = 0.0D;
      double var14;
      switch(var10) {
      case 1:
         var14 = var9[0];
         if (var14 > 0.0D && var14 < 1.0D) {
            var11[var37] = ((var0 * var14 + var2) * var14 + var4) * var14 + var6;
            var12[var37++] = var14;
         }
         break;
      case 2:
         if (var9[0] > var9[1]) {
            double var16 = var9[0];
            var9[0] = var9[1];
            var9[1] = var16;
         }

         var14 = var9[0];
         if (var14 > 0.0D && var14 < 1.0D) {
            var11[var37] = ((var0 * var14 + var2) * var14 + var4) * var14 + var6;
            var12[var37++] = var14;
         }

         var14 = var9[1];
         if (var14 > 0.0D && var14 < 1.0D) {
            var11[var37] = ((var0 * var14 + var2) * var14 + var4) * var14 + var6;
            var12[var37++] = var14;
         }
      }

      var11[var37] = var0 + var2 + var4 + var6;
      var12[var37++] = 1.0D;
      int var38 = 0;

      for(int var17 = 0; var17 < var37 - 1; ++var17) {
         double var18 = var11[var17];
         double var20 = var12[var17];
         double var22 = var11[var17 + 1];
         double var24 = var12[var17 + 1];
         if ((!(var18 < 0.0D) || !(var22 < 0.0D)) && (!(var18 > 0.0D) || !(var22 > 0.0D))) {
            double var26;
            if (var18 > var22) {
               var26 = var18;
               var18 = var22;
               var22 = var26;
               var26 = var20;
               var20 = var24;
               var24 = var26;
            }

            if (-var18 < 1.4210854715202004E-14D * var22) {
               var8[var38++] = var20;
            } else if (var22 < -1.4210854715202004E-14D * var18) {
               var8[var38++] = var24;
               ++var17;
            } else {
               var26 = 1.4210854715202004E-14D * (var22 - var18);

               int var28;
               for(var28 = 0; var28 < 20; ++var28) {
                  double var29 = var24 - var20;
                  double var31 = var22 - var18;
                  double var33 = var20 + (Math.abs(var18 / var31) * 99.0D + 0.5D) * var29 / 100.0D;
                  double var35 = ((var0 * var33 + var2) * var33 + var4) * var33 + var6;
                  if (Math.abs(var35) < var26) {
                     var8[var38++] = var33;
                     break;
                  }

                  if (var35 < 0.0D) {
                     var20 = var33;
                     var18 = var35;
                  } else {
                     var24 = var33;
                     var22 = var35;
                  }
               }

               if (var28 == 20) {
                  var8[var38++] = (var20 + var24) / 2.0D;
               }
            }
         }
      }

      return var38;
   }
}

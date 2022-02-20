package org.apache.batik.transcoder.wmf.tosvg;

import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D.Float;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import org.apache.batik.ext.awt.geom.Polygon2D;
import org.apache.batik.ext.awt.geom.Polyline2D;

public class WMFHeaderProperties extends AbstractWMFReader {
   private static final Integer INTEGER_0 = new Integer(0);
   protected DataInputStream stream;
   private int _bleft;
   private int _bright;
   private int _btop;
   private int _bbottom;
   private int _bwidth;
   private int _bheight;
   private int _ileft;
   private int _iright;
   private int _itop;
   private int _ibottom;
   private float scale = 1.0F;
   private int startX = 0;
   private int startY = 0;
   private int currentHorizAlign = 0;
   private int currentVertAlign = 0;
   private WMFFont wf = null;
   private static final FontRenderContext fontCtx = new FontRenderContext(new AffineTransform(), false, true);
   private transient boolean firstEffectivePaint = true;
   public static final int PEN = 1;
   public static final int BRUSH = 2;
   public static final int FONT = 3;
   public static final int NULL_PEN = 4;
   public static final int NULL_BRUSH = 5;
   public static final int PALETTE = 6;
   public static final int OBJ_BITMAP = 7;
   public static final int OBJ_REGION = 8;

   public WMFHeaderProperties(File var1) throws IOException {
      this.reset();
      this.stream = new DataInputStream(new BufferedInputStream(new FileInputStream(var1)));
      this.read(this.stream);
      this.stream.close();
   }

   public WMFHeaderProperties() {
   }

   public void closeResource() {
      try {
         if (this.stream != null) {
            this.stream.close();
         }
      } catch (IOException var2) {
      }

   }

   public void setFile(File var1) throws IOException {
      this.stream = new DataInputStream(new BufferedInputStream(new FileInputStream(var1)));
      this.read(this.stream);
      this.stream.close();
   }

   public void reset() {
      this.left = 0;
      this.right = 0;
      this.top = 1000;
      this.bottom = 1000;
      this.inch = 84;
      this._bleft = -1;
      this._bright = -1;
      this._btop = -1;
      this._bbottom = -1;
      this._ileft = -1;
      this._iright = -1;
      this._itop = -1;
      this._ibottom = -1;
      this._bwidth = -1;
      this._bheight = -1;
      this.vpW = -1;
      this.vpH = -1;
      this.vpX = 0;
      this.vpY = 0;
      this.startX = 0;
      this.startY = 0;
      this.scaleXY = 1.0F;
      this.firstEffectivePaint = true;
   }

   public DataInputStream getStream() {
      return this.stream;
   }

   protected boolean readRecords(DataInputStream var1) throws IOException {
      short var2 = 1;
      boolean var3 = false;
      short var5 = -1;
      short var6 = -1;
      short var7 = -1;

      label289:
      while(var2 > 0) {
         int var33 = this.readInt(var1);
         var33 -= 3;
         var2 = this.readShort(var1);
         if (var2 <= 0) {
            break;
         }

         short var4;
         GdiObject var8;
         byte var9;
         float var10;
         float var11;
         float var12;
         float var13;
         int var14;
         int var16;
         int var10000;
         int var24;
         int var25;
         short var34;
         short var35;
         boolean var36;
         int var37;
         short var38;
         int var39;
         int var40;
         short var41;
         int var42;
         int var46;
         float[] var47;
         Float var48;
         float[] var50;
         short var53;
         Color var55;
         switch(var2) {
         case 247:
            var36 = false;

            for(var37 = 0; var37 < var33; ++var37) {
               this.readShort(var1);
            }

            var39 = this.addObjectAt(8, INTEGER_0, 0);
            break;
         case 259:
            var34 = this.readShort(var1);
            if (var34 == 8) {
               this.isotropic = false;
            }
            break;
         case 301:
            var4 = this.readShort(var1);
            if ((var4 & Integer.MIN_VALUE) == 0) {
               var8 = this.getObject(var4);
               if (var8.used) {
                  switch(var8.type) {
                  case 1:
                     var6 = var4;
                     break;
                  case 2:
                     var5 = var4;
                     break;
                  case 3:
                     this.wf = (WMFFont)var8.obj;
                     var7 = var4;
                     break;
                  case 4:
                     var6 = -1;
                     break;
                  case 5:
                     var5 = -1;
                  }
               }
            }
            break;
         case 302:
            var34 = this.readShort(var1);
            if (var33 > 1) {
               for(var39 = 1; var39 < var33; ++var39) {
                  this.readShort(var1);
               }
            }

            this.currentHorizAlign = WMFUtilities.getHorizontalAlignment(var34);
            this.currentVertAlign = WMFUtilities.getVerticalAlignment(var34);
            break;
         case 496:
            var4 = this.readShort(var1);
            var8 = this.getObject(var4);
            if (var4 == var5) {
               var5 = -1;
            } else if (var4 == var6) {
               var6 = -1;
            } else if (var4 == var7) {
               var7 = -1;
            }

            var8.clear();
            break;
         case 523:
            this.vpY = this.readShort(var1);
            this.vpX = this.readShort(var1);
            break;
         case 524:
            this.vpH = this.readShort(var1);
            this.vpW = this.readShort(var1);
            if (!this.isotropic) {
               this.scaleXY = (float)this.vpW / (float)this.vpH;
            }

            this.vpW = (int)((float)this.vpW * this.scaleXY);
            break;
         case 531:
            var35 = this.readShort(var1);
            var37 = (int)((float)this.readShort(var1) * this.scaleXY);
            if (var6 >= 0) {
               this.resizeBounds(this.startX, this.startY);
               this.resizeBounds(var37, var35);
               this.firstEffectivePaint = false;
            }

            this.startX = var37;
            this.startY = var35;
            break;
         case 532:
            this.startY = this.readShort(var1);
            this.startX = (int)((float)this.readShort(var1) * this.scaleXY);
            break;
         case 762:
            var9 = 0;
            var35 = this.readShort(var1);
            this.readInt(var1);
            var37 = this.readInt(var1);
            var40 = var37 & 255;
            var42 = (var37 & '\uff00') >> 8;
            var14 = (var37 & 16711680) >> 16;
            var55 = new Color(var40, var42, var14);
            if (var33 == 6) {
               this.readShort(var1);
            }

            if (var35 == 5) {
               this.addObjectAt(4, var55, var9);
            } else {
               this.addObjectAt(1, var55, var9);
            }
            break;
         case 763:
            var35 = this.readShort(var1);
            var11 = (float)((int)(this.scaleY * (float)var35));
            this.readShort(var1);
            var41 = this.readShort(var1);
            var53 = this.readShort(var1);
            short var57 = this.readShort(var1);
            byte var60 = var1.readByte();
            byte var58 = var1.readByte();
            byte var63 = var1.readByte();
            int var64 = var1.readByte() & 255;
            byte var20 = var1.readByte();
            byte var66 = var1.readByte();
            byte var67 = var1.readByte();
            byte var68 = var1.readByte();
            var24 = var60 > 0 ? 2 : 0;
            var24 |= var57 > 400 ? 1 : 0;
            var25 = 2 * (var33 - 9);
            byte[] var70 = new byte[var25];

            for(int var28 = 0; var28 < var25; ++var28) {
               var70[var28] = var1.readByte();
            }

            String var71 = new String(var70);

            int var29;
            for(var29 = 0; var29 < var71.length() && (Character.isLetterOrDigit(var71.charAt(var29)) || Character.isWhitespace(var71.charAt(var29))); ++var29) {
            }

            if (var29 > 0) {
               var71 = var71.substring(0, var29);
            } else {
               var71 = "System";
            }

            if (var11 < 0.0F) {
               var11 = -var11;
            }

            byte var30 = 0;
            Font var31 = new Font(var71, var24, (int)var11);
            var31 = var31.deriveFont(var11);
            WMFFont var32 = new WMFFont(var31, var64, var58, var63, var60, var57, var53, var41);
            this.addObjectAt(3, var32, var30);
            break;
         case 764:
            var9 = 0;
            var35 = this.readShort(var1);
            var37 = this.readInt(var1);
            var40 = var37 & 255;
            var42 = (var37 & '\uff00') >> 8;
            var14 = (var37 & 16711680) >> 16;
            var55 = new Color(var40, var42, var14);
            this.readShort(var1);
            if (var35 == 5) {
               this.addObjectAt(5, var55, var9);
            } else {
               this.addObjectAt(2, var55, var9);
            }
            break;
         case 804:
            var35 = this.readShort(var1);
            var50 = new float[var35 + 1];
            var47 = new float[var35 + 1];

            for(var42 = 0; var42 < var35; ++var42) {
               var50[var42] = (float)this.readShort(var1) * this.scaleXY;
               var47[var42] = (float)this.readShort(var1);
            }

            var50[var35] = var50[0];
            var47[var35] = var47[0];
            Polygon2D var65 = new Polygon2D(var50, var47, var35);
            this.paint(var5, var6, var65);
            break;
         case 805:
            var35 = this.readShort(var1);
            var50 = new float[var35];
            var47 = new float[var35];

            for(var42 = 0; var42 < var35; ++var42) {
               var50[var42] = (float)this.readShort(var1) * this.scaleXY;
               var47[var42] = (float)this.readShort(var1);
            }

            Polyline2D var61 = new Polyline2D(var50, var47, var35);
            this.paintWithPen(var6, var61);
            break;
         case 1046:
         case 1048:
         case 1051:
            var35 = this.readShort(var1);
            var37 = (int)((float)this.readShort(var1) * this.scaleXY);
            var38 = this.readShort(var1);
            var42 = (int)((float)this.readShort(var1) * this.scaleXY);
            var48 = new Float((float)var42, (float)var38, (float)(var37 - var42), (float)(var35 - var38));
            this.paint(var5, var6, var48);
            break;
         case 1313:
         case 1583:
            var35 = this.readShort(var1);
            byte var44 = 1;
            byte[] var45 = new byte[var35];

            for(var42 = 0; var42 < var35; ++var42) {
               var45[var42] = var1.readByte();
            }

            String var52 = WMFUtilities.decodeString(this.wf, var45);
            if (var35 % 2 != 0) {
               var1.readByte();
            }

            var37 = var44 + (var35 + 1) / 2;
            var53 = this.readShort(var1);
            var10000 = (int)((float)this.readShort(var1) * this.scaleXY);
            var37 += 2;
            if (var37 < var33) {
               for(var16 = var37; var16 < var33; ++var16) {
                  this.readShort(var1);
               }
            }

            TextLayout var59 = new TextLayout(var52, this.wf.font, fontCtx);
            int var56 = (int)var59.getBounds().getWidth();
            var46 = (int)var59.getBounds().getX();
            int var62 = (int)this.getVerticalAlignmentValue(var59, this.currentVertAlign);
            this.resizeBounds(var46, var53);
            this.resizeBounds(var46 + var56, var53 + var62);
            break;
         case 1336:
            var35 = this.readShort(var1);
            int[] var43 = new int[var35];
            var40 = 0;

            for(var42 = 0; var42 < var35; ++var42) {
               var43[var42] = this.readShort(var1);
               var40 += var43[var42];
            }

            var42 = var35 + 1;

            for(var14 = 0; var14 < var35; ++var14) {
               for(var46 = 0; var46 < var43[var14]; ++var46) {
                  var16 = (int)((float)this.readShort(var1) * this.scaleXY);
                  short var54 = this.readShort(var1);
                  if (var5 >= 0 || var6 >= 0) {
                     this.resizeBounds(var16, var54);
                  }
               }
            }

            this.firstEffectivePaint = false;
            break;
         case 1564:
            this.readShort(var1);
            this.readShort(var1);
            var35 = this.readShort(var1);
            var37 = (int)((float)this.readShort(var1) * this.scaleXY);
            var38 = this.readShort(var1);
            var42 = (int)((float)this.readShort(var1) * this.scaleXY);
            var48 = new Float((float)var42, (float)var38, (float)(var37 - var42), (float)(var35 - var38));
            this.paint(var5, var6, var48);
            break;
         case 1565:
            this.readInt(var1);
            var35 = this.readShort(var1);
            var37 = (int)((float)this.readShort(var1) * this.scaleXY);
            var40 = (int)((float)this.readShort(var1) * this.scaleXY);
            var41 = this.readShort(var1);
            if (var6 >= 0) {
               this.resizeBounds(var40, var41);
            }

            if (var6 >= 0) {
               this.resizeBounds(var40 + var37, var41 + var35);
            }
            break;
         case 1791:
            var36 = false;

            for(var37 = 0; var37 < var33; ++var37) {
               this.readShort(var1);
            }

            var39 = this.addObjectAt(6, INTEGER_0, 0);
            break;
         case 2071:
         case 2074:
         case 2096:
            this.readShort(var1);
            this.readShort(var1);
            this.readShort(var1);
            this.readShort(var1);
            var35 = this.readShort(var1);
            var37 = (int)((float)this.readShort(var1) * this.scaleXY);
            var38 = this.readShort(var1);
            var42 = (int)((float)this.readShort(var1) * this.scaleXY);
            var48 = new Float((float)var42, (float)var38, (float)(var37 - var42), (float)(var35 - var38));
            this.paint(var5, var6, var48);
            break;
         case 2368:
            var1.readInt();
            this.readShort(var1);
            this.readShort(var1);
            this.readShort(var1);
            var10 = (float)this.readShort(var1) * (float)this.inch / PIXEL_PER_INCH * this.getVpHFactor();
            var11 = (float)this.readShort(var1) * (float)this.inch / PIXEL_PER_INCH * this.getVpWFactor() * this.scaleXY;
            var12 = (float)this.inch / PIXEL_PER_INCH * this.getVpHFactor() * (float)this.readShort(var1);
            var13 = (float)this.inch / PIXEL_PER_INCH * this.getVpWFactor() * (float)this.readShort(var1) * this.scaleXY;
            this.resizeImageBounds((int)var13, (int)var12);
            this.resizeImageBounds((int)(var13 + var11), (int)(var12 + var10));
            break;
         case 2610:
            var35 = this.readShort(var1);
            var10000 = (int)((float)this.readShort(var1) * this.scaleXY);
            var38 = this.readShort(var1);
            var41 = this.readShort(var1);
            var14 = 4;
            boolean var49 = false;
            boolean var51 = false;
            boolean var17 = false;
            boolean var18 = false;
            boolean var19 = false;
            if ((var41 & 4) != 0) {
               var10000 = (int)((float)this.readShort(var1) * this.scaleXY);
               this.readShort(var1);
               var10000 = (int)((float)this.readShort(var1) * this.scaleXY);
               this.readShort(var1);
               var14 += 4;
               var49 = true;
            }

            byte[] var21 = new byte[var38];

            for(int var22 = 0; var22 < var38; ++var22) {
               var21[var22] = var1.readByte();
            }

            String var23 = WMFUtilities.decodeString(this.wf, var21);
            var14 += (var38 + 1) / 2;
            if (var38 % 2 != 0) {
               var1.readByte();
            }

            if (var14 < var33) {
               for(var24 = var14; var24 < var33; ++var24) {
                  this.readShort(var1);
               }
            }

            TextLayout var69 = new TextLayout(var23, this.wf.font, fontCtx);
            var25 = (int)var69.getBounds().getWidth();
            var37 = (int)var69.getBounds().getX();
            int var26 = (int)this.getVerticalAlignmentValue(var69, this.currentVertAlign);
            this.resizeBounds(var37, var35);
            this.resizeBounds(var37 + var25, var35 + var26);
            this.firstEffectivePaint = false;
            break;
         case 2881:
            var1.readInt();
            this.readShort(var1);
            this.readShort(var1);
            this.readShort(var1);
            this.readShort(var1);
            var10 = (float)this.readShort(var1);
            var11 = (float)this.readShort(var1) * this.scaleXY;
            var12 = (float)this.readShort(var1) * this.getVpWFactor() * (float)this.inch / PIXEL_PER_INCH;
            var13 = (float)this.readShort(var1) * this.getVpWFactor() * (float)this.inch / PIXEL_PER_INCH * this.scaleXY;
            var11 = var11 * this.getVpWFactor() * (float)this.inch / PIXEL_PER_INCH;
            var10 = var10 * this.getVpHFactor() * (float)this.inch / PIXEL_PER_INCH;
            this.resizeImageBounds((int)var13, (int)var12);
            this.resizeImageBounds((int)(var13 + var11), (int)(var12 + var10));
            var14 = 2 * var33 - 20;
            var46 = 0;

            while(true) {
               if (var46 >= var14) {
                  continue label289;
               }

               var1.readByte();
               ++var46;
            }
         case 3907:
            var1.readInt();
            this.readShort(var1);
            this.readShort(var1);
            this.readShort(var1);
            this.readShort(var1);
            this.readShort(var1);
            var10 = (float)this.readShort(var1);
            var11 = (float)this.readShort(var1) * this.scaleXY;
            var12 = (float)this.readShort(var1) * this.getVpHFactor() * (float)this.inch / PIXEL_PER_INCH;
            var13 = (float)this.readShort(var1) * this.getVpHFactor() * (float)this.inch / PIXEL_PER_INCH * this.scaleXY;
            var11 = var11 * this.getVpWFactor() * (float)this.inch / PIXEL_PER_INCH;
            var10 = var10 * this.getVpHFactor() * (float)this.inch / PIXEL_PER_INCH;
            this.resizeImageBounds((int)var13, (int)var12);
            this.resizeImageBounds((int)(var13 + var11), (int)(var12 + var10));
            var14 = 2 * var33 - 22;
            byte[] var15 = new byte[var14];
            var16 = 0;

            while(true) {
               if (var16 >= var14) {
                  continue label289;
               }

               var15[var16] = var1.readByte();
               ++var16;
            }
         default:
            for(var39 = 0; var39 < var33; ++var39) {
               this.readShort(var1);
            }
         }
      }

      if (!this.isAldus) {
         this.width = this.vpW;
         this.height = this.vpH;
         this.right = this.vpX;
         this.left = this.vpX + this.vpW;
         this.top = this.vpY;
         this.bottom = this.vpY + this.vpH;
      }

      this.resetBounds();
      return true;
   }

   public int getWidthBoundsPixels() {
      return this._bwidth;
   }

   public int getHeightBoundsPixels() {
      return this._bheight;
   }

   public int getWidthBoundsUnits() {
      return (int)((float)this.inch * (float)this._bwidth / PIXEL_PER_INCH);
   }

   public int getHeightBoundsUnits() {
      return (int)((float)this.inch * (float)this._bheight / PIXEL_PER_INCH);
   }

   public int getXOffset() {
      return this._bleft;
   }

   public int getYOffset() {
      return this._btop;
   }

   private void resetBounds() {
      this.scale = (float)this.getWidthPixels() / (float)this.vpW;
      if (this._bright != -1) {
         this._bright = (int)(this.scale * (float)(this.vpX + this._bright));
         this._bleft = (int)(this.scale * (float)(this.vpX + this._bleft));
         this._bbottom = (int)(this.scale * (float)(this.vpY + this._bbottom));
         this._btop = (int)(this.scale * (float)(this.vpY + this._btop));
      }

      if (this._iright != -1) {
         this._iright = (int)((float)this._iright * (float)this.getWidthPixels() / (float)this.width);
         this._ileft = (int)((float)this._ileft * (float)this.getWidthPixels() / (float)this.width);
         this._ibottom = (int)((float)this._ibottom * (float)this.getWidthPixels() / (float)this.width);
         this._itop = (int)((float)this._itop * (float)this.getWidthPixels() / (float)this.width);
         if (this._bright == -1 || this._iright > this._bright) {
            this._bright = this._iright;
         }

         if (this._bleft == -1 || this._ileft < this._bleft) {
            this._bleft = this._ileft;
         }

         if (this._btop == -1 || this._itop < this._btop) {
            this._btop = this._itop;
         }

         if (this._bbottom == -1 || this._ibottom > this._bbottom) {
            this._bbottom = this._ibottom;
         }
      }

      if (this._bleft != -1 && this._bright != -1) {
         this._bwidth = this._bright - this._bleft;
      }

      if (this._btop != -1 && this._bbottom != -1) {
         this._bheight = this._bbottom - this._btop;
      }

   }

   private void resizeBounds(int var1, int var2) {
      if (this._bleft == -1) {
         this._bleft = var1;
      } else if (var1 < this._bleft) {
         this._bleft = var1;
      }

      if (this._bright == -1) {
         this._bright = var1;
      } else if (var1 > this._bright) {
         this._bright = var1;
      }

      if (this._btop == -1) {
         this._btop = var2;
      } else if (var2 < this._btop) {
         this._btop = var2;
      }

      if (this._bbottom == -1) {
         this._bbottom = var2;
      } else if (var2 > this._bbottom) {
         this._bbottom = var2;
      }

   }

   private void resizeImageBounds(int var1, int var2) {
      if (this._ileft == -1) {
         this._ileft = var1;
      } else if (var1 < this._ileft) {
         this._ileft = var1;
      }

      if (this._iright == -1) {
         this._iright = var1;
      } else if (var1 > this._iright) {
         this._iright = var1;
      }

      if (this._itop == -1) {
         this._itop = var2;
      } else if (var2 < this._itop) {
         this._itop = var2;
      }

      if (this._ibottom == -1) {
         this._ibottom = var2;
      } else if (var2 > this._ibottom) {
         this._ibottom = var2;
      }

   }

   private Color getColorFromObject(int var1) {
      Object var2 = null;
      if (var1 >= 0) {
         GdiObject var3 = this.getObject(var1);
         return (Color)var3.obj;
      } else {
         return null;
      }
   }

   private void paint(int var1, int var2, Shape var3) {
      if (var1 >= 0 || var2 >= 0) {
         Color var4;
         if (var1 >= 0) {
            var4 = this.getColorFromObject(var1);
         } else {
            var4 = this.getColorFromObject(var2);
         }

         if (!this.firstEffectivePaint || !var4.equals(Color.white)) {
            Rectangle var5 = var3.getBounds();
            this.resizeBounds((int)var5.getMinX(), (int)var5.getMinY());
            this.resizeBounds((int)var5.getMaxX(), (int)var5.getMaxY());
            this.firstEffectivePaint = false;
         }
      }

   }

   private void paintWithPen(int var1, Shape var2) {
      if (var1 >= 0) {
         Color var3 = this.getColorFromObject(var1);
         if (!this.firstEffectivePaint || !var3.equals(Color.white)) {
            Rectangle var4 = var2.getBounds();
            this.resizeBounds((int)var4.getMinX(), (int)var4.getMinY());
            this.resizeBounds((int)var4.getMaxX(), (int)var4.getMaxY());
            this.firstEffectivePaint = false;
         }
      }

   }

   private float getVerticalAlignmentValue(TextLayout var1, int var2) {
      if (var2 == 24) {
         return -var1.getAscent();
      } else {
         return var2 == 0 ? var1.getAscent() + var1.getDescent() : 0.0F;
      }
   }
}

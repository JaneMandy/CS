package com.mxgraph.shape;

import com.mxgraph.canvas.mxGraphics2DCanvas;
import com.mxgraph.view.mxCellState;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.GeneralPath;

public class mxCloudShape extends mxBasicShape {
   public Shape createShape(mxGraphics2DCanvas var1, mxCellState var2) {
      Rectangle var3 = var2.getRectangle();
      int var4 = var3.x;
      int var5 = var3.y;
      int var6 = var3.width;
      int var7 = var3.height;
      GeneralPath var8 = new GeneralPath();
      var8.moveTo((float)((double)var4 + 0.25D * (double)var6), (float)((double)var5 + 0.25D * (double)var7));
      var8.curveTo((float)((double)var4 + 0.05D * (double)var6), (float)((double)var5 + 0.25D * (double)var7), (float)var4, (float)((double)var5 + 0.5D * (double)var7), (float)((double)var4 + 0.16D * (double)var6), (float)((double)var5 + 0.55D * (double)var7));
      var8.curveTo((float)var4, (float)((double)var5 + 0.66D * (double)var7), (float)((double)var4 + 0.18D * (double)var6), (float)((double)var5 + 0.9D * (double)var7), (float)((double)var4 + 0.31D * (double)var6), (float)((double)var5 + 0.8D * (double)var7));
      var8.curveTo((float)((double)var4 + 0.4D * (double)var6), (float)(var5 + var7), (float)((double)var4 + 0.7D * (double)var6), (float)(var5 + var7), (float)((double)var4 + 0.8D * (double)var6), (float)((double)var5 + 0.8D * (double)var7));
      var8.curveTo((float)(var4 + var6), (float)((double)var5 + 0.8D * (double)var7), (float)(var4 + var6), (float)((double)var5 + 0.6D * (double)var7), (float)((double)var4 + 0.875D * (double)var6), (float)((double)var5 + 0.5D * (double)var7));
      var8.curveTo((float)(var4 + var6), (float)((double)var5 + 0.3D * (double)var7), (float)((double)var4 + 0.8D * (double)var6), (float)((double)var5 + 0.1D * (double)var7), (float)((double)var4 + 0.625D * (double)var6), (float)((double)var5 + 0.2D * (double)var7));
      var8.curveTo((float)((double)var4 + 0.5D * (double)var6), (float)((double)var5 + 0.05D * (double)var7), (float)((double)var4 + 0.3D * (double)var6), (float)((double)var5 + 0.05D * (double)var7), (float)((double)var4 + 0.25D * (double)var6), (float)((double)var5 + 0.25D * (double)var7));
      var8.closePath();
      return var8;
   }
}

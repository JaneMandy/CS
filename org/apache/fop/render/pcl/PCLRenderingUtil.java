package org.apache.fop.render.pcl;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Float;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.apps.FOUserAgent;
import org.apache.xmlgraphics.util.UnitConv;

public class PCLRenderingUtil {
   private static Log log;
   private FOUserAgent userAgent;
   private PCLRenderingMode renderingMode;
   private boolean allTextAsBitmaps;
   private boolean useColorCanvas;
   private boolean disabledPJL;

   PCLRenderingUtil(FOUserAgent userAgent) {
      this.renderingMode = PCLRenderingMode.SPEED;
      this.allTextAsBitmaps = false;
      this.useColorCanvas = false;
      this.disabledPJL = false;
      this.userAgent = userAgent;
      this.initialize();
   }

   private void initialize() {
   }

   public FOUserAgent getUserAgent() {
      return this.userAgent;
   }

   public void setRenderingMode(PCLRenderingMode mode) {
      this.renderingMode = mode;
   }

   public PCLRenderingMode getRenderingMode() {
      return this.renderingMode;
   }

   public void setPJLDisabled(boolean disable) {
      this.disabledPJL = disable;
   }

   public boolean isPJLDisabled() {
      return this.disabledPJL;
   }

   public void setAllTextAsBitmaps(boolean allTextAsBitmaps) {
      this.allTextAsBitmaps = allTextAsBitmaps;
   }

   public boolean isAllTextAsBitmaps() {
      return this.allTextAsBitmaps;
   }

   public boolean isColorCanvasEnabled() {
      return this.useColorCanvas;
   }

   public static int determinePrintDirection(AffineTransform transform) {
      short newDir;
      if (transform.getScaleX() == 0.0D && transform.getScaleY() == 0.0D && transform.getShearX() == 1.0D && transform.getShearY() == -1.0D) {
         newDir = 90;
      } else if (transform.getScaleX() == -1.0D && transform.getScaleY() == -1.0D && transform.getShearX() == 0.0D && transform.getShearY() == 0.0D) {
         newDir = 180;
      } else if (transform.getScaleX() == 0.0D && transform.getScaleY() == 0.0D && transform.getShearX() == -1.0D && transform.getShearY() == 1.0D) {
         newDir = 270;
      } else {
         newDir = 0;
      }

      return newDir;
   }

   public static Point2D transformedPoint(int x, int y, AffineTransform transform, PCLPageDefinition pageDefinition, int printDirection) {
      if (log.isTraceEnabled()) {
         log.trace("Current transform: " + transform);
      }

      Float orgPoint = new Float((float)x, (float)y);
      Float transPoint = new Float();
      transform.transform(orgPoint, transPoint);
      Dimension pageSize = pageDefinition.getPhysicalPageSize();
      Rectangle logRect = pageDefinition.getLogicalPageRect();
      switch(printDirection) {
      case 0:
         transPoint.x -= (float)logRect.x;
         transPoint.y -= (float)logRect.y;
         break;
      case 90:
         float ty = transPoint.x;
         transPoint.x = (float)pageSize.height - transPoint.y;
         transPoint.y = ty;
         transPoint.x -= (float)logRect.y;
         transPoint.y -= (float)logRect.x;
         break;
      case 180:
         transPoint.x = (float)pageSize.width - transPoint.x;
         transPoint.y = (float)pageSize.height - transPoint.y;
         transPoint.x -= (float)(pageSize.width - logRect.x - logRect.width);
         transPoint.y -= (float)(pageSize.height - logRect.y - logRect.height);
         transPoint.y = (float)((double)transPoint.y - UnitConv.in2mpt(0.5D));
         break;
      case 270:
         float tx = transPoint.y;
         transPoint.y = (float)pageSize.width - transPoint.x;
         transPoint.x = tx;
         transPoint.x -= (float)(pageSize.height - logRect.y - logRect.height);
         transPoint.y -= (float)(pageSize.width - logRect.x - logRect.width);
         break;
      default:
         throw new IllegalStateException("Illegal print direction: " + printDirection);
      }

      return transPoint;
   }

   static {
      log = LogFactory.getLog(PCLRenderingUtil.class);
   }
}

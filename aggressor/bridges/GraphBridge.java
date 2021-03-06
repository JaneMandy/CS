package aggressor.bridges;

import aggressor.TabManager;
import cortana.Cortana;
import graph.NetworkGraph;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import javax.imageio.ImageIO;
import sleep.bridges.BridgeUtilities;
import sleep.interfaces.Function;
import sleep.interfaces.Loadable;
import sleep.runtime.Scalar;
import sleep.runtime.ScriptInstance;
import sleep.runtime.SleepUtils;

public class GraphBridge implements Function, Loadable {
   protected TabManager manager;
   protected Cortana engine;
   protected static Map imageCache = new HashMap();

   public GraphBridge(Cortana var1, TabManager var2) {
      this.engine = var1;
      this.manager = var2;
   }

   public void scriptLoaded(ScriptInstance var1) {
      Cortana.put(var1, "&graph", this);
      Cortana.put(var1, "&graph_start", this);
      Cortana.put(var1, "&graph_end", this);
      Cortana.put(var1, "&graph_add", this);
      Cortana.put(var1, "&graph_connect", this);
      Cortana.put(var1, "&image_overlay", this);
      Cortana.put(var1, "&graph_zoom", this);
      Cortana.put(var1, "&graph_zoom_reset", this);
      Cortana.put(var1, "&graph_layout", this);
      Cortana.put(var1, "&graph_show_disconnected", this);
   }

   public void scriptUnloaded(ScriptInstance var1) {
   }

   public Scalar evaluate(String var1, ScriptInstance var2, Stack var3) {
      if (var1.equals("&graph")) {
         return SleepUtils.getScalar((Object)(new NetworkGraph()));
      } else {
         NetworkGraph var4;
         if (var1.equals("&graph_start")) {
            var4 = (NetworkGraph)BridgeUtilities.getObject(var3);
            var4.start();
         } else if (var1.equals("&graph_end")) {
            var4 = (NetworkGraph)BridgeUtilities.getObject(var3);
            var4.deleteNodes();
            var4.end();
         } else {
            String var5;
            String var6;
            String var7;
            String var9;
            if (var1.equals("&graph_add")) {
               var4 = (NetworkGraph)BridgeUtilities.getObject(var3);
               var5 = BridgeUtilities.getString(var3, "");
               var6 = BridgeUtilities.getString(var3, "");
               var7 = BridgeUtilities.getString(var3, "");
               Image var8 = (Image)BridgeUtilities.getObject(var3);
               var9 = BridgeUtilities.getString(var3, "");
               var4.addNode(var5, var6, var7, var8, var9, "");
            } else {
               String var18;
               if (var1.equals("&graph_connect")) {
                  var4 = (NetworkGraph)BridgeUtilities.getObject(var3);
                  var5 = BridgeUtilities.getString(var3, "");
                  var6 = BridgeUtilities.getString(var3, "");
                  var7 = BridgeUtilities.getString(var3, "");
                  var18 = BridgeUtilities.getString(var3, "");
                  var9 = BridgeUtilities.getString(var3, "");
                  String var10 = BridgeUtilities.getString(var3, "");
                  var4.addEdge(var5, var6, var7, var18, var9, var10, 1);
               } else {
                  if (var1.equals("&image_overlay")) {
                     var18 = BridgeUtilities.getString(var3, "");
                     synchronized(imageCache) {
                        if (imageCache.containsKey(var18)) {
                           return SleepUtils.getScalar(imageCache.get(var18));
                        }
                     }

                     BufferedImage var19 = new BufferedImage(1000, 776, 2);
                     Graphics2D var20 = var19.createGraphics();

                     while(!var3.isEmpty()) {
                        try {
                           var7 = BridgeUtilities.getString(var3, "");
                           FileInputStream var22 = new FileInputStream(var7);
                           BufferedImage var23 = ImageIO.read(new FileInputStream(var7));
                           var22.close();
                           var20.drawImage(var23, 0, 0, 1000, 776, (ImageObserver)null);
                        } catch (Exception var16) {
                           var16.printStackTrace();
                        }
                     }

                     var20.dispose();
                     synchronized(imageCache) {
                        imageCache.put(var18, var19);
                     }

                     return SleepUtils.getScalar((Object)var19);
                  }

                  if (var1.equals("&graph_zoom_reset")) {
                     var4 = (NetworkGraph)BridgeUtilities.getObject(var3);
                     var4.resetZoom();
                  } else if (var1.equals("&graph_zoom")) {
                     var4 = (NetworkGraph)BridgeUtilities.getObject(var3);
                     double var16 = BridgeUtilities.getDouble(var3);
                     var4.zoom(var16);
                  } else if (var1.equals("&graph_layout")) {
                     var4 = (NetworkGraph)BridgeUtilities.getObject(var3);
                     var5 = BridgeUtilities.getString(var3, "");
                     var4.setAutoLayout(var5);
                  } else if (var1.equals("&graph_show_disconnected")) {
                     var4 = (NetworkGraph)BridgeUtilities.getObject(var3);
                     boolean var17 = SleepUtils.isTrueScalar(BridgeUtilities.getScalar(var3));
                     var4.setShowDisconnected(var17);
                  }
               }
            }
         }

         return SleepUtils.getEmptyScalar();
      }
   }
}

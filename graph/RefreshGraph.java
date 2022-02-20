package graph;

import common.CommonUtils;
import java.awt.Image;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class RefreshGraph implements Runnable {
   protected List nodes = new LinkedList();
   protected List highlights = new LinkedList();
   protected List routes = new LinkedList();
   protected Refreshable graph = null;

   public RefreshGraph(Refreshable var1) {
      this.graph = var1;
   }

   public void go() {
      CommonUtils.runSafe(this);
   }

   public void addRoute(Route var1) {
      this.routes.add(var1);
   }

   public void addNode(String var1, String var2, String var3, Image var4, String var5) {
      RefreshGraph._B var6 = new RefreshGraph._B();
      var6.E = var1;
      var6.A = var2;
      var6.B = var3;
      var6.D = var4;
      var6.C = var5;
      this.nodes.add(var6);
   }

   public void addHighlight(String var1, String var2) {
      RefreshGraph._A var3 = new RefreshGraph._A();
      var3.B = var1;
      var3.A = var2;
      this.highlights.add(var3);
   }

   public void run() {
      this.graph.start();
      Iterator var1 = this.nodes.iterator();

      while(var1.hasNext()) {
         RefreshGraph._B var2 = (RefreshGraph._B)var1.next();
         this.graph.addNode(var2.E, var2.A, var2.B, var2.D, var2.C);
      }

      this.graph.setRoutes((Route[])((Route[])this.routes.toArray(new Route[0])));
      var1 = this.highlights.iterator();

      while(var1.hasNext()) {
         RefreshGraph._A var3 = (RefreshGraph._A)var1.next();
         this.graph.highlightRoute(var3.B, var3.A);
      }

      this.graph.deleteNodes();
      this.graph.end();
   }

   private static class _B {
      public String E;
      public String A;
      public String B;
      public Image D;
      public String C;

      private _B() {
         this.E = "";
         this.A = "";
         this.B = "";
         this.D = null;
         this.C = "";
      }

      // $FF: synthetic method
      _B(Object var1) {
         this();
      }
   }

   private static class _A {
      public String B;
      public String A;

      private _A() {
         this.B = "";
         this.A = "";
      }

      // $FF: synthetic method
      _A(Object var1) {
         this();
      }
   }
}

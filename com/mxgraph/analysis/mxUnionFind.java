package com.mxgraph.analysis;

import java.util.Hashtable;
import java.util.Map;

public class mxUnionFind {
   protected Map nodes = new Hashtable();

   public mxUnionFind(Object[] var1) {
      for(int var2 = 0; var2 < var1.length; ++var2) {
         this.nodes.put(var1[var2], new mxUnionFind.Node());
      }

   }

   public mxUnionFind.Node getNode(Object var1) {
      return (mxUnionFind.Node)this.nodes.get(var1);
   }

   public mxUnionFind.Node find(mxUnionFind.Node var1) {
      while(var1.getParent().getParent() != var1.getParent()) {
         mxUnionFind.Node var2 = var1.getParent().getParent();
         var1.setParent(var2);
         var1 = var2;
      }

      return var1.getParent();
   }

   public void union(mxUnionFind.Node var1, mxUnionFind.Node var2) {
      mxUnionFind.Node var3 = this.find(var1);
      mxUnionFind.Node var4 = this.find(var2);
      if (var3 != var4) {
         if (var3.getSize() < var4.getSize()) {
            var4.setParent(var3);
            var3.setSize(var3.getSize() + var4.getSize());
         } else {
            var3.setParent(var4);
            var4.setSize(var3.getSize() + var4.getSize());
         }
      }

   }

   public boolean differ(Object var1, Object var2) {
      mxUnionFind.Node var3 = this.find(this.getNode(var1));
      mxUnionFind.Node var4 = this.find(this.getNode(var2));
      return var3 != var4;
   }

   public class Node {
      protected mxUnionFind.Node parent = this;
      protected int size = 1;

      public mxUnionFind.Node getParent() {
         return this.parent;
      }

      public void setParent(mxUnionFind.Node var1) {
         this.parent = var1;
      }

      public int getSize() {
         return this.size;
      }

      public void setSize(int var1) {
         this.size = var1;
      }
   }
}

package sleep.engine.types;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.AbstractSequentialList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import sleep.runtime.SleepUtils;

public class MyLinkedList extends AbstractSequentialList implements Cloneable, Serializable, List {
   private transient int size = 0;
   private transient MyLinkedList.ListEntry header;
   private transient MyLinkedList parentList;

   public int size() {
      return this.size;
   }

   private MyLinkedList(MyLinkedList var1, MyLinkedList.ListEntry var2, MyLinkedList.ListEntry var3, int var4) {
      this.parentList = var1;
      this.modCount = this.parentList.modCount;
      this.header = new MyLinkedList.SublistHeaderEntry(var2, var3);
      this.size = var4;
   }

   public MyLinkedList() {
      this.header = new MyLinkedList.NormalListEntry(SleepUtils.getScalar("[:HEADER:]"), (MyLinkedList.ListEntry)null, (MyLinkedList.ListEntry)null);
      this.header.setNext(this.header);
      this.header.setPrevious(this.header);
   }

   public List subList(int var1, int var2) {
      this.checkSafety();
      MyLinkedList.ListEntry var3 = this.getAt(var1).next();

      MyLinkedList.ListEntry var4;
      for(var4 = this.getAt(var2); var3 instanceof MyLinkedList.ListEntryWrapper; var3 = ((MyLinkedList.ListEntryWrapper)var3).parent) {
      }

      while(var4 instanceof MyLinkedList.ListEntryWrapper) {
         var4 = ((MyLinkedList.ListEntryWrapper)var4).parent;
      }

      return new MyLinkedList(this.parentList == null ? this : this.parentList, var3, var4, var2 - var1);
   }

   public boolean add(Object var1) {
      MyLinkedList.ListEntry var2 = this.header;
      this.header.previous().addAfter(var1);
      return true;
   }

   public void add(int var1, Object var2) {
      MyLinkedList.ListEntry var3 = this.getAt(var1);
      var3.addAfter(var2);
   }

   public Object get(int var1) {
      if (var1 >= this.size) {
         throw new IndexOutOfBoundsException("Index: " + var1 + ", Size: " + this.size);
      } else {
         return this.getAt(var1).next().element();
      }
   }

   public Object remove(int var1) {
      if (var1 >= this.size) {
         throw new IndexOutOfBoundsException("Index: " + var1 + ", Size: " + this.size);
      } else {
         MyLinkedList.ListEntry var2 = this.getAt(var1).next();
         Object var3 = var2.element();
         var2.remove();
         return var3;
      }
   }

   private MyLinkedList.ListEntry getAt(int var1) {
      if (var1 >= 0 && var1 <= this.size) {
         MyLinkedList.ListEntry var2 = this.header;
         if (var1 == this.size) {
            return this.header.previous();
         } else {
            int var3;
            if (var1 < this.size / 2) {
               for(var3 = 0; var3 < var1; ++var3) {
                  var2 = var2.next();
               }
            } else {
               var2 = var2.previous();

               for(var3 = this.size; var3 > var1; --var3) {
                  var2 = var2.previous();
               }
            }

            return var2;
         }
      } else {
         throw new IndexOutOfBoundsException("Index: " + var1 + ", Size: " + this.size);
      }
   }

   public ListIterator listIterator(int var1) {
      return new MyLinkedList.MyListIterator(this.getAt(var1), var1);
   }

   public void checkSafety() {
      if (this.parentList != null && this.modCount != this.parentList.modCount) {
         throw new ConcurrentModificationException("parent @array changed after &sublist creation");
      }
   }

   private synchronized void writeObject(ObjectOutputStream var1) throws IOException {
      var1.defaultWriteObject();
      var1.writeInt(this.size);
      Iterator var2 = this.iterator();

      while(var2.hasNext()) {
         var1.writeObject(var2.next());
      }

   }

   private synchronized void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      var1.defaultReadObject();
      int var2 = var1.readInt();
      this.header = new MyLinkedList.NormalListEntry(SleepUtils.getScalar("[:HEADER:]"), (MyLinkedList.ListEntry)null, (MyLinkedList.ListEntry)null);
      this.header.setNext(this.header);
      this.header.setPrevious(this.header);

      for(int var3 = 0; var3 < var2; ++var3) {
         this.add(var1.readObject());
      }

   }

   private class NormalListEntry implements MyLinkedList.ListEntry {
      public Object element;
      public MyLinkedList.ListEntry previous;
      public MyLinkedList.ListEntry next;

      public NormalListEntry(Object var2, MyLinkedList.ListEntry var3, MyLinkedList.ListEntry var4) {
         this.element = var2;
         this.previous = var3;
         this.next = var4;
         if (this.previous != null) {
            this.previous.setNext(this);
         }

         if (this.next != null) {
            this.next.setPrevious(this);
         }

      }

      public void setNext(MyLinkedList.ListEntry var1) {
         this.next = var1;
      }

      public void setPrevious(MyLinkedList.ListEntry var1) {
         this.previous = var1;
      }

      public MyLinkedList.ListEntry next() {
         return this.next;
      }

      public MyLinkedList.ListEntry previous() {
         return this.previous;
      }

      public MyLinkedList.ListEntry remove() {
         MyLinkedList.ListEntry var1 = this.previous();
         MyLinkedList.ListEntry var2 = this.next();
         var2.setPrevious(var1);
         var1.setNext(var2);
         MyLinkedList.this.size--;
         MyLinkedList.this.modCount++;
         return var2;
      }

      public void setElement(Object var1) {
         this.element = var1;
      }

      public Object element() {
         return this.element;
      }

      public MyLinkedList.ListEntry addBefore(Object var1) {
         MyLinkedList.NormalListEntry var2 = MyLinkedList.this.new NormalListEntry(var1, this.previous, this);
         MyLinkedList.this.size++;
         MyLinkedList.this.modCount++;
         return var2;
      }

      public MyLinkedList.ListEntry addAfter(Object var1) {
         MyLinkedList.NormalListEntry var2 = MyLinkedList.this.new NormalListEntry(var1, this, this.next);
         MyLinkedList.this.size++;
         MyLinkedList.this.modCount++;
         return var2;
      }

      public String toString() {
         StringBuffer var1 = new StringBuffer(":[" + this.element() + "]:");
         if (this == MyLinkedList.this.header) {
            var1 = new StringBuffer(":[HEADER]:");
         }

         MyLinkedList.ListEntry var2;
         for(var2 = this.previous(); var2 != MyLinkedList.this.header; var2 = var2.previous()) {
            var1.insert(0, "[" + var2.element() + "]-> ");
         }

         for(var2 = this.next(); var2 != MyLinkedList.this.header; var2 = var2.next()) {
            var1.append(" ->[" + var2.element() + "]");
         }

         return var1.toString();
      }
   }

   private class ListEntryWrapper implements MyLinkedList.ListEntry {
      public MyLinkedList.ListEntry parent;

      public ListEntryWrapper(MyLinkedList.ListEntry var2) {
         this.parent = var2;
      }

      public MyLinkedList.ListEntry remove() {
         MyLinkedList.this.checkSafety();
         MyLinkedList.ListEntry var1 = this.parent.remove();
         MyLinkedList.this.size--;
         MyLinkedList.this.modCount++;
         if (MyLinkedList.this.size == 0) {
            return MyLinkedList.this.header;
         } else {
            if (this.parent == MyLinkedList.this.header.next()) {
               MyLinkedList.this.header.setNext(var1);
            }

            if (this.parent == MyLinkedList.this.header.previous()) {
               MyLinkedList.this.header.setPrevious(var1);
            }

            return MyLinkedList.this.new ListEntryWrapper(var1);
         }
      }

      public MyLinkedList.ListEntry addBefore(Object var1) {
         MyLinkedList.this.checkSafety();
         MyLinkedList.ListEntry var2 = this.parent.addBefore(var1);
         MyLinkedList.this.size++;
         MyLinkedList.this.modCount++;
         if (MyLinkedList.this.size == 1) {
            MyLinkedList.this.header.setNext(var2);
            MyLinkedList.this.header.setPrevious(var2);
         } else if (this.parent == MyLinkedList.this.header.next()) {
            MyLinkedList.this.header.setPrevious(var2);
         }

         return MyLinkedList.this.new ListEntryWrapper(var2);
      }

      public MyLinkedList.ListEntry addAfter(Object var1) {
         MyLinkedList.this.checkSafety();
         MyLinkedList.ListEntry var2 = this.parent.addAfter(var1);
         MyLinkedList.this.size++;
         MyLinkedList.this.modCount++;
         if (MyLinkedList.this.size == 1) {
            MyLinkedList.this.header.setNext(var2);
            MyLinkedList.this.header.setPrevious(var2);
         } else if (this.parent == MyLinkedList.this.header.previous()) {
            MyLinkedList.this.header.setNext(var2);
         }

         return MyLinkedList.this.new ListEntryWrapper(var2);
      }

      public void setNext(MyLinkedList.ListEntry var1) {
         throw new UnsupportedOperationException("ListEntryWrapper::setNext");
      }

      public void setPrevious(MyLinkedList.ListEntry var1) {
         throw new UnsupportedOperationException("ListEntryWrapper::setPrevious");
      }

      public Object element() {
         return this.parent.element();
      }

      public void setElement(Object var1) {
         this.parent.setElement(var1);
      }

      public MyLinkedList.ListEntry next() {
         MyLinkedList.this.checkSafety();
         if (this.parent == MyLinkedList.this.header.next()) {
            return MyLinkedList.this.new ListEntryWrapper(MyLinkedList.this.header);
         } else {
            MyLinkedList.ListEntryWrapper var1 = MyLinkedList.this.new ListEntryWrapper(this.parent.next());
            return var1;
         }
      }

      public MyLinkedList.ListEntry previous() {
         MyLinkedList.this.checkSafety();
         if (this.parent == MyLinkedList.this.header.previous()) {
            return MyLinkedList.this.new ListEntryWrapper(MyLinkedList.this.header);
         } else {
            MyLinkedList.ListEntryWrapper var1 = MyLinkedList.this.new ListEntryWrapper(this.parent.previous());
            return var1;
         }
      }
   }

   private class SublistHeaderEntry implements MyLinkedList.ListEntry {
      private MyLinkedList.ListEntry anchorLeft;
      private MyLinkedList.ListEntry anchorRight;

      public SublistHeaderEntry(MyLinkedList.ListEntry var2, MyLinkedList.ListEntry var3) {
         this.anchorLeft = var2.previous();
         this.anchorRight = var3.next();
      }

      public MyLinkedList.ListEntry remove() {
         throw new UnsupportedOperationException("remove");
      }

      public MyLinkedList.ListEntry previous() {
         return MyLinkedList.this.new ListEntryWrapper(this.anchorRight.previous());
      }

      public MyLinkedList.ListEntry next() {
         return MyLinkedList.this.new ListEntryWrapper(this.anchorLeft.next());
      }

      public void setNext(MyLinkedList.ListEntry var1) {
         this.anchorRight.setPrevious(var1);
         var1.setNext(this.anchorRight);
      }

      public void setPrevious(MyLinkedList.ListEntry var1) {
         this.anchorLeft.setNext(var1);
         var1.setPrevious(this.anchorLeft);
      }

      public MyLinkedList.ListEntry addBefore(Object var1) {
         return this.previous().addAfter(var1);
      }

      public MyLinkedList.ListEntry addAfter(Object var1) {
         return this.next().addBefore(var1);
      }

      public Object element() {
         return SleepUtils.getScalar("[:header:]");
      }

      public void setElement(Object var1) {
         throw new UnsupportedOperationException("setElement");
      }
   }

   private interface ListEntry extends Serializable {
      MyLinkedList.ListEntry remove();

      MyLinkedList.ListEntry addBefore(Object var1);

      MyLinkedList.ListEntry addAfter(Object var1);

      MyLinkedList.ListEntry next();

      MyLinkedList.ListEntry previous();

      void setNext(MyLinkedList.ListEntry var1);

      void setPrevious(MyLinkedList.ListEntry var1);

      Object element();

      void setElement(Object var1);
   }

   private class MyListIterator implements ListIterator, Serializable {
      protected int index;
      protected int start;
      protected MyLinkedList.ListEntry current;
      protected int modCountCheck;

      public void checkSafety() {
         if (this.modCountCheck != MyLinkedList.this.modCount) {
            throw new ConcurrentModificationException("@array changed during iteration");
         }
      }

      public MyListIterator(MyLinkedList.ListEntry var2, int var3) {
         this.modCountCheck = MyLinkedList.this.modCount;
         this.index = var3;
         this.start = var3;
         this.current = var2;
      }

      public void add(Object var1) {
         this.checkSafety();
         this.current = this.current.addAfter(var1);
         ++this.index;
         ++this.modCountCheck;
      }

      public boolean hasNext() {
         return this.index != MyLinkedList.this.size;
      }

      public boolean hasPrevious() {
         return this.index != 0;
      }

      public Object next() {
         this.checkSafety();
         this.current = this.current.next();
         ++this.index;
         return this.current.element();
      }

      public Object previous() {
         this.checkSafety();
         this.current = this.current.previous();
         --this.index;
         return this.current.element();
      }

      public int nextIndex() {
         return this.index;
      }

      public int previousIndex() {
         return this.index - 1;
      }

      public void remove() {
         if (this.current == MyLinkedList.this.header) {
            throw new IllegalStateException("list is empty");
         } else {
            this.checkSafety();
            this.current = this.current.remove().previous();
            --this.index;
            ++this.modCountCheck;
         }
      }

      public void set(Object var1) {
         if (this.current == MyLinkedList.this.header) {
            throw new IllegalStateException("list is empty");
         } else {
            this.checkSafety();
            this.current.setElement(var1);
         }
      }
   }
}

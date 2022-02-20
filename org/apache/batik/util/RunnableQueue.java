package org.apache.batik.util;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RunnableQueue implements Runnable {
   public static final RunnableQueue.RunnableQueueState RUNNING = new RunnableQueue.RunnableQueueState("Running");
   public static final RunnableQueue.RunnableQueueState SUSPENDING = new RunnableQueue.RunnableQueueState("Suspending");
   public static final RunnableQueue.RunnableQueueState SUSPENDED = new RunnableQueue.RunnableQueueState("Suspended");
   protected volatile RunnableQueue.RunnableQueueState state;
   protected final Object stateLock = new Object();
   protected boolean wasResumed;
   private final DoublyLinkedList list = new DoublyLinkedList();
   protected int preemptCount;
   protected RunnableQueue.RunHandler runHandler;
   protected volatile HaltingThread runnableQueueThread;
   private RunnableQueue.IdleRunnable idleRunnable;
   private long idleRunnableWaitTime;
   private static volatile int threadCount;

   public static RunnableQueue createRunnableQueue() {
      RunnableQueue var0 = new RunnableQueue();
      synchronized(var0) {
         HaltingThread var2 = new HaltingThread(var0, "RunnableQueue-" + threadCount++);
         var2.setDaemon(true);
         var2.start();

         while(var0.getThread() == null) {
            try {
               var0.wait();
            } catch (InterruptedException var5) {
            }
         }

         return var0;
      }
   }

   public void run() {
      synchronized(this) {
         this.runnableQueueThread = (HaltingThread)Thread.currentThread();
         this.notify();
      }

      try {
         while(!HaltingThread.hasBeenHalted()) {
            boolean var3 = false;
            boolean var4 = false;
            synchronized(this.stateLock) {
               if (this.state != RUNNING) {
                  this.state = SUSPENDED;
                  var3 = true;
               }
            }

            if (var3) {
               this.executionSuspended();
            }

            synchronized(this.stateLock) {
               while(this.state != RUNNING) {
                  this.state = SUSPENDED;
                  this.stateLock.notifyAll();

                  try {
                     this.stateLock.wait();
                  } catch (InterruptedException var27) {
                  }
               }

               if (this.wasResumed) {
                  this.wasResumed = false;
                  var4 = true;
               }
            }

            if (var4) {
               this.executionResumed();
            }

            RunnableQueue.Link var1;
            Object var2;
            synchronized(this.list) {
               if (this.state == SUSPENDING) {
                  continue;
               }

               var1 = (RunnableQueue.Link)this.list.pop();
               if (this.preemptCount != 0) {
                  --this.preemptCount;
               }

               if (var1 == null) {
                  if (this.idleRunnable == null || (this.idleRunnableWaitTime = this.idleRunnable.getWaitTime()) >= System.currentTimeMillis()) {
                     try {
                        if (this.idleRunnable != null && this.idleRunnableWaitTime != Long.MAX_VALUE) {
                           long var6 = this.idleRunnableWaitTime - System.currentTimeMillis();
                           if (var6 > 0L) {
                              this.list.wait(var6);
                           }
                        } else {
                           this.list.wait();
                        }
                     } catch (InterruptedException var29) {
                     }
                     continue;
                  }

                  var2 = this.idleRunnable;
               } else {
                  var2 = var1.runnable;
               }
            }

            this.runnableStart((Runnable)var2);

            try {
               ((Runnable)var2).run();
            } catch (ThreadDeath var25) {
               throw var25;
            } catch (Throwable var26) {
               var26.printStackTrace();
            }

            if (var1 != null) {
               var1.unlock();
            }

            this.runnableInvoked((Runnable)var2);
         }
      } finally {
         synchronized(this) {
            this.runnableQueueThread = null;
         }
      }

   }

   public HaltingThread getThread() {
      return this.runnableQueueThread;
   }

   public void invokeLater(Runnable var1) {
      if (this.runnableQueueThread == null) {
         throw new IllegalStateException("RunnableQueue not started or has exited");
      } else {
         synchronized(this.list) {
            this.list.push(new RunnableQueue.Link(var1));
            this.list.notify();
         }
      }
   }

   public void invokeAndWait(Runnable var1) throws InterruptedException {
      if (this.runnableQueueThread == null) {
         throw new IllegalStateException("RunnableQueue not started or has exited");
      } else if (this.runnableQueueThread == Thread.currentThread()) {
         throw new IllegalStateException("Cannot be called from the RunnableQueue thread");
      } else {
         RunnableQueue.LockableLink var2 = new RunnableQueue.LockableLink(var1);
         synchronized(this.list) {
            this.list.push(var2);
            this.list.notify();
         }

         var2.lock();
      }
   }

   public void preemptLater(Runnable var1) {
      if (this.runnableQueueThread == null) {
         throw new IllegalStateException("RunnableQueue not started or has exited");
      } else {
         synchronized(this.list) {
            this.list.add(this.preemptCount, new RunnableQueue.Link(var1));
            ++this.preemptCount;
            this.list.notify();
         }
      }
   }

   public void preemptAndWait(Runnable var1) throws InterruptedException {
      if (this.runnableQueueThread == null) {
         throw new IllegalStateException("RunnableQueue not started or has exited");
      } else if (this.runnableQueueThread == Thread.currentThread()) {
         throw new IllegalStateException("Cannot be called from the RunnableQueue thread");
      } else {
         RunnableQueue.LockableLink var2 = new RunnableQueue.LockableLink(var1);
         synchronized(this.list) {
            this.list.add(this.preemptCount, var2);
            ++this.preemptCount;
            this.list.notify();
         }

         var2.lock();
      }
   }

   public RunnableQueue.RunnableQueueState getQueueState() {
      synchronized(this.stateLock) {
         return this.state;
      }
   }

   public void suspendExecution(boolean var1) {
      if (this.runnableQueueThread == null) {
         throw new IllegalStateException("RunnableQueue not started or has exited");
      } else {
         synchronized(this.stateLock) {
            this.wasResumed = false;
            if (this.state == SUSPENDED) {
               this.stateLock.notifyAll();
            } else {
               if (this.state == RUNNING) {
                  this.state = SUSPENDING;
                  synchronized(this.list) {
                     this.list.notify();
                  }
               }

               if (var1) {
                  while(this.state == SUSPENDING) {
                     try {
                        this.stateLock.wait();
                     } catch (InterruptedException var6) {
                     }
                  }
               }

            }
         }
      }
   }

   public void resumeExecution() {
      if (this.runnableQueueThread == null) {
         throw new IllegalStateException("RunnableQueue not started or has exited");
      } else {
         synchronized(this.stateLock) {
            this.wasResumed = true;
            if (this.state != RUNNING) {
               this.state = RUNNING;
               this.stateLock.notifyAll();
            }

         }
      }
   }

   public Object getIteratorLock() {
      return this.list;
   }

   public Iterator iterator() {
      return new Iterator() {
         RunnableQueue.Link head;
         RunnableQueue.Link link;

         {
            this.head = (RunnableQueue.Link)RunnableQueue.this.list.getHead();
         }

         public boolean hasNext() {
            if (this.head == null) {
               return false;
            } else if (this.link == null) {
               return true;
            } else {
               return this.link != this.head;
            }
         }

         public Object next() {
            if (this.head != null && this.head != this.link) {
               if (this.link == null) {
                  this.link = (RunnableQueue.Link)this.head.getNext();
                  return this.head.runnable;
               } else {
                  Runnable var1 = this.link.runnable;
                  this.link = (RunnableQueue.Link)this.link.getNext();
                  return var1;
               }
            } else {
               throw new NoSuchElementException();
            }
         }

         public void remove() {
            throw new UnsupportedOperationException();
         }
      };
   }

   public synchronized void setRunHandler(RunnableQueue.RunHandler var1) {
      this.runHandler = var1;
   }

   public synchronized RunnableQueue.RunHandler getRunHandler() {
      return this.runHandler;
   }

   public void setIdleRunnable(RunnableQueue.IdleRunnable var1) {
      synchronized(this.list) {
         this.idleRunnable = var1;
         this.idleRunnableWaitTime = 0L;
         this.list.notify();
      }
   }

   protected synchronized void executionSuspended() {
      if (this.runHandler != null) {
         this.runHandler.executionSuspended(this);
      }

   }

   protected synchronized void executionResumed() {
      if (this.runHandler != null) {
         this.runHandler.executionResumed(this);
      }

   }

   protected synchronized void runnableStart(Runnable var1) {
      if (this.runHandler != null) {
         this.runHandler.runnableStart(this, var1);
      }

   }

   protected synchronized void runnableInvoked(Runnable var1) {
      if (this.runHandler != null) {
         this.runHandler.runnableInvoked(this, var1);
      }

   }

   protected static class LockableLink extends RunnableQueue.Link {
      private volatile boolean locked;

      public LockableLink(Runnable var1) {
         super(var1);
      }

      public boolean isLocked() {
         return this.locked;
      }

      public synchronized void lock() throws InterruptedException {
         this.locked = true;
         this.notify();
         this.wait();
      }

      public synchronized void unlock() {
         while(!this.locked) {
            try {
               this.wait();
            } catch (InterruptedException var2) {
            }
         }

         this.locked = false;
         this.notify();
      }
   }

   protected static class Link extends DoublyLinkedList.Node {
      private final Runnable runnable;

      public Link(Runnable var1) {
         this.runnable = var1;
      }

      public void unlock() {
      }
   }

   public static class RunHandlerAdapter implements RunnableQueue.RunHandler {
      public void runnableStart(RunnableQueue var1, Runnable var2) {
      }

      public void runnableInvoked(RunnableQueue var1, Runnable var2) {
      }

      public void executionSuspended(RunnableQueue var1) {
      }

      public void executionResumed(RunnableQueue var1) {
      }
   }

   public interface RunHandler {
      void runnableStart(RunnableQueue var1, Runnable var2);

      void runnableInvoked(RunnableQueue var1, Runnable var2);

      void executionSuspended(RunnableQueue var1);

      void executionResumed(RunnableQueue var1);
   }

   public interface IdleRunnable extends Runnable {
      long getWaitTime();
   }

   public static final class RunnableQueueState {
      private final String value;

      private RunnableQueueState(String var1) {
         this.value = var1;
      }

      public String getValue() {
         return this.value;
      }

      public String toString() {
         return "[RunnableQueueState: " + this.value + ']';
      }

      // $FF: synthetic method
      RunnableQueueState(String var1, Object var2) {
         this(var1);
      }
   }
}

package org.apache.xml.utils;

public class ThreadControllerWrapper {
   private static ThreadControllerWrapper.ThreadController m_tpool = new ThreadControllerWrapper.ThreadController();

   public static Thread runThread(Runnable runnable, int priority) {
      return m_tpool.run(runnable, priority);
   }

   public static void waitThread(Thread worker, Runnable task) throws InterruptedException {
      m_tpool.waitThread(worker, task);
   }

   public static class ThreadController {
      public Thread run(Runnable task, int priority) {
         Thread t = new Thread(task);
         t.start();
         return t;
      }

      public void waitThread(Thread worker, Runnable task) throws InterruptedException {
         worker.join();
      }
   }
}

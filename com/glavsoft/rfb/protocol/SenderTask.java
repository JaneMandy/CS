package com.glavsoft.rfb.protocol;

import com.glavsoft.exceptions.TransportException;
import com.glavsoft.rfb.client.ClientToServerMessage;
import com.glavsoft.transport.Writer;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Logger;

public class SenderTask implements Runnable {
   private final MessageQueue queue;
   private final Writer writer;
   private final ProtocolContext protocolContext;
   private volatile boolean isRunning = false;

   public SenderTask(MessageQueue messageQueue, Writer writer, ProtocolContext protocolContext) {
      this.queue = messageQueue;
      this.writer = writer;
      this.protocolContext = protocolContext;
   }

   public void run() {
      this.isRunning = true;

      while(this.isRunning) {
         try {
            ClientToServerMessage message = this.queue.get();
            if (message != null) {
               message.send(this.writer);
            }
         } catch (InterruptedException var4) {
         } catch (TransportException var5) {
            Logger.getLogger("com.glavsoft.rfb.protocol").severe("Close session: " + var5.getMessage());
            if (this.isRunning) {
               this.protocolContext.cleanUpSession("Connection closed");
            }

            this.stopTask();
         } catch (Throwable var6) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            var6.printStackTrace(pw);
            if (this.isRunning) {
               ProtocolContext var10000 = this.protocolContext;
               String var10001 = var6.getMessage();
               var10000.cleanUpSession(var10001 + "\n" + sw.toString());
            }

            this.stopTask();
         }
      }

   }

   public void stopTask() {
      this.isRunning = false;
   }
}

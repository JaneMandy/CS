package aggressor.browsers;

import aggressor.Aggressor;
import common.Callback;
import common.CommonUtils;
import common.TeamQueue;
import common.TeamSocket;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JLabel;
import ssl.ArmitageTrustListener;
import ssl.SecureSocket;

public class TestConnection implements Runnable, Callback, ArmitageTrustListener {
   private volatile boolean ă = true;
   private final String Ĉ;
   private final String Ă;
   private final String Ć;
   private final String Ą;
   private final JLabel ą;
   private final JButton ā;
   private final int ÿ = 10;
   private int ć = 10;
   private TeamQueue Ā = null;

   public TestConnection(Map var1, JLabel var2, JButton var3) {
      this.Ĉ = (String)var1.get("host");
      this.Ă = (String)var1.get("port");
      this.Ć = (String)var1.get("pass");
      this.Ą = (String)var1.get("user");
      this.ą = var2;
      this.ā = var3;
   }

   public void run() {
      while(this.ă) {
         try {
            this.ą.setText("Retrying");
            this.ą.getParent().validate();
            SecureSocket var1 = new SecureSocket(this.Ĉ, Integer.parseInt(this.Ă), this);
            this.ą.setText("Reconnecting");
            this.ą.getParent().validate();
            var1.authenticate(this.Ć);
            this.Ā = new TeamQueue(new TeamSocket(var1.getSocket()));
            this.Ā.call("aggressor.authenticate", CommonUtils.args(this.Ą, this.Ć, Aggressor.VERSION), this);
            return;
         } catch (Exception var4) {
            for(; this.ć > 0; --this.ć) {
               try {
                  this.ą.setText("Server unavailable, retry in " + this.ć + " seconds");
                  this.ą.getParent().validate();
                  Thread.sleep(1000L);
               } catch (InterruptedException var3) {
               }
            }

            this.ć = 10;
         }
      }

   }

   public void stop() {
      this.ă = false;
   }

   public boolean trust(String var1) {
      return true;
   }

   public void result(String var1, Object var2) {
      this.Ā.close();
      this.stop();
      CommonUtils.runSafe(new Runnable() {
         public void run() {
            TestConnection.this.ā.doClick();
         }
      });
   }
}

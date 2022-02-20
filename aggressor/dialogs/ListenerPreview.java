package aggressor.dialogs;

import aggressor.AggressorClient;
import aggressor.DataUtils;
import c2profile.Preview;
import c2profile.Profile;
import common.AObject;
import common.CommonUtils;
import common.ScListener;
import console.Display;
import dialog.DialogUtils;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;

public class ListenerPreview extends AObject implements ActionListener {
   protected AggressorClient client = null;
   protected ScListener listener = null;
   protected String name = null;
   protected JFrame dialog = null;

   public ListenerPreview(AggressorClient var1, String var2) {
      this.client = var1;
      this.name = var2;
      this.listener = new ScListener(var1, DataUtils.getListenerByName(var1.getData(), var2));
   }

   public void actionPerformed(ActionEvent var1) {
      this.dialog.setVisible(false);
      this.dialog.dispose();
   }

   public JComponent buildRaw(String var1) {
      Display var2 = new Display(new Properties());
      var2.setFont(Font.decode("Monospaced BOLD 14"));
      var2.setForeground(Color.decode("#ffffff"));
      var2.setBackground(Color.decode("#000000"));
      var2.append(var1);
      var2.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
      return var2;
   }

   public void kv(StringBuffer var1, String var2, String var3) {
      var1.append(CommonUtils.pad((String)(var2 + '\u0003' + "E:" + '\u000f'), 28));
      var1.append(var3);
      var1.append("\n");
   }

   public JComponent settings() {
      Profile var1 = this.listener.getProfile();
      StringBuffer var2 = new StringBuffer();
      this.kv(var2, "Jitter", var1.getString(".jitter") + "%");
      this.kv(var2, "Sleep Time", var1.getString(".sleeptime"));
      this.kv(var2, "Spawn To (x86)", var1.getString(".post-ex.spawnto_x86"));
      this.kv(var2, "Spawn To (x64)", var1.getString(".post-ex.spawnto_x64"));
      return this.buildRaw(var2.toString());
   }

   public JComponent stager() {
      Preview var1 = new Preview(this.listener.getProfile());
      StringBuffer var2 = new StringBuffer();
      var2.append('\u0003');
      var2.append("4");
      var2.append(CommonUtils.strrep(var1.getClientSample(".http-stager"), "\n", "\n\u00034"));
      var2.append('\u0003');
      var2.append("C");
      var2.append(CommonUtils.strrep(var1.getServerSample(".http-stager"), "\n", "\n\u0003C"));
      return this.buildRaw(var2.toString());
   }

   public JComponent traffic() {
      Preview var1 = new Preview(this.listener.getProfile());
      StringBuffer var2 = new StringBuffer();
      var2.append('\u0003');
      var2.append("4");
      var2.append(CommonUtils.strrep(var1.getClientSample(), "\n", "\n\u00034"));
      var2.append('\u0003');
      var2.append("C");
      var2.append(CommonUtils.strrep(var1.getServerSample(), "\n", "\n\u0003C"));
      if (this.listener.getProfile().shouldChunkPosts()) {
         var2.append("\n\nThis profile chunks responses when posting data. Expect screenshots and other large output to take awhile");
      }

      return this.buildRaw(var2.toString());
   }

   public JComponent PE() {
      Preview var1 = new Preview(this.listener.getProfile());
      Map var2 = var1.getPE();
      StringBuffer var3 = new StringBuffer();
      Iterator var4 = var2.entrySet().iterator();

      while(var4.hasNext()) {
         Entry var5 = (Entry)var4.next();
         if (!"Notes".equals(var5.getKey())) {
            var3.append(CommonUtils.pad((String)((String)var5.getKey() + '\u0003' + "E:" + '\u000f'), 28));
            var3.append(var5.getValue());
            var3.append("\n");
         }
      }

      var3.append("\n");
      var3.append(var2.get("Notes"));
      return this.buildRaw(var3.toString());
   }

   public void show() {
      this.dialog = DialogUtils.dialog("Listener: " + this.listener.getName(), 640, 480);
      this.dialog.setLayout(new BorderLayout());
      JTabbedPane var1 = new JTabbedPane();
      var1.addTab("Settings", this.settings());
      var1.addTab("HTTP C2", this.traffic());
      var1.addTab("HTTP Stager", this.stager());
      var1.addTab("PE", this.PE());
      JButton var2 = new JButton("Close");
      var2.addActionListener(this);
      this.dialog.add(var1, "Center");
      this.dialog.add(DialogUtils.center((JComponent)var2), "South");
      this.dialog.setVisible(true);
      this.dialog.show();
   }
}

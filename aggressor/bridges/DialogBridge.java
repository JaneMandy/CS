package aggressor.bridges;

import aggressor.AggressorClient;
import common.CommonUtils;
import cortana.Cortana;
import dialog.DialogListener;
import dialog.DialogManager;
import dialog.DialogUtils;
import java.awt.event.ActionEvent;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.Map.Entry;
import javax.swing.JButton;
import javax.swing.JFrame;
import sleep.bridges.BridgeUtilities;
import sleep.bridges.SleepClosure;
import sleep.interfaces.Function;
import sleep.interfaces.Loadable;
import sleep.runtime.Scalar;
import sleep.runtime.ScriptInstance;
import sleep.runtime.SleepUtils;

public class DialogBridge implements Function, Loadable {
   protected AggressorClient client;

   public DialogBridge(AggressorClient var1) {
      this.client = var1;
   }

   public void scriptLoaded(ScriptInstance var1) {
      Cortana.put(var1, "&dialog", this);
      Cortana.put(var1, "&dialog_show", this);
      Cortana.put(var1, "&dialog_description", this);
      Cortana.put(var1, "&drow_checkbox", this);
      Cortana.put(var1, "&drow_combobox", this);
      Cortana.put(var1, "&drow_file", this);
      Cortana.put(var1, "&drow_text", this);
      Cortana.put(var1, "&drow_text_big", this);
      Cortana.put(var1, "&drow_beacon", this);
      Cortana.put(var1, "&drow_exploits", this);
      Cortana.put(var1, "&drow_interface", this);
      Cortana.put(var1, "&drow_krbtgt", this);
      Cortana.put(var1, "&drow_listener", this);
      Cortana.put(var1, "&drow_listener_stage", this);
      Cortana.put(var1, "&drow_listener_smb", this);
      Cortana.put(var1, "&drow_mailserver", this);
      Cortana.put(var1, "&drow_proxyserver", this);
      Cortana.put(var1, "&drow_site", this);
      Cortana.put(var1, "&dbutton_action", this);
      Cortana.put(var1, "&dbutton_help", this);
   }

   public void scriptUnloaded(ScriptInstance var1) {
   }

   public Scalar evaluate(String var1, ScriptInstance var2, Stack var3) {
      if (var1.equals("&dialog")) {
         String var15 = BridgeUtilities.getString(var3, "");
         Map var16 = SleepUtils.getMapFromHash(BridgeUtilities.getHash(var3));
         SleepClosure var18 = BridgeUtilities.getFunction(var3, var2);
         DialogBridge._A var14 = new DialogBridge._A(var15, 640, 480, var18);
         Iterator var8 = var16.entrySet().iterator();

         while(var8.hasNext()) {
            Entry var9 = (Entry)var8.next();
            String var10 = var9.getKey().toString();
            String var11 = var9.getValue().toString();
            var14.C.set(var10, var11);
         }

         return SleepUtils.getScalar((Object)var14);
      } else {
         DialogBridge._A var4;
         if (var1.equals("&dialog_description")) {
            var4 = (DialogBridge._A)BridgeUtilities.getObject(var3);
            var4.D = BridgeUtilities.getString(var3, "");
         } else if (var1.equals("&dialog_show")) {
            var4 = (DialogBridge._A)BridgeUtilities.getObject(var3);
            var4.A();
         } else {
            String var5;
            String var6;
            if (var1.equals("&drow_checkbox")) {
               var4 = (DialogBridge._A)BridgeUtilities.getObject(var3);
               var5 = BridgeUtilities.getString(var3, "");
               var6 = BridgeUtilities.getString(var3, "");
               String var7 = BridgeUtilities.getString(var3, "");
               var4.C.checkbox_add(var5, var6, var7);
            } else if (var1.equals("&drow_combobox")) {
               var4 = (DialogBridge._A)BridgeUtilities.getObject(var3);
               var5 = BridgeUtilities.getString(var3, "");
               var6 = BridgeUtilities.getString(var3, "");
               String[] var12 = CommonUtils.toArray((Collection)SleepUtils.getListFromArray(BridgeUtilities.getArray(var3)));
               var4.C.combobox(var5, var6, var12);
            } else if (var1.equals("&drow_file")) {
               var4 = (DialogBridge._A)BridgeUtilities.getObject(var3);
               var5 = BridgeUtilities.getString(var3, "");
               var6 = BridgeUtilities.getString(var3, "");
               var4.C.file(var5, var6);
            } else if (var1.equals("&drow_text")) {
               var4 = (DialogBridge._A)BridgeUtilities.getObject(var3);
               var5 = BridgeUtilities.getString(var3, "");
               var6 = BridgeUtilities.getString(var3, "");
               int var13 = BridgeUtilities.getInt(var3, 20);
               var4.C.text(var5, var6, var13);
            } else if (var1.equals("&drow_text_big")) {
               var4 = (DialogBridge._A)BridgeUtilities.getObject(var3);
               var5 = BridgeUtilities.getString(var3, "");
               var6 = BridgeUtilities.getString(var3, "");
               var4.C.text_big(var5, var6);
            } else if (var1.equals("&drow_beacon")) {
               var4 = (DialogBridge._A)BridgeUtilities.getObject(var3);
               var5 = BridgeUtilities.getString(var3, "");
               var6 = BridgeUtilities.getString(var3, "");
               var4.C.beacon(var5, var6, this.client);
            } else if (var1.equals("&drow_exploits")) {
               var4 = (DialogBridge._A)BridgeUtilities.getObject(var3);
               var5 = BridgeUtilities.getString(var3, "");
               var6 = BridgeUtilities.getString(var3, "");
               var4.C.exploits(var5, var6, this.client);
            } else if (var1.equals("&drow_interface")) {
               var4 = (DialogBridge._A)BridgeUtilities.getObject(var3);
               var5 = BridgeUtilities.getString(var3, "");
               var6 = BridgeUtilities.getString(var3, "");
               var4.C.interfaces(var5, var6, this.client.getConnection(), this.client.getData());
            } else if (var1.equals("&drow_krbtgt")) {
               var4 = (DialogBridge._A)BridgeUtilities.getObject(var3);
               var5 = BridgeUtilities.getString(var3, "");
               var6 = BridgeUtilities.getString(var3, "");
               var4.C.krbtgt(var5, var6, this.client);
            } else if (var1.equals("&drow_listener")) {
               var4 = (DialogBridge._A)BridgeUtilities.getObject(var3);
               var5 = BridgeUtilities.getString(var3, "");
               var6 = BridgeUtilities.getString(var3, "");
               var4.C.sc_listener_stagers(var5, var6, this.client);
            } else if (!var1.equals("&drow_listener_stage") && !var1.equals("&drow_listener_smb")) {
               if (var1.equals("&drow_mailserver")) {
                  var4 = (DialogBridge._A)BridgeUtilities.getObject(var3);
                  var5 = BridgeUtilities.getString(var3, "");
                  var6 = BridgeUtilities.getString(var3, "");
                  var4.C.mailserver(var5, var6);
               } else if (var1.equals("&drow_proxyserver")) {
                  var4 = (DialogBridge._A)BridgeUtilities.getObject(var3);
                  var5 = BridgeUtilities.getString(var3, "");
                  var6 = BridgeUtilities.getString(var3, "");
                  var4.C.proxyserver(var5, var6, this.client);
               } else if (var1.equals("&drow_site")) {
                  var4 = (DialogBridge._A)BridgeUtilities.getObject(var3);
                  var5 = BridgeUtilities.getString(var3, "");
                  var6 = BridgeUtilities.getString(var3, "");
                  var4.C.site(var5, var6, this.client.getConnection(), this.client.getData());
               } else {
                  JButton var17;
                  if (var1.equals("&dbutton_action")) {
                     var4 = (DialogBridge._A)BridgeUtilities.getObject(var3);
                     var5 = BridgeUtilities.getString(var3, "");
                     var17 = var4.C.action(var5);
                     var4.E.add(var17);
                  } else if (var1.equals("&dbutton_help")) {
                     var4 = (DialogBridge._A)BridgeUtilities.getObject(var3);
                     var5 = BridgeUtilities.getString(var3, "");
                     var17 = var4.C.help(var5);
                     var4.E.add(var17);
                  }
               }
            } else {
               var4 = (DialogBridge._A)BridgeUtilities.getObject(var3);
               var5 = BridgeUtilities.getString(var3, "");
               var6 = BridgeUtilities.getString(var3, "");
               var4.C.sc_listener_all(var5, var6, this.client);
            }
         }

         return SleepUtils.getEmptyScalar();
      }
   }

   private class _A implements DialogListener {
      protected DialogManager C;
      protected JFrame B;
      protected LinkedList E = new LinkedList();
      protected String D = "";
      protected SleepClosure F;

      public _A(String var2, int var3, int var4, SleepClosure var5) {
         this.B = DialogUtils.dialog(var2, var3, var4);
         this.C = new DialogManager(this.B);
         this.C.addDialogListener(this);
         this.F = var5;
      }

      public void dialogAction(ActionEvent var1, Map var2) {
         Stack var3 = new Stack();
         var3.push(SleepUtils.getHashWrapper(var2));
         var3.push(SleepUtils.getScalar(var1.getActionCommand()));
         var3.push(SleepUtils.getScalar((Object)this));
         SleepUtils.runCode((SleepClosure)this.F, "", (ScriptInstance)null, var3);
      }

      public void A() {
         if (!"".equals(this.D)) {
            this.B.add(DialogUtils.description(this.D), "North");
         }

         this.B.add(this.C.layout(), "Center");
         if (this.E.size() > 0) {
            this.B.add(DialogUtils.center((List)this.E), "South");
         }

         this.B.pack();
         this.B.setVisible(true);
      }
   }
}

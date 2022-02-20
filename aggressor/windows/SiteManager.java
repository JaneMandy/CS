package aggressor.windows;

import aggressor.DataManager;
import aggressor.DataUtils;
import common.AObject;
import common.Callback;
import common.CommonUtils;
import common.TeamQueue;
import cortana.Cortana;
import dialog.DialogUtils;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import ui.ATable;
import ui.GenericTableModel;

public class SiteManager extends AObject implements Callback, ActionListener {
   protected TeamQueue conn = null;
   protected Cortana engine = null;
   protected DataManager data = null;
   protected GenericTableModel model = null;
   protected ATable table = null;
   protected String[] cols = new String[]{"URI", "Host", "Port", "Type", "Description"};

   public SiteManager(DataManager var1, Cortana var2, TeamQueue var3) {
      this.engine = var2;
      this.conn = var3;
      this.data = var1;
      this.model = DialogUtils.setupModel("URI", this.cols, DataUtils.getSites(var1));
      var1.subscribe("sites", this);
   }

   public ActionListener cleanup() {
      return this.data.unsubOnClose("sites", this);
   }

   public void actionPerformed(ActionEvent var1) {
      String var4;
      String var5;
      if ("Kill".equals(var1.getActionCommand())) {
         if (!this.model.hasSelectedRows(this.table)) {
            DialogUtils.showError(DialogUtils.MessageID.ROWS_MUST_BE_SELECTED);
            return;
         }

         Object[][] var2 = this.model.getSelectedValuesFromColumns(this.table, CommonUtils.toArray("URI, Port"));

         for(int var3 = 0; var3 < var2.length; ++var3) {
            var4 = ((Class)var2[var3][0]).makeConcatWithConstants<invokedynamic>(var2[var3][0]);
            var5 = ((Class)var2[var3][1]).makeConcatWithConstants<invokedynamic>(var2[var3][1]);
            this.conn.call("cloudstrike.kill_site", CommonUtils.args(var5, var4));
         }
      } else if ("Copy URL".equals(var1.getActionCommand())) {
         if (!this.model.hasSelectedRows(this.table)) {
            DialogUtils.showError(DialogUtils.MessageID.A_ROW_MUST_BE_SELECTED);
            return;
         }

         String var9 = ((Class)this.model.getSelectedValue(this.table)).makeConcatWithConstants<invokedynamic>(this.model.getSelectedValue(this.table));
         String var10 = ((Class)this.model.getSelectedValueFromColumn(this.table, "Host")).makeConcatWithConstants<invokedynamic>(this.model.getSelectedValueFromColumn(this.table, "Host"));
         var4 = ((Class)this.model.getSelectedValueFromColumn(this.table, "Port")).makeConcatWithConstants<invokedynamic>(this.model.getSelectedValueFromColumn(this.table, "Port"));
         var5 = ((Class)this.model.getSelectedValueFromColumn(this.table, "Proto")).makeConcatWithConstants<invokedynamic>(this.model.getSelectedValueFromColumn(this.table, "Proto"));
         String var6 = var5 + var10 + ":" + var4 + var9;
         String var7 = ((Class)this.model.getSelectedValueFromColumn(this.table, "Description")).makeConcatWithConstants<invokedynamic>(this.model.getSelectedValueFromColumn(this.table, "Description"));
         if ("PowerShell Web Delivery".equals(var7)) {
            DialogUtils.addToClipboard(CommonUtils.PowerShellOneLiner(var6));
         } else if (var7.startsWith("Scripted Web Delivery (") && var7.endsWith(")")) {
            String var8 = CommonUtils.strrep(var7, "Scripted Web Delivery (", "");
            var8 = CommonUtils.strrep(var8, ")", "");
            DialogUtils.addToClipboard(CommonUtils.OneLiner(var6, var8));
         } else {
            DialogUtils.addToClipboard(var6);
         }
      }

   }

   public JComponent getContent() {
      JPanel var1 = new JPanel();
      var1.setLayout(new BorderLayout());
      this.table = DialogUtils.setupTable(this.model, this.cols, true);
      DialogUtils.setTableColumnWidths(this.table, DialogUtils.toMap("URI: 125, Host: 125, Port: 60, Type: 60, Description: 250"));
      JButton var2 = new JButton("Kill");
      JButton var3 = new JButton("Copy URL");
      JButton var4 = new JButton("Help");
      var2.addActionListener(this);
      var3.addActionListener(this);
      var4.addActionListener(DialogUtils.gotoURL("https://www.cobaltstrike.com/help-manage-sites"));
      var1.add(DialogUtils.FilterAndScroll(this.table), "Center");
      var1.add(DialogUtils.center(var3, var2, var4), "South");
      return var1;
   }

   public void result(String var1, Object var2) {
      DialogUtils.setTable(this.table, this.model, (LinkedList)var2);
   }
}

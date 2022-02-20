package ui;

import aggressor.Prefs;
import common.CommonUtils;
import console.Activity;
import dialog.DialogUtils;
import java.awt.BorderLayout;
import java.awt.Color;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JSplitPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class DataBrowser extends JComponent implements ListSelectionListener, Activity {
   protected JSplitPane split = new JSplitPane(1);
   protected GenericTableModel model = null;
   protected ATable table = null;
   protected LinkedList listeners = new LinkedList();
   protected String col;
   protected JLabel label;
   protected Color original;

   public void registerLabel(JLabel var1) {
      this.original = var1.getForeground();
      this.label = var1;
   }

   public boolean requestFocusInWindow() {
      return this.table.requestFocusInWindow();
   }

   public void resetNotification() {
      this.label.setForeground(this.original);
      this.table.markSelections();
      this.table.fixSelection();
   }

   public static DataBrowser getBeaconDataBrowser(String var0, JComponent var1, LinkedList var2) {
      return new DataBrowser(var0, CommonUtils.toArray("user, computer, pid, when"), (Map)null, var1, var2);
   }

   public static DataBrowser getScreenshotDataBrowser(String var0, JComponent var1, LinkedList var2) {
      Map var3 = DialogUtils.toMap("user: 75, computer: 75, session: 75, when: 125, title: 50");
      return new DataBrowser(var0, CommonUtils.toArray("user, computer, session, when, title"), var3, var1, var2);
   }

   public static DataBrowser getKeystrokeDataBrowser(String var0, JComponent var1, LinkedList var2) {
      Map var3 = DialogUtils.toMap("user: 75, computer: 75, session: 75, when: 125, title: 50");
      return new DataBrowser(var0, CommonUtils.toArray("user, computer, session, when, title"), var3, var1, var2);
   }

   public DataBrowser(String var1, String[] var2, Map var3, JComponent var4, LinkedList var5) {
      this.setLayout(new BorderLayout());
      this.add(this.split, "Center");
      this.model = DialogUtils.setupModel(var1, var2, var5);
      this.table = DialogUtils.setupTable(this.model, var2, false);
      if (var3 != null) {
         DialogUtils.setTableColumnWidths(this.table, var3);
      }

      this.table.getSelectionModel().addListSelectionListener(this);
      this.split.add(DialogUtils.FilterAndScroll(this.table));
      this.split.add(var4);
   }

   public void valueChanged(ListSelectionEvent var1) {
      if (!var1.getValueIsAdjusting()) {
         Iterator var2 = this.listeners.iterator();

         while(var2.hasNext()) {
            DataSelectionListener var3 = (DataSelectionListener)var2.next();
            var3.selected(this.getSelectedValue());
         }
      }

   }

   public void addDataSelectionListener(DataSelectionListener var1) {
      this.listeners.add(var1);
   }

   public Object getSelectedValue() {
      return this.model.getSelectedValue(this.table);
   }

   public Map getSelectedRow() {
      Map[] var1 = this.getModel().getSelectedRows(this.getTable());
      return var1.length == 0 ? null : var1[0];
   }

   public GenericTableModel getModel() {
      return this.model;
   }

   public ATable getTable() {
      return this.table;
   }

   public void addEntry(final Map var1) {
      CommonUtils.runSafe(new Runnable() {
         public void run() {
            DataBrowser.this.table.markSelections();
            DataBrowser.this.model.addEntry(var1);
            DataBrowser.this.model.fireListeners();
            DataBrowser.this.table.restoreSelections();
            if (!DataBrowser.this.isShowing()) {
               DataBrowser.this.label.setForeground(Prefs.getPreferences().getColor("tab.highlight.color", "#0000ff"));
            }

         }
      });
   }

   public void setTable(final Collection var1) {
      CommonUtils.runSafe(new Runnable() {
         public void run() {
            DialogUtils.setTable(DataBrowser.this.table, DataBrowser.this.model, var1);
            if (!DataBrowser.this.isShowing()) {
               DataBrowser.this.label.setForeground(Prefs.getPreferences().getColor("tab.highlight.color", "#0000ff"));
            }

         }
      });
   }
}

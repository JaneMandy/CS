package importers;

import common.CommonUtils;
import common.MudgeSanity;
import common.OperatingSystem;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class NmapXML extends Importer {
   public NmapXML(ImportHandler var1) {
      super(var1);
   }

   public boolean isNmapXML(File var1) {
      String var2 = CommonUtils.peekFile(var1, 1024);
      return var2.startsWith("<?xml") && var2.indexOf("<nmaprun") > 0;
   }

   public boolean parse(File var1) throws Exception {
      if (!this.isNmapXML(var1)) {
         return false;
      } else {
         try {
            SAXParserFactory var2 = SAXParserFactory.newInstance();
            SAXParser var3 = var2.newSAXParser();
            var3.parse((InputStream)(new FileInputStream(var1)), (DefaultHandler)(new NmapXML._A()));
            return true;
         } catch (Exception var4) {
            MudgeSanity.logException("Nmap XML is partially corrupt: " + var1, var4, false);
            return true;
         }
      }
   }

   class _A extends DefaultHandler {
      protected String I;
      protected boolean C = false;
      protected String A;
      protected String H = null;
      protected String F = null;
      protected boolean D = false;
      protected OperatingSystem B = null;
      protected int G = 0;

      public void startElement(String var1, String var2, String var3, Attributes var4) throws SAXException {
         if ("host".equals(var3)) {
            this.B = null;
            this.G = 0;
            this.A = null;
            this.C = false;
            this.I = null;
            this.H = null;
            this.F = null;
            this.D = false;
         } else if ("status".equals(var3)) {
            this.C = "up".equals(var4.getValue("state"));
         } else if ("address".equals(var3) && "ipv4".equals(var4.getValue("addrtype"))) {
            this.I = var4.getValue("addr");
         } else if ("address".equals(var3) && "ipv6".equals(var4.getValue("addrtype"))) {
            this.I = var4.getValue("addr");
         } else if ("port".equals(var3)) {
            this.A = var4.getValue("portid");
            this.H = null;
            this.F = null;
         } else if ("service".equals(var3)) {
            this.H = var4.getValue("product");
            this.F = var4.getValue("version");
         } else if ("state".equals(var3)) {
            this.D = true;
         } else if ("os".equals(var3)) {
            this.B = null;
            this.G = 0;
         } else if ("osclass".equals(var3)) {
            String var5 = var4.getValue("osfamily");
            String var6 = var4.getValue("osgen");
            int var7 = CommonUtils.toNumber(var4.getValue("accuracy"), 0);
            OperatingSystem var8 = new OperatingSystem(var5 + " " + var6);
            if (var7 > this.G && !var8.isUnknown()) {
               this.B = var8;
               this.G = var7;
            }
         }

      }

      public void endElement(String var1, String var2, String var3) throws SAXException {
         if (this.D && "host".equals(var3)) {
            if (this.B != null) {
               NmapXML.this.host(this.I, (String)null, this.B.getName(), this.B.getVersion());
            } else {
               NmapXML.this.host(this.I, (String)null, (String)null, 0.0D);
            }
         } else if (this.C && "service".equals(var3)) {
            if (this.H != null && this.F != null) {
               NmapXML.this.service(this.I, this.A, this.H + " " + this.F);
            } else if (this.H != null) {
               NmapXML.this.service(this.I, this.A, this.H);
            } else {
               NmapXML.this.service(this.I, this.A, (String)null);
            }
         }

      }
   }
}

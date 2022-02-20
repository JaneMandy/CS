package beacon.setup;

public class BeaconDLL {
   public String fileName = "";
   public byte[] originalDLL;
   public byte[] peProcessedDLL;

   public BeaconDLL() {
   }

   public BeaconDLL(String var1) {
      this.fileName = var1;
   }
}

package tunnel;

public class Write extends TunnelMessage {
   protected byte[] data;

   public Write(String var1, int var2, byte[] var3) {
      super(var1, var2);
      this.data = var3;
   }

   public byte[] getData() {
      return this.data;
   }

   public String toString() {
      return this.toString("Write " + this.data.length + " bytes");
   }
}

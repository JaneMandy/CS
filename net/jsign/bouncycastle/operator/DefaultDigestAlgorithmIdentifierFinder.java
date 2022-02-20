package net.jsign.bouncycastle.operator;

import java.util.HashMap;
import java.util.Map;
import net.jsign.bouncycastle.asn1.ASN1ObjectIdentifier;
import net.jsign.bouncycastle.asn1.DERNull;
import net.jsign.bouncycastle.asn1.cryptopro.CryptoProObjectIdentifiers;
import net.jsign.bouncycastle.asn1.nist.NISTObjectIdentifiers;
import net.jsign.bouncycastle.asn1.oiw.OIWObjectIdentifiers;
import net.jsign.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import net.jsign.bouncycastle.asn1.pkcs.RSASSAPSSparams;
import net.jsign.bouncycastle.asn1.teletrust.TeleTrusTObjectIdentifiers;
import net.jsign.bouncycastle.asn1.x509.AlgorithmIdentifier;
import net.jsign.bouncycastle.asn1.x9.X9ObjectIdentifiers;

public class DefaultDigestAlgorithmIdentifierFinder implements DigestAlgorithmIdentifierFinder {
   private static Map digestOids = new HashMap();
   private static Map digestNameToOids = new HashMap();

   public AlgorithmIdentifier find(AlgorithmIdentifier var1) {
      AlgorithmIdentifier var2;
      if (var1.getAlgorithm().equals(PKCSObjectIdentifiers.id_RSASSA_PSS)) {
         var2 = RSASSAPSSparams.getInstance(var1.getParameters()).getHashAlgorithm();
      } else {
         var2 = new AlgorithmIdentifier((ASN1ObjectIdentifier)digestOids.get(var1.getAlgorithm()), DERNull.INSTANCE);
      }

      return var2;
   }

   public AlgorithmIdentifier find(String var1) {
      return new AlgorithmIdentifier((ASN1ObjectIdentifier)digestNameToOids.get(var1), DERNull.INSTANCE);
   }

   static {
      digestOids.put(OIWObjectIdentifiers.md4WithRSAEncryption, PKCSObjectIdentifiers.md4);
      digestOids.put(OIWObjectIdentifiers.md4WithRSA, PKCSObjectIdentifiers.md4);
      digestOids.put(OIWObjectIdentifiers.sha1WithRSA, OIWObjectIdentifiers.idSHA1);
      digestOids.put(PKCSObjectIdentifiers.sha224WithRSAEncryption, NISTObjectIdentifiers.id_sha224);
      digestOids.put(PKCSObjectIdentifiers.sha256WithRSAEncryption, NISTObjectIdentifiers.id_sha256);
      digestOids.put(PKCSObjectIdentifiers.sha384WithRSAEncryption, NISTObjectIdentifiers.id_sha384);
      digestOids.put(PKCSObjectIdentifiers.sha512WithRSAEncryption, NISTObjectIdentifiers.id_sha512);
      digestOids.put(PKCSObjectIdentifiers.md2WithRSAEncryption, PKCSObjectIdentifiers.md2);
      digestOids.put(PKCSObjectIdentifiers.md4WithRSAEncryption, PKCSObjectIdentifiers.md4);
      digestOids.put(PKCSObjectIdentifiers.md5WithRSAEncryption, PKCSObjectIdentifiers.md5);
      digestOids.put(PKCSObjectIdentifiers.sha1WithRSAEncryption, OIWObjectIdentifiers.idSHA1);
      digestOids.put(X9ObjectIdentifiers.ecdsa_with_SHA1, OIWObjectIdentifiers.idSHA1);
      digestOids.put(X9ObjectIdentifiers.ecdsa_with_SHA224, NISTObjectIdentifiers.id_sha224);
      digestOids.put(X9ObjectIdentifiers.ecdsa_with_SHA256, NISTObjectIdentifiers.id_sha256);
      digestOids.put(X9ObjectIdentifiers.ecdsa_with_SHA384, NISTObjectIdentifiers.id_sha384);
      digestOids.put(X9ObjectIdentifiers.ecdsa_with_SHA512, NISTObjectIdentifiers.id_sha512);
      digestOids.put(X9ObjectIdentifiers.id_dsa_with_sha1, OIWObjectIdentifiers.idSHA1);
      digestOids.put(NISTObjectIdentifiers.dsa_with_sha224, NISTObjectIdentifiers.id_sha224);
      digestOids.put(NISTObjectIdentifiers.dsa_with_sha256, NISTObjectIdentifiers.id_sha256);
      digestOids.put(NISTObjectIdentifiers.dsa_with_sha384, NISTObjectIdentifiers.id_sha384);
      digestOids.put(NISTObjectIdentifiers.dsa_with_sha512, NISTObjectIdentifiers.id_sha512);
      digestOids.put(TeleTrusTObjectIdentifiers.rsaSignatureWithripemd128, TeleTrusTObjectIdentifiers.ripemd128);
      digestOids.put(TeleTrusTObjectIdentifiers.rsaSignatureWithripemd160, TeleTrusTObjectIdentifiers.ripemd160);
      digestOids.put(TeleTrusTObjectIdentifiers.rsaSignatureWithripemd256, TeleTrusTObjectIdentifiers.ripemd256);
      digestOids.put(CryptoProObjectIdentifiers.gostR3411_94_with_gostR3410_94, CryptoProObjectIdentifiers.gostR3411);
      digestOids.put(CryptoProObjectIdentifiers.gostR3411_94_with_gostR3410_2001, CryptoProObjectIdentifiers.gostR3411);
      digestNameToOids.put("SHA-1", OIWObjectIdentifiers.idSHA1);
      digestNameToOids.put("SHA-224", NISTObjectIdentifiers.id_sha224);
      digestNameToOids.put("SHA-256", NISTObjectIdentifiers.id_sha256);
      digestNameToOids.put("SHA-384", NISTObjectIdentifiers.id_sha384);
      digestNameToOids.put("SHA-512", NISTObjectIdentifiers.id_sha512);
      digestNameToOids.put("SHA1", OIWObjectIdentifiers.idSHA1);
      digestNameToOids.put("SHA224", NISTObjectIdentifiers.id_sha224);
      digestNameToOids.put("SHA256", NISTObjectIdentifiers.id_sha256);
      digestNameToOids.put("SHA384", NISTObjectIdentifiers.id_sha384);
      digestNameToOids.put("SHA512", NISTObjectIdentifiers.id_sha512);
      digestNameToOids.put("SHA3-224", NISTObjectIdentifiers.id_sha3_224);
      digestNameToOids.put("SHA3-256", NISTObjectIdentifiers.id_sha3_256);
      digestNameToOids.put("SHA3-384", NISTObjectIdentifiers.id_sha3_384);
      digestNameToOids.put("SHA3-512", NISTObjectIdentifiers.id_sha3_512);
      digestNameToOids.put("SHAKE-128", NISTObjectIdentifiers.id_shake128);
      digestNameToOids.put("SHAKE-256", NISTObjectIdentifiers.id_shake256);
      digestNameToOids.put("GOST3411", CryptoProObjectIdentifiers.gostR3411);
      digestNameToOids.put("MD2", PKCSObjectIdentifiers.md2);
      digestNameToOids.put("MD4", PKCSObjectIdentifiers.md4);
      digestNameToOids.put("MD5", PKCSObjectIdentifiers.md5);
      digestNameToOids.put("RIPEMD128", TeleTrusTObjectIdentifiers.ripemd128);
      digestNameToOids.put("RIPEMD160", TeleTrusTObjectIdentifiers.ripemd160);
      digestNameToOids.put("RIPEMD256", TeleTrusTObjectIdentifiers.ripemd256);
   }
}

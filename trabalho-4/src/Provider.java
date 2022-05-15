import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.Key;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;

/**
 * Fiz essa classe p/ agrupar alguns métodos que serão utilizados mais de uma vez ao longo do programa.
 * Juntei todos aqui, mas a ideia é colocar os métodos em lugares mais adequados mais a frente.
 * Implementado como singleton
 */
public class Provider {

    private static Provider instance;

    private Provider() throws Exception {
    }

    public static Provider getInstance() throws Exception {
        if (instance == null) {
            instance = new Provider();
        }
        return instance;
    }


    public PublicKey getPublicKeyFromCert(String certPath) throws IOException, CertificateException{
        // Retrieve Certificate File Bytes
        byte[] certificateBytes = this.getBytesFromFile(certPath);

        // Generate Certificate from File bytes
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        Certificate userCertificate = certificateFactory.generateCertificate(new ByteArrayInputStream(certificateBytes));

        PublicKey publicKey = userCertificate.getPublicKey();
        return publicKey;
    }

    boolean verifySignature(PublicKey publicKey, byte[] PlainText, byte[] signature) throws Exception{
        Signature sig = Signature.getInstance("SHA1withRSA");
    
        sig.initVerify(publicKey);
        sig.update(PlainText);

        return sig.verify(signature);
    }

    byte[] getBytesFromFile(String filePath) throws IOException{
        File file = new File(filePath);
        byte[] allBytes = Files.readAllBytes(file.toPath());
        return allBytes;
    }

    byte[] decryptBytes(Key key, byte[] encryptedBytes, String algorithm) throws Exception{
        // Cipher setup
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.DECRYPT_MODE, key);

        // Decryption result
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        return decryptedBytes;
    }

    Key generateSymmetricKey(String seed) throws Exception{
        // PRNG Setup
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        secureRandom.setSeed(seed.getBytes("UTF8"));

        // Key generation with created PRNG 
        KeyGenerator keyGen = KeyGenerator.getInstance("DES");
        keyGen.init(56, secureRandom);
        Key key = keyGen.generateKey();
        return key;
    }

    byte[] decryptFileWithSymmetricKey(String filepath, String seed) throws Exception{
        // File bytes retrieval
        byte[] encryptedBytes = this.getBytesFromFile(filepath);

        // Generate symmetric key using seed
        Key key = this.generateSymmetricKey(seed);

        // Decryption result
        byte[] decryptedBytes = this.decryptBytes(key, encryptedBytes, "DES/ECB/PKCS5Padding");
        return decryptedBytes;
    }
    
    PrivateKey decryptPrivateKey(String privateKeyFilePath, String passPhrase) throws Exception{

        // Decryption result - base 64 encoded private key
        byte[] encodedPKCS8PrivateKey = this.decryptFileWithSymmetricKey(privateKeyFilePath, passPhrase);

        // Base 64 decode
        String privateKeyPlainText = new String(encodedPKCS8PrivateKey, "UTF8");
        String privateKeyPEM = privateKeyPlainText
                                            .replace("-----BEGIN PRIVATE KEY-----", "")
                                            .replaceAll("\n", "")
                                            .replace("-----END PRIVATE KEY-----", "");

        byte[] decodedPKCS8PrivateKey = Base64.getDecoder().decode(privateKeyPEM);

        // Private Key Generation
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(decodedPKCS8PrivateKey));
        return privateKey;
    }
}

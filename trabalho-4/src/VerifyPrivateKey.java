import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.spec.PKCS8EncodedKeySpec;

import javax.crypto.*;
import javax.crypto.spec.*;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.file.Files;

import java.util.Base64;


public class VerifyPrivateKey {
    public static void main(String[] args) throws Exception {
        // verifica args e recebe o texto plano + padrao de assinatura
        if (args.length != 3) {
            System.err.println("Usage: java VerifyPrivateKey palavra-passe private-key public-key");
            System.exit(1);
        }
        byte[] palavraPasse = args[0].getBytes("UTF8");
        String privateKeyFilepath = args[1];
        String publicKeyFilepath = args[2];
        
        // Etapas
            // 1: Restauracao da chave privada do usuario
            // 2: Restauracao da chave publica do usuario
            // 3: Assinatura de bytes aleatorios com esta chave privada
            // 4: Verificacao da assinatura com a chave publica do usuario
            // 5: Output = True False de assinatura OK

        // ================= 1 ====================
        // File bytes retrieval
        File encryptedKey = new File(privateKeyFilepath);
        byte[] privKeyEncryptedBytes = Files.readAllBytes(encryptedKey.toPath());

        // PRNG Setup
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        secureRandom.setSeed(palavraPasse);

        // Key generation with created PRNG
        KeyGenerator keyGen = KeyGenerator.getInstance("DES");
        keyGen.init(56, secureRandom);
        Key key = keyGen.generateKey();

        // Cipher setup
        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key);

        // Decryption result - base 64 encoded private key
        byte[] encodedPKCS8PrivateKey = cipher.doFinal(privKeyEncryptedBytes);

        // Base 64 decode
        String privateKeyPlainText = new String(encodedPKCS8PrivateKey, "UTF8");
        String privateKeyPEM = privateKeyPlainText
                                            .replace("-----BEGIN PRIVATE KEY-----", "")
                                            .replaceAll(System.lineSeparator(), "")
                                            .replace("-----END PRIVATE KEY-----", "");

        byte[] decodedPKCS8PrivateKey = Base64.getDecoder().decode(privateKeyPEM);

        // Private Key Generation
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(decodedPKCS8PrivateKey));


        // ================= 2 ====================
        // Retrieve Certificate File Bytes
        File certificateFile = new File(publicKeyFilepath);
        byte[] certificateBytes = Files.readAllBytes(certificateFile.toPath());

        // Generate Certificate from File bytes
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        Certificate userCertificate = certificateFactory.generateCertificate(new ByteArrayInputStream(certificateBytes));

        PublicKey publicKey = userCertificate.getPublicKey();


        // ================= 3 ====================
        // Generate 2048 random bytes
        byte[] randomBytes = new byte[2048];
        SecureRandom.getInstanceStrong().nextBytes(randomBytes);

        // Signature setup and exec
        Signature signature = Signature.getInstance("SHA1withRSA");

        signature.initSign(privateKey);
        signature.update(randomBytes);

        byte[] signatureValue = signature.sign();

        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < signatureValue.length; i++) {
            String hex = Integer.toHexString(0x0100 + (signatureValue[i] & 0x00FF)).substring(1);
            buf.append((hex.length() < 2 ? "0" : "") + hex);
        }

        System.out.println( buf.toString() );

        // signature.initVerify(certificate); tb funciona? 
        signature.initVerify(publicKey);
        signature.update(randomBytes);
        if (signature.verify(signatureValue)) {
            System.out.println("OK");
        } else {
            System.out.println("FALHOU");
        }

        
    }
}
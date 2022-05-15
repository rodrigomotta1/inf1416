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

public class DecryptAndCheckFile {
    public static void main(String[] args) throws Exception{
        // verifica args e recebe chave priv, arquivo .enc, arquivo cert e nome-base do arquivo (i.e, sem extensão)
        // if (args.length != 4) {
        //     System.err.println("Usage: java DecryptAndCheckFile palavra-passe private-key public-key crypt-file-base-name");
        //     System.exit(1);
        // }
        // String palavraPasse = args[0];
        // String privateKeyFilePath = args[1];
        // String certificateFilePath = args[2];
        // String cryptFileBaseName = args[3];

        // Deixei hardcoded por enquanto pra facilitar a testagem
        String palavraPasse = "user01";
        String privateKeyFilePath = "C:/Users/bruno/OneDrive/Documents/PUC/INF1416/inf1416/trabalho-4/keys/user01-pkcs8-des.key";
        String certificateFilePath = "C:/Users/bruno/OneDrive/Documents/PUC/INF1416/inf1416/trabalho-4/keys/user01-x509.crt";
        String cryptFileBaseName = "C:/Users/bruno/OneDrive/Documents/PUC/INF1416/inf1416/trabalho-4/files/index";

        // ================= Inicializando variáveis iniciais =================

        // Adiciona extensões ao nome-base do arquivo p/ ter caminho do envelope digital, do criptograma e da assinatura digital
        String digitalEnvelopeFilePath = cryptFileBaseName + ".env";
        String criptogramFilePath = cryptFileBaseName + ".enc";
        String digitalSignatureFilePath = cryptFileBaseName + ".asd";

        // instancia essa classe p/ poder usar métodos auxiliares dela
        DecryptAndCheckFile decryptAndCheckFile = new DecryptAndCheckFile();

        // Etapas
            // 1: Decripta envelope (.env) com chave privada, pegando aí a semente da chave simétrica
            // 2: A partir da semente, produz chave simétrica e decripta arquivo .enc (criptograma)
            // 3: Com texto plano, calcula hash dele e compara c/ hash da assinatura digital p/ validar
            //    integridade e autenticidade

        // ================= 1 ====================
        // Get digital envelope bytes and private key object
        byte[] digEnvEncryptedBytes = decryptAndCheckFile.getBytesFromFile(digitalEnvelopeFilePath);
        PrivateKey privateKey = decryptAndCheckFile.decryptPrivateKey(privateKeyFilePath, palavraPasse);        
        
        // Decrypt digital envelope with private key to get seed for symmetric key
        byte[] decryptedSeed = decryptAndCheckFile.decryptBytes(privateKey, digEnvEncryptedBytes, "RSA/ECB/PKCS1Padding");
        String seed = new String(decryptedSeed,"UTF8");


        // ================= 2 ====================
        // Decrypt criptogram with symmetric key generated from seed
        byte[] decryptedCriptogram = decryptAndCheckFile.decryptFileWithSymmetricKey(criptogramFilePath, seed);        


        // ================= 3 ====================
        // Get sigital signature bytes and public key object
        byte[] signatureEncryptedBytes = decryptAndCheckFile.getBytesFromFile(digitalSignatureFilePath);
        PublicKey publicKey = decryptAndCheckFile.getPublicKeyFromCert(certificateFilePath);
        // Check if signature is valid by comparing hashes
        boolean isValid = decryptAndCheckFile.verifySignature(publicKey, decryptedCriptogram, signatureEncryptedBytes);
        
        if (isValid){
            System.out.println("Parabéns, íntegro e autêntico");
            System.out.println("Conteúdo do arquivo texto:");
            String textoPlano = new String(decryptedCriptogram,"UTF8");
            System.out.println(textoPlano);
        }
        else
            System.out.println("Cuidado: assinatura digital inválida; não podemos garantir a autoria do arquivo");
    }

    PublicKey getPublicKeyFromCert(String certPath) throws IOException, CertificateException{
        DecryptAndCheckFile decryptAndCheckFile = new DecryptAndCheckFile();

        // Retrieve Certificate File Bytes
        byte[] certificateBytes = decryptAndCheckFile.getBytesFromFile(certPath);

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
        DecryptAndCheckFile decryptAndCheckFile = new DecryptAndCheckFile();

        // File bytes retrieval
        byte[] encryptedBytes = decryptAndCheckFile.getBytesFromFile(filepath);

        // Generate symmetric key using seed
        Key key = decryptAndCheckFile.generateSymmetricKey(seed);

        // Decryption result
        byte[] decryptedBytes = decryptAndCheckFile.decryptBytes(key, encryptedBytes, "DES/ECB/PKCS5Padding");
        return decryptedBytes;
    }
    
    PrivateKey decryptPrivateKey(String privateKeyFilePath, String passPhrase) throws Exception{
        DecryptAndCheckFile decryptAndCheckFile = new DecryptAndCheckFile();

        // Decryption result - base 64 encoded private key
        byte[] encodedPKCS8PrivateKey = decryptAndCheckFile.decryptFileWithSymmetricKey(privateKeyFilePath, passPhrase);

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

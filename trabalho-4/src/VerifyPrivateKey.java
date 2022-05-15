import java.security.*;


public class VerifyPrivateKey {
    public static void main(String[] args) throws Exception {
        // verifica args e recebe o texto plano + padrao de assinatura
        if (args.length != 3) {
            System.err.println("Usage: java VerifyPrivateKey palavra-passe private-key public-key");
            System.exit(1);
        }
        String palavraPasse = args[0];
        String privateKeyFilepath = args[1];
        String publicKeyFilepath = args[2];
        
        // Etapas
            // 1: Restauracao da chave privada do usuario
            // 2: Restauracao da chave publica do usuario
            // 3: Assinatura de bytes aleatorios com esta chave privada
            // 4: Verificacao da assinatura com a chave publica do usuario
            // 5: Output = True False de assinatura OK

        // ================= 1 ====================
        // instancia essa classe para usar métodos auxiliares dela
        Provider provider = Provider.getInstance();

        // Private Key Generation
        PrivateKey privateKey = provider.decryptPrivateKey(privateKeyFilepath, palavraPasse);


        // ================= 2 ====================
        // Generate Public Kew object from Certificate File
        PublicKey publicKey = provider.getPublicKeyFromCert(publicKeyFilepath);


        // ================= 3, 4, 5 ====================
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
        if (provider.verifySignature(publicKey, randomBytes, signatureValue)) {
            System.out.println("OK");
        } else {
            System.out.println("FALHOU");
        }
        
    }
}
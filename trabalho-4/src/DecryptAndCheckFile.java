import java.security.PrivateKey;
import java.security.PublicKey;


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
        Provider provider = Provider.getInstance();

        // Etapas
            // 1: Decripta envelope (.env) com chave privada, pegando aí a semente da chave simétrica
            // 2: A partir da semente, produz chave simétrica e decripta arquivo .enc (criptograma)
            // 3: Com texto plano, calcula hash dele e compara c/ hash da assinatura digital p/ validar
            //    integridade e autenticidade

        // ================= 1 ====================
        // Get digital envelope bytes and private key object
        byte[] digEnvEncryptedBytes = provider.getBytesFromFile(digitalEnvelopeFilePath);
        PrivateKey privateKey = provider.decryptPrivateKey(privateKeyFilePath, palavraPasse);        
        
        // Decrypt digital envelope with private key to get seed for symmetric key
        byte[] decryptedSeed = provider.decryptBytes(privateKey, digEnvEncryptedBytes, "RSA/ECB/PKCS1Padding");
        String seed = new String(decryptedSeed,"UTF8");


        // ================= 2 ====================
        // Decrypt criptogram with symmetric key generated from seed
        byte[] decryptedCriptogram = provider.decryptFileWithSymmetricKey(criptogramFilePath, seed);        


        // ================= 3 ====================
        // Get sigital signature bytes and public key object
        byte[] signatureEncryptedBytes = provider.getBytesFromFile(digitalSignatureFilePath);
        PublicKey publicKey = provider.getPublicKeyFromCert(certificateFilePath);

        // Check if signature is valid by comparing hashes
        boolean isValid = provider.verifySignature(publicKey, decryptedCriptogram, signatureEncryptedBytes);
        
        // Print results
        if (isValid){
            System.out.println("Parabéns, íntegro e autêntico");
            System.out.println("Conteúdo do arquivo texto:");
            String textoPlano = new String(decryptedCriptogram,"UTF8");
            System.out.println(textoPlano);
        }
        else
            System.out.println("Cuidado: assinatura digital inválida; não podemos garantir a autoria do arquivo");
    }
    
}

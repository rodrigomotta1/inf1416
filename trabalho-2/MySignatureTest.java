import java.security.*;

/**
* Esta classe testa a classe MySignature com base no texto plano e no padrão fornecidos via linha de comando.
*
* @author  Bruno Coutinho (1910392)
* @author Rodrigo Motta (1811524)
* 
*/
public class MySignatureTest {
    public static void main(String[] args) throws Exception{
    
        // verifica args e recebe o texto plano + padrao de assinatura
        if (args.length != 2){
            System.err.println("Usage: java MySignatureTest string padrao");
            System.exit(1);
        }
        byte[] plainText = args[0].getBytes("UTF8");
        String padrao = args[1];
    
        // gera o par de chaves RSA
        System.out.println( "\nStart generating RSA key" );
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        KeyPair key = keyGen.generateKeyPair();
        System.out.println( "Finish generating RSA key" );
    
        // define um objeto signature para utilizar padrão fornecido
        // e assina o texto plano com a chave privada
        System.out.println( "\nStart generating signature" );
        System.out.println( "Instantiating MySignature class with chosen algorithm" );
        MySignature sig = MySignature.getInstance(padrao);
        System.out.println( "Initiating sign with generated private key - switching to encrypt mode" );
        sig.initSign(key.getPrivate());
        System.out.println( "Updating text" );
        sig.update(plainText);
        System.out.println( "Generating signature - encrypting digest" );
        byte[] signature = sig.sign();
        System.out.println( "\nFinish generating signature" );

        System.out.println( "\nSignature:" );
        // converte o signature para hexadecimal
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < signature.length; i++) {
            String hex = Integer.toHexString(0x0100 + (signature[i] & 0x00FF)).substring(1);
            buf.append((hex.length() < 2 ? "0" : "") + hex);
        }

        // imprime o signature em hexadecimal
        System.out.println( buf.toString() );

        // verifica a assinatura com a chave publica
        System.out.println( "\nStart signature verification" );
        System.out.println( "Initiating verification with generated public key - switching to decrypt mode" );
        sig.initVerify(key.getPublic());
        System.out.println( "Updating text" );
        sig.update(plainText);
        System.out.println( "Comparing digests to verify signature" );
        try {
            if (sig.verify(signature)) {
                System.out.println( "\nSignature verified" );
            } 
            else System.out.println( "\nSignature failed" );
        } 
        catch (SignatureException se) {
            System.out.println( "\nSingature failed" );
        }
        return;
    }
}
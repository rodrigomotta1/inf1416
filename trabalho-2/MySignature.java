import java.security.*;
import java.util.Arrays;

import javax.crypto.*;

/**
* Esta classe implementa e disponbiliza um conjunto de métodos para
* a geração e validação de uma Assinatura Digital
*
* Esta classe utiliza o padrão Singleton, ou seja, a instância é criada
* uma única vez e reutilizada em toda a duração do programa
* 
* @author  Bruno Coutinho (1910392)
* @author Rodrigo Motta (1811524)
* 
* @see https://refactoring.guru/pt-br/design-patterns/singleton/java/example#:~:text=O%20Singleton%20%C3%A9%20um%20padr%C3%A3o,contras%20que%20as%20vari%C3%A1veis%20globais.
*
*/
public final class MySignature {

    /**
     * 
     * Guarda a instância da classe MySignature
     * 
     */
    private static MySignature instance;


     /**
     * 
     * Guarda o resumo de mensagem do texto plano a ser assinado
     * 
     */
    private static byte[] resumoMensagem;


    /**
     * 
     * Guarda o padrão de assinatura que está sendo utilizado
     * 
     */
    private static String padraoAssinatura;

    private static PrivateKey chavePrivada;
    private static PublicKey chavePublica;
    private static Cipher cipher;
    private static MessageDigest providerDigest;

    /**
     * 
     * Instancia a classe de MySignature com o valor "padrao", definindo o padrão de assinatura. Também instancia o objeto cipher da classe.
     *
     * @param padrao 
     *   string contendo o padrão de assinatura, 
     *   deve ser um dos seguintes: "MD5withRSA", "SHA1withRSA", "SHA256withRSA", "SHA512withRSA"
     *
     */
    private MySignature(String padrao) throws Exception {
        MySignature.padraoAssinatura = padrao;
        MySignature.cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
    }


    /**
     * 
     * Método de acesso à classe MySignature
     *
     * @param padrao 
     *   string contendo o padrão de assinatura, 
     *   deve ser um dos seguintes: "MD5withRSA", "SHA1withRSA", "SHA256withRSA", "SHA512withRSA"
     *
     */
    public static MySignature getInstance(String padrao) throws Exception {
        if (instance == null) {
            instance = new MySignature(padrao);
        }
        return instance;
    }


    /**
     * 
     * Guarda o valor da chave privada recebida na instância para futura encriptação do resumo de mensagem.
     * 
     * @param key valor da chave privada necessária para a encriptação do resumo de mensagem.
     * 
     */
    public final void initSign(PrivateKey key) throws Exception {
        MySignature.chavePrivada = key;

        // Inicializa o objeto cipher em modo de encryptação com a chave privada recebida
        cipher.init(Cipher.ENCRYPT_MODE, MySignature.chavePrivada);
    }


    /**
     * 
     * Guarda o resumo de mensagem do texto plano recebido na instância da classe. O tipo de digest também é definido dependendo do padrão de assinatura escolhido
     * 
     * @param textoPlano texto plano que será assinado
     * 
     */
    public final void update(byte[] textoPlano) throws Exception {

        // Define padrão de Digest em função do padrão de assinatura informado
        switch (MySignature.padraoAssinatura) {
            case "MD5withRSA":
                MySignature.providerDigest = MessageDigest.getInstance("MD5");
                break;

            case "SHA1withRSA":
                MySignature.providerDigest = MessageDigest.getInstance("SHA1");
                break;

            case "SHA256withRSA":
                MySignature.providerDigest = MessageDigest.getInstance("SHA256");
                break;

            case "SHA512withRSA":
                MySignature.providerDigest = MessageDigest.getInstance("SHA512");
                break;

            default:
                System.out.println("Padrao de assinatura nao suportado");
                break;
        }

        // Calcula e armazena o resumo de mensagem do texto plano recebido em bytes
        MySignature.providerDigest.update(textoPlano);
        MySignature.resumoMensagem = MySignature.providerDigest.digest();
    }


    /**
     * 
     * Realiza a encriptação do resumo de mensagem guardado, gerado a partir do texto plano recebido, e imprime digest. O valor é retornado.
     * 
     * @return string de bytes contendo a assinatura digital do texto plano guardado
     */
    public final byte[] sign() throws Exception {
        System.out.println("Digest calculated for message:");
		for(int i = 0; i != MySignature.resumoMensagem.length; i++)
			System.out.print(String.format("%02X", MySignature.resumoMensagem[i]));

        return MySignature.cipher.doFinal(MySignature.resumoMensagem);
    }

    /**
     * 
     * Guarda o valor da chave publica recebida na instância para futura decriptação da assinatura. Também troca o modo de operação para a decriptação
     * 
     * @param key valor da chave publica necessária para a decriptação da assinatura.
     * 
     */
    public final void initVerify(PublicKey key) throws Exception{
        MySignature.chavePublica = key;

        // Inicializa o objeto cipher em modo de decriptação com a chave pública recebida
        cipher.init(Cipher.DECRYPT_MODE, MySignature.chavePublica);
    }

    /**
     * 
     * Realiza a decriptacao da assinatura, imprime na saída padrão e compara o valor com o resumo de mensagem armazenado
     * 
     * @return true se o resumo de mensagem guardado for igual ao resumo de mensagem percebido apos a decriptacao da assinatura. false caso contrario
     */
    public final boolean verify(byte[] assinatura) throws Exception {
        byte[] DigestAssinatura = MySignature.cipher.doFinal(assinatura);
        System.out.println("Digest obtained from decrypting digital signature:");
		for(int i = 0; i != DigestAssinatura.length; i++)
			System.out.print(String.format("%02X", DigestAssinatura[i]));
    
        return Arrays.equals(MySignature.resumoMensagem, DigestAssinatura);
    }

}
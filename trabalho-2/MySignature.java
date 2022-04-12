import java.security.*;
import javax.crypto.*;

/**
* Esta classe implementa e disponbiliza um conjunto de métodos para
* a geração e validação de uma Assinatura Digital
*
* Esta classe utiliza o padrão Singleton, ou seja, a instância é criada
* uma única vez e reutilizada em toda a duração do programa
* 
* @author  Bruno Coutinho (matricula)
* @author Rodrigo Motta (1811524)
* 
*/
public final class MySignature {

    /**
    * Guarda a instância da classe MySignature
    */
    private static MySignature instance;

    /**
    * Guarda o padrão de assinatura que está sendo utilizado
    */
    private String padrao_assinatura;

    /**
    * Instancia a classe de MySignature com o valor "padrao", definindo o padrão de assinatura
    *
    * @param padrao 
    *   string contendo o padrão de assinatura, 
    *   deve ser um dos seguintes: "MD5withRSA", "SHA1withRSA", "SHA256withRSA", "SHA512withRSA"
    *
    */
    private MySignature(String padrao) {
        this.padrao_assinatura = padrao;
    }

    /**
    * Método de acesso à classe MySignature
    *
    * @param padrao 
    *   string contendo o padrão de assinatura, 
    *   deve ser um dos seguintes: "MD5withRSA", "SHA1withRSA", "SHA256withRSA", "SHA512withRSA"
    *
    */
    public static MySignature getInstance(String padrao) {
        if (instance == null) {
            instance = new MySignature(padrao);
        }
        return instance;
    }


}
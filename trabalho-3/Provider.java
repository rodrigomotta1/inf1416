import java.security.*;
import java.util.ArrayList;

public class Provider {

    private static Provider instance;
    private static String padraoDigest;

    /**
     * 
     * Instancia a classe de Provider com o valor "padrao", definindo o padrão de digest. 
     *
     * @param padrao 
     *   string contendo o padrão de digest, 
     *   deve ser um dos seguintes: "MD5", "SHA1", "SHA256", "SHA512"
     *
     */
    private Provider(String padrao) throws Exception {
        Provider.padraoDigest = padrao;
    }

    /**
     * 
     * Método de acesso à classe Provider
     *
     * @param padrao 
     *   string contendo o padrão de assinatura, 
     *   deve ser um dos seguintes: "MD5", "SHA1", "SHA256", "SHA512"
     *
     */
    public static Provider getInstance(String padrao) throws Exception {
        if (instance == null) {
            instance = new Provider(padrao);
        }
        return instance;
    }

    /**
     * Recebe caminho para um diretório e transforma em lista de objetos da classe Arquivo
     * 
     * @param path_to_folder caminho para diretório que contém arquivos que serão processados
     * @return lista de Arquivos inicializados com seu path
     */
    public ArrayList<Arquivo> inicializa_arquivos(String path_to_folder){

    }

    /**
     * Recebe arquivo que contém uma lista de digests de arquivos conhecidos e transforma em
     * lista de objetos da classe Arquivo
     * 
     * @param path_to_digest_list caminho do arquivo que contém uma lista de digests de arquivos conhecidos
     * @return lista de DigestConhecidos, com todos seus atributos atualizados
     */
    public ArrayList<DigestConhecido> inicializa_digests_conhecidos(String path_to_digest_list){

    }
    
    /**
     * Compara um arquivo com os outros e com a lista de digests conhecidos para determinar
     * o status dele
     * 
     * @param arquivo
     * @param listaArquivos
     * @param path_to_digest_list
     */
    public void check_status(Arquivo arquivo, ArrayList<Arquivo> listaArquivos,
                              ArrayList<DigestConhecido> digestsConhecidos){
        // if colision:
        //     return colision
        // if not_found:
        //     return not_found
        // if ok:
        //     return ok
        // return not_ok
    }

    /**
     * 
     * @return
     */
    private int check_colision(){

    }

    /**
     * 
     * @return
     */
    private int check_not_found(){

    }

    /**
     * 
     * @return
     */
    private int check_ok(){

    }
}

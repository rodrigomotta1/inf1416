import java.security.*;
import java.util.ArrayList;


/**
 * Esta classe calcula o digest solicitado do conteúdo de todos os arquivos presentes na pasta fornecida ao executar o programa.
 *
 * Para cada arquivo, compara-se o digest calculado com o digest armazenado no arquivo contendo os registros de digests dos arquivos passado.
 * 
 * Uma lista é impressa na saída, com o seguinte formato:
 *      Nome_Arq1 Tipo_Digest Digest_Hex_Arq1 STATUS
 *      Nome_Arq2 Tipo_Digest Digest_Hex_Arq2 STATUS
 *                        [ ... ]
 *      Nome_Arq1 Tipo_Digest Digest_Hex_Arq1 STATUS
 * 
 * Os STATUS podem ser:
 *      OK -> Digest calculado é igual ao registrado, sem colisão com outros digests armazenados no arquivo de registro
 *      NOT OK -> Digest calculado é diferente do registrado, sem colisão com outros digests armazenados no arquivo de registro
 *      NOT FOUND -> Arquivo não foi encontrado no arquivo de registro de digests
 *      COLISION -> Digest calculado é igual ao digest de outro arquivo registrado na lista de registro de digests
 * 
 * @author  Bruno Coutinho (1910392)
 * @author Rodrigo Motta (1811524)
 * 
 * 
 */
public class DigestCalculator {
    private class DigestConhecido{
        String fileName;
        String digestType;
        String digestHex;
    }
    public static void main(String[] args) throws Exception{
    
        // verifica args e recebe o texto plano + padrao de assinatura

        // deve receber, nesta ordem:
            // tipo do resumo de mensagem
            // caminho pro arquivo com lista de resumos (base)
            // caminho pra pasta de arquivos

        if (args.length != 4){
            System.err.println("Usage: java DigestCalculator digest_type path/to/digest-list.txt path/to/files-folder");
            System.exit(1);
        }

        //TODO conferir se parâmetros são válidos

        String digest_type = args[0];
        String path_to_digest_list = args[1];
        String path_to_folder = args[2];
        DigestCalculator digestCalculator = new DigestCalculator();

        // processa as informações da pasta de arquivos e do arquivo de digests conhecidos,
        // guardando os dados em estruturas adequadas
        ArrayList<Arquivo> arquivosPasta = digestCalculator.inicializa_arquivos(path_to_folder);
        ArrayList<DigestConhecido> digestsConhecidos = digestCalculator.inicializa_digests_conhecidos(path_to_digest_list);

        // pseudocódigo:
        // for arquivo in pasta:
        //      calcula o digest de cada
        // for digest_calculado:
        //      determina status, comparando com outros (tanto os do arquivo quanto recém calculados)
        // imprime tudo

        // calcula o digest de cada arquivo da pasta, armazenando-o no objeto do arquivo
        for (final Arquivo arquivo : arquivosPasta){
            arquivo.calcula_digest(digest_type);
        }

        // determina o status de cada arquivo da pasta, comparando o digest de cada arquivo com
        // a lista de digests conhecidos e com os digests dos outros arquivos da pasta
        for (final Arquivo arquivo : arquivosPasta){
            digestCalculator.check_status(arquivo, arquivosPasta, digestsConhecidos);
            //TODO if status==not_found, adicionar ao arquivo de lista de digests
        }

        //TODO imprimir tudo


        // Calcular o resumo de mensagem de todos os arquivos presentes na pasta recebida como parametro

        // Comparar cada digest com todos os outros presentes na base, inclusive o próprio, se existir
        
        // Na saída padrão, informar cada digest calculado, junto do tipo e do status definido

        return;
    }

    /**
     * Recebe caminho para um diretório e transforma em lista de objetos da classe Arquivo
     * 
     * @param path_to_folder caminho para diretório que contém arquivos que serão processados
     * @return lista de Arquivos inicializados com seu path
     */
    private ArrayList<Arquivo> inicializa_arquivos(String path_to_folder){

    }

    /**
     * Recebe arquivo que contém uma lista de digests de arquivos conhecidos e transforma em
     * lista de objetos da classe Arquivo
     * 
     * @param path_to_digest_list caminho do arquivo que contém uma lista de digests de arquivos conhecidos
     * @return lista de DigestConhecidos, com todos seus atributos atualizados
     */
    private ArrayList<DigestConhecido> inicializa_digests_conhecidos(String path_to_digest_list){

    }

    /**
     * Compara um arquivo com os outros e com a lista de digests conhecidos para determinar
     * o status dele
     * 
     * @param arquivo
     * @param listaArquivos
     * @param path_to_digest_list
     */
    private void check_status(Arquivo arquivo, ArrayList<Arquivo> listaArquivos,
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

import java.io.File;
import java.io.FileNotFoundException;
import java.security.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

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
     * @param caminho_pasta caminho para diretório que contém arquivos que serão processados
     * @return lista de Arquivos inicializados com seu path
     * @throws Exception
     */
    public ArrayList<Arquivo> inicializa_arquivos(String caminho_pasta, String tipo_digest) throws Exception{
        File pasta = new File(caminho_pasta);
        if (!pasta.isDirectory()){
            System.err.println("Error: path/to/files-folder is not a valid path for a folder");
            System.exit(1);
        }

        ArrayList<Arquivo> listaArquivos = new ArrayList<Arquivo>();
        ArrayList<String> file_names = new ArrayList<String>(Arrays.asList(pasta.list()));

        for (final String file_name : file_names){            
            listaArquivos.add(new Arquivo(caminho_pasta + '/' + file_name, tipo_digest));
        }
        return listaArquivos;
    }

    /**
     * Recebe arquivo que contém uma lista de digests de arquivos conhecidos e transforma em
     * lista de objetos da classe Arquivo
     * 
     * @param pathToDigestList caminho do arquivo que contém uma lista de digests de arquivos conhecidos
     * @return lista de DigestConhecidos, com todos seus atributos atualizados
     * @throws FileNotFoundException
     */
    public ArrayList<DigestConhecido> inicializa_digests_conhecidos(String pathToDigestList) throws FileNotFoundException{
        File arquivoDigestsConhecidos = new File(pathToDigestList);
        ArrayList<DigestConhecido> listaDigestsConhecidos = new ArrayList<>();

        Scanner leitor = new Scanner(arquivoDigestsConhecidos);
        while(leitor.hasNextLine()){
            String linha = leitor.nextLine();
            String[] partesLinha = linha.split("\s");
            DigestConhecido primeiroDigest = new DigestConhecido();
            primeiroDigest.fileName = partesLinha[0];
            primeiroDigest.digestType = partesLinha[1];
            primeiroDigest.digestHex = partesLinha[2];
            listaDigestsConhecidos.add(primeiroDigest);

            if (partesLinha.length > 3){
                for(int i = 3; i < partesLinha.length; i += 2){
                    DigestConhecido outroDigest = new DigestConhecido();
                    outroDigest.fileName = partesLinha[0];
                    outroDigest.digestType = partesLinha[i];
                    outroDigest.digestHex = partesLinha[i + 1];
                    listaDigestsConhecidos.add(outroDigest);
                }
            }
        }
        leitor.close();
        return listaDigestsConhecidos;
    }
    
    /**
     * Compara um arquivo com os outros e com a lista de digests conhecidos para determinar
     * o status dele
     * 
     * @param arquivo
     * @param listaArquivos
     * @param pathToDigestList
     */
    public Arquivo.DigestCheckStatus check_status(Arquivo arquivo, ArrayList<Arquivo> listaArquivos,
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
        return 1;
    }

    /**
     * 
     * @return
     */
    private int check_not_found(){
        return 1;
    }

    /**
     * 
     * @return
     */
    private int check_ok(){
        return 1;
    }
}

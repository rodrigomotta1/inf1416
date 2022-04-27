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
     * @param caminhoPasta caminho para diretório que contém arquivos que serão processados
     * @return lista de Arquivos inicializados com seu path
     * @throws Exception
     */
    public ArrayList<Arquivo> inicializaArquivos(String caminhoPasta, String tipoDigest) throws Exception{
        File pasta = new File(caminhoPasta);
        if (!pasta.isDirectory()){
            System.err.println("Error: path/to/files-folder is not a valid path for a folder");
            System.exit(1);
        }

        ArrayList<Arquivo> listaArquivos = new ArrayList<Arquivo>();
        ArrayList<String> fileNames = new ArrayList<String>(Arrays.asList(pasta.list()));

        for (final String fileName : fileNames){            
            listaArquivos.add(new Arquivo(caminhoPasta + '/' + fileName, tipoDigest));
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
    public ArrayList<DigestConhecido> inicializaDigestsConhecidos(String pathToDigestList) throws FileNotFoundException{
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
    public Arquivo.DigestCheckStatus checkStatus(Arquivo arquivo, ArrayList<Arquivo> listaArquivos,
    ArrayList<DigestConhecido> digestsConhecidos){
        if (this.checkColision(arquivo, listaArquivos, digestsConhecidos)){
            return Arquivo.DigestCheckStatus.COLISION;
        }
        if (this.checkNotFound(arquivo, digestsConhecidos)){
            return Arquivo.DigestCheckStatus.NOT_FOUND;
        }
        if (this.checkOk(arquivo, digestsConhecidos)){
            return Arquivo.DigestCheckStatus.OK;
        }
        return Arquivo.DigestCheckStatus.NOT_OK;
    }

    /**
     * 
     * @return
     */
    private boolean checkColision(Arquivo arquivo, ArrayList<Arquivo> listaArquivos,
    ArrayList<DigestConhecido> digestsConhecidos){
        return true;
    }

    /**
     * 
     * @return
     */
    private boolean checkNotFound(Arquivo arquivo, ArrayList<DigestConhecido> digestsConhecidos){
        return true;
    }

    /**
     * 
     * @return
     */
    private boolean checkOk(Arquivo arquivo, ArrayList<DigestConhecido> digestsConhecidos){
        return true;
    }
}

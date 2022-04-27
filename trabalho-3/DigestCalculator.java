import java.security.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.nio.file.*;
import java.io.FileOutputStream;
import java.io.BufferedReader;
import java.io.FileReader;


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
 *      NOT FOUND -> Digest do Arquivo não foi encontrado no arquivo de registro de digests
 *      COLISION -> Digest calculado é igual ao digest de outro arquivo registrado na lista de registro de digests
 * 
 * @author  Bruno Coutinho (1910392)
 * @author Rodrigo Motta (1811524)
 * 
 * 
 */
public class DigestCalculator {
    public static void main(String[] args) throws Exception{
    
        if (args.length != 4){
            System.err.println("Usage: java DigestCalculator tipoDigest path/to/digest-list.txt path/to/files-folder");
            System.exit(1);
        }

        String digest_type = args[0];
        String path_to_digest_list = args[1];
        String path_to_folder = args[2];

        if(!pathValido(path_to_digest_list) | !pathValido(path_to_folder))
        {
            System.out.println("Path invalido");
            System.exit(1);
        }

        if (!digestValido(digest_type)) {
            System.out.println("Tipo de digest invalido");
            System.exit(1);
        }

        Provider provider = Provider.getInstance(digest_type);

        // processa as informações da pasta de arquivos e do arquivo de digests conhecidos,
        // guardando os dados em estruturas adequadas e calculando os digests
        ArrayList<Arquivo> arquivosPasta = provider.inicializaArquivos(path_to_folder, digest_type);
        ArrayList<DigestConhecido> digestsConhecidos = provider.inicializaDigestsConhecidos(path_to_digest_list);

        // determina o status de cada arquivo da pasta, comparando o digest de cada arquivo com
        // a lista de digests conhecidos e com os digests dos outros arquivos da pasta
        for (final Arquivo arquivo : arquivosPasta){
            Arquivo.DigestCheckStatus status =  provider.checkStatus(arquivo, arquivosPasta, digestsConhecidos);
            arquivo.setStatus(status);

            // adicionando arquivo com status not found à lista de digests conhecidos
            if (status == Arquivo.DigestCheckStatus.NOT_FOUND) {
                replaceLines(path_to_digest_list, arquivo.getNome(), digest_type + " " + arquivo.getDigest());
            } 
        }

        for (final Arquivo arquivo : arquivosPasta) {
            System.out.println(arquivo.getNome() + " " + digest_type + " " + arquivo.getDigest() + " " + arquivo.getStatus());
        }

        return;
    }

    private static boolean pathValido(String path) {
        try {
            Paths.get(path);

        } catch (InvalidPathException | NullPointerException ex) {

            return false;
        }

        return true;
    }

    private static boolean digestValido(String digest) {
        if (digest != "MD5" & digest != "SHA1" & digest != "SHA256" & digest != "SHA512") {
            return false;
        } else {
            return true;
        }
    }

    // read file one line at a time
    // replace line as you read the file and store updated lines in StringBuffer
    // overwrite the file with the new lines
    public static void replaceLines(String path, String filename, String newContent) {
        try {
            // input the (modified) file content to the StringBuffer "input"
            BufferedReader file = new BufferedReader(new FileReader(path));
            StringBuffer inputBuffer = new StringBuffer();
            String line;

            while ((line = file.readLine()) != null) {
                if (line.split(" ", 2)[0] == filename) {
                    line = line + newContent; // replace the line here
                }
                inputBuffer.append(line);
                inputBuffer.append('\n');
            }
            file.close();

            // write the new string with the replaced line OVER the same file
            FileOutputStream fileOut = new FileOutputStream(path);
            fileOut.write(inputBuffer.toString().getBytes());
            fileOut.close();

        } catch (Exception e) {
            System.out.println("Problem reading file.");
            System.exit(1);
        }
    }
}

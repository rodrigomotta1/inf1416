import java.security.*;

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
    public static void main(String[] args) throws Exception{
    
        // verifica args e recebe o texto plano + padrao de assinatura
        if (args.length != 2){
            System.err.println("Usage: java MySignatureTest string padrao");
            System.exit(1);
        }
        
        return;
    }
}

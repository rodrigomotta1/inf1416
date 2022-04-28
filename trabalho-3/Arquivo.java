import java.security.*;
import java.nio.file.*;

/**
 * 
 * Classe contendo informações de um arquivo o qual o resumo de mensagem será calculado por DigestCalculator.
 * 
 * Após o cálculo do resumo de mensagem de um arquivo instanciado por essa classe, o status gerado 
 * pela checagem do resumo calculado com relação ao da base de digests fica salvo como o status da instância, podendo ser recuperado futuramente.
 * 
 * 
 * @author  Bruno Coutinho (1910392)
 * @author Rodrigo Motta (1811524)
 * 
 */
public class Arquivo {

    private byte[] digest;

    private Path path;

    private String nome;

    private byte[] conteudo;

    private DigestCheckStatus status;

    private MessageDigest providerDigest;

    private String tipoDigest;

    public enum DigestCheckStatus {
        OK,                         // digest calculado igual ao encontrado na base
        NOT_OK,                     // digest calculado diferente do encontrado na base
        NOT_FOUND,                  // digest do arquivo não localizado na base
        COLISION,                   // digset calculado foi encontrado como digest de outro arquivo distinto na base
        UNDEFINED                   // status especial, só existente no ato de criação do arquivo
    }

    public Arquivo(String path, String tipoDigest) throws Exception {
        this.status = DigestCheckStatus.UNDEFINED;
        this.path = Paths.get(path);
        // Pega e guarda o conteúdo do arquivo informado em path
        this.conteudo = Files.readAllBytes(this.path);
        this.nome = this.path.getFileName().toString();
        this.tipoDigest = tipoDigest;

        // Calcula e armazena o resumo de mensagem do conteúdo do arquivo informado em path
        try {

            this.providerDigest = MessageDigest.getInstance(tipoDigest);
            
        } catch (NoSuchAlgorithmException e) {

            System.out.println("Padrao de assinatura nao suportado");
            System.exit(1);
        }

        this.providerDigest.update(this.conteudo);
        this.digest = this.providerDigest.digest();

    }


    /**
     * Define o valor do status de checagem da instância
     * 
     * @param status: tipo do status desejado. deve corresponder ao ENUM desta classe
     */
    public void setStatus(DigestCheckStatus status) {
        this.status = status;
    }

    /**
     * Retorna o valor do status calculado do arquivo
     */
    public DigestCheckStatus getStatus() {
        return this.status;
    }

    /**
     * Retorna lista de bytes contendo o digest calculado do arquivo no momento de instanciação
     */
    public byte[] getDigest() {
        return this.digest;
    }

    public String getNome() {
        return this.nome;
    }

    public String getTipoDigest(){
        return this.tipoDigest;
    }

}

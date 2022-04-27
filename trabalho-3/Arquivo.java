import java.security.*;

/**
 * Classe representando um arquivo cujo digest será calculado e comparado.
 *
 * @author  Bruno Coutinho (1910392)
 * @author Rodrigo Motta (1811524)
 * 
 */
public class Arquivo {
    static enum Status{
        OK,
        NOT_OK,
        NOT_FOUND,
        COLISION
    }
    private Status status;
    MessageDigest digest;
    String path;

    public Arquivo(String path){
        this.path = path;
    }

    /**
     * 
     * @param type
     */
    public void calcula_digest(String type){

    }

    /**
     * 
     * @return
     */
    public status getStatus(){
        return Status;
    }

    /**
     * 
     * @param Status
     * @return
     */
    public int setStatus(status Status){
        return 1;
    }

}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package comunicacaoprocessos;

/**
 *
 * @author Tiago
 */
public class Arquivo {
    
    private String nome;
    private String ipPrimario;
    private String ipSecundario;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getIpPrimario() {
        return ipPrimario;
    }

    public void setIpPrimario(String ipPrimario) {
        this.ipPrimario = ipPrimario;
    }

    public String getIpSecundario() {
        return ipSecundario;
    }

    public void setIpSecundario(String ipSecundario) {
        this.ipSecundario = ipSecundario;
    }   

    public Arquivo(String nome, String ipPrimario, String ipSecundario) {
        this.nome = nome;
        this.ipPrimario = ipPrimario;
        this.ipSecundario = ipSecundario;
    }
    
}

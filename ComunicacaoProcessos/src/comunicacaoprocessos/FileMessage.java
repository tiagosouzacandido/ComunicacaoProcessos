/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package comunicacaoprocessos;

import java.io.File;
import java.io.Serializable;

/**
 *
 * @author Tiago
 */
public class FileMessage implements Serializable {

    private String cliente;
    private File arquivo;
    private int tipo;
    private File arquivoCopiar;

    public FileMessage(int tipo, String cliente, File arquivo) {
        this.tipo = tipo;
        this.cliente = cliente;
        this.arquivo = arquivo;
    }

    public FileMessage(String cliente) {
        this.cliente = cliente;
    }

    public FileMessage() {
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public File getArquivo() {
        return arquivo;
    }

    public void setArquivo(File arquivo) {
        this.arquivo = arquivo;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public File getArquivoCopiar() {
        return arquivoCopiar;
    }

    public void setArquivoCopiar(File arquivoCopiar) {
        this.arquivoCopiar = arquivoCopiar;
    }
    
    
}

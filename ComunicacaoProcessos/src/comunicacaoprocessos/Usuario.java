/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package comunicacaoprocessos;

import java.io.File;
import java.util.ArrayList;

/**
 *
 * @author Tiago
 */
public class Usuario {

    String usuario;
    String enderecoIP;

    public Usuario(String usuario, String enderecoIP) {
        this.usuario = usuario.toLowerCase();
        this.enderecoIP = enderecoIP;
    }

    public void fileList() {
        ArrayList<String> lista = new ArrayList<String>();
        File pasta = new File("C:\\comunicacaoprocessos");
        File[] arquivos = pasta.listFiles();
        for (int i = 0; i < arquivos.length; i++) {
            lista.add(i, arquivos[i].toString());
        }
    }
}
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
public class Pasta {
    
    private ArrayList<String> minhaLista;
    
    public Pasta(){
        minhaLista = listaArquivos();
    }
    
    
    private ArrayList<String> listaArquivos() {
        ArrayList<String> lista = new ArrayList<>();
        File pasta = new File("C:\\comunicacaoprocessos");
        File[] arquivos = pasta.listFiles();
        for (int i = 0; i < arquivos.length; i++) {
            lista.add(i, arquivos[i].toString());
        }
        return lista;
    }
    
}

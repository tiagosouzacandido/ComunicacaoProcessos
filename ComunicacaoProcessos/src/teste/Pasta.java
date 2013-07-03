/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package teste;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

/**
 *
 * @author Tiago
 */
public class Pasta {

    private ArrayList<String> minhaLista;

    public Pasta() {
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

    public void copiarArquivos(Path origem, Path destino) throws IOException {
        // se é um diretório, tentamos criar. se já existir, não tem problema.
        if (Files.isDirectory(origem)) {
            Files.createDirectories(destino);
            // listamos todas as entradas do diretório
            DirectoryStream<Path> entradas = Files.newDirectoryStream(origem);
            for (Path entrada : entradas) {
                // para cada entrada, achamos o arquivo equivalente dentro de cada arvore
                Path novaOrigem = origem.resolve(entrada.getFileName());
                Path novoDestino = destino.resolve(entrada.getFileName());
                // invoca o metodo de maneira recursiva
                copiarArquivos(novaOrigem, novoDestino);
            }
        } else {
            // copiamos o arquivo
            Files.copy(origem, destino);
        }
    }
}

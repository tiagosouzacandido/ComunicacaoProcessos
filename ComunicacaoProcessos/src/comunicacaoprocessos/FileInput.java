/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package comunicacaoprocessos;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 *
 * @author Tiago
 */
public class FileInput {

    InputStream inputStream = null;
    BufferedReader buffered = null;

    protected void uploadArquivo(String caminhoArquivo) {
        try {
            inputStream = new FileInputStream(caminhoArquivo);
            buffered = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = buffered.readLine()) != null) {
                sb.append(line);
            }
            System.out.println(sb.toString());
            System.out.println("\nPronto!");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (buffered != null) {
                try {
                    buffered.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

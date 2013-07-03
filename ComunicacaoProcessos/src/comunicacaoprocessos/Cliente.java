/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package comunicacaoprocessos;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 *
 * @author Tiago
 */
public class Cliente {

    private Socket socket;
    private ObjectOutputStream outputStream;

    public Cliente() throws UnknownHostException, IOException {

        this.socket = new Socket("localhost", 5555);
        this.outputStream = new ObjectOutputStream(socket.getOutputStream());
        new Thread(new ListenerSocket(socket)).start();
        menu();
    }

    private void menu() throws IOException {
        Scanner scanner = new Scanner(System.in);
        String nome = JOptionPane.showInputDialog("Digite seu nome: ");
        this.outputStream.writeObject(new FileMessage(nome));

        int option = 0;
        while (option != -1) {
            System.out.print("1 - Sair | 2 - Enviar Arquivo | 3 - Copiar Arquivo | 4 - Deletar Arquivo : ");
            option = scanner.nextInt();
            if (option == 2) {              
                send(nome);
            } else if (option == 1) {
                System.exit(0);
            } else if (option == 3) {
                ArrayList<String> lista = listaArquivos();
                int opcao2 = scanner.nextInt();
                String opcaoArquivo = lista.get(opcao2 - 1);
                copy(opcaoArquivo,nome);
            } else if (option == 4){     
                ArrayList<String> lista = listaArquivos();
                int opcao2 = scanner.nextInt();
                String opcaoArquivo = lista.get(opcao2 - 1);
                delete(opcaoArquivo, nome);
            }
        }
    }

    private void send(String nome) throws IOException {
        JFileChooser fileChooser = new JFileChooser();
        int opt = fileChooser.showOpenDialog(null);
        if (opt == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            this.outputStream.writeObject(new FileMessage(1,nome, file));
        }
    }
    
    private void delete(String arquivo,String nome) throws IOException {
        //JFileChooser fileChooser = new JFileChooser();
        //int opt = fileChooser.showOpenDialog(null);
        //if (opt == JFileChooser.APPROVE_OPTION) {
            //File file = fileChooser.getSelectedFile();
            File file = new File("C:\\comunicacaoprocessos" + arquivo);
            this.outputStream.writeObject(new FileMessage(2,nome, file));
        //}
    }
    
    private void copy(String opcao,String nome) throws IOException {
        
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int opt = fileChooser.showOpenDialog(null);
        if (opt == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            //System.out.println(file.getAbsolutePath());
            FileMessage message = new FileMessage(3,nome, file);
            message.setArquivoCopiar(file);
            this.outputStream.writeObject(message);
        }
    }

    public long tamanhoDisco(File pasta) {
        long tamanho = 0;
        for (File arquivo : pasta.listFiles()) {
            if (arquivo.isFile()) {
                tamanho += arquivo.length();
            } else {
                tamanho += tamanhoDisco(arquivo);
            }
        }
        return tamanho;
    }

    private ArrayList<String> listaArquivos() {
        ArrayList<String> lista = new ArrayList<>();
        File pasta = new File("C:\\comunicacaoprocessos");
        File[] arquivos = pasta.listFiles();
        System.out.println("Escolha o arquivo: ");
        for (int i = 0; i < arquivos.length; i++) {
            lista.add(i, arquivos[i].toString());
            System.out.println((i + 1) + " - " + arquivos[i].toString());
        }
        return lista;
    }

    private class ListenerSocket implements Runnable {

        private ObjectInputStream inputStream;
        public ListenerSocket(Socket socket) throws IOException {
            this.inputStream = new ObjectInputStream(socket.getInputStream());
        }

        public void run() {
            FileMessage message = null;
            try {
                while ((message = (FileMessage) inputStream.readObject()) != null) {             
                    if (message.getTipo() == 1){
                        System.out.println("\nO disco recebeu um arquivo de " + message.getCliente());
                        System.out.println("O arquivo é " + message.getArquivo().getName());
                        salvar(message);
                    }else if(message.getTipo() == 2){
                        System.out.println("\n" + message.getCliente() + " deletou um arquivo do disco");
                        System.out.println("O arquivo deletado era: " + message.getArquivo().getName());
                        deletar(message);
                    }else if(message.getTipo() == 3){
                        copiar(message);
                    }
                    System.out.print("1 - Sair | 2 - Enviar Arquivo | 3 - Copiar Arquivo | 4 - Deletar Arquivo : ");
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        }

        private void salvar(FileMessage message) {
            try {
                FileInputStream fileInputStream = new FileInputStream(message.getArquivo());
                FileOutputStream fileOutputStream = new FileOutputStream("c:\\comunicacaoprocessos\\" + message.getArquivo().getName());
                FileChannel fcInput = fileInputStream.getChannel();
                FileChannel fcOutput = fileOutputStream.getChannel();
                long size = fcInput.size();
                fcInput.transferTo(0, size, fcOutput);
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        private void deletar(FileMessage message) {
            try {
                File file = new File("c:\\comunicacaoprocessos\\" + message.getArquivo().getName());
                file.delete();     
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        private void copiar(FileMessage message) {
            try {
                FileInputStream fileInputStream = new FileInputStream(message.getArquivo());
                FileOutputStream fileOutputStream = new FileOutputStream(message.getArquivo().getAbsolutePath() + message.getArquivoCopiar().getName());
                FileChannel fcInput = fileInputStream.getChannel();
                FileChannel fcOutput = fileOutputStream.getChannel();
                long size = fcInput.size();
                fcInput.transferTo(0, size, fcOutput);
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        try {
            new Cliente();
        } catch (UnknownHostException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}

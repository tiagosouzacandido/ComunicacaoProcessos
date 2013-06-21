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
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;

/**
 *
 * @author Tiago
 */
public class Cliente {
    
    private Socket socket;
    private ObjectOutputStream outputStream;

    public Cliente() throws UnknownHostException, IOException {
        
        this.socket = new Socket("localhost",5555);
        this.outputStream = new ObjectOutputStream(socket.getOutputStream());
        
        new Thread(new ListenerSocket(socket)).start();
    
        menu();
    }

    private void menu() throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Digite seu nome: ");
        String nome = scanner.nextLine();
        
        this.outputStream.writeObject(new FileMessage(nome));
    
        int option = 0;
                
        while(option != -1){
            System.out.print("1 - Sair | 2 - Enviar: ");
            if(option == 2){
                send(nome);
            }else if(option == 1){
                System.exit(0);
            }
            
        }
    }

    private void send(String nome) throws IOException {
        JFileChooser fileChooser = new JFileChooser();       
        int opt = fileChooser.showOpenDialog(null);
        if(opt == JFileChooser.APPROVE_OPTION){
            File file = fileChooser.getSelectedFile();     
            this.outputStream.writeObject(new FileMessage(nome,file));
        }
    }
    
    private class ListenerSocket implements Runnable{
        
        private ObjectInputStream inputStream;
        
        public ListenerSocket(Socket socket) throws IOException{
            this.inputStream = new ObjectInputStream(socket.getInputStream());
        }
        
        public void run(){
            FileMessage message = null;
            try {
                while ((message = (FileMessage) inputStream.readObject()) != null) {
                    System.out.println("\nVocê recebeu um arquivo de " + message.getCliente());
                    System.out.println("O arquivo é " + message.getArquivo().getName());
                
                    //imprime(message);
                    
                    salvar(message);
                    
                    System.out.print("1 - Sair | 2 - Enviar: ");
                }
            } catch (IOException ex) {
               ex.printStackTrace();
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        }

        private void imprime(FileMessage message) {
            try {
                FileReader fileReader = new FileReader(message.getArquivo());
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                String linha;
                
                while((linha = bufferedReader.readLine()) != null){
                    System.out.println(linha);
                }
            
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
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
    }
    
    public static void main(String[] args){
        try {
            new Cliente();
        } catch (UnknownHostException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
}

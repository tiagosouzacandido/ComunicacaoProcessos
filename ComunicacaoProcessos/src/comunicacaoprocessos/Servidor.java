/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package comunicacaoprocessos;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Tiago
 */
public class Servidor {

    private ServerSocket serverSocket;
    private Socket socket;
    private Map<String, ObjectOutputStream> streamMap = new HashMap<String, ObjectOutputStream>();

    public Servidor(ServerSocket serverSocket, Socket socket) {
        this.serverSocket = serverSocket;
        this.socket = socket;
    }

    public Servidor() {
        try {
            serverSocket = new ServerSocket(5555);
            System.out.println("Servidor ligado!");
            while (true) {
                socket = serverSocket.accept();
                new Thread(new ListenerSocket(socket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class ListenerSocket implements Runnable {

        private ObjectOutputStream outputStream;
        private ObjectInputStream inputStream;

        public ListenerSocket(Socket socket) throws IOException {
            this.outputStream = new ObjectOutputStream(socket.getOutputStream());
            this.inputStream = new ObjectInputStream(socket.getInputStream());
        }

        public void run() {
            FileMessage message = null;
            try {
                while ((message = (FileMessage) inputStream.readObject()) != null) {
                    streamMap.put(message.getCliente(), outputStream);
                    if (message.getArquivo() != null) {
                        for (Map.Entry<String, ObjectOutputStream> kv : streamMap.entrySet()) {
                            if (message.getTipo() == 1) {
                                //se eh dono da mensagem salva no PC
                                if (message.getCliente().equals(kv.getKey())) {
                                    kv.getValue().writeObject(message);
                                    //se nao eh o dono, verifica o que tem mais memoria pra salvar
                                } else if (!message.getCliente().equals(kv.getKey())) {
                                    kv.getValue().writeObject(message);
                                }
                            }else if(message.getTipo() == 2){
                                // mensagem de delete enviada para todos
                                kv.getValue().writeObject(message);
                            }else if(message.getTipo() == 3){
                                // mensagem de copiar apenas para dono
                                if (message.getCliente().equals(kv.getKey())) {
                                    kv.getValue().writeObject(message);
                                }
                            }
                            
                        }
                    }
                }
            } catch (IOException ex) {
                streamMap.remove(message.getCliente());
                System.out.println(message.getCliente() + " desconectou!");
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static void main(String[] args) {
        new Servidor();
    }
}

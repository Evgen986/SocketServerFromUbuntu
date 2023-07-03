import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

public class Server {
    public final static int PORT = 55555;
    public static List<ServerSomthing> serverList = new LinkedList<>();

    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(PORT);
        try {
            while (true){
                Socket socket = server.accept();
                try {
                    serverList.add(new ServerSomthing(socket));
                }catch (IOException e){
                    socket.close();
                }
            }
        }finally {
            server.close();
        }
    }
}

class ServerSomthing extends Thread{
    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;

    public ServerSomthing(Socket socket) throws IOException {
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        start();
    }
    public void run(){
        String word;
        try{
            while (true){
                word = in.readLine();
                if(word.equals("exit")) break;
                for (ServerSomthing vr : Server.serverList){
                    if (vr.equals(this)) vr.send("Ваше сообщение доставленно");
                    vr.send(word);
                }
            }
        }catch (IOException e){
            System.out.println("Ошибка отправки письма1");
        }
    }

    public void send(String message){
        try{
            out.write(message + "\n");
            out.flush();
        }catch (IOException e){
            System.out.println("Ошибка отправки письма2");
        }
    }
}
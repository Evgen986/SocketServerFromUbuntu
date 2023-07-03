import java.io.*;
import java.net.Socket;


public class TestClient {
    private Socket socket;
    private String ipAddress;
    private int port;
    private BufferedReader in;
    private BufferedWriter out;

    private BufferedReader inputUser;

    private String name;

    public TestClient(String ipAddress, int port){
        this.ipAddress = ipAddress;
        this.port = port;

        try {
            this.socket = new Socket(ipAddress, port);
        }catch (IOException e){
            System.out.println("Ошибка создания сокета!");
        }
        try{
            inputUser = new BufferedReader(new InputStreamReader(System.in));
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.getName();
            new Read().start();
            new Write().start();
        }catch (IOException e){
            System.out.println("Ошибка");
            this.stopSocket();
        }
    }



    public void getName(){
        System.out.print("Введите свое имя: ");
        try {
            name = inputUser.readLine();
            out.write("Привет " + name + "\n");
            out.flush();
        }catch (IOException e){
            System.out.println("Ошибка создания имени!");
        }
    }

    public void stopSocket(){
        try {
            if (!socket.isClosed()) {
                this.in.close();
                this.out.close();
                this.socket.close();
            }
        }catch (IOException e){
            System.out.println("Ошибка закрытия сокета!");
        }
    }

    private class Read extends Thread{
        @Override
        public void run() {
            String message;
            try {
                while (true) {
                    message = in.readLine();
                    if(message.equals("exit")){
                        TestClient.this.stopSocket();
                        System.out.println("Соединение завершено");
                        break;
                    }
                    System.out.println(message);
                }
            } catch (IOException e) {
                System.out.println("Ошибка чтения");
            }
        }
    }

    private class Write extends Thread{
        public void run() {
            while (true) {
                String message;
                try {
                    message = inputUser.readLine();
                    if (message.equals("exit")) {
                        TestClient.this.stopSocket();
                        System.out.println("Соединение завершено");
                        break;
                    }
                    out.write(String.format("%s написал: %s\n", name, message));
                    out.flush();
                } catch(IOException e){
                    TestClient.this.stopSocket();
                    System.out.println("Ошибка записи");
                }
            }
        }
    }
}

class RunClient{
    private static String IP = "192.168.1.106";
    private static int PORT = 55555;


    public static void main(String[] args) {
        new TestClient(IP, PORT);


    }
}
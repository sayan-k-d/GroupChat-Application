import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Server {
    static Map<String, ArrayList<ManageClients>> groups=new HashMap<>();
    static ArrayList<String> clients= new ArrayList<String>();


    public static void main(String[] args) {
        try {
                BufferedReader br= new BufferedReader(new InputStreamReader(System.in));
                ServerSocket serversocket= new ServerSocket(8055);
                Socket socket;
                while (true){           //always ready to accept clients
                    socket=serversocket.accept();
                    DataInputStream dis = new DataInputStream(socket.getInputStream());
                    DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                    ManageClients manageClients=new ManageClients(socket,dis,dos);

                    Thread t= new Thread(manageClients);
                    t.start();
                }

        }catch (Exception e) {
            System.out.println("SERVER: "+e.toString());
        }

    }

}

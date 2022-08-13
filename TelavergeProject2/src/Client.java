import java.io.*;
import java.net.Socket;


public class Client {

    public static void main(String[] args) {
        String clientName;
        String groupName;
        try {
            BufferedReader bufferedReader= new BufferedReader(new InputStreamReader(System.in));
            Socket socket= new Socket("192.168.1.2",8055);
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            System.out.println("Connected to Server");
            System.out.print("Enter Client Name: ");
            clientName=bufferedReader.readLine();            //Taking Client Name
            System.out.println("----------------------- "+clientName+" -----------------------");
            System.out.println("1. Create New Group\n2. Join New Group\n3. Exit");
            System.out.print("Enter Choice: ");
            int choice= Integer.parseInt(bufferedReader.readLine());
            switch (choice) {
                case 1 -> {
                    System.out.print("Enter Group Name: ");
                    groupName = bufferedReader.readLine();        //Taking Group Name
                    dataOutputStream.writeUTF(clientName + "," + groupName + ",1");
                }
                case 2 -> {
                    System.out.println("Available Groups: ");
                    dataOutputStream.writeUTF(clientName + "," + null + ",2");
                    int groupSize = Integer.parseInt(dataInputStream.readUTF());
                    for (int i = 0; i < groupSize; i++) {
                        System.out.println(dataInputStream.readUTF());
                    }
                    System.out.print("Enter Group Name: ");
                    groupName = bufferedReader.readLine();        //Taking Group Name
                    dataOutputStream.writeUTF(groupName);
                }
                case 3 -> {
                    System.out.println("Thank You");
                    System.exit(0);
                }
                default -> System.out.println("Wrong Input");
            }

            String finalclientName = clientName;
            Thread sendMessage= new Thread(new Runnable() {
                @Override
                public void run() {
                    System.out.println("Start Typing your Messages "+ finalclientName +"...  ");
                    while (true){
                        try {

                            String sendmsg=bufferedReader.readLine();
                            dataOutputStream.writeUTF(sendmsg);
                        } catch (Exception e) {
                            System.out.println(e.toString());
                            break;
                        }
                    }
                }
            });
            Thread recieveMessage= new Thread(new Runnable() {
                @Override
                public void run() {

                    while (true){
                        try {
                            String rcvmsg=dataInputStream.readUTF();
                            System.out.println(rcvmsg);
                        }catch (Exception e){
                            System.out.println(e.toString());
                            break;
                        }
                    }
                }
            });
            sendMessage.start();
            recieveMessage.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

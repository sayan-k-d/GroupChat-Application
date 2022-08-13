import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

import java.util.ArrayList;


public class ManageClients implements Runnable{
    Socket s;
    private String clientName;
    private String groupName;
    private final DataInputStream dataInputStream;
    private final DataOutputStream dataOutputStream;
    ManageClients(Socket s, DataInputStream dataInputStream, DataOutputStream dataOutputStream) {
        this.dataInputStream = dataInputStream;
        this.dataOutputStream=dataOutputStream;
        this.s=s;
    }


    public void clientInfo(){
        String choice;
        try {
            while(true){            //this loop will wait until it gets the infos. from clients.
                String[] clientInfos=dataInputStream.readUTF().split(",");
                clientName=clientInfos[0];
                groupName=clientInfos[1];
                choice=clientInfos[2];

                if (clientName!=null&& choice.equals("2")){
                    dataOutputStream.writeUTF(String.valueOf(Server.groups.size()));
                    for (String g:Server.groups.keySet()) {
                        dataOutputStream.writeUTF(g);
                    }
                    groupName=dataInputStream.readUTF();
                }
                if(clientName!=null && groupName!=null){
                    System.out.println("New Client ["+clientName+"] is Connected.");

                    if (Server.groups.get(groupName) == null) {
                        Server.groups.put(groupName, new ArrayList<>());
                    }
                    Server.groups.get(groupName).add(this);
                    break;
                }
            }
        }catch (Exception e){
            System.out.println(e);
        }
    }

    public void exitClient(){
        int clientIndex;
        clientIndex=Server.groups.get(groupName).indexOf(this);
        System.out.println(clientName+" : "+clientIndex);
        System.out.println("Client "+clientName+" Exited From the Group.");
        Server.groups.get(groupName).remove(clientIndex);

    }
    public  void  closeAll(){
        try {
            s.close();
            dataInputStream.close();
            dataOutputStream.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public void run() {
        String recieveMessage;
        clientInfo();
            while (true){
                try {
                    recieveMessage=dataInputStream.readUTF();
                    System.out.println(recieveMessage);
                    if(recieveMessage.equalsIgnoreCase("exit")){
                        exitClient();
                    }
                    for(int i=0;i<Server.groups.get(groupName).size();i++){
                        String otherClients=Server.groups.get(groupName).get(i).clientName;
                        if(!otherClients.equals(clientName)){
                            Server.groups.get(groupName).get(i).dataOutputStream.writeUTF(clientName.toUpperCase()+" : "+recieveMessage);
                        }
                    }
                }catch (Exception e){
                    closeAll();
                    System.out.println(e);
                    break;
                }
            }
    }
}

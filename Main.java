import java.io.*;
import java.net.*;




public class Main {

    public static void main(String[] args) {
        try 
        {
            ServerSocket serverSocket = new ServerSocket(80); //create a server socket object
            boolean isstop = false;
            while(!isstop) //while server is not stopped
            {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client " + clientSocket.getInetAddress().getHostAddress() + " is connected");//print client
                ClientThread clientThread = new ClientThread(clientSocket);//creata a new thread for each client
                clientThread.start();
            }
            
                   
        }
        catch (Exception e) 
        {
            System.out.println(e.toString());
        }
    }
    
}

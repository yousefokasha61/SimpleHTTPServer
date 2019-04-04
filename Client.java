import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    public static void main(String args[]) throws IOException {
        Socket socket = new Socket("127.0.0.1", 8080);
        Scanner userInput = new Scanner(System.in), socketInput = new Scanner(socket.getInputStream());
        PrintStream socketOutput = new PrintStream(socket.getOutputStream());
        String message;
        System.out.print("Enter HTTP Request Header: ");
        message = userInput.nextLine();
        String line;
        String RequestBody = "";
        if (message.contains("GET"))
        {
            socketOutput.print(message + "\r\n\r\n");
        }
        if (message.contains("POST")) {
            while (true) {
                line = userInput.nextLine();
                if (line.equals("FINISHED"))
                    break;
                RequestBody += line;
                RequestBody += "\r\n";
            }
            RequestBody += "\r\n\r\n";
            socketOutput.print(message + "\r\n\r\n");
            socketOutput.print(RequestBody);
        }
        else if (message.contains("DELETE"))
        {
            socketOutput.print(message + "\r\n\r\n");
        }
        while(socketInput.hasNextLine())
            System.out.println(socketInput.nextLine());

    }

}

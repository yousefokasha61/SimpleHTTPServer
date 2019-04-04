import java.io.*;
import java.net.*;
import java.util.Date;

public class SingleThread extends Thread
{
    private Socket socket;
    // Reads and prints to socket streams
    private BufferedReader in;
    private PrintWriter out;
    final static String CRLF = "\r\n";

    public SingleThread(Socket clientSocket)
    {
        this.socket = clientSocket;
    }

    public void run()
    {
        try
        {
            //create a buffer reader
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            //create a PrintWriter
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
            String line;
            String FilePath = "D:/htdocs"; // File path to the required file. NOTE: Can be changed
            String httpRequest = "";
            while (((line = in.readLine()) != null) && !line.equals("") && !line.equals(CRLF))
            {
                httpRequest += line;
                httpRequest += CRLF;
            }
            if (!httpRequest.equals("")) {
                String[] request = httpRequest.split(" ");
                System.out.println("Client HTTP Request: " + CRLF + httpRequest);
                if (request[0].equals("GET")) // if line contains get
                {
                    //extract the html filename
                    if (request[1].equals("/"))
                        request[1] = "/index.html";
                    FilePath += request[1];
                    processRequestGET(FilePath); // process the request
                }
            }


            closeConnection(); // close the socket connection and input/output buffers
        }
        catch(Exception e) //print error stack trace
        {
            e.printStackTrace();
        }
    }

    public void processRequestGET(String FilePath) throws Exception
    {
        File file = new File(FilePath); //create a file variable
        if(file.exists() && FilePath.contains(".html")) // if file exists
        {
            //create a BufferedReader to read the file content
            BufferedReader reader = new BufferedReader(new FileReader(file));

            //sent the HTTP head (HTTP 200 OK)
            out.print("HTTP/1.1 200 OK" + CRLF);
            Date date = new Date();
            out.print("Date: " + date.toString() + CRLF);
            out.print("Server: java tiny web server" + CRLF);
            out.print("Content-Type: text/html" + CRLF);
            out.print("Content-Length: " + file.length() + CRLF);
            out.println("Content-Type: text/html; charset=iso-8859-1" + CRLF);
            //end of http header

            String line = "";
            while((line = reader.readLine()) != null) //read a line from the html file
            {
                out.println(line); //write the line to the socket connection
            }
        }
        else if (!file.exists()) //if file does not exists
        {
            //sent the HTTP head (404 Not Found)
            out.print("HTTP/1.1 404 Not Found" + CRLF);
            Date date = new Date();
            out.print("Date: " + date.toString() + CRLF);
            out.print("Server: java tiny web server" + CRLF);
            out.print("Connection: close" + CRLF);
            out.println("Content-Type: text/html; charset=iso-8859-1" + CRLF);
            //end of http header

            //send file not found message
            file = new File("D:/htdocs/404.html");
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null)
            {
                out.println(line);
            }
        }
        else
        {
            //sent the HTTP head (415 Unsupported Media Type)
            out.print("HTTP/1.1 415 Unsupported Media Type" + CRLF);
            Date date = new Date();
            out.print("Date: " + date.toString() + CRLF);
            out.print("Server: java tiny web server" + CRLF);
            out.print("Connection: close" + CRLF);
            out.println("Content-Type: text/html; charset=iso-8859-1" + CRLF);
            //end of http header

            //send file not found message
            file = new File("D:/htdocs/415.html");
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null)
            {
                out.println(line);
            }

        }
    }

    private void closeConnection()
    {
        try
        {
            System.out.println("\nClient disconnected!\n");
            out.close(); // close output stream
            in.close(); // close input stream
            socket.close(); //close socket
        }
        catch(Exception e)
        {
            System.out.println(e.toString());
        }
    }
}
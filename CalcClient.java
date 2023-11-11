package socket;
import java.io.*;
import java.net.*;
import java.util.StringTokenizer;
import java.util.HashMap;
import java.util.Scanner;

public class CalcClient {
	
	private static final String CONFIG_FILE = "socketserver_info.dat";
	private static final int DEFAULT_PORT = 6666;
	private static final String DEFAULT_IP = "127.0.0.1";
	
	public enum ErrorCode {
        TOO_MANY_ARGUMENTS,
        DIVIDED_BY_ZERO,
        WRONG_EXPRESSION
    }
    public static class ErrorResponse {

        private ErrorCode errorCode;
        private String message;

        public ErrorResponse(ErrorCode errorCode, String message) {
            this.errorCode = errorCode;
            this.message = message;
        }

        public ErrorCode getErrorCode() {
            return errorCode;
        }

        public String getMessage() {
            return message;
        }
    }
    public static void main(String[] args) throws FileNotFoundException,IOException {
    	// Read server information
    	String serverIp=null;
    	int serverPort=0;
    
        try {
        	File file = new File(CONFIG_FILE);
            InputStream inputStream = new FileInputStream(file);
            Scanner scanner1 = new Scanner(inputStream);
        	serverPort = scanner1.nextInt();
            serverIp = scanner1.next();

            
        }catch(FileNotFoundException e){

    		serverIp = DEFAULT_IP;
            serverPort = DEFAULT_PORT;
    	}
        


     // Create a socket
	    Socket socket = new Socket(serverIp, serverPort);
        BufferedReader input = null;
        Scanner scanner = new Scanner(System.in);
        BufferedWriter output = null;
        

        // Create input and output streams
        input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        
        // Read the user's input
        System.out.print("식 입력: ");
        String outputMessage = scanner.nextLine();

     // Send the user's input to the server
        output.write(outputMessage+"\n");
        output.flush();
        

        // Receive the server's response
        String response = input.readLine();
        // Remove the curly braces from the response
        response = response.replaceAll("\\{", "");
        response = response.replaceAll("\\}", "");

     // Split the response into a hash map
        String[] resArr = new String[3];
        String rest= "";
        HashMap<String, Object> newMap = new HashMap<>();
        StringTokenizer st = new StringTokenizer(response,", ");
        for(int i=0;i<3;i++) {
        	resArr[i]=st.nextToken();
        	rest=resArr[i];
        	StringTokenizer st1 = new StringTokenizer(rest,"=");
        	String st2=st1.nextToken();
        	String st3 = st1.nextToken();
        	st3 = st3.replace("_", " ");
        	newMap.put(st2, st3);	
        }
        // Check if the response is an error
        if(newMap.get("hasError").toString().equals("yes")){
        	//print the error message
        	System.out.println("Error message:");
        	System.out.println(newMap.get("message"));
        }else {
        	//print the answer
        	System.out.println("Answer: "+newMap.get("message"));
        }

        // close the socket
        socket.close();
           
        
            
    	
    }
}
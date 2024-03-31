package socket;
import java.io.*;
import java.net.*;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.HashMap;

public class CalcServer {
	//set default port number
    private static final int PORT = 6666;
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
    public static String calc(String exp) {
		StringTokenizer st = new StringTokenizer(exp, " ");
		if (st.countTokens() != 3)
			return "error";
		String res = "";

		String opcode = st.nextToken();
		opcode = opcode.toUpperCase();
		int op1 = Integer.parseInt(st.nextToken());
		int op2 = Integer.parseInt(st.nextToken());
		
		switch (opcode) {
		case "ADD":
			res = Integer.toString(op1 + op2);
			break;
		case "SUB":
			res = Integer.toString(op1 - op2);
			break;
		case "MUL":
			res = Integer.toString(op1 * op2);
			break;
		case "DIV":
			if(op2==0) {
				return "error2";
			}else {
			res = Integer.toString(op1/op2);
			break;
			}
		default:
			res = "error3";
		}
		return res;
	}

    public static void main(String[] args) throws IOException {
    	// Create a server socket on port 6666
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("서버 소켓 생성");
        // Create a thread pool with 10 threads
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        // Enter an infinite loop to accept client connectio
        while (true) {
        	 // Accept a client connection
            Socket socket = serverSocket.accept();
            System.out.println("서버 연결 성공");
            // Run a thread to handle the client request
            executorService.execute(new ServerThread(socket));
        }
    }

    static class ServerThread implements Runnable {

        private Socket socket;

        public ServerThread(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
            	// Create input and output streams for communication with the client
            	String response = "";
                BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedWriter output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

                // Receive the client's request
                String request = null;
                request=input.readLine();

                // Process the request
                response = calc(request);
                
                // Send the response to the client
                if (response.equals("error")) {
                	// Create an error response object
                    ErrorResponse errorResponse = new ErrorResponse(ErrorCode.TOO_MANY_ARGUMENTS,"too_many_argument");
                    // Create a hash map to store the error response details
                    HashMap<String, Object> responses = new HashMap<>();
                    responses.put("hasError", "yes");
                    responses.put("errorCode", errorResponse.getErrorCode().name());
                    responses.put("message", errorResponse.getMessage());
                    // Send the error response to the client
                    output.write(responses.toString());
                    output.flush();
                }else if(response.equals("error2")) {
                	// Create an error response object
                	ErrorResponse errorResponse2 = new ErrorResponse(ErrorCode.DIVIDED_BY_ZERO, "divide_by_zero");
                	HashMap<String, Object> responses = new HashMap<>();
                	// Create a hash map to store the error response details
                	responses.put("hasError", "yes");
                    responses.put("errorCode", errorResponse2.getErrorCode().name());
                    responses.put("message", errorResponse2.getMessage());
                    // Send the error response to the client
                	output.write(responses.toString());
                    output.flush();
                }else if(response.equals("error3")){
                	// Create an error response object
                	ErrorResponse errorResponse3 = new ErrorResponse(ErrorCode.WRONG_EXPRESSION, "wrong_expression");
                	// Create a hash map to store the error response details
                	HashMap<String, Object> responses = new HashMap<>();
                	responses.put("hasError", "yes");
                    responses.put("errorCode", errorResponse3.getErrorCode().name());
                    responses.put("message", errorResponse3.getMessage());
                    // Send the error response to the client
                	output.write(responses.toString());
                    output.flush();
                }else {
                	// Create a hash map to store the response details
                	HashMap<String, Object> responses = new HashMap<>();
                	responses.put("hasError", "no");
                	responses.put("errorCode", "none");
                	responses.put("message", response);
                	 // Send the response to the client
                    output.write(responses.toString());
                    output.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

   
}
/*
 * Autores:
 * Thiago Rodrigues - Eng. Telecomunicações
 * Ulysses Rocha - Eng. Computação
 * 
 * Fontes de consulta:
 * Roteiro disponibilizado pelo professor da disciplina
 * http://www.tutorialspoint.com/http/http_methods.htm
 * http://www.w3.org/Protocols/rfc2616/rfc2616-sec9.html
 * http://en.wikipedia.org/wiki/Hypertext_Transfer_Protocol
 * http://en.wikipedia.org/wiki/HTTP_ETag
 * https://www.owasp.org/index.php/Testing_for_HTTP_Verb_Tampering_(OWASP-DV-003)
 * 
 * */


import java.io.* ; 
import java.net.* ; 
import java.util.Date;
import java.util.StringTokenizer;

public final class Servidor 
{ 
	 public static void main(String argv[]) throws Exception 
	 { 
		 int port = 6789; 
		
		 ServerSocket servsc = new ServerSocket(port);
		 while (true) { 
			 Socket sc = servsc.accept();
			 HttpRequest request = new HttpRequest(sc); 
			 Thread thread = new Thread(request);
			 System.out.println("Inicio");
			 thread.start(); 
		 } 
	 } 
} 
 
final class HttpRequest implements Runnable 
{
	final static String CRLF = "\r\n"; 
	 Socket socket; 
	 
	 // Constructor 
	 public HttpRequest(Socket socket) throws Exception 
	 { 
		 this.socket = socket; 
	 } 
	 
	 // Implement the run() method of the Runnable interface. 
	 public void run() 
	 { 
		 try { 
		 processRequest(); 
		 } catch (Exception e) { 
		 System.out.println(e); 
		 } 
	 } 
	 
	 private void processRequest() throws Exception 
	 { 
		 InputStreamReader is = new InputStreamReader(socket.getInputStream());
		 DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
		 BufferedReader br = new BufferedReader(is);	
		 String requestLine = br.readLine();
		 System.out.println(requestLine);
		 StringTokenizer tokens = new StringTokenizer(requestLine);
		 String acao = tokens.nextToken();

		 switch(acao)
		 {
		 	case "GET":
		 		doGet(dos,br,tokens,requestLine);
			break;
			
		 	case "POST": //meia boca
		 		doPost(dos,br,tokens,requestLine);
			break;
			
		 	case "DELETE":
		 		doDelete(dos,br,tokens,requestLine);
			break;
			
		 	case "PUT":
		 		doPut(dos,br,tokens,requestLine);
		 	break;
		 	
		 	case "HEAD":
		 		doHead(dos,br,tokens,requestLine);
		 	break;
		 	
			default:
			break;
		 }
		 
	 }
	 
	 private void doHead(DataOutputStream dos, BufferedReader br, StringTokenizer tokens, String requestLine ) throws Exception
	 {

		 	String fileName = tokens.nextToken();
			
			fileName = "./Pagina" + fileName;
			
			System.out.println(fileName);
			
			System.out.println(requestLine);
			
			String headerLine = null;
			while ((headerLine = br.readLine()).length() != 0) {
				System.out.println(headerLine);
			}
			
			// Open the requested file.
			FileInputStream fis = null;
			boolean fileExists = true;
			try {
				fis = new FileInputStream(fileName);
			} 
			catch (FileNotFoundException e) {
				fileExists = false;
			}
			
			
			// Construct the response message.
			String statusLine = null;
			String contentTypeLine = null;
			String entityBody = null;
			
			if(fileExists){
				File file = new File(fileName);
				Date x = new Date();
				x.setDate((int) file.lastModified());
				statusLine = "HTTP/1.0 200 OK"+CRLF;
				contentTypeLine =
						"Last-Modified: "+ 
						 x.toString()+CRLF+
						 "Content-Length: "+					
						 file.length()+CRLF+
						 "Content-type: " +
						 contentType( fileName ) +
						 CRLF;
			} 
			
			else {
				statusLine = "404 Not Found"+CRLF;
			
			}
			
			// Send the status line.
			dos.writeBytes(statusLine);
			//Send the content type line.
			
			Date x = new Date();
			dos.writeBytes(x.toString()+CRLF);
			dos.writeBytes("Server: Lion/0.0"+CRLF);
			dos.writeBytes(contentTypeLine);
			// Send a blank line to indicate the end of the header lines.
			dos.writeBytes(CRLF);
			
			 dos.close(); 
			 br.close(); 
			 socket.close(); 

	 }   
		 
	 private void doPost(DataOutputStream dos, BufferedReader br, StringTokenizer tokens, String requestLine) throws Exception{
			String fileName = tokens.nextToken();
			fileName = "./Pagina/" + "Dados.txt";
			System.out.println(fileName);
			System.out.println(requestLine);
			String headerLine = null;
			File arquivo = new File(fileName);

			while ((headerLine = br.readLine()).length() != 0) {
				System.out.println(headerLine);
			}
			
			if (!arquivo.exists()) {
				//cria um arquivo (vazio)
				arquivo.createNewFile();
			}
			
			do{
				headerLine =  br.readLine();
			}while (headerLine == null || headerLine=="\r\n" );

			FileWriter fw = new FileWriter(arquivo, true);
			BufferedWriter bw = new BufferedWriter(fw);
			System.out.println(headerLine);
			bw.append(headerLine);
			bw.newLine();
	
			bw.close();
			fw.close();
			 dos.close(); 
			 br.close(); 
			 socket.close(); 
	 
	 }
	 
	 private void doPut(DataOutputStream dos, BufferedReader br, StringTokenizer tokens, String requestLine) throws Exception{
		 	String fileName = tokens.nextToken();
			fileName = "./Pagina" + fileName;
			System.out.println(fileName);
			System.out.println(requestLine);
			String headerLine = null;
			File arquivo = new File(fileName);
			String statusLine = null;
			String contentTypeLine = null;
			String entityBody = null;
			
			while ((headerLine = br.readLine()).length() != 0) {
				System.out.println(headerLine);
			}
			
			if (!arquivo.exists()) {
				arquivo.createNewFile();
				statusLine = "HTTP/1.0 201 CREATED"+CRLF;
			}
			else{		
				statusLine = "HTTP/1.0 200 OK"+CRLF;
				arquivo.delete();
				arquivo.createNewFile();
			}
			
			FileWriter fw = new FileWriter(arquivo, true);
			BufferedWriter bw = new BufferedWriter(fw);
		
			while ((headerLine = br.readLine()).length() != 0) {
				System.out.println(headerLine);
				bw.append(headerLine);
				bw.newLine();
			}
			bw.close();
			fw.close();
			dos.writeBytes(statusLine);
		
						
			 dos.close(); 
			 br.close(); 
			 socket.close();  


		 
	 }
	 
	 private void doDelete(DataOutputStream dos, BufferedReader br, StringTokenizer tokens, String requestLine) throws Exception
	 {
		 	String fileName = tokens.nextToken();
			fileName = "./Pagina" + fileName;
			System.out.println(fileName);
			String statusLine = null;
			
			System.out.println(requestLine);
			// Open the requested file.
			File f = new File(fileName);
			if(f.exists()){
				f.delete();	
				statusLine = "HTTP/1.0 200 OK"+CRLF;
			}
			else{
				statusLine = "HTTP/1.0 204  NO CONTENT"+CRLF;
			}
				
			try {
				dos.writeBytes(statusLine);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			 dos.close(); 
			 br.close(); 
			 socket.close(); 
		
	 }
	  
	 private void doGet(DataOutputStream dos, BufferedReader br, StringTokenizer tokens, String requestLine ) throws Exception
	 {

		 	String fileName = tokens.nextToken();
			
			fileName = "./Pagina" + fileName;
			
			System.out.println(fileName);
			
			System.out.println(requestLine);
			
			String headerLine = null;
			while ((headerLine = br.readLine()).length() != 0) {
				System.out.println(headerLine);
			}
			
			// Open the requested file.
			FileInputStream fis = null;
			boolean fileExists = true;
			try {
				fis = new FileInputStream(fileName);
			} 
			catch (FileNotFoundException e) {
				fileExists = false;
			}
			
			
			// Construct the response message.
			String statusLine = null;
			String contentTypeLine = null;
			String entityBody = null;
			
			if(fileExists){
				File file = new File(fileName);
				Date x = new Date();
				x.setDate((int) file.lastModified());
				statusLine = "HTTP/1.0 200 OK"+CRLF;
				contentTypeLine =
						"Last-Modified: "+ 
						 x.toString()+CRLF+
						 "Content-Length: "+					
						 file.length()+CRLF+
						 "Content-type: " +
						 contentType( fileName ) +
						 CRLF;
			} 
			
			else {
				statusLine = "404 Not Found"+CRLF;
				contentTypeLine = "Content-type: index/html " + CRLF;
				entityBody = "<HTML>" +
							 "<HEAD><TITLE>Not Found</TITLE></HEAD>" +
							 "<BODY>Not Found</BODY></HTML>";
			}
			
			// Send the status line.
			dos.writeBytes(statusLine);
			//Send the content type line.
			
			Date x = new Date();
			dos.writeBytes(x.toString()+CRLF);
			dos.writeBytes("Server: Lion/0.0"+CRLF);
			dos.writeBytes(contentTypeLine);
			// Send a blank line to indicate the end of the header lines.
			dos.writeBytes(CRLF);
			
			// Send the entity body.
			if (fileExists) {
				sendBytes(fis, dos);
				fis.close();
				} 
				else {
					dos.writeBytes(entityBody);
				}

			 dos.close(); 
			 br.close(); 
			 socket.close(); 

	 }   
	
	 private static String contentType(String fileName)
		{
			if(fileName.endsWith(".htm") || fileName.endsWith(".html")) {
				return "text/html";
			}
			
			if(fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
				return "image/jpeg";
			}
			
			if(fileName.endsWith(".png") || fileName.endsWith(".jpeg")) {
				return "image/png";
			}

			return "application/octet-stream";
			
		}
	 
	 private static void sendBytes(FileInputStream fis, OutputStream os) 
			 throws Exception { 
			  // Construct a 1K buffer to hold bytes on their way to the socket. 
			  byte[] buffer = new byte[1024]; 
			  int bytes = 0; 
			  
			  // Copy requested file into the socket's output stream. 
			  while((bytes = fis.read(buffer)) != -1 ) { 
				  os.write(buffer, 0, bytes); 
			  } 
			 } 

	 
} 

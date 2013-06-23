package com.oppo.transfer.core.socket;


import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class FileReceiver {

    static final String confirmationStamp = "OK";
    private Socket socket;
    private InputStream inputStreamOnSocket;
    private BufferedReader socketReader;
    private PrintWriter socketWriter;

    public FileReceiver(Socket socket) {
        this.socket = socket;//new Socket(serverIPAddress, communicationPort);
        try {
			inputStreamOnSocket = socket.getInputStream();

	        socketReader = new BufferedReader(new InputStreamReader(inputStreamOnSocket));
	        socketWriter = new PrintWriter(socket.getOutputStream(), true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    static final String newFileDestination = "/sdcard/Storage/";
    public void downloadFile(){
    	downloadFile("");
    }
    
    public void downloadFile(String str){//String newFileDestination) {
        long filesize;
        String filename;
		try {
			filename = getFileNameFromServer();
			filesize = getFileLengthFromServer();
	
	        byte[] fileBytes = readFileFromServer(filesize);
	        saveToFile(fileBytes, newFileDestination+str+"/"+filename);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    private void saveToFile(byte[] fileBytes, String newFileDestination) throws FileNotFoundException, IOException {
        BufferedOutputStream fileWriter = new BufferedOutputStream(new FileOutputStream(newFileDestination));
        fileWriter.write(fileBytes, 0, fileBytes.length);
        fileWriter.flush();
        fileWriter.close();
    }
    
    private String getFileNameFromServer() throws IOException {
        return readFileName();
    }
    
    private long getFileLengthFromServer() throws IOException {
        return Long.parseLong(readFileLength());
    }
    
    private String readFileName() throws IOException {
        String serializedFileName = socketReader.readLine();
        sendTransferConfirmation();
        return serializedFileName;
    }

    private String readFileLength() throws IOException {
        String serializedFileLength = socketReader.readLine();
        sendTransferConfirmation();
        return serializedFileLength;
    }

    private byte[] readFileFromServer(long fileLength) throws IOException {
        byte[] fileBytes = new byte[(int) fileLength];
        readAllFileBytes(fileBytes, fileLength);
        sendTransferConfirmation();
        return fileBytes;
    }

    private void readAllFileBytes(byte[] fileBytes, long fileLength) throws IOException {
        int numberOfBytesRead = inputStreamOnSocket.read(fileBytes, 0, fileBytes.length);
        int numberOfBytesReadSoFar = numberOfBytesRead;

        do {
            numberOfBytesRead = inputStreamOnSocket.read(fileBytes, numberOfBytesReadSoFar, fileBytes.length - numberOfBytesReadSoFar);
            if (numberOfBytesRead >= 0) {
                numberOfBytesReadSoFar += numberOfBytesRead;
            }
        } while (numberOfBytesRead > 0);

        if (numberOfBytesReadSoFar != fileLength) {
            //throw new WrongFileLengthException("File length do not match the length sent from the server");
        }
    }

    private void sendTransferConfirmation() {
        socketWriter.println(confirmationStamp);
    }
    
    
    public String getMsg() throws IOException {
    	System.out.println("receive msg");
        String msg = socketReader.readLine();
        System.out.println("receivemsg:"+msg);
        sendTransferConfirmation();
        System.out.println("receivemsg:ok");
        return msg;
    }
    
    public void sendMsg(String msg){
    	socketWriter.println(msg);
    	try {
			validateTransferConfirmation(socketReader.readLine());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
    
 
    
    private void validateTransferConfirmation(String receivedLine) throws IOException {
        if (!receivedLine.equals(confirmationStamp)) {
            throw new IOException("Transfer confirmation not received.");
        }
    }
    
}
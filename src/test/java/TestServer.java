import junit.framework.TestCase;
import org.junit.Test;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class TestServer extends TestCase{
    private Server server;
    private static boolean isStarted = false;

    @Override
    protected void setUp() throws IOException{
        if(!isStarted) {
            server = new Server("localhost", 5678);
            isStarted = true;
        }
    }

    @Test
    public void testTopic() throws IOException{
        Socket socket = new Socket("localhost", 5678);
        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
        dataOutputStream.writeByte(1);
        dataOutputStream.writeUTF("message");
        dataOutputStream.flush();
        dataOutputStream.writeByte(4);
        dataOutputStream.writeInt(1003);
        dataOutputStream.flush();
        dataOutputStream.writeByte(-1);
        dataOutputStream.flush();
        dataOutputStream.close();
        socket.close();

        ServerSocket input = new ServerSocket(1003,0, InetAddress.getByName("localhost"));
        boolean getResult = true;
        ArrayList<String> listOfTopics = new ArrayList<>();
        while (getResult){
            socket = input.accept();
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            int size = dataInputStream.readInt();
            for(int i = 0;i < size; i++){
                listOfTopics.add(dataInputStream.readUTF());
            }
            socket.close();
            getResult = false;
        }
        input.close();
        ArrayList<String> correctList = new ArrayList<>();
        correctList.add("message");
        assertEquals(correctList,listOfTopics);
    }

    @Test
    public void testSubscribe() throws IOException{

        Socket socket = new Socket("localhost", 5678);
        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
        dataOutputStream.writeByte(1);
        dataOutputStream.writeUTF("message");
        dataOutputStream.flush();
        dataOutputStream.writeByte(3);
        dataOutputStream.writeUTF("message");
        dataOutputStream.writeInt(1004);
        dataOutputStream.flush();
        dataOutputStream.writeByte(2);
        dataOutputStream.writeUTF("message");
        dataOutputStream.writeUTF("TEXT");
        dataOutputStream.flush();
        dataOutputStream.writeByte(-1);
        dataOutputStream.flush();
        socket.close();
        ServerSocket input = new ServerSocket(1004,0, InetAddress.getByName("localhost"));
        boolean getResult = true;
        String message = null;
        while (getResult){
            socket = input.accept();
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            message = dataInputStream.readUTF();
            socket.close();
            getResult = false;
        }
        String correctMessage = "TEXT";
        assertEquals(message,correctMessage);
    }

}

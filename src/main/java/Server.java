import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;

class Server extends Thread{
    private ServerSocket socket;
    private String host;
    private final ConcurrentHashMap<String, HashSet<Integer>> news;
    public Server(String host, int port) throws IOException{
        socket = new ServerSocket(port,0, InetAddress.getByName(host));
        news = new ConcurrentHashMap<>();
        this.host = host;
        start();
    }
    public void run(){
        try {
            while (true){
                new Worker(socket.accept(),host, news);
            }
        } catch (IOException e){
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

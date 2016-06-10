import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;

class Worker extends Thread{
    private ConcurrentHashMap<String, HashSet<Integer>> news;
    private String host;

    public Worker(Socket socket, String host, ConcurrentHashMap<String, HashSet<Integer>> news){
        try {
            this.news = news;
            this.host = host;

            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            boolean end = false;
            while (!end){
                byte messageType = dataInputStream.readByte();

                switch (messageType){
                    case 1: //add topic
                        addTopic(dataInputStream);
                        break;
                    case 2: //post statue to topic
                        postStatue(dataInputStream);
                        break;
                    case 3://subscribe to topic
                        subscribe(dataInputStream);
                        break;
                    case 4: // get all topics
                        getAllTopics(dataInputStream);
                        break;
                    default:
                        dataInputStream.close();
                        end = true;
                }
            }
        } catch (IOException e){
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e){
                e.printStackTrace();
            }
        }

    }

    private void getAllTopics(DataInputStream dataInputStream){
        Integer port = null;
        try {
            port = dataInputStream.readInt();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            Socket sendSocket = new Socket(host, port);
            DataOutputStream dataOutputStream = new DataOutputStream(sendSocket.getOutputStream());
            dataOutputStream.writeInt(news.keySet().size());
            news.keySet().forEach((key) -> {
                try {
                    dataOutputStream.writeUTF(key);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            sendSocket.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    private void subscribe(DataInputStream dataInputStream){
        String topic1 = null;
        Integer port = null;
        try {
            topic1 = dataInputStream.readUTF();
            port = dataInputStream.readInt();
        } catch (IOException e) {
            e.printStackTrace();
        }

        news.get(topic1).add(port);
    }

    private void addTopic(DataInputStream dataInputStream){
        String newTopic = null;
        try {
            newTopic = dataInputStream.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(!news.containsKey(newTopic)){
            news.put(newTopic, new HashSet<>());
        }
    }

    private void postStatue(DataInputStream dataInputStream){
        String topic = null, text = null;
        try {
            topic = dataInputStream.readUTF();
            text  = dataInputStream.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
        }

        HashSet<Integer> map = new HashSet<>(news.get(topic));
        final String finalText = text;
        map.forEach((port) ->{
            try {
                Socket sendSocket = new Socket(host, port);
                DataOutputStream dataOutputStream = new DataOutputStream(sendSocket.getOutputStream());
                dataOutputStream.writeUTF(finalText);
                dataOutputStream.flush();
                sendSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}

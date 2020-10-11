import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.function.Consumer;


public class Client extends Thread{

    GameInfo client = new GameInfo();
    boolean listViewSet = false;
    Socket socketClient;
    ObjectOutputStream out;
    ObjectInputStream in;
    int port;
    String address;
    private Consumer<Serializable> callback;

    Client(Consumer<Serializable> call, int port, String address){
        callback = call;
        this.port = port;
        this.address = address;
    }

    public void run() {

        try {
            socketClient= new Socket(address,port);
            out = new ObjectOutputStream(socketClient.getOutputStream());
            in = new ObjectInputStream(socketClient.getInputStream());
            socketClient.setTcpNoDelay(true);
        }
        catch(Exception e) {}

        while(true) {

            try {

                // Reads in the gameInfo sent from the server
                GameInfo data = (GameInfo) in.readObject();

                if ((data.rejected == true) && (client.requested == true)) {
                    client.requested = false;
                    client.rejected = true;
                    client.rejectedMessage = data.rejectedMessage;
                }

                if (client.idSet == false) {
                    client.setId(data.getId());
                    client.idSet = true;
                }

                if (data.challenging == true) {
                    if (client.getId() == data.getId()) {
                        client.gameAddedto_gamesArray = data.gameAddedto_gamesArray;
                        client.gameId = data.gameId;
                    }
                    if (client.getId() == data.challegeId) {
                        client.gameAddedto_gamesArray = data.gameAddedto_gamesArray;
                        client.gameId = data.gameId;
                        client.isChallenged = true;
                        client.challegeId = data.challegeId;
                        callback.accept(data);
                    }
                }

                if (data.gameId == client.gameId) {
                    // Checks if id is 1 meaning if its client 1
                    if ((data.getId() == client.challegeId)) {
                        // Sets the variables that was taken from server
                        client.setP1Plays(data.getP1Plays());
                        client.setP2Plays(data.getP2Plays());
                        client.setWhoWon(data.getWhoWon());
                        client.setP1Points(data.getP1Points());
                        client.setP2Points(data.getP2Points());
                    }

                    // Checks if id is 2 meaning if its client 2
                    if ((data.getId() != client.challegeId)) {
                        // Sets the variables that was taken from server
                        client.setP1Plays(data.getP1Plays());
                        client.setP2Plays(data.getP2Plays());
                        client.setWhoWon(data.getWhoWon());
                        client.setP1Points(data.getP1Points());
                        client.setP2Points(data.getP2Points());
                    }
                }

                if (listViewSet == false) {
                    listViewSet = true;
                    for (int i = 0; i < data.clientCount.size(); i++) {
                        client.clientCount = data.clientCount;
                        callback.accept("Client: " + data.clientCount.get(i));
                    }
                } else {
                    for (int i = data.clientCount.size() - 1; i < data.clientCount.size(); i++) {
                        client.clientCount = (data.clientCount);
                        callback.accept("Client: " + data.clientCount.get(i));
                    }
                }

            }
            catch(Exception e) {}
        }

    }

    public void send(Serializable data) {

        try {
            out.writeObject(data);
            out.reset();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}

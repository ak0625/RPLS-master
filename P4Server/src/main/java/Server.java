import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.function.Consumer;

import javafx.application.Platform;
import javafx.scene.control.ListView;

public class Server {

//    int tempCount = 0;
    int gameCount = 0;
    ArrayList<GameInfo> games= new ArrayList<GameInfo>();
    int count = 1;
    ArrayList<ClientThread> clients = new ArrayList<ClientThread>();
    TheServer server;
    private Consumer<Serializable> callback;
    int port;
    GameInfo client1GM = new GameInfo();

    Server(Consumer<Serializable> call, int port){
        this.port = port;
        callback = call;
        server = new TheServer();
        server.start();
    }

    public class TheServer extends Thread{

        public void run() {

            try(ServerSocket mysocket = new ServerSocket(port);){
                System.out.println("Port is: " + port);
                System.out.println("Server is waiting for a client!");
                while(true) {

                    ClientThread c = new ClientThread(mysocket.accept(), count);
                    callback.accept("client has connected to server: " + "client #" + count + "             Connection: " + count);
                    clients.add(c);
                    c.start();
                    count++;
                }
            }//end of try
            catch(Exception e) {
                callback.accept("Server socket did not launch");
            }
        }//end of while
    }


    class ClientThread extends Thread{

        Socket connection;
        int count;
        ObjectInputStream in;
        ObjectOutputStream out;
        int myId;

        ClientThread(Socket s, int count){
            this.connection = s;
            this.count = count;
        }

        // Updates the client with the gameInfo object
        public void updateClients(GameInfo data) {
            for(int i = 0; i < clients.size(); i++) {
                ClientThread t = clients.get(i);
                try {
                    t.out.writeObject(data);
                    t.out.reset();
                }
                catch(Exception e) {}
            }
        }

        public void run(){

            try {
                in = new ObjectInputStream(connection.getInputStream());
                out = new ObjectOutputStream(connection.getOutputStream());
                connection.setTcpNoDelay(true);
            }
            catch(Exception e) {
                System.out.println("Streams not open");
            }

            while(true) {
                try {
                    // Sets the id when client joins and send back the id
                    client1GM.setId(count);
                    myId = count;
                    if(client1GM.challenging == false) {
                        client1GM.clientCount.add(client1GM.getId());
                    }

                    updateClients(client1GM);

                    // reads the gameInfo that is being sent from the client
                    GameInfo data = (GameInfo) in.readObject();

                    // Goes through the entire games arraylist and assigns it to client1gm
                    for (int i = 0; i < games.size(); i++){
                        if(data.gameId == games.get(i).gameId){
                            client1GM = games.get(i);
                        }
                    }

                    // Adds the two clinets into the games arraylist
                    if ((data.challenging == true)&&(data.gameAddedto_gamesArray == false)){
                        data.gameAddedto_gamesArray = true;
                        data.gameId = ++gameCount;
                        games.add(data);
                        client1GM = data;
                        client1GM.movePicked = data.movePicked;
                        updateClients(client1GM);
                    }

                    // Checks if client is in game already and if in game then update client with reject message
                    for (int i = 0; i < games.size(); i++){
                        if (((data.challegeId == games.get(i).getId()) || (data.challegeId == games.get(i).challegeId)) && (data.requested == true)){
                            client1GM.rejected = true;
                            client1GM.rejectedMessage = "Client in-game! Try Different Client!";
                            updateClients(client1GM);
                        }
                    }

                    // When id is 1 it sets the plays and points to the server gameInfo
                    if ((data.getId() == myId) && (data.movePicked == true)){
                        client1GM.tempCount++;
                        // Sets p2 plays when its same
                        if (data.getId() == data.challegeId){
                            client1GM.setP2Plays(data.p2Plays);
                            client1GM.setP2Points(data.p2Points);
                            callback.accept("client: " + count + " sent: " + client1GM.getP2Plays());
                        }
                        // Sets p1 plays when its same
                        if (data.getId() != data.challegeId){
                            client1GM.setP1Plays(data.p1Plays);
                            client1GM.setP1Points(data.p1Points);
                            callback.accept("client: " + count + " sent: " + client1GM.getP1Plays());
                        }
                    }

                    // Once both the clients have chosen it determines the winner and sends back the entire gameInfo to the clients
                    if (((client1GM.tempCount % 2) == 0) && (client1GM.tempCount != 0)){
                        String winner = whoWon();
                        client1GM.setWhoWon(winner);
                        callback.accept(client1GM.getWhoWon());
                        updateClients(client1GM);
                        // Removes the game once it evalutes the winner
                        for (int i = 0; i < games.size(); i++){
                            if (client1GM.gameId == games.get(i).gameId){
                                games.remove(i);
                            }
                        }
                    }
                }

                catch(Exception e) {
                    callback.accept("OOOOPPs...Something wrong with the socket from client: " + count + "....closing down!");
                    for (int i = 0; i < client1GM.clientCount.size(); i++){
                        if (client1GM.clientCount.get(i) == myId){
                            client1GM.clientCount.remove(i);
                        }
                    }
                    System.out.println("New list is: " + client1GM.clientCount);
                    updateClients(client1GM);
                    clients.remove(this);
                    break;
                }
            }
        }//end of run

        // Used to determine who the winner is based on the move chosen and returns a string
        public String whoWon(){
            System.out.println(client1GM.getP1Plays());
            System.out.println(client1GM.getP2Plays());
            if (client1GM.getP1Plays().equals("rock")){
                if ((client1GM.getP2Plays().equals("lizard")) || (client1GM.getP2Plays().equals("scissor"))){
                    client1GM.setP1Points(client1GM.getP1Points() + 1);
                    return "Player 1 Wins!";
                }
                if (client1GM.getP2Plays().equals("rock")){
                    return "Game is Tie!";
                }
                else{
                    client1GM.setP2Points(client1GM.getP2Points() + 1);
                    return "Player 2 Wins!";
                }
            }
            if (client1GM.getP1Plays().equals("paper")){
                if ((client1GM.getP2Plays().equals("rock")) || (client1GM.getP2Plays().equals("spock"))){
                    client1GM.setP1Points(client1GM.getP1Points() + 1);
                    return "Player 1 Wins!";
                }
                if (client1GM.getP2Plays().equals("paper")){
                    return "Game is Tie!";
                }
                else{
                    client1GM.setP2Points(client1GM.getP2Points() + 1);
                    return "Player 2 Wins!";
                }
            }
            if (client1GM.getP1Plays().equals("scissor")){
                if ((client1GM.getP2Plays().equals("paper")) || (client1GM.getP2Plays().equals("lizard"))){
                    client1GM.setP1Points(client1GM.getP1Points() + 1);
                    return "Player 1 Wins!";
                }
                if (client1GM.getP2Plays().equals("scissor")){
                    return "Game is Tie!";
                }
                else{
                    client1GM.setP2Points(client1GM.getP2Points() + 1);
                    return "Player 2 Wins!";
                }
            }
            if (client1GM.getP1Plays().equals("lizard")){
                if ((client1GM.getP2Plays().equals("paper")) || (client1GM.getP2Plays().equals("spock"))){
                    client1GM.setP1Points(client1GM.getP1Points() + 1);
                    return "Player 1 Wins!";
                }
                if (client1GM.getP2Plays().equals("lizard")){
                    return "Game is Tie!";
                }
                else{
                    client1GM.setP2Points(client1GM.getP2Points() + 1);
                    return "Player 2 Wins!";
                }
            }
            if (client1GM.getP1Plays().equals("spock")){
                if ((client1GM.getP2Plays().equals("rock")) || (client1GM.getP2Plays().equals("scissor"))){
                    client1GM.setP1Points(client1GM.getP1Points() + 1);
                    return "Player 1 Wins!";
                }
                if (client1GM.getP2Plays().equals("spock")){
                    return "Game is Tie!";
                }
                else{
                    client1GM.setP2Points(client1GM.getP2Points() + 1);
                    return "Player 2 Wins!";
                }
            }
            return "";
        }
    }//end of client thread

}

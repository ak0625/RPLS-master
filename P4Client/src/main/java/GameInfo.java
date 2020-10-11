import java.io.Serializable;
import java.util.ArrayList;

public class GameInfo implements Serializable {

    int p1Points;
    int p2Points;
    int id;
    int tempCount = 0;
    String p1Plays;
    String p2Plays;
    String rejectedMessage;
    boolean rejected;
    boolean requested;
    Boolean have2players;
    String whoWon;
    Boolean idSet = false;
    ArrayList<Integer> clientCount = new ArrayList<>();
    ArrayList<Integer> inGameClients = new ArrayList<>();
    boolean gameAddedto_gamesArray = false;
    int challegeId;
    boolean challenging;
    int gameId;
    boolean isChallenged;
    boolean movePicked;
    boolean inListView;

    public String getWhoWon() {
        return whoWon;
    }

    public void setWhoWon(String whoWon) {
        this.whoWon = whoWon;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    ///////////////////////////////////////////////
    public int getP1Points() {
        return p1Points;
    }
    public void setP1Points(int p1Points) {
        this.p1Points = p1Points;
    }
///////////////////////////////////////////////
    public int getP2Points() {
        return p2Points;
    }
    public void setP2Points(int p2Points) {
        this.p2Points = p2Points;
    }
///////////////////////////////////////////////
    public String getP1Plays() {
        return p1Plays;
    }
    public void setP1Plays(String p1Plays) {
        this.p1Plays = p1Plays;
    }
///////////////////////////////////////////////
    public String getP2Plays() {
        return p2Plays;
    }
    public void setP2Plays(String p2Plays) {
        this.p2Plays = p2Plays;
    }
///////////////////////////////////////////////
    public Boolean getHave2players() {
        return have2players;
    }
    public void setHave2players(Boolean have2players) {
        this.have2players = have2players;
    }

}

import java.awt.*;
import java.util.HashMap;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.animation.PauseTransition;
import javafx.util.Duration;
import javafx.scene.text.Font;
import javafx.scene.control.TextArea;
import javafx.scene.paint.Color;

public class RPLS extends Application{
    PauseTransition pause1 = new PauseTransition(Duration.seconds(3));
    PauseTransition pause2 = new PauseTransition(Duration.seconds(2));

    Boolean gameStart = false;

    Button challengeBtn = new Button();
    TextField s1,s2, pauseStatement;
    Font stmntFont = Font.font("ALGERIAN", 12);
    Button clientChoice;
    Client clientConnection;
    ListView<String> listItems2;
    ListView<Integer> clientlistItems = new ListView<Integer>();
    ListView<Integer> inGamelistItems = new ListView<Integer>();
    int port;
    Button b1 = new Button();
    Button b2 = new Button();
    Button b3 = new Button();
    Button b4 = new Button();
    Button b5 = new Button();

    VBox leftSide;
    VBox rightSide;

    TextField t3 = new TextField("Player 1 Score: ");
    TextField t4 = new TextField("Player 2 Score: ");
    TextField textArea = new TextField("Choose One Image for your Move");
    TextField clientTeller = new TextField("I am Client ");

    TextArea gameText = new TextArea("Challenge by selecting one of the clients and press the button in center");

    MenuBar menu = new MenuBar();

    Button player1Pic = new Button();
    Button player2Pic = new Button();

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // TODO Auto-generated method stub
        primaryStage.setTitle("This is client");

        // Menu bar for Third Scene
        Menu mOne = new Menu("Option");
        MenuItem mTwo = new MenuItem("Replay");
        MenuItem mThree = new MenuItem("Quit");
        mOne.getItems().addAll(mTwo, mThree);
        menu.getMenus().addAll((mOne));

        // Menu bar for Second Scene
        MenuBar menu = new MenuBar();
        Menu m1 = new Menu("Options");
        MenuItem i2 = new MenuItem("Restart");
        MenuItem i3 = new MenuItem("Quit");
        m1.getItems().addAll(i2, i3);
        menu.getMenus().addAll((m1));

        s1 = new TextField("Enter Port Number");
        s2 = new TextField("Enter IP Address");
        pauseStatement = new TextField("CLICK THE BUTTON ON THE RIGHT TO REVEAL YOUR OPPONENT'S MOVE");
        pauseStatement.setAlignment(Pos.CENTER);
        pauseStatement.setFont(stmntFont);
        pauseStatement.setVisible(false);
        s1.setMaxWidth(135);
        s2.setMaxWidth(130);
        player1Pic.setStyle("-fx-background-color: coral;");
        player2Pic.setStyle("-fx-background-color: coral;");
        clientChoice = new Button("Connect");

        // This is when the client enters port and address and presses connect
        // Change the scene and check for input
        clientChoice.setOnAction(e-> {primaryStage.setScene(clientGUIfirstScene());
            primaryStage.setTitle("This is a client");
            port = Integer.parseInt(s1.getText());
            System.out.println("Port Number is: " + port);
            System.out.println("Address is: " + s2.getText());
            clientConnection = new Client(data->{
                Platform.runLater(()->{
                    // Lets each client know what number they are
                    clientTeller.setText("I am Client " + clientConnection.client.getId());

                    // Displays message if a challenege cilent is in game
                    if (clientConnection.client.rejected == true){
                        gameText.setText(clientConnection.client.rejectedMessage);
                    }

                    // Once challenged it changes the scene for the client who gets challenged
                    if ((clientConnection.client.isChallenged == true) && (gameStart == false)){
                        gameStart = true;
                        primaryStage.setScene(createClientGui());
                    }

                    // Clears list view and then re-populate it with the amount of clients on server
                    clientlistItems.getItems().clear();
                    for (int i = 0; i < clientConnection.client.clientCount.size(); i++) {
                        clientlistItems.getItems().add(clientConnection.client.clientCount.get(i));
                    }
                    // Clears list view and then re-populate it with the amount of clients in game
                    inGamelistItems.getItems().clear();
                    for (int i = 0; i < clientConnection.client.inGameClients.size(); i++) {
                        inGamelistItems.getItems().add(clientConnection.client.inGameClients.get(i));
                    }

                });
            }, port, s2.getText());
            clientConnection.start();
        });

        // Rock picture if clicked
        b1.setOnAction(e -> {
            primaryStage.setScene(createClientThirdGui());
            primaryStage.setTitle("This is third GUI for client");
            pauseStatement.setText("CLICK THE BUTTON ON THE RIGHT TO REVEAL YOUR OPPONENT'S MOVE");
            Image pic1 = new Image("rock.png");
            ImageView v1 = new ImageView(pic1);
            v1.setFitHeight(100);
            v1.setFitWidth(100);
            v1.setPreserveRatio(true);
            player1Pic.setGraphic(v1);
            Image rB = new Image("button.png");
            ImageView rBtn = new ImageView(rB);
            rBtn.setFitHeight(100);
            rBtn.setFitWidth(100);
            rBtn.setPreserveRatio(true);
            player2Pic.setGraphic(rBtn);
            clientConnection.client.movePicked = true;
            if ((clientConnection.client.getId() != clientConnection.client.challegeId)) {
                clientConnection.client.setP1Plays("rock");
                clientConnection.send(clientConnection.client);
            }
            if((clientConnection.client.getId() == clientConnection.client.challegeId)){
                clientConnection.client.setP2Plays("rock");
                clientConnection.send(clientConnection.client);
            }
            pause1.play();
            pause1.setOnFinished(event -> {
                pauseStatement.setVisible(true);
            });
        });

        // paper picture pressed
        b2.setOnAction(e -> {primaryStage.setScene(createClientThirdGui());
            primaryStage.setTitle("This is third GUI for client");
            pauseStatement.setText("CLICK THE BUTTON ON THE RIGHT TO REVEAL YOUR OPPONENT'S MOVE");
            Image pic2 = new Image("Paper.png");
            ImageView v2 = new ImageView(pic2);
            v2.setFitHeight(100);
            v2.setFitWidth(100);
            v2.setPreserveRatio(true);
            player1Pic.setGraphic(v2);
            Image rB = new Image("button.png");
            ImageView rBtn = new ImageView(rB);
            rBtn.setFitHeight(100);
            rBtn.setFitWidth(100);
            rBtn.setPreserveRatio(true);
            player2Pic.setGraphic(rBtn);
            clientConnection.client.movePicked = true;
            if ((clientConnection.client.getId() != clientConnection.client.challegeId)) {
                clientConnection.client.setP1Plays("paper");
                clientConnection.send(clientConnection.client);
            }
            if ((clientConnection.client.getId() == clientConnection.client.challegeId)) {
                clientConnection.client.setP2Plays("paper");
                clientConnection.send(clientConnection.client);
            }
            pause1.play();
            pause1.setOnFinished(event -> {
                pauseStatement.setVisible(true);
            });
        });

        // scissor picture pressed
        b3.setOnAction(e -> {primaryStage.setScene(createClientThirdGui());
            primaryStage.setTitle("This is third GUI for client");
            pauseStatement.setText("CLICK BUTTON ON RIGHT TO REVEAL YOUR OPPONENT'S MOVE");
            Image pic3 = new Image("Scissor.png");
            ImageView v3 = new ImageView(pic3);
            v3.setFitHeight(100);
            v3.setFitWidth(100);
//        v3.setPreserveRatio(true);
            player1Pic.setGraphic(v3);
            Image rB = new Image("button.png");
            ImageView rBtn = new ImageView(rB);
            rBtn.setFitHeight(100);
            rBtn.setFitWidth(100);
            rBtn.setPreserveRatio(true);
            player2Pic.setGraphic(rBtn);
            clientConnection.client.movePicked = true;
            if ((clientConnection.client.getId() != clientConnection.client.challegeId)) {
                clientConnection.client.setP1Plays("scissor");
                clientConnection.send(clientConnection.client);
            }
            if ((clientConnection.client.getId() == clientConnection.client.challegeId)) {
                clientConnection.client.setP2Plays("scissor");
                clientConnection.send(clientConnection.client);
            }
            pause1.play();
            pause1.setOnFinished(event -> {
                pauseStatement.setVisible(true);
            });
        });

        // lizard picture pressed
        b4.setOnAction(e -> {primaryStage.setScene(createClientThirdGui());
            primaryStage.setTitle("This is third GUI for client");
            pauseStatement.setText("CLICK BUTTON ON RIGHT TO REVEAL YOUR OPPONENT'S MOVE");
            Image pic4 = new Image("lizard.png");
            ImageView v4 = new ImageView(pic4);
            v4.setFitHeight(100);
            v4.setFitWidth(100);
            v4.setPreserveRatio(true);
            player1Pic.setGraphic(v4);
            Image rB = new Image("button.png");
            ImageView rBtn = new ImageView(rB);
            rBtn.setFitHeight(100);
            rBtn.setFitWidth(100);
            rBtn.setPreserveRatio(true);
            player2Pic.setGraphic(rBtn);
            clientConnection.client.movePicked = true;
            if ((clientConnection.client.getId() != clientConnection.client.challegeId)) {
                clientConnection.client.setP1Plays("lizard");
                clientConnection.send(clientConnection.client);
            }
            if ((clientConnection.client.getId() == clientConnection.client.challegeId)) {
                clientConnection.client.setP2Plays("lizard");
                clientConnection.send(clientConnection.client);
            }
            pause1.play();
            pause1.setOnFinished(event -> {
                pauseStatement.setVisible(true);
            });
        });

        // spock picture pressed
        b5.setOnAction(e -> {primaryStage.setScene(createClientThirdGui());
            primaryStage.setTitle("This is third GUI for client");
            pauseStatement.setText("CLICK BUTTON ON RIGHT TO REVEAL YOUR OPPONENT'S MOVE");
            Image pic5 = new Image("spock.png");
            ImageView v5 = new ImageView(pic5);
            v5.setFitHeight(100);
            v5.setFitWidth(100);
            v5.setPreserveRatio(true);
            player1Pic.setGraphic(v5);
            Image rB = new Image("button.png");
            ImageView rBtn = new ImageView(rB);
            rBtn.setFitHeight(100);
            rBtn.setFitWidth(100);
            rBtn.setPreserveRatio(true);
            player2Pic.setGraphic(rBtn);
            clientConnection.client.movePicked = true;
            if ((clientConnection.client.getId() != clientConnection.client.challegeId)) {
                clientConnection.client.setP1Plays("spock");
                clientConnection.send(clientConnection.client);
            }
            if ((clientConnection.client.getId() == clientConnection.client.challegeId)) {
                clientConnection.client.setP2Plays("spock");
                clientConnection.send(clientConnection.client);
            }
            pause1.play();
            pause1.setOnFinished(event -> {
                pauseStatement.setVisible(true);
            });
        });

        // Sets the opponent move picture in third scene
        player2Pic.setOnAction(event -> {
            // This sets the image for opponent move depending on the string they chose
            if(clientConnection.client.id != clientConnection.client.challegeId){
                if(clientConnection.client.p2Plays.equals("rock")){
                    Image pic1 = new Image("rock.png");
                    ImageView v1 = new ImageView(pic1);
                    v1.setFitHeight(100);
                    v1.setFitWidth(100);
                    v1.setPreserveRatio(true);
                    player2Pic.setGraphic(v1);
                }
                if(clientConnection.client.p2Plays.equals("paper")){
                    Image pic1 = new Image("paper.png");
                    ImageView v1 = new ImageView(pic1);
                    v1.setFitHeight(100);
                    v1.setFitWidth(100);
                    v1.setPreserveRatio(true);
                    player2Pic.setGraphic(v1);
                }
                if(clientConnection.client.p2Plays.equals("scissor")){
                    Image pic1 = new Image("Scissor.png");
                    ImageView v1 = new ImageView(pic1);
                    v1.setFitHeight(100);
                    v1.setFitWidth(100);
                    v1.setPreserveRatio(true);
                    player2Pic.setGraphic(v1);
                }
                if(clientConnection.client.p2Plays.equals("lizard")){
                    Image pic1 = new Image("lizard.png");
                    ImageView v1 = new ImageView(pic1);
                    v1.setFitHeight(100);
                    v1.setFitWidth(100);
                    v1.setPreserveRatio(true);
                    player2Pic.setGraphic(v1);
                }
                if(clientConnection.client.p2Plays.equals("spock")){
                    Image pic1 = new Image("spock.png");
                    ImageView v1 = new ImageView(pic1);
                    v1.setFitHeight(100);
                    v1.setFitWidth(100);
                    v1.setPreserveRatio(true);
                    player2Pic.setGraphic(v1);
                }
            }
            // This sets the image for opponent move depending on the string they chose
            if(clientConnection.client.id == clientConnection.client.challegeId){
                if(clientConnection.client.p1Plays.equals("rock")){
                    Image pic1 = new Image("rock.png");
                    ImageView v1 = new ImageView(pic1);
                    v1.setFitHeight(100);
                    v1.setFitWidth(100);
                    v1.setPreserveRatio(true);
                    player2Pic.setGraphic(v1);
                }
                if(clientConnection.client.p1Plays.equals("paper")){
                    Image pic1 = new Image("paper.png");
                    ImageView v1 = new ImageView(pic1);
                    v1.setFitHeight(100);
                    v1.setFitWidth(100);
                    v1.setPreserveRatio(true);
                    player2Pic.setGraphic(v1);
                }
                if(clientConnection.client.p1Plays.equals("scissor")){
                    Image pic1 = new Image("scissor.png");
                    ImageView v1 = new ImageView(pic1);
                    v1.setFitHeight(100);
                    v1.setFitWidth(100);
                    v1.setPreserveRatio(true);
                    player2Pic.setGraphic(v1);
                }
                if(clientConnection.client.p1Plays.equals("lizard")){
                    Image pic1 = new Image("lizard.png");
                    ImageView v1 = new ImageView(pic1);
                    v1.setFitHeight(100);
                    v1.setFitWidth(100);
                    v1.setPreserveRatio(true);
                    player2Pic.setGraphic(v1);
                }
                if(clientConnection.client.p1Plays.equals("spock")){
                    Image pic1 = new Image("spock.png");
                    ImageView v1 = new ImageView(pic1);
                    v1.setFitHeight(100);
                    v1.setFitWidth(100);
                    v1.setPreserveRatio(true);
                    player2Pic.setGraphic(v1);
                }
            }
            pause2.play();
            pause2.setOnFinished(event1 -> {
                pauseStatement.setText(clientConnection.client.getWhoWon() + " Click Replay for Next Round!");
                t3.setText("Player 1 Score: " + clientConnection.client.getP1Points());
                t4.setText("Player 2 Score: " + clientConnection.client.getP2Points());
                pauseStatement.setFont(stmntFont);
            });

        });

        // Resets all of the gameinfo objects to default which lets another game be played
        mTwo.setOnAction(e->{
            primaryStage.setScene(clientGUIfirstScene());
            gameStart = false;
            clientConnection.client.challenging = false;
            clientConnection.client.isChallenged = false;
            clientConnection.client.tempCount = 0;
            clientConnection.client.p1Plays = null;
            clientConnection.client.p2Plays = null;
            clientConnection.client.whoWon = null;
            clientConnection.client.movePicked = false;
            clientConnection.client.gameAddedto_gamesArray = false;
            clientConnection.client.challegeId = 0;
            clientConnection.client.gameId = 0;

            pauseStatement.clear();
            if ((clientConnection.client.getP1Points() == 3) || (clientConnection.client.getP2Points() == 3)){
                b1.setDisable(true);
                b2.setDisable(true);
                b3.setDisable(true);
                b4.setDisable(true);
                b5.setDisable(true);
            }
            if ((clientConnection.client.getP1Points() == 3)){
                textArea.setText("Player 1 Wins!!! Click Restart to Start New Game!");
            }
            if ((clientConnection.client.getP2Points() == 3)){
                textArea.setText("Player 2 Wins!!! Click Restart to Start New Game!");
            }
        });

        // Exits the platform
        mThree.setOnAction(event -> {Platform.exit();});

        // Exits the platform
        i3.setOnAction(event -> {Platform.exit();});

        // This is used to reset the game(new game)
        i2.setOnAction(event -> { clientConnection.client.setP1Points(0);
            clientConnection.client.setP2Points(0);
            primaryStage.setScene(clientGUIfirstScene());
            listItems2.getItems().clear();
        });

        VBox buttonBox = new VBox(10, s1, s2, clientChoice);
        buttonBox.setAlignment(Pos.CENTER);
        BorderPane startPane = new BorderPane();
        startPane.setCenter(buttonBox);
        startPane.setStyle("-fx-background-color: lightpink");

        Scene startScene = new Scene(startPane, 500,500);

        listItems2 = new ListView<String>();

        // Used to know which item is being selected from the listview
        clientlistItems.getSelectionModel().selectedItemProperty().addListener(e -> {
            // Gets the selected text and sets it to the challege btn
            Integer selectedItem = clientlistItems.getSelectionModel().getSelectedItem();
            challengeBtn.setText("Challenge: " + selectedItem);
            clientConnection.client.requested = true;

            // Once the challenge button is pressed it changes the scene and sends gameinfo to server
            challengeBtn.setOnAction(event->{
                primaryStage.setScene(createClientGui());
                clientConnection.client.challegeId = selectedItem;
                clientConnection.client.challenging = true;
                clientConnection.client.isChallenged = true;
                clientConnection.send(clientConnection.client);
            });
        });

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                Platform.exit();
                System.exit(0);
            }
        });

        primaryStage.setScene(startScene);
        primaryStage.show();

    }

    public Scene clientGUIfirstScene(){

        BorderPane bpane = new BorderPane();
        clientTeller.setAlignment(Pos.CENTER);
        clientTeller.setStyle("-fx-background-color: gold");
        VBox center = new VBox(10, clientTeller, challengeBtn);
        center.setAlignment(Pos.CENTER);
        bpane.setCenter(center);

        TextField title = new TextField("Rock! Paper! Scissor! Lizard! Spock!");
        title.setAlignment(Pos.CENTER);
        title.setStyle("-fx-background-color: gold");
        title.setFont(Font.font("Verdana", FontWeight.BOLD, 23));
        bpane.setTop(title);

        gameText.setWrapText(true);
        gameText.setMaxHeight(200);
        gameText.setStyle("-fx-control-inner-background: gold;");
        gameText.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
        gameText.setMaxHeight(50);
        bpane.setBottom(gameText);

        TextField connectionList = new TextField("Client Connection");
        connectionList.setStyle("-fx-background-color: gold");
        connectionList.setAlignment(Pos.CENTER);

        TextField inGameList = new TextField("Clients In Game");
        inGameList.setStyle("-fx-background-color: gold");
        inGameList.setAlignment(Pos.CENTER);

        clientlistItems.setPrefWidth(150);
        inGamelistItems.setPrefWidth(150);

        leftSide = new VBox(0, connectionList, clientlistItems);
        bpane.setLeft(leftSide);

        rightSide = new VBox(0, inGameList, inGamelistItems);
        bpane.setRight(rightSide);

        bpane.setStyle("-fx-background-color: gold");

        return new Scene(bpane, 500, 500);
    }

    // Second scene when you press connect
    public Scene createClientGui() {

        BorderPane bPane = new BorderPane();
        // Top menu
        bPane.setTop(menu);

        b1.setStyle("-fx-background-color: lightblue;");
        b2.setStyle("-fx-background-color: lightblue;");
        b3.setStyle("-fx-background-color: lightblue;");
        b4.setStyle("-fx-background-color: lightblue;");
        b5.setStyle("-fx-background-color: lightblue;");

        textArea.setMaxWidth(400);
        textArea.setAlignment(Pos.CENTER);
        textArea.setStyle("-fx-background-color: lightblue;");

        // Sets the images for the second scene
        Image pic1 = new Image("rock.png");
        ImageView v1 = new ImageView(pic1);
        v1.setFitHeight(100);
        v1.setFitWidth(100);
        v1.setPreserveRatio(true);
        b1.setGraphic(v1);

        Image pic2 = new Image("Paper.png");
        ImageView v2 = new ImageView(pic2);
        v2.setFitHeight(100);
        v2.setFitWidth(100);
        v2.setPreserveRatio(true);
        b2.setGraphic(v2);

        Image pic3 = new Image("Scissor.png");
        ImageView v3 = new ImageView(pic3);
        v3.setFitHeight(100);
        v3.setFitWidth(100);
        b3.setGraphic(v3);

        Image pic4 = new Image("lizard.png");
        ImageView v4 = new ImageView(pic4);
        v4.setFitHeight(100);
        v4.setFitWidth(100);
        v4.setPreserveRatio(true);
        b4.setGraphic(v4);

        Image pic5 = new Image("spock.png");
        ImageView v5 = new ImageView(pic5);
        v5.setFitHeight(100);
        v5.setFitWidth(100);
        v5.setPreserveRatio(true);
        b5.setGraphic(v5);

        HBox hBox1 = new HBox(10, b1, b2, b3);
        hBox1.setAlignment(Pos.CENTER);
        HBox hBox2 = new HBox(10, b4, b5);
        hBox2.setAlignment(Pos.CENTER);
        VBox vBox = new VBox(20, textArea, hBox1, hBox2);
        vBox.setAlignment(Pos.CENTER);
        bPane.setCenter(vBox);
        bPane.setStyle("-fx-background-color: lightblue");

        return new Scene(bPane, 500, 500);

    }

    // Third scene when you press one of the images
    public Scene createClientThirdGui() {

        BorderPane bPane = new BorderPane();
        // Top menu
        bPane.setTop(menu);

        TextField t1 = new TextField("Player 1 Move");
        TextField t2 = new TextField("Player 2 Move");
        TextField t5 = new TextField("VS");

        HBox hbox = new HBox(150, t1, t2);
        HBox hbox1 = new HBox(50, player1Pic, t5, player2Pic);
        HBox hbox2 = new HBox(150, t3, t4);
        hbox1.setAlignment(Pos.CENTER);
        listItems2.setPrefWidth(500);
        listItems2.setPrefHeight(50);
        VBox vb1 = new VBox(25, pauseStatement, listItems2);
        bPane.setBottom(vb1);

        VBox vbox = new VBox(50, hbox, hbox1, hbox2);
        bPane.setCenter(vbox);
        bPane.setMargin(vbox, new Insets(0,50,0,50));
        vbox.setAlignment(Pos.CENTER);

        t1.setStyle("-fx-background-color: coral;");
        t2.setStyle("-fx-background-color: coral;");
        t3.setStyle("-fx-background-color: coral;");
        t4.setStyle("-fx-background-color: coral;");
        t5.setStyle("-fx-background-color: coral;");
        pauseStatement.setStyle("-fx-background-color: coral;");

        TextField textArea = new TextField("Choose One Image for your Move");
        textArea.setMaxWidth(225);

        bPane.setStyle("-fx-background-color: coral");

        return new Scene(bPane, 500, 500);

    }

}

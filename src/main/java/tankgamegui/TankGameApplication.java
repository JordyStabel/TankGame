package tankgamegui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import tankgame.GameEngine;
import tankgame.ITankGame;
import tankgame.TankGame;
import tankgamegui.enums.BlockType;
import tankgamegui.enums.ShellType;
import tankgamegui.enums.TankType;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TankGameApplication extends Application implements ITankGameGUI, Runnable {

    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    // Game 'engine' stuff
    GameEngine gameEngine = new GameEngine();

    // For testing only
    public Circle ball;

    // Constants to define size of GUI elements
    private final int BORDERSIZE = 10; // Size of borders in pixels
    private final int AREAWIDTH = 1000; // Width of area in pixels
    private final int AREAHEIGHT = 1000; // Height of area in pixels
    private final int SQUAREWIDTH = 10; // Width of single square in pixels
    private final int SQUAREHEIGHT = 10; // Height of single square in pixels
    private final int BUTTONWIDTH = 180; // Width of button

    // Constants to define number of squares horizontal and vertical
    private final int NRSQUARESHORIZONTAL = 100;
    private final int NRSQUARESVERTICAL = 100;

    // Opponent's name
    private String opponentName;

    // Label for opponent's name
    private Label labelOpponentName;

    // player's number
    private int playerNr = 0;

    // player's name
    private String playerName = null;

    // player that may fire a shot (player 0 or player 1)
    private int playerTurn = 0;

    // Label for player's name
    private Label labelPlayerName;

    // Text field to set player's name
    private Label labelYourName;
    private TextField textFieldPlayerName;

    // Ocean area, a 10 x 10 grid where the player's ships are placed
    private Rectangle oceanArea;

    // Area for testing
    private Rectangle testArea;

    // Squares for the ocean area
    private Rectangle[][] squaresOceanArea;

    // Sea battle game
    private ITankGame game;

    // Flag to indicate whether game is in single-player or multiplayer mode
    private boolean singlePlayerMode = true;

    // Radio buttons to indicate whether game is in single-player or multiplayer mode
    private Label labelSingleMultiPlayer;
    private RadioButton radioSinglePlayer;
    private RadioButton radioMultiPlayer;

    // Flag to indicate whether the game is in playing mode
    private boolean playingMode = false;

    // Flag to indicate that the game is ended
    private boolean gameEnded = false;

    // Flag to indicate whether next ship should be placed horizontally or vertically
    private boolean horizontal = true;

    // Radio buttons to indicate whether next ship should be placed horizontally or vertically
    private Label labelHorizontalVertical;
    private RadioButton radioHorizontal;
    private RadioButton radioVertical;

    // Buttons to register player, startSocket the game, and place or remove ships
    private Button buttonRegisterPlayer;
    private Button buttonPlaceAllShips;
    private Button buttonRemoveAllShips;
    private Button buttonReadyToPlay;
    private Button buttonStartNewGame;
    private Button buttonPlaceAircraftCarrier;
    private Button buttonRemoveShip;

    // Flag to indicate whether square is selected in ocean area
    private boolean squareSelectedInOceanArea = false;

    // X and y-position of selected square in ocean region
    private int selectedSquareX;
    private int selectedSquareY;

    @Override
    public void start(Stage primaryStage) {

        // Define grid pane
        GridPane grid;
        grid = new GridPane();
        grid.setHgap(BORDERSIZE);
        grid.setVgap(BORDERSIZE);
        grid.setPadding(new Insets(BORDERSIZE, BORDERSIZE, BORDERSIZE, BORDERSIZE));

        // For debug purposes
        // Make de grid lines visible
        // grid.setGridLinesVisible(true);

        // Create the scene and add the grid pane
        Group root = new Group();
        Scene scene = new Scene(root, AREAWIDTH + BUTTONWIDTH + 3 * BORDERSIZE, AREAHEIGHT + 2 * BORDERSIZE + 65, Color.GREY);
        root.getChildren().add(grid);

        // Label for opponent's name
        opponentName = "Opponent";
        labelOpponentName = new Label(opponentName);
        labelOpponentName.setMinWidth(AREAWIDTH);
        grid.add(labelOpponentName, 0, 0, 1, 2);

        // Label for player's name
        playerName = "";
        labelPlayerName = new Label("Your grid");
        labelPlayerName.setMinWidth(AREAWIDTH);
        grid.add(labelPlayerName, 0, 35, 1, 2);

        // Ocean area, a 10 x 10 grid where the player's ships are placed
        oceanArea = new Rectangle(BORDERSIZE, BORDERSIZE, AREAWIDTH, AREAHEIGHT);
        oceanArea.setFill(Color.RED);
        root.getChildren().add(oceanArea);

        // Creating the testingArea
        testArea = new Rectangle(1100,500,400, 400);
        testArea.setFill(Color.WHITE);
        root.getChildren().add(testArea);

        // Create 10 x 10 squares for the ocean area
        squaresOceanArea = new Rectangle[NRSQUARESHORIZONTAL][NRSQUARESVERTICAL];
        for (int i = 0; i < NRSQUARESHORIZONTAL; i++) {
            for (int j = 0; j < NRSQUARESVERTICAL; j++) {
                double x = oceanArea.getX() + i * (AREAWIDTH / NRSQUARESHORIZONTAL) + 2;
                double y = oceanArea.getY() + j * (AREAHEIGHT / NRSQUARESVERTICAL) + 2;
                Rectangle rectangle = new Rectangle(x, y, SQUAREWIDTH, SQUAREHEIGHT);
                rectangle.setStroke(Color.BLACK);
                rectangle.setFill(Color.LIGHTBLUE);
                rectangle.setVisible(true);
                final int xpos = i;
                final int ypos = j;
                rectangle.addEventHandler(MouseEvent.MOUSE_PRESSED,
                        new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent event) {
                                rectangleOceanAreaMousePressed(event, xpos, ypos);
                                System.out.println(xpos + "-" + ypos);
                            }
                        });
                squaresOceanArea[i][j] = rectangle;
                root.getChildren().add(rectangle);
            }
        }

        // Adding a ball to testArea
        double x = testArea.getX() + testArea.getWidth() / 2;
        double y = testArea.getY()  + testArea.getHeight() / 2;
        ball = new Circle(x, y,25, Color.GREEN);
        ball.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
//                while (ball.getCenterY() >= testArea.getY()){
//                    ball.setCenterY(ball.getCenterY() - 15);
//
//                }
                System.out.println("Ball is clicked!");
            }
        });
        root.getChildren().add(ball);

        // Text field to set the player's name
        labelYourName = new Label("Your name:");
        grid.add(labelYourName, 1, 2, 1, 2);
        textFieldPlayerName = new TextField();
        textFieldPlayerName.setMinWidth(BUTTONWIDTH);
        textFieldPlayerName.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                playerName = textFieldPlayerName.getText();
                labelPlayerName.setText(playerName);
            }
        });
        grid.add(textFieldPlayerName, 1, 4, 1, 2);

        // Radio buttons to choose between single-player and multi-player mode
        labelSingleMultiPlayer = new Label("Play game in: ");
        radioSinglePlayer = new RadioButton("single-player mode");
        Tooltip tooltipSinglePlayer = new Tooltip("Play game in single-player mode");
        radioSinglePlayer.setTooltip(tooltipSinglePlayer);
        radioSinglePlayer.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                singlePlayerMode = true;
            }
        });
        radioMultiPlayer = new RadioButton("multi-player mode");
        Tooltip tooltipMultiPlayer = new Tooltip("Play game in multi-player mode");
        radioMultiPlayer.setTooltip(tooltipMultiPlayer);
        radioMultiPlayer.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                singlePlayerMode = false;
            }
        });
        ToggleGroup tgSingleMultiPlayer = new ToggleGroup();
        radioSinglePlayer.setToggleGroup(tgSingleMultiPlayer);
        radioMultiPlayer.setToggleGroup(tgSingleMultiPlayer);
        radioSinglePlayer.setSelected(true);
        grid.add(labelSingleMultiPlayer, 1, 6, 1, 2);
        grid.add(radioSinglePlayer, 1, 8, 1, 2);
        grid.add(radioMultiPlayer, 1, 10, 1, 2);

        // Button to register the player
        buttonRegisterPlayer = new Button("Register");
        buttonRegisterPlayer.setMinWidth(BUTTONWIDTH);
        Tooltip tooltipRegisterParticipant =
                new Tooltip("Press this button to register as player");
        buttonRegisterPlayer.setTooltip(tooltipRegisterParticipant);
        buttonRegisterPlayer.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                try {
                    registerPlayer();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        grid.add(buttonRegisterPlayer, 1, 14, 1, 3);

        // Button to place the player's ships automatically
        buttonPlaceAllShips = new Button("Place ships for me");
        buttonPlaceAllShips.setMinWidth(BUTTONWIDTH);
        Tooltip tooltipPlaceShips =
                new Tooltip("Press this button to let the computer place your ships");
        buttonPlaceAllShips.setTooltip(tooltipPlaceShips);
        buttonPlaceAllShips.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                placeShipsAutomatically();
            }
        });
        buttonPlaceAllShips.setDisable(true);
        grid.add(buttonPlaceAllShips, 1, 18, 1, 3);

        // Button to remove the player's ships that are already placed
        buttonRemoveAllShips = new Button("Remove all my ships");
        buttonRemoveAllShips.setMinWidth(BUTTONWIDTH);
        Tooltip tooltipRemoveAllShips =
                new Tooltip("Press this button to remove all your ships");
        buttonRemoveAllShips.setTooltip(tooltipRemoveAllShips);
        buttonRemoveAllShips.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                removeAllShips();
            }
        });
        buttonRemoveAllShips.setDisable(true);
        grid.add(buttonRemoveAllShips, 1, 22, 1, 3);

        // Button to notify that the player is ready to startSocket playing
        buttonReadyToPlay = new Button("Ready to play");
        buttonReadyToPlay.setMinWidth(BUTTONWIDTH);
        Tooltip tooltipReadyToPlay =
                new Tooltip("Press this button when you are ready to play");
        buttonReadyToPlay.setTooltip(tooltipReadyToPlay);
        buttonReadyToPlay.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                notifyWhenReady();
            }
        });
        buttonReadyToPlay.setDisable(true);
        grid.add(buttonReadyToPlay, 1, 26, 1, 3);

        // Button to startSocket a new game
        buttonStartNewGame = new Button("Start new game");
        buttonStartNewGame.setMinWidth(BUTTONWIDTH);
        Tooltip tooltipStartNewGame =
                new Tooltip("Press this button to startSocket a new game");
        buttonStartNewGame.setTooltip(tooltipStartNewGame);
        buttonStartNewGame.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                try {
                    startNewGame();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        buttonStartNewGame.setDisable(true);
        grid.add(buttonStartNewGame, 1, 30, 1, 3);

        // Radio buttons to place ships horizontally or vertically
        labelHorizontalVertical = new Label("Place next ship: ");
        radioHorizontal = new RadioButton("horizontally");
        Tooltip tooltipHorizontal = new Tooltip("Place next ship horizontally");
        radioHorizontal.setTooltip(tooltipHorizontal);
        radioHorizontal.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                horizontal = true;
            }
        });
        radioVertical = new RadioButton("vertically");
        Tooltip tooltipVertical = new Tooltip("Place next ship vertically");
        radioVertical.setTooltip(tooltipVertical);
        radioVertical.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                horizontal = false;
            }
        });
        ToggleGroup tgHorizontalVertical = new ToggleGroup();
        radioHorizontal.setToggleGroup(tgHorizontalVertical);
        radioVertical.setToggleGroup(tgHorizontalVertical);
        radioHorizontal.setSelected(true);
        labelHorizontalVertical.setDisable(true);
        radioHorizontal.setDisable(true);
        radioVertical.setDisable(true);
        grid.add(labelHorizontalVertical, 1, 40, 1, 2);
        grid.add(radioHorizontal, 1, 42, 1, 2);
        grid.add(radioVertical, 1, 44, 1, 2);

        // Button to place aircraft carrier on selected square
        buttonPlaceAircraftCarrier = new Button("Place aircraft carrier (5)");
        buttonPlaceAircraftCarrier.setMinWidth(BUTTONWIDTH);
        Tooltip tooltipPlaceAircraftCarrier =
                new Tooltip("Press this button to place the aircraft carrier on the selected square");
        buttonPlaceAircraftCarrier.setTooltip(tooltipPlaceAircraftCarrier);
        buttonPlaceAircraftCarrier.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                placeShipAtSelectedSquare(TankType.SMALL);
            }
        });
        buttonPlaceAircraftCarrier.setDisable(true);
        grid.add(buttonPlaceAircraftCarrier, 1, 48, 1, 3);

        // Button to remove ship that is positioned at selected square
        buttonRemoveShip = new Button("Remove ship");
        buttonRemoveShip.setMinWidth(BUTTONWIDTH);
        Tooltip tooltipRemoveShip =
                new Tooltip("Press this button to remove ship that is "
                        + "positioned on the selected square");
        buttonRemoveShip.setTooltip(tooltipRemoveShip);
        buttonRemoveShip.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                removeShipAtSelectedSquare();
            }
        });
        buttonRemoveShip.setDisable(true);
        grid.add(buttonRemoveShip, 1, 68, 1, 3);

        // Define title and assign the scene for main window
        primaryStage.setTitle("Tank Game");
        primaryStage.setResizable(true); // Change back to false
        primaryStage.setScene(scene);
        primaryStage.show();


        // Create instance of class that implements java interface ISeaBattleGame.
        // The class SeaBattleGame is not implemented yet.
        // When invoking methods of class SeaBattleGame an
        // UnsupportedOperationException will be thrown
        game = new TankGame();

        // Start the game engine thread
        startEngine();
    }

    public void setPlayerName(int playerNr, String name) {
        // Check identification of player
        if (playerNr != this.playerNr) {
            showMessage("ERROR: Wrong player number method setPlayerName()");
            return;
        } else {
            showMessage("player name " + name + " registered");
        }
        playerName = name;
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                labelPlayerName.setText(playerName + "\'s grid");
            }
        });
    }

    public void setOpponentName(int playerNr, String name) {
        // Check identification of player
        if (playerNr != this.playerNr) {
            showMessage("ERROR: Wrong player number method setOpponentName()");
            return;
        } else {
            showMessage("Your opponent is " + name);
        }
        opponentName = name;
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                labelOpponentName.setText(opponentName + "\'s grid");
            }
        });
    }

    public void opponentFiresShot(int playerNr, ShellType shellType) {
        if (shellType.equals(ShellType.DESTROYED)) {
            showMessage("Winner: " + opponentName + ".\nPress Start new game to continue");
            buttonStartNewGame.setDisable(false);
            gameEnded = true;
        }
        switchTurn();
    }

    public void showSquarePlayer(int playerNr, final int posX, final int posY, final BlockType blockType) {
        // Check identification of player
        if (playerNr != this.playerNr) {
            showMessage("ERROR: Wrong player number method showSquarePlayer()");
            return;
        }
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Rectangle square = squaresOceanArea[posX][posY];
                setSquareColor(square, blockType);
            }
        });
    }

    @Override
    public void showSquareOpponent(int playerNr, int posX, int posY, BlockType blockType) {

    }

    public void startGame() {
        playingMode = true;
        labelHorizontalVertical.setDisable(true);
        radioHorizontal.setDisable(true);
        radioVertical.setDisable(true);
        buttonPlaceAllShips.setDisable(true);
        buttonRemoveAllShips.setDisable(true);
        buttonReadyToPlay.setDisable(true);
        buttonStartNewGame.setDisable(true);
        buttonPlaceAircraftCarrier.setDisable(true);
        buttonRemoveShip.setDisable(true);
    }

    private void setSquareColor(final Rectangle square, final BlockType blockType) {
        // Ensure that changing the color of the square is performed by
        // the JavaFX Application Thread.
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                switch (blockType) {
                    case WATER:
                        square.setFill(Color.LIGHTBLUE);
                        break;
                    case TANK:
                        square.setFill(Color.DARKGRAY);
                        break;
                    case GROUND:
                        square.setFill(Color.BLUE);
                        break;
                    case SURFACE:
                        square.setFill(Color.RED);
                        break;
                    default:
                        square.setFill(Color.LIGHTBLUE);
                        break;
                }
            }
        });
    }

    // Register player
    private void registerPlayer() throws Exception {
        playerName = textFieldPlayerName.getText();
        if ("".equals(playerName) || playerName == null) {
            showMessage("Enter your name before registering");
        } else {
            labelPlayerName.setText(playerName + "\'s grid");
            playerNr = game.registerPlayer(playerName, this, singlePlayerMode);
            if (playerNr != -1) {
                labelYourName.setDisable(true);
                textFieldPlayerName.setDisable(true);
                labelSingleMultiPlayer.setDisable(true);
                radioSinglePlayer.setDisable(true);
                radioMultiPlayer.setDisable(true);
                buttonRegisterPlayer.setDisable(true);
                labelHorizontalVertical.setDisable(false);
                radioHorizontal.setDisable(false);
                radioVertical.setDisable(false);
                buttonPlaceAllShips.setDisable(false);
                buttonRemoveAllShips.setDisable(false);
                buttonReadyToPlay.setDisable(false);
                buttonPlaceAircraftCarrier.setDisable(false);
                buttonRemoveShip.setDisable(false);
                showMessage("player " + playerName + " registered");
            } else {
                showMessage("Name already defined");
            }
        }
    }

    /**
     * Place the player's ships automatically.
     */
    private void placeShipsAutomatically() {
        // Place the player's ships automatically.
        //game.placeShipsAutomatically(playerNr);
    }

    /**
     * Remove the player's ships.
     */
    private void removeAllShips() {
        // Remove the player's ships
        //game.removeAllShips(playerNr);
    }

    /**
     * Notify that the player is ready to startSocket the game.
     */
    private void notifyWhenReady() {
        // Notify that the player is ready is startSocket the game.
        playingMode = game.notifyWhenReady(playerNr);
        if (playingMode) {
            startGame();
        } else {
            showMessage("Place all ships and then press Ready to play");
        }
    }

    // Start new game
    private void startNewGame() throws IOException {
        // The player wants to startSocket a new game.
        game.startNewGame(playerNr);
        boolean success = true;
        try{
            game = new TankGame();
            for (Rectangle[] tileArray :
                    squaresOceanArea) {
                for (Rectangle tile :
                        tileArray) {
                    setSquareColor(tile, BlockType.WATER);
                }
            }
        }catch (Exception e){
            success = false;
        }
        if (success) {
            playingMode = false;
            gameEnded = false;
            labelYourName.setDisable(false);
            textFieldPlayerName.setDisable(false);
            labelSingleMultiPlayer.setDisable(false);
            radioSinglePlayer.setDisable(false);
            radioMultiPlayer.setDisable(false);
            buttonRegisterPlayer.setDisable(false);
        } else {
            showMessage("Cannot startSocket new game");
        }
    }

    private void placeShipAtSelectedSquare(TankType tankType) {
        if (squareSelectedInOceanArea) {
            int bowX = selectedSquareX;
            int bowY = selectedSquareY;
            boolean success = game.placeTank(playerNr, tankType, bowX, bowY);
            if (!success) {
                showMessage("Cannot place ship");
            }
        } else {
            showMessage("Select square in " + playerName + "\'s grid to place ship");
        }
    }

    /**
     * Remove ship that is positioned at selected square in ocean area.
     */
    private void removeShipAtSelectedSquare() {
        if (squareSelectedInOceanArea) {
            int posX = selectedSquareX;
            int posY = selectedSquareY;
            boolean success = game.removeTanks(playerNr);
            if (!success) {
                showMessage("Cannot remove tanks");
            }
        } else {
            showMessage("Select square in " + playerName + "\'s grid to remove tank");
        }
    }

    /**
     * Show an alert message.
     * The message will disappear when the user presses ok.
     */
    //
    private void showMessage(final String message) {
        // Use Platform.runLater() to ensure that code concerning
        // the Alert message is executed by the JavaFX Application Thread
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Sea battle");
                alert.setHeaderText("Message for " + playerName);
                alert.setContentText(message);
                alert.showAndWait();
            }
        });
    }

    /**
     * Event handler when mouse button is pressed in rectangle in ocean area.
     * When not in playing mode: the square that was selected before will
     * become light blue and the the selected square will become yellow.
     * A message will be shown when in playing mode.
     *
     * @param event mouse event
     * @param x     x-coordinate of selected square
     * @param y     y-coordinate of selected square
     */
    private void rectangleOceanAreaMousePressed(MouseEvent event, int x, int y) {
        if (!playingMode) {
            // Game is not in playing mode: select square to place a ship
            if (squareSelectedInOceanArea) {
                Rectangle square = squaresOceanArea[selectedSquareX][selectedSquareY];
                if (square.getFill().equals(Color.YELLOW)) {
                    square.setFill(Color.LIGHTBLUE);
                }
            }
            selectedSquareX = x;
            selectedSquareY = y;
            squaresOceanArea[x][y].setFill(Color.YELLOW);
            squareSelectedInOceanArea = true;
        } else {
            showMessage("Select square in " + opponentName + "\'s grid to fire");
        }
    }

    // Switch current player
    private synchronized void switchTurn() {
        playerTurn = 1 - playerTurn;
    }

    // Check if it's player's turn
    private synchronized boolean playersTurn() {
        return playerNr == playerTurn;
    }

    // Main method for the game
    public static void main(String[] args){
        launch(args);
        setupLogger();
        LOGGER.log(Level.INFO, "Program has started");
    }

    // Setup for logger
    private static void setupLogger(){
        try{
            FileHandler fileHandler = new FileHandler("tankgame_logger.log", true);
            fileHandler.setLevel(Level.FINEST);
            LOGGER.addHandler(fileHandler);
        }
        catch (IOException e){
            LOGGER.log(Level.SEVERE, "File logger failed." , e);
        }
    }

    @Override
    public void run() {

        isRunning = true;

        System.out.println("Been here");

        boolean render = false;
        double firstTime = 0;
        double lastTime = System.nanoTime() / 1000000000.0;
        double passedTime = 0;
        double unprocessedTime = 0;

        double frameTime = 0;
        int frames = 0;
        int fps = 0;

        while(isRunning){

            render = false;

            firstTime = System.nanoTime() / 1000000000.0;
            passedTime = firstTime - lastTime;
            lastTime = firstTime;

            unprocessedTime += passedTime;
            frameTime += passedTime;

            while(unprocessedTime >= UPDATE_LIMIT){

                unprocessedTime -= UPDATE_LIMIT;
                render = true;

                //lowerBall();

                if(frameTime >= 1.0){
                    frameTime = 0;
                    fps = frames;
                    frames = 0;
                    System.out.println("FPS: " + fps);
                }
            }

            if(render){
                frames++;
            }
            else{
                try{
                    Thread.sleep(1);
                }
                catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        }
        dispose();
    }



    public void startEngine(){

        thread = new Thread(this);
        thread.run();

    }

    public void stop(){

    }

    private void dispose(){

    }

    private void lowerBall(){
       if(ball.getCenterY() >= testArea.getY()){
            ball.setCenterY(ball.getCenterY() - 1);
       }
    }
}
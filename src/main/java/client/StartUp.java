package client;

import client.connection.ClientEndPointSocket;
import client.connection.ClientSocketResponseHandler;
import client.game.TankGame;
import processing.core.PApplet;
import server.actions.Actions;
import server.actions.Message;
import server.actions.Register;

import java.io.IOException;
import java.util.Scanner;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import static processing.core.PApplet.println;

public class StartUp {

    private static ClientEndPointSocket clientEndPointSocket;

    private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private boolean exit;

    private String firstName = null;
    private String secondName = null;
    private String ip = null;

    private String name = null;

    private Scanner inputScanner = new Scanner(System.in);

    public static void main(String[] args) throws Throwable {
        StartUp menu = new StartUp();
        setupLogger();
        menu.runMenu();
    }

    private void runMenu(){
        while(!exit){
            printMenu();
            int option = getOption();
            runOption(option);
        }
    }

    private void printMenu(){
        println("\nOptions:");
        if (firstName != null){ println("1) Change First Name");}
        else println("1) Enter First Name");
        if (secondName != null)println("2) Change Second Name");
        else println("2) Enter Second Name");
        if (ip != null)println("3) Change IP");
        else println("3) Enter An IP");
        println("4) Start The Game");

        println("0) Exit");
        println("Enter A Number: ");
    }

    private int getOption(){
        int option = -1;

        while (option < 0){
            try{
                option = Integer.parseInt(inputScanner.nextLine());
            }
            catch(NumberFormatException e){
                println("Invalid Option. Please Try Again.");
            }
        }
        return option;
    }

    private void runOption(int option){
        switch(option){
            case 0:
                exit = true;
                println("Exiting");
                break;
            case 1:
                firstName = readInput("Enter A Name:");
                break;
            case 2:
                secondName = readInput("Enter A Name:");
                break;
            case 3:
                ip = readInput("Enter An IP:");
                break;
            case 4:
                if (firstName != null && secondName != null && ip != null){
                    start(ip, firstName);
                    start(ip, secondName);
                    exit = true;
                    println("Starting Tank Game!");
                } else {
                    println("Make Sure To Set All Fields");
                }
                break;
            default:
                println("Oops Something Went Wrong, ¯\\_(ツ)_/¯");
        }
    }

    private static void start(String ip, String playerName){
        TankGame tankGame = new TankGame(50,50);

        clientEndPointSocket = new ClientEndPointSocket();
        ClientSocketResponseHandler clientSocketResponseHandler = new ClientSocketResponseHandler(tankGame);
        clientEndPointSocket.setMessageHandler(clientSocketResponseHandler);

        clientEndPointSocket.connect("localhost:9090/tankgame/");

        Register register = new Register(playerName);
        Message message = new Message(Actions.REGISTER, register);
        clientEndPointSocket.sendMessage(message);

        Client client = new Client(tankGame, clientEndPointSocket);

        clientSocketResponseHandler.setClient(client);

        String[] processingArgs = {"TankGame"};
        PApplet.runSketch(processingArgs, client);

        clientEndPointSocket.sendMessage(new Message(Actions.READY));
    }

    private String readInput(String message){
        println(message);
        return inputScanner.nextLine();
    }

    private static void setupLogger(){
        try{
            FileHandler fileHandler = new FileHandler("tankgame_logger.log", true);
            fileHandler.setLevel(Level.INFO);
            LOGGER.addHandler(fileHandler);
        }
        catch (IOException e){
            LOGGER.log(Level.SEVERE, "File logger failed." , e);
        }
    }
}
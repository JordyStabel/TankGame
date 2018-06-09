package tankgamegui;

import java.util.Scanner;
import static processing.core.PApplet.println;

public class PreGameStart {

    private boolean exit;

    private String selfName = null;
    private String opponentName = null;

    private Scanner inputScanner = new Scanner(System.in);

    public static void main(String[] args) throws Throwable {
        PreGameStart menu = new PreGameStart();
        menu.runMenu();
    }

    private void runMenu(){
        while(!exit){
            printMenu();
            int option = getInput();
            runOption(option);
        }
    }

    private void printMenu(){
        println("\nOptions:");
        if (selfName != null){ println("1) Change Self Name");}
        else println("1) Enter Self Name");
        if (opponentName != null)println("2) Change Opponent Name");
        else println("2) Enter Opponent Name");
        println("3) Start The Game");

        println("0) Exit");
        println("Enter A Number: ");
    }

    private int getInput(){
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
                selfName = getSelfName();
                break;
            case 2:
                opponentName = getOpponentName();
                break;
            case 3:
                start();
                exit = true;
                println("Starting Tank Game!");
                break;
            default:
                println("Oops Something Went Wrong, ¯\\_(ツ)_/¯");
        }
    }

    private void start(){
        if (selfName != null && opponentName != null){
            TankGameApplication.main(new String[] {selfName});
            TankGameApplication.main(new String[] {opponentName});
        }
        else{
            println("Give Both Players A Name First");
        }
    }

    private String getSelfName(){
        println("Enter A Selfname: ");
        return inputScanner.nextLine();
    }

    private String getOpponentName(){
        println("Enter An Opponentname: ");
        return inputScanner.nextLine();
    }
}
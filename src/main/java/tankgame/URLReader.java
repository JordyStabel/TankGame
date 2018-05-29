package tankgame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class URLReader {

    public String readUrl(String url) throws IOException {
        URL tankGame = new URL(url);
        URLConnection urlConnection = tankGame.openConnection();

        BufferedReader input = new BufferedReader(new InputStreamReader(
                urlConnection.getInputStream()));

        String inputLine;
        inputLine = input.readLine();
        input.close();

        return inputLine;
    }
}
package hangman;

import java.lang.*;
import java.util.*;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class HangmanController extends HttpServlet {
    private static Map<String, HangmanGame> currentSessions = new HashMap<>();

    public final static String GAME_STARTED_QUERY = "game_started";
    public final static String NEW_GAME_QUERY = "new_game";
    public final static String CURRENT_GAMES_QUERY = "current_games";
    public final static int WORDS_IN_DICT = 127141;
    public ServletContext context = null;

    public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
        HttpSession session = request.getSession( true );
        String sessionId = session.getId();
        if ( context == null ) {
            context = session.getServletContext();
        }

        String query = request.getQueryString();
        PrintWriter out = response.getWriter();
        if( query.equals(NEW_GAME_QUERY) ) {
            response.setContentType("text/plain");
            HangmanGame game = new HangmanGame();
            String newWord = getRandomWord().toUpperCase();

            game.word = newWord;
            game.guessedLetters = "";

            currentSessions.put( sessionId, game );
            out.println( newWord );

        } else if ( query.equals( GAME_STARTED_QUERY ) ) {
            HangmanGame game = new HangmanGame();
            for ( Cookie cookie : request.getCookies() ) {
                if ( cookie.getName().equals("word") ) {
                    game.word = cookie.getValue();
                } else if ( cookie.getName().equals("guessedLetters") ) {
                    game.guessedLetters = cookie.getValue();
                }
            }

            currentSessions.put( sessionId, game );
        } else if ( query.equals( CURRENT_GAMES_QUERY ) ) {
            response.setContentType("text/html");
            out.println("<head><title>Current Games!</title></head>" + 
                        "<body>" + 
                        "<h1 align=center> Current Games!</h1>");

            for ( Map.Entry<String, HangmanGame> entry : currentSessions.entrySet() ) {
                out.println("<h2>word = " + entry.getValue().word + ", guessed letters = " + entry.getValue().guessedLetters + "</h2>");
            }

            out.println("</body>");
        }

        out.close();
    }

    public String getRandomWord() {
        BufferedReader dict;
        String line = "";
        try {
            dict = new BufferedReader(new FileReader(context.getRealPath("/WEB-INF/dictionary.txt")));
            double randomLine = (int)(Math.random()*WORDS_IN_DICT);
            for ( int i = 0; i < randomLine; i++ ) {
                line = dict.readLine(); // very slow way of getting to this particular line... cant see any other fast methods without counting characters
            }
        } catch ( FileNotFoundException e ) {
            line = "cannot find file!!!";
        } catch ( IOException e ) {
            line = "some io exception occurred!";
        }

        line.trim();

        return line;
    }
}

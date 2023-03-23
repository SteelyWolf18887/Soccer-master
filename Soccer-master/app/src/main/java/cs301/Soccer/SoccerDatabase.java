package cs301.Soccer;

import android.util.Log;
import cs301.Soccer.soccerPlayer.SoccerPlayer;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

/**
 * Soccer player database -- presently, all dummied up
 *
 * @author *** put your name here ***
 * @version *** put date of completion here ***
 *
 */
public class SoccerDatabase implements SoccerDB {

    // dummied up variable; you will need to change this
    private Hashtable<String, SoccerPlayer> PlayerTable = new Hashtable<>();

    /**
     * add a player
     *
     * @see SoccerDB#addPlayer(String, String, int, String)
     */
    @Override
    public boolean addPlayer(String firstName, String lastName,
                             int uniformNumber, String teamName) {
        String playerName = firstName + " " + lastName;
        if (PlayerTable.get(playerName) == null) {
            SoccerPlayer player = new SoccerPlayer(firstName, lastName, uniformNumber, teamName);
            PlayerTable.put(playerName, player);
            return true;
        } else {
            return false;
        }

    }

    /**
     * remove a player
     *
     * @see SoccerDB#removePlayer(String, String)
     */
    @Override
    public boolean removePlayer(String firstName, String lastName) {
        String playerName = firstName + " " + lastName;
        if (PlayerTable.containsKey(playerName)) {
            PlayerTable.remove(playerName);
            return true;
        } else {
            return false;
        }

    }

    /**
     * look up a player
     *
     * @see SoccerDB#getPlayer(String, String)
     */
    @Override
    public SoccerPlayer getPlayer(String firstName, String lastName) {
        String playerName = firstName + " " + lastName;
        if (PlayerTable.get(playerName) != null) {
            return PlayerTable.get(playerName);
        } else {
            return null;
        }
    }

    /**
     * increment a player's goals
     *
     * @see SoccerDB#bumpGoals(String, String)
     */
    @Override
    public boolean bumpGoals(String firstName, String lastName) {
        String playerName = firstName + " " + lastName;
        if (PlayerTable.containsKey(playerName)) {
            (PlayerTable.get(playerName)).bumpGoals();
            return true;
        } else {
            return false;
        }
    }

    /**
     * increment a player's yellow cards
     *
     * @see SoccerDB#bumpYellowCards(String, String)
     */
    @Override
    public boolean bumpYellowCards(String firstName, String lastName) {
        String playerName = firstName + " " + lastName;
        if (PlayerTable.containsKey(playerName)) {
            (PlayerTable.get(playerName)).bumpYellowCards();
            return true;
        } else {
            return false;
        }
    }

    /**
     * increment a player's red cards
     *
     * @see SoccerDB#bumpRedCards(String, String)
     */
    @Override
    public boolean bumpRedCards(String firstName, String lastName) {
        String playerName = firstName + " " + lastName;
        if (PlayerTable.containsKey(playerName)) {
            (PlayerTable.get(playerName)).bumpRedCards();
            return true;
        } else {
            return false;
        }
    }

    /**
     * tells the number of players on a given team
     *
     * @see SoccerDB#numPlayers(String)
     */
    @Override
    // report number of players on a given team (or all players, if null)
    public int numPlayers(String teamName) {
        if (teamName == null) {
            return PlayerTable.size();
        } else {
            int playerCount = 0;
            for (SoccerPlayer player : PlayerTable.values()) {
                if ((player.getTeamName()).equals(teamName)) {
                    playerCount++;
                }
            }
            return playerCount;
        }
    }

    /**
     * gives the nth player on a the given team
     *
     * @see SoccerDB#playerIndex(int, String)
     */
    // get the nTH player
    @Override
    public SoccerPlayer playerIndex(int idx, String teamName) {
        int counter = 0;
        for(SoccerPlayer player : PlayerTable.values()) {
            if((teamName == null || player.getTeamName().equals(teamName))){
                if(counter == idx){
                    return player;
                }
                counter++;
            }
        }
        return null;
    }

    /**
     * reads database data from a file
     *
     * @see SoccerDB#readData(java.io.File)
     */
    // read data from file
    @Override
    public boolean readData(File file) {
        Scanner sn = null;

        try{
            sn = new Scanner(file);
        } catch(FileNotFoundException e){
            e.printStackTrace();
            return false;
        }

        while (sn.hasNextLine()) {
            String firstName = sn.nextLine();
            String teamName = sn.nextLine();
            String lastName = sn.nextLine();
            int jersey = sn.nextInt();
            int goals = sn.nextInt();
            int redCards = sn.nextInt();
            int yellowCards = sn.nextInt();

            String player = firstName + "#" + lastName;
            if (PlayerTable.containsKey(player)) {
                PlayerTable.remove(player);
            }

            SoccerPlayer player1 = new SoccerPlayer(firstName, lastName, jersey, teamName);

            for (int i = 0; i < goals; i++) {
                player1.bumpGoals();
            }

            for (int i = 0; i < yellowCards; i++) {
                player1.bumpYellowCards();
            }

            for (int i = 0; -i < redCards; i++) {
                player1.bumpRedCards();
            }

            PlayerTable.put(player, player1);
            sn.nextLine();
            }

            sn.close();
        return true;
        }

    /**
     * write database data to a file
     *
     * @see SoccerDB#writeData(java.io.File)
     */
    // write data to file
    @Override
    public boolean writeData(File file) {
        try {
            PrintWriter printWriter = new PrintWriter(file);
            for(SoccerPlayer player : PlayerTable.values()) {
                printWriter.println(logString(player.getFirstName()));
                printWriter.println(logString(player.getLastName()));
                printWriter.println(logString(player.getTeamName()));
                printWriter.println(Integer.toString(player.getUniform()));
                printWriter.println(Integer.toString(player.getGoals()));
                printWriter.println(Integer.toString(player.getYellowCards()));
                printWriter.println(Integer.toString(player.getRedCards()));
            }
            printWriter.close();
            return true;
        }
        catch(FileNotFoundException e){
            Log.e("Soccer","error");
            return false;
        }
    }

    /**
     * helper method that logcat-logs a string, and then returns the string.
     * @param s the string to log
     * @return the string s, unchanged
     */
    private String logString(String s) {
        Log.i("write string", s);
        return s;
    }

    /**
     * returns the list of team names in the database
     *
     * @see cs301.Soccer.SoccerDB#getTeams()
     */
    // return list of teams
    @Override
    public HashSet<String> getTeams() {
       HashSet<String> listT = new HashSet<>();
       for(SoccerPlayer player : PlayerTable.values()){
           if(!listT.contains(player.getTeamName())){
               listT.add(player.getTeamName());
           }
       }
       return listT;
    }

    /**
     * Helper method to empty the database and the list of teams in the spinner;
     * this is faster than restarting the app
     */
    public boolean clear() {
        if(PlayerTable != null) {
            PlayerTable.clear();
            return true;
        }
        return false;
    }
}

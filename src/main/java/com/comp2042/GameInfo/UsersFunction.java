package com.comp2042.GameInfo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class UsersFunction {
    static HashMap<String, String> user = new HashMap<>();
    static HashMap<String, Integer> scores = new HashMap<>();
    private static final Path USERS_FILE=Path.of(System.getProperty("user.dir"),"users.txt");
    public static String currentUser;
    public static int currentUserScore;
    public static boolean UserLoggedIn=false;

//    Register

    /**
     * Register new user
     * @param username
     * @param password
     */
    public static void RegisterUser(String username, String password){
        user.put(username, password);
        scores.put(username, 0);
        saveToFile();
    }

    /**
     * Update new info to txt file
     */
    public static void saveToFile(){
        try{
            Files.createDirectories(USERS_FILE.getParent());
            try(BufferedWriter writer = Files.newBufferedWriter(USERS_FILE, StandardOpenOption.CREATE,StandardOpenOption.TRUNCATE_EXISTING)){
                for(Map.Entry<String, String> entry: user.entrySet()){
                    String username=entry.getKey();
                    String password=entry.getValue();
                    int score=scores.get(username);
                    if (Objects.equals(username, "Guest")){
                        password="0000";
                        score=0;
                    }
                    writer.write(username+","+password+","+score);
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Load previous game data
     * @throws IOException
     */
    public static void loadFromFile() throws IOException {
        user.clear();
        scores.clear();

        if(!Files.exists(USERS_FILE)){
            System.err.println("Users file does not exist");
            return;
        }

        try(BufferedReader reader=Files.newBufferedReader(USERS_FILE)){
            String line;
            while ((line=reader.readLine())!=null){
                String[] lineSplit = line.split(",",3);
                user.put(lineSplit[0],lineSplit[1]);
                scores.put(lineSplit[0],Integer.parseInt(lineSplit[2]));
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }

//    Login

    /**
     * Verify user information
     * @param username
     * @param password
     * @return boolean
     */
    public static boolean loginVerify(String username, String password){
        return user.containsKey(username) && user.get(username).equals(password);
    }

//  Update Scores

    /**
     * Update score if user break their self record
     * @param username
     * @param newscore
     */
    public static void updateScores(String username, int newscore){
        if (username == null || username.isEmpty()) return;

        int current = scores.getOrDefault(username, 0);
        int best = Math.max(current, newscore);

        scores.put(username, best);
        saveToFile();
    }

    /**
     * Current user playing update, show on panel
     * @param username
     */
    public static void currentUserUpdate(String username){
        currentUser=username;
        currentUserScore= scores.getOrDefault(username, 0);
    }

    /**
     * Return user's previous high score
     * @param username
     * @return
     */
    public static String UserHighScore(String username){
        int temsc=scores.getOrDefault(username,0);
        return String.valueOf(temsc);
    }

    /**
     * Sort the list and helper function of leaderboard
     * @return
     */
    public static List<Map.Entry<String, Integer>> getLeaderboard() {
        return scores.entrySet()
                .stream()
                .sorted((e1, e2) -> Integer.compare(e2.getValue(), e1.getValue()))
                .collect(Collectors.toList());
    }

    /**
     * Test helper function
     */
    public static void resetForTesting() {
        user.clear();
        scores.clear();
        currentUser = null;
        currentUserScore = 0;
        UserLoggedIn = false;
    }

}

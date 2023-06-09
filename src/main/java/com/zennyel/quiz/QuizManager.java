package com.zennyel.quiz;

import com.zennyel.QuizPlugin;
import com.zennyel.database.MariaDB;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuizManager {

    private QuizPlugin instance;

    private Map<Player, List<String>> messages;
    private Map<Player, Boolean> creatingQuiz;
    private Map<Player, Boolean> isJoining;

    private MariaDB database;
    private Boolean onQuizEvent;
    private Quiz quiz;
    private String awnser;


    public QuizManager() {
        instance = QuizPlugin.getPlugin(QuizPlugin.class);

        messages = new HashMap<>();
        creatingQuiz = new HashMap<>();
        isJoining = new HashMap<>();
        onQuizEvent = false;

        awnser = null;
        database = instance.getDatabase();

    }

    public QuizPlugin getInstance() {
        return instance;
    }

    public void setInstance(QuizPlugin instance) {
        this.instance = instance;
    }

    public Map<Player, List<String>> getMessages() {
        return messages;
    }

    public void setMessages(Map<Player, List<String>> messages) {
        this.messages = messages;
    }

    public Map<Player, Boolean> getCreatingQuiz() {
        return creatingQuiz;
    }

    public void setCreatingQuiz(Map<Player, Boolean> creatingQuiz) {
        this.creatingQuiz = creatingQuiz;
    }

    public void setIsJoining(Map<Player, Boolean> isJoining) {
        this.isJoining = isJoining;
    }

    public MariaDB getDatabase() {
        return database;
    }

    public void setDatabase(MariaDB database) {
        this.database = database;
    }

    public Boolean getOnQuizEvent() {
        return onQuizEvent;
    }

    public void setOnQuizEvent(Boolean onQuizEvent) {
        this.onQuizEvent = onQuizEvent;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    public void setAwnser(String awnser) {
        this.awnser = awnser;
    }

    public void updatePoints(Player player){
        if(database.getPoints(player) < 0){
            database.insertPoints(player, 0);
        }
    }

    public void closeEvent(){
        for(Player p : Bukkit.getOnlinePlayers()){
            setJoining(p, false);
            setCreatingQuiz(p, false);
            messages.put(p, null);
        }
        setOnQuizEvent(false);
        quiz = null;
        awnser = null;
    }

    public void setWinner(Player player){
        Bukkit.broadcastMessage("                   ");
        Bukkit.broadcastMessage("§5§l[QuizEvent] §f§a§l"+ player.getName() + " §fwon the quiz event!!");
        Bukkit.broadcastMessage("§5§l[QuizEvent] §fQuiz event closing, thank everyone!");
        Bukkit.broadcastMessage("                   ");
        for(Player p : Bukkit.getOnlinePlayers()){
            p.sendTitle("§f§a§lWinner: " + player.getName(), "Congratulations to won the quiz event!", 1,20, 10);
            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1, 1);
        }
        closeEvent();
        database.updatePoints(player, database.getPoints(player) + 1);
    }

    public String getAwnser() {
        return awnser;
    }
    public void setJoining(Player player, boolean isJoining){
        this.isJoining.put(player, isJoining);
    }

    public boolean isJoining(Player p){
        if(isJoining.get(p) == null){
            setJoining(p, false);
        }
        return isJoining.get(p);
    }

    public Map<Player, Boolean> getIsJoining() {
        return isJoining;
    }


    public void addMessage(Player player, String s){
        if(getMessages(player) == null){
            List<String> messageList = new ArrayList<>();
            messages.put(player, messageList);
        }
        List<String> messageList = getMessages(player);
        messageList.add(s);
        messages.put(player, messageList);
    }

    public void setCreatingQuiz(Player player, boolean isCreating){
        creatingQuiz.put(player, isCreating);
    }


    public boolean isCreatingQuiz(Player player){
        if(creatingQuiz.get(player) == null){
            return false;
        }
        return creatingQuiz.get(player);
    }

    public List<String> getMessages(Player p){
        return messages.get(p);
    }



}

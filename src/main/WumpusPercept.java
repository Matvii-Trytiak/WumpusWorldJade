package main;

import java.util.Random;
import java.util.List;
import java.util.ArrayList;

public class WumpusPercept {
    private boolean stench;
    private boolean breeze;
    private boolean glitter;
    private boolean scream;

	public static final class PerceptSentences {
		public static List<String> pit_near = List.of("There is a breeze", "I feel breeze", "It's breezy here", "I feel something, like a breeze");
		public static List<String> wumpus_near = List.of("There is a stench", "It's stinky here", "I smell something", "I smell something, like a stench");
		public static List<String> gold_near = List.of("There is a glitter", "I see something shiny", "It's glittery here", "I see something, like a glitter");
		public static List<String> wall_near = List.of("There is a bump", "It's bumping here", "I hit the wall", "I feel something, like a bump");
		public static List<String> wumpusKilled = List.of("There is a scream", "It's screaming here", "I hear something", "I hear something, like a scream");
	}


    public WumpusPercept setStench() {
        stench = true;
        return this;
    }

    public WumpusPercept setBreeze() {
        breeze = true;
        return this;
    }

    public WumpusPercept setGlitter() {
        glitter = true;
        return this;
    }

    public WumpusPercept setScream() {
        scream = true;
        return this;
    }

    public boolean isStench() {
        return stench;
    }

    public boolean isBreeze() {
        return breeze;
    }

    public boolean isGlitter() {
        return glitter;
    }

    public boolean isScream() {
        return scream;
    }

    @Override
    public String toString() {
    	Random randomGenerator = new Random();
        List<String> feelings = new ArrayList<>();
        int index = 0;
        if (breeze) {
        	index = randomGenerator.nextInt(PerceptSentences.pit_near.size());
        	feelings.add(PerceptSentences.pit_near.get(index)); }
        if (stench) {
        	index = randomGenerator.nextInt(PerceptSentences.wumpus_near.size());
        	feelings.add(PerceptSentences.wumpus_near.get(index)); }
        if (glitter) {
        	index = randomGenerator.nextInt(PerceptSentences.gold_near.size());
        	feelings.add(PerceptSentences.gold_near.get(index)); }
        if (scream) {
        	index = randomGenerator.nextInt(PerceptSentences.wumpusKilled.size());
        	feelings.add(PerceptSentences.wumpusKilled.get(index)); }
        return String.join(". ", feelings);
    }
}
package com.company;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NPCNameLibrary {
    List<String> npcNames = new ArrayList<>();
    Random random = new Random();

    NPCNameLibrary(){
        npcNames.add("Eli");
        npcNames.add("Mark");
        npcNames.add("Ben");
        npcNames.add("Nick");
        npcNames.add("Seph");
        npcNames.add("Jihn");
    }

    public String getRandomName(){
       return  npcNames.get(random.nextInt(npcNames.size()));
    }

}

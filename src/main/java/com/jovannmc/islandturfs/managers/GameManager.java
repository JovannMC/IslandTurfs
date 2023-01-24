package com.jovannmc.islandturfs.managers;

import com.jovannmc.islandturfs.IslandTurfs;
import com.jovannmc.islandturfs.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import java.util.logging.Level;

public class GameManager implements Listener {

    /*
        TODO:
        - Rely on main IslandTurfsCommand class for GameManager logic
        - Team logic
            - Join teams
            - Leave teams
            - Ready up
        - Game logic
            - Start game
            - End game
            - Reset game
     */

    IslandTurfs plugin = IslandTurfs.getInstance();

    Utils utils = new Utils();

    public void startGame(String mapName) {
        if (mapName.equalsIgnoreCase("newMap")) {
            Bukkit.getLogger().log(Level.INFO, "Starting game on newMap");
            // TODO: add start game logic
        } else if (mapName.equalsIgnoreCase("oldMap")) {
            Bukkit.getLogger().log(Level.INFO, "Starting game on oldMap");
            // TODO: add start game logic
        }
    }

    public void endGame(String mapName) {
        if (mapName.equalsIgnoreCase("newMap")) {
            Bukkit.getLogger().log(Level.INFO, "Ending game on newMap");
            TeamManager.getInstance().redTeam.clear();
            TeamManager.getInstance().blueTeam.clear();
            // TODO: add end game logic
        } else if (mapName.equalsIgnoreCase("oldMap")) {
            Bukkit.getLogger().log(Level.INFO, "Ending game on oldMap");
            TeamManager.getInstance().redTeam.clear();
            TeamManager.getInstance().blueTeam.clear();
            // TODO: add end game logic
        }
    }

    public void resetGame(String mapName) {
        if (mapName.equalsIgnoreCase("newMap")) {
            Bukkit.getLogger().log(Level.INFO, "Resetting newMap");
            // TODO: add reset logic
        } else if (mapName.equalsIgnoreCase("oldMap")) {
            Bukkit.getLogger().log(Level.INFO, "Resetting oldMap");
            // TODO: add reset logic
        }
    }

}

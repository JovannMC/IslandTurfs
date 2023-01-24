package com.jovannmc.islandturfs.managers;

import com.jovannmc.islandturfs.IslandTurfs;
import com.jovannmc.islandturfs.utils.Utils;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.UUID;

public class TeamManager implements Listener {

    IslandTurfs plugin = IslandTurfs.getInstance();

    private static TeamManager instance = new TeamManager();

    Utils utils = new Utils();

    public HashMap<UUID, String> redTeam = new HashMap();
    public HashMap<UUID, String> blueTeam = new HashMap<>();

    public boolean redReady = false;
    public boolean blueReady = false;

    public static TeamManager getInstance() {
        return instance;
    }

}

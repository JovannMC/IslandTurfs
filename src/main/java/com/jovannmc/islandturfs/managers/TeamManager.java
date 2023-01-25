package com.jovannmc.islandturfs.managers;

import com.jovannmc.islandturfs.IslandTurfs;
import com.jovannmc.islandturfs.utils.Utils;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.UUID;

public class TeamManager implements Listener {

    public static HashMap<UUID, String> redTeam = new HashMap();
    public static HashMap<UUID, String> blueTeam = new HashMap<>();

    public static boolean redReady = false;
    public static boolean blueReady = false;

}

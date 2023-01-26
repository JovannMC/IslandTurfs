package com.jovannmc.islandturfs.managers;

import org.bukkit.event.Listener;
import java.util.HashMap;
import java.util.UUID;

public class TeamManager implements Listener {

    public static HashMap<UUID, String> redTeam = new HashMap();
    public static HashMap<UUID, String> blueTeam = new HashMap<>();
    public static HashMap<UUID, String> spectators = new HashMap<>();

    public static boolean ITC_1_redReady = false;
    public static boolean ITC_1_blueReady = false;
    public static boolean ITC_2_redReady = false;
    public static boolean ITC_2_blueReady = false;

}

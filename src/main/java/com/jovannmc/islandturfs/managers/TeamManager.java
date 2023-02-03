package com.jovannmc.islandturfs.managers;

import org.bukkit.event.Listener;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class TeamManager implements Listener {

    public static ConcurrentHashMap<UUID, String> redTeam = new ConcurrentHashMap();
    public static ConcurrentHashMap<UUID, String> blueTeam = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<UUID, String> spectators = new ConcurrentHashMap<>();

    public static boolean ITC_1_redReady = false;
    public static boolean ITC_1_blueReady = false;
    public static boolean ITC_2_redReady = false;
    public static boolean ITC_2_blueReady = false;

}

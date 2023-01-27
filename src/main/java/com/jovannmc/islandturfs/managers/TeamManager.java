package com.jovannmc.islandturfs.managers;

import net.minecraft.util.Tuple;
import org.bukkit.event.Listener;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class TeamManager implements Listener {

    public static ConcurrentHashMap<UUID, Tuple<Boolean, String>> redTeam = new ConcurrentHashMap();
    public static ConcurrentHashMap<UUID, Tuple<Boolean, String>> blueTeam = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<UUID, Tuple<Boolean, String>> spectators = new ConcurrentHashMap<>();

    public static boolean ITC_1_redReady = false;
    public static boolean ITC_1_blueReady = false;
    public static boolean ITC_2_redReady = false;
    public static boolean ITC_2_blueReady = false;

}

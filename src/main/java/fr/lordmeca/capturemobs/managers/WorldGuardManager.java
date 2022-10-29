/*package fr.lordmeca.capturemobs.managers;


import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

import fr.lordmeca.capturemobs.Main;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.UUID;

public class WorldGuardManager {

    private static WorldGuardManager instance = new WorldGuardManager();



    public WorldGuardPlugin worldGuardPlugin;

    public ArrayList<String> enteredInHoldUpEvent = new ArrayList<>();

    public static WorldGuardManager getInstance() {
        return instance;
    }

    public WorldGuardPlugin getWorldGuard() {
        Plugin plugin = Main.getInstance().getServer().getPluginManager().getPlugin("WorldGuard");

        if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
            return null;
        }

        return (WorldGuardPlugin) plugin;
    }

    public void init(){

        worldGuardPlugin = getWorldGuard();

    }



}*/

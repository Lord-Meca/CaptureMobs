package fr.lordmeca.capturemobs;

import fr.lordmeca.capturemobs.cmds.CaptureBallCmd;
import fr.lordmeca.capturemobs.cmds.CaptureMobsCmd;
import fr.lordmeca.capturemobs.listeners.*;
//import fr.lordmeca.capturemobs.managers.WorldGuardManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Zombie;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.bukkit.SkullType.ZOMBIE;

public class Main extends JavaPlugin {

    private static Main instance;

    public String prefix = getConfig().getString("messages.prefix");

    public List<String> inMonsterCapture = new ArrayList<>();
    public List<String> throwingMonster = new ArrayList<>();

    public Map<String, Entity> fightWithMonster = new HashMap<>();
    public Map<String, Entity> fightAllyMonster = new HashMap<>();



    public List<String> monsters = new ArrayList<>();

    public Map<String, Boolean> itIsPlayerTurnInFight = new HashMap<>();

    public static Main getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {

        initAll();

        Bukkit.getConsoleSender().sendMessage("§d§l# §d§l§m--------------- CAPTURE MOBS ---------------");
        Bukkit.getConsoleSender().sendMessage("§d§l");
        Bukkit.getConsoleSender().sendMessage("§d§l# §aName > Capture Mobs");
        Bukkit.getConsoleSender().sendMessage("§d§l# §aVersion > 1.0");
        Bukkit.getConsoleSender().sendMessage("§d§l");
        Bukkit.getConsoleSender().sendMessage("§d§l# §aListeners > Loaded successfuly !");
        Bukkit.getConsoleSender().sendMessage("§d§l# §aCommands > Loaded successfuly !");
        Bukkit.getConsoleSender().sendMessage("§d§l");
        Bukkit.getConsoleSender().sendMessage("§d§l# §d§l§m--------------------------------------------");

    }

    public void initAll(){

        instance = this;

        saveDefaultConfig();

        registerCommands();
        registerListeners();

        //WorldGuardManager.getInstance().init();

        addMonsters();

    }

    public void addMonsters(){

        monsters.add("ZOMBIE");
        monsters.add("PIG");
        monsters.add("CHICKEN");
        monsters.add("COW");
        monsters.add("BLAZE");
        monsters.add("SHULKER");
        monsters.add("WOLF");
        monsters.add("SALMON");
        monsters.add("PHANTOM");
        monsters.add("FOX");
        monsters.add("PARROT");
        monsters.add("PANDA");
        monsters.add("RAVAGER");

    }

    public void registerCommands(){

        getCommand("captureball").setExecutor(new CaptureBallCmd());
        getCommand("capturemobs").setExecutor(new CaptureMobsCmd());

    }

    public void registerListeners(){

        PluginManager pm = getServer().getPluginManager();

        pm.registerEvents(new EntityDamageByAnotherEntityEvent(), this);

        pm.registerEvents(new PlayerProjectileHitEvent(), this);
        pm.registerEvents(new InteractPlayerEvent(), this);
        pm.registerEvents(new PlayerInventoryClickEvent(), this);
        pm.registerEvents(new PlayerInteractOnEntityEvent(), this);
        pm.registerEvents(new PlayerProjectileLaunchEvent(), this);
        pm.registerEvents(new PlayerCloseInventoryEvent(), this);
        pm.registerEvents(new PlayerWalkEvent(), this);




    }
}

package fr.lordmeca.capturemobs.listeners;

import fr.lordmeca.capturemobs.Main;
import fr.lordmeca.capturemobs.managers.FightMonstersManager;
import fr.lordmeca.capturemobs.managers.MonstersManager;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.raidstone.wgevents.WorldGuardEvents;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class PlayerWalkEvent implements Listener {

    public static List<String> playersInTallGrass = new ArrayList<String>();

    @EventHandler
    public void playerWalk(PlayerMoveEvent event) {

        Player player = event.getPlayer();

        if (event.getFrom().getX() != event.getTo().getX() || event.getFrom().getY() != event.getTo().getY() || event.getFrom().getZ() != event.getTo().getZ()) {

            /*Iterator<ProtectedRegion> iterator = WorldGuard.getRegionManager(player.getWorld()).getApplicableRegions(player.getLocation()).iterator();
            while (iterator.hasNext()) {
                ProtectedRegion r = iterator.next();

                if (r.getId().contains("hautesherbes")) {*/
            for(int i = 1; i < 500; i++) {
                if (WorldGuardEvents.isPlayerInAnyRegion(player.getUniqueId(), "tallgrass"+i)) {


                    if (playersInTallGrass.contains(player.getName())) {
                        return;
                    }

                    playersInTallGrass.add(player.getName());
                    int chance = (int) (Math.random() * 100);
                    //player.sendMessage("C: §l"+chance);
                    //player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§l" + chance));

                    if (chance <= 1) {

                        if (player.getInventory().getItemInMainHand().getItemMeta().getLore() == null) return;

                        String monster = player.getInventory().getItemInMainHand().getItemMeta().getLore().get(7).replace("§d", "");

                        if (player.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals(Main.getInstance().getConfig().getString("item.captureballs.name_mob_and_full").replace("{mob}", monster))) {

                            int level = Integer.parseInt(player.getInventory().getItemInMainHand().getItemMeta().getLore().get(1).replace("§e", ""));
                            int id = Integer.parseInt(player.getInventory().getItemInMainHand().getItemMeta().getLore().get(5).replace("#", "").replace("§f", ""));
                            int health = Integer.parseInt(player.getInventory().getItemInMainHand().getItemMeta().getLore().get(9).replace("§c", ""));

                            String line3 = player.getInventory().getItemInMainHand().getItemMeta().getLore().get(3).replace("§7", "").replace("/", "");
                            String[] exp = line3.split("§9");

                            String type = player.getInventory().getItemInMainHand().getItemMeta().getLore().get(11).replace("§a", "");

                            if (health == 0) {
                                event.setCancelled(true);
                                player.sendMessage(Main.getInstance().prefix + " " + Main.getInstance().getConfig().getString("messages.throw_monsters_when_ko"));
                                return;

                            }

                            String e = Main.getInstance().monsters.stream().skip(new Random().nextInt(Main.getInstance().monsters.size())).findFirst().get();

                            Location enemyLoc = new Location(player.getWorld(), player.getLocation().getX() + 2, player.getLocation().getY(), player.getLocation().getZ());

                            Entity entity = create(EntityType.valueOf(e), player.getLocation());
                            Entity enemy = Bukkit.getWorld(player.getWorld().getName()).spawnEntity(enemyLoc, entity.getType());
                            LivingEntity livingEntity = (LivingEntity) enemy;
                            livingEntity.setAI(false);

                            int luckShinyDrop = (int) (Math.random() * 100);
                            //player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§a§l" + luckShinyDrop));player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§a§l" + luckShinyDrop));
                            if (luckShinyDrop <= 50) {

                                player.sendMessage("§9Vous êtes tombé sur un §b§lSHINY");

                                livingEntity.setGlowing(true);
                                Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();
                                Team team = board.getTeam("shiny");


                                team.addEntry(enemy.getUniqueId().toString());

                                FightMonstersManager.getInstance().fightMonster(player, enemy, health, level, monster, level);
                                MonstersManager.getInstance().spawnMonster("§f" + monster, EntityType.valueOf(monster), player, player.getLocation(), id, level, health, monster, exp, type);
                                //MonstersManager.getInstance().spawnMonster("Test", entity.getType(), player, player.getLocation(), id, level, health, monster, exp, type);


                                player.sendTitle(" ", "§6Un monstre vous a trouvé !", 5, 10, 5);


                                return;
                            }

                            FightMonstersManager.getInstance().fightMonster(player, enemy, health, level, monster, level);
                            MonstersManager.getInstance().spawnMonster("§f" + monster, EntityType.valueOf(monster), player, player.getLocation(), id, level, health, monster, exp, type);
                            //MonstersManager.getInstance().spawnMonster("Test", entity.getType(), player, player.getLocation(), id, level, health, monster, exp, type);


                            player.sendTitle(" ", Main.getInstance().getConfig().getString("messages.monsterFoundYou"), 5, 10, 5);


                            return;
                        }

                    }
                    playersInTallGrass.remove(player.getName());


                    return;

                }
                return;
            }


        }







    }

    public static Entity create(EntityType entityType, Location location) {
        try {
            // We get the craftworld class with nms so it can be used in multiple versions
            Class<?> craftWorldClass = getNMSClass("org.bukkit.craftbukkit.", "CraftWorld");

            // Cast the bukkit world to the craftworld
            Object craftWorldObject = craftWorldClass.cast(location.getWorld());

            // Create variable with the method that creates the entity
            // https://hub.spigotmc.org/stash/projects/SPIGOT/repos/craftbukkit/browse/src/main/java/org/bukkit/craftbukkit/CraftWorld.java#896
            Method createEntityMethod = craftWorldObject.getClass().getMethod("createEntity", Location.class, Class.class);

            // Attempt to invoke the method that creates the entity itself. This returns a net.minecraft.server entity
            Object entity = createEntityMethod.invoke(craftWorldObject, location, entityType.getEntityClass());

            // finally we run the getBukkitEntity method in the entity class to get a usable object
            return (Entity) entity.getClass().getMethod("getBukkitEntity").invoke(entity);
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException exception) {
            exception.printStackTrace();
        }


        // If something went wrong we just return null
        return null;
    }

    private static Class<?> getNMSClass(String prefix, String nmsClassString) throws ClassNotFoundException {
        // Getting the version by splitting the package
        String version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3] + ".";

        // Combining the prefix + version + nmsClassString for the full class path
        String name = prefix + version + nmsClassString;
        return Class.forName(name);
    }

}

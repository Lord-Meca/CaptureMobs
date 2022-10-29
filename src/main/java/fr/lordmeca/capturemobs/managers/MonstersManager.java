package fr.lordmeca.capturemobs.managers;

import fr.lordmeca.capturemobs.Main;
import fr.lordmeca.capturemobs.utils.ItemBuilder;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.io.IOException;
import java.util.Map;

public class MonstersManager {

    private static MonstersManager instance = new MonstersManager();

    public static MonstersManager getInstance() {
        return instance;
    }

    public static boolean mount = false;
    public static Entity mountEntity = null;

    public void spawnMonster(String name, EntityType monster, Player player, Location captureBallHitLoc, int id, int level, int health, String monsterString, String[] exp, String type){

        Entity entity = Bukkit.getWorld(player.getWorld().getName()).spawnEntity(captureBallHitLoc, monster);

        LivingEntity livingEntity = (LivingEntity) entity;

        livingEntity.setAI(true);

        if(monster.equals(EntityType.WOLF)){

            entity.setPassenger(player);
            mount = true;
            mountEntity = entity;

        }

        if(type.equals("SHINY")) {

            livingEntity.setGlowing(true);
            Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();
            Team team = board.getTeam("shiny");
            team.addEntry(entity.getUniqueId().toString());
            entity.setCustomName(name + " #" + id + " §7- §eLvl." + level + " §7- §b§lSHINY §7- §8§o" + player.getName());
            entity.setCustomNameVisible(true);

            Main.getInstance().fightAllyMonster.put(player.getName(), entity);

            Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
                player.getInventory().setItemInMainHand(new ItemBuilder(Main.getInstance().getConfig().getString("item.captureballs.material"))
                        .setName(Main.getInstance().getConfig().getString("item.captureballs.name_mob_and_empty").replace("{mob}", monsterString))
                        .setAmount(1)
                        .setLore("§7Niveaux :", "§e" + level,
                                "§7Expériences :", "§b" + exp[0].replace("§b", "") + "§7/§9" + exp[1].replace("§9", ""),
                                "§7Identifiant :", "§f#" + id,
                                "§7Monstre :", "§d" + monster,
                                "§7Vies :", "§c" + health, "§7Type :", "§a" + type)
                        .build());

                Main.getInstance().throwingMonster.remove(player.getName());
            }, 2);

            return;
        }
            entity.setCustomName(name + " #" + id + " §7- §eLvl." + level + " §7- §8§o" + player.getName());
            entity.setCustomNameVisible(true);



            Main.getInstance().fightAllyMonster.put(player.getName(), entity);
            Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
                player.getInventory().setItemInMainHand(new ItemBuilder(Main.getInstance().getConfig().getString("item.captureballs.material"))
                        .setName(Main.getInstance().getConfig().getString("item.captureballs.name_mob_and_empty").replace("{mob}", monsterString))
                        .setAmount(1)
                        .setLore("§7Niveaux :", "§e" + level,
                                "§7Expériences :", "§b" + exp[0].replace("§b", "") + "§7/§9" + exp[1].replace("§9", ""),
                                "§7Identifiant :", "§f#" + id,
                                "§7Monstre :", "§d" + monster,
                                "§7Vies :", "§c" + health, "§7Type :", "§a" + type)
                        .build());
                Main.getInstance().throwingMonster.remove(player.getName());
            }, 2);






    }

    public void healMonsterInHand(Player player, Block block){

        if(player.getInventory().getItemInMainHand().getType() != Material.SNOWBALL) {
            player.sendMessage(Main.getInstance().prefix + " " + Main.getInstance().getConfig().getString("messages.youMustHoldFullCaptureBall"));
            return;
        }
        if(player.getInventory().getItemInMainHand().getItemMeta().getLore() == null) {
            player.sendMessage(Main.getInstance().prefix + " " + Main.getInstance().getConfig().getString("messages.youMustHoldFullCaptureBall"));
            return;
        }

        if(player.getInventory().getItemInMainHand().getType() == Material.SNOWBALL){
            String monster = player.getInventory().getItemInMainHand().getItemMeta().getLore().get(7).replace("§d", "");
            if(player.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals(Main.getInstance().getConfig().getString("item.captureballs.name_mob_and_full").replace("{mob}", monster))){
                String name = player.getInventory().getItemInMainHand().getItemMeta().getDisplayName();
                int level = Integer.parseInt(player.getInventory().getItemInMainHand().getItemMeta().getLore().get(1).replace("§e", ""));
                int id = Integer.parseInt(player.getInventory().getItemInMainHand().getItemMeta().getLore().get(5).replace("§f", "").replace("#", ""));
                int health = Integer.parseInt(player.getInventory().getItemInMainHand().getItemMeta().getLore().get(9).replace("§c", "").replace("#", ""));

                String line3 = player.getInventory().getItemInMainHand().getItemMeta().getLore().get(3).replace("§7", "").replace("/", "");
                String type = player.getInventory().getItemInMainHand().getItemMeta().getLore().get(11).replace("§a", "");

                String[] exp = line3.split("§9");


                if(level <= 29) {

                    if(health == 50){
                        player.sendMessage(Main.getInstance().prefix+" " + Main.getInstance().getConfig().getString("messages.monsterAlreadyHealed"));
                        return;
                    }

                    health = 50;

                    player.setItemInHand(new ItemBuilder(Material.SNOWBALL.toString())
                            .setName(name)
                            .setLore("§7Niveaux :", "§e" + level,
                                    "§7Expériences :", "§b"+exp[0].replace("§b", "")+"§7/§9"+exp[1].replace("§9", ""),
                                    "§7Identifiant :", "§f#" + id,
                                    "§7Monstre :", "§d" + monster,
                                    "§7Vies :", "§c" + health,
                                    "§7Type :", "§a"+type)
                            .build());

                    player.updateInventory();

                    player.sendMessage(Main.getInstance().prefix+" " + Main.getInstance().getConfig().getString("messages.monsterHealed").replace("{monster}", monster).replace("{id}", ""+id));

                    player.spawnParticle(Particle.HEART, new Location(player.getWorld(), block.getLocation().getX(), block.getLocation().getY() + 1, block.getLocation().getZ()), 1);

                }

                if(level >= 50) {

                    if(health == 200){
                        player.sendMessage(Main.getInstance().prefix+" " + Main.getInstance().getConfig().getString("messages.monsterAlreadyHealed"));
                        return;
                    }

                    health = 200;

                    player.setItemInHand(new ItemBuilder(Material.SNOWBALL.toString())
                            .setName(name)
                            .setLore("§7Niveaux :", "§e" + level, "§7Expériences :", "§b"+exp[0].replace("§b", "")+"§7/§9"+exp[1].replace("§9", ""), "§7Identifiant :", "§f#" + id, "§7Monstre :", "§d" + monster,
                                    "§7Vies :", "§c" + health,
                                    "§7Type :", "§a" + type)
                            .build());

                    player.updateInventory();

                    player.sendMessage(Main.getInstance().prefix+" " + Main.getInstance().getConfig().getString("messages.monsterHealed").replace("{monster}", monster).replace("{id}", ""+id));

                    player.spawnParticle(Particle.HEART, new Location(player.getWorld(), block.getLocation().getX(), block.getLocation().getY() + 1, block.getLocation().getZ()), 1, 1);

                }

                if(level > 30 && level < 50) {

                    if(health == 100){
                        player.sendMessage(Main.getInstance().prefix+" " + Main.getInstance().getConfig().getString("messages.monsterAlreadyHealed"));
                        return;
                    }

                    health = 100;

                    player.getInventory().setItemInMainHand(new ItemBuilder(Material.SNOWBALL.toString())
                            .setName(name)
                            .setLore("§7Niveaux :", "§e" + level, "§7Expériences :", "§b"+exp[0].replace("§b", "")+"§7/§9"+exp[1].replace("§9", ""), "§7Identifiant :", "§f#" + id, "§7Monstre :", "§d" + monster, "§7Vies :", "§c" + health,
                                    "§7Type :", "§a"+type)
                            .build());

                    player.updateInventory();

                    player.sendMessage(Main.getInstance().prefix+" §7Le monstre §e"+monster+" #"+id + " §7a été §asoigné §7!");

                    //Bukkit.getWorld(player.getWorld().getName()).playEffect(new Location(player.getWorld(), block.getLocation().getX(), block.getLocation().getY() + 1, block.getLocation().getZ()), Effect.HEART, Integer.MAX_VALUE);
                    player.spawnParticle(Particle.HEART, new Location(player.getWorld(), block.getLocation().getX(), block.getLocation().getY() + 1, block.getLocation().getZ()), 1, 1);

                }
            } else {
                player.sendMessage(Main.getInstance().prefix+" §cVous devez tenir dans la main une Capture Ball pleine !");
            }
        }


    }

    public void catchMonster(Player player, Entity mob, YamlConfiguration playersConfig){

        if(mob.getCustomName() != null) {


            if (!mob.getCustomName().contains("§8§o" + player.getName())) {

                player.sendMessage(Main.getInstance().prefix + " " + Main.getInstance().getConfig().getString("messages.cantCatchThisMonster"));
                return;

            }
        }

        if(mob.getCustomName() == null) {

            String mobName = mob.getName().toUpperCase().replace(" ", "_");

            if(mobName.equals("MOOSHROOM")){
                mobName = "MUSHROOM_COW";
            }

            Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();
            Team team = board.getTeam("shiny");
            String type = "Normal";
            if(team.hasEntry(mob.getUniqueId().toString())){

                type = "SHINY";

            }

            mob.remove();
            try {
                FileManager.getInstance().registerNewPlayerMobInFile(player, mobName);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            int id = playersConfig.getInt("mobs.alls." + mobName);
            int level;
            int health;
            int exp = 0;
            int expRequiresToLevelUp = 100;


            player.getInventory().addItem(
                    new ItemBuilder(Main.getInstance().getConfig().getString("item.captureballs.material"))
                            .setName(Main.getInstance().getConfig().getString("item.captureballs.name_mob_and_full").replace("{mob}", mobName))
                            .setAmount(1)
                            .setLore("§7Niveaux :", "§e1",
                                    "§7Expériences :", "§b"+exp+"§7/§9"+expRequiresToLevelUp,
                                    "§7Identifiant :", "§f#" + id,
                                    "§7Monstre :", "§d" + mobName,
                                    "§7Vies :", "§c50",
                                    "§7Type :", "§a"+type)
                            .build());

            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§7Vous venez d'attraper un §e" + mobName + " §7!"));
            Main.getInstance().inMonsterCapture.remove(player.getName());
        }

    }
}

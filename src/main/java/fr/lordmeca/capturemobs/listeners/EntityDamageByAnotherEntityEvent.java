package fr.lordmeca.capturemobs.listeners;

import fr.lordmeca.capturemobs.Main;
import fr.lordmeca.capturemobs.managers.*;
import fr.lordmeca.capturemobs.utils.ItemBuilder;
import fr.lordmeca.capturemobs.utils.JsonMessageBuilder;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.io.File;
import java.io.IOException;
import java.util.Random;

public class EntityDamageByAnotherEntityEvent implements Listener {



    @EventHandler
    public void captureBallThrow(EntityDamageByEntityEvent event) throws IOException {

        if(event.getEntity() instanceof Pig){

            if(event.getDamager() instanceof WitherSkull){
                event.setCancelled(true);

            }



        }

        if(!(event.getDamager() instanceof Snowball)) return;

        Snowball projectile = (Snowball) event.getDamager();
        Entity mob = event.getEntity();


        if(projectile.getShooter() instanceof Player) {


            Player shooter = (Player) projectile.getShooter();

            YamlConfiguration playersConfig = YamlConfiguration.loadConfiguration(new File("plugins/CaptureMobs/players", shooter.getName() + ".yml"));
            if(shooter.getInventory().getItemInMainHand() == null) return;
            if (shooter.getInventory().getItemInMainHand().getItemMeta() == null) return;
            if (shooter.getInventory().getItemInMainHand().getItemMeta().getDisplayName() == null) return;

            //if(shooter.getInventory().getItemInMainHand().getItemMeta().getLore() == null) return;





            if (shooter.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals(Main.getInstance().getConfig().getString("item.captureballs.name_no_mob_and_empty"))) {


                if (mob.getCustomName() != null) return;

                event.setCancelled(true);

                /*if (Main.getInstance().inMonsterCapture.contains(shooter.getName())) {
                    event.setCancelled(true);
                    shooter.sendMessage(Main.getInstance().prefix+" §cVous capturez déjà un monstre !");
                    return;
                }*/

                Main.getInstance().inMonsterCapture.add(shooter.getName());

                int chance = new Random().nextInt(10);

                if (chance > 5) {

                    shooter.spawnParticle(Particle.VILLAGER_HAPPY, new Location(shooter.getWorld(), mob.getLocation().getX(), mob.getLocation().getY() + 2, mob.getLocation().getZ()), 1);

                    Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {

                        shooter.spawnParticle(Particle.VILLAGER_HAPPY, new Location(shooter.getWorld(), mob.getLocation().getX(), mob.getLocation().getY() + 2, mob.getLocation().getZ()), 1);

                        Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {

                            shooter.spawnParticle(Particle.HEART, new Location(shooter.getWorld(), mob.getLocation().getX(), mob.getLocation().getY() + 2, mob.getLocation().getZ()), 1);

                            MonstersManager.getInstance().catchMonster(shooter, mob, playersConfig);

                        }, 20);
                    }, 20);

                }

                if (chance < 5) {


                    shooter.spawnParticle(Particle.VILLAGER_HAPPY, new Location(shooter.getWorld(), mob.getLocation().getX(), mob.getLocation().getY() + 2, mob.getLocation().getZ()), 1);

                    Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {

                        shooter.spawnParticle(Particle.VILLAGER_HAPPY, new Location(shooter.getWorld(), mob.getLocation().getX(), mob.getLocation().getY() + 2, mob.getLocation().getZ()), 1);

                        Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {

                            shooter.spawnParticle(Particle.VILLAGER_ANGRY, new Location(shooter.getWorld(), mob.getLocation().getX(), mob.getLocation().getY() + 2, mob.getLocation().getZ()), 1);

                            String mobName = mob.getName().toUpperCase().replace(" ", "_");

                            shooter.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent( "§e" + mobName + " §cs'est enfui !"));

                            Vector unitVector = new Vector(mob.getLocation().getDirection().getX(), 0, mob.getLocation().getDirection().getZ());
                            unitVector = unitVector.normalize();
                            mob.setVelocity(unitVector.multiply(3));
                            Main.getInstance().inMonsterCapture.remove(shooter.getName());

                        }, 20);
                    }, 20);
                    return;
                }


            }

            if (shooter.getInventory().getItemInMainHand().getItemMeta().getLore() == null) return;

            String monster = shooter.getInventory().getItemInMainHand().getItemMeta().getLore().get(7).replace("§d", "");



            /*if(shooter.getInventory().getItemInMainHand().getItemMeta().getDisplayName().contains("VIDE")){

                int level = Integer.parseInt(shooter.getInventory().getItemInMainHand().getItemMeta().getLore().get(1).replace("§e", ""));
                int id = Integer.parseInt(shooter.getInventory().getItemInMainHand().getItemMeta().getLore().get(5).replace("#", "").replace("§f", ""));
                int health = Integer.parseInt(shooter.getInventory().getItemInMainHand().getItemMeta().getLore().get(9).replace("§c", ""));

                String line3 = shooter.getInventory().getItemInMainHand().getItemMeta().getLore().get(3).replace("§7", "").replace("/", "");
                String[] exp = line3.split("§9");

                String type = shooter.getInventory().getItemInMainHand().getItemMeta().getLore().get(11).replace("§a", "");

                shooter.setItemInHand(new ItemBuilder(Material.SNOWBALL.toString())
                        .setName(Main.getInstance().getConfig().getString("item.captureballs.name_mob_and_full").replace("{mob}", monster))
                        .setLore("§7Niveaux :", "§e" + level,
                                "§7Expériences :", "§b"+exp[0].replace("§b", "")+"§7/§9"+exp[1].replace("§9", ""),
                                "§7Identifiant :", "§f#" + id,
                                "§7Monstre :", "§d" + monster,
                                "§7Vies :", "§c" + health,
                                "§7Type :", "§a"+type)
                        .build());

            }*/

            if(shooter.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals(Main.getInstance().getConfig().getString("item.captureballs.name_mob_and_full").replace("{mob}", monster))) {


                if (mob != null) {



                    if (mob.getCustomName() == null) {

                        if(Main.getInstance().fightWithMonster.containsKey(shooter.getName())){
                            event.setCancelled(true);
                            shooter.sendMessage(Main.getInstance().prefix + " §cVous êtes déjà en combat contre un monstre !");
                            return;

                        }

                        event.setCancelled(true);
                        int level = Integer.parseInt(shooter.getInventory().getItemInMainHand().getItemMeta().getLore().get(1).replace("§e", ""));
                        int id = Integer.parseInt(shooter.getInventory().getItemInMainHand().getItemMeta().getLore().get(5).replace("#", "").replace("§f", ""));
                        int health = Integer.parseInt(shooter.getInventory().getItemInMainHand().getItemMeta().getLore().get(9).replace("§c", ""));

                        String line3 = shooter.getInventory().getItemInMainHand().getItemMeta().getLore().get(3).replace("§7", "").replace("/", "");
                        String[] exp = line3.split("§9");

                        String type = shooter.getInventory().getItemInMainHand().getItemMeta().getLore().get(11).replace("§a", "");

                        if (health <= 0) {
                            event.setCancelled(true);
                            shooter.sendMessage(Main.getInstance().prefix + " " + Main.getInstance().getConfig().getString("messages.throw_monsters_when_ko"));
                            return;

                        }

                        Main.getInstance().throwingMonster.add(shooter.getName());



                        FightMonstersManager.getInstance().fightMonster(shooter, mob, health, level, monster, level);

                        /*Bukkit.getScheduler().runTaskLater(Main.getInstance(), () ->{
                            MonstersManager.getInstance().spawnMonster("§f" + monster,
                                    EntityType.valueOf(monster), shooter, mob.getLocation(), id, level, health, monster, exp, type);
                        }, 5);*/


                    }

                    if (mob.getCustomName() == null) return;

                    if (mob.getCustomName().contains("§8§o" + shooter.getName()) && mob.getCustomName().contains("§eLvl")) {
                        event.setCancelled(true);
                        return;
                    }

                    String[] monsterCustomName = mob.getCustomName().split("§8§o");
                    event.setCancelled(true);

                    Player target = Bukkit.getPlayer(monsterCustomName[1]);

                    shooter.sendMessage(Main.getInstance().prefix + " §7Demande de combat envoyé à : §9" +target.getName());
                    target.sendMessage(Main.getInstance().prefix + " §7Demande de combat reçu de : §e" +shooter.getName());

                    target.spigot().sendMessage(new JsonMessageBuilder("§a§l[ACCEPTER]")
                            .setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                    new ComponentBuilder("§aAccepter la demande").create()))
                            .setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,
                                    "/capturemobs duels accept " + shooter.getName())).addExtra(
                                    new JsonMessageBuilder(" §7- §c§l[REFUSER]")
                                            .setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                                    new ComponentBuilder("§cRefuser la demande").create()))
                                            .setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,
                                                    "/capturemobs duels deny " + shooter.getName())).build())
                            .build());

                    PlayerMonstersDuelManager.getInstance().targetMonster.put(target.getName(), mob);

                }
            }

            assert mob != null;
            if(mob.getCustomName() == null) return;

            if (shooter.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals(Main.getInstance().getConfig().getString("item.captureballs.name_mob_and_empty").replace("{mob}", monster))) {

                if (mob.getCustomName().contains(monster)) {



                    event.setCancelled(true);

                    int level = Integer.parseInt(shooter.getInventory().getItemInMainHand().getItemMeta().getLore().get(1).replace("§e", ""));
                    int id = Integer.parseInt(shooter.getInventory().getItemInMainHand().getItemMeta().getLore().get(5).replace("#", "").replace("§f", ""));
                    int health = Integer.parseInt(shooter.getInventory().getItemInMainHand().getItemMeta().getLore().get(9).replace("§c", ""));
                    String type = shooter.getInventory().getItemInMainHand().getItemMeta().getLore().get(11).replace("§a", "");
                    String line3 = shooter.getInventory().getItemInMainHand().getItemMeta().getLore().get(3).replace("§7", "").replace("/", "");

                    String[] exp = line3.split("§9");

                    if (mob.getCustomName().contains("#" + id)) {

                        Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {


                            if (shooter.getInventory().getItemInMainHand().getItemMeta().getDisplayName().contains(monster)) {
                                mob.remove();
                                shooter.getInventory().setItemInMainHand(
                                        new ItemBuilder(Main.getInstance().getConfig().getString("item.captureballs.material"))
                                                .setName(Main.getInstance().getConfig().getString("item.captureballs.name_mob_and_full").replace("{mob}", monster))
                                                .setAmount(1)
                                                .setLore("§7Niveaux :", "§e" + level, "§7Expériences :",  "§b"+exp[0].replace("§b", "")+"§7/§9"+exp[1].replace("§9", ""), "§7Identifiant :", "§f#" + id, "§7Monstre :", "§d" + monster, "§7Vies :", "§c" + health,
                                                        "§7Type :", "§a"+type)
                                                .build());
                            }
                            return;

                        }, 5);
                        return;


                    } else {
                        event.setCancelled(true);
                        shooter.sendMessage(Main.getInstance().prefix + " §cVous ne pouvez pas attraper ce monstre !");
                        return;
                    }
                } else {
                    event.setCancelled(true);
                    shooter.sendMessage(Main.getInstance().prefix + " §cVous ne pouvez pas attraper ce monstre !");
                    return;
                }
            }




        }


    }

}

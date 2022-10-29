package fr.lordmeca.capturemobs.listeners;

import fr.lordmeca.capturemobs.Main;
import fr.lordmeca.capturemobs.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.WitherSkull;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;

public class PlayerProjectileLaunchEvent implements Listener {

    @EventHandler
    public void projectileLaunch(ProjectileLaunchEvent event){

        if(event.getEntity() instanceof WitherSkull){

            event.setCancelled(true);

        }

        if(event.getEntity() instanceof Snowball){
            if(event.getEntity().getShooter() instanceof Player){
                Player shooter = (Player) event.getEntity().getShooter();

                if(shooter.getInventory().getItemInMainHand().getItemMeta().getDisplayName() == null) return;

                String name = shooter.getInventory().getItemInMainHand().getItemMeta().getDisplayName();
                int amount = shooter.getInventory().getItemInMainHand().getAmount();

                if(Main.getInstance().throwingMonster.contains(shooter.getName())){

                    event.setCancelled(true);
                    return;

                }

                if (name.equals(Main.getInstance().getConfig().getString("item.captureballs.name_no_mob_and_empty"))) {

                    Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {


                        //if(shooter.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals(Main.getInstance().getConfig().getString("item.captureballs.name_mob_and_full").replace("{mob}", monster))) {



                        shooter.getInventory().setItemInMainHand(new ItemBuilder(Main.getInstance().getConfig().getString("item.captureballs.material"))
                                .setName(Main.getInstance().getConfig().getString("item.captureballs.name_no_mob_and_empty"))
                                .setAmount(amount).build());

                        ItemStack captureBall = shooter.getInventory().getItemInMainHand();

                        if(captureBall.getAmount() == 1){
                            shooter.getInventory().remove(captureBall);
                        }


                        captureBall.setAmount(captureBall.getAmount() - 1);




                    }, 2);

                }
                if(shooter.getInventory().getItemInMainHand().getItemMeta().getLore() == null) return;
                String monster = shooter.getInventory().getItemInMainHand().getItemMeta().getLore().get(7).replace("§d", "");

                int level = Integer.parseInt(shooter.getInventory().getItemInMainHand().getItemMeta().getLore().get(1).replace("§e", ""));
                int id = Integer.parseInt(shooter.getInventory().getItemInMainHand().getItemMeta().getLore().get(5).replace("#", "").replace("§f", ""));
                int health = Integer.parseInt(shooter.getInventory().getItemInMainHand().getItemMeta().getLore().get(9).replace("§c", ""));

                String line3 = shooter.getInventory().getItemInMainHand().getItemMeta().getLore().get(3).replace("§7", "").replace("/", "");

                String[] exp = line3.split("§9");

                String type = shooter.getInventory().getItemInMainHand().getItemMeta().getLore().get(11).replace("§a", "");

                //event.setCancelled(true);

                if(name.equals(Main.getInstance().getConfig().getString("item.captureballs.name_mob_and_full").replace("{mob}", monster))) {



                    Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {


                        //if(shooter.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals(Main.getInstance().getConfig().getString("item.captureballs.name_mob_and_full").replace("{mob}", monster))) {

                        if (health == 0) {

                            shooter.sendMessage(Main.getInstance().prefix + " " + Main.getInstance().getConfig().getString("messages.throw_monsters_when_ko"));
                            return;

                        }

                        shooter.getInventory().setItemInMainHand(new ItemBuilder(Main.getInstance().getConfig().getString("item.captureballs.material"))
                                .setName(Main.getInstance().getConfig().getString("item.captureballs.name_mob_and_full").replace("{mob}", monster))
                                .setAmount(1)
                                .setLore("§7Niveaux :", "§e" + level, "§7Expériences :", "§b"+exp[0].replace("§b", "")+"§7/§9"+exp[1].replace("§9", ""), "§7Identifiant :", "§f#" + id, "§7Monstre :", "§d" + monster, "§7Vies :", "§c" + health,
                                        "§7Type :", "§a"+type)
                                .build());


                    }, 2);
                }

                if(name.equals(Main.getInstance().getConfig().getString("item.captureballs.name_mob_and_empty").replace("{mob}", monster))) {

                    Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {


                        //if(shooter.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals(Main.getInstance().getConfig().getString("item.captureballs.name_mob_and_full").replace("{mob}", monster))) {



                        shooter.getInventory().setItemInMainHand(new ItemBuilder(Main.getInstance().getConfig().getString("item.captureballs.material"))
                                .setName(Main.getInstance().getConfig().getString("item.captureballs.name_mob_and_empty").replace("{mob}", monster))
                                .setAmount(1)
                                .setLore("§7Niveaux :", "§e" + level, "§7Expériences :", "§b"+exp[0].replace("§b", "")+"§7/§9"+exp[1].replace("§9", ""), "§7Identifiant :", "§f#" + id, "§7Monstre :", "§d" + monster, "§7Vies :", "§c" + health,
                                        "§7Type :", "§a"+type)
                                .build());


                    }, 2);

                }






            }

        }

    }

}

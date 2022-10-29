package fr.lordmeca.capturemobs.listeners;

import fr.lordmeca.capturemobs.Main;
import fr.lordmeca.capturemobs.utils.ItemBuilder;
import fr.lordmeca.capturemobs.managers.MonstersManager;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

import java.util.List;

public class PlayerProjectileHitEvent implements Listener {

    @EventHandler
    public void onCaptureBallThrow(ProjectileHitEvent event) {

        if (event.getEntity() instanceof Snowball) {


            if(event.getEntity().getShooter() instanceof Player){
                Player shooter = (Player) event.getEntity().getShooter();



                if(shooter.getInventory().getItemInMainHand() == null) return;
                if(shooter.getInventory().getItemInMainHand().getItemMeta() == null) return;
                if(shooter.getInventory().getItemInMainHand().getItemMeta().getLore() == null) return;

                String monster = shooter.getInventory().getItemInMainHand().getItemMeta().getLore().get(7).replace("§d", "");

                if(shooter.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals(Main.getInstance().getConfig().getString("item.captureballs.name_mob_and_full").replace("{mob}", monster))){




                    int level = Integer.parseInt(shooter.getInventory().getItemInMainHand().getItemMeta().getLore().get(1).replace("§e", ""));
                    int id = Integer.parseInt(shooter.getInventory().getItemInMainHand().getItemMeta().getLore().get(5).replace("#", "").replace("§f", ""));
                    int health = Integer.parseInt(shooter.getInventory().getItemInMainHand().getItemMeta().getLore().get(9).replace("§c", ""));

                    String line3 = shooter.getInventory().getItemInMainHand().getItemMeta().getLore().get(3).replace("§7", "").replace("/", "");

                    String[] exp = line3.split("§9");

                    String type = shooter.getInventory().getItemInMainHand().getItemMeta().getLore().get(11).replace("§a", "");

                    if(health <= 0){

                        shooter.sendMessage(Main.getInstance().prefix+" " +Main.getInstance().getConfig().getString("messages.throw_monsters_when_ko"));
                        return;

                    }

                    if(Main.getInstance().throwingMonster.contains(shooter.getName())){

                        event.setCancelled(true);
                        return;

                    }

                    Main.getInstance().throwingMonster.add(shooter.getName());

                    MonstersManager.getInstance().spawnMonster("§f" + monster, EntityType.valueOf(monster), shooter, event.getEntity().getLocation(), id, level, health, monster, exp, type);

                    return;




                }

            }

        }
    }

    private static final BlockFace[] axis = {
            BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST
    };

    private BlockFace yawToFace(float yaw) {
        return axis[Math.round(yaw / 90f) & 0x3].getOppositeFace();
    }

}

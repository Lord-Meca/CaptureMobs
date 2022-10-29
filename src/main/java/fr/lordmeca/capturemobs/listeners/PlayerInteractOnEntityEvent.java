package fr.lordmeca.capturemobs.listeners;

import fr.lordmeca.capturemobs.Main;
import fr.lordmeca.capturemobs.managers.MenusManager;
import fr.lordmeca.capturemobs.managers.MonstersManager;
import fr.lordmeca.capturemobs.utils.ItemBuilder;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.ItemFlag;

import java.util.HashSet;
import java.util.Random;

public class PlayerInteractOnEntityEvent implements Listener {

    @EventHandler
    public void playerInteractOnEntity(PlayerInteractAtEntityEvent event){

        Player player = event.getPlayer();
        Entity entityClicked = event.getRightClicked();

        if(entityClicked instanceof ArmorStand) {

            if (entityClicked.getCustomName().equals("§bSoigner vos monstres")) {

                event.setCancelled(true);

                MonstersManager.getInstance().healMonsterInHand(player, ((ArmorStand) entityClicked).getTargetBlock(null, 4));


            }

            if (entityClicked.getCustomName().equals("§cObjet aléatoire")) {

                event.setCancelled(true);

                entityClicked.remove();

                Location entityBlockLoc = entityClicked.getLocation();
                entityBlockLoc.setY(entityBlockLoc.getY() + 1.2);

                entityBlockLoc.getBlock().setType(Material.AIR);

                int chance = new Random().nextInt(10);

                if(chance < 3) {

                    player.getInventory().addItem(
                            new ItemBuilder(Main.getInstance().getConfig().getString("item.captureballs.material"))
                                    .setName(Main.getInstance().getConfig().getString("item.captureballs.name_no_mob_and_empty"))
                                    .setAmount(5).build());
                    return;
                }

                if(chance > 7){

                    player.getInventory().addItem(
                            new ItemBuilder(Main.getInstance().getConfig().getString("item.captureballs.material"))
                                    .setName(Main.getInstance().getConfig().getString("item.captureballs.name_no_mob_and_empty"))
                                    .setAmount(2).build());
                    return;

                }

                if(chance > 3 && chance < 7){

                    player.getInventory().addItem(new ItemBuilder(Material.DRAGON_BREATH.toString())
                            .setName("§8● §6Potion [1]")
                            .setAmount(2)
                            .addItemFlags(ItemFlag.HIDE_POTION_EFFECTS)
                            .build());

                    player.getInventory().addItem(new ItemBuilder(Material.DRAGON_BREATH.toString())
                            .setName("§8● §aPotion [2]")
                            .setAmount(1)
                            .addItemFlags(ItemFlag.HIDE_POTION_EFFECTS)
                            .build());

                }


            }


        }



    }

}

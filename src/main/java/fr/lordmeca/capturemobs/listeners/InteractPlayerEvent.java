package fr.lordmeca.capturemobs.listeners;

import fr.lordmeca.capturemobs.Main;
import fr.lordmeca.capturemobs.managers.MenusManager;
import fr.lordmeca.capturemobs.managers.MonstersManager;
import fr.lordmeca.capturemobs.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class InteractPlayerEvent implements Listener {

    public static String titleMenuHealMonster;

    @EventHandler
    public void interact(PlayerInteractEvent event){

        Player player = event.getPlayer();
        ItemStack it = event.getItem();

        if(it == null) return;
        if(player.getItemInHand() == null) return;
        if(player.getItemInHand().getItemMeta().getDisplayName() == null) return;




        if(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK){

            if(it.getType().equals(Material.DRAGON_BREATH)){

                if(it.getItemMeta().getDisplayName().equals("§8● §6Potion [1]")){

                    titleMenuHealMonster = "§8§l» §6Soigner votre monstre [1]";
                    MenusManager.getInstance().openGuiHealMonster(player);
                    return;

                }

                if(it.getItemMeta().getDisplayName().equals("§8● §aPotion [2]")){

                    titleMenuHealMonster = "§8§l» §aSoigner votre monstre [2]";
                    MenusManager.getInstance().openGuiHealMonster(player);

                }

            }

            if(it.getType().equals(Material.BOOK)){

                if(it.getItemMeta().getDisplayName().equals("§8§l» §aMonstersDex")){

                    if(MonstersManager.mount){

                        Entity entity = MonstersManager.mountEntity;

                        if(yawToFace(player.getLocation().getYaw()) == BlockFace.NORTH){

                            Vector unitVector = new Vector(0, 0, player.getLocation().getDirection().getZ());
                            unitVector = unitVector.normalize();
                            entity.setVelocity(unitVector.multiply(3));
                        }

                        if(yawToFace(player.getLocation().getYaw()) == BlockFace.SOUTH){

                            Vector unitVector = new Vector(0, 0, player.getLocation().getDirection().getZ());
                            unitVector = unitVector.normalize();
                            entity.setVelocity(unitVector.multiply(3));
                        }

                        if(yawToFace(player.getLocation().getYaw()) == BlockFace.WEST){

                            Vector unitVector = new Vector(player.getLocation().getDirection().getX(), 0, 0);
                            unitVector = unitVector.normalize();
                            entity.setVelocity(unitVector.multiply(3));
                        }

                        if(yawToFace(player.getLocation().getYaw()) == BlockFace.EAST){

                            Vector unitVector = new Vector(player.getLocation().getDirection().getX(), 0, 0);
                            unitVector = unitVector.normalize();
                            entity.setVelocity(unitVector.multiply(-3));
                        }

                    }
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

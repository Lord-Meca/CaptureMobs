package fr.lordmeca.capturemobs.listeners;

import fr.lordmeca.capturemobs.Main;
import fr.lordmeca.capturemobs.managers.PlayerMonstersDuelManager;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.raidstone.wgevents.WorldGuardEvents;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

public class PlayerCloseInventoryEvent implements Listener {

    @EventHandler
    public void playerCloseInventory(InventoryCloseEvent event){

        Player player = (Player) event.getPlayer();
        Inventory inv = event.getInventory();

        if(event.getView().getTitle().contains("§8§l» §eCombat contre")){

            for(int i = 1; i < 500; i++) {
                if (WorldGuardEvents.isPlayerInAnyRegion(player.getUniqueId(), "tallgrass" + i)) {

                    Main.getInstance().fightWithMonster.get(player.getName()).remove();
                    Entity monsterEnemy = Main.getInstance().fightWithMonster.get(player.getName());

                    if (monsterEnemy == null) return;

                    Main.getInstance().fightWithMonster.remove(player.getName());

                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§7Vous avez fui §c'" + monsterEnemy.getName() + "'"));

                    return;
                }

                Entity monsterEnemy = Main.getInstance().fightWithMonster.get(player.getName());

                if (monsterEnemy == null) return;

                Main.getInstance().fightWithMonster.remove(player.getName());

                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§7Vous avez fui §c'" + monsterEnemy.getName() + "'"));

            }
        }

        /*if(event.getView().getTitle().contains("§8§l» §aDuel")){

            String[] shooterPlayerName = event.getInventory().getItem(10).getItemMeta().getDisplayName().split(":".replace(" ", "").replace("§b", ""));
            String[] targetPlayerName = event.getInventory().getItem(16).getItemMeta().getDisplayName().split(":".replace(" ", "").replace("§c", ""));



            String targetMonster = event.getInventory().getItem(16).getItemMeta().getLore().get(5).replace("§d", "");
            String shooterMonster = event.getInventory().getItem(10).getItemMeta().getLore().get(5).replace("§d", "");

            int targetMonsterHealth = Integer.parseInt(event.getInventory().getItem(16).getItemMeta().getLore().get(1).replace("§c", ""));
            int shooterMonsterHealth = Integer.parseInt(event.getInventory().getItem(10).getItemMeta().getLore().get(1).replace("§c", ""));

            int targetMonsterLevel = Integer.parseInt(event.getInventory().getItem(16).getItemMeta().getLore().get(3).replace("§e", ""));
            int shooterMonsterLevel = Integer.parseInt(event.getInventory().getItem(10).getItemMeta().getLore().get(3).replace("§e", ""));

            String targetMonsterType = event.getInventory().getItem(16).getItemMeta().getLore().get(7).replace("§a", "");
            String shooterMonsterType = event.getInventory().getItem(10).getItemMeta().getLore().get(7).replace("§a", "");

            PlayerMonstersDuelManager.getInstance().startDuel(Bukkit.getPlayer(shooterPlayerName[1].replace(" ", "").replace("§b", "")), Bukkit.getPlayer(targetPlayerName[1].replace(" ", "").replace("§c", "")), shooterMonster, targetMonster, targetMonsterHealth,
                    shooterMonsterHealth, targetMonsterLevel, shooterMonsterLevel, targetMonsterType, shooterMonsterType);


        }*/

    }

}

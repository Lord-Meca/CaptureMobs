package fr.lordmeca.capturemobs.managers;

import fr.lordmeca.capturemobs.Main;
import fr.lordmeca.capturemobs.listeners.InteractPlayerEvent;
import fr.lordmeca.capturemobs.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.*;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.material.SpawnEgg;

import static org.bukkit.entity.EntityType.PIG;
import static org.bukkit.entity.EntityType.ZOMBIE;

public class MenusManager {

    private static MenusManager instance = new MenusManager();

    FileConfiguration config = Main.getInstance().getConfig();


    public static MenusManager getInstance() {
        return instance;
    }

    public void openGuiManageMonster(Player player, String mob){

        int id = Integer.parseInt(player.getItemInHand().getItemMeta().getLore().get(5).replace("#", "").replace("§f", ""));

        Inventory menu = Bukkit.createInventory(null, InventoryType.DISPENSER, "§8§l» §dGérer le monstre §8§l- §f#"+id+"."+mob);

        menu.setItem(4, new ItemBuilder(Material.BARRIER.toString()).setName("§8§l● §cRelacher le monstre").build());

        player.openInventory(menu);

    }

    public void openGuiHealMonster(Player player){



        Inventory menu = Bukkit.createInventory(null, InventoryType.DISPENSER, InteractPlayerEvent.titleMenuHealMonster);

        for(int i = 0; i < 4; i++){

            menu.setItem(i, new ItemBuilder(Material.RED_STAINED_GLASS_PANE.toString(), (byte)14).setName(" ").build());

        }

        for(int i = 5; i < 7; i++){

            menu.setItem(i, new ItemBuilder(Material.RED_STAINED_GLASS_PANE.toString(), (byte)14).setName(" ").build());


        }

        menu.setItem(8, new ItemBuilder(Material.PINK_DYE.toString()).setName("§8§l» §dSoigner").build());
        menu.setItem(7, new ItemBuilder(Material.RED_STAINED_GLASS_PANE.toString(), (byte)14).setName(" ").build());

        player.openInventory(menu);

    }

    public Integer getAmount(Player player, ItemStack itemStack){

        if(player == null){

            return 0;

        }

        int amount = 0;
        for(int i = 0; i < 36; i++){

            ItemStack slot = player.getInventory().getItem(i);
            if(slot == null || !slot.isSimilar(itemStack)){

                continue;

            }

            amount += slot.getAmount();

        }

        return amount;

    }



}

package fr.lordmeca.capturemobs.managers;

import fr.lordmeca.capturemobs.Main;
import fr.lordmeca.capturemobs.listeners.PlayerWalkEvent;
import fr.lordmeca.capturemobs.utils.ItemBuilder;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.raidstone.wgevents.WorldGuardEvents;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class FightMonstersManager {

    private static FightMonstersManager instance = new FightMonstersManager();

    FileConfiguration config = Main.getInstance().getConfig();

    public static FightMonstersManager getInstance() {
        return instance;
    }

    public void fightMonster(Player player, Entity enemy, int allyHealth, int allyLevel, String allyName, int level){

        LivingEntity livingEntityEnemy = (LivingEntity) enemy;


        String categorie = "fight.attacks."+allyName+".";
        String levelCategorie = "beetween_level_1_and_30";

        if(Main.getInstance().getConfig().getString(categorie + "items.1.name") == null){
            player.sendMessage(Main.getInstance().prefix + " " + Main.getInstance().getConfig().getString("messages.monsterDontHaveAttacks"));
            return;
        }

        if(level > 30 && level < 51){

            levelCategorie = "beetween_level_30_and_50";

        }

        if(level > 50 && level < 101){
            levelCategorie = "beetween_level_50_and_100";

        }

        Main.getInstance().fightWithMonster.put(player.getName(), enemy);
        Main.getInstance().itIsPlayerTurnInFight.put(player.getName(), true);

        Inventory menu = Bukkit.createInventory(null, InventoryType.DISPENSER, "§8§l» §eCombat contre : §l"+enemy.getName());

        String allyNameUpperCase = allyName.toUpperCase();
        String enemyNameUpperCase = enemy.getName().toUpperCase();

        /*ItemStack egg1 = new ItemStack(Material.CHICKEN_SPAWN_EGG, 1);
        egg1.setDurability((short) EntityType.valueOf(allyNameUpperCase).getTypeId());


        ItemStack egg2 = new ItemStack(Material.CHICKEN_SPAWN_EGG, 1);
        egg2.setDurability((short) EntityType.valueOf(enemyNameUpperCase).getTypeId());


        menu.setItem(0, egg1);
        menu.setItem(2, egg2);*/

        menu.setItem(0, new ItemBuilder(Material.CHICKEN_SPAWN_EGG.toString())
                .setName("§7Allié §8» §a"+allyName)
                .setLore("§7Vies: §c"+allyHealth,
                        "§7Niveaux : §e"+allyLevel)
                .build());
        menu.setItem(2, new ItemBuilder(Material.CHICKEN_SPAWN_EGG.toString())
                .setName("§7Ennemi §8» §c"+enemy.getName())
                .setLore("§7Vies: §c"+livingEntityEnemy.getHealth()).build());


        menu.setItem(1, new ItemBuilder(Material.RED_STAINED_GLASS_PANE.toString(), (byte)14).setName(" ").build());
        menu.setItem(3, new ItemBuilder(Material.RED_STAINED_GLASS_PANE.toString(), (byte)14).setName(" ").build());
        //menu.setItem(4, new ItemBuilder(Material.RED_STAINED_GLASS_PANE.toString(), (byte)14).setName(" ").build());
        menu.setItem(5, new ItemBuilder(Material.RED_STAINED_GLASS_PANE.toString(), (byte)14).setName(" ").build());

        menu.setItem(4, new ItemBuilder(Material.SNOWBALL.toString()).setName("§8» §eCapturer le monstre").build());

        menu.setItem(6, new ItemBuilder(config.getString(categorie+"items.1.material"))
                .setName(config.getString(categorie+"items.1.name"))
                .setLore("§7Dégâts: §c" + Main.getInstance().getConfig().getInt(categorie+levelCategorie+"."
                        + config.getString(categorie + "items.1.name") + ".attack1"))
                .build());
        menu.setItem(7, new ItemBuilder(config.getString(categorie+"items.2.material"))
                .setName(config.getString(categorie+"items.2.name")).setLore("§7Dégâts: §c" + Main.getInstance().getConfig().getInt(categorie+levelCategorie+"."
                        + config.getString(categorie + "items.2.name") + ".attack2"))
                .build());

        menu.setItem(8, new ItemBuilder(Material.FEATHER.toString()).setName("§8● §2Fuir").build());

        player.openInventory(menu);


    }

    public void checkIfWildernessMonsterIsDead(Player player, Inventory inv, LivingEntity livingMonsterEnemyEntity, String enemyMonsterName, String allyMonsterName, int level){

        if(livingMonsterEnemyEntity.getHealth() <= 0){

            String line3 = player.getInventory().getItemInMainHand().getItemMeta().getLore().get(3).replace("§7", "").replace("/", "");
            String[] exp = line3.split("§9");
            int expActual = Integer.parseInt(exp[0].replace("§b", ""));

            Main.getInstance().fightWithMonster.remove(player.getName());
            player.closeInventory();
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(Main.getInstance().getConfig().getString("messages.killWildernessMonster").replace("{enemy}", enemyMonsterName)));

            Random r = new Random();
            int expWin = r.nextInt((10 - 1) + 1) + 1;

            if(level > 50 && level < 101){
                expWin = r.nextInt((50 - 15) + 1) + 15;
            }

            if(level > 30 && level < 51){
                expWin = r.nextInt((25 - 5) + 1) + 5;
            }




            String name = player.getInventory().getItemInMainHand().getItemMeta().getDisplayName();
            level = Integer.parseInt(player.getInventory().getItemInMainHand().getItemMeta().getLore().get(1).replace("§e", ""));
            int id = Integer.parseInt(player.getInventory().getItemInMainHand().getItemMeta().getLore().get(5).replace("§f", "").replace("#", ""));
            int health = Integer.parseInt(player.getInventory().getItemInMainHand().getItemMeta().getLore().get(9).replace("§c", "").replace("#", ""));


            String type = player.getInventory().getItemInMainHand().getItemMeta().getLore().get(11).replace("§a", "");
            int finalXp = LevelsManager.getInstance().addXp(player, expActual, expWin, level, Integer.parseInt(exp[1].replace("§9", "")), allyMonsterName, type);
            player.setItemInHand(new ItemBuilder(Material.SNOWBALL.toString())
                    .setName(name)
                    .setLore("§7Niveaux :", "§e" + level,
                            "§7Expériences :", "§b"+finalXp+"§7/§9"+exp[1].replace("§9", ""),
                            "§7Identifiant :", "§f#" + id,
                            "§7Monstre :", "§d" + allyMonsterName,
                            "§7Vies :", "§c" + health,
                            "§7Type :", "§a"+type)
                    .build());

            player.updateInventory();

            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(Main.getInstance().getConfig().getString("messages.yourMonsterWonXp").replace("{ally}", allyMonsterName).replace("{xpwin}", ""+expWin)));

            Main.getInstance().throwingMonster.remove(player.getName());
            PlayerWalkEvent.playersInTallGrass.remove(player.getName());

            return;

        } else {



            level = Integer.parseInt(player.getInventory().getItemInMainHand().getItemMeta().getLore().get(1).replace("§e", ""));
            int health = Integer.parseInt(player.getInventory().getItemInMainHand().getItemMeta().getLore().get(9).replace("§c", ""));
            int id = Integer.parseInt(player.getInventory().getItemInMainHand().getItemMeta().getLore().get(5).replace("§f", "").replace("#", ""));
            String line3 = player.getInventory().getItemInMainHand().getItemMeta().getLore().get(3).replace("§7", "").replace("/", "");
            String monsterAllyName = player.getInventory().getItemInMainHand().getItemMeta().getLore().get(7).replace("§d", "");
            String type = player.getInventory().getItemInMainHand().getItemMeta().getLore().get(11).replace("§a", "");
            String[] exp = line3.split("§9");


            inv.setItem(2, new ItemBuilder(Material.CHICKEN_SPAWN_EGG.toString()).setName("§8● §d"+enemyMonsterName).setLore("§7Vies: §c"+livingMonsterEnemyEntity.getHealth()).build());

            player.updateInventory();

            int finalLevel = level;
            Bukkit.getScheduler().runTaskLater(Main.getInstance(), () ->{

                int randomDamage = ThreadLocalRandom.current().nextInt(5, 15);
                itsWildernessMonsterEnemyTurnInFight(player, inv, livingMonsterEnemyEntity, randomDamage, finalLevel, health, id, exp, monsterAllyName, type);

            }, 20);

        }

    }

    public void itsWildernessMonsterEnemyTurnInFight(Player player, Inventory inv, Entity enemy, int damage, int level, int health, int id, String[] exp, String allyMonsterName, String type){

        Entity ally = Main.getInstance().fightAllyMonster.get(player.getName());
        LivingEntity livingAllyEntity = (LivingEntity) ally;

        player.sendMessage(Main.getInstance().prefix + " " + Main.getInstance().getConfig().getString("messages.monsterInflictedDamageToPlayer")
                .replace("{enemy}", enemy.getName())
                .replace("{damage}", String.valueOf(damage)));

        Main.getInstance().itIsPlayerTurnInFight.put(player.getName(), true);

        livingAllyEntity.damage(1);

        health = health - damage;

        if(health <= 0){
            checkIfAllyMonsterIsDead(player, inv, (LivingEntity) enemy, enemy.getName(), allyMonsterName, level, health, type);
            return;
        }

        player.getInventory().setItemInMainHand(new ItemBuilder(Main.getInstance().getConfig().getString("item.captureballs.material"))
                .setName(Main.getInstance().getConfig().getString("item.captureballs.name_mob_and_empty").replace("{mob}", allyMonsterName))
                .setAmount(1)
                .setLore("§7Niveaux :", "§e"+level, "§7Expériences :", "§b"+exp[0].replace("§b", "")+"§7/§9"+exp[1].replace("§9", ""), "§7Identifiant :", "§f#"+id,
                        "§7Monstre :", "§d"+allyMonsterName, "§7Vies :", "§c"+health, "§7Type :", "§a"+type)
                .build());

        inv.setItem(0, new ItemBuilder(Material.CHICKEN_SPAWN_EGG.toString()).setName("§8● §a"+ally.getName())
                .setLore("§7Vies: §c" + health,
                        "§7Niveaux: §e" + level).build());

        player.updateInventory();

        Bukkit.getScheduler().runTaskLater(Main.getInstance(), () ->{

            player.sendMessage(Main.getInstance().prefix + " " + Main.getInstance().getConfig().getString("messages.turnMessageInPlayersDuel").replace("{player}", player.getName()));

        }, 20);

    }

    public void checkIfAllyMonsterIsDead(Player player, Inventory inv, LivingEntity livingEntityEnemy, String enemyMonsterName, String allyMonsterName, int level, int health, String type){

       if(health <= 0){

            String line3 = player.getInventory().getItemInMainHand().getItemMeta().getLore().get(3).replace("§7", "").replace("/", "");
            String[] exp = line3.split("§9");
            Main.getInstance().fightAllyMonster.get(player.getName()).remove();

            for(int i = 1; i < 500; i++) {
                if (WorldGuardEvents.isPlayerInAnyRegion(player.getUniqueId(), "tallgrass"+i)) {
                    Main.getInstance().fightWithMonster.get(player.getName()).remove();
                }
            }


            Main.getInstance().fightWithMonster.remove(player.getName());
            Main.getInstance().fightAllyMonster.remove(player.getName());
            Main.getInstance().throwingMonster.remove(player.getName());
            PlayerWalkEvent.playersInTallGrass.remove(player.getName());
            player.closeInventory();

            String name = player.getInventory().getItemInMainHand().getItemMeta().getDisplayName();
            level = Integer.parseInt(player.getInventory().getItemInMainHand().getItemMeta().getLore().get(1).replace("§e", ""));
            int id = Integer.parseInt(player.getInventory().getItemInMainHand().getItemMeta().getLore().get(5).replace("§f", "").replace("#", ""));

            player.setItemInHand(new ItemBuilder(Material.SNOWBALL.toString())
                    .setName(Main.getInstance().getConfig().getString("item.captureballs.name_mob_and_full").replace("{mob}", allyMonsterName))
                    .setLore("§7Niveaux :", "§e" + level,
                            "§7Expériences :", "§b"+exp[0].replace("§b", "")+"§7/§9"+exp[1].replace("§9", ""),
                            "§7Identifiant :", "§f#" + id,
                            "§7Monstre :", "§d" + allyMonsterName,
                            "§7Vies :", "§c" + health,
                            "§7Type :", "§a"+type)
                    .build());

            player.updateInventory();



            player.sendTitle(Main.getInstance().getConfig().getString("messages.yourMonsterLoose1"), Main.getInstance().getConfig().getString("messages.yourMonsterLoose2"), 10, 20, 10);


            return;

        }

    }
}

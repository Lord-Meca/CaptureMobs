package fr.lordmeca.capturemobs.listeners;

import fr.lordmeca.capturemobs.Main;
import fr.lordmeca.capturemobs.managers.FightMonstersManager;
import fr.lordmeca.capturemobs.managers.MenusManager;
import fr.lordmeca.capturemobs.managers.PlayerMonstersDuelManager;
import fr.lordmeca.capturemobs.utils.ItemBuilder;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.raidstone.wgevents.WorldGuardEvents;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;

public class PlayerInventoryClickEvent implements Listener {


    @EventHandler
    public void invClick(InventoryClickEvent event) throws IOException {

        Player player = (Player) event.getWhoClicked();
        ItemStack it = event.getCurrentItem();
        String invName = event.getView().getTitle();
        YamlConfiguration playersConfig = YamlConfiguration.loadConfiguration(new File("plugins/CaptureMobs/players", player.getName() + ".yml"));
        int slotClicked = event.getRawSlot();


        if (it == null) return;
        if (it.getItemMeta() == null) return;
        if (it.getItemMeta().getDisplayName() == null) return;


        if (event.getView().getTitle().contains("§8§l» §dGérer le monstre §8§l-")) {
            event.setCancelled(true);

            String[] id = invName.split("\\.");
            int monstersAmount = playersConfig.getInt("mobs.amount");
            int specificMonsterAmount = playersConfig.getInt("mobs.alls." + id[1]);

            if (it.getType().equals(Material.BARRIER)) {
                event.setCancelled(true);

                monstersAmount = monstersAmount - 1;
                specificMonsterAmount = specificMonsterAmount - 1;
                playersConfig.set("mobs.amount", monstersAmount);
                playersConfig.set("mobs.alls." + id[1], specificMonsterAmount);
                playersConfig.save(new File("plugins/CaptureMobs/players", player.getName() + ".yml"));

                player.getInventory().remove(player.getItemInHand());
                player.closeInventory();
                player.sendMessage("§cMonstre relaché !");


            }


        }

        if (event.getView().getTitle().contains("§8§l» §dGérer les soins §8§l-")) {
            event.setCancelled(true);
            String[] id = invName.split("\\.");

            if (it.getType().equals(Material.POTION)) {

                event.setCancelled(true);

            }

            if (it.getType().equals(Material.ARROW)) {

                event.setCancelled(true);


                MenusManager.getInstance().openGuiManageMonster(player, id[1]);

            }
        }

        if (event.getView().getTitle().contains("§8§l» §eCombat contre")) {
            event.setCancelled(true);


            int i = 0;

            //int health = Integer.parseInt(player.getInventory().getItemInMainHand().getItemMeta().getLore().get(9).replace("§c", ""));
            int level = Integer.parseInt(player.getInventory().getItemInMainHand().getItemMeta().getLore().get(1).replace("§e", ""));
            int health = Integer.parseInt(player.getInventory().getItemInMainHand().getItemMeta().getLore().get(9).replace("§c", ""));
            String allyName = player.getInventory().getItemInMainHand().getItemMeta().getLore().get(7).replace("§d", "");
            String type = player.getInventory().getItemInMainHand().getItemMeta().getLore().get(11).replace("§a", "");
            String itemName = it.getItemMeta().getDisplayName();
            String categorie = "fight.attacks." + allyName + ".";
            String levelCategorie = "beetween_level_1_and_30";

            Entity monsterEnemy = Main.getInstance().fightWithMonster.get(player.getName());
            LivingEntity livingMonsterEnemy = (LivingEntity) monsterEnemy;

            if (it.getType().equals(Material.FEATHER)) {

                if (itemName.equals("§8● §2Fuir")) {

                    if (WorldGuardEvents.isPlayerInAnyRegion(player.getUniqueId(), "hautesherbes1")) {

                        Main.getInstance().fightWithMonster.get(player.getName()).remove();
                        Main.getInstance().fightWithMonster.remove(player.getName());
                        player.closeInventory();
                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§7Vous avez fui §c'" + monsterEnemy.getName() + "'"));
                        return;

                    }

                    Main.getInstance().fightWithMonster.remove(player.getName());
                    player.closeInventory();
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§7Vous avez fui §c'" + monsterEnemy.getName() + "'"));
                    return;
                }

            }


            if (it.getType().equals(Material.RED_STAINED_GLASS_PANE)) {

                event.setCancelled(true);
                return;
            }

            if (it.getType().equals(Material.CHICKEN_SPAWN_EGG)) {
                event.setCancelled(true);
                return;
            }

            if (it.getType().equals(Material.SNOWBALL)) {
                if (itemName.contains("§eCaptureBalls")) {
                    event.setCancelled(true);
                    return;
                }

                if(itemName.equals("§8» §eCapturer le monstre")){

                    Main.getInstance().fightWithMonster.remove(player.getName());
                    player.closeInventory();
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§a§lBonne chance !"));
                    return;

                }
            }


            if (it.getType().equals(Material.valueOf(Main.getInstance().getConfig().getString(categorie + "items.1.material")))) {
                i = 1;
            }

            if (it.getType().equals(Material.valueOf(Main.getInstance().getConfig().getString(categorie + "items.2.material")))) {
                i = 2;
            }

            if (level > 30 && level < 51) {

                levelCategorie = "beetween_level_30_and_50";

            }
            if (level > 50 && level < 101) {
                levelCategorie = "beetween_level_50_and_100";

            }

            if (!Main.getInstance().itIsPlayerTurnInFight.get(player.getName())) {
                player.sendMessage(Main.getInstance().prefix + " §cCe n'est pas à votre tour d'attaquer !");
                return;
            }

            int damageByEnemyMonster = Main.getInstance().getConfig().getInt(categorie + levelCategorie + "." + itemName + ".attack" + i);

            player.sendMessage(Main.getInstance().prefix + " " + Main.getInstance().getConfig().getString("messages.playerInflictedDamageToWildernessMonster")
                    .replace("{yourMonster}", allyName)
                    .replace("{enemyMonster}", monsterEnemy.getName())
                    .replace("{damage}", String.valueOf(damageByEnemyMonster)));

            Main.getInstance().itIsPlayerTurnInFight.put(player.getName(), false);

            ((LivingEntity) monsterEnemy).damage(damageByEnemyMonster);

            FightMonstersManager.getInstance().checkIfWildernessMonsterIsDead(player, event.getInventory(), livingMonsterEnemy, monsterEnemy.getName(), allyName, level);
            FightMonstersManager.getInstance().checkIfAllyMonsterIsDead(player, event.getInventory(), livingMonsterEnemy, monsterEnemy.getName(), allyName, level, health, type);
        }

        if (invName.equals(InteractPlayerEvent.titleMenuHealMonster)) {

            if (it.getType().equals(Material.RED_STAINED_GLASS_PANE)) {
                event.setCancelled(true);
            }

            if (InteractPlayerEvent.titleMenuHealMonster.contains("[1]")) {
                if (it.getType().equals(Material.PINK_DYE)) {

                    event.setCancelled(true);

                    if (event.getInventory().getItem(4) == null) {
                        player.sendMessage(Main.getInstance().prefix + " §cVous devez mettre une Capture Ball pleine !");
                        return;
                    }

                    if (event.getInventory().getItem(4).getType() == null) {
                        player.sendMessage(Main.getInstance().prefix + " §cVous devez mettre une Capture Ball pleine !");
                        return;
                    }

                    if (event.getInventory().getItem(4).getType() == Material.SNOWBALL) {

                        if (event.getInventory().getItem(4).getItemMeta().getLore() == null) return;
                        if (event.getInventory().getItem(4).getItemMeta().getDisplayName() == null) return;

                        String name = event.getInventory().getItem(4).getItemMeta().getDisplayName();
                        String monster = event.getInventory().getItem(4).getItemMeta().getLore().get(7).replace("§d", "");
                        if (name.equals(Main.getInstance().getConfig().getString("item.captureballs.name_mob_and_full").replace("{mob}", monster))) {


                            int level = Integer.parseInt(event.getInventory().getItem(4).getItemMeta().getLore().get(1).replace("§e", ""));
                            int health = Integer.parseInt(event.getInventory().getItem(4).getItemMeta().getLore().get(9).replace("§c", ""));
                            int id = Integer.parseInt(event.getInventory().getItem(4).getItemMeta().getLore().get(5).replace("§f", "").replace("#", ""));

                            String line3 = event.getInventory().getItem(4).getItemMeta().getLore().get(3).replace("§7", "").replace("/", "");
                            String[] exp = line3.split("§9");

                            String type = event.getInventory().getItem(4).getItemMeta().getLore().get(11).replace("§a", "");

                            if (level > 0 && level < 30) {
                                if (health == 50) {
                                    player.sendMessage(Main.getInstance().prefix + " §cVotre monstre a déjà toute sa vie !");
                                    return;
                                }
                            }

                            if (level >= 30 && level < 50) {
                                if (health == 100) {
                                    player.sendMessage(Main.getInstance().prefix + " §cVotre monstre a déjà toute sa vie !");
                                    return;
                                }
                            }

                            if (level >= 50 && level < 100) {
                                if (health == 200) {
                                    player.sendMessage(Main.getInstance().prefix + " §cVotre monstre a déjà toute sa vie !");
                                    return;
                                }
                            }

                            event.getInventory().setItem(4, new ItemBuilder(Main.getInstance().getConfig().getString("item.captureballs.material"))
                                    .setName(Main.getInstance().getConfig().getString("item.captureballs.name_mob_and_full").replace("{mob}", monster))
                                    .setAmount(1)
                                    .setLore("§7Niveaux :", "§e" + level,
                                            "§7Expériences :", "§b" + exp[0].replace("§b", "") + "§7/§9" + exp[1].replace("§9", ""),
                                            "§7Identifiant :", "§f#" + id,
                                            "§7Monstre :", "§d" + monster,
                                            "§7Vies :", "§c" + healMonster(level, health, 20),
                                            "§7Type :", "§a" + type)
                                    .build());

                            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 2, 2);

                            ItemStack captureBallHealed = event.getInventory().getItem(4);

                            player.getInventory().addItem(captureBallHealed);

                            player.closeInventory();

                            ItemStack potion = player.getInventory().getItemInMainHand();

                            if (potion.getAmount() == 1) {

                                player.getInventory().remove(potion);


                            }

                            potion.setAmount(potion.getAmount() - 1);

                        }

                    }


                }
            }

            if (InteractPlayerEvent.titleMenuHealMonster.contains("[2]")) {
                if (it.getType().equals(Material.PINK_DYE)) {

                    event.setCancelled(true);

                    if (event.getInventory().getItem(4) == null) {
                        player.sendMessage(Main.getInstance().prefix + " §cVous devez mettre une Capture Ball pleine !");
                        return;
                    }

                    if (event.getInventory().getItem(4).getType() == null) {
                        player.sendMessage(Main.getInstance().prefix + " §cVous devez mettre une Capture Ball pleine !");
                        return;
                    }

                    if (event.getInventory().getItem(4).getType() == Material.SNOWBALL) {

                        if (event.getInventory().getItem(4).getItemMeta().getLore() == null) return;
                        if (event.getInventory().getItem(4).getItemMeta().getDisplayName() == null) return;

                        String name = event.getInventory().getItem(4).getItemMeta().getDisplayName();
                        String monster = event.getInventory().getItem(4).getItemMeta().getLore().get(7).replace("§d", "");
                        if (name.equals(Main.getInstance().getConfig().getString("item.captureballs.name_mob_and_full").replace("{mob}", monster))) {


                            int level = Integer.parseInt(event.getInventory().getItem(4).getItemMeta().getLore().get(1).replace("§e", ""));
                            int health = Integer.parseInt(event.getInventory().getItem(4).getItemMeta().getLore().get(9).replace("§c", ""));
                            int id = Integer.parseInt(event.getInventory().getItem(4).getItemMeta().getLore().get(5).replace("§f", "").replace("#", ""));

                            String line3 = event.getInventory().getItem(4).getItemMeta().getLore().get(3).replace("§7", "").replace("/", "");
                            String[] exp = line3.split("§9");

                            String type = event.getInventory().getItem(4).getItemMeta().getLore().get(11).replace("§a", "");


                            event.getInventory().setItem(4, new ItemBuilder(Main.getInstance().getConfig().getString("item.captureballs.material"))
                                    .setName(Main.getInstance().getConfig().getString("item.captureballs.name_mob_and_full").replace("{mob}", monster))
                                    .setAmount(1)
                                    .setLore("§7Niveaux :", "§e" + level,
                                            "§7Expériences :", "§b" + exp[0].replace("§b", "") + "§7/§9" + exp[1].replace("§9", ""),
                                            "§7Identifiant :", "§f#" + id,
                                            "§7Monstre :", "§d" + monster,
                                            "§7Vies :", "§c" + healMonster(level, health, 50),
                                            "§7Type :", "§a" + type)
                                    .build());

                            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 2, 2);

                            ItemStack captureBallHealed = event.getInventory().getItem(4);

                            player.getInventory().addItem(captureBallHealed);

                            player.closeInventory();

                            ItemStack potion = player.getInventory().getItemInMainHand();

                            if (potion.getAmount() == 1) {

                                player.getInventory().remove(potion);


                            }

                            potion.setAmount(potion.getAmount() - 1);

                        }

                    }


                }
            }


        }

        if (event.getView().getTitle().contains("§8§l» §aDuel")) {

            event.setCancelled(true);
            if (it.getItemMeta().getLore() == null) {
                event.setCancelled(true);
                return;
            }

            String levelCategorieTargetMonster = "beetween_level_1_and_30";
            String levelCategorieShooterMonster = "beetween_level_1_and_30";

            String itemName = it.getItemMeta().getDisplayName();
            String shooterMonsterName = event.getInventory().getItem(10).getItemMeta().getLore().get(5).replace("§d", "");
            String targetMonsterName = event.getInventory().getItem(16).getItemMeta().getLore().get(5).replace("§d", "");

            int targetMonsterHealth = Integer.parseInt(event.getInventory().getItem(16).getItemMeta().getLore().get(1).replace("§c", ""));
            int shooterMonsterHealth = Integer.parseInt(event.getInventory().getItem(10).getItemMeta().getLore().get(1).replace("§c", ""));

            String[] shooterPlayerName = event.getInventory().getItem(10).getItemMeta().getDisplayName().split(":".replace(" ", "").replace("§b", ""));
            String[] targetPlayerName = event.getInventory().getItem(16).getItemMeta().getDisplayName().split(":".replace(" ", "").replace("§c", ""));

            int a = 1;


            //Player target = Bukkit.getPlayer(Main.getInstance().playersMonstersDuel.get(player.getName()));

            if (it.getType().equals(Material.RED_STAINED_GLASS_PANE)) {

                event.setCancelled(true);

            }

            if (it.getType().equals(Material.CHICKEN_SPAWN_EGG)) {

                event.setCancelled(true);

            }

            if(!it.getItemMeta().getLore().get(1).contains(player.getName())){

                event.setCancelled(true);
                return;

            }

            if (PlayerMonstersDuelManager.getInstance().itsYourTurnAttackInPlayersDuel.get(player.getName())) {
                event.setCancelled(true);


                if(event.getInventory().getItem(10).getItemMeta().getDisplayName().contains(player.getName())){
                    event.setCancelled(true);

                    if(it.getType().equals(Material.valueOf(Main.getInstance().getConfig().getString("fight.attacks." + shooterMonsterName + "." + "items.1.material")))){

                        event.setCancelled(true);
                        a = 1;

                    }

                    if(it.getType().equals(Material.valueOf(Main.getInstance().getConfig().getString("fight.attacks." + shooterMonsterName + "." + "items.2.material")))){
                        event.setCancelled(true);
                        a = 2;

                    }

                    int damageToTargetMonster = Main.getInstance().getConfig().getInt("fight.attacks." + shooterMonsterName + "." + levelCategorieShooterMonster + "." + itemName + "." + "attack"+a);

                    PlayerMonstersDuelManager.getInstance().attackTargetMonster(player, Bukkit.getPlayer(targetPlayerName[1].replace(" ", "").replace("§c", "")), shooterMonsterHealth, targetMonsterHealth, damageToTargetMonster, event.getInventory());

                }

                if(event.getInventory().getItem(16).getItemMeta().getDisplayName().contains(player.getName())){

                    event.setCancelled(true);
                    if(it.getType().equals(Material.valueOf(Main.getInstance().getConfig().getString("fight.attacks." + targetMonsterName + "." + "items.1.material")))){
                        event.setCancelled(true);
                        a = 1;

                    }

                    if(it.getType().equals(Material.valueOf(Main.getInstance().getConfig().getString("fight.attacks." + targetMonsterName + "." + "items.2.material")))){
                        event.setCancelled(true);
                        a = 2;

                    }

                    int damageToTargetMonster = Main.getInstance().getConfig().getInt("fight.attacks." + targetMonsterName + "." + levelCategorieTargetMonster + "." + itemName + "." + "attack"+a);

                    PlayerMonstersDuelManager.getInstance().attackShooterMonster(Bukkit.getPlayer(shooterPlayerName[1].replace(" ", "").replace("§b", "")), player, shooterMonsterHealth, targetMonsterHealth, damageToTargetMonster, event.getInventory());

                }


            }
        }


    }








    public Integer healMonster(int level, int health, int amountHp){

        if(level > 0 && level < 30){

            if(health == 50){

                return 50;


            }

            if(health > 50){
                health = 50;
                return health;
            }

            health = health + amountHp;

            if(health > 50){
                health = 50;
                return health;
            }

        }

        if(level >= 30 && level < 50){

            if(health == 100){

                return 100;


            }

            if(health >= 100){
                health = 100;
                return health;
            }

            health = health + amountHp;

            if(health > 100){
                health = 100;
                return health;
            }

        }

        if(level >= 50 && level < 100){
            if(health == 200){

                return 200;


            }
            if(health >= 200){
                health = 200;
                return health;
            }

            health = health + amountHp;

            if(health >= 200){
                health = 200;
                return health;
            }
        }

        return health;

    }

}

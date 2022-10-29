package fr.lordmeca.capturemobs.managers;

import fr.lordmeca.capturemobs.Main;
import fr.lordmeca.capturemobs.utils.ItemBuilder;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class PlayerMonstersDuelManager {

    private static PlayerMonstersDuelManager instance = new PlayerMonstersDuelManager();

    public Map<String, Boolean> itsYourTurnAttackInPlayersDuel = new HashMap<>();

    public Map<String, String> playersMonstersDuel = new HashMap<>();

    public Map<String, Entity> targetMonster = new HashMap<>();

    FileConfiguration config = Main.getInstance().getConfig();

    public static PlayerMonstersDuelManager getInstance() {
        return instance;
    }

    public void startDuel(Player shooter, Player target, String shooterPlayerMobName, String targetPlayerMobName, int targetPlayerMobHealth, int shooterPlayerMobHealth, int targetPlayerMobLevel, int shooterPlayerMobLevel, String targetPlayerMobType, String shooterPlayerMobType){

        Inventory menu = Bukkit.createInventory(null, 27, "§8§l» §aDuel : §b" + shooter.getName() + " §a- §c"+target.getName());

        String categorieShooter = "fight.attacks."+shooterPlayerMobName+".";
        String categorieTarget = "fight.attacks."+targetPlayerMobName+".";
        String levelCategorieShooter = "beetween_level_1_and_30";
        String levelCategorieTarget = "beetween_level_1_and_30";

        itsYourTurnAttackInPlayersDuel.put(target.getName(), true);
        itsYourTurnAttackInPlayersDuel.put(shooter.getName(), false);

        target.sendMessage(Main.getInstance().getConfig().getString("messages.turnMessageInPlayersDuel").replace("{player}", target.getName()));
        shooter.sendMessage(Main.getInstance().getConfig().getString("messages.turnMessageInPlayersDuel").replace("{player}", target.getName()));

        for(int i = 0; i < 9; i++){

            menu.setItem(i, new ItemBuilder(Material.RED_STAINED_GLASS_PANE.toString(), (byte)14).setName(" ").build());

        }

        for(int i = 18; i < 27; i++){

            menu.setItem(i, new ItemBuilder(Material.RED_STAINED_GLASS_PANE.toString(), (byte)14).setName(" ").build());

        }

        menu.setItem(9, new ItemBuilder(Material.RED_STAINED_GLASS_PANE.toString(), (byte)14).setName(" ").build());
        menu.setItem(17, new ItemBuilder(Material.RED_STAINED_GLASS_PANE.toString(), (byte)14).setName(" ").build());
        menu.setItem(13, new ItemBuilder(Material.RED_STAINED_GLASS_PANE.toString(), (byte)14).setName(" ").build());

        menu.setItem(10, new ItemBuilder(Material.CHICKEN_SPAWN_EGG.toString()).setName("§7Monstre de : §b" + shooter.getName())
                .setLore("§7Vies :", "§c"+targetPlayerMobHealth,
                        "§7Niveaux :", "§e"+targetPlayerMobLevel,
                        "§7Créature :", "§d"+shooterPlayerMobName,
                        "§7Type :", "§a"+targetPlayerMobType).build());
        menu.setItem(16, new ItemBuilder(Material.CHICKEN_SPAWN_EGG.toString()).setName("§7Monstre de : §c" + target.getName())
                .setLore("§7Vies :", "§c"+shooterPlayerMobHealth,
                        "§7Niveaux :", "§e"+shooterPlayerMobLevel,
                        "§7Créature :", "§d"+targetPlayerMobName,
                        "§7Type :", "§a"+shooterPlayerMobType).build());

        menu.setItem(11, new ItemBuilder(config.getString(categorieShooter+"items.1.material"))
                .setName(config.getString(categorieShooter+"items.1.name"))
                .setLore("§7Dégâts: §c" + Main.getInstance().getConfig().getInt(categorieShooter+levelCategorieShooter+"."
                        + config.getString(categorieShooter + "items.1.name") + ".attack1"),
                        "§8§o" + shooter.getName())
                .build());
        menu.setItem(12, new ItemBuilder(config.getString(categorieShooter+"items.2.material"))
                .setName(config.getString(categorieShooter+"items.2.name")).setLore("§7Dégâts: §c" + Main.getInstance().getConfig().getInt(categorieShooter+levelCategorieShooter+"."
                        + config.getString(categorieShooter + "items.2.name") + ".attack2"),
                        "§8§o" + shooter.getName())
                .build());

        menu.setItem(15, new ItemBuilder(config.getString(categorieTarget+"items.1.material"))
                .setName(config.getString(categorieTarget+"items.1.name"))
                .setLore("§7Dégâts: §c" + Main.getInstance().getConfig().getInt(categorieTarget+levelCategorieTarget+"."
                        + config.getString(categorieTarget + "items.1.name") + ".attack1"),
                        "§8§o" + target.getName())
                .build());
        menu.setItem(14, new ItemBuilder(config.getString(categorieTarget+"items.2.material"))
                .setName(config.getString(categorieTarget+"items.2.name")).setLore("§7Dégâts: §c" + Main.getInstance().getConfig().getInt(categorieTarget+levelCategorieTarget+"."
                        + config.getString(categorieTarget + "items.2.name") + ".attack2"),
                        "§8§o" + target.getName())
                .build());


        target.openInventory(menu);
        shooter.openInventory(menu);

    }

    public void attackTargetMonster(Player shooter, Player target, int healthShooterMonster, int healthTargetMonster, int damage, Inventory inv){

        if(itsYourTurnAttackInPlayersDuel.get(shooter.getName())){

            int level = Integer.parseInt(target.getInventory().getItemInMainHand().getItemMeta().getLore().get(1).replace("§e", ""));
            int id = Integer.parseInt(target.getInventory().getItemInMainHand().getItemMeta().getLore().get(5).replace("#", "").replace("§f", ""));
            //int health = Integer.parseInt(shooter.getInventory().getItemInMainHand().getItemMeta().getLore().get(9).replace("§c", ""));

            String line3 = target.getInventory().getItemInMainHand().getItemMeta().getLore().get(3).replace("§7", "").replace("/", "");
            String[] exp = line3.split("§9");

            String type = target.getInventory().getItemInMainHand().getItemMeta().getLore().get(11).replace("§a", "");
            String monster = target.getInventory().getItemInMainHand().getItemMeta().getLore().get(7).replace("§d", "");

            Entity shooterMonster = Main.getInstance().fightAllyMonster.get(shooter.getName());
            Entity targetMonster = PlayerMonstersDuelManager.getInstance().targetMonster.get(target.getName());

            LivingEntity shooterMonsterLivingEntity = (LivingEntity) shooterMonster;
            LivingEntity targetMonsterLivingEntity = (LivingEntity) targetMonster;

            targetMonsterLivingEntity.damage(0.1);

            healthTargetMonster = healthTargetMonster - damage;



            inv.setItem(16, new ItemBuilder(Material.CHICKEN_SPAWN_EGG.toString()).setName("§7Monstre de : §c" + target.getName())
                    .setLore("§7Vies :", "§c"+healthTargetMonster,
                            "§7Niveaux :", "§e"+level,
                            "§7Créature :", "§d"+monster,
                            "§7Type :", "§a"+type).build());

            shooter.sendMessage(Main.getInstance().prefix + " " + Main.getInstance().getConfig().getString("messages.monsterInflictedDamageInPlayersDuel")
                    .replace("{attacker}", shooter.getName())
                    .replace("{damage}", String.valueOf(damage))
                    .replace("{victim}", target.getName()));
            target.sendMessage(Main.getInstance().prefix + " " + Main.getInstance().getConfig().getString("messages.monsterInflictedDamageInPlayersDuel")
                    .replace("{attacker}", shooter.getName())
                    .replace("{damage}", String.valueOf(damage))
                    .replace("{victim}", target.getName()));

            itsYourTurnAttackInPlayersDuel.remove(shooter.getName());
            itsYourTurnAttackInPlayersDuel.put(shooter.getName(), false);

            itsYourTurnAttackInPlayersDuel.remove(target.getName());
            itsYourTurnAttackInPlayersDuel.put(target.getName(), true);

            target.sendMessage(Main.getInstance().prefix + " " + Main.getInstance().getConfig().getString("messages.turnMessageInPlayersDuel").replace("{player}", target.getName()));
            shooter.sendMessage(Main.getInstance().prefix + " " + Main.getInstance().getConfig().getString("messages.turnMessageInPlayersDuel").replace("{player}", target.getName()));
            checkIfTargetMonsterIsDead(shooter, target, healthTargetMonster, targetMonster, monster);
        }

    }
    public void attackShooterMonster(Player shooter, Player target, int healthShooterMonster, int healthTargetMonster, int damage, Inventory inv){

        if(itsYourTurnAttackInPlayersDuel.get(target.getName())){

            int level = Integer.parseInt(shooter.getInventory().getItemInMainHand().getItemMeta().getLore().get(1).replace("§e", ""));
            int id = Integer.parseInt(shooter.getInventory().getItemInMainHand().getItemMeta().getLore().get(5).replace("#", "").replace("§f", ""));
            //int health = Integer.parseInt(shooter.getInventory().getItemInMainHand().getItemMeta().getLore().get(9).replace("§c", ""));

            String line3 = shooter.getInventory().getItemInMainHand().getItemMeta().getLore().get(3).replace("§7", "").replace("/", "");
            String[] exp = line3.split("§9");

            String type = shooter.getInventory().getItemInMainHand().getItemMeta().getLore().get(11).replace("§a", "");
            String monster = shooter.getInventory().getItemInMainHand().getItemMeta().getLore().get(7).replace("§d", "");

            Entity shooterMonster = Main.getInstance().fightAllyMonster.get(shooter.getName());
            Entity targetMonster = PlayerMonstersDuelManager.getInstance().targetMonster.get(target.getName());

            LivingEntity shooterMonsterLivingEntity = (LivingEntity) shooterMonster;
            LivingEntity targetMonsterLivingEntity = (LivingEntity) targetMonster;

            shooterMonsterLivingEntity.damage(0.1);

            healthShooterMonster = healthShooterMonster - damage;



            shooter.getInventory().setItemInMainHand(new ItemBuilder(Main.getInstance().getConfig().getString("item.captureballs.material"))
                    .setName(Main.getInstance().getConfig().getString("item.captureballs.name_mob_and_empty").replace("{mob}", monster))
                    .setAmount(1)
                    .setLore("§7Niveaux :", "§e"+level, "§7Expériences :", "§b"+exp[0].replace("§b", "")+"§7/§9"+exp[1].replace("§9", ""), "§7Identifiant :", "§f#"+id,
                            "§7Monstre :", "§d"+monster, "§7Vies :", "§c"+healthShooterMonster, "§7Type :", "§a"+type)
                    .build());

            inv.setItem(10, new ItemBuilder(Material.CHICKEN_SPAWN_EGG.toString()).setName("§7Monstre de : §b" + shooter.getName())
                    .setLore("§7Vies :", "§c"+healthShooterMonster,
                            "§7Niveaux :", "§e"+level,
                            "§7Créature :", "§d"+monster,
                            "§7Type :", "§a"+type).build());

            shooter.sendMessage(Main.getInstance().prefix + " " + Main.getInstance().getConfig().getString("messages.monsterInflictedDamageInPlayersDuel")
                    .replace("{attacker}", target.getName())
                    .replace("{damage}", String.valueOf(damage))
                    .replace("{victim}", shooter.getName()));
            target.sendMessage(Main.getInstance().prefix + " " + Main.getInstance().getConfig().getString("messages.monsterInflictedDamageInPlayersDuel")
                    .replace("{attacker}", target.getName())
                    .replace("{damage}", String.valueOf(damage))
                    .replace("{victim}", shooter.getName()));

            itsYourTurnAttackInPlayersDuel.remove(target.getName());
            itsYourTurnAttackInPlayersDuel.put(target.getName(), false);

            itsYourTurnAttackInPlayersDuel.remove(shooter.getName());
            itsYourTurnAttackInPlayersDuel.put(shooter.getName(), true);

            target.sendMessage(Main.getInstance().prefix + " " + Main.getInstance().getConfig().getString("messages.turnMessageInPlayersDuel").replace("{player}", shooter.getName()));
            shooter.sendMessage(Main.getInstance().prefix + " " + Main.getInstance().getConfig().getString("messages.turnMessageInPlayersDuel").replace("{player}", shooter.getName()));
            checkIfShooterMonsterIsDead(shooter, target, healthShooterMonster, shooterMonster, monster);
        }

    }

    public void checkIfTargetMonsterIsDead(Player shooter, Player target, int healthTargetMonster, Entity monster, String monsterName){

        if(healthTargetMonster <= 0){

            monster.remove();

            shooter.closeInventory();
            target.closeInventory();

            shooter.sendMessage(Main.getInstance().prefix + " " + Main.getInstance().getConfig().getString("messages.duelBeetweenPlayerIsFinish").replace("{winner}", shooter.getName()));
            target.sendMessage(Main.getInstance().prefix + " " + Main.getInstance().getConfig().getString("messages.duelBeetweenPlayerIsFinish").replace("{winner}", shooter.getName()));

            itsYourTurnAttackInPlayersDuel.remove(shooter.getName());
            itsYourTurnAttackInPlayersDuel.remove(target.getName());

            playersMonstersDuel.remove(shooter.getName());
            playersMonstersDuel.remove(target.getName());

            targetMonster.remove(shooter.getName());
            targetMonster.remove(target.getName());

            Main.getInstance().throwingMonster.remove(shooter.getName());
            Main.getInstance().throwingMonster.remove(target.getName());

            String line3 = shooter.getInventory().getItemInMainHand().getItemMeta().getLore().get(3).replace("§7", "").replace("/", "");
            String allyMonsterName = shooter.getInventory().getItemInMainHand().getItemMeta().getLore().get(7).replace("§d", "");
            String[] exp = line3.split("§9");
            int expActual = Integer.parseInt(exp[0].replace("§b", ""));

            String name = shooter.getInventory().getItemInMainHand().getItemMeta().getDisplayName();
            int level = Integer.parseInt(shooter.getInventory().getItemInMainHand().getItemMeta().getLore().get(1).replace("§e", ""));
            int id = Integer.parseInt(shooter.getInventory().getItemInMainHand().getItemMeta().getLore().get(5).replace("§f", "").replace("#", ""));
            int health = 0;

            Random r = new Random();
            int expWin = r.nextInt((10 - 1) + 1) + 1;

            if(level > 50 && level < 101){
                expWin = r.nextInt((50 - 15) + 1) + 15;
            }

            if(level > 30 && level < 51){
                expWin = r.nextInt((25 - 5) + 1) + 5;
            }

            String type = shooter.getInventory().getItemInMainHand().getItemMeta().getLore().get(11).replace("§a", "");
            int finalXp = LevelsManager.getInstance().addXp(shooter, expActual, expWin, level, Integer.parseInt(exp[1].replace("§9", "")), allyMonsterName, type);
            target.setItemInHand(new ItemBuilder(Material.SNOWBALL.toString())
                    .setName(Main.getInstance().getConfig().getString("item.captureballs.name_mob_and_full").replace("{mob}", monsterName))
                    .setLore("§7Niveaux :", "§e" + level,
                            "§7Expériences :", "§b"+finalXp+"§7/§9"+exp[1].replace("§9", ""),
                            "§7Identifiant :", "§f#" + id,
                            "§7Monstre :", "§d" + allyMonsterName,
                            "§7Vies :", "§c" + health,
                            "§7Type :", "§a"+type)
                    .build());

            shooter.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(Main.getInstance().getConfig().getString("messages.yourMonsterWonXp").replace("{ally}", allyMonsterName).replace("{xpwin}", ""+expWin)));



            target.sendTitle("§cVotre §e" + monster, "§ca perdu !", 10, 20, 10);

        }

    }

    public void checkIfShooterMonsterIsDead(Player shooter, Player target, int healthShooterMonster, Entity monster, String monsterName){

        if(healthShooterMonster <= 0){

            monster.remove();

            shooter.closeInventory();
            target.closeInventory();

            shooter.sendMessage(Main.getInstance().prefix + " " + Main.getInstance().getConfig().getString("messages.duelBeetweenPlayerIsFinish").replace("{winner}", target.getName()));
            target.sendMessage(Main.getInstance().prefix + " " + Main.getInstance().getConfig().getString("messages.duelBeetweenPlayerIsFinish").replace("{winner}", target.getName()));

            itsYourTurnAttackInPlayersDuel.remove(shooter.getName());
            itsYourTurnAttackInPlayersDuel.remove(target.getName());

            playersMonstersDuel.remove(shooter.getName());
            playersMonstersDuel.remove(target.getName());

            targetMonster.remove(shooter.getName());
            targetMonster.remove(target.getName());

            Main.getInstance().throwingMonster.remove(shooter.getName());
            Main.getInstance().throwingMonster.remove(target.getName());

            String line3 = target.getInventory().getItemInMainHand().getItemMeta().getLore().get(3).replace("§7", "").replace("/", "");
            String allyMonsterName = target.getInventory().getItemInMainHand().getItemMeta().getLore().get(7).replace("§d", "");
            String[] exp = line3.split("§9");
            int expActual = Integer.parseInt(exp[0].replace("§b", ""));

            String name = target.getInventory().getItemInMainHand().getItemMeta().getDisplayName();
            int level = Integer.parseInt(target.getInventory().getItemInMainHand().getItemMeta().getLore().get(1).replace("§e", ""));
            int id = Integer.parseInt(target.getInventory().getItemInMainHand().getItemMeta().getLore().get(5).replace("§f", "").replace("#", ""));
            int health = 0;

            Random r = new Random();
            int expWin = r.nextInt((10 - 1) + 1) + 1;

            if(level > 50 && level < 101){
                expWin = r.nextInt((50 - 15) + 1) + 15;
            }

            if(level > 30 && level < 51){
                expWin = r.nextInt((25 - 5) + 1) + 5;
            }

            String type = target.getInventory().getItemInMainHand().getItemMeta().getLore().get(11).replace("§a", "");
            int finalXp = LevelsManager.getInstance().addXp(target, expActual, expWin, level, Integer.parseInt(exp[1].replace("§9", "")), allyMonsterName, type);
            shooter.setItemInHand(new ItemBuilder(Material.SNOWBALL.toString())
                    .setName(Main.getInstance().getConfig().getString("item.captureballs.name_mob_and_full").replace("{mob}", monsterName))
                    .setLore("§7Niveaux :", "§e" + level,
                            "§7Expériences :", "§b"+finalXp+"§7/§9"+exp[1].replace("§9", ""),
                            "§7Identifiant :", "§f#" + id,
                            "§7Monstre :", "§d" + allyMonsterName,
                            "§7Vies :", "§c" + health,
                            "§7Type :", "§a"+type)
                    .build());

            target.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(Main.getInstance().getConfig().getString("messages.yourMonsterWonXp").replace("{ally}", allyMonsterName).replace("{expwin}", ""+expWin)));

            shooter.sendTitle("§cVotre §e" + monsterName, "§ca perdu !", 10, 20, 10);

        }

    }
}

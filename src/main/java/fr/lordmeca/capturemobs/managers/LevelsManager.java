package fr.lordmeca.capturemobs.managers;

import fr.lordmeca.capturemobs.Main;
import fr.lordmeca.capturemobs.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class LevelsManager {

    private static LevelsManager instance = new LevelsManager();

    public static LevelsManager getInstance() {
        return instance;
    }

    public Integer addXp(Player player, int actualXp, int xpWin, int level, int xpRequires, String monsterAllyName, String type){

        int finalXp = actualXp + xpWin;
        int finalLevel = level + 1;

        checkIfYouCanRankUpLevel(player, finalLevel, finalXp, xpRequires, monsterAllyName, type);

        return finalXp;

    }

    public void checkIfYouCanRankUpLevel(Player player, int level, int actualXp, int xpRequires, String monsterAllyName, String type){




        if(level < 30 && level > 0){



            if(xpRequires < 100){
                xpRequires = 100;
            }



            if(actualXp >= 100){

                player.sendTitle("§7Votre §a" + monsterAllyName, "§7est maintenant niveau §9" + level, 10, 20, 10);
                upgradeLevel(player, 0, level, xpRequires, type);

            }

        }

        if(level < 50 && level >= 30){


            if(xpRequires < 200){
                xpRequires = 200;
            }



            if(level == 30){
                player.sendTitle("§7Votre §e" + monsterAllyName, "§7est maintenant niveau §9" + level, 10, 20, 10);
                upgradeLevel(player, 0, level, xpRequires, type);
                return;
            }

            if(actualXp >= 200){

                player.sendTitle("§7Votre §e" + monsterAllyName, "§7est maintenant niveau §9" + level, 10, 20, 10);
                upgradeLevel(player, 0, level, xpRequires, type);

            }

        }

        if(level < 101 && level >= 50){

            if(xpRequires < 300){
                xpRequires = 300;
            }

            if(level == 50){
                player.sendTitle("§7Votre §c" + monsterAllyName, "§7est maintenant niveau §9" + level, 10, 20, 10);
                upgradeLevel(player, 0, level, xpRequires, type);
                return;
            }

            if(actualXp >= 300){

                player.sendTitle("§7Votre §c" + monsterAllyName, "§7est maintenant niveau §9" + level, 10, 20, 10);
                upgradeLevel(player, 0, level, xpRequires, type);

            }

        }






    }

    public void upgradeLevel(Player player, Integer newXp, Integer newLevel, Integer xpRequires, String type){



        String name = player.getInventory().getItemInMainHand().getItemMeta().getDisplayName();

        int id = Integer.parseInt(player.getInventory().getItemInMainHand().getItemMeta().getLore().get(5).replace("§f", "").replace("#", ""));

        int health = Integer.parseInt(player.getInventory().getItemInMainHand().getItemMeta().getLore().get(9).replace("§c", ""));

        String allyName = player.getInventory().getItemInMainHand().getItemMeta().getLore().get(7).replace("§d", "");
        String line3 = player.getInventory().getItemInMainHand().getItemMeta().getLore().get(3).replace("§7", "").replace("/", "");
        String[] exp = line3.split("§9");

    ;
        Bukkit.getScheduler().runTaskLater(Main.getInstance(), () ->{
            player.getInventory().setItemInMainHand(new ItemBuilder(Material.SNOWBALL.toString())
                    .setName(name)
                    .setLore("§7Niveaux :", "§e" + newLevel,
                            "§7Expériences :", "§b"+newXp+"§7/§9"+xpRequires,
                            "§7Identifiant :", "§f#" + id,
                            "§7Monstre :", "§d" + allyName,
                            "§7Vies :", "§c" + health,
                            "§7Type :", "§a"+type)
                    .build());
        }, 3);






    }

}

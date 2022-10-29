package fr.lordmeca.capturemobs.cmds;

import fr.lordmeca.capturemobs.Main;
import fr.lordmeca.capturemobs.listeners.PlayerWalkEvent;
import fr.lordmeca.capturemobs.utils.ItemBuilder;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;

public class CaptureBallCmd implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player)){
            return true;
        }

        Player player = (Player) sender;

        YamlConfiguration playersConfig = YamlConfiguration.loadConfiguration(new File("plugins/CaptureMobs/players", player.getName() + ".yml"));

        if(!player.hasPermission(Main.getInstance().getConfig().getString("command.permission"))){
            player.sendMessage(Main.getInstance().prefix + " " + Main.getInstance().getConfig().getString("command.permission_message"));
            return true;
        }


        if(args.length == 0){

            player.sendMessage(Main.getInstance().prefix + " " + Main.getInstance().getConfig().getString("command.correct_usage"));


            return true;

        }

        if(args.length == 1){

            if(args[0].equalsIgnoreCase("resurrect")){

                String name = player.getInventory().getItemInMainHand().getItemMeta().getDisplayName();

                int level = Integer.parseInt(player.getInventory().getItemInMainHand().getItemMeta().getLore().get(1).replace("§e", ""));
                int id = Integer.parseInt(player.getInventory().getItemInMainHand().getItemMeta().getLore().get(5).replace("#", "").replace("§f", ""));
                int health = Integer.parseInt(player.getInventory().getItemInMainHand().getItemMeta().getLore().get(9).replace("§c", ""));
                String monster = player.getInventory().getItemInMainHand().getItemMeta().getLore().get(7).replace("§d", "");

                String line3 = player.getInventory().getItemInMainHand().getItemMeta().getLore().get(3).replace("§7", "").replace("/", "");
                String type = player.getInventory().getItemInMainHand().getItemMeta().getLore().get(11).replace("§a", "");

                String[] exp = line3.split("§9");

                player.setItemInHand(
                        new ItemBuilder(Main.getInstance().getConfig().getString("item.captureballs.material"))
                                .setName(Main.getInstance().getConfig().getString("item.captureballs.name_mob_and_full").replace("{mob}", monster))
                                .setAmount(1)
                                .setLore("§7Niveaux :", "§e" + level, "§7Expériences :", "§b"+exp[0].replace("§b", "") + "§7/§9" + exp[1].replace("§9", ""), "§7Identifiant :", "§f#" + id, "§7Monstre :", "§d"+monster, "§7Vies :", "§c"+health,
                                        "§7Type :", "§a"+type)
                                .build());




            }

            if(args[0].equalsIgnoreCase("healblock")){

                Location playerLoc = player.getLocation();

                playerLoc.getBlock().setType(Material.END_PORTAL_FRAME);

                playerLoc.setY(playerLoc.getY() - 1);

                ArmorStand stand = (ArmorStand) player.getWorld().spawnEntity(playerLoc, EntityType.ARMOR_STAND);

                stand.setHelmet(new ItemStack(Material.BEACON));
                stand.setBasePlate(false);
                stand.setArms(false);
                stand.setGravity(false);
                stand.setVisible(false);
                stand.setSmall(false);
                stand.setCustomName(Main.getInstance().getConfig().getString("messages.titleHealBlock"));
                stand.setCustomNameVisible(true);

            }

            if(args[0].equalsIgnoreCase("rdm")){

                Location playerLoc = player.getLocation();

                playerLoc.getBlock().setType(Material.FLOWER_POT);

                playerLoc.setY(playerLoc.getY() - 1.2);

                ArmorStand stand = (ArmorStand) player.getWorld().spawnEntity(playerLoc, EntityType.ARMOR_STAND);


                stand.setBasePlate(false);
                stand.setArms(false);
                stand.setGravity(false);
                stand.setVisible(false);
                stand.setSmall(false);
                stand.setCustomName("§cObjet aléatoire");
                stand.setCustomNameVisible(false);

            }
        }

        if(args.length == 3){



            if(args[0].equalsIgnoreCase("give")){
                Player target = Bukkit.getPlayer(args[1]);
                int amount = Integer.parseInt(args[2]);
                if(target == null){
                    player.sendMessage(Main.getInstance().prefix + " " + Main.getInstance().getConfig().getString("command.player_not_found"));
                    return true;
                }

                target.getInventory().addItem(
                        new ItemBuilder(Main.getInstance().getConfig().getString("item.captureballs.material"))
                                .setName(Main.getInstance().getConfig().getString("item.captureballs.name_no_mob_and_empty"))
                                .setAmount(amount).build());

                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§aOpération réussite avec succès !"));

            }




            if(args[0].equalsIgnoreCase("set")){

                if(args[1].equalsIgnoreCase("health")){

                    int health = Integer.parseInt(args[2]);

                    String monster = player.getInventory().getItemInMainHand().getItemMeta().getLore().get(7).replace("§d", "");
                    String name = player.getInventory().getItemInMainHand().getItemMeta().getDisplayName();
                    int level = Integer.parseInt(player.getInventory().getItemInMainHand().getItemMeta().getLore().get(1).replace("§e", ""));
                    int id = Integer.parseInt(player.getInventory().getItemInMainHand().getItemMeta().getLore().get(5).replace("§f", "").replace("#", ""));

                    String line3 = player.getInventory().getItemInMainHand().getItemMeta().getLore().get(3).replace("§7", "").replace("/", "");
                    String type = player.getInventory().getItemInMainHand().getItemMeta().getLore().get(11).replace("§a", "");

                    String[] exp = line3.split("§9");

                    player.setItemInHand(new ItemBuilder(Material.SNOWBALL.toString())
                            .setName(name)
                            .setLore("§7Niveaux :", "§e" + level, "§7Expériences :", "§b"+exp[0].replace("§b", "")+"§7/§9"+exp[1].replace("§9", ""), "§7Identifiant :", "§f#" + id, "§7Monstre :", "§d" + monster, "§7Vies :", "§c" + health,
                                    "§7Type :", "§a"+type)
                            .build());

                }

                if(args[1].equalsIgnoreCase("xp")){

                    int xp = Integer.parseInt(args[2]);

                    String monster = player.getInventory().getItemInMainHand().getItemMeta().getLore().get(7).replace("§d", "");
                    String name = player.getInventory().getItemInMainHand().getItemMeta().getDisplayName();
                    int level = Integer.parseInt(player.getInventory().getItemInMainHand().getItemMeta().getLore().get(1).replace("§e", ""));
                    int id = Integer.parseInt(player.getInventory().getItemInMainHand().getItemMeta().getLore().get(5).replace("§f", "").replace("#", ""));
                    int health = Integer.parseInt(player.getInventory().getItemInMainHand().getItemMeta().getLore().get(9).replace("§c", ""));

                    String line3 = player.getInventory().getItemInMainHand().getItemMeta().getLore().get(3).replace("§7", "").replace("/", "");
                    String type = player.getInventory().getItemInMainHand().getItemMeta().getLore().get(11).replace("§a", "");
                    String[] exp = line3.split("§9");

                    player.setItemInHand(new ItemBuilder(Material.SNOWBALL.toString())
                            .setName(name)
                            .setLore("§7Niveaux :", "§e" + level, "§7Expériences :", "§b"+xp+"§7/§9"+exp[1].replace("§9", ""), "§7Identifiant :", "§f#" + id, "§7Monstre :", "§d" + monster, "§7Vies :", "§c" + health,
                                    "§7Type :", "§a"+type)
                            .build());

                }

                if(args[1].equalsIgnoreCase("lvl")){

                    int lvl = Integer.parseInt(args[2]);

                    String monster = player.getInventory().getItemInMainHand().getItemMeta().getLore().get(7).replace("§d", "");
                    String name = player.getInventory().getItemInMainHand().getItemMeta().getDisplayName();
                    //int xp = Integer.parseInt(player.getInventory().getItemInMainHand().getItemMeta().getLore().get(1).replace("§e", ""));
                    int id = Integer.parseInt(player.getInventory().getItemInMainHand().getItemMeta().getLore().get(5).replace("§f", "").replace("#", ""));
                    int health = Integer.parseInt(player.getInventory().getItemInMainHand().getItemMeta().getLore().get(9).replace("§c", ""));

                    String line3 = player.getInventory().getItemInMainHand().getItemMeta().getLore().get(3).replace("§7", "").replace("/", "");
                    String type = player.getInventory().getItemInMainHand().getItemMeta().getLore().get(11).replace("§a", "");

                    String[] exp = line3.split("§9");

                    player.setItemInHand(new ItemBuilder(Material.SNOWBALL.toString())
                            .setName(name)
                            .setLore("§7Niveaux :", "§e" + lvl, "§7Expériences :", "§b"+exp[0].replace("§b", "")+"§7/§9"+exp[1].replace("§9", ""), "§7Identifiant :", "§f#" + id, "§7Monstre :", "§d" + monster, "§7Vies :", "§c" + health, "§7Type :", "§a"+type)
                            .build());

                }

            }



        }

        if(args.length == 4){

            if(args[0].equalsIgnoreCase("give")){

                Player target = Bukkit.getPlayer(args[1]);

                if(args[2].equalsIgnoreCase("potion")){

                    if(args[3].equalsIgnoreCase("1")){

                        player.getInventory().addItem(new ItemBuilder(Material.DRAGON_BREATH.toString())
                                .setName("§8● §6Potion [1]")
                                .setAmount(1)
                                .build());

                    }

                    if(args[3].equalsIgnoreCase("2")){

                        player.getInventory().addItem(new ItemBuilder(Material.DRAGON_BREATH.toString())
                                .setName("§8● §aPotion [2]")
                                .setAmount(1)
                                .build());

                    }

                }

                if(target == null){
                    player.sendMessage(Main.getInstance().prefix + " " + Main.getInstance().getConfig().getString("command.player_not_found"));
                    return true;
                }

            }

        }

        if(args.length == 2){

            if(args[0].equalsIgnoreCase("set")){

                if(args[1].equalsIgnoreCase("shiny")){

                    int level = Integer.parseInt(player.getInventory().getItemInMainHand().getItemMeta().getLore().get(1).replace("§e", ""));
                    int id = Integer.parseInt(player.getInventory().getItemInMainHand().getItemMeta().getLore().get(5).replace("#", "").replace("§f", ""));
                    int health = Integer.parseInt(player.getInventory().getItemInMainHand().getItemMeta().getLore().get(9).replace("§c", ""));
                    String monster = player.getInventory().getItemInMainHand().getItemMeta().getLore().get(7).replace("§d", "");

                    String line3 = player.getInventory().getItemInMainHand().getItemMeta().getLore().get(3).replace("§7", "").replace("/", "");
                    String type = "SHINY";

                    String[] exp = line3.split("§9");

                    player.getInventory().setItemInMainHand(
                            new ItemBuilder(Main.getInstance().getConfig().getString("item.captureballs.material"))
                                    .setName(Main.getInstance().getConfig().getString("item.captureballs.name_mob_and_full").replace("{mob}", monster))
                                    .setAmount(1)
                                    .setLore("§7Niveaux :", "§e" + level, "§7Expériences :", "§b"+exp[0].replace("§b", "") + "§7/§9" + exp[1].replace("§9", ""), "§7Identifiant :", "§f#" + id, "§7Monstre :", "§d"+monster, "§7Vies :", "§c"+health,
                                            "§7Type :", "§a"+type)
                                    .build());

                }

                if(args[1].equalsIgnoreCase("normal")){

                    int level = Integer.parseInt(player.getInventory().getItemInMainHand().getItemMeta().getLore().get(1).replace("§e", ""));
                    int id = Integer.parseInt(player.getInventory().getItemInMainHand().getItemMeta().getLore().get(5).replace("#", "").replace("§f", ""));
                    int health = Integer.parseInt(player.getInventory().getItemInMainHand().getItemMeta().getLore().get(9).replace("§c", ""));
                    String monster = player.getInventory().getItemInMainHand().getItemMeta().getLore().get(7).replace("§d", "");

                    String line3 = player.getInventory().getItemInMainHand().getItemMeta().getLore().get(3).replace("§7", "").replace("/", "");
                    String type = "Normal";

                    String[] exp = line3.split("§9");

                    player.getInventory().setItemInMainHand(
                            new ItemBuilder(Main.getInstance().getConfig().getString("item.captureballs.material"))
                                    .setName(Main.getInstance().getConfig().getString("item.captureballs.name_mob_and_full").replace("{mob}", monster))
                                    .setAmount(1)
                                    .setLore("§7Niveaux :", "§e" + level, "§7Expériences :", "§b"+exp[0].replace("§b", "") + "§7/§9" + exp[1].replace("§9", ""), "§7Identifiant :", "§f#" + id, "§7Monstre :", "§d"+monster, "§7Vies :", "§c"+health,
                                            "§7Type :", "§a"+type)
                                    .build());

                }

            }

        }

        return false;
    }
}

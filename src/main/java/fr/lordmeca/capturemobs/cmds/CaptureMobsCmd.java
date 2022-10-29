package fr.lordmeca.capturemobs.cmds;

import fr.lordmeca.capturemobs.Main;
import fr.lordmeca.capturemobs.managers.PlayerMonstersDuelManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CaptureMobsCmd implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player)){

            return true;

        }

        Player player = (Player) sender;

        if(args.length == 3){

            if(args[0].equalsIgnoreCase("duels")){

                if(args[1].equalsIgnoreCase("accept")){

                    Player target = Bukkit.getPlayer(args[2]);

                    if(target == null){

                        player.sendMessage(Main.getInstance().prefix + " " + Main.getInstance().getConfig().getString("messages.playerNotFound"));
                        return true;

                    }

                    if(PlayerMonstersDuelManager.getInstance().playersMonstersDuel.containsKey(player.getName())){

                        player.sendMessage(Main.getInstance().prefix + " " + Main.getInstance().getConfig().getString("messages.youAreAlreadyInFight"));
                        return true;

                    }

                    if(PlayerMonstersDuelManager.getInstance().playersMonstersDuel.containsKey(target.getName())){

                        player.sendMessage(Main.getInstance().prefix + " " + Main.getInstance().getConfig().getString("messages.targetPlayerAlreadyInFight"));
                        return true;

                    }

                    int levelP = Integer.parseInt(player.getInventory().getItemInMainHand().getItemMeta().getLore().get(1).replace("§e", ""));
                    int healthP = Integer.parseInt(player.getInventory().getItemInMainHand().getItemMeta().getLore().get(9).replace("§c", ""));
                    String typeP = player.getInventory().getItemInMainHand().getItemMeta().getLore().get(11).replace("§a", "");
                    String monsterP = player.getInventory().getItemInMainHand().getItemMeta().getLore().get(7).replace("§d", "");

                    int levelT = Integer.parseInt(target.getInventory().getItemInMainHand().getItemMeta().getLore().get(1).replace("§e", ""));
                    int healthT = Integer.parseInt(target.getInventory().getItemInMainHand().getItemMeta().getLore().get(9).replace("§c", ""));
                    String typeT = target.getInventory().getItemInMainHand().getItemMeta().getLore().get(11).replace("§a", "");
                    String monsterT = target.getInventory().getItemInMainHand().getItemMeta().getLore().get(7).replace("§d", "");
                    PlayerMonstersDuelManager.getInstance().playersMonstersDuel.put(player.getName(), target.getName());
                    PlayerMonstersDuelManager.getInstance().startDuel(target, player, monsterT, monsterP, healthT, healthP, levelT, levelP, typeT, typeP);

                }

            }

        }

        return false;
    }
}

package fr.lordmeca.capturemobs.managers;

import fr.lordmeca.capturemobs.Main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;

public class FileManager {

    private static FileManager instance = new FileManager();

    private final Main main = Main.getInstance();

    public static FileManager getInstance() {
        return instance;
    }

    /**
     * Init.
     */
    public void init() {
        createFile("locations");
    }

    private void createFile(String fileName) {

        if (!main.getDataFolder().exists()) {
            main.getDataFolder().mkdir();
        }

        File file = new File(main.getDataFolder(), fileName + ".yml");

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }

    }

    /**
     * Gets file.
     *
     * @param fileName the file name
     * @return the file
     */
    public File getFile(String fileName) {

        return new File(main.getDataFolder(), fileName + ".yml");

    }

    /**
     * Save file.
     *
     * @param fileName the file name
     * @param file     the file
     */
    public void saveFile(String fileName, FileConfiguration file) {

        try {
            file.save(getFile(fileName));
        } catch (IOException exception) {
            exception.printStackTrace();
        }

    }

    public void registerNewPlayerMobInFile(Player player, String mob) throws IOException {
        YamlConfiguration c = YamlConfiguration.loadConfiguration(new File("plugins/CaptureMobs/players", player.getName() + ".yml"));

        if(c.getInt("mobs.amount") == 0){

            YamlConfiguration c2 = new YamlConfiguration();
            c2.set("mobs.amount", 1);
            c2.set("mobs.alls."+mob, 1);
            c2.save(new File("plugins/CaptureMobs/players", player.getName() + ".yml"));

        } else {

            int mobs_amount = c.getInt("mobs.amount");
            int mob_selected = c.getInt("mobs.alls."+mob);

            int finalMobsAmount = mobs_amount + 1;
            int finalMobsSelected = mob_selected + 1;

            c.set("mobs.amount", finalMobsAmount);
            c.set("mobs.alls."+mob, finalMobsSelected);
            c.save(new File("plugins/CaptureMobs/players", player.getName() + ".yml"));
        }


    }

    /*public void giveKit(Player player, String kit) throws IOException {
        YamlConfiguration c = YamlConfiguration.loadConfiguration(new File("plugins/PvpBoxPlus/kits", kit + ".yml"));
        ItemStack[] content = ((List<ItemStack>) c.get("inventory.armor")).toArray(new ItemStack[0]);
        player.getInventory().setArmorContents(content);
        content = ((List<ItemStack>) c.get("inventory.content")).toArray(new ItemStack[0]);
        player.getInventory().setContents(content);
    }*/
}

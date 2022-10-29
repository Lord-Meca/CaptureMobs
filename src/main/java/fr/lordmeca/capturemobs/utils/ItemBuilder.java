package fr.lordmeca.capturemobs.utils;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Arrays;


/**
 * The type Item builder.
 */
public class ItemBuilder {
    private ItemStack item;
    private ItemMeta itemMeta;
    private SkullMeta skullMeta;

    /**
     * Instantiates a new Item builder.
     */
    public ItemBuilder() {

    }

    /**
     * Instantiates a new Item builder.
     *
     * @param material the material
     * @param subId    the sub id
     */
    public ItemBuilder(String material, short subId) {
        item = new ItemStack(Material.valueOf(material), 1, subId);
        itemMeta = item.getItemMeta();
    }

    /**
     * Instantiates a new Item builder.
     *
     * @param material the material
     */
    public ItemBuilder(String material) {
        this(String.valueOf(Material.valueOf(material)), (short) 0);
    }



    /**
     * Set name item builder.
     *
     * @param name the name
     * @return the item builder
     */
    public ItemBuilder setName(String name) {
        itemMeta.setDisplayName(name);
        return this;
    }

    /**
     * Set lore item builder.
     *
     * @param lore the lore
     * @return the item builder
     */
    public ItemBuilder setLore(String... lore) {
        itemMeta.setLore(Arrays.asList(lore));
        return this;
    }

    /**
     * Set durability item builder.
     *
     * @param durability the durability
     * @return the item builder
     */
    public ItemBuilder setDurability(short durability) {
        item.setDurability(durability);
        return this;
    }



    /**
     * Add item flags item builder.
     *
     * @param itemFlag the item flag
     * @return the item builder
     */
    public ItemBuilder addItemFlags(ItemFlag itemFlag) {
        itemMeta.addItemFlags(itemFlag);
        return this;
    }

    /**
     * Add enchants item builder.
     *
     * @param enchantment the enchantment
     * @param level       the level
     * @param bool        the bool
     * @return the item builder
     */
    public ItemBuilder addEnchants(Enchantment enchantment, Integer level, Boolean bool) {
        itemMeta.addEnchant(enchantment, level, bool);
        return this;
    }

    /**
     * Set amount item builder.
     *
     * @param amount the amount
     * @return the item builder
     */
    public ItemBuilder setAmount(int amount) {
        item.setAmount(amount);
        return this;
    }



    /**
     * Set unbreakable item builder.
     *
     * @param unbreakable the unbreakable
     * @return the item builder
     */
    public ItemBuilder setUnbreakable(boolean unbreakable) {
        itemMeta.setUnbreakable(unbreakable);
        return this;
    }

    /**
     * Build item stack.
     *
     * @return the item stack
     */
    public ItemStack build() {
        item.setItemMeta(itemMeta);
        return item;
    }

}

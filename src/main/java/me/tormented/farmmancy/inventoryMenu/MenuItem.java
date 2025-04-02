package me.tormented.farmmancy.inventoryMenu;

import com.google.common.collect.MultimapBuilder;
import net.kyori.adventure.text.Component;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MenuItem {

    private final ItemStack item;
    private ClickHandler clickHandler;


    public MenuItem(@NotNull ItemStack item) {
        this.item = item;
    }

    public MenuItem(@NotNull ItemStack item, Component displayName) {
        this.item = item;
        setDisplayName(displayName);
    }

    public MenuItem(@NotNull ItemStack item, boolean showTooltip) {
        this.item = item;
        setShowTooltip(showTooltip);
    }

    public MenuItem(@NotNull ItemStack item, List<? extends Component> lore) {
        this.item = item;
        setLore(lore);
    }

    public MenuItem(@NotNull ItemStack item, Component displayName, List<? extends Component> lore) {
        this.item = item;
        setDisplayName(displayName);
        setLore(lore);
    }

    /**
     * @param displayName The new display name to assign
     * @return This same <code>MenuItem</code> instance
     */
    public MenuItem setDisplayName(Component displayName) {

        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta != null) {
            itemMeta.customName(displayName);
            item.setItemMeta(itemMeta);
        }

        return this;
    }

    /**
     * @param lore The new lore to assign
     * @return This same <code>MenuItem</code> instance
     */
    public MenuItem setLore(List<? extends Component> lore) {

        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta != null) {
            itemMeta.lore(lore);
            item.setItemMeta(itemMeta);
        }

        return this;
    }

    /**
     * @param showTooltip Whether to show the tooltip
     * @return This same <code>MenuItem</code> instance
     */
    public MenuItem setShowTooltip(boolean showTooltip) {
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setHideTooltip(!showTooltip);
        item.setItemMeta(itemMeta);

        return this;
    }

    /**
     * @param itemFlags The item flag to add
     * @return This same <code>MenuItem</code> instance
     */
    public MenuItem addItemFlags(ItemFlag... itemFlags) {
        item.addItemFlags(itemFlags);
        return this;
    }

    /**
     * Because <code>ItemMeta#addItemFlags(ItemFlag.HIDE_ATTRIBUTES)</code> doesn't do it's ONE job of hiding attributes.
     *
     * @return This same <code>MenuItem</code> instance
     */
    public MenuItem deleteAttributes() {
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setAttributeModifiers(MultimapBuilder.hashKeys().hashSetValues().build());  // *It's like magic!*
        item.setItemMeta(itemMeta);

        return this;
    }

    /**
     * Returns the mutable item that is being encapsulated.
     *
     * @return The item
     */
    public ItemStack getItem() {
        return item;
    }

    /**
     * @param clickHandler The new click handler to assign
     * @return This same <code>MenuItem</code> instance
     */
    public MenuItem setClickHandler(ClickHandler clickHandler) {
        this.clickHandler = clickHandler;
        return this;
    }

    public MenuItem setEnchantmentGlintOverride(@Nullable Boolean glint) {
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setEnchantmentGlintOverride(glint);
        item.setItemMeta(itemMeta);

        return this;
    }

    public @NotNull ClickResponse clicked(@NotNull InventoryClickEvent event, Menu menu) {
        if (clickHandler != null && clickHandler.onClicked(menu, this, event) instanceof ClickResponse clickResponse)
            return clickResponse;
        return new ClickResponse.Nothing();
    }

    @Override
    public MenuItem clone() {
        MenuItem clonedMenuItem = new MenuItem(item.clone());
        clonedMenuItem.clickHandler = clickHandler;
        return clonedMenuItem;
    }

}

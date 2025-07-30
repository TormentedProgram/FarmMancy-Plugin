package me.tormented.farmmancy.inventoryMenu;

import me.tormented.farmmancy.FarmMancy;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class Menu {

    private @NotNull Map<String, MenuFactory> menuNavigation = new HashMap<>();

    private final Inventory inventory;
    private final @NotNull Player player;
    private final MenuItem[] menuItems;
    private final int rows;
    private @Nullable MenuTicker menuTicker;

    public Menu(int rows, @NotNull Player player, Component title) {

        this.rows = rows;
        this.player = player;
        inventory = Bukkit.createInventory(player, rows * 9, title);
        menuItems = new MenuItem[rows * 9];
    }

    public void send() {
        player.openInventory(inventory);
        player.setMetadata("InventoryMenu", new FixedMetadataValue(FarmMancy.getInstance(), this));
    }

    public @Nullable ItemStack setItem(int index, MenuItem menuItem) {
        if (index > inventory.getSize() - 1)
            throw new IndexOutOfBoundsException("Provided index is greater than the inventory size");

        ItemStack oldItem = inventory.getItem(index);
        inventory.setItem(index, menuItem == null ? null : menuItem.getItem());
        menuItems[index] = menuItem;
        return oldItem;
    }

    public @Nullable ItemStack setItem(int x, int y, MenuItem menuItem) {
        int index = convertSlotIndex(x, y);

        return setItem(index, menuItem);
    }

    public @Nullable MenuItem getItem(int index) {
        if (index > inventory.getSize() - 1)
            throw new IndexOutOfBoundsException("Provided index is greater than the inventory size");

        return menuItems[index];
    }

    public @Nullable MenuItem getItem(int x, int y) {
        int index = convertSlotIndex(x, y);

        return getItem(index);
    }

    public Inventory getInventory() {
        return inventory;
    }

    public static int convertSlotIndex(int x, int y) {

        if (x < 1 || x > 9 || y < 1)
            throw new IllegalArgumentException("Position (" + x + ", " + y + ") is out of bounds for any inventory");

        return (y - 1) * 9 + (x - 1);
    }

    public void setRelativeMenuNavigation(Map<String, MenuFactory> navigation) {
        this.menuNavigation = navigation;
    }


    public void processClickEvent(@NotNull InventoryClickEvent event) {
        event.setCancelled(true);

        if (player != event.getWhoClicked()) return;

        if (event.getInventory() == getInventory()) {
            if (event.getInventory() == event.getClickedInventory()) {
                // Clicked inside menu
                MenuItem clickedMenuItem = menuItems[event.getSlot()];

                if (clickedMenuItem != null) {
                    try {
                        ClickResponse clickResponse = clickedMenuItem.clicked(event, this);

                        switch (clickResponse) {
                            case ClickResponse.NavigateMenu navigateMenu -> {
                                MenuFactory targetMenuFactory = menuNavigation.get(navigateMenu.getKey());
                                if (targetMenuFactory != null) {
                                    targetMenuFactory.sendToPlayer(player);
                                }
                            }
                            case ClickResponse.CloseMenu closeMenu -> {
                                player.closeInventory();
                            }
                            default -> {
                            }
                        }

                    } catch (Exception e) {
                        FarmMancy.getInstance().getLogger().warning("Failed to process menu click:");
                        e.printStackTrace();
                    }

                }
            } else {
                // Clicked in lower inventory
            }
        }
    }

    public void processCloseEvent(@NotNull InventoryCloseEvent event) {

        if (player != event.getPlayer()) return;

        player.removeMetadata("InventoryMenu", FarmMancy.getInstance());
    }

    public void setMenuTicker(@Nullable MenuTicker menuTicker) {
        this.menuTicker = menuTicker;
        processTick();
    }

    public void processTick() {
        if (menuTicker != null)
            menuTicker.tick(this);
    }

    public @NotNull Player getPlayer() {
        return player;
    }

    public int getColumns() {
        return 9;
    }

    public int getRows() {
        return rows;
    }

}

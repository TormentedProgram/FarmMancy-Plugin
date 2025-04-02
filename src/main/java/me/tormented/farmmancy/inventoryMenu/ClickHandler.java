package me.tormented.farmmancy.inventoryMenu;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;

/**
 * An interface for code to run when the menu item this is assigned to is clicked.
 */
public interface ClickHandler {

    @NotNull ClickResponse onClicked(@NotNull Menu menuInstance, @NotNull MenuItem menuItem, @NotNull InventoryClickEvent event);

}

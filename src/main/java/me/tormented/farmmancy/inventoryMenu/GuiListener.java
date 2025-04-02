package me.tormented.farmmancy.inventoryMenu;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.metadata.MetadataValue;

import java.util.List;

public class GuiListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent event) {

        if (event.isCancelled()) return;

        Player player = (Player) event.getWhoClicked();

        List<MetadataValue> metadata = player.getMetadata("InventoryMenu");
        if (!metadata.isEmpty() && metadata.getFirst().value() instanceof Menu menu) {
            menu.processClickEvent(event);
        }
    }

    @EventHandler
    public void onInventoryClosed(InventoryCloseEvent event) {

        Player player = (Player) event.getPlayer();

        List<MetadataValue> metadata = player.getMetadata("InventoryMenu");
        if (!metadata.isEmpty() && metadata.getFirst().value() instanceof Menu menu) {
            menu.processCloseEvent(event);
        }
    }

}

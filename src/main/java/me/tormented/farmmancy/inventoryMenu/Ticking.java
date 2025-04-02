package me.tormented.farmmancy.inventoryMenu;

import me.tormented.farmmancy.FarmMancy;
import org.bukkit.entity.Player;
import org.bukkit.metadata.MetadataValue;

import java.util.List;

public class Ticking implements Runnable {

    private static Ticking instance = new Ticking();

    public static Ticking getInstance() {
        return instance;
    }

    private Ticking() {
    }

    @Override
    public void run() {

        for (Player player : FarmMancy.getInstance().getServer().getOnlinePlayers()) {
            List<MetadataValue> metadata = player.getMetadata("InventoryMenu");
            if (!metadata.isEmpty() && metadata.getFirst().value() instanceof Menu menu) {
                menu.processTick();
            }
        }

    }
}

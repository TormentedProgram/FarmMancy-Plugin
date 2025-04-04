package me.tormented.farmmancy.abilities.utils;

import me.tormented.farmmancy.utils.HeadProvider;
import org.bukkit.entity.ItemDisplay;

public final class AbilityHeadDisplay {

    private AbilityHeadDisplay() {}

    public static ItemDisplay finalizeSpawn(ItemDisplay itemDisplay, HeadProvider headProvider) {
        itemDisplay.setTeleportDuration(1);
        itemDisplay.setItemStack(headProvider.getHeadItem());
        return itemDisplay;
    }

}

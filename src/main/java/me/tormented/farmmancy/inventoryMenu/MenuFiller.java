package me.tormented.farmmancy.inventoryMenu;

import org.jetbrains.annotations.NotNull;

/**
 * An interface for assigned the contents of a <code>Menu</code> instance when being built in a <code>MenuFactory</code>
 */
public interface MenuFiller {
    void fillMenu(@NotNull Menu menu);
}

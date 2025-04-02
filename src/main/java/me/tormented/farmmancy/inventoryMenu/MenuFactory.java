package me.tormented.farmmancy.inventoryMenu;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuFactory {

    private final int rows;
    private final Component title;
    private final MenuItem[] menuItems;
    private final List<MenuFiller> menuFillers = new ArrayList<>();
    private @Nullable MenuTicker menuTicker = null;

    private final Map<String, MenuFactory> relativeMenuNavigation = new HashMap<>();


    public MenuFactory(int rows, Component title) {
        this.rows = rows;
        this.title = title;

        if (rows < 1 || rows > 6) throw new IllegalArgumentException("'rows' must be between 1 and 6");

        menuItems = new MenuItem[rows * 9];
    }

    public Menu sendToPlayer(@NotNull Player player) {
        Menu menu = new Menu(rows, player, title.asComponent());

        menu.setRelativeMenuNavigation(relativeMenuNavigation);

        if (menuTicker != null) {
            menu.setMenuTicker(menuTicker);
        }

        for (MenuFiller menuFiller : menuFillers) {
            menuFiller.fillMenu(menu);
        }

        for (int i = 0; i < menuItems.length; i++) {
            if (menuItems[i] == null) continue;
            menu.setItem(i, menuItems[i]);
        }

        menu.send();

        return menu;
    }

    public MenuFactory putNavigation(@NotNull String key, @NotNull MenuFactory menuFactory) {
        return relativeMenuNavigation.put(key, menuFactory);
    }

    public @Nullable MenuItem setItem(int index, MenuItem menuItem) {
        if (index >= menuItems.length)
            throw new IndexOutOfBoundsException("Provided index is greater than the inventory size");

        MenuItem oldItem = menuItems[index];
        menuItems[index] = menuItem;
        return oldItem;
    }

    public @Nullable MenuItem setItem(int x, int y, MenuItem menuItem) {
        int index = Menu.convertSlotIndex(x, y);

        return setItem(index, menuItem);
    }

    public void addMenuFiller(MenuFiller menuFiller) {
        this.menuFillers.add(menuFiller);
    }

    public void setMenuTicker(@Nullable MenuTicker menuTicker) {
        this.menuTicker = menuTicker;
    }
}

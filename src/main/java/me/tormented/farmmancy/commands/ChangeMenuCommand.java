package me.tormented.farmmancy.commands;

import me.tormented.farmmancy.inventoryMenu.*;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ChangeMenuCommand {
    private final MenuFactory menuFactory;

    public ChangeMenuCommand() {
        menuFactory = new MenuFactory(5, Component.text("FarmMancy Ability Swapper"));

        menuFactory.addMenuFiller(new Fillers.FillAll(new MenuItem(ItemStack.of(Material.BLACK_STAINED_GLASS_PANE), false)));

        menuFactory.addMenuFiller(new MenuFiller() {

            static class FungusTracker implements MenuTicker {
                @Override
                public void tick(@NotNull Menu menu) {
                    MenuItem slot1 = new MenuItem(ItemStack.of(Material.CREEPER_HEAD),
                            Component.text("Placeholder Entity", NamedTextColor.AQUA)
                                    .decoration(TextDecoration.ITALIC, false))
                            .setEnchantmentGlintOverride(false)
                            .deleteAttributes()
                            .setClickHandler(new ClickHandler() {
                                @Override
                                public @NotNull ClickResponse onClicked(@NotNull Menu menuInstance, @NotNull MenuItem menuItem, @NotNull InventoryClickEvent event) {
                                    if (event.getClick() == ClickType.LEFT) {
                                        menu.getPlayer().playSound(menu.getPlayer(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.0f);
                                    }
                                    return new ClickResponse.Nothing();
                                }
                            });

                    MenuItem air = new MenuItem(ItemStack.of(Material.AIR));

                    //sect 1
                    menu.setItem(2, 2, slot1);
                    menu.setItem(2, 3, slot1);
                    menu.setItem(2, 4, slot1);

                    //sect 2
                    for (int x = 4; x < 9; x++) {
                        for (int y = 2; y < 5; y++) {
                            menu.setItem(x, y, air);
                        }
                    }
                    menu.setItem(4, 2, slot1);
                }
            }

            @Override
            public void fillMenu(@NotNull Menu menu) {
                FungusTracker fungusTracker = new FungusTracker();
                menu.setMenuTicker(fungusTracker);
            }
        });

        /*
        //unfinished
        new CommandAPICommand("testMenu")
                .withPermission("CommandPermission.OP")
                .executesPlayer((player, args) -> {
                    menuFactory.sendToPlayer(player);
                })
                .register();
        */
    }
}

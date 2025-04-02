package me.tormented.farmmancy.Commands;

import dev.jorel.commandapi.CommandAPICommand;
import me.tormented.farmmancy.CowMancer.CowMancer;
import me.tormented.farmmancy.CowMancer.TickingCow;
import me.tormented.farmmancy.FarmConfig;
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

import java.util.List;

public class MenuCommand {
    private final MenuFactory menuFactory;
    private static final int maxMobCap = FarmConfig.getInstance().getMaxMobCap();

    public MenuCommand() {
        menuFactory = new MenuFactory(5, Component.text("FarmMancy Station"));

        menuFactory.addMenuFiller(new Fillers.FillAll(new MenuItem(ItemStack.of(Material.BLACK_STAINED_GLASS_PANE), false)));

        menuFactory.addMenuFiller(new MenuFiller() {

            static class FungusTracker implements MenuTicker {
                int amountToSpawn = 8;
                boolean isBaby = false;

                @Override
                public void tick(Menu menu) {
                    MenuItem FungusButton = new MenuItem(ItemStack.of(Material.NETHERITE_HOE),
                            Component.text("Become a FarmMancer", NamedTextColor.YELLOW)
                                    .decoration(TextDecoration.ITALIC, false))
                            .setEnchantmentGlintOverride(true)
                            .deleteAttributes()
                            .setClickHandler(new ClickHandler() {
                                @Override
                                public @NotNull ClickResponse onClicked(@NotNull Menu menuInstance, @NotNull MenuItem menuItem, @NotNull InventoryClickEvent event) {
                                    if (event.getClick() == ClickType.LEFT) {
                                        CowMancer theMancer = TickingCow.getInstance().setCowMancer(menuInstance.getPlayer());
                                        theMancer.createAll(amountToSpawn, isBaby);
                                        menu.getPlayer().playSound(menu.getPlayer(), Sound.BLOCK_ANVIL_USE, 1.0f, 1.0f);
                                        return new ClickResponse.CloseMenu();
                                    }
                                    return new ClickResponse.Nothing();
                                }
                            })
                            .setLore(List.of(
                                    Component.text("Mob Count: " + amountToSpawn, NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false)
                            ));

                    MenuItem ToggleBaby = new MenuItem(ItemStack.of(Material.WHEAT),
                            Component.text("BabyMode: " + (isBaby ? "Yes" : "No"), NamedTextColor.YELLOW)
                                    .decoration(TextDecoration.ITALIC, false))
                            .setEnchantmentGlintOverride(isBaby)
                            .deleteAttributes()
                            .setClickHandler(new ClickHandler() {
                                @Override
                                public @NotNull ClickResponse onClicked(@NotNull Menu menuInstance, @NotNull MenuItem menuItem, @NotNull InventoryClickEvent event) {
                                    if (event.getClick() == ClickType.LEFT) {
                                        isBaby = !isBaby;
                                        menu.getPlayer().playSound(menu.getPlayer(), Sound.BLOCK_DISPENSER_DISPENSE, 1.0f, 1.0f);
                                    }
                                    return new ClickResponse.Nothing();
                                }
                            });

                    MenuItem UpButton_Plus2 = new MenuItem(ItemStack.of(Material.LIME_STAINED_GLASS),
                            Component.text("+2 Mobs", NamedTextColor.GREEN)
                                    .decoration(TextDecoration.ITALIC, false))
                            .setEnchantmentGlintOverride(false)
                            .deleteAttributes()
                            .setClickHandler(new ClickHandler() {
                                @Override
                                public @NotNull ClickResponse onClicked(@NotNull Menu menuInstance, @NotNull MenuItem menuItem, @NotNull InventoryClickEvent event) {
                                    if (event.getClick() == ClickType.LEFT) {
                                        if ((amountToSpawn + 2) > maxMobCap) {
                                            menu.getPlayer().playSound(menu.getPlayer(), Sound.ENTITY_PILLAGER_HURT, 1.0f, 1.0f);
                                            amountToSpawn = maxMobCap;
                                        } else {
                                            menu.getPlayer().playSound(menu.getPlayer(), Sound.BLOCK_NOTE_BLOCK_PLING, 2.0f, 1.0f);
                                            amountToSpawn += 2;
                                        }
                                    }
                                    return new ClickResponse.Nothing();
                                }
                            })
                            .setLore(List.of(
                                    Component.text("(" + amountToSpawn + ")", NamedTextColor.DARK_GREEN).decoration(TextDecoration.ITALIC, false)
                            ));

                    MenuItem DownButton_Subtract2 = new MenuItem(ItemStack.of(Material.RED_STAINED_GLASS),
                            Component.text("-2 Mobs", NamedTextColor.RED)
                                    .decoration(TextDecoration.ITALIC, false))
                            .setEnchantmentGlintOverride(false)
                            .deleteAttributes()
                            .setClickHandler(new ClickHandler() {
                                @Override
                                public @NotNull ClickResponse onClicked(@NotNull Menu menuInstance, @NotNull MenuItem menuItem, @NotNull InventoryClickEvent event) {
                                    if (event.getClick() == ClickType.LEFT) {
                                        if ((amountToSpawn - 2) < 0) {
                                            menu.getPlayer().playSound(menu.getPlayer(), Sound.ENTITY_PILLAGER_HURT, 1.0f, 1.0f);
                                            amountToSpawn = 0;
                                        } else {
                                            menu.getPlayer().playSound(menu.getPlayer(), Sound.BLOCK_NOTE_BLOCK_PLING, 2.0f, 0.7f);
                                            amountToSpawn -= 2;
                                        }
                                    }
                                    return new ClickResponse.Nothing();
                                }
                            })
                            .setLore(List.of(
                                    Component.text("(" + amountToSpawn + ")", NamedTextColor.DARK_RED).decoration(TextDecoration.ITALIC, false)
                            ));

                    MenuItem UpButton_Plus4 = new MenuItem(ItemStack.of(Material.LIME_STAINED_GLASS),
                            Component.text("+4 Mobs", NamedTextColor.GREEN)
                                    .decoration(TextDecoration.ITALIC, false))
                            .setEnchantmentGlintOverride(false)
                            .deleteAttributes()
                            .setClickHandler(new ClickHandler() {
                                @Override
                                public @NotNull ClickResponse onClicked(@NotNull Menu menuInstance, @NotNull MenuItem menuItem, @NotNull InventoryClickEvent event) {
                                    if (event.getClick() == ClickType.LEFT) {
                                        if (event.getClick() == ClickType.LEFT) {
                                            if ((amountToSpawn + 4) > maxMobCap) {
                                                menu.getPlayer().playSound(menu.getPlayer(), Sound.ENTITY_PILLAGER_HURT, 1.0f, 1.0f);
                                                amountToSpawn = maxMobCap;
                                            } else {
                                                menu.getPlayer().playSound(menu.getPlayer(), Sound.BLOCK_NOTE_BLOCK_PLING, 2.0f, 1.0f);
                                                amountToSpawn += 4;
                                            }
                                        }
                                    }
                                    return new ClickResponse.Nothing();
                                }
                            })
                            .setLore(List.of(
                                    Component.text("(" + amountToSpawn + ")", NamedTextColor.DARK_GREEN).decoration(TextDecoration.ITALIC, false)
                            ));

                    MenuItem DownButton_Subtract4 = new MenuItem(ItemStack.of(Material.RED_STAINED_GLASS),
                            Component.text("-4 Mobs", NamedTextColor.RED)
                                    .decoration(TextDecoration.ITALIC, false))
                            .setEnchantmentGlintOverride(false)
                            .deleteAttributes()
                            .setClickHandler(new ClickHandler() {
                                @Override
                                public @NotNull ClickResponse onClicked(@NotNull Menu menuInstance, @NotNull MenuItem menuItem, @NotNull InventoryClickEvent event) {
                                    if (event.getClick() == ClickType.LEFT) {
                                        if ((amountToSpawn - 4) < 0) {
                                            menu.getPlayer().playSound(menu.getPlayer(), Sound.ENTITY_PILLAGER_HURT, 1.0f, 1.0f);
                                            amountToSpawn = 0;
                                        } else {
                                            menu.getPlayer().playSound(menu.getPlayer(), Sound.BLOCK_NOTE_BLOCK_PLING, 2.0f, 0.7f);
                                            amountToSpawn -= 4;
                                        }
                                    }
                                    return new ClickResponse.Nothing();
                                }
                            })
                            .setLore(List.of(
                                    Component.text("(" + amountToSpawn + ")", NamedTextColor.DARK_RED).decoration(TextDecoration.ITALIC, false)
                            ));

                    menu.setItem(3, 3, DownButton_Subtract2);
                    menu.setItem(7, 3, UpButton_Plus2);

                    menu.setItem(2, 3, DownButton_Subtract4);
                    menu.setItem(8, 3, UpButton_Plus4);

                    menu.setItem(5, 3, FungusButton);
                    menu.setItem(5, 5, ToggleBaby);
                }
            }

            @Override
            public void fillMenu(@NotNull Menu menu) {
                FungusTracker fungusTracker = new FungusTracker();

                MenuItem RemoveAbility = new MenuItem(ItemStack.of(Material.REDSTONE_BLOCK),
                        Component.text("Relinquish your powers", NamedTextColor.RED)
                                .decoration(TextDecoration.ITALIC, false))
                        .setEnchantmentGlintOverride(false)
                        .deleteAttributes()
                        .setClickHandler(new ClickHandler() {
                            @Override
                            public @NotNull ClickResponse onClicked(@NotNull Menu menuInstance, @NotNull MenuItem menuItem, @NotNull InventoryClickEvent event) {
                                TickingCow.getInstance().removeCowMancer(menuInstance.getPlayer());
                                menu.getPlayer().playSound(menu.getPlayer(), Sound.BLOCK_ANVIL_USE, 1.0f, 0.7f);
                                return new ClickResponse.Nothing();
                            }
                        });

                menu.setItem(5, 1, RemoveAbility);
                menu.setMenuTicker(fungusTracker);
            }
        });

        new CommandAPICommand("farmMenu")
                .withPermission("CommandPermission.OP")
                .executesPlayer((player, args) -> {
                    menuFactory.sendToPlayer(player);
                })
                .register();
    }
}

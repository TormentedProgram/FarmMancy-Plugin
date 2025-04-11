package me.tormented.farmmancy.commands;

import dev.jorel.commandapi.CommandAPICommand;
import me.tormented.farmmancy.abilities.Ability;
import me.tormented.farmmancy.abilities.AbilityFactory;
import me.tormented.farmmancy.abilities.MobAbility;
import me.tormented.farmmancy.farmmancer.FarmMancer;
import me.tormented.farmmancy.farmmancer.FarmMancerManager;
import me.tormented.farmmancy.inventoryMenu.*;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class ChangeMenuCommand {
    private final MenuFactory menuFactory;

    public ChangeMenuCommand() {
        menuFactory = new MenuFactory(5, Component.text("FarmMancy Ability Swapper"));

        menuFactory.addMenuFiller(new Fillers.FillAll(new MenuItem(ItemStack.of(Material.BLACK_STAINED_GLASS_PANE), false)));

        menuFactory.addMenuFiller(new MenuFiller() {

            static class FungusTracker implements MenuTicker {
                @Override
                public void tick(@NotNull Menu menu) {
                    MenuItem air = new MenuItem(ItemStack.of(Material.AIR));
                    Map<ItemStack, AbilityFactory> equippedAbilityHeads = new HashMap<>();

                    Map<ItemStack, Object> slot0 = getEquippedAbilitySlot(0, menu.getPlayer());
                    Map<ItemStack, Object> slot1 = getEquippedAbilitySlot(1, menu.getPlayer());
                    Map<ItemStack, Object> slot2 = getEquippedAbilitySlot(2, menu.getPlayer());

                    for (Map.Entry<ItemStack, Object> entry : slot0.entrySet()) {
                        AbilityFactory abilityFactory = (entry.getValue() instanceof AbilityFactory) ? (AbilityFactory) entry.getValue() : null;
                        equippedAbilityHeads.put(entry.getKey(), abilityFactory);
                    }

                    for (Map.Entry<ItemStack, Object> entry : slot1.entrySet()) {
                        AbilityFactory abilityFactory = (entry.getValue() instanceof AbilityFactory) ? (AbilityFactory) entry.getValue() : null;
                        equippedAbilityHeads.put(entry.getKey(), abilityFactory);
                    }

                    for (Map.Entry<ItemStack, Object> entry : slot2.entrySet()) {
                        AbilityFactory abilityFactory = (entry.getValue() instanceof AbilityFactory) ? (AbilityFactory) entry.getValue() : null;
                        equippedAbilityHeads.put(entry.getKey(), abilityFactory);
                    }

                    if (slot0 != null) {
                        for (Map.Entry<ItemStack, Object> entry : slot0.entrySet()) {
                            ItemStack head = entry.getKey();
                            AbilityFactory factory = (entry.getValue() instanceof AbilityFactory) ? (AbilityFactory) entry.getValue() : null;
                            TextComponent headName = (factory != null) ? (TextComponent) factory.getName() : Component.text("Empty Slot", NamedTextColor.DARK_GRAY);
                            if (head != null) {
                                menu.setItem(2, 2, new MenuItem(head,
                                        headName.decoration(TextDecoration.ITALIC, false))
                                        .setEnchantmentGlintOverride(false)
                                        .deleteAttributes()
                                        .setClickHandler(new ClickHandler() {
                                            @Override
                                            public @NotNull ClickResponse onClicked(@NotNull Menu menuInstance, @NotNull MenuItem menuItem, @NotNull InventoryClickEvent event) {
                                                if (event.getClick() == ClickType.LEFT) {
                                                    System.out.println("Clicked on " + head.getType());
                                                }
                                                return new ClickResponse.Nothing();
                                            }
                                        }));
                            }
                        }
                    }
                    if (slot1 != null) {
                        for (Map.Entry<ItemStack, Object> entry : slot1.entrySet()) {
                            ItemStack head = entry.getKey();
                            AbilityFactory factory = (entry.getValue() instanceof AbilityFactory) ? (AbilityFactory) entry.getValue() : null;
                            TextComponent headName = (factory != null) ? (TextComponent) factory.getName() : Component.text("Empty Slot", NamedTextColor.DARK_GRAY);
                            if (head != null) {
                                menu.setItem(2, 3, new MenuItem(head,
                                        headName.decoration(TextDecoration.ITALIC, false))
                                        .setEnchantmentGlintOverride(false)
                                        .deleteAttributes()
                                        .setClickHandler(new ClickHandler() {
                                            @Override
                                            public @NotNull ClickResponse onClicked(@NotNull Menu menuInstance, @NotNull MenuItem menuItem, @NotNull InventoryClickEvent event) {
                                                if (event.getClick() == ClickType.LEFT) {
                                                    System.out.println("Clicked on " + head.getType());
                                                }
                                                return new ClickResponse.Nothing();
                                            }
                                        }));
                            }
                        }
                    }
                    if (slot2 != null) {
                        for (Map.Entry<ItemStack, Object> entry : slot2.entrySet()) {
                            ItemStack head = entry.getKey();
                            AbilityFactory factory = (entry.getValue() instanceof AbilityFactory) ? (AbilityFactory) entry.getValue() : null;
                            TextComponent headName = (factory != null) ? (TextComponent) factory.getName() : Component.text("Empty Slot", NamedTextColor.DARK_GRAY);
                            if (head != null) {
                                menu.setItem(2, 4, new MenuItem(head,
                                        headName.decoration(TextDecoration.ITALIC, false))
                                        .setEnchantmentGlintOverride(false)
                                        .deleteAttributes()
                                        .setClickHandler(new ClickHandler() {
                                            @Override
                                            public @NotNull ClickResponse onClicked(@NotNull Menu menuInstance, @NotNull MenuItem menuItem, @NotNull InventoryClickEvent event) {
                                                if (event.getClick() == ClickType.LEFT) {
                                                    System.out.println("Clicked on " + head.getType());
                                                }
                                                return new ClickResponse.Nothing();
                                            }
                                        }));
                            }
                        }
                    }

                    for (int x = 4; x < 9; x++) {
                        for (int y = 2; y < 5; y++) {
                            menu.setItem(x, y, air);
                        }
                    }

                    Map<ItemStack, AbilityFactory> abilityHeads = getUnlockedAbilitySlots(menu.getPlayer());
                    for (Map.Entry<ItemStack, AbilityFactory> equippedEntry : equippedAbilityHeads.entrySet()) {
                        ItemStack equippedHead = equippedEntry.getKey();
                        AbilityFactory equippedAbilityFactory = equippedEntry.getValue();

                        if (equippedHead != null && equippedAbilityFactory != null) {
                            List<ItemStack> keysToRemove = new ArrayList<>();

                            for (Map.Entry<ItemStack, AbilityFactory> unlockedEntry : abilityHeads.entrySet()) {
                                AbilityFactory unlockedAbilityFactory = unlockedEntry.getValue();

                                if (equippedAbilityFactory.equals(unlockedAbilityFactory)) {
                                    keysToRemove.add(unlockedEntry.getKey());
                                }
                            }

                            for (ItemStack key : keysToRemove) {
                                abilityHeads.remove(key);
                            }
                        }
                    }


                    List<Map.Entry<ItemStack, AbilityFactory>> entries = new ArrayList<>(abilityHeads.entrySet());
                    entries.sort((entry1, entry2) -> {
                        Object name1 = entry1.getValue().getName();
                        Object name2 = entry2.getValue().getName();

                        if (name1 instanceof TextComponent textComponent1 && name2 instanceof TextComponent textComponent2) {
                            return textComponent1.content().compareToIgnoreCase(textComponent2.content());
                        }
                        return 0;
                    });

                    int x = 0;
                    int y = 0;
                    for (Map.Entry<ItemStack, AbilityFactory> entry : entries) {
                        ItemStack head = entry.getKey();
                        AbilityFactory factory = entry.getValue();
                        if (head != null) {
                            if (x >= menu.getRows()) {
                                y += 1;
                                x = 0;
                            }
                            if (y <= 4 && (4 + x) < menu.getInventory().getSize() && (2 + y) < menu.getInventory().getSize() / menu.getColumns()) { // Ensure within bounds
                                MenuItem menuItem = new MenuItem(head,
                                        factory.getName()
                                                .decoration(TextDecoration.ITALIC, false))
                                        .setEnchantmentGlintOverride(false)
                                        .deleteAttributes()
                                        .setClickHandler(new ClickHandler() {
                                            @Override
                                            public @NotNull ClickResponse onClicked(@NotNull Menu menuInstance, @NotNull MenuItem menuItem, @NotNull InventoryClickEvent event) {
                                                if (event.getClick() == ClickType.LEFT) {
                                                    System.out.println("Clicked on " + head.getType());
                                                }
                                                return new ClickResponse.Nothing();
                                            }
                                        })
                                        .setLore(List.of(
                                                MiniMessage.miniMessage().deserialize("<!italic><bold><gold>LEFT-CLICK</gold></bold><dark_gray> to select then select a slot</dark_gray></!italic>")
                                        ));
                                menu.setItem(4 + x, 2 + y, menuItem);
                            }
                            x++;
                        }
                    }

                }
            }

            @Override
            public void fillMenu(@NotNull Menu menu) {
                FungusTracker fungusTracker = new FungusTracker();
                fungusTracker.tick(menu);

                MenuItem slot1 = new MenuItem(ItemStack.of(Material.LIME_WOOL),
                        Component.text("Update Menu", NamedTextColor.AQUA)
                                .decoration(TextDecoration.ITALIC, false))
                        .setEnchantmentGlintOverride(false)
                        .deleteAttributes()
                        .setClickHandler(new ClickHandler() {
                            @Override
                            public @NotNull ClickResponse onClicked(@NotNull Menu menuInstance, @NotNull MenuItem menuItem, @NotNull InventoryClickEvent event) {
                                if (event.getClick() == ClickType.LEFT) {
                                    fungusTracker.tick(menu);
                                }
                                return new ClickResponse.Nothing();
                            }
                        });

                menu.setItem(1, 1, slot1);
            }
        });

        new CommandAPICommand("farmmancy")
                .withSubcommand(
                        new CommandAPICommand("menu")
                                .withSubcommand(
                                        new CommandAPICommand("change")
                                                .withPermission("farmmancy.menu.change")
                                                .executesPlayer((player, args) -> {
                                                    menuFactory.sendToPlayer(player);
                                                }))
                ).register();
    }

    public static Map<ItemStack, AbilityFactory> getUnlockedAbilitySlots(Player player) {
        if (FarmMancerManager.getInstance().farmMancerMap.containsKey(player)) {
            FarmMancer farmMancer = FarmMancerManager.getInstance().farmMancerMap.get(player);
            if (farmMancer != null) {
                Map<String, Ability> abilityMap = farmMancer.getMappedUnlockedAbilities();
                Map<ItemStack, AbilityFactory> abilityHeads = new HashMap<>();
                for (Map.Entry<String, Ability> entry : abilityMap.entrySet()) {
                    if (entry.getValue() instanceof MobAbility<?> ability) {
                        abilityHeads.put(ability.getHeadItem(null), ability.getAbilityFactory());
                    }
                }
                return abilityHeads;
            }
        }
        return Collections.emptyMap();
    }

    public static Map<ItemStack, Object> getEquippedAbilitySlot(int slot, Player player) {
        if (FarmMancerManager.getInstance().farmMancerMap.containsKey(player)) {
            FarmMancer farmMancer = FarmMancerManager.getInstance().farmMancerMap.get(player);
            if (farmMancer != null) {
                Map<String, Ability> abilityMap = farmMancer.getMappedUnlockedAbilities();
                Map<ItemStack, Object> singleEntryMap = new HashMap<>();
                for (Map.Entry<String, Ability> entry : abilityMap.entrySet()) {
                    if (entry.getValue() instanceof MobAbility<?> ability) {
                        if (ability.slot == slot) {
                            ItemStack headItem = ability.getHeadItem(null);
                            singleEntryMap.put(headItem, ability.getAbilityFactory());
                            return singleEntryMap;
                        }
                    }
                }
            }
        }
        Map<ItemStack, Object> singleEntryMap = new HashMap<>();
        singleEntryMap.put(new ItemStack(Material.SKELETON_SKULL), null);
        return singleEntryMap;
    }
}

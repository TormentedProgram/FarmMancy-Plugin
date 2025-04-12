package me.tormented.farmmancy.commands;

import dev.jorel.commandapi.CommandAPICommand;
import me.tormented.farmmancy.abilities.Ability;
import me.tormented.farmmancy.farmmancer.FarmMancer;
import me.tormented.farmmancy.farmmancer.FarmMancerManager;
import me.tormented.farmmancy.inventoryMenu.*;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class ChangeMenuCommand {
    private static MenuFactory menuFactory = null;

    public static MenuFactory getMenuFactory() {
        return menuFactory;
    }

    private static class AbilityChangeMenuInstance {

        private static final PlainTextComponentSerializer plainTextComponentSerializer = PlainTextComponentSerializer.plainText();

        private final Menu menu;
        private final FarmMancer farmMancer;

        public Ability[] equippedAbilities;
        public Set<Ability> unlockedAbilities;

        private Ability equippingAbility = null;


        public AbilityChangeMenuInstance(@NotNull Menu menu, @NotNull FarmMancer farmMancer) {
            this.menu = menu;
            this.farmMancer = farmMancer;

            this.equippedAbilities = farmMancer.getEquippedAbilities();
            this.unlockedAbilities = getAbilitiesFromIterator(farmMancer.getUnlockedAbilities());

            menuUpdateEquippedAbilities();
            menuUpdateUnlockedAbilities();
        }

        private static @NotNull Set<Ability> getAbilitiesFromIterator(@NotNull Iterator<Ability> abilityIterator) {
            Set<Ability> abilitySet = new HashSet<>();

            while (abilityIterator.hasNext()) {
                abilitySet.add(abilityIterator.next());
            }

            return abilitySet;
        }

        private static @NotNull List<Ability> sortAbilities(@NotNull Set<Ability> abilitySet) {
            List<Ability> abilityList = new ArrayList<>(abilitySet);

            abilityList.sort((entry1, entry2) -> {
                Component name1 = entry1.getName();
                Component name2 = entry2.getName();
                return plainTextComponentSerializer.serialize(name1).compareToIgnoreCase(plainTextComponentSerializer.serialize(name2));

            });
            return abilityList;
        }

        public void menuUpdateEquippedAbilities() {
            for (int slotIndex = 0; slotIndex < 3; slotIndex++) {
                Ability slotAbility = equippedAbilities[slotIndex];

                MenuItem menuItem;

                if (slotAbility != null) {
                    menuItem = new MenuItem(slotAbility.getAbilityFactory().getIcon(), slotAbility.getAbilityFactory().getName());
                } else {
                    menuItem = new MenuItem(ItemStack.of(Material.STONE_BUTTON), Component.text("Empty Ability Slot", NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false));
                }


                int finalSlotIndex = slotIndex;
                menuItem.setClickHandler(new ClickHandler() {

                    final AbilityChangeMenuInstance abilityChangeMenuInstance;
                    int slotIndex;

                    {
                        abilityChangeMenuInstance = AbilityChangeMenuInstance.this;
                        slotIndex = finalSlotIndex;
                    }

                    @Override
                    public @NotNull ClickResponse onClicked(@NotNull Menu menuInstance, @NotNull MenuItem menuItem, @NotNull InventoryClickEvent event) {
                        if (event.getClick() == ClickType.LEFT) {
                            if (equippingAbility != null) {
                                farmMancer.setEquippedAbility(slotIndex, equippingAbility);
                                equippedAbilities = farmMancer.getEquippedAbilities();
                                equippingAbility = null;
                                menuUpdateEquippedAbilities();
                                menuUpdateUnlockedAbilities();
                                farmMancer.activateAll();
                                menuInstance.getPlayer().playSound(menuInstance.getPlayer(), Sound.BLOCK_DISPENSER_DISPENSE, 1.0f, 2.0f);
                            } else {
                                if (slotAbility != null) {
                                    farmMancer.deactivateAll(true);
                                    farmMancer.setEquippedAbility(slotIndex, null);
                                    menuInstance.getPlayer().playSound(menuInstance.getPlayer(), Sound.ENTITY_VILLAGER_DEATH, 1.0f, 0.7f);
                                    equippedAbilities = farmMancer.getEquippedAbilities();
                                    menuUpdateEquippedAbilities();
                                    menuUpdateUnlockedAbilities();
                                }
                            }
                        }
                        return new ClickResponse.Nothing();
                    }
                });

                menu.setItem(2, 2 + slotIndex, menuItem);

            }
        }

        public void menuUpdateUnlockedAbilities() {
            Set<Ability> unlockedAbilitiesClone = new HashSet<>(unlockedAbilities);
            Arrays.stream(equippedAbilities).filter(Objects::nonNull).toList().forEach(unlockedAbilitiesClone::remove);
            List<Ability> sortedAbilities = sortAbilities(unlockedAbilitiesClone);
            for (int slotIndex = 0; slotIndex < 5 * 3; slotIndex++) {

                MenuItem menuItem;

                if (slotIndex < sortedAbilities.size()) {
                    Ability slotAbility = sortedAbilities.get(slotIndex);


                    if (slotAbility == equippingAbility) {
                        menuItem = new MenuItem(
                                slotAbility.getAbilityFactory().getIcon(),
                                slotAbility.getAbilityFactory().getName(),
                                List.of(
                                        MiniMessage.miniMessage().deserialize("<!italic><bold><green>SELECTED</green></bold><dark_green> for equipping</dark_green></!italic>")
                                )
                        ).setClickHandler(new UnlockedSlotAbility(slotAbility, this));
                    } else {
                        menuItem = new MenuItem(
                                slotAbility.getAbilityFactory().getIcon(),
                                slotAbility.getAbilityFactory().getName(),
                                List.of(
                                        MiniMessage.miniMessage().deserialize("<!italic><bold><gold>LEFT-CLICK</gold></bold><dark_gray> to select for equipping</dark_gray></!italic>")
                                )
                        )
                                .setClickHandler(new UnlockedSlotAbility(slotAbility, this));
                    }
                } else {
                    menuItem = new MenuItem(ItemStack.of(Material.AIR));
                }
                menu.setItem((slotIndex % 5) + 4, (slotIndex / 5) + 2, menuItem);
            }
        }

        public void toggledAbility(Ability ability) {
            if (equippingAbility == ability) {
                equippingAbility = null;
                menuUpdateUnlockedAbilities();
                return;
            }
            if (equippingAbility != null) {
                equippingAbility = null;
            }
            equippingAbility = ability;
            menuUpdateUnlockedAbilities();
        }

        record UnlockedSlotAbility(Ability ability,
                                   AbilityChangeMenuInstance abilityChangeMenuInstance) implements ClickHandler {

            @Override
            public @NotNull ClickResponse onClicked(@NotNull Menu menuInstance, @NotNull MenuItem menuItem, @NotNull InventoryClickEvent event) {
                if (event.getClick() == ClickType.LEFT) {
                    abilityChangeMenuInstance.toggledAbility(ability);
                    menuInstance.getPlayer().playSound(menuInstance.getPlayer(), Sound.BLOCK_DISPENSER_DISPENSE, 0.5f, 2.0f);
                }
                return new ClickResponse.Nothing();
            }
        }

    }

    public ChangeMenuCommand() {
        menuFactory = new MenuFactory(5, Component.text("FarmMancy Ability Swapper"));

        menuFactory.addMenuFiller(new Fillers.FillAll(new MenuItem(ItemStack.of(Material.BLACK_STAINED_GLASS_PANE), false)));

        menuFactory.addMenuFiller(new MenuFiller() {
            @Override
            public void fillMenu(@NotNull Menu menu) {

                if (FarmMancerManager.getInstance().farmMancerMap.get(menu.getPlayer().getUniqueId()) instanceof FarmMancer farmMancer) {
                    new AbilityChangeMenuInstance(menu, farmMancer);
                } else {
                    menu.setItem(5, 3, new MenuItem(
                            ItemStack.of(Material.BARRIER),
                            Component.text("ERROR", NamedTextColor.DARK_RED, TextDecoration.BOLD).decoration(TextDecoration.ITALIC, false),
                            List.of(
                                    Component.text("You don't seem to be a farmmancer!", NamedTextColor.RED).decoration(TextDecoration.ITALIC, false),
                                    Component.text("This menu will not work without you", NamedTextColor.RED).decoration(TextDecoration.ITALIC, false),
                                    Component.text("being one!", NamedTextColor.RED).decoration(TextDecoration.ITALIC, false)
                            )
                    ));
                }
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
                                                        }
                                                )
                                )
                ).register();
    }

}

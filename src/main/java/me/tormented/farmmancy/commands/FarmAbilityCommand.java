package me.tormented.farmmancy.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;
import me.tormented.farmmancy.abilities.Ability;
import me.tormented.farmmancy.farmmancer.FarmMancer;
import me.tormented.farmmancy.farmmancer.FarmMancerManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public class FarmAbilityCommand {
    public FarmAbilityCommand() {
        new CommandAPICommand("farmability")
                .withPermission("CommandPermission.OP")
                .withArguments(new MultiLiteralArgument("doing", "set"))
                .withArguments(new EntitySelectorArgument.ManyPlayers("Players", false))
                .withArguments(new IntegerArgument("slot", 1, 4))
                .withArguments(new GreedyStringArgument("abilityType").replaceSuggestions(
                        ArgumentSuggestions.stringCollection(info ->
                                {
                                    if (info.sender() instanceof Player player) {
                                        if (FarmMancerManager.getInstance().farmMancerMap.containsKey(player)) {
                                            FarmMancer farmMancer = FarmMancerManager.getInstance().farmMancerMap.get(player);
                                            if (farmMancer != null) {
                                                return new ArrayList<>(farmMancer.getMappedUnlockedAbilities().keySet());
                                            }
                                        }
                                    }
                                    return Collections.emptyList();
                                }
                        )))
                .executes((sender, args) -> {
                    String doing = (String) args.get("doing");
                    Object slotObject = args.get("slot");
                    String abilityType = (String) args.get("abilityType");
                    @SuppressWarnings("unchecked")
                    Collection<Player> players = (Collection<Player>) args.get("Players");

                    switch (doing) {
                        case "set" -> {
                            if (players != null && slotObject instanceof Integer slot && abilityType != null) {
                                for (Player player : players) {
                                    if (FarmMancerManager.getInstance().farmMancerMap.containsKey(player)) {
                                        FarmMancer farmMancer = FarmMancerManager.getInstance().farmMancerMap.get(player);
                                        Map<String, Ability> unlockedAbilities = getPlayerAbilities(sender);
                                        if (unlockedAbilities.containsKey(abilityType)) {
                                            farmMancer.setEquippedAbility(slot - 1, unlockedAbilities.get(abilityType));
                                            player.sendMessage(Component.text("Setting ability " + abilityType + " to slot " + slot).color(NamedTextColor.GREEN));
                                        } else {
                                            player.sendMessage(Component.text("You do not have this ability unlocked or doesn't exist!").color(NamedTextColor.RED));
                                        }
                                    } else {
                                        player.sendMessage(Component.text("You are not a FarmMancer...").color(NamedTextColor.RED));
                                    }
                                }
                            }
                        }
                        case null -> throw new NullPointerException("Set type is null");
                        default -> throw new IllegalStateException("Unexpected value: " + doing);
                    }
                })
                .register();
    }

    public Map<String, Ability> getPlayerAbilities(CommandSender sender) {
        if (sender instanceof Player player) {
            if (FarmMancerManager.getInstance().farmMancerMap.containsKey(player)) {
                FarmMancer farmMancer = FarmMancerManager.getInstance().farmMancerMap.get(player);
                if (farmMancer != null) {
                    return farmMancer.getMappedUnlockedAbilities();
                }
            }
        }
        return Collections.emptyMap();
    }
}

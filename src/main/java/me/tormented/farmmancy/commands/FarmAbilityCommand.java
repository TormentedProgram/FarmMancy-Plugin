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

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public class FarmAbilityCommand {
    public FarmAbilityCommand() {
        new CommandAPICommand("farmability")
                .withPermission("CommandPermission.OP")
                .withArguments(new MultiLiteralArgument("doing", "set"))
                .withArguments(new EntitySelectorArgument.ManyPlayers("Players", false))
                .withArguments(new StringArgument("abilityType").replaceSuggestions(
                        ArgumentSuggestions.strings(info ->
                                getPlayerAbilitiesNames(info.sender())
                        )))
                .withArguments(new IntegerArgument("slot", 1, 4))
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
                                    player.sendMessage(Component.text("Setting ability " + abilityType + " to slot " + slot).color(NamedTextColor.GREEN));
                                    FarmMancer farmMancer = FarmMancerManager.getInstance().farmMancerMap.get(player);
                                    Map<String, Ability> unlockedAbilities = getPlayerAbilities(sender);
                                    farmMancer.setEquippedAbility(slot-1, unlockedAbilities.get(abilityType));
                                }
                            }
                        }
                        case null -> throw new NullPointerException("Set type is null");
                        default -> throw new IllegalStateException("Unexpected value: " + doing);
                    }
                })
                .register();
    }

    public String[] getPlayerAbilitiesNames(CommandSender sender) {
        if (sender instanceof Player player) {
            if (FarmMancerManager.getInstance().farmMancerMap.containsKey(player)) {
                FarmMancer farmMancer = FarmMancerManager.getInstance().farmMancerMap.get(player);
                if (farmMancer != null) {
                    return farmMancer.getUnlockedAbilitiesOfDoom().keySet().toArray(new String[0]);
                }
            }
        }
        return new String[0];
    }

    public Map<String, Ability> getPlayerAbilities(CommandSender sender) {
        if (sender instanceof Player player) {
            if (FarmMancerManager.getInstance().farmMancerMap.containsKey(player)) {
                FarmMancer farmMancer = FarmMancerManager.getInstance().farmMancerMap.get(player);
                if (farmMancer != null) {
                    return farmMancer.getUnlockedAbilitiesOfDoom();
                }
            }
        }
        return Collections.emptyMap();
    }
}

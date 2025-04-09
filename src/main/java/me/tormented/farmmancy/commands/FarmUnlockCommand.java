package me.tormented.farmmancy.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import dev.jorel.commandapi.arguments.MultiLiteralArgument;
import me.tormented.farmmancy.Registries;
import me.tormented.farmmancy.abilities.AbilityFactory;
import me.tormented.farmmancy.farmmancer.FarmMancer;
import me.tormented.farmmancy.farmmancer.FarmMancerManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;

import java.util.Collection;

public class FarmUnlockCommand {
    public FarmUnlockCommand() {
        new CommandAPICommand("farmunlock")
                .withPermission("CommandPermission.OP")
                .withArguments(new MultiLiteralArgument("doing", "set"))
                .withArguments(new EntitySelectorArgument.ManyPlayers("Players", false))
                .withArguments(new MultiLiteralArgument("abilityType", Registries.abilityRegistry.getAllKeys().toArray(new String[0])))
                .executes((sender, args) -> {
                    String doing = (String) args.get("doing");
                    String abilityType = (String) args.get("abilityType");
                    @SuppressWarnings("unchecked")
                    Collection<Player> players = (Collection<Player>) args.get("Players");

                    switch (doing) {
                        case "set" -> {
                            if (players != null && abilityType != null) {
                                for (Player player : players) {
                                    if (FarmMancerManager.getInstance().farmMancerMap.containsKey(player)) {
                                        player.sendMessage(Component.text("Force unlocked ability: ")
                                                .append(Component.text(abilityType)
                                                        .color(NamedTextColor.AQUA))
                                                .color(NamedTextColor.GREEN));
                                        FarmMancer farmMancer = FarmMancerManager.getInstance().farmMancerMap.get(player);
                                        AbilityFactory factory = Registries.abilityRegistry.getFactory(abilityType);
                                        if (factory != null) {
                                            farmMancer.unlockAbility(factory);
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
}

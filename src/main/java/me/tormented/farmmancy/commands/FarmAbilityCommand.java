package me.tormented.farmmancy.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.arguments.MultiLiteralArgument;
import me.tormented.farmmancy.Registries;
import me.tormented.farmmancy.abilities.AbilityFactory;
import me.tormented.farmmancy.farmmancer.FarmMancer;
import me.tormented.farmmancy.farmmancer.FarmMancerManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.UUID;

public class FarmAbilityCommand {
    public FarmAbilityCommand() {
        new CommandAPICommand("farmability")
                .withPermission("CommandPermission.OP")
                .withArguments(new MultiLiteralArgument("doing", "set"))
                .withArguments(new EntitySelectorArgument.ManyPlayers("Players", false))
                .withArguments(new MultiLiteralArgument("abilityType", Registries.abilityRegistry.getAllKeys().toArray(new String[0])))
                .withArguments(new IntegerArgument("slot", 1, 4))
                .executes((sender, args) -> {
                    String doing = (String) args.get("doing");
                    Object slotObject = args.get("slot");
                    String abilityType = (String) args.get("abilityType");
                    @SuppressWarnings("unchecked")
                    Collection<Player> players = (Collection<Player>) args.get("Players");

                    switch (doing) {
                        case "set" -> {
                            if (players != null && slotObject instanceof Integer slot && abilityType != null && Registries.abilityRegistry.getFactory(abilityType) instanceof AbilityFactory abilityFactory) {
                                for (Player player : players) {
                                    FarmMancer theMancer = FarmMancerManager.getInstance().setFarmMancer(player);
                                    if (slot == 4) {
                                        theMancer.setSpecialEquippedAbility(abilityFactory.createAbility(UUID.randomUUID(), player.getUniqueId()));
                                    } else {
                                        theMancer.setEquippedAbility(slot - 1,  abilityFactory.createAbility(UUID.randomUUID(), player.getUniqueId()));
                                    }
                                    player.sendMessage(Component.text("Set " + abilityType.toUpperCase() + " in slot " + slot, NamedTextColor.GREEN));
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

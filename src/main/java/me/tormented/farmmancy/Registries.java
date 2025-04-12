package me.tormented.farmmancy;

import me.tormented.farmmancy.abilities.AbilityFactory;
import me.tormented.farmmancy.abilities.AbilityRegistry;
import me.tormented.farmmancy.abilities.implementations.*;
import me.tormented.farmmancy.abilities.implementations.customs.DioAbility;
import me.tormented.farmmancy.abilities.implementations.customs.ReisenAbility;
import me.tormented.farmmancy.abilities.implementations.customs.YuyukoAbility;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class Registries {
    public static final AbilityRegistry abilityRegistry = new AbilityRegistry();

    static {
        abilityRegistry.register("minecraft:cow", new AbilityFactory(CowAbility::new)
                .setName(Component.text("Cow Ability", NamedTextColor.GOLD).decoration(TextDecoration.ITALIC, false))
                .setIcon(CowAbility.headProvider.getHeadItem())
        );
        abilityRegistry.register("minecraft:pig", new AbilityFactory(PigAbility::new)
                .setName(Component.text("Pig Ability", NamedTextColor.LIGHT_PURPLE).decoration(TextDecoration.ITALIC, false))
                .setIcon(PigAbility.headProvider.getHeadItem())
        );
        abilityRegistry.register("minecraft:drowned", new AbilityFactory(DrownedAbility::new)
                .setName(Component.text("Drowned Ability", NamedTextColor.AQUA).decoration(TextDecoration.ITALIC, false))
                .setIcon(DrownedAbility.headProvider.getHeadItem())
        );
        abilityRegistry.register("minecraft:chicken", new AbilityFactory(ChickenAbility::new)
                .setName(Component.text("Chicken Ability", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false))
                .setIcon(ChickenAbility.headProvider.getHeadItem())
        );
        abilityRegistry.register("minecraft:bee", new AbilityFactory(BeeAbility::new)
                .setName(Component.text("Bee Ability", NamedTextColor.YELLOW).decoration(TextDecoration.ITALIC, false))
                .setIcon(BeeAbility.headProvider.getHeadItem())
        );
        abilityRegistry.register("minecraft:squid", new AbilityFactory(SquidAbility::new)
                .setName(Component.text("Squid Ability", TextColor.color(43, 45, 48)).decoration(TextDecoration.ITALIC, false))
                .setIcon(SquidAbility.headProvider.getHeadItem())
        );
        abilityRegistry.register("minecraft:blaze", new AbilityFactory(BlazeAbility::new)
                .setName(Component.text("Blaze Ability", NamedTextColor.GOLD).decoration(TextDecoration.ITALIC, false))
                .setIcon(BlazeAbility.headProvider.getHeadItem())
        );
        abilityRegistry.register("minecraft:warden", new AbilityFactory(WardenAbility::new)
                .setName(Component.text("Warden Ability", NamedTextColor.DARK_AQUA).decoration(TextDecoration.ITALIC, false))
                .setIcon(WardenAbility.headProvider.getHeadItem())
        );
        abilityRegistry.register("minecraft:strider", new AbilityFactory(StriderAbility::new)
                .setName(Component.text("Strider Ability", NamedTextColor.RED).decoration(TextDecoration.ITALIC, false))
                .setIcon(StriderAbility.headProvider.getHeadItem())
        );
        abilityRegistry.register("minecraft:enderman", new AbilityFactory(EndermanAbility::new)
                .setName(Component.text("Enderman Ability", NamedTextColor.LIGHT_PURPLE).decoration(TextDecoration.ITALIC, false))
                .setIcon(EndermanAbility.headProvider.getHeadItem())
        );
        abilityRegistry.register("minecraft:evoker", new AbilityFactory(EvokerAbility::new)
                .setName(Component.text("Evoker Ability", NamedTextColor.DARK_GREEN).decoration(TextDecoration.ITALIC, false))
                .setIcon(EvokerAbility.headProvider.getHeadItem())
        );
        abilityRegistry.register("minecraft:ghast", new AbilityFactory(GhastAbility::new)
                .setName(Component.text("Ghast Ability", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false))
                .setIcon(GhastAbility.headProvider.getHeadItem())
        );
        abilityRegistry.register("custom/dio", new AbilityFactory(DioAbility::new)
                .setName(MiniMessage.miniMessage().deserialize("<gradient:yellow:green>Dio Ability</gradient>"))
                .setIcon(DioAbility.headProvider.getHeadItem())
        );
        abilityRegistry.register("custom/yuyuko", new AbilityFactory(YuyukoAbility::new)
                .setName(MiniMessage.miniMessage().deserialize("<gradient:aqua:light_purple>Yuyuko Ability</gradient>"))
                .setIcon(YuyukoAbility.headProvider.getHeadItem())
        );
        abilityRegistry.register("custom/reisen", new AbilityFactory(ReisenAbility::new)
                .setName(MiniMessage.miniMessage().deserialize("<gradient:light_purple:red>Reisen Ability</gradient>"))
                .setIcon(ReisenAbility.headProvider.getHeadItem())
        );
    }
}

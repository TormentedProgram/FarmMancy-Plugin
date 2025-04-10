package me.tormented.farmmancy;

import me.tormented.farmmancy.abilities.AbilityFactory;
import me.tormented.farmmancy.abilities.AbilityRegistry;
import me.tormented.farmmancy.abilities.implementations.*;
import me.tormented.farmmancy.abilities.implementations.customs.*;

public class Registries {
    public static final AbilityRegistry abilityRegistry = new AbilityRegistry();

    static {
        abilityRegistry.register("minecraft:cow", new AbilityFactory(CowAbility::new));
        abilityRegistry.register("minecraft:pig", new AbilityFactory(PigAbility::new));
        abilityRegistry.register("minecraft:chicken", new AbilityFactory(ChickenAbility::new));
        abilityRegistry.register("minecraft:bee", new AbilityFactory(BeeAbility::new));
        abilityRegistry.register("minecraft:squid", new AbilityFactory(SquidAbility::new));
        abilityRegistry.register("minecraft:blaze", new AbilityFactory(BlazeAbility::new));
        abilityRegistry.register("minecraft:warden", new AbilityFactory(WardenAbility::new));
        abilityRegistry.register("minecraft:strider", new AbilityFactory(StriderAbility::new));
        abilityRegistry.register("custom/dio", new AbilityFactory(DioAbility::new));
        abilityRegistry.register("minecraft:enderman", new AbilityFactory(EndermanAbility::new));
        abilityRegistry.register("minecraft:evoker", new AbilityFactory(EvokerAbility::new));
        abilityRegistry.register("minecraft:ghast", new AbilityFactory(GhastAbility::new));
        abilityRegistry.register("custom/yuyuko", new AbilityFactory(YuyukoAbility::new));
        abilityRegistry.register("custom/reisen", new AbilityFactory(ReisenAbility::new));
    }
}

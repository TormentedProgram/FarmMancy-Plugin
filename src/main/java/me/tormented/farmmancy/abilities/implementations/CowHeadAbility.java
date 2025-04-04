package me.tormented.farmmancy.abilities.implementations;

import com.destroystokyo.paper.profile.PlayerProfile;
import me.tormented.farmmancy.FarmMancer.FarmMancer;
import me.tormented.farmmancy.FarmMancy;
import me.tormented.farmmancy.abilities.Hook;
import me.tormented.farmmancy.abilities.MobunitionAbility;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.profile.PlayerTextures;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.UUID;

public class CowHeadAbility extends MobunitionAbility<ItemDisplay> implements Hook.PlayerInteraction {

    @Override
    public Class<ItemDisplay> getEntityClass() {
        return ItemDisplay.class;
    }

    public CowHeadAbility(@NotNull UUID id, @NotNull UUID owner) {
        super(id, owner);
        modRingRadius = 3f;
    }

    public static String texture = "http://textures.minecraft.net/texture/2ee55241bd84c77790da1407990de74f11adedc7292986ae7e2faaba6d6c29d4";

    public static PlayerProfile getProfile(String texture) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) head.getItemMeta();

        PlayerProfile profile = Bukkit.createProfile(UUID.fromString(texture)); // Get a new player profile
        PlayerTextures textures = profile.getTextures();
        URL urlObject;
        try {
            urlObject = URL.of(new URI(texture), null);
        } catch (MalformedURLException | URISyntaxException exception) {
            throw new RuntimeException("Invalid URL", exception);
        }
        textures.setSkin(urlObject); // Set the skin of the player profile to the URL
        profile.setTextures(textures); // Set the textures back to the profile

        return profile;
    }

    @Override
    public void processPlayerInteract(PlayerInteractEvent event) {
        ItemStack heldItem = event.getPlayer().getInventory().getItemInMainHand();
        if (heldItem.getItemMeta().getPersistentDataContainer().has(FarmMancer.magic_hoe_key, PersistentDataType.BYTE)) {
            Player player = event.getPlayer();

            if (player != getOwnerPlayer()) return;

            if (entities.isEmpty()) {
                return;
            }

            ItemDisplay flingingCow = entities.getLast();

            entities.remove(flingingCow);

            Location loc = player.getLocation();
            Vector direction = loc.getDirection();
            Location targetLoc = loc.add(direction.multiply(modRingRadius));

            flingingCow.setMetadata("FarmMancy_Projectile", new FixedMetadataValue(FarmMancy.getInstance(), this));
            flingingCow.teleport(targetLoc);

            Vector velocity = direction.multiply(1.0);

            flingingCow.setVelocity(velocity);
        }
    }
}

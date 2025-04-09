package me.tormented.farmmancy.utils;

import com.destroystokyo.paper.profile.PlayerProfile;
import me.tormented.farmmancy.FarmMancy;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerTextures;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Base64;
import java.util.UUID;

public record HeadProvider(@NotNull String textureUrl) {

    public static @NotNull PlayerProfile getProfile(@NotNull String texture)  {
        if (isBase64(texture)) {
            byte[] decodedBytes = Base64.getDecoder().decode(texture);
            String jsonString = new String(decodedBytes);
            JSONParser parser = new JSONParser();
            try {
                JSONObject jsonObject = (JSONObject) parser.parse(jsonString);
                JSONObject textures = (JSONObject) jsonObject.get("textures");
                JSONObject skin = (JSONObject) textures.get("SKIN");
                texture = (String) skin.get("url");
            } catch (ParseException error) {
                FarmMancy.getInstance().getLogger().warning("Failed to parse base64 texture: " + error.getMessage());
            }
        }

        PlayerProfile profile = Bukkit.createProfile(UUID.randomUUID()); // Get a new player profile
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

    public @NotNull ItemStack getHeadItem() {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);

        SkullMeta meta = (SkullMeta) head.getItemMeta();
        meta.setPlayerProfile(getProfile(textureUrl)); // Set the owning player of the head to the player profile
        head.setItemMeta(meta);

        return head;
    }

    public static boolean isBase64(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        if (str.length() % 4 != 0) {
            return false;
        }
        return str.matches("^[A-Za-z0-9+/]*={0,2}$");
    }
}

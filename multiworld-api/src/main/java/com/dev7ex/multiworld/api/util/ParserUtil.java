package com.dev7ex.multiworld.api.util;

import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;

public class ParserUtil {

    public static TextColor parseTextColor(String hex) {
        if (hex == null) return TextColor.color(255, 255, 255);
        try {
            return TextColor.fromHexString(hex);
        } catch (IllegalArgumentException ex) {
            return TextColor.color(255, 255, 255);
        }
    }

    public static Material parseMaterial(String name) {
        if (name == null) return Material.STONE;
        try {
            return Material.valueOf(name.toUpperCase());
        } catch (IllegalArgumentException ex) {
            return Material.STONE;
        }
    }

}

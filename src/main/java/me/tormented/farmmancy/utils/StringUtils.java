package me.tormented.farmmancy.utils;

public class StringUtils {
    public static String makeHumanReadable(String input) {
        if (input == null || input.isEmpty()) return input;
        return Character.toUpperCase(input.charAt(0)) + input.substring(1).toLowerCase();
    }

    public static String makeIDHumanReadable(String id) {
        if (id == null || id.isEmpty()) return id;
        String[] words = id.split("_");
        StringBuilder formattedNameBuilder = new StringBuilder();
        for (String word : words) {
            if (!word.isEmpty()) {
                word = makeHumanReadable(word);
                formattedNameBuilder.append(word)
                        .append(" ");
            }
        }

        return formattedNameBuilder.toString().trim();
    }
}

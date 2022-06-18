/*
 *     TaggerNationLib - Common utility library for our products.
 *     Copyright (C) 2022  TaggerNation
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.taggernation.taggernationlib.placeholder;

import org.bukkit.entity.Player;
import me.clip.placeholderapi.PlaceholderAPI;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class Placeholder {

    /**
     * Replace placeholders in a String
     * @param text The text to replace
     * @param player The player to replace text for
     * @return The replaced text
     */
    public String replace(String text, Player player) {
        return PlaceholderAPI.setPlaceholders(player, text);
    }

    /**
     * Replace placeholders in a list of Strings
     * @param text The text list to replace
     * @param player The player to replace text for
     * @return The replaced text list
     */
    public List<String> replace(List<String> text, Player player) {
        List<String> newText = new ArrayList<>();
        for (String s : text) {
            newText.add(replace(s, player));
        }
        return newText;
    }
}

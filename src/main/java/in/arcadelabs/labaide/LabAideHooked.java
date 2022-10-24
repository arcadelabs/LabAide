/*
 *                LabAide - Common utility library for our products.
 *                Copyright (C) 2022  ArcadeLabs Production.
 *
 *       This program is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU General Public License as published by
 *       the Free Software Foundation, either version 3 of the License, or
 *       (at your option) any later version.
 *
 *       This program is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU General Public License for more details.
 *
 *       You should have received a copy of the GNU General Public License
 *       along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package in.arcadelabs.labaide;

import in.arcadelabs.labaide.logger.Logger;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class LabAideHooked {

  private final List<Plugin> dependants = new ArrayList<>();

  public LabAideHooked(final Logger logger) {

    final Plugin[] serverPlugins = Bukkit.getPluginManager().getPlugins();
    for (final Plugin plugin : serverPlugins) {
      if (plugin.isEnabled()) {
        if (plugin.getDescription().getDepend().contains("LabAide")
                || plugin.getDescription().getSoftDepend().contains("LabAide")) dependants.add(plugin);
      }
    }
    if (!dependants.isEmpty()) {
      final String pluralOrNot = dependants.size() > 1 ?
              "Hooked into " + dependants.size() + " plugins.\n Dependants: "
                      + getPluginList() :
              "Hooked into " + dependants.get(0);
      logger.log(Logger.Level.INFO, MiniMessage.miniMessage().deserialize(
              "<b><gradient:#e01e37:#f58c67>" + pluralOrNot + "</gradient></b>"));
    } else logger.log(Logger.Level.INFO, MiniMessage.miniMessage().deserialize(
            "<b><gradient:#e01e37:#f58c67>No <st>one</st> plugins wanna hook with me," +
                    " guess you and me are same afterall.</gradient></b>"));
  }

  private String getPluginList() {
    StringBuilder pluginList = new StringBuilder();
    Plugin[] plugins = Bukkit.getPluginManager().getPlugins();

    for (Plugin plugin : plugins) {
      if (pluginList.length() > 0) {
        pluginList.append("<color:#007f5f>");
        pluginList.append(", ");
      }
      if (plugin.isEnabled()) {
        if (plugin.getDescription().getDepend().contains("LabAide")
                || plugin.getDescription().getSoftDepend().contains("LabAide")) dependants.add(plugin);
      }
    }
    return pluginList.toString();
  }
}

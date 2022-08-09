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
import in.arcadelabs.labaide.metrics.BStats;
import lombok.Getter;
import lombok.SneakyThrows;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

@Getter
public class LabAide extends JavaPlugin {

  @Getter
  private static LabAide instance;
  private static Logger logger;
  private final LegacyComponentSerializer legecySerializer = LegacyComponentSerializer.builder().hexColors().useUnusualXRepeatedCharacterHexFormat().build();
  private final MiniMessage miniMessage = MiniMessage.builder().build();
  private final List<Plugin> dependants = new ArrayList<>();
  private BStats metrics;

  public static Logger Logger() {
    return logger;
  }

  @SneakyThrows
  @Override
  public void onEnable() {
    metrics = new BStats(this, 16060);
    instance = this;
    logger = new Logger("LabAide",
            MiniMessage.miniMessage().deserialize(
                    "<b><color:#f58066>⌬</color></b>  "),
            null, null);
    logger.log(Logger.Level.INFO, MiniMessage.miniMessage().deserialize(
            "<b><gradient:#e01e37:#f58c67>" +
                    "LabAide </gradient><color:#f89999><gradient:#f58c67:#f10f5d>up and functional!" +
                    "</gradient></b>"));

    final List<Plugin> serverPlugins = List.of(Bukkit.getPluginManager().getPlugins());
    for (final Plugin plugin : serverPlugins) {
      if (plugin.getDescription().getDepend().contains("LabAide")
              || plugin.getDescription().getSoftDepend().contains("LabAide")) dependants.add(plugin);
    }
    final String pluralOrNot = dependants.size() > 1 ?
            "Hooked into " + dependants.size() + " plugins.\n Dependants: "
                    + dependants.toString().substring(1, dependants.toString().length() - 1) :
            "Hooked into " + dependants.get(0);
    logger.log(Logger.Level.INFO, MiniMessage.miniMessage().deserialize(
            "<b><gradient:#e01e37:#f58c67>" + pluralOrNot + "</gradient></b>"));
  }

  @Override
  public void onDisable() {
    logger.log(Logger.Level.INFO, MiniMessage.miniMessage().deserialize(
            "<b><gradient:#f58c67:#f10f5d>Adios...</gradient></b>"));
  }
}
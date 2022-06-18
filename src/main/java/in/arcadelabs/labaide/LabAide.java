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

import io.github.alenalex.adventurelib.spigot.impl.SpigotMessenger;
import lombok.Getter;
import lombok.SneakyThrows;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class LabAide extends JavaPlugin {

  @Getter
  private static MiniMessage miniMessage;
  @Getter
  private static SpigotMessenger messenger;

  @SneakyThrows
  @Override
  public void onEnable() {
    miniMessage = MiniMessage.builder().build();
    messenger = SpigotMessenger
            .builder()
            .setPlugin(this)
            .defaultToMiniMessageTranslator()
            .build();
  }

  @Override
  public void onDisable() {
    // Plugin shutdown logic
  }
}

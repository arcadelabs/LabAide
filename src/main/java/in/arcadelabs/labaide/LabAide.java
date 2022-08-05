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
import lombok.Getter;
import lombok.SneakyThrows;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class LabAide extends JavaPlugin {

  @Getter
  private static LabAide instance;
  private static Logger logger;

  @SneakyThrows
  @Override
  public void onEnable() {
    logger = new Logger("❥",
            MiniMessage.miniMessage().deserialize(
                    "<gradient:#f10f5d:#f58c67><b><color:#f89999>『</color>ArcadeLabs<color:#f89999>』"),
            null, null);
    logger.logger(Logger.Level.INFO, MiniMessage.miniMessage().deserialize("<green><b>LabAide up and functional!"));
  }

  public static Logger Logger() {
    return logger;
  }

  @Override
  public void onDisable() {
  }
}
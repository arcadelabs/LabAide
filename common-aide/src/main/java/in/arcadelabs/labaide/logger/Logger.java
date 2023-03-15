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

package in.arcadelabs.labaide.logger;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.entity.Player;

public class Logger {

  private final ComponentLogger logger;
  private final MiniMessage miniMessage;
  private final Component prefix;
  private final String allPrefix, playerPrefix;

  /**
   * Instantiates a new Logger.
   *
   * @param name         the name
   * @param prefix       the prefix
   * @param allPrefix    the all prefix
   * @param playerPrefix the player prefix
   */
  public Logger(final String name, final Component prefix, final String allPrefix, final String playerPrefix) {
    this.logger = ComponentLogger.logger(name);
    this.miniMessage = MiniMessage.miniMessage();
    this.prefix = prefix;
    this.allPrefix = allPrefix;
    this.playerPrefix = playerPrefix;
  }

  /**
   * Logger.
   *
   * @param level   the level
   * @param message the message
   */
  public void log(final Level level, final Component message) {
    switch (level) {
      case INFO -> logger.info(prefix.append(message));
      case WARN -> logger.warn(prefix.append(message));
      case ERROR -> logger.error(prefix.append(message));
      case DEBUG -> logger.debug(prefix.append(message));
    }
  }

  /**
   * Logger with stackrace.
   *
   * @param level     the level
   * @param message   the message
   * @param stackrace the stackrace
   */
  public void log(final Level level, final Component message, final Throwable stackrace) {
    switch (level) {
      case INFO -> logger.info(prefix.append(message), stackrace);
      case WARN -> logger.warn(prefix.append(message), stackrace);
      case ERROR -> logger.error(prefix.append(message), stackrace);
      case DEBUG -> logger.debug(prefix.append(message), stackrace);
    }
  }

  /**
   * Logger with player placeholder.
   *
   * @param player  the player
   * @param level   the level
   * @param message the message
   */
  public void log(final Player player, final Level level, final Component message) {
    if (player == null) this.log(level, miniMessage.deserialize(allPrefix).append(message));
    else this.log(level, miniMessage.deserialize(playerPrefix, Placeholder.component("player", player.name()))
            .append(message));
  }

  /**
   * The enum Level.
   */
  public enum Level {
    INFO,
    WARN,
    ERROR,
    DEBUG
  }
}

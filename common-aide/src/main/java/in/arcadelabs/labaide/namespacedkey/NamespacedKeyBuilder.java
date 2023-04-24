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

package in.arcadelabs.labaide.namespacedkey;

import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Builder util for {@link NamespacedKey}s.
 */
public final class NamespacedKeyBuilder {
  private final String keyPrefix;
  private final JavaPlugin plugin;

  /**
   * Instantiates a new Namespaced key builder.
   *
   * @param keyPrefix the key prefix
   * @param plugin    the plugin
   */
  public NamespacedKeyBuilder(final String keyPrefix, final JavaPlugin plugin) {
    this.keyPrefix = keyPrefix.toLowerCase();
    this.plugin = plugin;
  }

  /**
   * Instantiates a new Namespaced key builder.
   *
   * @param keyPrefix the key prefix
   */
  public NamespacedKeyBuilder(final String keyPrefix) {
    this.keyPrefix = keyPrefix.toLowerCase();
    this.plugin = null;
  }

  /**
   * Gets new key.
   *
   * @param key the key
   * @return the new key
   */
  public NamespacedKey getNewKey(final String key) {
    if (this.plugin == null) return new NamespacedKey(this.keyPrefix, this.keyPrefix + "_" + key.toLowerCase());
    else return new NamespacedKey(this.plugin, this.keyPrefix + "_" + key.toLowerCase());
  }
}

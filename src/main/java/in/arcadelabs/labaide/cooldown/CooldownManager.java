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

package in.arcadelabs.labaide.cooldown;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class CooldownManager {
  private final HashMap<UUID, Long> cooldownMap;
  private long cooldownPeriod;

  /**
   * Instantiates a new Cooldown manager.
   *
   * @param seconds the seconds
   */
  public CooldownManager(final int seconds) {
    this.cooldownMap = new HashMap<>();
    this.cooldownPeriod = seconds * 1000L;
  }

  /**
   * On cooldown check.
   *
   * @param uuid the uuid
   * @return the boolean
   */
  public boolean isOnCooldown(final UUID uuid) {
    if (!this.cooldownMap.containsKey(uuid)) return false;
    return this.getRemainingTime(uuid) > 0;
  }

  /**
   * Sets uuid on cooldown.
   *
   * @param uuid the uuid
   */
  public void setCooldown(final UUID uuid) {
    this.cooldownMap.put(uuid, System.currentTimeMillis() + this.cooldownPeriod);
  }

  /**
   * Remove uuid's cooldown.
   *
   * @param uuid the uuid
   */
  public void removeCooldown(final UUID uuid) {
    if (!this.cooldownMap.containsKey(uuid)) return;
    this.cooldownMap.remove(uuid);
  }

  /**
   * Gets remaining time.
   *
   * @param uuid the uuid
   * @return the remaining time
   */
  public long getRemainingTime(final UUID uuid) {
    if (!this.cooldownMap.containsKey(uuid)) return 0;
    long difference = this.cooldownMap.get(uuid) - System.currentTimeMillis();
    if (difference <= 0) return 0;
    return TimeUnit.MILLISECONDS.toSeconds(difference);
  }

  /**
   * Sets cooldown time.
   *
   * @param seconds the cooldown time
   */
  public void setCooldownPeriod(final int seconds) {
    this.cooldownPeriod = seconds * 1000L;
  }

  /**
   * Gets cooldown map.
   *
   * @return the cooldown map
   */
  public HashMap<UUID, Long> getCooldownMap() {
    return this.cooldownMap;
  }

  /**
   * Clear cooldowns.
   */
  public void clearCooldowns() {
    this.cooldownMap.clear();
  }
}
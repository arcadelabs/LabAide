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

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class CooldownManager<T> {
  private final Map<T, Long> cooldownMap;
  private final long cooldownPeriod;

  public CooldownManager(long cooldownPeriod, TimeUnit unit) {
    this.cooldownMap = new HashMap<>();
    this.cooldownPeriod = unit.toMillis(cooldownPeriod);
  }

  public boolean isOnCooldown(@NotNull T key) {
    return this.cooldownMap.containsKey(key) && this.cooldownMap.get(key) > System.currentTimeMillis();
  }

  public void setCooldown(@NotNull T key) {
    this.cooldownMap.put(key, System.currentTimeMillis() + this.cooldownPeriod);
  }

  public void removeCooldown(@NotNull T key) {
    this.cooldownMap.remove(key);
  }

  public long getRemainingTime(@NotNull T key, @NotNull TimeUnit unit) {
    long remaining = this.cooldownMap.containsKey(key) ? this.cooldownMap.get(key) - System.currentTimeMillis() : 0;
    return unit.convert(Math.max(remaining, 0), TimeUnit.MILLISECONDS);
  }

  public Map<T, Long> getCooldownMap() {
    return cooldownMap;
  }

  public long getCooldownPeriod() {
    return cooldownPeriod;
  }

  public void clearCooldowns() {
    this.cooldownMap.clear();
  }
}

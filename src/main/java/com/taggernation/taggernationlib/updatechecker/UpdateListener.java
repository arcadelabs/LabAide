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

package com.taggernation.taggernationlib.updatechecker;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import static com.taggernation.taggernationlib.TaggerNationLib.messenger;

public class UpdateListener implements Listener {

  private final UpdateChecker instance;

  public UpdateListener(UpdateChecker instance) {
    this.instance = instance;
  }

  @EventHandler
  public void playerJoinNotification(PlayerJoinEvent event) {
    Player player = event.getPlayer();
    if ((player.isOp() && instance.isOpNotificationEnabled()) ||
            (instance.getNotificationPermission() != null &&
                    player.hasPermission(instance.getNotificationPermission()))) {
      for (final String message : instance.getMessage()) {
        messenger.sendMessage(player, message);
      }
    }
  }
}

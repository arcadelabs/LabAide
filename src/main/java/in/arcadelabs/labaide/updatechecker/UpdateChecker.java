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

package in.arcadelabs.labaide.updatechecker;

import com.google.gson.Gson;
import in.arcadelabs.labaide.LabAide;
import in.arcadelabs.labaide.updatechecker.downloads.DownloadManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*
 * Json update checker
 *
 * To-Do DownloadManager
 */
@SuppressWarnings("unused")
public class UpdateChecker {
  private final Plugin plugin;
  private final UpdateChecker instance;
  private final URL url;
  Gson gson = new Gson();
  private int interval;
  private Update update;
  private List<String> message = new ArrayList<>();
  private String permission = null;
  private boolean opNotify = false;
  private DownloadManager downloadManager = null;
  private InputStreamReader reader;

  /**
   * Initialize the update checker for the plugin.
   *
   * @param plugin   The plugin to check for an update for
   * @param url      Json file URL to check for an update
   * @param interval The interval to check for an update in ticks
   */
  public UpdateChecker(Plugin plugin, URL url, int interval) throws IOException {
    this.plugin = plugin;
    this.interval = interval;
    this.url = url;
    this.instance = this;
    reader = new InputStreamReader(url.openStream());
    plugin.getServer().getPluginManager().registerEvents(new UpdateListener(this), plugin);
    this.update = gson.fromJson(reader, Update.class);
  }

  public void setUpdate(Update update) {
    this.update = update;
  }

  private void setReader(InputStreamReader reader) {
    this.reader = reader;
  }

  private void processMessage() {
    List<String> formatter = new ArrayList<>();
    for (String list : update.message) {
      if (list.contains("{pluginName}")) {
        list = list.replace("{pluginName}", plugin.getName());
      }
      if (list.contains("{pluginVersion}")) {
        list = list.replace("{pluginVersion}", plugin.getDescription().getVersion());
      }
      if (list.contains("{updateLink}")) {
        list = list.replace("{updateLink}", update.updateLink);
      }
      if (list.contains("{latestVersion}")) {
        list = list.replace("{latestVersion}", update.version);
      }
      if (list.contains("{changeLog}")) {
        list = list.replace("{changeLog}", String.join("\n", update.changeLog));
      }
      if (list.contains("{type}")) {
        list = list.replace("{type}", getUpdateType());
      }
      formatter.add(list);
    }
    if (Double.parseDouble(update.version) <= Double.parseDouble(plugin.getDescription().getVersion())) {
      message = Collections.singletonList("No updates found for " + plugin.getName() + ".");
    } else {
      message = formatter;
    }
  }

  private String getUpdateType() {
    if (update.hotFix) {
      return "Hotfix";
    } else if (update.bugFix) {
      return "Bugfix";
    } else if (update.newFeature) {
      return "New Feature";
    } else {
      return "Update";
    }

  }

  public List<String> getMessage() {
    return message;
  }

  /**
   * Set Interval to check for an update in ticks.
   *
   * @param interval The interval to check for an update in ticks
   */
  public UpdateChecker setInterval(int interval) {
    this.interval = interval;
    return this;
  }

  /**
   * Check if an update is available
   *
   * @return true if an update is available
   */
  public boolean isUpdateAvailable() {
    return !(Double.parseDouble(update.version) <= Double.parseDouble(plugin.getDescription().getVersion()));
  }

  /**
   * Check for update on players with permission join.
   */
  public UpdateChecker enableOnJoin() {
    plugin.getServer().getPluginManager().registerEvents(new UpdateListener(instance), plugin);
    return this;
  }

  /**
   * Set whether to notify op players without permission.
   *
   * @param opNotify true to notify op players without permission, false to not
   */
  public UpdateChecker enableOpNotification(boolean opNotify) {
    this.opNotify = opNotify;
    return this;
  }

  /**
   * Is Op Notification enabled?
   *
   * @return true if Op Notification is enabled, false if not
   */
  public boolean isOpNotificationEnabled() {
    return opNotify;
  }

  /**
   * Get the permission required to receive a notification.
   *
   * @return Permission required to receive a notification
   */
  public String getNotificationPermission() {
    return permission;
  }

  /**
   * Set the permission required to receive a notification.
   *
   * @param permission Permission required to receive a notification
   */
  public UpdateChecker setNotificationPermission(String permission) {
    this.permission = permission;
    return this;
  }

  /**
   * Set up the update checker.
   */
  public void setup() {
    checkForUpdate();
  }

  /**
   * If provided when an update is available. latest update will download automatically.
   *
   * @param directDownloadLink DirectLink to the latest file
   */
  public UpdateChecker downloadLatestUpdate(URL directDownloadLink) {
    this.downloadManager = new DownloadManager(directDownloadLink, plugin);
    return this;
  }

  /**
   * Set file to overwrite the existing one. This option is set to true by default
   *
   * @param overwrite true to overwrite the default file, false to not
   */
  public UpdateChecker setOverwrite(boolean overwrite) {
    this.downloadManager.setOverride(overwrite);
    return this;
  }

  /**
   * Check for an update.
   */
  private void checkForUpdate() {
    new BukkitRunnable() {
      @Override
      public void run() {
        processMessage();
        for (Player player : Bukkit.getOnlinePlayers()) {
          if (Bukkit.getOnlinePlayers().size() > 0 && player.hasPermission("greetings.update")) {
            for (final String message : instance.getMessage()) {
              LabAide.getMessenger().sendMessage(player, message);
            }
          }
        }
        for (final String message : instance.getMessage()) {
          Bukkit.getLogger().info(message);
        }
        try {
          reader = new InputStreamReader(url.openStream());
          setReader(reader);
        } catch (IOException e) {
          e.printStackTrace();
        }
        update = gson.fromJson(reader, Update.class);
        setUpdate(update);
        try {
          downloadManager.initialize();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }.runTaskTimerAsynchronously(plugin, 250, interval);
  }
}

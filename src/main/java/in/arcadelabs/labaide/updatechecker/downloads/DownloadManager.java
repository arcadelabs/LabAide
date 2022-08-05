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

package in.arcadelabs.labaide.updatechecker.downloads;

import org.apache.commons.io.FilenameUtils;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;


public class DownloadManager {

  URL downloadUrl;
  boolean override;
  Plugin plugin;
  boolean downloadSuccess;
  File pluginsDirectory;

  public DownloadManager(URL downloadUrl, Plugin plugin) {
    this.downloadUrl = downloadUrl;
    this.plugin = plugin;
    this.override = true;
    this.downloadSuccess = false;
    this.pluginsDirectory = new File("." + File.separatorChar + "plugins");
  }

  public boolean getOverride() {
    return override;
  }

  public void setOverride(boolean override) {
    this.override = override;
  }

  public String getDownloadUrl() {
    return downloadUrl.toString();
  }

  public void setDownloadUrl(URL downloadUrl) {
    this.downloadUrl = downloadUrl;
  }

  public void initialize() throws IOException {
    if (downloadUrl == null) {
      throw new IllegalArgumentException("Download URL cannot be null");
    }
    File outputFile = new File(pluginsDirectory, FilenameUtils.getName(downloadUrl.toString()));

    if (downloadSuccess) return;
    plugin.getLogger().info("Downloading Latest Version of " + plugin.getName() + "...");

    ReadableByteChannel byteChannel = Channels.newChannel(downloadUrl.openStream());
    FileOutputStream outputStream = new FileOutputStream(outputFile);
    outputStream.getChannel().transferFrom(byteChannel, 0, Long.MAX_VALUE);
    byteChannel.close();
    outputStream.close();

    plugin.getLogger().info("Downloaded Latest Version of " + plugin.getName() + "!. Restart to apply changes.");
    this.downloadSuccess = true;
  }
}
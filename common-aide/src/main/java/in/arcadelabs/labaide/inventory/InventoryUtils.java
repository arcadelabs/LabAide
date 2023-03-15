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

package in.arcadelabs.labaide.inventory;

import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;

@SuppressWarnings("unused")
@UtilityClass
public class InventoryUtils {

  /**
   * Converts an array of ItemStacks to a Base64 encoded string representation.
   *
   * @param itemStacks the array of ItemStacks to convert
   * @return a Base64 encoded string representation of the ItemStack array
   * @throws IllegalArgumentException if the ItemStack array is null
   * @throws IllegalStateException    if there is an error converting the ItemStack array to a Base64 string
   */
  public static String itemStackArrayToBase64(@NotNull final ItemStack[] itemStacks) throws IllegalArgumentException, IllegalStateException {
    if (itemStacks == null) {
      throw new IllegalArgumentException("ItemStack array cannot be null.");
    }

    try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
         BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream)) {

      dataOutput.writeInt(itemStacks.length);

      for (ItemStack itemStack : itemStacks) {
        dataOutput.writeObject(itemStack);
      }

      byte[] bytes = outputStream.toByteArray();
      return Base64.getEncoder().encodeToString(bytes);

    } catch (IOException e) {
      throw new IllegalStateException("Unable to save items stacks.", e);
    }
  }

  /**
   * Converts an Inventory object to a Base64 encoded string representation.
   *
   * @param inventory the Inventory object to convert
   * @return a Base64 encoded string representation of the inventory
   * @throws IllegalArgumentException if the inventory is null
   * @throws IllegalStateException    if there is an error converting the inventory to a Base64 string
   */
  public static String inventoryToBase64(@NotNull final Inventory inventory) throws IllegalArgumentException, IllegalStateException {

    try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
         BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream)) {

      dataOutput.writeInt(inventory.getSize());

      for (int i = 0; i < inventory.getSize(); i++) {
        dataOutput.writeObject(inventory.getItem(i));
      }

      byte[] bytes = outputStream.toByteArray();
      return Base64.getEncoder().encodeToString(bytes);

    } catch (IOException e) {
      throw new IllegalStateException("Unable to save inventory.", e);
    }
  }

  /**
   * Converts a Base64 encoded string representation of an inventory to an Inventory object.
   *
   * @param data the Base64 encoded string representation of an inventory
   * @return an Inventory object representing the decoded inventory data
   * @throws IllegalArgumentException if the data string is null
   * @throws IOException              if there is an error decoding the inventory data
   */
  public static Inventory inventoryFromBase64(@NotNull final String data) throws IllegalArgumentException, IOException {

    try (ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64.getDecoder().decode(data));
         BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream)) {

      Inventory inventory = Bukkit.getServer().createInventory(null, dataInput.readInt());

      for (int i = 0; i < inventory.getSize(); i++) {
        inventory.setItem(i, (ItemStack) dataInput.readObject());
      }

      return inventory;

    } catch (ClassNotFoundException e) {
      throw new IOException("Unable to decode class type.", e);
    }
  }

  /**
   * Converts a Base64 encoded string representation of an array of ItemStacks to an ItemStack array.
   *
   * @param data the Base64 encoded string representation of an array of ItemStacks
   * @return an ItemStack array representing the decoded data
   * @throws IllegalArgumentException if the data string is null
   * @throws IOException              if there is an error decoding the data
   */
  public static ItemStack[] itemStackArrayFromBase64(@NotNull final String data) throws IllegalArgumentException, IOException {

    try (ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64.getDecoder().decode(data));
         BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream)) {

      ItemStack[] items = new ItemStack[dataInput.readInt()];

      for (int i = 0; i < items.length; i++) {
        items[i] = (ItemStack) dataInput.readObject();
      }

      return items;

    } catch (ClassNotFoundException e) {
      throw new IOException("Unable to decode class type.", e);
    }
  }

  /**
   * Saves the player's inventory to their persistent data container and clears their inventory.
   *
   * @param player The player whose inventory is to be saved.
   */
  public static void saveInventory(@NotNull final Player player, @NotNull final NamespacedKey inventoryKey) {
    PlayerInventory inventory = player.getInventory();
    if (inventory.isEmpty()) {
      return;
    }

    String encodedInventory = inventoryToBase64(inventory);
    player.getPersistentDataContainer().set(inventoryKey, PersistentDataType.STRING, encodedInventory);
    inventory.clear();
  }

  /**
   * Load the player's inventory from the persistent data container.
   *
   * @param player The player whose inventory should be loaded.
   */
  public static void loadInventory(@NotNull final Player player, @NotNull final NamespacedKey inventoryKey) throws IOException {
    PlayerInventory inventory = player.getInventory();
    inventory.clear();

    String inventoryData = player.getPersistentDataContainer().get(inventoryKey, PersistentDataType.STRING);

    if (inventoryData == null) {
      return;
    }

    ItemStack[] items = itemStackArrayFromBase64(inventoryData);

    for (int i = 0; i < items.length; i++) {
      inventory.setItem(i, items[i]);
    }

    player.getPersistentDataContainer().remove(inventoryKey);
  }

  /**
   * Gives an ItemStack to a player's inventory, or drops it on the ground if their inventory is full.
   *
   * @param player the player who will receive the item
   * @param items  the ItemStack to give to the player
   */
  public static void giveItemToInventoryOrDrop(@NotNull final Player player, @NotNull final ItemStack... items) {
    HashMap<Integer, ItemStack> leftovers = player.getInventory().addItem(items);
    leftovers.values().forEach(itemstack -> player.getWorld().dropItemNaturally(player.getLocation(), itemstack));
  }
}

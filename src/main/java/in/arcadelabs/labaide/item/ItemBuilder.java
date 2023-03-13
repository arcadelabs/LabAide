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

package in.arcadelabs.labaide.item;

import com.destroystokyo.paper.Namespaced;
import com.google.common.collect.Multimap;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
public class ItemBuilder {
  private final ItemStack itemStack;
  private final ItemMeta itemMeta;

  /**
   * Instantiates a new Item builder.
   *
   * @param material the material
   */
  public ItemBuilder(@NotNull final Material material) {
    this.itemStack = new ItemStack(material);
    this.itemMeta = this.itemStack.getItemMeta();
  }

  /**
   * Instantiates a new Item builder with head texture.
   *
   * @param material   the material
   * @param textureMap the texture map
   */
  public ItemBuilder(@NotNull final Material material, @NotNull final Map<String, Object> textureMap) {
    this.itemStack = new ItemStack(material);
    this.itemMeta = (ItemMeta) ConfigurationSerialization.deserializeObject(textureMap);
  }

  /**
   * Sets name.
   *
   * @param name the name
   * @return the name
   */
  public ItemBuilder setName(@NotNull final Component name) {
    this.itemMeta.displayName(name);
    return this;
  }

  /**
   * Sets lore.
   *
   * @param loreList the lore list
   * @return the lore
   */
  public ItemBuilder setLore(@NotNull final List<Component> loreList) {
    this.itemMeta.lore(loreList);
    return this;
  }

  /**
   * Sets model data.
   *
   * @param modelData the model data
   * @return the model data
   */
  public ItemBuilder setModelData(final int modelData) {
    this.itemMeta.setCustomModelData(modelData);
    return this;
  }

  /**
   * Sets unbreakable.
   *
   * @param unbreakable the unbreakable
   * @return the unbreakable
   */
  public ItemBuilder setUnbreakable(final boolean unbreakable) {
    this.itemMeta.setUnbreakable(unbreakable);
    return this;
  }

  /**
   * Sets pdc object.
   *
   * @param <T>           the type parameter
   * @param <Z>           the type parameter
   * @param namespacedKey the namespaced key
   * @param type          the type
   * @param value         the value
   * @return the pdc object
   */
  public <T, Z> ItemBuilder setPDCObject(@NotNull final NamespacedKey namespacedKey, @NotNull final PersistentDataType<T, Z> type, @NotNull final Z value) {
    this.itemMeta.getPersistentDataContainer().set(namespacedKey, type, value);
    return this;
  }

  /**
   * Add enchantment item builder.
   *
   * @param enchantment            the enchantment
   * @param level                  the level
   * @param ignoreLevelRestriction the ignore level restriction
   * @return the item builder
   */
  public ItemBuilder addEnchantment(@NotNull final Enchantment enchantment, final int level, final boolean ignoreLevelRestriction) {
    this.itemMeta.addEnchant(enchantment, level, ignoreLevelRestriction);
    return this;
  }

  /**
   * Add item flags item builder.
   *
   * @param itemFlags the item flags
   * @return the item builder
   */
  public ItemBuilder addItemFlags(@NotNull final ItemFlag... itemFlags) {
    this.itemMeta.addItemFlags(itemFlags);
    return this;
  }

  /**
   * Add attribute modifier item builder.
   *
   * @param attribute the attribute
   * @param modifier  the modifier
   * @return the item builder
   */
  public ItemBuilder addAttributeModifier(@NotNull final Attribute attribute, @NotNull final AttributeModifier modifier) {
    this.itemMeta.addAttributeModifier(attribute, modifier);
    return this;
  }

  /**
   * Sets attribute modifiers.
   *
   * @param attributeModifiers the attribute modifiers
   * @return the attribute modifiers
   */
  public ItemBuilder setAttributeModifiers(@NotNull final Multimap<Attribute, AttributeModifier> attributeModifiers) {
    this.itemMeta.setAttributeModifiers(attributeModifiers);
    return this;
  }

  /**
   * Sets destroyable keys.
   *
   * @param canDestroy the can destroy
   * @return the destroyable keys
   */
  public ItemBuilder setDestroyableKeys(@NotNull final Collection<Namespaced> canDestroy) {
    this.itemMeta.setDestroyableKeys(canDestroy);
    return this;
  }

  /**
   * Sets placeable keys.
   *
   * @param canPlaceOn the can place on
   * @return the placeable keys
   */
  public ItemBuilder setPlaceableKeys(@NotNull final Collection<com.destroystokyo.paper.Namespaced> canPlaceOn) {
    this.itemMeta.setPlaceableKeys(canPlaceOn);
    return this;
  }

  /**
   * Build item builder.
   *
   * @return the item builder
   */
  public ItemBuilder build() {
    this.itemStack.setItemMeta(this.itemMeta);
    return this;
  }

  /**
   * Gets built item.
   *
   * @return the built item
   */
  public ItemStack getBuiltItem() {
    return this.itemStack;
  }
}

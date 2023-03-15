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

package in.arcadelabs.labaide.recipe.impl;

import in.arcadelabs.labaide.recipe.RecipeBuilder;
import io.papermc.paper.potion.PotionMix;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.plugin.java.JavaPlugin;

public class PotionMixBuilder implements RecipeBuilder {

  private final JavaPlugin instance;
  private NamespacedKey key;
  private ItemStack result;
  private RecipeChoice input, ingredient;
  private PotionMix potionMix;

  public PotionMixBuilder(JavaPlugin instance) {
    this.instance = instance;
  }

  @Override
  public RecipeBuilder setResult(ItemStack result) {
    this.result = result;
    return this;
  }

  @Override
  public RecipeBuilder setKey(NamespacedKey key) {
    this.key = key;
    return this;
  }

  public RecipeBuilder setIngredients(RecipeChoice input, RecipeChoice ingredient) {
    this.input = input;
    this.ingredient = ingredient;
    return this;
  }

  @Override
  public RecipeBuilder build() {
    this.potionMix = new PotionMix(this.key, this.result, this.input, this.ingredient);
    return this;
  }

  @Override
  public Recipe getRecipe() {
    return null;
  }

  public PotionMix getPotionMix() {
    return this.potionMix;
  }

  @Override
  public void registerRecipe() {
    this.instance.getServer().getPotionBrewer().addPotionMix(this.potionMix);
  }
}

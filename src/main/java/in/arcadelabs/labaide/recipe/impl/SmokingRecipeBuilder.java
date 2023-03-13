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
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.SmokingRecipe;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class SmokingRecipeBuilder implements RecipeBuilder {

  private final JavaPlugin instance;
  private ItemStack result;
  private NamespacedKey key;
  private RecipeChoice ingredients;
  private float experience;
  private int cookingTime;
  private Recipe recipe;

  public SmokingRecipeBuilder(JavaPlugin instance) {
    this.instance = instance;
  }

  @Override
  public RecipeBuilder setResult(@NotNull final ItemStack result) {
    this.result = result;
    return this;
  }

  @Override
  public RecipeBuilder setKey(@NotNull final NamespacedKey key) {
    this.key = key;
    return this;
  }

  public RecipeBuilder setIngredients(@NotNull final RecipeChoice ingredients) {
    this.ingredients = ingredients;
    return this;
  }

  public RecipeBuilder setExperience(float experience) {
    this.experience = experience;
    return this;
  }

  public RecipeBuilder setCookingTime(int cookingTime) {
    this.cookingTime = cookingTime;
    return this;
  }

  @Override
  public RecipeBuilder build() {
    this.recipe = new SmokingRecipe(this.key, this.result, this.ingredients, this.experience, this.cookingTime);
    return this;
  }

  @Override
  public @NotNull Recipe getRecipe() {
    return this.recipe;
  }

  @Override
  public void registerRecipe() {
    this.instance.getServer().addRecipe(this.recipe);
  }
}

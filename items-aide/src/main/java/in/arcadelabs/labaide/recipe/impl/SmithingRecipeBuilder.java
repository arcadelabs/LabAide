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
import org.bukkit.inventory.SmithingRecipe;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class SmithingRecipeBuilder implements RecipeBuilder {

  private final JavaPlugin instance;
  private NamespacedKey key;
  private ItemStack result;
  private RecipeChoice base;
  private RecipeChoice addition;
  private boolean copyNbt;
  private Recipe recipe;


  public SmithingRecipeBuilder(JavaPlugin instance) {
    this.instance = instance;
  }

  @Override
  public RecipeBuilder setResult(ItemStack result) {
    this.result = result;
    return null;
  }

  @Override
  public RecipeBuilder setKey(NamespacedKey key) {
    this.key = key;
    return null;
  }

  public RecipeBuilder setBase(RecipeChoice base) {
    this.base = base;
    return this;
  }

  public RecipeBuilder setAddition(RecipeChoice addition) {
    this.addition = addition;
    return this;
  }

  public RecipeBuilder setCopyNbt(boolean copyNbt) {
    this.copyNbt = copyNbt;
    return this;
  }

  @Override
  public RecipeBuilder build() {
    this.recipe = new SmithingRecipe(this.key, this.result, this.base, this.addition, this.copyNbt);
    return null;
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

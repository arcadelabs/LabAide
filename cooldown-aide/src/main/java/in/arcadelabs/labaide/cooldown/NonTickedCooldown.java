package in.arcadelabs.labaide.cooldown;

import in.arcadelabs.labaide.cooldown.abstraction.AbstractCooldown;
import in.arcadelabs.labaide.cooldown.abstraction.CooldownExpiryAction;

import java.util.concurrent.TimeUnit;

public class NonTickedCooldown<T> extends AbstractCooldown<T> {

  private NonTickedCooldown(long defaultExpiryDuration, CooldownExpiryAction<T> expiryAction) {
    super(defaultExpiryDuration, expiryAction);
  }
  //--------------------------------------------------------------------------------

  /**
   * Clears and shutdown the current cooldown provider
   * NOTE: This should be strictly called on ScheduledCooldown, since this will handle the thread shutdown properly
   */
  @Override
  public void flush() {
    this.clear();
  }

  /**
   * Checks whether a given key is in cooldown. Returns false if the key is absent
   *
   * @param key The key to be checked
   * @return Returns true if in cooldown or else false
   */
  @Override
  public boolean isInCooldown(T key) {
    if (!this.cache.containsKey(key))
      return false;

    Long value = this.cache.get(key);
    if (value == null)
      return false;

    if (isInCooldownInternal(value))
      return true;

    this.cache.remove(key);
    super.setExpired(key);
    return false;
  }

  //----------------------------------------------------------------------------
  // BUILDER
  //----------------------------------------------------------------------------
  public static class NonTickedCooldownBuilder<TKey> {
    private long defaultExpiryDuration;
    private TimeUnit timeUnit;
    private CooldownExpiryAction<TKey> expiryAction;

    public NonTickedCooldownBuilder() {
      this.withDefaultSettings();
    }

    /**
     * Sets the default expiry time of the cooldown provider
     *
     * @param expiryTime expiry time
     * @return The builder
     */
    public NonTickedCooldownBuilder<TKey> withDefaultExpiryTime(long expiryTime) {
      this.defaultExpiryDuration = expiryTime;
      return this;
    }

    public NonTickedCooldownBuilder<TKey> withDefaultExpiryTimeUnit(TimeUnit unit) {
      this.timeUnit = unit;
      return this;
    }

    public NonTickedCooldownBuilder<TKey> setOnExpiryAction(CooldownExpiryAction<TKey> action) {
      this.expiryAction = action;
      return this;
    }

    public NonTickedCooldownBuilder<TKey> setNoActionOnExpiry() {
      this.expiryAction = null;
      return this;
    }

    public NonTickedCooldownBuilder<TKey> withDefaultSettings() {
      this.expiryAction = null;
      this.timeUnit = TimeUnit.SECONDS;
      this.defaultExpiryDuration = 1L;
      return this;
    }

    public long getDefaultExpiryDuration() {
      return defaultExpiryDuration;
    }

    public TimeUnit getTimeUnit() {
      return timeUnit;
    }

    public CooldownExpiryAction<TKey> getExpiryAction() {
      return expiryAction;
    }

    public NonTickedCooldown<TKey> build() {
      return new NonTickedCooldown<TKey>(this.timeUnit.toMillis(this.defaultExpiryDuration), expiryAction);
    }
  }
}

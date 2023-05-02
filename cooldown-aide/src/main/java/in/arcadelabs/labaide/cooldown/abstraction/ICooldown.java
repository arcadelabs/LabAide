package in.arcadelabs.labaide.cooldown.abstraction;

import java.util.Map;
import java.util.concurrent.TimeUnit;

public interface ICooldown<T> {

  /**
   * Checks whether a given key is in cooldown. Returns false if the key is absent
   *
   * @param key The key to be checked
   * @return Returns true if in cooldown or else false
   */
  boolean isInCooldown(T key);

  /**
   * Set the given key to cooldown with the default cooldown timer provided
   *
   * @param key The key that has to be set on Cooldown
   */
  void setCooldown(T key);

  /**
   * Set the given key to the cooldown with the specified expiry time
   *
   * @param key            The key that has to be set on Cooldown
   * @param customTime     How long does the key should be in Cooldown
   * @param customTimeUnit {@link TimeUnit} of the customTime
   */
  void setCooldown(T key, long customTime, TimeUnit customTimeUnit);

  /**
   * Remove a key from the cooldown if it exists in the check, else return silently
   *
   * @param key The key that has to be removed
   */
  void removeCooldown(T key);

  /**
   * Gets a copy of all the cooldown that has been in the registry
   *
   * @return Returns a copy of {@link Map<T, Long>} instance of the original cache
   */
  Map<T, Long> getAllCooldown();

  /**
   * Clears all the keys in the cooldown check
   */
  void clear();

  /**
   * Clears and shutdown the current cooldown provider
   * NOTE: This should be strictly called on ScheduledCooldown, since this will handle the thread shutdown properly
   */
  void flush();

  //----------------------------------------------------
  // STATIC METHODS
  // ---------------------------------------------------

}

package in.arcadelabs.labaide.cooldown.abstraction;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public abstract class AbstractCooldown<T> implements ICooldown<T> {

    protected final HashMap<T, Long> cache;
    protected long defaultExpiryDuration;

    protected final CooldownExpiryAction<T> onExpired;

    public AbstractCooldown(long defaultExpiryDuration, CooldownExpiryAction<T> expiryAction) {
        this.cache = new HashMap<>();
        this.defaultExpiryDuration = defaultExpiryDuration;
        this.onExpired = expiryAction;
    }

    public AbstractCooldown(long defaultExpiryDuration) {
        this.defaultExpiryDuration = defaultExpiryDuration;
        this.cache = new HashMap<>();
        this.onExpired = null;
    }

    /**
     * Common implementation of what to do when a key has been expired. Basically will remove from the cache and call
     * onExpired if present
     * @param key The key to be set as Expired
     */
    protected void setExpired(T key){
        if(this.onExpired != null){
            this.onExpired.onKeyExpired(key);
        }
    }

    protected boolean isEmpty(){
        return this.cache.isEmpty();
    }

    /**
     * Gets a copy of all the cooldown that has been in the registry
     *
     * @return Returns a copy of {@link Map<T, Long>} instance of the original cache
     */
    @Override
    public Map<T, Long> getAllCooldown() {
        return new ConcurrentHashMap<>(this.cache);
    }

    /**
     * Clears all the keys in the cooldown check
     */
    @Override
    public void clear() {
        this.cache.clear();
    }

    protected boolean isInCooldownInternal(long value){
        return value > System.currentTimeMillis();
    }

    /**
     * Checks whether a given key is in cooldown. Returns false if the key is absent
     *
     * @param key The key to be checked
     * @return Returns true if in cooldown or else false
     */
    @Override
    public boolean isInCooldown(T key) {
        if(!this.cache.containsKey(key))
            return false;

        Long value = this.cache.get(key);

        return isInCooldownInternal(value);
    }

    /**
     * Set the given key to cooldown with the default cooldown timer provided
     *
     * @param key The key that has to be set on Cooldown
     */
    @Override
    public void setCooldown(T key) {
        this.setCooldown(key, defaultExpiryDuration, TimeUnit.MILLISECONDS);
    }


    /**
     * Set the given key to the cooldown with the specified expiry time
     *
     * @param key            The key that has to be set on Cooldown
     * @param customTime     How long does the key should be in Cooldown
     * @param customTimeUnit {@link TimeUnit} of the customTime
     */
    @Override
    public void setCooldown(T key, long customTime, TimeUnit customTimeUnit) {
        this.cache.put(key, customTimeUnit.toMillis(customTime));
    }

    /**
     * Remove a key from the cooldown if it exists in the check, else return silently
     *
     * @param key The key that has to be removed
     */
    @Override
    public void removeCooldown(T key) {
        this.cache.remove(key);
    }

    public long getDefaultExpiryDuration() {
        return defaultExpiryDuration;
    }
}

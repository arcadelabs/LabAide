package in.arcadelabs.labaide.cooldown;

import in.arcadelabs.labaide.cooldown.abstraction.AbstractCooldown;
import in.arcadelabs.labaide.cooldown.abstraction.CooldownExpiryAction;
import in.arcadelabs.labaide.cooldown.exception.CooldownServiceException;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TickedCooldown<T> extends AbstractCooldown<T> implements Runnable {

  private final long tickTime;
  //--------------------------------------------------------------------------------
  private final TimeUnit timeUnit;
  private final HashSet<T> toBeRemoved = new HashSet<>();
  private final ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

  public TickedCooldown(long defaultExpiryDuration, CooldownExpiryAction<T> expiryAction, long tickTime, TimeUnit timeUnit) {
    super(defaultExpiryDuration, expiryAction);
    this.tickTime = tickTime;
    this.timeUnit = timeUnit;
    this.initThread();
  }

  public TickedCooldown(long defaultExpiryDuration, long tickTime, TimeUnit timeUnit) {
    super(defaultExpiryDuration);
    this.tickTime = tickTime;
    this.timeUnit = timeUnit;
    this.initThread();
  }

  public TickedCooldown(long defaultExpiryDuration, CooldownExpiryAction<T> expiryAction) {
    super(defaultExpiryDuration, expiryAction);
    this.tickTime = 1L;
    this.timeUnit = TimeUnit.SECONDS;
    this.initThread();
  }

  public TickedCooldown(long defaultExpiryDuration) {
    super(defaultExpiryDuration);
    this.tickTime = 1L;
    this.timeUnit = TimeUnit.SECONDS;
    this.initThread();
  }

  private void initThread() {
    this.scheduledExecutorService.scheduleAtFixedRate(this, 0L, this.tickTime, this.timeUnit);
  }

  /**
   * Clears and shutdown the current cooldown provider
   * NOTE: This should be strictly called on ScheduledCooldown, since this will handle the thread shutdown properly
   */
  @Override
  public void flush() {
    try {
      this.clear();
      this.scheduledExecutorService.shutdown();
    } catch (Exception e) {
      throw new CooldownServiceException(e);
    }
  }

  /**
   * Marks the element to be removed
   *
   * @param key The key that has to be removed
   */
  @Override
  public void removeCooldown(T key) {
    this.toBeRemoved.add(key);
  }

  /**
   * The logic for ticking each element and handle it if the cooldown is over
   *
   * @see Runnable#run()
   * @see Thread#run()
   */
  @Override
  public void run() {
    if (isEmpty())
      return;

    if (!this.toBeRemoved.isEmpty()) {
      Iterator<T> iterator = this.toBeRemoved.iterator();
      while (iterator.hasNext()) {
        T next = iterator.next();
        this.cache.remove(next);
        iterator.remove();
      }
    }

    Iterator<Map.Entry<T, Long>> iterator = this.cache.entrySet().iterator();
    while (iterator.hasNext()) {
      Map.Entry<T, Long> entry = iterator.next();
      if (isInCooldownInternal(entry.getValue()))
        continue;

      iterator.remove();
      super.setExpired(entry.getKey());
    }
  }

  //----------------------------------------------------------------------------
  // BUILDER
  //----------------------------------------------------------------------------
  public static class TickedCooldownBuilder<TKey> {
    private long defaultExpiryDuration;
    private TimeUnit timeUnit;
    private CooldownExpiryAction<TKey> expiryAction;

    private long defaultTickTime;

    private TimeUnit tickTimeUnit;

    public TickedCooldownBuilder() {
      this.withDefaultSettings();
    }

    /**
     * Sets the default expiry time of the cooldown provider
     *
     * @param expiryTime expiry time
     * @return The builder
     */
    public TickedCooldown.TickedCooldownBuilder<TKey> withDefaultExpiryTime(long expiryTime) {
      this.defaultExpiryDuration = expiryTime;
      return this;
    }

    public TickedCooldown.TickedCooldownBuilder<TKey> withDefaultExpiryTimeUnit(TimeUnit unit) {
      this.timeUnit = unit;
      return this;
    }

    public TickedCooldown.TickedCooldownBuilder<TKey> setOnExpiryAction(CooldownExpiryAction<TKey> action) {
      this.expiryAction = action;
      return this;
    }

    public TickedCooldown.TickedCooldownBuilder<TKey> setNoActionOnExpiry() {
      this.expiryAction = null;
      return this;
    }

    public TickedCooldown.TickedCooldownBuilder<TKey> withCooldownTickInterval(long defaultTickTime) {
      this.defaultTickTime = defaultTickTime;
      return this;
    }

    public TickedCooldown.TickedCooldownBuilder<TKey> withCooldownTickTimeUnit(TimeUnit timeUnit) {
      this.tickTimeUnit = timeUnit;
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

    public long getDefaultTickTime() {
      return defaultTickTime;
    }

    public TimeUnit getTickTimeUnit() {
      return tickTimeUnit;
    }

    public TickedCooldown.TickedCooldownBuilder<TKey> withDefaultSettings() {
      this.expiryAction = null;
      this.timeUnit = TimeUnit.SECONDS;
      this.defaultExpiryDuration = 1L;
      this.defaultTickTime = 200;
      this.timeUnit = TimeUnit.MILLISECONDS;
      return this;
    }

    public TickedCooldown<TKey> build() {
      return new TickedCooldown<TKey>(this.timeUnit.toMillis(this.defaultExpiryDuration), expiryAction, defaultTickTime, tickTimeUnit);
    }
  }
}

package in.arcadelabs.labaide.cooldown;

import in.arcadelabs.labaide.cooldown.abstraction.AbstractCooldown;
import in.arcadelabs.labaide.cooldown.abstraction.CooldownExpiryAction;
import in.arcadelabs.labaide.cooldown.exception.CooldownServiceException;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TickedCooldown<T> extends AbstractCooldown<T> implements Runnable {



    private final long tickTime;
    private final TimeUnit timeUnit;

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

    private void initThread(){
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
        }catch (Exception e){
            throw new CooldownServiceException(e);
        }
    }

    /**
     * When an object implementing interface {@code Runnable} is used
     * to create a thread, starting the thread causes the object's
     * {@code run} method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method {@code run} is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        if(isEmpty())
            return;

        Iterator<Map.Entry<T, Long>> iterator = this.cache.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<T, Long> entry = iterator.next();
            if(isInCooldownInternal(entry.getValue()))
                continue;

            iterator.remove();
            super.setExpired(entry.getKey());
        }
    }
}

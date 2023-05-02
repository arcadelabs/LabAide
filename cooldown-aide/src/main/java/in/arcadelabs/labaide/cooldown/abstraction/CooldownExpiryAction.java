package in.arcadelabs.labaide.cooldown.abstraction;

public interface CooldownExpiryAction<T> {

  void onKeyExpired(T key);

}

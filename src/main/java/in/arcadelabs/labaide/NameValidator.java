package in.arcadelabs.labaide;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import in.arcadelabs.labaide.json.JsonFetcher;
import lombok.experimental.UtilityClass;

import java.io.IOException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UtilityClass
public class NameValidator {

  public boolean offlineCheck(final String username) {
    if (username.length() < 3 || username.length() > 16) return false;
    if (username.contains(" ")) return false;

    Pattern p = Pattern.compile("[^a-z0-9_]", Pattern.CASE_INSENSITIVE);
    Matcher m = p.matcher(username);

    return !m.matches();
  }

  public boolean geyserCheck(final String username, final String geyserPrefix) {
    if (!offlineCheck(username)) return false;
    return username.startsWith(geyserPrefix);
  }

  public boolean onlineCheck(final String username) throws IOException {
    if (!offlineCheck(username)) return false;

    String json = JsonFetcher.getJsonString(new URL("https://api.mojang.com/users/profiles/minecraft/" + username));
    JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();

    return jsonObject.has("id");
  }
}

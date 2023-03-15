package in.arcadelabs.labaide.json;

import lombok.experimental.UtilityClass;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

@UtilityClass
public class JsonFetcher {

  public String getJsonString(URL url) throws IOException {
    URLConnection urlConnection = url.openConnection();
    BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
    StringBuilder buffer = new StringBuilder();
    String line;

    while ((line = reader.readLine()) != null) {
      buffer.append(line);
    }
    return buffer.toString();
  }

}



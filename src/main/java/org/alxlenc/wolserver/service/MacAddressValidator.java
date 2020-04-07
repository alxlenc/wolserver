package org.alxlenc.wolserver.service;

import java.util.regex.Pattern;
import org.springframework.stereotype.Component;

@Component
public class MacAddressValidator {

  private static final String MAC_REGEX = "^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})$";

  private static final Pattern MAC_PATTERN = Pattern.compile(MAC_REGEX);

  public boolean validate(String macAddress) {
    return MAC_PATTERN.matcher(macAddress).find();
  }

}

package org.alxlenc.wolserver.service;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.validator.routines.InetAddressValidator;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
@Log4j2
public class WolService {

  private static final int PORT = 9;

  private static final String MAC_SPLIT_STRING = "(\\:|\\-)";

  private static final int MAC_BYTES = 6;

  private MacAddressValidator macValidator;

  private InetAddressValidator addressValidator = InetAddressValidator.getInstance();

  public WolService(final MacAddressValidator macValidator) {
    this.macValidator = macValidator;
  }

  public void sendWol(final String addressStr, final String macStr)
      throws UnknownHostException {

    Assert.isTrue(addressValidator.isValidInet4Address(addressStr),
        "Invalid ipv4 address: " + addressStr);
    Assert.isTrue(macValidator.validate(macStr), "Invalid mac address: " + macStr);

    byte[] macBytes = getMacBytes(macStr);
    byte[] bytes = new byte[6 + 16 * MAC_BYTES];
    for (int i = 0; i < 6; i++) {
      bytes[i] = (byte) 0xff;
    }
    for (int i = 6; i < bytes.length; i += MAC_BYTES) {
      System.arraycopy(macBytes, 0, bytes, i, macBytes.length);
    }

    InetAddress address = InetAddress.getByName(addressStr);
    DatagramPacket packet = new DatagramPacket(bytes, bytes.length, address, PORT);

    try (DatagramSocket socket = new DatagramSocket()) {
      socket.send(packet);
    } catch (IOException e) {
      e.printStackTrace();
    }

    System.out.println("Wake-on-LAN packet sent.");


  }

  private static byte[] getMacBytes(final String macStr) {

    byte[] bytes = new byte[MAC_BYTES];
    String[] hex = macStr.split(MAC_SPLIT_STRING);

    for (int i = 0; i < MAC_BYTES; i++) {
      bytes[i] = (byte) Integer.parseInt(hex[i], 16);
    }

    return bytes;
  }

}

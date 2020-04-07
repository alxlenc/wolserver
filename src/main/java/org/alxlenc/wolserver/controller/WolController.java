package org.alxlenc.wolserver.controller;

import java.net.UnknownHostException;
import org.alxlenc.wolserver.service.WolService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/wol")
public class WolController {

  private WolService wolService;

  public WolController(final WolService wolService) {
    this.wolService = wolService;
  }

  @GetMapping("{ip}/{mac}")
  public String doWake(@PathVariable String ip, @PathVariable String mac)
      throws UnknownHostException {

    this.wolService.sendWol(ip, mac);
    return "OK";

  }

}

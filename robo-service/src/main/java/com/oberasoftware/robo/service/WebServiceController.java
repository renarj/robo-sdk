package com.oberasoftware.robo.service;

import com.sdl.odata.controller.AbstractODataController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author rdevries
 */
@Controller
@RequestMapping("/robot.svc/**")
public class WebServiceController extends AbstractODataController {
}

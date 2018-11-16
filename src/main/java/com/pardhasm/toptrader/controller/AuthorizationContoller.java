package com.pardhasm.toptrader.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.auth0.AuthenticationController;
import com.pardhasm.toptrader.common.CommonConstants;
import com.pardhasm.toptrader.security.AppConfig;

@Controller
public class AuthorizationContoller implements ErrorController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private AuthenticationController controller;

	@Autowired
	private AppConfig appConfig;

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	protected String login(final HttpServletRequest req) {
		String redirectUri = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + "/callback";
		String authorizeUrl = controller.buildAuthorizeUrl(req, redirectUri)
				.withAudience(String.format("https://%s/userinfo", appConfig.getDomain())).build();
		return "redirect:" + authorizeUrl;
	}

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	protected String logout(final HttpServletRequest req) {
		logger.debug("Performing logout");
		invalidateSession(req);
		return "redirect:" + req.getContextPath() + "/login";
	}

	@RequestMapping("/error")
	protected String error(final RedirectAttributes redirectAttributes) throws IOException {
		logger.error("Handling error");
		redirectAttributes.addFlashAttribute("error", true);
		return "redirect:/login";
	}
	
	/*@RequestMapping("/userInfo")
	public String getUserInfo(final HttpServletRequest req){
		
	}*/

	private void invalidateSession(HttpServletRequest request) {
		if (request.getSession() != null) {
			request.getSession().invalidate();
		}
	}

	@Override
	public String getErrorPath() {
		return CommonConstants.PATH;
	}

}

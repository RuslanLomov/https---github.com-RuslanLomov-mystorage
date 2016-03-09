package trainee.controller;

import java.io.File;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import trainee.facade.interf.FileDataFacade;
import trainee.facade.interf.UserFacade;
import trainee.model.FileData;
import trainee.model.User;
import trainee.model.UserRole;

@Controller
public class UserController {
	private static final Logger logger = LoggerFactory.getLogger("MyLogger_ALL");
	private static final String MAIN_ROOT = "D:/javaTest/";
	private static final String MAIN_FAKE_ROOT = "src/";
	private static final String MAIN_FAKE_ROOT_NAME = "src";

	@Autowired
	private UserFacade userFacade;
	@Autowired
	private FileDataFacade fileDataFacade;

	@RequestMapping(value = "/admin**", method = RequestMethod.GET)
	public ModelAndView adminPage() {
		ModelAndView model = new ModelAndView();
		model.addObject("title", "Spring Security Login Form - Database Authentication");
		model.addObject("message", "This page is for ROLE_ADMIN only!");
		model.setViewName("authentication/admin");
		return model;
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public ModelAndView login(
			@RequestParam(value = "error", required = false) String error,
			@RequestParam(value = "logout", required = false) String logout) {
		ModelAndView model = new ModelAndView();
		if (error != null) {
			model.addObject("error", "Invalid username and password!");
		}
		if (logout != null) {
			model.addObject("msg", "You've been logged out successfully.");
		}
		model.setViewName("authentication/login");
		return model;
	}

	// for 403 access denied page
	@RequestMapping(value = "/403", method = RequestMethod.GET)
	public ModelAndView accesssDenied() {
		ModelAndView model = new ModelAndView();
		UserDetails userDetail = UserController.getUserDetails();
		model.addObject("username", userDetail.getUsername());
		model.setViewName("authentication/403");
		return model;
	}

	@RequestMapping("/")
	public ModelAndView root() {
		return new ModelAndView("authentication/login", "", null);
	}

	@RequestMapping("/registration")
	public ModelAndView registration() {
		return new ModelAndView("authentication/registration", "", null);
	}

	@RequestMapping(value = "/check_user", method = RequestMethod.POST)
	public ModelAndView confirmationResult(@ModelAttribute User user,
			HttpServletRequest req) {
		if (req.getParameter("reg") != null) {
			String registrationResponse = userFacade.checkUser(user.getLogin());
			switch (registrationResponse) {
			case "USER_NOT_EXIST": {
				try {
					BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
					String hashedPassword = passwordEncoder.encode(user.getPassword());
					user.setPassword(hashedPassword);
				} catch (Exception e) {
					logger.error("UserController.confirmationResult() threw ", e);
				}
				// insert user
				userFacade.insert(user);
				user = userFacade.getUserByLogin(user.getLogin());
				UserRole role = new UserRole(user.getId(), "ROLE_USER");
				// insert user role
				userFacade.insertUserRole(role);
				File mainFolder = new File(MAIN_ROOT + user.getLogin());
				if (!mainFolder.exists()) {
					// create main user folder
					mainFolder.mkdir();
					// add source folder
					FileData userMainFolder = new FileData(MAIN_FAKE_ROOT_NAME,
							mainFolder.getPath(), MAIN_FAKE_ROOT, user.getId());
					fileDataFacade.insertIntoFiles(userMainFolder);
					// if (!fileDataFacade.isDatabaseFileExist(userMainFolder))
					// {
					//
					// }
				}
				return login(null, null);
			}
			default: {
				return new ModelAndView("authentication/registration", "result",
						"Sorry, such username already reserved. Try another one");
			}
			}
		}
		return new ModelAndView("redirect:" + "authentication/login");
	}

	private static UserDetails getUserDetails() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			return (UserDetails) auth.getPrincipal();
		}
		return null;
	}
}

package vn.tpsc.it4u.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import io.swagger.v3.oas.annotations.Operation;
import java.util.Optional;
import java.util.UUID;
import java.util.Map;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;

import vn.tpsc.it4u.models.User;
import vn.tpsc.it4u.services.EmailService;
import vn.tpsc.it4u.services.ForgotUserService;

@RestController
@RequestMapping("${app.api.version}")
public class PasswordController {
	@Autowired
	private ForgotUserService forgotUserService;

	@Autowired
	private EmailService emailService;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Operation(description = "Get forgot password")
	@GetMapping("/forgot")
	public ModelAndView displayForgotPasswordPage() {
		return new ModelAndView("forgotPassword");
	}

	@Operation(description = "Post forgot password")
	@PostMapping("/forgot")
	public ModelAndView processForgotPasswordPage(ModelAndView modelAndView,
			@RequestBody String postData, HttpServletRequest request) {
		JSONObject getData = new JSONObject(postData);
		String userEmail = getData.getString("email");
		// String test = "";
		Optional<User> optional = forgotUserService.findUserByEmail(userEmail);
		if (!optional.isPresent()) {
			modelAndView.addObject("errorMessage", "We didn't find an account for that e-mail address. ");
		} else {
			User user = optional.get();
			user.setResetToken(UUID.randomUUID().toString());

			// save token to database
			forgotUserService.save(user);
			String appUrl = request.getScheme() + "://" + request.getServerName();

			// Email message
			SimpleMailMessage passwordResetEmail = new SimpleMailMessage();
			passwordResetEmail.setFrom("lap.dang@tpsc.vn");
			passwordResetEmail.setTo(user.getEmail());
			passwordResetEmail.setSubject("Password Reset Request");
			passwordResetEmail.setText("To reset your password, click the link below:\n" + appUrl + "/reset?token="
					+ user.getResetToken());
			emailService.sendEmail(passwordResetEmail);

			// Add success message to view
			modelAndView.addObject("SuccessMessage", "A password reset link has been sent to");

		}
		modelAndView.setViewName("forgotPassword");
		return modelAndView;
	}

	@Operation(description = "Process reset password form")
	@PostMapping("/reset")
	public ModelAndView setNewPassword(ModelAndView modelAndView, @RequestParam Map<String, String> requestParams,
			RedirectAttributes redir) {
		Optional<User> user = forgotUserService.findUserByResetToken(requestParams.get("token"));
		if (user.isPresent()) {
			User resetuser = user.get();
			resetuser.setPassword(bCryptPasswordEncoder.encode(requestParams.get("password")));
			resetuser.setResetToken(null);
			forgotUserService.save(resetuser);
			redir.addFlashAttribute("successMessage", "You have successfully reset your password. You may now login.");
			modelAndView.setViewName("redirect: login");
			return modelAndView;
		} else {
			modelAndView.addObject("errorMessage", "Oops! this is invalid password reset link.");
			modelAndView.setViewName("resetPassword");
		}
		return modelAndView;
	}

	@Operation(description = "Display form to reset password")
	@GetMapping("/reset")
	public ModelAndView getNewPassword(ModelAndView modelAndView, @RequestParam("token") String token) {
		Optional<User> user = forgotUserService.findUserByResetToken(token);
		if (user.isPresent()) {
			modelAndView.addObject("resetToken", token);
		} else {
			modelAndView.addObject("errorMessage", "Oops! This is an invalid password reset link.");
		}

		modelAndView.setViewName("resetPassword");
		return modelAndView;
	}

	@ExceptionHandler(MissingServletRequestParameterException.class)
	public ModelAndView handleMissingParams(MissingServletRequestParameterException ex) {
		return new ModelAndView("redirect:login");
	}
}
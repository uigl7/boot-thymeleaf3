package idu.cs.controller;

import java.util.List;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.servlet.http.HttpSession;
import idu.cs.domain.User;
import idu.cs.exception.ResourceNotFoundException;
import idu.cs.repository.UserRepository;

@Controller // Spring Framework에게 이 클래스로 부터 작성된 객체는 Controller 역할을 항믈 알려줌
            // Spring이 이 클래스로 부터 Bean 객체를 생성해서 등록할 수 있음
			// @Component, @Service, @Repository
public class UserController {
	@Autowired UserRepository userRepo; // Dependency Injection
	@GetMapping("/")
	public String home(Model model) { 
		return "index";
	}
	@GetMapping("/user-login-form")
	public String getLoginForm(Model model) {
		return "login";
	}
	@PostMapping("/login")
	public String loginUser(@Valid User user, HttpSession session) {
		System.out.println("login process : ");
		User sessionUser = userRepo.findByUserId(user.getUserId());
		if(sessionUser == null) {
			System.out.println("id error : ");
			return "redirect:/user-login-form";
		}
		if(!sessionUser.getUserPw().equals(user.getUserPw())) {
			System.out.println("pw error : ");
			return "redirect:/user-login-form";
		}
		session.setAttribute("user", sessionUser);
		return "redirect:/";
	}
	@GetMapping("/logout")
	public String logoutUser(HttpSession session) {
		session.removeAttribute("user");
		// session.invalidate();
		return "redirect:/";
	}
	@GetMapping("/user-register-form")
	public String getRegForm(Model model) {
		return "register";
		}
		@GetMapping("/users")
		public String getAllUser(Model model) {
			model.addAttribute("users", userRepo.findAll());
			return "userlist";
		}
		@PostMapping("/users")
		public String createUser(@Valid User user, Model model) {		
			if(userRepo.save(user) != null)
				System.out.println("Database 등록 성공");
			else
				System.out.println("Database 등록 실패");
			model.addAttribute("users", userRepo.findAll());
			return "redirect:/users";
		}

		
	}
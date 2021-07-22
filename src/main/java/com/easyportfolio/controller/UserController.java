package com.easyportfolio.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.easyportfolio.dao.DetailRepository;
import com.easyportfolio.dao.UserRepository;
import com.easyportfolio.entities.DetailInfo;
import com.easyportfolio.entities.Education;
import com.easyportfolio.entities.Skill;
import com.easyportfolio.entities.User;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private DetailRepository detailRepository;
	@RequestMapping("/dashboard")
	public String dashboard(Model model, Principal principal) {
		try {
			model.addAttribute("title","Dashboard");
			String email = principal.getName();
	//		System.out.println(email);
			User user = userRepository.getUserByUserName(email);
			int userId = user.getDetailId();
	//		System.out.println(userId);
			DetailInfo details = detailRepository.getById(userId);
			System.out.println(details.getFull_name());
			byte[] image = details.getImage();
	//		System.out.println(details.getFirstName());
			model.addAttribute("image", new String(image, "UTF-8"));
			model.addAttribute("user",user);
			return "user_dashboard";
		}
		catch(Exception e) {
			model.addAttribute("title","Dashboard");
			String email = principal.getName();
			User user = userRepository.getUserByUserName(email);
			model.addAttribute(user);
			return "user_dashboard";
		}
	}
	@RequestMapping(path="/dashboard", method=RequestMethod.POST)
	private String editDashboard(final DetailInfo details,final User tempuser,Model model, Principal principal,@RequestParam("file") MultipartFile file) throws IOException {
		String email = principal.getName();
		User user = userRepository.getUserByUserName(email);
		if(file!=null) {
			byte[] image = java.util.Base64.getEncoder().encode(file.getBytes());
			model.addAttribute("image", new String(image, "UTF-8"));
			details.setImage(image);
		}
		if(details!=null)
			user.setDetails(details);
		user.setEducations(tempuser.getEducations());
		user.setExperience(tempuser.getExperience());
		user.setProfile_links(tempuser.getProfile_links());
		user.setSkills(tempuser.getSkills());
		user.setProjects(tempuser.getProjects());
		user.setAchievements(tempuser.getAchievements());
		user.setReference(tempuser.getReference());
		userRepository.save(user);
		model.addAttribute(user);
		return "user_dashboard";
	}
	@GetMapping("/inputform")
	private String inputInformation(Principal principal, Model model) throws UnsupportedEncodingException {
		String email = principal.getName();
		User user = userRepository.getUserByUserName(email);
		int userId = user.getDetailId();
		DetailInfo details = detailRepository.getById(userId);
		byte[] image = details.getImage();
		model.addAttribute("image", new String(image, "UTF-8"));
		model.addAttribute(user);
		return "inputform";
	}
	@RequestMapping(path="/inputform", method= RequestMethod.POST)
	private String ProcessInputForm(final DetailInfo details,final User tempuser,Model model, Principal principal, @RequestParam("file") MultipartFile file) throws IOException {
		String email = principal.getName();
		User user = userRepository.getUserByUserName(email);
		if(file!=null) {
			byte[] image = java.util.Base64.getEncoder().encode(file.getBytes());
			model.addAttribute("image", new String(image, "UTF-8"));
			details.setImage(image);
		}
		if(details!=null)
			user.setDetails(details);
		user.setEducations(tempuser.getEducations());
		user.setExperience(tempuser.getExperience());
		user.setProfile_links(tempuser.getProfile_links());
		user.setSkills(tempuser.getSkills());
		user.setProjects(tempuser.getProjects());
		user.setAchievements(tempuser.getAchievements());
		user.setReference(tempuser.getReference());
		userRepository.save(user);
		model.addAttribute(user);
		return "inputform";
	}
	@GetMapping("/cv1")
	private String cv1(Model model) {
		return "CV01";
	}
	@GetMapping("/cv2")
	private String cv2(Model model) {
		return "CV02";
	}
	@RequestMapping(path="/ex", method= RequestMethod.POST)
	private String exa(final User user, Model model) {
		List<Skill> skill= user.getSkills();
		System.out.println(skill.get(0).getCategory()+skill.get(0).getSkillName());
		return "user_dashboard";
	}
}
package com.ecom.controller;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.ecom.model.Category;
import com.ecom.model.Product;
import com.ecom.model.UserDtls;
import com.ecom.repository.ProductRepository;
import com.ecom.service.CartService;
import com.ecom.service.CategoryService;
import com.ecom.service.ProductService;
import com.ecom.service.UserService;
import com.ecom.util.CommonUtil;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;


@Controller
public class HomeControlller {
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CommonUtil commonUtil;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private CartService cartService;
	
	@ModelAttribute
	public void getUerDetails(Principal p,Model m) 
	{
		 if(p!=null) {
			 String email=p.getName();
		     UserDtls userDtls = userService.getUserByEmail(email);
		     m.addAttribute("user",userDtls);
		     Integer countCart=cartService.getCountCar(userDtls.getId());
		     m.addAttribute("countCart",countCart);
		 }
		 
		 List<Category> allActiveCategory= categoryService.getAllActiveCategory();
			m.addAttribute( "category", allActiveCategory);
	}
	
	@GetMapping("/")
	public String index() {
		return "index";
	}
	
	@GetMapping("/signin")
	public String login() {
		return "login";
	}
	
	@GetMapping("/register")
	public String register() {
		return "register";
	}
		
	@GetMapping("/products")
	public String products(Model m, @RequestParam(value = "category", defaultValue = "") String category,
			@RequestParam(name ="pageNo",defaultValue="0") Integer pageNo,
			@RequestParam(name ="pageSize",defaultValue="5") Integer pageSize) {
		// System.out.println("category="+category);
		List<Category> categories = categoryService.getAllActiveCategory();
		m.addAttribute("paramValue", category);
		m.addAttribute("categories", categories);
		
		Page<Product> page=productService.getAllActiveProductPagination(pageNo, pageSize, category);
		List<Product> products=page.getContent();
		m.addAttribute("products",products);
		m.addAttribute("productsSize",products.size());
		m.addAttribute("pageNo",page.getNumber());
		m.addAttribute("totalElements",page.getTotalElements());
		m.addAttribute("totalPages",page.getTotalPages());
		m.addAttribute("isFirst",page.isFirst());
		m.addAttribute("isLast",page.isLast());
		m.addAttribute("pageSize",pageSize);
		return "product";
	}
	
	@GetMapping("/product/{id}")
	public String product(@PathVariable int id,Model m) {
		Product productById=productService.getProductById(id);
		m.addAttribute("product",productById);
		return "view_product";
	}
	
	@PostMapping("/saveUser")
	public String saveUser(@ModelAttribute UserDtls user, @RequestParam("img") MultipartFile file, HttpSession session)
			throws IOException {

		String imageName = file.isEmpty() ? "default.jpg" : file.getOriginalFilename();
		user.setProfileImage(imageName);
		UserDtls saveUser = userService.saveUser(user);

		if (!ObjectUtils.isEmpty(saveUser)) {
			if (!file.isEmpty()) {
				File saveFile = new ClassPathResource("static/img").getFile();

				Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "profile_img" + File.separator
						+ file.getOriginalFilename());

				System.out.println(path);
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
			}
			session.setAttribute("succMsg", "Register successfully");
		} else {
			session.setAttribute("errorMsg", "something wrong on server");
		}

		return "redirect:/register";
	}
	
	@GetMapping("/forgot-password")
	public String showForgotPassword() {
		return "forgot_password.html";
	}
	
	@PostMapping("/forgot-password")
	public String processForgotPassword(@RequestParam String email,HttpSession session,HttpServletRequest request) throws UnsupportedEncodingException, MessagingException {
		
		UserDtls userByEmail=userService.getUserByEmail(email);
		
		if(ObjectUtils.isEmpty(userByEmail)) {
			session.setAttribute("errorMsg", "Invalid email");
		}else {
			
		String resetToken=UUID.randomUUID().toString();
		userService.updateUserToken(email,resetToken);
		
		//generate URL 
				
		String url = commonUtil.generateUrl(request) + "/reset-password?token=" + resetToken;

		 Boolean sendMail = commonUtil.sendMail(url,email);
		 
		 if (sendMail) {
				session.setAttribute("succMsg", "Please check your email..Password Reset link sent");
			} else {
				session.setAttribute("errorMsg", "Somethong wrong on server ! Email not send");
			}
		}
		
		return "redirect:/forgot-password";
	}
	
	@GetMapping("/reset-password")
	public String showResetPassword(@RequestParam String token,HttpSession session,Model m) {
		UserDtls userByToken= userService.getUserByToken(token);
		
		if(userByToken == null) {
			m.addAttribute("msg", "Your link is invalid or expired !!");
			return "message";
		}
		m.addAttribute("token",token);
		return "reset_password.html";
	}
	
	@PostMapping("/reset-password")
	public String resetPassword(@RequestParam String token,@RequestParam String password,HttpSession session,Model m) {
		UserDtls userByToken= userService.getUserByToken(token);
		if(userByToken == null) {
			m.addAttribute("errorMsg", "Your link is invalid or expired !!");
			return "message";
		}else {
			userByToken.setPassword(passwordEncoder.encode(password));
			userByToken.setResetToken(null);
			userService.updateUser(userByToken);
//			session.setAttribute("succMsg", "Password change succesfully");
			m.addAttribute("msg","Password change succesfully");
			return "message";
		}
	}
	
	@GetMapping("/search")
	public String searchProduct(@RequestParam String ch,Model m) {
		List<Product> searchProduct=productService.searchProduct(ch);
		m.addAttribute("products",searchProduct);
		List<Category> categories=categoryService.getAllActiveCategory();
		m.addAttribute("categories",categories);
		return "/product";
	}
	
	
}

package com.Reap.ReapProject.controller;

import com.Reap.ReapProject.component.LoggedInUser;
import com.Reap.ReapProject.component.SearchUser;
import com.Reap.ReapProject.entity.*;
import com.Reap.ReapProject.exception.UnauthorisedAccessException;
import com.Reap.ReapProject.exception.UserNotFoundException;
import com.Reap.ReapProject.service.ItemService;
import com.Reap.ReapProject.service.OrderSummaryService;
import com.Reap.ReapProject.service.RecognitionService;
import com.Reap.ReapProject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Controller
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    RecognitionService recognitionService;

    @Autowired
    OrderSummaryService orderSummaryService;

    @Autowired
    ItemService itemService;

    // Create new user
    @PostMapping("/users")
    public ModelAndView addUser(@Valid @ModelAttribute("user") User user, BindingResult result,@ModelAttribute("loggedUser")LoggedInUser loggedInUser, @RequestParam("photo") MultipartFile file,HttpServletRequest request,RedirectAttributes redirectAttributes){
        if(result.hasErrors()){
            return new ModelAndView("index");
        }
        else {
                List<String> emails=userService.findAllEmails();
                //for checking unique email id
                if(emails.contains(user.getEmail())){
                    System.out.println("email id already exists");
                    ModelAndView modelAndView=new ModelAndView("redirect:/");
                    redirectAttributes.addFlashAttribute("registrationError","Email id already Taken Try a Different One");
                    return modelAndView;
                }
            HttpSession session=request.getSession();
            session.setAttribute("loginUser",user);
            List<Item> itemList=new ArrayList<>();
            session.setAttribute("itemList",itemList);
            session.setAttribute("currentCartTotal", 0);
            try {
                String path=saveImagePath(file);
                user.setImage(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
            userService.addUser(user);
            return new ModelAndView("redirect:/users/"+user.getId());
        }
    }

    //utility method for saving the path of profile picture of user
    public String saveImagePath(MultipartFile file) throws IOException {
        String UPLOADED_FOLDER = "/home/joyy/Documents/Reap/ReapProject/out/production/resources/static/images/userImages/";
                byte[] bytes = file.getBytes();
                Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
                Files.write(path, bytes);
                return "/images/userImages/"+file.getOriginalFilename();
    }


    @GetMapping("/users")
    public List<User> getAllUsers(){
        return userService.getAllUser();
    }

    // Show user dashboard
    @GetMapping("/users/{id}")
    public ModelAndView getUserById(@PathVariable("id") Integer id, Model model, HttpServletRequest request,RedirectAttributes redirectAttributes){
        HttpSession session=request.getSession();
        User user1=(User) session.getAttribute("loginUser");
        try {
            if(!id.equals(user1.getId())){
                ModelAndView modelAndView=new ModelAndView("redirect:/");
                redirectAttributes.addFlashAttribute("loginError","Please login to continue");
                return modelAndView;
            }
        }
        catch (NullPointerException e){
            ModelAndView modelAndView=new ModelAndView("redirect:/");
            redirectAttributes.addFlashAttribute("loginError","Please login to continue");
            return modelAndView;
        }


        Optional<User> user=userService.getUserById(id);
        if(user.isPresent()){
            ModelAndView modelAndView=new ModelAndView("UserPage");
            model.addAttribute("user",user.get());
            model.addAttribute("recognition",new Recognition());
            model.addAttribute("searchUser",new SearchUser());

            List<Recognition> recognitions=recognitionService.getListOfRecognitions();
            Collections.reverse(recognitions);
            model.addAttribute("recognitions",recognitions);
            if(user.get().getRoleSet().contains(Role.ADMIN)){
                model.addAttribute("isAdmin",true);
                List<User> users=userService.getAllUser();
                model.addAttribute("users",users);
            }
            return modelAndView;
        }
        else throw new UserNotFoundException("no user with the given id exists");
    }


    //this path variable id is coming from form and differs from session id
    //Controller Meant for Admin editing user roles and points
    // Modify user with id {id}
    @PutMapping("/users/{id}")
    public ModelAndView updateUser(@PathVariable Integer id,@RequestParam Map<String, String> requestParams,HttpServletRequest request,RedirectAttributes redirectAttributes){
        HttpSession session=request.getSession();
        User loggedUser=(User)session.getAttribute("loginUser");

        if(session==null){
            throw new UnauthorisedAccessException("Unauthorized Access");
        }

        System.out.println("path variable id "+id);
        System.out.println("session id "+loggedUser.getId());
        Optional<User> user1=userService.getUserById(id);
        if(user1.isPresent()){
            //updating users active status
            if(requestParams.get("active")==null){
                user1.get().setActive(false);
            }
             else {
                user1.get().setActive(true);
            }

            Set<Role> roles=user1.get().getRoleSet();
            roles=roleChecker(roles,requestParams.get("adminCheck"),Role.ADMIN);
            roles=roleChecker(roles,requestParams.get("practiceHeadCheck"),Role.PRACTICE_HEAD);
            roles=roleChecker(roles,requestParams.get("supervisorCheck"),Role.SUPERVISOR);
            roles=roleChecker(roles,requestParams.get("userCheck"),Role.USER);

           //updating roles
            user1.get().setRoleSet(roles);

            //updating the Badges by Admin
            user1.get().setGoldRedeemable(Integer.parseInt(requestParams.get("goldRedeemable")));
            user1.get().setSilverRedeemable(Integer.parseInt(requestParams.get("silverRedeemable")));
            user1.get().setBronzeRedeemable(Integer.parseInt(requestParams.get("bronzeRedeemable")));



            userService.adminEditUser(user1.get());
            redirectAttributes.addFlashAttribute("successfulUpdate","user successfully Updated");

            //Update User and Admin points in the Current Session
            User activeUserRefreshed = userService.getUserById(loggedUser.getId()).get();
            session.setAttribute("loginUser", activeUserRefreshed);
            ModelAndView modelAndView=new ModelAndView("redirect:/users/"+loggedUser.getId());
            return modelAndView;
        }
        else throw new UserNotFoundException("no user with the given id exists");
    }

    // Log user in
    @PostMapping("/login")
    public ModelAndView userLogin(@ModelAttribute("loggedUser")LoggedInUser loggedInUser, HttpServletRequest request, RedirectAttributes redirectAttributes){

        User user=userService.getUserByEmailAndPasswordAndActive(loggedInUser.getEmail(),loggedInUser.getPassword());

        if(user!=null){
            HttpSession session=request.getSession();
            List<Item> itemList=new ArrayList<>();
            session.setAttribute("loginUser",user);
            session.setAttribute("itemList",itemList);
            session.setAttribute("currentCartTotal", 0);
            return  new ModelAndView("redirect:/users/"+user.getId());
        }
        else {
            ModelAndView modelAndView=new ModelAndView("redirect:/");
            redirectAttributes.addFlashAttribute("loginError","Invalid Credentials");
            return modelAndView;
        }
    }

    // Search recognitions by receiver name
    @PostMapping("/searchRecogByName")
    @ResponseBody
    public List<Recognition> getUserRecognitionByNameSearchUser(@ModelAttribute("searchUser")SearchUser searchUser){
        searchUser.getCurrentUserId();
        List<Recognition> recognitions=recognitionService.getListOfRecognitionsByReceiverName(searchUser.getFullName());
        return  recognitions;
    }

    // Search recognitions by date
    @GetMapping("/searchRecogByDate/{date}")
    @ResponseBody
    public List<Recognition> getUserRecognitionByNameDate(@PathVariable("date") String date){
        List<Recognition> recognitions=recognitionService.findRecognitionByDateBetween(date);
        return recognitions;
    }

    // Autocomplete user name
    @GetMapping("/autocomplete")
    @ResponseBody
    public List<User> autoComplete(@RequestParam("pattern")String namePattern){
        return userService.findByFullNameLike(namePattern+"%");
    }

    // Log user out
    @PostMapping("/logout")
    public String logout(HttpServletRequest request)
    {
        HttpSession session=request.getSession();
        session.invalidate();
        return "redirect:/";
    }


    // Show user recognitions
    @GetMapping("/users/{id}/recognitions")
    public ModelAndView getUserRecognitions(@PathVariable("id") Integer id,
                                            HttpServletRequest httpServletRequest,
                                            RedirectAttributes redirectAttributes) {
        HttpSession httpSession = httpServletRequest.getSession();
        User activeUser = (User) httpSession.getAttribute("loginUser");
        try {
            if (!id.equals(activeUser.getId())) {
                ModelAndView modelAndView = new ModelAndView("redirect:/");
                redirectAttributes.addFlashAttribute("loginError", "Please log in to view your recognitions");
                return modelAndView;
            }
        } catch (NullPointerException ne) {
            ModelAndView modelAndView = new ModelAndView("redirect:/");
            redirectAttributes.addFlashAttribute("loginError", "Please log in to view your recognitions");
            return modelAndView;
        }
        Optional<User> optionalUser = userService.getUserById(id);

        ModelAndView modelAndView = new ModelAndView("recognitions");
        modelAndView.addObject("user", optionalUser.get());

        List<Recognition> receivedRecognitionsList = recognitionService.findRecognitionByReceiverId(optionalUser.get().getId());
        modelAndView.addObject("receivedRecognitionsList", receivedRecognitionsList);

        List<Recognition> sentRecognitionsList = recognitionService.findRecognitionBySenderId(optionalUser.get().getId());
        modelAndView.addObject("sentRecognitionsList", sentRecognitionsList);
        return modelAndView;
    }


    //user role checking utility methods
    public Set<Role> roleChecker(Set<Role> roles,String status,Role role){
        if(status==null){
            roles.remove(role);
            return roles;
        }
        else {
            roles.add(role);
            return roles;
        }
    }


    //cart controller
    // Show user cart
    @GetMapping("/users/{id}/cart")
    public ModelAndView getUserCart(@PathVariable("id") Integer id,
                                    HttpServletRequest httpServletRequest,
                                    RedirectAttributes redirectAttributes) {
        HttpSession httpSession = httpServletRequest.getSession();
        User activeUser = (User) httpSession.getAttribute("loginUser");
        try {
            if (!id.equals(activeUser.getId())) {
                ModelAndView modelAndView = new ModelAndView("redirect:/");
                redirectAttributes.addFlashAttribute("error", "Please log in to view your cart");
                return modelAndView;
            }
        } catch (NullPointerException ne) {
            ModelAndView modelAndView = new ModelAndView("redirect:/");
            redirectAttributes.addFlashAttribute("error", "Please log in to view your cart");
            return modelAndView;
        }
        Optional<User> optionalUser = userService.getUserById(id);
        if (!optionalUser.isPresent()) {
            throw new UserNotFoundException("No user with id " + id);
        }
        ModelAndView modelAndView = new ModelAndView("cart");
        modelAndView.addObject("user", optionalUser.get());
        List<Item> itemList = (List<Item>) httpSession.getAttribute("itemList");
        modelAndView.addObject("itemList", itemList);
        Integer currentCartTotal = (Integer) httpSession.getAttribute("currentCartTotal");
        modelAndView.addObject("currentCartTotal", currentCartTotal);
        return modelAndView;
    }

    //orderHistory
    // Show user order history
    @GetMapping("/users/{id}/orders")
    public ModelAndView getOrderHistory(@PathVariable("id") Integer id,
                                    HttpServletRequest httpServletRequest,
                                    RedirectAttributes redirectAttributes) {
        HttpSession httpSession = httpServletRequest.getSession();
        User activeUser = (User) httpSession.getAttribute("loginUser");
        try {
            if (!id.equals(activeUser.getId())) {
                ModelAndView modelAndView = new ModelAndView("redirect:/");
                redirectAttributes.addFlashAttribute("error", "Please log in to view your order History");
                return modelAndView;
            }
        } catch (NullPointerException ne) {
            ModelAndView modelAndView = new ModelAndView("redirect:/");
            redirectAttributes.addFlashAttribute("error", "Please log in to view your order history");
            return modelAndView;
        }
        Optional<User> optionalUser = userService.getUserById(id);
        if (!optionalUser.isPresent()) {
            throw new UserNotFoundException("No user with id " + id);
        }

        ModelAndView modelAndView = new ModelAndView("OrderHistory");
        modelAndView.addObject("user", optionalUser.get());
        List<OrderSummary> orderSummaries =orderSummaryService.getAllOrdersByUserId(activeUser.getId());
        modelAndView.addObject("orderSummaryList", orderSummaries);

        //this list will store all the item keys in the order summaries
        List<Integer> itemIdList = new ArrayList<>();

        //this set will be added in the model and view to render the items in the frontend ie order history page
        Set<Item> itemSetInOrderSummaryList = new LinkedHashSet<>();
        for (OrderSummary orderSummary : orderSummaries) {
            itemIdList = orderSummary.getItemIdsInOrderSummary();
            for (Integer itemId : itemIdList) {
                itemSetInOrderSummaryList.add(itemService.findItemById(itemId).get());
            }
        }
        modelAndView.addObject("itemSetInOrderSummaryList", itemSetInOrderSummaryList);
        return modelAndView;
    }

}


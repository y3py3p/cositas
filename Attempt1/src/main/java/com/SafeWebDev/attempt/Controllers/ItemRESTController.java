package com.SafeWebDev.attempt.Controllers;


import com.SafeWebDev.attempt.Controllers.Payloads.LoginRequest;
import com.SafeWebDev.attempt.Controllers.Payloads.MessageResponse;
import com.SafeWebDev.attempt.Controllers.Payloads.SignupRequest;
import com.SafeWebDev.attempt.Controllers.Payloads.UserInfoResponse;
import com.SafeWebDev.attempt.Controllers.Security.JwtUtils;
import com.SafeWebDev.attempt.Models.*;
import com.SafeWebDev.attempt.Models.Services.*;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.bytecode.internal.bytebuddy.PassThroughInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import javax.annotation.PostConstruct;
import javax.management.relation.Role;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
public class ItemRESTController {

    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private ItemService itemService;
    @Autowired
    private UserService userService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private CuponService cuponService;
    @Autowired
    private EntityManagerFactory entityManagerFactory;
    @Autowired
    private PasswordEncoder encoder;

    private User currentUser;

    @PostConstruct
    public void init(){
        currentUser=new User("Default");
    }

    @GetMapping("/see") //to see every item on stock
    public List<Item> getItems(){

        return itemService.getAll();
    }

    @GetMapping("/see/{id}")    //see a specified item with id
    public ResponseEntity<Item> getById(@PathVariable long id){

        if(itemService.exists(id)){
            return new ResponseEntity<>(itemService.findById(id), HttpStatus.ACCEPTED);
        }
        else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @GetMapping("/del/{id}")    //delete an item
    public ResponseEntity<Item> deleteItem(@PathVariable long id){
        itemService.delete(itemService.findById(id));
        return new ResponseEntity<>(itemService.findById(id),HttpStatus.ACCEPTED);
    }

    @PostMapping("/addItem")    //add item to stock
    public ResponseEntity<Item> add(@RequestBody Item item){

        itemService.add(item);

        return new ResponseEntity<>(item, HttpStatus.CREATED);
    }

    @PostMapping("/editItem/{id}")  //edit item info
    public ResponseEntity<Item> edit(@RequestBody Item item, @PathVariable long id){

        if(itemService.exists(id)){
            itemService.updateItem(id, item);
            return new ResponseEntity<>(itemService.findById(id), HttpStatus.CREATED);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }


    }

    @PostMapping("/newUser")
    public ResponseEntity<User> newUser(@RequestBody User user){

        userDetailsService.saveUser(user);

        //currentUser=user;

        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest){
        if(userService.findByOnlyName(signupRequest.getUsername()) != null){
            return ResponseEntity.badRequest().body(new MessageResponse("Error: nombre de usuario ya usado"));
        }

        User user = new User(signupRequest.getUsername(), signupRequest.getEmail(), signupRequest.getPassword(), signupRequest.getAddress(), signupRequest.getPersonalName());
        user.setUserPass(encoder.encode(user.getUserPass()));
        userDetailsService.saveUser(user);
        return ResponseEntity.ok(new MessageResponse("Usuario registrado"));
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginRe(@Valid @RequestBody LoginRequest loginRequest){

        log.info("Esto llega");
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        log.info("Esto llega 2");
        SecurityContextHolder.getContext().setAuthentication(authentication);
        log.info("Esto llega 3");
        UserDetailsEntityImpl userDetails = (UserDetailsEntityImpl) authentication.getPrincipal();
        log.info("Esto llega4");
        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

        log.info("Esto llega 5");


        String role = userDetails.getAuthorities().toString();
        RoleName roleName;
        if(role == RoleName.ADMIN.toString()){
            roleName = RoleName.ADMIN;
        }else {
            roleName = RoleName.USER;
        }

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .body(new UserInfoResponse(userDetails.getId(), userDetails.getUsername(), userDetails.getEmail(), roleName));


    }

    /*@PostMapping("/login")
    public ResponseEntity<User> login(@RequestParam String username, @RequestParam String password){


    }*/

    @GetMapping("/addCart/{id}")    //add item to cart
    public ResponseEntity<List<Item>> addCart(@PathVariable long id){

        if(currentUser.getCart().contains(itemService.findById(id))){
            return new ResponseEntity<>( HttpStatus.NOT_ACCEPTABLE);
        }else{
            currentUser.addCart(itemService.findById(id));
            return new ResponseEntity<>(currentUser.getCart(), HttpStatus.ACCEPTED);
        }

    }

    @GetMapping("/seeCart") //see the cart
    public List<Item> seeCart(){

        return currentUser.getCart();

    }

    @GetMapping("/removeCart/{id}") //remove item from cart
    public ResponseEntity<List<Item>> removeCart(@PathVariable int id){
        currentUser.getCart().remove(itemService.findById(id));
        return new ResponseEntity<>(currentUser.getCart(),HttpStatus.ACCEPTED);
    }

    @GetMapping("/usr") //see your user
    public User seeUser(HttpServletRequest request){

        /*if(currentUser!=null){    //check if you're loged in
            return currentUser;
        }else{
            return null;
        }*/
        if(request.getUserPrincipal() != null){

            User user = userService.findByOnlyName(request.getUserPrincipal().getName());
            return user;
        }else {
            return null;
        }

    }
    @GetMapping("/comments")    //see every comment in our database
    public ResponseEntity<List<Comment>> comments(Model model){
        return new ResponseEntity<>(commentService.getAll(),HttpStatus.ACCEPTED);
    }

    @PostMapping("/NewComment")     //add a comment to our database
    public ResponseEntity<Comment> addComment(Model model, @RequestBody Comment comment){
        commentService.addComment(comment);
        return new ResponseEntity<>(comment,HttpStatus.ACCEPTED);
    }

    @PostMapping("/coupon/new") //create a coupon
    public ResponseEntity<Cupon> createCoupon(@RequestBody Cupon coupon){
        cuponService.addCupon(coupon);
        currentUser.addCupon(coupon);
        return new ResponseEntity<>(coupon, HttpStatus.CREATED);
    }

    @GetMapping("/coupons") //see the coupons
    public ResponseEntity<List<Cupon>> getCupons(){
        return new ResponseEntity<>(currentUser.getCupones(), HttpStatus.ACCEPTED);
    }

    @GetMapping("/pay") //pay
    public ResponseEntity<Float> pay(){

        return new ResponseEntity<>(currentUser.getPrice(), HttpStatus.ACCEPTED);
    }

    @GetMapping("pay/cupon/{id}")   //pay with coupons
    public ResponseEntity<Float> payCupons(@PathVariable long id){

        return new ResponseEntity<>(currentUser.priceCupon(cuponService.findById(id)), HttpStatus.ACCEPTED);
    }

    @GetMapping("/search/{name}")   //search by name
    public ResponseEntity<List<Item>> searchByName(@PathVariable String name){

        String webo = name.replaceAll(".*([';]+|(--)+).*", " ");
        System.out.println(webo);
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        TypedQuery<Item> q1=entityManager.createQuery("SELECT c FROM Item c WHERE c.productName like concat('%',:webo,'%')",Item.class).setParameter("webo",webo);
        List<Item> items=q1.getResultList();
        entityManager.getTransaction().commit();
        entityManager.close();
        return new ResponseEntity<>(items, HttpStatus.ACCEPTED);
    }

}
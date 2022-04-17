package com.SafeWebDev.attempt.Controllers;


import com.SafeWebDev.attempt.Models.*;
import com.SafeWebDev.attempt.Models.Services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ItemRESTController {


    @Autowired
    private ItemService itemService;
    @Autowired
    private UserService userService;
    @Autowired
    private CommentService commentService;

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

    @GetMapping("/del/{id}")
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

        userService.saveUser(user);

        currentUser=user;

        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

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
    public User seeUser(){

        if(currentUser!=null){    //check if you're loged in
            return currentUser;
        }else{
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

    @GetMapping("/coupons")
    public ResponseEntity<List<Cupon>> getCupons(){
        return new ResponseEntity<>(currentUser.getCupones(), HttpStatus.ACCEPTED);
    }

    @PostMapping("/pay")
    public void pay(){


    }

}
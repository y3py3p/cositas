package com.SafeWebDev.attempt.Controllers;

import com.SafeWebDev.attempt.Models.Entities.Item;
import com.SafeWebDev.attempt.Models.Respositories.ItemRepository;
import com.SafeWebDev.attempt.Models.Holders.*;
import com.SafeWebDev.attempt.Models.Services.ItemService;
import com.SafeWebDev.attempt.Models.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

@Controller
public class ControllerBasic {

    //private GeneralHolder generalHolder=new GeneralHolder();
    @Autowired
    private ItemService itemService;

    @Autowired
    private UserService userService;


    public ControllerBasic() {  //initializing the default products
        /*generalHolder.addItem(new Item("Boxers Hombre", "XXL", "Desgastado, dado de sí y manchado", 10));
        generalHolder.addItem(new Item("Bragas Mujer", "XL", "Desgastado, sucio", 15));
        generalHolder.addItem(new Item("Calcetin Blanco", "L", "Con agujeros, olor a esmegma", 35));
        generalHolder.addItem(new Item("Sujetador Mujer", "92B", "Hecho mierda", 25));
        generalHolder.addUsr(new User("hola","deez"));
        generalHolder.addUsr(new User("Usuario temporal","deez"));
        generalHolder.setCurrentUser(generalHolder.getUsrId(2));*/

        //itemService.add(new Item("Bragas Mujer", "XL", "Desgastado, sucio", 15));
    }


    @GetMapping("")     //redirect to StartPage.html, the main page
    public String homePage(Model model) {
        /*if(generalHolder.getCurrentUser() == null){
            model.addAttribute("login", "LogIn");   //login link will show up only if you're not logged in
        }else{
            model.addAttribute("login", "");
        }
        return "StartPage";*/
        return "StartPage";
    }

    @GetMapping("/item/edit/{id}")
    public String updateItem(Model model,@PathVariable long id){
        /*model.addAttribute("item",generalHolder.getItemId(id));
        return "ItemEdit";*/

        model.addAttribute("item", itemService.findById(id));
        return "ItemEdit";
    }

    @PostMapping("/editting/{id}")
    public String updatingItem(Model model,@PathVariable long id,Item item){
        /*generalHolder.getItemId(id).update(item);
        return "ItemEdited";*/
        itemService.findById(id).update(item);
        return "ItemEdited";
    }

    @GetMapping("/item/{id}")   //redirect to ItemPage.html, where you can see the info of one item
    public String itemPage(Model model, @PathVariable long id) {
        /*model.addAttribute("item",generalHolder.getItemId(id));
        return "ItemPage";*/

        model.addAttribute("item", itemService.findById(id));
        return "ItemPage";
    }

    @GetMapping("/usr") //redirect to UsrPage.html with your usr info (right now just the admin user)
    public String usrPage(Model model) {
        /*model.addAttribute("user", generalHolder.getCurrentUser());
        return "UsrPage";*/

        model.addAttribute("user", userService.getCurrentUser());
        return "UsrPage";

    }

    @PostMapping("/item/new")   //redirect to ItemAdded.html after adding an item to our general List
    public String addItem(Model model,Item item){
        /*generalHolder.addItem(item);
        return "ItemAdded";*/
        itemService.add(item);
        return "ItemAdded";
    }

    @GetMapping("/items")   //redirect to ItemsList.html, where you can see every product aviable
    public String listaItems(Model model){
        /*model.addAttribute("items", generalHolder.getItems().values());
        return "ItemsList";*/
        model.addAttribute("items", itemService.getAll());
        return "ItemsList";
    }

    @GetMapping("/cart")    //redirect to Cart.html, with your cart info
    public String carrito(Model model){
        /*model.addAttribute("cart", generalHolder.getCurrentUser().getCart());
        return "Cart";*/
        model.addAttribute("cart", userService.getCart());
        return "Cart";
    }

    @GetMapping("/cart/{id}")   //redirect to CartAdded.html or CartAlreadyContains.html
    public String addCarrito(Model model, @PathVariable long id){
        /*if(!generalHolder.getCurrentUser().cartContains(generalHolder.getItemId(id))){
            generalHolder.getCurrentUser().addCart(generalHolder.getItemId(id));
            return "CartAdded"; //item added
        }else{
            return "CartAlreadyContains";   //you already have the item in your cart
        }*/

        userService.addCart(itemService.findById(id));
        return "CartAdded";
    }

    @GetMapping("/cart/del/{id}")   //redirect to ItemDeleted.html, to confirm the item was deleted
    public String deleteItem(@PathVariable int id){
        /*generalHolder.getCurrentUser().delCart(generalHolder.getItemId(id));
        return "ItemDeleted";*/
        return "ItemDeleted";

    }

    @GetMapping("/login")   //redirect to LogIn.html, where you'll be able to log in
    public String logIn(){
        return "LogIn";
    }

    @GetMapping("/createAccount")   //redirect to CreateAccount.html, to sign up
    public String createAccount(){
        return "CreateAccount";

    }
    
}

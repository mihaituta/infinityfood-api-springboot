package com.tm.controller;


import com.tm.dto.MenuRequest;
import com.tm.dto.UpdateRestaurantRequest;
import com.tm.service.MenuService;
import com.tm.util.Response;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/staff")
@PreAuthorize("hasAuthority('Staff')")
public class MenuController {

    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    // GET MENUS
    @GetMapping("/menus")
    public ResponseEntity<Response<Object>> getMenus() {
        return menuService.getMenus();
    }


    // CREATE MENU
    @PostMapping("/menu/create")
    public ResponseEntity<Response<Void>> createMenu(@ModelAttribute MenuRequest request) {
        return menuService.createMenu(request);
    }

    // UPDATE MENU
    @PatchMapping("/menu/{id}")
    public ResponseEntity<Response<Void>> updateMenu( @PathVariable Long id, @Valid @ModelAttribute MenuRequest request) {
        return menuService.updateMenu(id, request);
    }


    // DELETE MENU
    @DeleteMapping("/menu/{id}")
    public ResponseEntity<Response<Void>> deleteMenu(@PathVariable Long id) {
        return menuService.deleteMenu(id);
    }

}

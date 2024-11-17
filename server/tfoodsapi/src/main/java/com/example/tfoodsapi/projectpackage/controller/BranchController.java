package com.example.tfoodsapi.projectpackage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.tfoodsapi.projectpackage.DTOModel.BranchDTO;
import com.example.tfoodsapi.projectpackage.model.Branch;
import com.example.tfoodsapi.projectpackage.service.BranchService;
import com.example.tfoodsapi.projectpackage.service.UserService;
import com.example.tfoodsapi.security.authentication.CustomAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/branches")
public class BranchController {
    @Autowired
    private BranchService branchService;
    @Autowired
    private UserService userService;

    @PostMapping("/add")
    public ResponseEntity<String> addBranch(@RequestBody BranchDTO branch) {
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        Integer userId = getAuthenticatedUserId(authentication);

        if (!userService.isSeller(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied: User is not a SELLER.");
        }

        branchService.addBranch(branch, userId);
        return ResponseEntity.ok("Branch added successfully.");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateBranch(@PathVariable Integer id, @RequestBody BranchDTO branch) {
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        Integer userId = getAuthenticatedUserId(authentication);

        if (!userService.isSeller(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied: User is not a SELLER.");
        }

        branchService.updateBranch(id, branch, userId);
        return ResponseEntity.ok("Branch updated successfully.");
    }

    @GetMapping("/{id}")
    public Branch getMethodName(@PathVariable Integer id) {
        return branchService.getBranchByID(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBranch(@PathVariable Integer id) {
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        Integer userId = getAuthenticatedUserId(authentication);
        System.out.println(userId);
        if (!userService.isSeller(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied: User is not a SELLER.");
        }

        branchService.deleteBranch(id, userId);
        return ResponseEntity.ok("Branch deleted successfully.");
    }

    private Integer getAuthenticatedUserId(Authentication authentication) {
        try {

            if (authentication instanceof CustomAuthenticationToken) {
                CustomAuthenticationToken customAuth = (CustomAuthenticationToken) authentication;

                return customAuth.getUserId();
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }

    }

    @GetMapping("/search")
    public Page<Branch> getBranches(
            @RequestParam(defaultValue = "0") int page,

            @RequestParam(required = false) String name,
            @RequestParam(required = false) String address) {

        Pageable pageable = PageRequest.of(page, 10);
        return branchService.getBranches(name, address, pageable);
    }

    @GetMapping("/mybranches")
    public ResponseEntity<Page<Branch>> getCurrentUserBranches(@RequestParam(defaultValue = "0") int page) {
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        // Assuming the UserDetails contains the user ID or a method to retrieve it.
        Integer userId = getAuthenticatedUserId(authentication); // Implement this method accordingly
        if (!userService.isSeller(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        Pageable pageable = PageRequest.of(page, 10);
        return ResponseEntity.ok(branchService.getBranchesByUserId(userId, pageable));
    }
}

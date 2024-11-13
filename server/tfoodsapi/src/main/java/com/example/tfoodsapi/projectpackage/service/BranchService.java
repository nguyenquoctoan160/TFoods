package com.example.tfoodsapi.projectpackage.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.tfoodsapi.projectpackage.model.Branch;
import com.example.tfoodsapi.projectpackage.model.User;
import com.example.tfoodsapi.projectpackage.repository.BranchRepository;

@Service
public class BranchService {
    @Autowired
    private BranchRepository branchRepository;
    @Autowired
    private UserService userService;

    public void addBranch(Branch branch, Integer userId) {
        User seller = userService.getUserById(userId);
        branch.setSeller(seller);
        branchRepository.save(branch);
    }

    public void updateBranch(Integer branchId, Branch updatedBranch, Integer userId) {
        Branch existingBranch = branchRepository.findById(branchId)
                .orElseThrow(() -> new RuntimeException("Branch not found"));

        existingBranch.setName(updatedBranch.getName());
        existingBranch.setAddress(updatedBranch.getAddress());
        branchRepository.save(existingBranch);
    }

    public void deleteBranch(Integer branchId, Integer userId) {
        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new RuntimeException("Branch not found"));

        branchRepository.delete(branch);
    }
}

package com.example.tfoodsapi.projectpackage.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.tfoodsapi.projectpackage.DTOModel.BranchDTO;
import com.example.tfoodsapi.projectpackage.model.Branch;
import com.example.tfoodsapi.projectpackage.model.User;
import com.example.tfoodsapi.projectpackage.repository.BranchRepository;

@Service
public class BranchService {
    @Autowired
    private BranchRepository branchRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private RedisService redisService;

    public Page<Branch> getBranches(String name, String address, Pageable pageable) {
        if ((name != null && !name.isEmpty()) || (address != null && !address.isEmpty())) {
            return branchRepository.findByNameContainingIgnoreCaseOrAddressContainingIgnoreCase(name, address,
                    pageable);
        } else {
            return branchRepository.findAll(pageable);
        }
    }

    public Page<Branch> getBranchesByUserId(Integer userId, Pageable pageable) {
        User user = userService.getUserById(userId);
        return branchRepository.findBySeller(user, pageable);
    }

    public void addBranch(BranchDTO branchDTO, Integer userId) {
        User seller = userService.getUserById(userId);
        Branch branch = new Branch(branchDTO.getName(), branchDTO.getAddress(), seller);
        branch.setSeller(seller);
        branchRepository.save(branch);
        redisService.setBranchFromID(branch);
    }

    public void updateBranch(Integer branchId, BranchDTO updatedBranch, Integer userId) {
        Branch existingBranch = branchRepository.findById(branchId)
                .orElseThrow(() -> new RuntimeException("Branch not found"));

        existingBranch.setName(updatedBranch.getName());
        existingBranch.setAddress(updatedBranch.getAddress());
        branchRepository.save(existingBranch);
        redisService.setBranchFromID(existingBranch);
    }

    public void deleteBranch(Integer branchId, Integer userId) {
        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new RuntimeException("Branch not found"));

        // Check if the branch belongs to the user
        if (!branch.getOwnerId().equals(userId)) {
            throw new RuntimeException("You do not have permission to delete this branch");
        }

        branchRepository.delete(branch);
        redisService.deleteBranchById(branchId);
    }

    public Branch getBranchByID(Integer branchID) {
        Branch branch = redisService.getBranchFromID(branchID);
        if (branch != null) {
            return branch;
        }
        branch = branchRepository.findById(branchID)
                .orElseThrow(() -> new RuntimeException("Branch not found with id: " + branchID));
        redisService.setBranchFromID(branch);
        return branch;
    }

}

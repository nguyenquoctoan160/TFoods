import { Component } from '@angular/core';
import { Page } from 'src/app/interfaces/page';
import { Branch } from 'src/app/models/branch';
import { BranchService } from 'src/app/services/branch.service';

@Component({
  selector: 'app-branch-search',
  templateUrl: './branch-search.component.html',
  styleUrls: ['./branch-search.component.scss']
})
export class BranchSearchComponent {
  name: string = '';
  address: string = '';
  branches: Branch[] = [];
  page: number = 0;

  constructor(private branchService: BranchService) { }

  ngOnInit(): void {
    this.searchBranches();
  }

  searchBranches(): void {
    this.branchService.getBranches(this.page, this.name, this.address)
      .subscribe((response: Page<Branch>) => {
        this.branches = response.content;
      });
  }

  onPageChange(page: number): void {
    this.page = page;
    this.searchBranches();
  }

  viewBranch(id: number): void {
    // Logic to navigate to branch details
  }

  editBranch(id: number): void {
    // Logic to navigate to branch edit page
  }
}

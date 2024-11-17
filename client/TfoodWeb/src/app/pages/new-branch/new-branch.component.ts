import { HttpErrorResponse } from '@angular/common/http';
import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { ApiResponse } from 'src/app/interfaces/response';
import { Branch } from 'src/app/models/branch';
import { BranchDTO } from 'src/app/models/branch-dto';
import { BranchService } from 'src/app/services/branch.service';

@Component({
  selector: 'app-new-branch',
  templateUrl: './new-branch.component.html',
  styleUrls: ['./new-branch.component.scss']
})
export class NewBranchComponent {
  branch: BranchDTO = {
    name: '',
    address: '',

  };

  constructor(private translate: TranslateService, private branchService: BranchService, private router: Router) { }


  addBranch() {
    this.branchService.addBranch(this.branch).subscribe({
      next: (response) => {
        this.translate.get('BRANCH_ADDED_SUCCESS').subscribe((successMessage: string) => {
          this.translate.get('CONFIRM_REDIRECT_TO_MY_BRANCHES').subscribe((confirmMessage: string) => {
            if (confirm(confirmMessage)) {
              this.router.navigate(['/branches/my-branches']);
            }
          });
        });
      },
      error: (error: HttpErrorResponse) => {
        console.error(error);
        this.translate.get('BRANCH_ADD_FAILED').subscribe((errorMessage: string) => {
          alert(`${errorMessage}: ${error.message || error}`);
        });
      },
    });
  }

}

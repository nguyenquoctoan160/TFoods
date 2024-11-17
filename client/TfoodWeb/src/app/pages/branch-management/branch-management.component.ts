import { Component } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { TranslateService } from '@ngx-translate/core';
import { Page } from 'src/app/interfaces/page';
import { Branch } from 'src/app/models/branch';
import { BranchService } from 'src/app/services/branch.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ConfirmationDialogComponent } from 'src/app/components/confirmation-dialog/confirmation-dialog.component';
import { EditBranchDialogComponent } from 'src/app/components/edit-branch-dialog/edit-branch-dialog.component';
@Component({
  selector: 'app-branch-management',
  templateUrl: './branch-management.component.html',
  styleUrls: ['./branch-management.component.scss']
})
export class BranchManagementComponent {
  myBranches: Branch[] = [];
  page: number = 0;
  previousLabel: string = '';
  nextLabel: string = '';
  constructor(private branchService: BranchService, private translate: TranslateService, private dialog: MatDialog,
    private snackBar: MatSnackBar) { }

  ngOnInit(): void {
    this.loadTranslations();
    this.loadMyBranches();
  }
  loadTranslations() {
    this.translate.get('PREVIOUS').subscribe((res: string) => {
      this.previousLabel = res;
    });
    this.translate.get('NEXT').subscribe((res: string) => {
      this.nextLabel = res;
    });
  }
  loadMyBranches(): void {
    this.branchService.getBranchesByUserId(this.page)
      .subscribe((response: Page<Branch>) => {
        this.myBranches = response.content;
      });
  }

  onPageChange(page: number): void {
    this.page = page;
    this.loadMyBranches();
  }

  editBranch(branch: Branch): void {
    this.translate.get(['EDIT_BRANCH_TITLE', 'CANCEL', 'SAVE']).subscribe(translations => {
      const dialogRef = this.dialog.open(EditBranchDialogComponent, {
        data: { branch },
        width: '400px'
      });

      dialogRef.afterClosed().subscribe(result => {
        if (result) {
          const updatedBranch: Branch = { ...branch, ...result };
          this.branchService.updateBranch(branch.id, updatedBranch).subscribe(
            () => {
              this.translate.get('EDIT_SUCCESS_MESSAGE').subscribe(successMessage => {
                this.snackBar.open(successMessage, '', { duration: 3000 });
              });
              this.loadMyBranches(); // Tải lại danh sách chi nhánh
            },
            (error) => {
              this.translate.get('EDIT_ERROR_MESSAGE').subscribe(errorMessage => {
                this.snackBar.open(errorMessage, '', { duration: 3000 });
              });
              console.error(error);
            }
          );
        }
      });
    });
  }

  deleteBranch(id: number, name: string): void {
    // Dịch nội dung hộp thoại
    this.translate.get(['DELETE_CONFIRM_TITLE', 'DELETE_CONFIRM_MESSAGE', 'YES', 'NO']).subscribe(translations => {
      const dialogRef = this.dialog.open(ConfirmationDialogComponent, {
        data: {
          title: translations['DELETE_CONFIRM_TITLE'],
          message: `${translations['DELETE_CONFIRM_MESSAGE']} ${name}?`,
          confirmText: translations['YES'],
          cancelText: translations['NO']
        }
      });

      // Xử lý khi người dùng chọn xác nhận hoặc hủy
      dialogRef.afterClosed().subscribe(result => {
        if (result === true) {
          // Gọi API xóa
          this.branchService.deleteBranch(id).subscribe(
            (response) => {
              this.translate.get('DELETE_SUCCESS_MESSAGE').subscribe(successMessage => {
                this.snackBar.open(successMessage, '', { duration: 3000 });
              });
              this.loadMyBranches(); // Reload danh sách sau khi xóa thành công
            },
            (error) => {
              this.translate.get('DELETE_ERROR_MESSAGE').subscribe(errorMessage => {
                this.snackBar.open(errorMessage, '', { duration: 3000 });
              });
              console.error(error); // Ghi log lỗi
            }
          );
        }
      });
    });
  }
}

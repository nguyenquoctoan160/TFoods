import { Component } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { TranslateService } from '@ngx-translate/core';
import { CategoryDialogComponent } from 'src/app/components/category-dialog/category-dialog.component';
import { ConfirmationDialogComponent } from 'src/app/components/confirmation-dialog/confirmation-dialog.component';
import { Category } from 'src/app/models/category';
import { CategoryService } from 'src/app/services/category.service';

@Component({
  selector: 'app-category-management',
  templateUrl: './category-management.component.html',
  styleUrls: ['./category-management.component.scss']
})
export class CategoryManagementComponent {
  categories: Category[] = [];
  page: number = 0;
  previousLabel: string = '';
  nextLabel: string = '';
  searchQuery: string = '';


  constructor(
    private categoryService: CategoryService,
    private translate: TranslateService,
    private dialog: MatDialog,
    private snackBar: MatSnackBar
  ) { }

  ngOnInit(): void {
    this.loadTranslations();
    this.loadCategories();
  }
  onSearch(): void {
    this.categoryService.getCategories(this.page, this.searchQuery).subscribe((response) => {
      this.categories = response.content;
    });
  }
  loadTranslations() {
    this.translate.get('PREVIOUS').subscribe((res) => (this.previousLabel = res));
    this.translate.get('NEXT').subscribe((res) => (this.nextLabel = res));
  }

  loadCategories(): void {
    this.categoryService.getCategories(this.page).subscribe((response) => {
      this.categories = response.content;
    });
  }

  onPageChange(page: number): void {
    this.page = page;
    this.loadCategories();
  }
  editCategory(category: Category): void {
    this.translate.get(['EDIT_CATEGORY_TITLE', 'CANCEL', 'SAVE']).subscribe(translations => {
      const dialogRef = this.dialog.open(CategoryDialogComponent, {
        data: { category },
        width: '400px'
      });

      dialogRef.afterClosed().subscribe(result => {
        if (result) {
          const updatedCategory: Category = { ...category, ...result };
          this.categoryService.updateCategory(category.id, updatedCategory.name).subscribe(
            () => {
              this.translate.get('EDIT_SUCCESS_MESSAGE').subscribe(successMessage => {
                this.snackBar.open(successMessage, '', { duration: 3000 });
              });
              this.loadCategories(); // Tải lại danh sách danh mục
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


  deleteCategory(id: number, name: string): void {
    this.translate.get(['DELETE_CONFIRM_TITLE', 'DELETE_CONFIRM_MESSAGE', 'YES', 'NO']).subscribe((translations) => {
      const dialogRef = this.dialog.open(ConfirmationDialogComponent, {
        data: {
          title: translations['DELETE_CONFIRM_TITLE'],
          message: `${translations['DELETE_CONFIRM_MESSAGE']} ${name}?`,
          confirmText: translations['YES'],
          cancelText: translations['NO'],
        },
      });

      dialogRef.afterClosed().subscribe((result) => {
        if (result === true) {
          this.categoryService.deleteCategory(id).subscribe(() => {
            this.translate.get('DELETE_SUCCESS_MESSAGE').subscribe((msg) => {
              this.snackBar.open(msg, '', { duration: 3000 });
              this.loadCategories();
            });
          });
        }
      });
    });
  }
}

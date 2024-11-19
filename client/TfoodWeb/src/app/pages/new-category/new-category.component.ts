import { HttpErrorResponse } from '@angular/common/http';
import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { Category } from 'src/app/models/category';
import { CategoryService } from 'src/app/services/category.service';

@Component({
  selector: 'app-new-category',
  templateUrl: './new-category.component.html',
  styleUrls: ['./new-category.component.scss']
})
export class NewCategoryComponent {
  categoryname: string = '';

  constructor(
    private translate: TranslateService,
    private categoryService: CategoryService,
    private router: Router
  ) { }

  addCategory(): void {
    this.categoryService.createCategory(this.categoryname).subscribe({
      next: () => {
        this.translate.get('CATEGORY_ADDED_SUCCESS').subscribe((msg) => {
          alert(msg);
          this.router.navigate(['/categorymanagement']);
        });
      },
      error: (error: HttpErrorResponse) => {
        this.translate.get('CATEGORY_ADD_FAILED').subscribe((msg) => {
          alert(`${msg}: ${error.message}`);
        });
      },
    });
  }
}

import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Category } from 'src/app/models/category';

@Component({
  selector: 'app-category-dialog',
  templateUrl: './category-dialog.component.html',
  styleUrls: ['./category-dialog.component.scss']
})
export class CategoryDialogComponent {
  category: Category;

  constructor(
    public dialogRef: MatDialogRef<CategoryDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { category: Category }
  ) {
    this.category = { ...data.category };
  }

  // Hàm đóng dialog mà không trả kết quả
  onCancel(): void {
    this.dialogRef.close();
  }

  // Hàm đóng dialog và trả kết quả (danh mục đã sửa)
  onSave(): void {
    this.dialogRef.close(this.category);
  }
}

import { Component, Inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-edit-branch-dialog',
  templateUrl: './edit-branch-dialog.component.html',
  styleUrls: ['./edit-branch-dialog.component.scss']
})
export class EditBranchDialogComponent {
  editBranchForm: FormGroup;

  constructor(
    public dialogRef: MatDialogRef<EditBranchDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private fb: FormBuilder
  ) {
    this.editBranchForm = this.fb.group({
      name: [data.branch.name, Validators.required],
      address: [data.branch.address, Validators.required]
    });
  }

  save(): void {
    if (this.editBranchForm.valid) {
      this.dialogRef.close(this.editBranchForm.value);
    }
  }

  cancel(): void {
    this.dialogRef.close(null);
  }
}

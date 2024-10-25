import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { User } from 'src/app/models/user';
import { AuthenticationService } from 'src/app/services/authentication.service';
import { UserService } from 'src/app/services/user.service';
import { MatDialog } from '@angular/material/dialog';
import { AvatarUploadDialogComponent } from 'src/app/components/avatar-upload-dialog/avatar-upload-dialog.component';
@Component({
  selector: 'app-myinfo',
  templateUrl: './myinfo.component.html',
  styleUrls: ['./myinfo.component.scss']
})
export class MyinfoComponent {
  user: User = new User();

  constructor(private userService: UserService, public translate: TranslateService, private dialog: MatDialog) { }

  ngOnInit(): void {
    this.getUserInfo();
  }

  // Lấy thông tin người dùng từ UserService
  getUserInfo(): void {
    this.userService.getUserInfo().subscribe({
      next: (data) => {
        this.user = data;

      },
      error: (err) => {
        console.error('Failed to load user information', err);
      }
    });
  }
  openAvatarDialog() {
    const dialogRef = this.dialog.open(AvatarUploadDialogComponent, {
      width: '400px',
      height: 'auto',
      disableClose: true,
      data: { avatarUrl: this.user.avatarUrl } // Pass current avatar URL if needed
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.user.avatarUrl = result;
        // Trigger API call to update the avatar on the server
      }
    });
  }

  // Lưu thông tin người dùng thông qua UserService
  // saveUserInfo(): void {
  //   this.userService.saveUserInfo(this.user).subscribe({
  //     next: () => {
  //       alert('User information saved successfully.');
  //     },
  //     error: (err) => {
  //       console.error('Failed to save user information', err);
  //     }
  //   });
  // }
}

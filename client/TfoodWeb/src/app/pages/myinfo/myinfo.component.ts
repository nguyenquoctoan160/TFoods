import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { User } from 'src/app/models/user';
import { AuthenticationService } from 'src/app/services/authentication.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-myinfo',
  templateUrl: './myinfo.component.html',
  styleUrls: ['./myinfo.component.scss']
})
export class MyinfoComponent {
  user: User = new User();

  constructor(private userService: UserService) { }

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

import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { User } from 'src/app/models/user';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private baseUrl = 'http://localhost:3040/api'; // Biến toàn cục chứa URL API

  constructor(private http: HttpClient) { }



  // Lấy thông tin người dùng hiện tại
  getUserInfo(): Observable<User> {
    // Gọi hàm để lấy header chứa token
    return this.http.get<User>(`${this.baseUrl}/users/myinfo`, { withCredentials: true });
  }

  // // Lưu thông tin người dùng sau khi chỉnh sửa
  // saveUserInfo(user: User): Observable<User> {
  //   return this.http.put<User>(`${this.baseUrl}/users/${user.id}`, user);
  // }
}

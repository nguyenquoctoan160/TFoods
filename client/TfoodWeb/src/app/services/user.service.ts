import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { User } from 'src/app/models/user';
import { Observable } from 'rxjs';
import { environment } from 'src/app/environment/environment';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private baseUrl = environment.baseUrl; // Biến toàn cục chứa URL API

  constructor(private http: HttpClient) { }



  // Lấy thông tin người dùng hiện tại
  getUserInfo(): Observable<User> {
    // Gọi hàm để lấy header chứa token
    return this.http.get<User>(`${this.baseUrl}/users/myinfo`, { withCredentials: true });
  }


}

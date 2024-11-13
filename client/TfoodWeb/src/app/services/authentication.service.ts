import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { Router } from '@angular/router';
import { Loginresponse } from 'src/app/models/loginresponse';
import { CookieService } from 'ngx-cookie-service';
import { LoginUser } from 'src/app/models/login-user';
import { RegisterUser } from 'src/app/models/register-user';
import { environment } from 'src/app/environment/environment';
@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {
  private baseUrl = environment.baseUrl; // Change URL if needed
  private token: string | null = localStorage.getItem('token');
  private isLoggedInSubject = new BehaviorSubject<boolean>(this.token ? true : false); // BehaviorSubject for login status
  isLogin$ = this.isLoggedInSubject.asObservable(); // Expose as observable

  constructor(private http: HttpClient, private router: Router, private cookieService: CookieService) { }

  register(user: LoginUser): Observable<any> {
    return this.http.post(`${this.baseUrl}/users/register`, user);
  }

  login(user: RegisterUser): void {
    this.http.post<Loginresponse>(`${this.baseUrl}/users/login`, user).subscribe({
      next: (response) => {
        console.log(response);
        const { token, username, id } = response; // Destructure the response
        if (token && username) {
          this.setSession(token, username, id || "");
          this.router.navigate(['/']); // Navigate to the home page
        } else {
          console.error('Token not found in response');
          alert('Login failed. Please try again.');
        }
      },
      error: (err) => {
        alert(err.error?.error || 'An error occurred');
      }
    });
  }

  logout(): void {
    this.clearSession();
    this.router.navigate(['/login']); // Navigate to the login page
  }

  private setSession(token: string, username: string, userid: string): void {
    this.cookieService.set('JWtoken', token, 7, '/');
    localStorage.setItem('username', username);
    localStorage.setItem('id', userid)

    this.isLoggedInSubject.next(true); // Update login status
  }

  private clearSession(): void {
    this.cookieService.delete('JWtoken', '/');
    localStorage.removeItem('username');
    localStorage.removeItem('id');
    this.isLoggedInSubject.next(false); // Update login status
  }
}

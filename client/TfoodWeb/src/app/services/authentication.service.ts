import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { Router } from '@angular/router';
import { Loginresponse } from 'src/app/models/loginresponse';
@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {
  private baseUrl = 'http://localhost:3040/api'; // Change URL if needed
  private token: string | null = localStorage.getItem('token');
  private isLoggedInSubject = new BehaviorSubject<boolean>(this.token ? true : false); // BehaviorSubject for login status
  isLogin$ = this.isLoggedInSubject.asObservable(); // Expose as observable

  constructor(private http: HttpClient, private router: Router) { }

  register(user: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/users/register`, user);
  }

  login(user: any): void {
    this.http.post<Loginresponse>(`${this.baseUrl}/users/login`, user).subscribe({
      next: (response) => {
        console.log(response);
        const { token, username, imgUrl } = response; // Destructure the response
        if (token && username) {
          this.setSession(token, username, imgUrl || '');
          this.router.navigate(['/']); // Navigate to the home page
        } else {
          console.error('Token not found in response');
          alert('Login failed. Please try again.');
        }
      },
      error: (err) => {
        alert(err.error?.message || 'An error occurred');
      }
    });
  }

  logout(): void {
    this.clearSession();
    this.router.navigate(['/login']); // Navigate to the login page
  }

  private setSession(token: string, username: string, imgUrl: string): void {
    localStorage.setItem('token', token);
    localStorage.setItem('username', username);
    localStorage.setItem('imgUrl', imgUrl);
    this.isLoggedInSubject.next(true); // Update login status
  }

  private clearSession(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('username');
    localStorage.removeItem('imgUrl');
    this.isLoggedInSubject.next(false); // Update login status
  }
}

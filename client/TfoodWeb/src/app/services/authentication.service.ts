import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { Router } from '@angular/router';
import { Loginresponse } from 'src/app/models/loginresponse';
import { CookieService } from 'ngx-cookie-service';
import { LoginUser } from 'src/app/models/login-user';
import { RegisterUser } from 'src/app/models/register-user';
import { environment } from 'src/app/environment/environment';
import { TranslateService } from '@ngx-translate/core';
@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {
  private baseUrl = environment.baseUrl; // Change URL if needed
  private token: string | null = localStorage.getItem('token');
  private isLoggedInSubject = new BehaviorSubject<boolean>(this.token ? true : false); // BehaviorSubject for login status
  isLogin$ = this.isLoggedInSubject.asObservable(); // Expose as observable

  constructor(private http: HttpClient, private router: Router, private translate: TranslateService, private cookieService: CookieService) { }

  register(user: LoginUser): Observable<any> {
    return this.http.post(`${this.baseUrl}/users/register`, user, { responseType: 'text' as 'json' });
  }

  login(user: RegisterUser): void {
    this.http.post<Loginresponse>(`${this.baseUrl}/users/login`, user, { observe: 'response' }).subscribe({
      next: (response) => {
        if (response.status === 200) {
          const { token, username, id, role, admin } = response.body || {}; // Đảm bảo lấy `body` từ `response`
          if (token && username) {
            this.setSession(token, username, id || "", role || "", admin || "false");
            this.router.navigate(['/']); // Navigate to the home page
          } else {
            this.translate.get('LOGIN_FAILED').subscribe((msg) => {
              alert(msg);
            });
          }
        } else {
          this.translate.get('LOGIN_UNKNOWN_STATUS').subscribe((msg) => {
            alert(`${msg}: ${response.status}`);
          });
        }
      },
      error: (err: HttpErrorResponse) => {
        if (err.status === 401) {
          this.translate.get('LOGIN_UNAUTHORIZED').subscribe((msg) => {
            alert(msg);
          });
        } else if (err.status === 500) {
          this.translate.get('LOGIN_SERVER_ERROR').subscribe((msg) => {
            alert(msg);
          });
        } else {
          this.translate.get('LOGIN_UNKNOWN_ERROR').subscribe((msg) => {
            alert(`${msg}: ${err.message}`);
          });
        }
      },
    });
  }


  logout(): void {
    this.clearSession();
    this.router.navigate(['/login']); // Navigate to the login page
  }

  private setSession(token: string, username: string, userid: string, role: string, admin: string): void {
    this.cookieService.set('JWtoken', token, 7, '/');
    localStorage.setItem('username', username);
    localStorage.setItem('id', userid)
    localStorage.setItem('role', role);
    localStorage.setItem('admin', admin);
    this.isLoggedInSubject.next(true); // Update login status
  }

  private clearSession(): void {
    this.cookieService.delete('JWtoken', '/');
    localStorage.removeItem('username');
    localStorage.removeItem('id');
    localStorage.removeItem("role");
    localStorage.removeItem('admin');
    this.isLoggedInSubject.next(false); // Update login status
  }
}

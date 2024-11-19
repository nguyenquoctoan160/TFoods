import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { CookieService } from 'ngx-cookie-service';
import { Subscription } from 'rxjs';
import { environment } from 'src/app/environment/environment';
import { AuthenticationService } from 'src/app/services/authentication.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent {
  isLoggedIn = false;
  userName = '';
  userAvatar = '';
  userid = '';
  role = '';
  admin = false;
  private baseUrl = environment.baseUrl;
  private loginSubscription: Subscription = new Subscription();
  constructor(private router: Router, private authService: AuthenticationService, public translate: TranslateService, private cookieService: CookieService) { }
  userMenuOpen = false;

  ngOnInit(): void {
    this.translate.use(localStorage.getItem('language') || 'en');
    this.reloadUserData();
    this.loginSubscription = this.authService.isLogin$.subscribe(isLoggedIn => {
      this.isLoggedIn = isLoggedIn;
      this.reloadUserData(); // Reload user data when login state changes
    });
  }
  toggleUserMenu() {
    this.userMenuOpen = !this.userMenuOpen;
  }
  private reloadUserData(): void {
    const token = this.cookieService.get("JWtoken");
    if (token) {
      this.isLoggedIn = true;
      this.userName = localStorage.getItem('username') || 'Guest';
      this.userid = localStorage.getItem('id') || '';
      this.role = localStorage.getItem('role') || '';
      this.userAvatar = `${this.baseUrl}/users/avatar/${this.userid}` || 'default-avatar.png';
      this.admin = localStorage.getItem('admin') === 'true' ? true : false;
    } else {
      this.isLoggedIn = false;
      this.userName = 'Guest';
      this.userAvatar = 'default-avatar.png';
    }
  }

  logout(): void {
    this.authService.logout(); // Call the logout method from the AuthenticationService
    window.location.reload(); // Reload the page to update the UI
  }
  switchLanguage(language: string) {
    localStorage.setItem("language", language);
    this.translate.use(language);
    window.location.reload();
  }

}

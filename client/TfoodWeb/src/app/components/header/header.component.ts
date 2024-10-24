import { Component } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { Subscription } from 'rxjs';
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
  private loginSubscription: Subscription = new Subscription();
  constructor(private authService: AuthenticationService, public translate: TranslateService) { }

  ngOnInit(): void {
    this.reloadUserData();
    this.loginSubscription = this.authService.isLogin$.subscribe(isLoggedIn => {
      this.isLoggedIn = isLoggedIn;
      this.reloadUserData(); // Reload user data when login state changes
    });
  }

  private reloadUserData(): void {
    const token = localStorage.getItem('token');
    if (token) {
      this.isLoggedIn = true;
      this.userName = localStorage.getItem('username') || 'Guest';
      this.userAvatar = localStorage.getItem('imgUrl') || 'default-avatar.png';
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
    this.translate.use(language);
  }

}

import { HttpErrorResponse } from '@angular/common/http';
import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { AuthenticationService } from 'src/app/services/authentication.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent {
  registerForm: FormGroup;

  constructor(
    private formBuilder: FormBuilder,
    private authService: AuthenticationService,
    private router: Router,
    private translate: TranslateService
  ) {
    this.registerForm = this.formBuilder.group({
      username: ['', [Validators.required, Validators.minLength(6)]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(8)]],
      repassword: ['', Validators.required]
    });
  }

  onRegister(): void {
    if (this.registerForm.valid) {
      this.authService.register(this.registerForm.value).subscribe({
        next: (response: any) => {


          this.translate.get('USER_REGISTERED_SUCCESS').subscribe((msg) => {
            alert(msg);
            this.router.navigate(['/login']);
          });

        },
        error: (error: HttpErrorResponse) => {
          if (error.status === 400) {
            this.translate.get('USER_REGISTER_BAD_REQUEST').subscribe((msg) => {
              alert(`${msg}: ${error.error}`);
            });
          } else if (error.status === 500) {
            this.translate.get('USER_REGISTER_SERVER_ERROR').subscribe((msg) => {
              alert(msg);
            });
          } else {
            this.translate.get('USER_REGISTER_UNKNOWN_ERROR').subscribe((msg) => {
              alert(`${msg}: ${error.message}`);
            });
          }
        },
      });
    }
  }


}

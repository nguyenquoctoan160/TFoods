import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { RegisterComponent } from 'src/app/pages/register/register.component';
import { AppComponent } from 'src/app/app.component';
import { LoginComponent } from 'src/app/pages/login/login.component';
import { HomeComponent } from 'src/app/pages/home/home.component';
import { authGuard } from 'src/app/security/auth-guard.guard';


const routes: Routes = [{ path: '', component: HomeComponent, canActivate: [authGuard] },
{ path: 'register', component: RegisterComponent },
{ path: "login", component: LoginComponent }];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }

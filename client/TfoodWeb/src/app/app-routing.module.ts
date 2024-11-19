import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { RegisterComponent } from 'src/app/pages/register/register.component';
import { AppComponent } from 'src/app/app.component';
import { LoginComponent } from 'src/app/pages/login/login.component';
import { HomeComponent } from 'src/app/pages/home/home.component';
import { authGuard } from 'src/app/security/auth-guard.guard';
import { MyinfoComponent } from 'src/app/pages/myinfo/myinfo.component';
import { BranchSearchComponent } from 'src/app/pages/branch-search/branch-search.component';
import { NewBranchComponent } from 'src/app/pages/new-branch/new-branch.component';
import { BranchManagementComponent } from 'src/app/pages/branch-management/branch-management.component';
import { sellerGuard } from 'src/app/security/seller.guard';
import { adminGuard } from 'src/app/security/admin.guard';
import { CategoryManagementComponent } from 'src/app/components/category-management/category-management.component';
import { NewCategoryComponent } from 'src/app/pages/new-category/new-category.component';


const routes: Routes = [{ path: '', component: HomeComponent, canActivate: [authGuard] },
{ path: 'register', component: RegisterComponent },
{ path: "login", component: LoginComponent },
{ path: 'branches/search', component: BranchSearchComponent },
{ path: 'branches/my-branches', component: BranchManagementComponent, canActivate: [sellerGuard] },
{ path: 'branches/add', component: NewBranchComponent, canActivate: [sellerGuard] },
{ path: "profile", component: MyinfoComponent, canActivate: [authGuard] },
{ path: "categorymanagement", component: CategoryManagementComponent, canActivate: [adminGuard] },
{ path: "categories/add", component: NewCategoryComponent, canActivate: [adminGuard] }];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }

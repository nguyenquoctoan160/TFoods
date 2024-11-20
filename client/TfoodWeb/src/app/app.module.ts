import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { ReactiveFormsModule } from '@angular/forms';
import { AppRoutingModule } from 'src/app/app-routing.module';
import { AppComponent } from 'src/app/app.component';
import { RegisterComponent } from 'src/app/pages/register/register.component';
import { LoginComponent } from 'src/app/pages/login/login.component';
import { HomeComponent } from 'src/app/pages/home/home.component';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { TranslateLoader, TranslateModule } from '@ngx-translate/core';
import { HeaderComponent } from 'src/app/components/header/header.component';
import { FooterComponent } from 'src/app/components/footer/footer.component';
import { MyinfoComponent } from 'src/app/pages/myinfo/myinfo.component';
import { FormsModule } from '@angular/forms';
import { CookieService } from 'ngx-cookie-service';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { AvatarUploadDialogComponent } from 'src/app/components/avatar-upload-dialog/avatar-upload-dialog.component';
import { MatDialogModule } from '@angular/material/dialog';
import { ImageCropperModule } from 'ngx-image-cropper';
import { BranchSearchComponent } from 'src/app/pages/branch-search/branch-search.component';
import { BranchManagementComponent } from 'src/app/pages/branch-management/branch-management.component';
import { NewBranchComponent } from 'src/app/pages/new-branch/new-branch.component';
import { NgxPaginationModule } from 'ngx-pagination';
import { ConfirmationDialogComponent } from 'src/app/components/confirmation-dialog/confirmation-dialog.component';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { EditBranchDialogComponent } from 'src/app/components/edit-branch-dialog/edit-branch-dialog.component';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';

import { NewCategoryComponent } from 'src/app/pages/new-category/new-category.component';
import { CategoryManagementComponent } from 'src/app/pages/category-management/category-management.component';
import { CategoryDialogComponent } from 'src/app/components/category-dialog/category-dialog.component';

export function HttpLoaderFactory(http: HttpClient) {
  return new TranslateHttpLoader(http, './assets/i18n/', '.json');
}
@NgModule({
  declarations: [
    AppComponent,
    RegisterComponent,
    LoginComponent,
    HomeComponent,
    HeaderComponent,
    FooterComponent,
    MyinfoComponent,
    AvatarUploadDialogComponent,
    BranchSearchComponent,
    BranchManagementComponent,
    NewBranchComponent,
    ConfirmationDialogComponent,
    EditBranchDialogComponent,
    CategoryManagementComponent,
    NewCategoryComponent,
    CategoryDialogComponent
  ],
  imports: [
    ReactiveFormsModule,
    BrowserModule,
    MatDialogModule,
    AppRoutingModule,
    ImageCropperModule,
    HttpClientModule,
    BrowserModule,
    BrowserAnimationsModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    NgxPaginationModule,
    FormsModule,
    MatSnackBarModule,
    TranslateModule.forRoot({
      loader: {
        provide: TranslateLoader,
        useFactory: HttpLoaderFactory,
        deps: [HttpClient]
      }
    }),
    BrowserAnimationsModule
  ],
  providers: [CookieService],
  bootstrap: [AppComponent]
})
export class AppModule { }

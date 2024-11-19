import { inject, Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, CanActivateFn, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { CookieService } from 'ngx-cookie-service';
import { Observable } from 'rxjs';

export const adminGuard: CanActivateFn = () => {
  const router = inject(Router); // Sử dụng inject để thay thế constructor injection
  const cookieService = inject(CookieService); // Sử dụng inject để lấy CookieService
  const token = cookieService.get('JWtoken'); // Lấy token từ cookie
  const isAdmin = localStorage.getItem('admin') || '';
  if (token) {
    if (isAdmin === "true")
      return true; // Cho phép truy cập nếu có token
    else {
      router.navigate(['/']); // Chuyển đến trang đăng nhập nếu không có token
      return false;
    }
  } else {
    router.navigate(['/']); // Chuyển đến trang đăng nhập nếu không có token
    return false;
  }
};

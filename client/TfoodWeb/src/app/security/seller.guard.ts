import { inject, Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, CanActivateFn, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { CookieService } from 'ngx-cookie-service';
import { Observable } from 'rxjs';

export const sellerGuard: CanActivateFn = () => {
  const router = inject(Router); // Sử dụng inject để thay thế constructor injection
  const cookieService = inject(CookieService); // Sử dụng inject để lấy CookieService
  const token = cookieService.get('JWtoken'); // Lấy token từ cookie
  const role = localStorage.getItem('role') || '';
  if (token) {
    if (role === "SELLER")
      return true; // Cho phép truy cập nếu có token
    else {
      router.navigate(['/login']); // Chuyển đến trang đăng nhập nếu không có token
      return false;
    }
  } else {
    router.navigate(['/login']); // Chuyển đến trang đăng nhập nếu không có token
    return false;
  }
};

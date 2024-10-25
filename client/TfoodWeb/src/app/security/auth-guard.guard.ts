import { inject } from '@angular/core';
import { Router, CanActivateFn } from '@angular/router';
import { CookieService } from 'ngx-cookie-service';

export const authGuard: CanActivateFn = () => {
  const router = inject(Router); // Sử dụng inject để thay thế constructor injection
  const cookieService = inject(CookieService); // Sử dụng inject để lấy CookieService
  const token = cookieService.get('JWtoken'); // Lấy token từ cookie

  if (token) {
    return true; // Cho phép truy cập nếu có token
  } else {
    router.navigate(['/login']); // Chuyển đến trang đăng nhập nếu không có token
    return false;
  }
};

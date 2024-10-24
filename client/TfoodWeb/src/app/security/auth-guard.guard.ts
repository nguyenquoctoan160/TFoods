import { inject } from '@angular/core';
import { Router, CanActivateFn } from '@angular/router';

export const authGuard: CanActivateFn = () => {
  const router = inject(Router); // Sử dụng inject để thay thế constructor injection
  const token = localStorage.getItem('token');

  if (token) {
    return true; // Cho phép truy cập nếu có token
  } else {
    router.navigate(['/login']); // Chuyển đến trang đăng nhập nếu không có token
    return false;
  }
};

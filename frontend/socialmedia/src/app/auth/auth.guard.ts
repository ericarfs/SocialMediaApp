import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from './auth';

export const authGuard: CanActivateFn = (route, state) => {
  const auth = inject(AuthService);
  const router = inject(Router);

  if(!auth.isAuthenticated()){
    router.navigate(['/login']);
    return false;
  }
  return true;
};

export const noAuthGuard: CanActivateFn = (route, state) => {
  const auth = inject(AuthService);
  const router = inject(Router);

  if(auth.isAuthenticated()){
    router.navigate(['/home']);
    return false;
  }
  return true;
};

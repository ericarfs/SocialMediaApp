import { inject } from '@angular/core';
import { HttpInterceptorFn, HttpErrorResponse, HttpRequest, HttpHandlerFn } from '@angular/common/http';
import { AuthService } from '../../auth/auth';
import { catchError, switchMap, throwError } from 'rxjs';
import { Router } from '@angular/router';

let isRefreshing = false;

function addTokenHeader(request: HttpRequest<any>, token: string) {
  return request.clone({
    setHeaders: {
      Authorization: `Bearer ${token}`,
    },
  });
}

function handleUnauthorizedError(authService: AuthService, req: HttpRequest<any>, next: HttpHandlerFn, router: Router) {
  if (!isRefreshing) {
    isRefreshing = true;

    return authService.refreshToken().pipe(
      switchMap((res) => {
        isRefreshing = false;

        const newToken = localStorage.getItem('accessToken');
        if (newToken) {
            return next(addTokenHeader(req, newToken));
        } else {
            authService.logout();
            router.navigate(['/login']);
            return throwError(() => new Error('Refresh token failed (no new token).'));
        }
      }),
      catchError((err) => {
        isRefreshing = false;
        authService.logout();
        router.navigate(['/login']);
        return throwError(() => err);
      })
    );
  }

  return throwError(() => new HttpErrorResponse({ status: 401, statusText: 'Unauthorized (refreshing)' }));
}


export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  const token = authService.loadToken();
  const authReq = token ? addTokenHeader(req, token) : req;

  return next(authReq).pipe(
    catchError((error) => {
      if (
          error instanceof HttpErrorResponse &&
          error.status === 401 &&
          !req.url.includes('/auth/token') &&
          !req.url.includes('/auth/token/refresh')
      ) {
        return handleUnauthorizedError(authService, req, next, router);
      }
      return throwError(() => error);
    })
  );
};

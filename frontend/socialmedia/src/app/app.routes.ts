import { Routes } from '@angular/router';
import { WelcomePage } from './welcome-page/welcome-page';
import { Login } from './auth/login/login';
import { Register } from './auth/register/register';
import { Home } from './home/home';
import { authGuard, noAuthGuard } from './auth/auth.guard';
import { Logout } from './auth/logout';
import { ResetPassword } from './auth/reset-password/reset-password';

export const routes: Routes = [
  {
    path: "",
    component: WelcomePage,
    canActivate: [noAuthGuard]
  },
  {
    path: "home",
    component: Home,
    canActivate: [authGuard]
  },
  {
    path: "login",
    component: Login,
    canActivate: [noAuthGuard]
  },
  {
    path: "reset-password",
    component: ResetPassword,
    canActivate: [noAuthGuard]
  },
  {
    path: "register",
    component: Register,
    canActivate: [noAuthGuard]
  },
  {
    path: "logout",
    component: Logout
  }
];

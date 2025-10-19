import { Routes } from '@angular/router';
import { WelcomePage } from './welcome-page/welcome-page';
import { Login } from './auth/login/login';
import { Register } from './auth/register/register';

import { authGuard, noAuthGuard } from './auth/auth.guard';
import { Logout } from './auth/logout/logout';
import { ResetPassword } from './auth/reset-password/reset-password';
import { NewPassword } from './auth/new-password/new-password';
import { Home } from './pages/home/home';
import { Inbox } from './pages/inbox/inbox';
import { Notifications } from './pages/notifications/notifications';
import { Profile } from './pages/profile/profile';
import { Settings } from './pages/settings/settings';

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
    path: "notifications",
    component: Notifications,
    canActivate: [authGuard]
  },
  {
    path: "inbox",
    component: Inbox,
    canActivate: [authGuard]
  },
  {
    path: "profile",
    component: Profile,
    canActivate: [authGuard]
  },
  {
    path: "settings",
    component: Settings,
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
    path: "reset-password/:token",
    component: NewPassword,
    canActivate: [noAuthGuard]
  },
  {
    path: "register",
    component: Register,
    canActivate: [noAuthGuard]
  },
  {
    path: "logout",
    component: Logout,
    canActivate: [authGuard]
  }
];

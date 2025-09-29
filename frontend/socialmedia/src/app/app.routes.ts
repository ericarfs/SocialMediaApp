import { Routes } from '@angular/router';
import { WelcomePage } from './welcome-page/welcome-page';
import { Login } from './auth/login/login';
import { Register } from './auth/register/register';

export const routes: Routes = [
  {
    path: "",
    component: WelcomePage
  },
  {
    path: "login",
    component: Login
  },
  {
    path: "register",
    component: Register
  }
];

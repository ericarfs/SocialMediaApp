import { AuthService } from './../auth';
import { Component, inject } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Layout } from '../components/layout/layout';
import { InputComponent } from '../components/input/input';
import { Router, RouterLink } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';

interface LoginForm {
  username: FormControl,
  password: FormControl
}

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    Layout,
    ReactiveFormsModule,
    InputComponent,
    RouterLink,
],
  providers: [
  ],
  templateUrl: './login.html',
  styleUrl: './login.scss'
})
export class Login {
  loginForm!: FormGroup<LoginForm>;

  passwordType = 'password';
  showPassword: boolean = false;

  private snackBar = inject(MatSnackBar);

  constructor(
    private authService: AuthService,
    private router: Router
  ){
    this.loginForm = new FormGroup({
      username: new FormControl('', [Validators.required, Validators.minLength(4)]),
      password: new FormControl('', [Validators.required, Validators.minLength(5)])
    })
  }

  togglePasswordVisibility(){
    if (this.passwordType === 'password') {
      this.passwordType = 'text';
      this.showPassword = true;
    } else {
      this.passwordType = 'password';
      this.showPassword = false;
    }
  }

  submit(){
    this.authService
      .login(
        this.loginForm.value.username,
        this.loginForm.value.password)
      .subscribe({
        next: () => this.router.navigate(["home"]),
        error: (err) => {
          if(err.status === 401){
            this.snackBar.open('Username or Password incorrect!','',{
              duration:3000,
              horizontalPosition: 'center',
              verticalPosition: 'top',
              panelClass:['snackbar-error']
            })
          }
          else{
            this.snackBar.open('Failed to login!','',{
              duration:3000,
              horizontalPosition: 'center',
              verticalPosition: 'top',
              panelClass:['snackbar-error']
            })
          }
        }
      })
  }

  navigate(){
    this.router.navigate(["register"])
  }
}

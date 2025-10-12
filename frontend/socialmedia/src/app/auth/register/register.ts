import { Component, inject } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Layout } from '../components/layout/layout';
import { InputComponent } from '../components/input/input';
import { Router } from '@angular/router';
import { AuthService } from '../auth';
import { MatSnackBar } from '@angular/material/snack-bar';
import { FormControlError } from '../components/form-control-error';
import { matchPasswordValidator } from './match-password.validator';

interface RegisterForm {
  username: FormControl,
  email: FormControl,
  password: FormControl,
  confirmPassword: FormControl
}

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [
    Layout,
    ReactiveFormsModule,
    InputComponent,
    FormControlError,
],
  providers: [
  ],
  templateUrl: './register.html',
  styleUrl: './register.scss'
})
export class Register {
  registerForm!: FormGroup<RegisterForm>;

  passwordType = 'password';
  showPassword: boolean = false;
  confirmPasswordType = 'password';
  showConfirmPassword: boolean = false;

  private snackBar = inject(MatSnackBar);

  constructor(
    private authService: AuthService,
    private router: Router
  ){
    this.registerForm = new FormGroup({
      email: new FormControl('', [Validators.required, Validators.email]),
      username: new FormControl('', [Validators.required, Validators.minLength(4), Validators.maxLength(20)]),
      password: new FormControl('', [Validators.required, Validators.minLength(8)]),
      confirmPassword: new FormControl('', [Validators.required, Validators.minLength(8)])
    },
    {
      validators: [
        matchPasswordValidator('password', 'confirmPassword')
      ]
    });
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

  toggleConfirmPasswordVisibility(){
    if (this.confirmPasswordType === 'password') {
      this.confirmPasswordType = 'text';
      this.showConfirmPassword = true;
    } else {
      this.confirmPasswordType = 'password';
      this.showConfirmPassword = false;
    }
  }

  submit(){
    this.authService
      .register(
        this.registerForm.value.email,
        this.registerForm.value.username,
        this.registerForm.value.password)
      .subscribe({
        next: () => {
          this.snackBar.open("Account created successfully.",'',{
              duration: 1000,
              horizontalPosition: 'center',
              verticalPosition: 'top',
              panelClass:['snackbar-success']
          })
          this.authService
            .login(
              this.registerForm.value.username,
              this.registerForm.value.password)
            .subscribe({
              next: () => this.router.navigate(["home"])
            });
        },
        error: (err) => {
          if (err.status === 409){
            this.snackBar.open(err.error.message,'',{
              duration: 2000,
              horizontalPosition: 'center',
              verticalPosition: 'top',
              panelClass:['snackbar-error']
            })
          }
          else{
            this.snackBar.open("Failed to create an account.",'',{
              duration: 2000,
              horizontalPosition: 'center',
              verticalPosition: 'top',
              panelClass:['snackbar-error']
            })
          }
        }
      })
  }

  navigate(){
    this.router.navigate(["login"])
  }
}

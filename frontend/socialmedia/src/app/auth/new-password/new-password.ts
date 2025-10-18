import { Component, inject } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Layout } from '../components/layout/layout';
import { InputComponent } from '../components/input/input';
import { FormControlError } from '../components/form-control-error';
import { MatSnackBar } from '@angular/material/snack-bar';
import { AuthService } from '../auth';
import { ActivatedRoute, Router } from '@angular/router';
import { matchPasswordValidator } from '../register/match-password.validator';

interface NewPasswordForm {
  password: FormControl,
  confirmPassword: FormControl
}

@Component({
  selector: 'app-new-password',
  imports: [
    Layout,
    ReactiveFormsModule,
    InputComponent,
    FormControlError,
  ],
  templateUrl: './new-password.html',
  styleUrl: './new-password.scss'
})
export class NewPassword {
  token!: string;
  newPasswordForm!: FormGroup<NewPasswordForm>;

  passwordType = 'password';
  showPassword: boolean = false;
  confirmPasswordType = 'password';
  showConfirmPassword: boolean = false;

  private snackBar = inject(MatSnackBar);

  constructor(
    private authService: AuthService,
    private router: Router,
    private route: ActivatedRoute
  ){
    this.newPasswordForm = new FormGroup({
      password: new FormControl('', [Validators.required, Validators.minLength(8)]),
      confirmPassword: new FormControl('', [Validators.required, Validators.minLength(8)])
    },
    {
      validators: [
        matchPasswordValidator('password', 'confirmPassword')
      ]
    });
  }

  ngOnInit() {
    this.token = this.route.snapshot.paramMap.get('token')!;
    this.authService
      .checkResetToken(this.token)
      .subscribe({
        error: () => {
          this.snackBar.open("Invalid token.",'',{
            duration: 1000,
            horizontalPosition: 'center',
            verticalPosition: 'top',
            panelClass:['snackbar-error']
          })
          this.router.navigate(["reset-password"])
        }
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
      .resetPassword(
        this.token,
        this.newPasswordForm.value.password)
      .subscribe({
        next: () => {
          this.snackBar.open("Password updated successfully.",'',{
              duration: 1000,
              horizontalPosition: 'center',
              verticalPosition: 'top',
              panelClass:['snackbar-success']
          })
          this.router.navigate(["login"])
        },
        error: () => {
          this.snackBar.open("Failed to update your password.",'',{
            duration: 2000,
            horizontalPosition: 'center',
            verticalPosition: 'top',
            panelClass:['snackbar-error']
          })
        }
      })
  }
}

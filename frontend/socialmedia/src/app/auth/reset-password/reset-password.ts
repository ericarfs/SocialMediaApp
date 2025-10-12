import { AuthService } from './../auth';
import { Component, inject } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Layout } from '../components/layout/layout';
import { InputComponent } from '../components/input/input';
import { Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';
import { FormControlError } from '../components/form-control-error';

interface ResetPasswordForm {
  email: FormControl,
}

@Component({
  selector: 'app-reset-password',
  standalone: true,
  imports: [
    Layout,
    ReactiveFormsModule,
    InputComponent,
    FormControlError,
],
  providers: [
  ],
  templateUrl: './reset-password.html',
  styleUrl: './reset-password.scss'
})
export class ResetPassword {
  resetPasswordForm!: FormGroup<ResetPasswordForm>;

  private snackBar = inject(MatSnackBar);

  constructor(
    private authService: AuthService,
    private router: Router
  ){
    this.resetPasswordForm = new FormGroup({
      email: new FormControl('', [Validators.required, Validators.email])
    })
  }

  submit(){
    console.log("Reset Password")
  }
}

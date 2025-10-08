import { Component } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Layout } from '../components/layout/layout';
import { InputComponent } from '../components/input/input';
import { Router } from '@angular/router';

interface RegisterForm {
  username: FormControl,
  email: FormControl,
  password: FormControl
}

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [
    Layout,
    ReactiveFormsModule,
    InputComponent,
],
  providers: [
  ],
  templateUrl: './register.html',
  styleUrl: './register.scss'
})
export class Register {
  registerForm!: FormGroup<RegisterForm>;

  constructor(
    private router: Router
  ){
    this.registerForm = new FormGroup({
      email: new FormControl('', [Validators.required, Validators.email]),
      username: new FormControl('', [Validators.required, Validators.minLength(4)]),
      password: new FormControl('', [Validators.required, Validators.minLength(8)])
    })
  }

  submit(){
    console.log("Login realizado com sucesso!")
  }

  navigate(){
    this.router.navigate(["register"])
  }
}

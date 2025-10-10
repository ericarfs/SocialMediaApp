import { Component, OnInit } from '@angular/core';
import { AuthService } from './auth';


@Component({
  template: ''
})
export class Logout implements OnInit {
  constructor(private authService: AuthService) {}
  ngOnInit(): void {
    this.authService.logout();
  }
}

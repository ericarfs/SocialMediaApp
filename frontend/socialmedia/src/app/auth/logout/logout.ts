import { Component } from '@angular/core';
import { AuthService } from '../auth';
import { Location } from '@angular/common';

@Component({
  selector: 'app-logout',
  standalone: true,
  imports: [
],
  providers: [
  ],
  templateUrl: './logout.html',
  styleUrl: './logout.scss'
})
export class Logout {
  constructor(
    private authService: AuthService,
    private location: Location
  ) {}

  confirmLogout(): void {
    this.authService.logout();
    window.location.reload();
  }

  cancelLogout(): void {
    this.location.back();
  }
}

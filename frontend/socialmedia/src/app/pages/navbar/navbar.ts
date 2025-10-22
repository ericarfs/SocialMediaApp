import { NgClass } from '@angular/common';
import { Component } from '@angular/core';
import { RouterLink, Router } from '@angular/router';

@Component({
  selector: 'app-navbar',
  imports: [RouterLink, NgClass],
  templateUrl: './navbar.html',
  styleUrl: './navbar.scss'
})

export class Navbar {
  url!: string;
  title!: string;
  active = false;
  items = ['Home', 'Notifications', 'Inbox', 'Profile', 'Settings']

  constructor(private router: Router) {}

  ngOnInit() {
    this.url = this.router.url;
    this.title = this.formatTitle(this.url.split('/')[1]);
  }

  formatTitle(url:string){
    return url[0].toUpperCase() + url.slice(1);
  }
}

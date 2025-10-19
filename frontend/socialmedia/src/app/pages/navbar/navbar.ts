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
  active = false;
  constructor(private router: Router) {}

  ngOnInit() {
    this.url = this.router.url;
    console.log(this.url)
  }
}

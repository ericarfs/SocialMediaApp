import { Component } from '@angular/core';
import { Navbar } from '../navbar/navbar';
import { RouterLink } from "@angular/router";

@Component({
  selector: 'app-pages-layout',
  imports: [Navbar, RouterLink],
  templateUrl: './layout.html',
  styleUrl: './layout.scss'
})
export class Layout {

}

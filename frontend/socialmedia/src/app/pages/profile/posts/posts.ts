import { Component, OnInit } from '@angular/core';
import { FeedTemplate } from '../../feed-template/feed-template';
import { Router } from '@angular/router';
import { AuthService } from '../../../auth/auth';

@Component({
  selector: 'app-profile-posts',
  imports: [FeedTemplate],
  templateUrl: './posts.html',
  styleUrl: './posts.scss',
  host:{
    class: 'block w-full'
  }
})
export class Posts implements OnInit{
  url!: string;
  currentUser!: string;

  baseUrl = '';
  noContentMessage = "This user hasn't answered any questions yet.";
  emptyHintMessage = "Be the first one to ask anything!";

  constructor(
    private router: Router,
    private authService: AuthService
  ) { }


  ngOnInit(){
    this.authService.currentUser$.subscribe(username => {
      if (username)
        this.currentUser = username;

      this.url = this.router.url;
      let urlParts = this.url.split('/');

      let userToCheck = this.currentUser;

      if(urlParts.length > 2)
        userToCheck = urlParts[urlParts.length - 1]

      this.baseUrl = `http://localhost:8080/api/users/${userToCheck}/activities`
      }
    );
  }
}

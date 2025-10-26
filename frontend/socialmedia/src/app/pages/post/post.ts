import { Component, input, OnInit } from '@angular/core';
import { AnswerResponse } from '../types/answer-response';
import { RouterLink } from '@angular/router';
import { NgClass } from '@angular/common';
import { PostService } from './post.service';
import { AuthService } from '../../auth/auth';

@Component({
  selector: 'app-post',
  imports: [RouterLink, NgClass],
  templateUrl: './post.html',
  styleUrl: './post.scss',
  host:{
    class: 'block w-[98%]'
  }
})
export class Post implements OnInit {
  answer = input<AnswerResponse>();
  currentUser: string | null = null;

  isLiking = false;
  isSharing = false;

  constructor(
    private postService: PostService,
    private authService: AuthService
  ) {}

  ngOnInit() {
    this.authService.currentUser$.subscribe(user => {
      this.currentUser = user;
    });
  }

  authorIsCurrentUser(): boolean{
    return this.answer()?.author.username === this.currentUser;
  }

  toggleLike() {
    const a = this.answer();
    if (!a || this.isLiking) return;

    this.isLiking = true;
    const old = { ...a.likesInfo };

    a.likesInfo.hasUserLiked = !a.likesInfo.hasUserLiked;
    a.likesInfo.likesCount += a.likesInfo.hasUserLiked ? 1 : -1;

    this.postService.toggleLike(a.id).subscribe({
      next: () => {
        this.isLiking = false;
      },
      error: () => {
        a.likesInfo = old;
        this.isLiking = false;
      }
    });
  }

  toggleShare() {
    const a = this.answer();
    if (!a || this.isSharing) return;

    this.isSharing = true;
    const old = { ...a.sharesInfo };

    a.sharesInfo.hasUserShared = !a.sharesInfo.hasUserShared;
    a.sharesInfo.sharesCount += a.sharesInfo.hasUserShared ? 1 : -1;

    this.postService.toggleShare(a.id).subscribe({
      next: () => {
        this.isSharing = false;
      },
      error: () => {
        a.sharesInfo = old;
        this.isSharing = false;
      }
    });
  }
}

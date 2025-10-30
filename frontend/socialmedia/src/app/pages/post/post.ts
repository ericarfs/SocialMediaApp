import { Component, inject, input, OnInit, output, signal } from '@angular/core';
import { AnswerBasic, AnswerResponse } from '../types/answer-response';
import { RouterLink } from '@angular/router';
import { NgClass } from '@angular/common';
import { PostService } from './post.service';
import { AuthService } from '../../auth/auth';
import { MatDialog } from '@angular/material/dialog';
import { ConfirmDialog } from '../confirm-dialog/confirm-dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';

interface ReplyForm {
  body: FormControl,
}

interface QuestionForm {
  body: FormControl,
  anon: FormControl
}

@Component({
  selector: 'app-post',
  imports: [
    RouterLink,
    ReactiveFormsModule,
    NgClass],
  templateUrl: './post.html',
  styleUrl: './post.scss',
  host:{
    class: 'block w-[98%]'
  }
})
export class Post implements OnInit {
  replyForm!: FormGroup<ReplyForm>;
  questionForm!: FormGroup<QuestionForm>;

  relatedAnswers: AnswerBasic[] | [] = [];

  currentUser: string | null = null;

  answer = input<AnswerResponse>();
  isSelectedEdit = input<boolean>();
  isSelectedReply = input<boolean>();
  isSelectedThread = input<boolean>();

  isLiking = false;
  isSharing = false;
  isDeleting = false;
  isUpdating = false;
  isReplying = false;

  replyClicked = output<void>();
  deleteClicked = output<void>();
  editClicked = output<void>();
  cancelClicked = output<void>();
  editedClicked = output<string>();
  threadClicked = output<void>();

  readonly MAX_LENGTH_ANSWER = 2048;
  readonly MIN_LENGTH_ANSWER = 4;
  readonly initialBodyValue = this.answer()?.body || '';

  readonly MAX_LENGTH_QUESTION = 1024;
  readonly MIN_LENGTH_QUESTION = 4;

  private snackBar = inject(MatSnackBar);

  constructor(
    private postService: PostService,
    private authService: AuthService,
    private dialog: MatDialog
  ) {
    this.replyForm = new FormGroup({
      body: new FormControl('', [Validators.required, Validators.minLength(this.MIN_LENGTH_ANSWER), Validators.maxLength(this.MAX_LENGTH_ANSWER)])
    })

    this.questionForm = new FormGroup({
      body: new FormControl('', [Validators.required, Validators.minLength(this.MIN_LENGTH_QUESTION), Validators.maxLength(this.MAX_LENGTH_QUESTION)]),
      anon: new FormControl()
    })
  }

  ngOnInit() {
    this.authService.currentUser$.subscribe(user => {
      this.currentUser = user;
    });

    if (this.answer()?.body) {
      this.replyForm.get('body')?.setValue(this.answer()!.body);
    }

    if (this.authorIsCurrentUser()) {
        this.questionForm.get('anon')?.disable();
    } else {
        this.questionForm.get('anon')?.patchValue(true);
    }

    this.getRelatedAnswers();
  }

  authorIsCurrentUser(): boolean {
    return this.answer()?.author.username === this.currentUser;
  }

  getNumberOfCharactersAnswer(): number {
    return this.replyForm.controls.body.value?.length || 0;
  }

  getNumberOfCharactersQuestion(): number {
    return this.questionForm.controls.body.value?.length || 0;
  }

  getRelatedAnswers(){
    const relatedList: AnswerBasic[] = [];
    let currentAnswer: AnswerResponse | AnswerBasic | undefined = this.answer();

    while(currentAnswer && currentAnswer.inResponseTo){
      relatedList.push(currentAnswer.inResponseTo);

      currentAnswer = currentAnswer.inResponseTo;
    }
    relatedList.reverse();
    this.relatedAnswers = relatedList;
  }

  showRelatedAnswers(){
    this.threadClicked.emit();
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

  onReplyClick() {
    this.replyClicked.emit();
  }

  onEditClick() {
    this.editClicked.emit();
  }

  onCancelClick() {
    this.cancelClicked.emit();
  }

  sendQuestion(id: any){
    this.isReplying = true;
    this.postService.sendQuestion(id, this.questionForm.controls.body.value, this.questionForm.controls.anon.value)
      .subscribe({
        next: () => {
          this.snackBar.open("Reply sent successfully.",'',{
            duration: 1000,
            horizontalPosition: 'center',
            verticalPosition: 'top',
            panelClass:['snackbar-success']
        })
        this.editedClicked.emit(this.replyForm.controls.body.value);
        },
        error: () => {
          this.snackBar.open("Failed to send the reply. Try again later.",'',{
            duration: 2000,
            horizontalPosition: 'center',
            verticalPosition: 'top',
            panelClass:['snackbar-error']
          })
        }
      })
    this.isReplying = false;
  }

  sendEditedAnswer(id: any){
    this.isUpdating = true;
    this.postService.update(id, this.replyForm.controls.body.value)
      .subscribe({
        next: () => {
          this.snackBar.open("Answer updated successfully.",'',{
            duration: 1000,
            horizontalPosition: 'center',
            verticalPosition: 'top',
            panelClass:['snackbar-success']
        })
        this.editedClicked.emit(this.replyForm.controls.body.value);
        },
        error: () => {
          this.snackBar.open("Failed to update the answer. Try again later.",'',{
            duration: 2000,
            horizontalPosition: 'center',
            verticalPosition: 'top',
            panelClass:['snackbar-error']
          })
        }
      })
    this.isUpdating = false;
  }

  onDeleteClick(id: any) {
    const dialogRef = this.dialog.open(ConfirmDialog, {
      position:{
        top: '10vh'
      },
      data: { title: 'Are you sure you want to delete this question?' },
      backdropClass: 'darker-backdrop'
    });
    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.isDeleting = true;
        this.postService.delete(id)
        .subscribe({
          next: () => {
            this.snackBar.open("Question deleted successfully.",'',{
                duration: 1000,
                horizontalPosition: 'center',
                verticalPosition: 'top',
                panelClass:['snackbar-success']
            })
            this.deleteClicked.emit();
          },
          error: () => {
            this.snackBar.open("Failed to delete the question. Try again later.",'',{
                duration: 2000,
                horizontalPosition: 'center',
                verticalPosition: 'top',
                panelClass:['snackbar-error']
            })
          }
        })
        this.isDeleting = false;
      }

    });
  }
}

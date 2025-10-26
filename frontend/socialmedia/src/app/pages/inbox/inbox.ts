import { AfterViewInit, Component, DestroyRef, ElementRef, inject, OnInit, Signal, ViewChild } from '@angular/core';
import { Layout } from '../layout/layout';
import { InboxService } from './inbox.service';
import { Question } from "../question/question";
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatDialog } from '@angular/material/dialog';
import { ConfirmDialog } from '../confirm-dialog/confirm-dialog';
import { QuestionResponse } from '../types/question-response';

@Component({
  selector: 'app-inbox',
  imports: [Layout, Question],
  templateUrl: './inbox.html',
  styleUrl: './inbox.scss'
})
export class Inbox implements OnInit, AfterViewInit {
  readonly questions!: Signal<any[]>;
  readonly loading!: Signal<boolean>;

  isDeleting = false;
  isReplying = false;
  isSelected = false;

  currentQuestion?: QuestionResponse;

  @ViewChild('scrollAnchor', { static: false }) scrollAnchor!: ElementRef;

  private observer!: IntersectionObserver;

  private snackBar = inject(MatSnackBar);

  constructor(
    private inboxService: InboxService,
    private destroyRef: DestroyRef,
    private dialog: MatDialog
  ) {
    this.questions = this.inboxService.questions$;
    this.loading = this.inboxService.loading$;
  }

  ngOnInit(): void {
    this.inboxService.resetFeed();
    this.inboxService.loadNextPage();
  }

  ngAfterViewInit(): void {
    this.observer = new IntersectionObserver(
      (entries) => {
        if (entries.some((e) => e.isIntersecting)) {
          this.inboxService.loadNextPage();
        }
      },
      { rootMargin: '100px' }
    );

    if (this.scrollAnchor) {
      this.observer.observe(this.scrollAnchor.nativeElement);
    }
  }

  ngOnDestroy(): void {
    this.observer?.disconnect();
  }

  answerQuestion(id: number){
    this.isSelected = true;
    this.currentQuestion = this.questions().find(q => q.id === id);
  }

  sendAnswer(body: string, id: number){
    this.isReplying = true;
    this.inboxService.sendAnswer(body, id)
    .subscribe({
      next: () => {
        this.snackBar.open("Question answered successfully.",'',{
            duration: 1000,
            horizontalPosition: 'center',
            verticalPosition: 'top',
            panelClass:['snackbar-success']
        })
        this.cleanReply();
      },
      error: () => {
        this.snackBar.open("Failed to send answer. Try again later.",'',{
            duration: 2000,
            horizontalPosition: 'center',
            verticalPosition: 'top',
            panelClass:['snackbar-error']
        })
      }
    })
    this.isReplying = false;
  }

  cleanReply(){
    this.isSelected = false;
    this.currentQuestion = undefined;
  }

  deleteQuestion(id: number){
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
        this.inboxService.delete(id)
        .subscribe({
          next: () => {
            this.snackBar.open("Question deleted successfully.",'',{
                duration: 1000,
                horizontalPosition: 'center',
                verticalPosition: 'top',
                panelClass:['snackbar-success']
            })
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

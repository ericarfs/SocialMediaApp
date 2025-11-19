import { UserProfile, UserResponse } from './../../types/user-response';
import { Component, inject, OnInit, Signal } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../../auth/auth';
import { UserService } from './user-info.service';
import { NgClass } from '@angular/common';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatDialog } from '@angular/material/dialog';
import { ConfirmDialog } from '../../confirm-dialog/confirm-dialog';

interface QuestionForm {
  body: FormControl,
  anon: FormControl
}

@Component({
  selector: 'app-user-info',
  imports: [
    NgClass,
    ReactiveFormsModule,
    RouterLink],
  templateUrl: './user-info.html',
  styleUrl: './user-info.scss',
  host: {
    class: "flex flex-col items-center gap-1 w-2xl max-w-full mx-auto"
  }
})
export class UserInfo implements OnInit{
  url!: string;
  currentUser!: string;
  loadedUser!: Signal<UserProfile | null>;

  isLoggedUser: boolean = false;
  isSending: boolean = false;
  questionClicked: boolean = false;

  questionForm!: FormGroup<QuestionForm>;

  readonly MAX_LENGTH_QUESTION = 1024;
  readonly MIN_LENGTH_QUESTION = 4;

  private snackBar = inject(MatSnackBar);

  constructor(
    private router: Router,
    private authService: AuthService,
    private userService: UserService,
    private dialog: MatDialog
  ) {
    this.loadedUser = this.userService.loadedUserSignal;
    this.questionForm = new FormGroup({
      body: new FormControl('', [Validators.required, Validators.minLength(this.MIN_LENGTH_QUESTION), Validators.maxLength(this.MAX_LENGTH_QUESTION)]),
      anon: new FormControl()
    })
  }

  ngOnInit(){
    this.authService.currentUser$.subscribe(username => {
      if (username)
        this.currentUser = username;

      this.url = this.router.url;
      let urlParts = this.url.split('/');

      let userToCheck = this.currentUser;

      if(urlParts.length > 2)
        userToCheck = urlParts[urlParts.length - 1]

      if (userToCheck === this.currentUser){
        this.isLoggedUser = true;
        this.router.navigate(["/profile"])
      }

      this.userService.getUserInfo(userToCheck).subscribe({
        next: (res) => {
          if (!this.loadedUser()?.allowAnonQuestions) {
            this.questionForm.get('anon')?.patchValue(false);
            this.questionForm.get('anon')?.disable();
          }
          console.log(this.loadedUser())
        },
        error: (err) => {
          console.log(err.error)
        }
      })
      }
    );

    if (this.isLoggedUser) {
        this.questionForm.get('anon')?.disable();
    } else {
        this.questionForm.get('anon')?.patchValue(true);
    }
  }

  getNumberOfCharacters(): number {
    return this.questionForm.controls.body.value?.length || 0;
  }

  getFormattedJoinedDate(isoDateString: string | undefined): string {
    if (!isoDateString) {
        return 'Data indisponível'; // Ou outra mensagem padrão
    }

    // 1. Cria um objeto Date a partir da string ISO
    const date = new Date(isoDateString);

    // 2. Define as opções de formatação
    const options: Intl.DateTimeFormatOptions = {
        // Exibe o nome do mês por extenso (long)
        month: 'long',
        // Exibe o ano com 4 dígitos
        year: 'numeric',
    };

    // 3. Formata a data para a língua/localização correta (Inglês neste caso)
    const formattedDate = date.toLocaleDateString('en-US', options);
    // Exemplo: "November 2025"

    // 4. Concatena com o texto "Joined in"
    return `Joined in ${formattedDate}`;
  }

  unfollow(){
    let userToUnfollow = this.loadedUser()?.username;
    if (userToUnfollow){
      const dialogRef = this.dialog.open(ConfirmDialog, {
        position:{
          top: '10vh'
        },
        data: { title: `Are you sure you want to unfollow @${userToUnfollow}?`,
                subtitle: "Their posts will no longer appear on your timeline." },
        backdropClass: 'darker-backdrop'
      });
      dialogRef.afterClosed().subscribe(result => {
        if (result) {
          this.userService.unfollow(userToUnfollow).subscribe();
        }
      })
    }
  }

  follow(){
    let userToFollow = this.loadedUser()?.username;
    if (userToFollow){
      this.userService.follow(userToFollow).subscribe();
    }
  }

  buttonCall(){
    if(this.isLoggedUser){
      console.log('editar');
      return;
    }
    if(!this.loadedUser()?.isFollowing)
      return this.follow();

    return this.unfollow();
  }

  sendQuestion(){
    let username = this.loadedUser()?.username;
    if (!username) return

    this.isSending = true;
    this.userService.sendQuestion(username, this.questionForm.controls.body.value, this.questionForm.controls.anon.value)
      .subscribe({
        next: () => {
          this.snackBar.open("Question sent successfully.",'',{
            duration: 1000,
            horizontalPosition: 'center',
            verticalPosition: 'top',
            panelClass:['snackbar-success']
        })
        this.questionClicked = false;
        this.questionForm.reset({});
        },
        error: () => {
          this.snackBar.open("Failed to send the question. Try again later.",'',{
            duration: 2000,
            horizontalPosition: 'center',
            verticalPosition: 'top',
            panelClass:['snackbar-error']
          })
        }
      })
    this.isSending = false;
  }

  closeQuestionForm(){
    this.questionClicked = false;
    this.questionForm.reset({});
  }

}

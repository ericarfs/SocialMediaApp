import { Component, input, output } from '@angular/core';
import { RouterLink } from '@angular/router';
import { QuestionResponse } from '../types/question-response';
import { FormBuilder, FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { NgClass } from '@angular/common';

interface ReplyForm {
  body: FormControl,
}

@Component({
  selector: 'app-question',
  imports: [
    RouterLink,
    ReactiveFormsModule,
    NgClass],
  templateUrl: './question.html',
  styleUrl: './question.scss',
  host:{
    class: 'block w-[98%]'
  }
})
export class Question {
  form!: FormGroup<ReplyForm>;

  question = input<QuestionResponse>();
  isDeleting = input<boolean>();
  isSelected = input<boolean>();
  isReplying = input<boolean>();

  deleteClicked = output<void>();
  replyClicked = output<void>();

  cancelReplyClicked = output<void>();
  sendReplyClicked = output<string>();

  readonly MAX_LENGTH = 2048;
  readonly MIN_LENGTH = 4;

  constructor() {
   this.form = new FormGroup({
      body: new FormControl('', [Validators.required, Validators.minLength(4), Validators.maxLength(2048)])
    })
  }

  getNumberOfCharacters(): number {
    return this.form.controls.body.value?.length || 0;
  }

  onDeleteClick() {
    this.deleteClicked.emit();
  }

  onReplyClick() {
    this.replyClicked.emit();
  }

  onSendReplyClick() {
    if (this.form.valid) {
      this.sendReplyClicked.emit(this.form.value.body);
    } else {
      this.form.markAllAsTouched();
    }
  }

  onCancelReplyClick() {
    this.cancelReplyClicked.emit();
  }
}

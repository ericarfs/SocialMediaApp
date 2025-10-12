import { Component, Input } from '@angular/core';
import { AbstractControl, ReactiveFormsModule } from '@angular/forms';


export const VALIDATION_MESSAGES: { [key: string]: (error: any, label: string) => string } = {
  required: (error, label) => `O campo ${label.toLowerCase()} é obrigatório.`,
  email: () => 'Informe um email válido.',
  minlength: (error, label) =>
    `O campo ${label.toLowerCase()} deve ter no mínimo ${error.requiredLength} caracteres.`,
  maxlength: (error, label) =>
    `O campo ${label.toLowerCase()} deve ter no máximo ${error.requiredLength} caracteres.`,
  mismatch: () => 'As senhas não coincidem.'
};

@Component({
  selector: 'app-form-control-error',
  standalone: true,
  imports: [ReactiveFormsModule],
  template: `
    @if (errorMessage) {
      <div class="text-xs text-red-500 -mt-1 pl-1">
        {{ errorMessage }}
      </div>
    }
  `,
})
export class FormControlError {
  @Input({ required: true }) control!: AbstractControl;
  @Input() label: string = 'Este campo';

  get errorMessage(): string | null {
    const control = this.control;
    if (control.invalid && (control.dirty || control.touched) && control.errors) {
      const firstErrorKey = Object.keys(control.errors)[0];
      const errorValue = control.errors[firstErrorKey];

      const getMessage = VALIDATION_MESSAGES[firstErrorKey];

      if (getMessage) {
        return getMessage(errorValue, this.label);
      }
      return 'Erro de validação desconhecido.';
    }
    return null;
  }
}

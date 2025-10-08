import { Component, EventEmitter, Input, Output } from '@angular/core';
import { RouterLink } from '@angular/router';


@Component({
  selector: 'app-auth-layout',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './layout.html',
  styleUrl: './layout.scss'
})
export class Layout{
  @Input() title: string = "";
  @Input() subtitle: string = "";
  @Input() routerRedirect: string = "";
  @Input() redirectText: string = "";
  @Input() disableAuthBtn: boolean = true;
  @Output("submit") onSubmit = new EventEmitter();

  @Output("navigate") onNavigate = new EventEmitter();

  submit(){
    this.onSubmit.emit();
  }

  navigate(){
    this.onNavigate.emit();
  }
}

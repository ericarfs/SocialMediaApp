import {
  Component,
  ViewChild, AfterViewInit, ElementRef,
  DestroyRef,
  Signal,
  input
} from '@angular/core';
import { AnswerResponse } from '../types/answer-response';
import { FeedTemplateService } from './feed-template.service';
import { Post } from '../post/post';

@Component({
  selector: 'app-feed-template',
  imports: [Post],
  templateUrl: './feed-template.html',
  styleUrl: './feed-template.scss',
  host:{
    class: 'block w-full'
  }
})
export class FeedTemplate implements AfterViewInit {
  baseUrl = input<string>('');
  noContentMessage = input<string>('');
  emptyHintMessage = input<string>('');

  readonly answers!: Signal<any[]>;
  readonly loading!: Signal<boolean>;

  isSelectedEdit = false;
  isSelectedReply = false;
  isSelectedThread = false;

  currentAnswer: AnswerResponse | null = null;

  @ViewChild('scrollAnchor', { static: false }) scrollAnchor!: ElementRef;

  private observer!: IntersectionObserver;

  constructor(
    private service: FeedTemplateService,
    private destroyRef: DestroyRef
  ) {
    this.answers = this.service.answers$;
    this.loading = this.service.loading$;
  }

  ngOnInit(): void {
    this.service.resetFeed();

    this.service.loadNextPage(this.baseUrl());
  }

  ngAfterViewInit(): void {
    this.observer = new IntersectionObserver(
      (entries) => {
        if (entries.some((e) => e.isIntersecting)) {
          this.service.loadNextPage(this.baseUrl());
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

  showThreadSelect(id: number){
    this.isSelectedThread = true;
    this.currentAnswer = this.answers().find(a => a.id == id)
  }

  editAnswerSelect(id: number){
    this.isSelectedEdit = true;
    this.currentAnswer = this.answers().find(a => a.id == id)
  }

  replyAnswerSelect(id: number){
    this.isSelectedReply = true;
    this.currentAnswer = this.answers().find(a => a.id == id)
  }

  editAnswer(body:string, id: number){
    this.service.edit(id, body);
    this.cancelSelect();
  }

  cancelSelect(){
    this.isSelectedEdit = false;
    this.isSelectedReply = false;
    this.isSelectedThread = false;
    this.currentAnswer= null;
  }

  deleteAnswer(id: number){
    this.service.delete(id);
  }
}

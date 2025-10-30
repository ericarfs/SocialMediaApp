import { AfterViewInit, Component, DestroyRef, ElementRef, OnInit, Signal, ViewChild } from '@angular/core';
import { Layout } from '../layout/layout';
import { AnswerResponse } from '../types/answer-response';
import { HomeService } from './home.service';
import { Post } from '../post/post';


@Component({
  selector: 'app-home',
  imports: [Layout,
    Post
  ],
  templateUrl: './home.html',
  styleUrl: './home.scss'
})
export class Home implements OnInit, AfterViewInit {
  readonly answers!: Signal<any[]>;
  readonly loading!: Signal<boolean>;

  isSelectedEdit = false;
  isSelectedReply = false;
  isSelectedThread = false;

  currentAnswer: AnswerResponse | null = null;

  @ViewChild('scrollAnchor', { static: false }) scrollAnchor!: ElementRef;

  private observer!: IntersectionObserver;

  constructor(
    private homeService: HomeService,
    private destroyRef: DestroyRef
  ) {
    this.answers = this.homeService.answers$;
    this.loading = this.homeService.loading$;
  }

  ngOnInit(): void {
    this.homeService.resetFeed();
    this.homeService.loadNextPage();
  }

  ngAfterViewInit(): void {
    this.observer = new IntersectionObserver(
      (entries) => {
        if (entries.some((e) => e.isIntersecting)) {
          this.homeService.loadNextPage();
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
    this.homeService.edit(id, body);
    this.cancelSelect();
  }

  cancelSelect(){
    this.isSelectedEdit = false;
    this.isSelectedReply = false;
    this.isSelectedThread = false;
    this.currentAnswer= null;
  }

  deleteAnswer(id: number){
    this.homeService.delete(id);
  }
}

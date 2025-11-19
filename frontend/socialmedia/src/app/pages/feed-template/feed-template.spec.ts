import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FeedTemplate } from './feed-template';

describe('FeedTemplate', () => {
  let component: FeedTemplate;
  let fixture: ComponentFixture<FeedTemplate>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FeedTemplate]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FeedTemplate);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

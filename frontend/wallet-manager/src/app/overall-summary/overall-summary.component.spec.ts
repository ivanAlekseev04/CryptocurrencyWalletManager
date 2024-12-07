import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OverallSummaryComponent } from './overall-summary.component';

describe('OverallSummaryComponent', () => {
  let component: OverallSummaryComponent;
  let fixture: ComponentFixture<OverallSummaryComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [OverallSummaryComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(OverallSummaryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

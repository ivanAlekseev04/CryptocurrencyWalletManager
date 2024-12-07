import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HistoryPeriodComponent } from './history-period.component';

describe('HistoryPeriodComponent', () => {
  let component: HistoryPeriodComponent;
  let fixture: ComponentFixture<HistoryPeriodComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HistoryPeriodComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(HistoryPeriodComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BottombarComponent } from './bottombar.component';

describe('BottombarComponent', () => {
  let component: BottombarComponent;
  let fixture: ComponentFixture<BottombarComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ BottombarComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BottombarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

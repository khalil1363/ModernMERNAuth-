import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EducationChildForm } from './education-child-form';

describe('EducationChildForm', () => {
  let component: EducationChildForm;
  let fixture: ComponentFixture<EducationChildForm>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EducationChildForm]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EducationChildForm);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

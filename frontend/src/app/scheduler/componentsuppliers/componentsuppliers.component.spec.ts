import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ComponentsuppliersComponent } from './componentsuppliers.component';

describe('ComponentsuppliersComponent', () => {
  let component: ComponentsuppliersComponent;
  let fixture: ComponentFixture<ComponentsuppliersComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ComponentsuppliersComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ComponentsuppliersComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SidebarCocina } from './sidebar-cocina';

describe('SidebarCocina', () => {
  let component: SidebarCocina;
  let fixture: ComponentFixture<SidebarCocina>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [SidebarCocina]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SidebarCocina);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

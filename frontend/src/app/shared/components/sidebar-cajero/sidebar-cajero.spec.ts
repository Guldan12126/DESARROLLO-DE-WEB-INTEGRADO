import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SidebarCajero } from './sidebar-cajero';

describe('SidebarCajero', () => {
  let component: SidebarCajero;
  let fixture: ComponentFixture<SidebarCajero>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [SidebarCajero]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SidebarCajero);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SidebarMozo } from './sidebar-mozo';

describe('SidebarMozo', () => {
  let component: SidebarMozo;
  let fixture: ComponentFixture<SidebarMozo>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [SidebarMozo]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SidebarMozo);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

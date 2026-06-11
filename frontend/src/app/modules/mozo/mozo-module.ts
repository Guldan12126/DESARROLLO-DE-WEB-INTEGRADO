import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { MozoComponent } from './mozo.component';
import { DashboardMozoComponent } from './pages/dashboard-mozo/dashboard-mozo.component';
import { SidebarMozo } from '../../shared/components/sidebar-mozo/sidebar-mozo.component';
import { MozoRoutingModule } from './mozo-routing-module';
import { SharedModule } from '../../shared/shared.module';

@NgModule({
  declarations: [
    MozoComponent,
    DashboardMozoComponent,
    SidebarMozo
  ],
  imports: [
    CommonModule,   
    FormsModule,
    MozoRoutingModule,
    SharedModule
  ]
})
export class MozoModule { }
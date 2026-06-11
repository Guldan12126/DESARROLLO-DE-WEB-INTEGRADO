import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { CajeroComponent } from './cajero.component';
import { DashboardCajeroComponent } from './pages/dashboard-cajero/dashboard-cajero.component';
import { SidebarCajero } from '../../shared/components/sidebar-cajero/sidebar-cajero.component';
import { CajeroRoutingModule } from './cajero-routing-module';
import { SharedModule } from '../../shared/shared.module';

@NgModule({
  declarations: [
    CajeroComponent,
    DashboardCajeroComponent,
    SidebarCajero
  ],
  imports: [
    CommonModule,
    FormsModule,
    CajeroRoutingModule,
    SharedModule
  ]
})
export class CajeroModule { }

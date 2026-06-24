import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { MozoComponent } from './mozo.component';
import { DashboardMozoComponent } from './pages/dashboard-mozo/dashboard-mozo.component';
import { SidebarMozo } from '../../shared/components/sidebar-mozo/sidebar-mozo.component';
import { MozoRoutingModule } from './mozo-routing-module';
import { SharedModule } from '../../shared/shared.module';
import { MesasMapaComponent } from './pages/mesas-mapa/mesas-mapa';
import { PedidosNuevoComponent } from './pages/pedidos-nuevo/pedidos-nuevo';
import { PedidosListaComponent } from './pages/pedidos-lista/pedidos-lista';
import { PedidosHistorialComponent } from './pages/pedidos-historial/pedidos-historial';

@NgModule({
  declarations: [
    MozoComponent,
    DashboardMozoComponent,
    SidebarMozo,
    MesasMapaComponent,
    PedidosNuevoComponent,
    PedidosListaComponent,
    PedidosHistorialComponent
  ],
  imports: [
    CommonModule,   
    FormsModule,
    MozoRoutingModule,
    SharedModule
  ]
})
export class MozoModule { }
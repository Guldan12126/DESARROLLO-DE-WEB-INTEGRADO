import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { CajeroComponent } from './cajero.component';
import { DashboardCajeroComponent } from './pages/dashboard-cajero/dashboard-cajero.component';
import { SidebarCajero } from '../../shared/components/sidebar-cajero/sidebar-cajero.component';
import { CajeroRoutingModule } from './cajero-routing-module';
import { SharedModule } from '../../shared/shared.module';

import { CajaGestionComponent } from './pages/caja-gestion/caja-gestion';
import { CajaMovimientosComponent } from './pages/caja-movimientos/caja-movimientos';
import { VentasHistorialComponent } from './pages/ventas-historial/ventas-historial';
import { PedidosHistorialComponent } from './pages/pedidos-historial/pedidos-historial';
import { PedidosPorCobrar } from './pages/pedidos-por-cobrar/pedidos-por-cobrar';

@NgModule({
  declarations: [
    CajeroComponent,
    DashboardCajeroComponent,
    SidebarCajero,
    CajaGestionComponent,
    CajaMovimientosComponent,
    VentasHistorialComponent,
    PedidosHistorialComponent,
    PedidosPorCobrar
  ],
  imports: [
    CommonModule,
    FormsModule,
    CajeroRoutingModule,
    SharedModule
  ]
})
export class CajeroModule { }

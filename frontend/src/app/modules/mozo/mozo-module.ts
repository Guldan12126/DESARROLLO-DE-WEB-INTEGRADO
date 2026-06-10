import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { MozoRoutingModule } from './mozo-routing-module';
import { DashboardMozo } from './pages/dashboard-mozo/dashboard-mozo';
import { MesasMozo } from './pages/mesas-mozo/mesas-mozo';
import { PedidosMozoComponent } from './pages/pedidos-mozo/pedidos-mozo';
import { PedidosNuevoComponent } from './pages/pedidos-mozo/pedidos-nuevo';
import { PedidosHistorialComponent } from './pages/pedidos-mozo/pedidos-historial';
import { HeaderMozo } from '../../shared/components/header-mozo/header-mozo';
import { SidebarMozo } from '../../shared/components/sidebar-mozo/sidebar-mozo.component';


@NgModule({
  declarations: [],
  declarations: [
    DashboardMozo,
    MesasMozo,
    PedidosHistorialComponent
  ],
  imports: [
    CommonModule,
    MozoRoutingModule
    MozoRoutingModule,
    HeaderMozo,
    SidebarMozo,
    PedidosMozoComponent,
    PedidosNuevoComponent
  ]
})
export class MozoModule { }

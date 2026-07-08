import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { CocinaComponent } from './cocina.component';
import { DashboardCocinaComponent } from './pages/dashboard-cocina/dashboard-cocina.component';
import { SidebarCocina } from '../../shared/components/sidebar-cocina/sidebar-cocina.component';
import { CocinaRoutingModule } from './cocina-routing-module';
import { SharedModule } from '../../shared/shared.module';
import { RecetarioComponent } from './pages/recetario/recetario';
import { PedidosCompletadosComponent } from './pages/pedidos-completados/pedidos-completados';
import { ColaPreparacionComponent } from './pages/cola-preparacion/cola-preparacion';

@NgModule({
  declarations: [
    CocinaComponent,
    DashboardCocinaComponent,
    SidebarCocina,
    RecetarioComponent,
    PedidosCompletadosComponent,
    ColaPreparacionComponent
  ],
  imports: [
    CommonModule,
    FormsModule,
    CocinaRoutingModule,
    SharedModule
  ]
})
export class CocinaModule { }

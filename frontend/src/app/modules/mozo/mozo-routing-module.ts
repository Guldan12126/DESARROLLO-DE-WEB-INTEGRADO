import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { MozoComponent } from './mozo.component';
import { DashboardMozoComponent } from './pages/dashboard-mozo/dashboard-mozo.component';

import { MesasMapaComponent } from './pages/mesas-mapa/mesas-mapa';
import { PedidosNuevoComponent } from './pages/pedidos-nuevo/pedidos-nuevo';
import { PedidosListaComponent } from './pages/pedidos-lista/pedidos-lista';
import { PedidosHistorialComponent } from './pages/pedidos-historial/pedidos-historial';

const routes: Routes = [
  {
    path: '',
    component: MozoComponent,
    children: [
      { path: 'dashboard', component: DashboardMozoComponent },
      { path: 'mesas/mapa', component: MesasMapaComponent },
      { path: 'mesas/lista', redirectTo: 'dashboard', pathMatch: 'full' },
      { path: 'pedidos/nuevo', component: PedidosNuevoComponent },
      { path: 'pedidos/lista', component: PedidosListaComponent },
      { path: 'pedidos/historial', component: PedidosHistorialComponent },
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class MozoRoutingModule { }

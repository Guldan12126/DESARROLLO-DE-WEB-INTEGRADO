import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CajeroComponent } from './cajero.component';
import { DashboardCajeroComponent } from './pages/dashboard-cajero/dashboard-cajero.component';
import { CajaGestionComponent } from './pages/caja-gestion/caja-gestion';
import { CajaMovimientosComponent } from './pages/caja-movimientos/caja-movimientos';
import { VentasHistorialComponent } from './pages/ventas-historial/ventas-historial';
import { PedidosHistorialComponent } from './pages/pedidos-historial/pedidos-historial';

const routes: Routes = [
  {
    path: '',
    component: CajeroComponent,
    children: [
      { path: 'dashboard', component: DashboardCajeroComponent },
      { path: 'caja/gestion', component: CajaGestionComponent },
      { path: 'caja/movimientos', component: CajaMovimientosComponent },
      { path: 'ventas/nueva', redirectTo: 'dashboard', pathMatch: 'full' },
      { path: 'ventas/historial', component: VentasHistorialComponent },
      { path: 'pedidos/por-cobrar', redirectTo: 'dashboard', pathMatch: 'full' },
      { path: 'pedidos/historial', component: PedidosHistorialComponent },
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class CajeroRoutingModule { }

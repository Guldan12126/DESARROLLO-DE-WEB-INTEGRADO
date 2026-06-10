import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DashboardMozo } from './pages/dashboard-mozo/dashboard-mozo';
import { MesasMozo } from './pages/mesas-mozo/mesas-mozo';
import { PedidosNuevoComponent } from './pages/pedidos-mozo/pedidos-nuevo';
import { PedidosHistorialComponent } from './pages/pedidos-mozo/pedidos-historial';
import { PedidosMozoComponent } from './pages/pedidos-mozo/pedidos-mozo';

const routes: Routes = [];
const routes: Routes = [
  { path: '', component: DashboardMozo },
  { path: 'dashboard', component: DashboardMozo },
  { path: 'mesas', component: MesasMozo },
  { path: 'pedidos/nuevo', component: PedidosNuevoComponent },
  { path: 'pedidos/historial', component: PedidosHistorialComponent },
  { path: 'pedidos-mozo/:id', component: PedidosMozoComponent }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class MozoRoutingModule { }

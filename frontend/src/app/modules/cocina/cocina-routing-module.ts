import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CocinaComponent } from './cocina.component';
import { DashboardCocinaComponent } from './pages/dashboard-cocina/dashboard-cocina.component';
import { RecetarioComponent } from './pages/recetario/recetario';
import { PedidosCompletadosComponent } from './pages/pedidos-completados/pedidos-completados';
import { ColaPreparacionComponent } from './pages/cola-preparacion/cola-preparacion';

const routes: Routes = [
  {
    path: '',
    component: CocinaComponent,
    children: [
      { path: 'dashboard', component: DashboardCocinaComponent },
      { path: 'pedidos/cola', component: ColaPreparacionComponent },
      { path: 'recetario', component: RecetarioComponent },
      { path: 'pedidos/completados', component: PedidosCompletadosComponent },
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class CocinaRoutingModule { }

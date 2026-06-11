import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { AdminComponent } from './admin.component';
import { UsuariosComponent } from './pages/usuarios/usuarios';
import { UsuariosListaComponent } from './pages/usuarios/usuarios-lista.component';
import { UsuariosRolesComponent } from './pages/usuarios/usuarios-roles.component';
import { ProductosListaComponent } from './pages/productos/productos-lista.component';
import { Productos } from './pages/productos/productos';
import { Mesas } from './pages/mesas/mesas';
import { Pedidos } from './pages/pedidos/pedidos';
import { Ventas } from './pages/ventas/ventas';
import { Reportes } from './pages/reportes/reportes';
import { CajaComponent } from './pages/caja/caja';

const routes: Routes = [
  {
    path: '',
    component: AdminComponent,
    children: [
      { path: 'dashboard', component: DashboardComponent },
      {
        path: 'usuarios',
        children: [
          { path: '', redirectTo: 'lista', pathMatch: 'full' },
          { path: 'lista', component: UsuariosListaComponent },
          { path: 'crear', component: UsuariosComponent },
          { path: 'roles', component: UsuariosRolesComponent },
        ],
      },
      {
        path: 'productos',
        children: [
          { path: '', redirectTo: 'lista', pathMatch: 'full' },
          { path: 'lista', component: ProductosListaComponent },
          { path: 'crear', component: Productos },
          { path: 'categorias', component: Productos },
          { path: 'stock', component: Productos },
        ],
      },
      {
        path: 'mesas',
        children: [
          { path: '', redirectTo: 'lista', pathMatch: 'full' },
          { path: 'lista', component: Mesas },
          { path: 'crear', component: Mesas },
          { path: 'mapa', component: Mesas },
        ],
      },
      {
        path: 'pedidos',
        children: [
          { path: '', redirectTo: 'lista', pathMatch: 'full' },
          { path: 'lista', component: Pedidos },
          { path: 'ver', component: Pedidos },
          { path: 'nuevo', component: Pedidos },
          { path: 'historial', component: Pedidos },
        ],
      },
      {
        path: 'ventas',
        children: [
          { path: '', redirectTo: 'lista', pathMatch: 'full' },
          { path: 'lista', component: Ventas },
          { path: 'ver', component: Ventas },
          { path: 'detalle', component: Ventas },
        ],
      },
      {
        path: 'caja',
        children: [
          { path: '', redirectTo: 'gestion', pathMatch: 'full' },
          { path: 'gestion', component: CajaComponent },
          { path: 'movimientos', component: CajaComponent },
        ],
      },
      {
        path: 'reportes',
        children: [
          { path: '', redirectTo: 'ventas-diarias', pathMatch: 'full' },
          { path: 'ventas-diarias', component: Reportes },
          { path: 'ventas-semanales', component: Reportes },
          { path: 'ventas-mensuales', component: Reportes },
          { path: 'productos-vendidos', component: Reportes },
          { path: 'mesas-mas-usadas', component: Reportes },
        ],
      },
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
      { path: '**', redirectTo: 'dashboard' },
    ],
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class AdminRoutingModule {}

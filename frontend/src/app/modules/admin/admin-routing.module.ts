import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { AdminComponent } from './admin.component';
import { UsuariosComponent } from './pages/usuarios/usuarios';
import { UsuariosListaComponent } from './pages/usuarios/usuarios-lista.component';
import { UsuariosRolesComponent } from './pages/usuarios/usuarios-roles.component';
import { ProductosListaComponent } from './pages/productos/productos-lista/productos-lista.component';
import { Productos } from './pages/productos/productos';
import { ProductosCategorias } from './pages/productos/productos-categorias/productos-categorias';
import { ProductosStock } from './pages/productos/productos-stock/productos-stock';
import { Mesas } from './pages/mesas/mesas';
import { MesasLista } from './pages/mesas/mesas-lista/mesas-lista';
import { MesasCrear } from './pages/mesas/mesas-crear/mesas-crear';
import { MesasMapa } from './pages/mesas/mesas-mapa/mesas-mapa';
import { Pedidos } from './pages/pedidos/pedidos';
import { PedidosVer } from './pages/pedidos/pedidos-ver/pedidos-ver';
import { PedidosNuevo } from './pages/pedidos/pedidos-nuevo/pedidos-nuevo';
import { PedidosHistorial } from './pages/pedidos/pedidos-historial/pedidos-historial';
import { Ventas } from './pages/ventas/ventas';
import { VentasVer } from './pages/ventas/ventas-ver/ventas-ver';
import { VentasDetalle } from './pages/ventas/ventas-detalle/ventas-detalle';
import { Reportes } from './pages/reportes/reportes';
import { ReportesSemanales } from './pages/reportes/reportes-semanales/reportes-semanales';
import { ReportesMensuales } from './pages/reportes/reportes-mensuales/reportes-mensuales';
import { ReportesProductos } from './pages/reportes/reportes-productos/reportes-productos';
import { ReportesMesas } from './pages/reportes/reportes-mesas/reportes-mesas';
import { CajaComponent } from './pages/caja/caja';
import { CajaMovimientos } from './pages/caja/caja-movimientos/caja-movimientos';

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
          { path: 'categorias', component: ProductosCategorias },
          { path: 'stock', component: ProductosStock },
        ],
      },
      {
        path: 'mesas',
        children: [
          { path: '', redirectTo: 'lista', pathMatch: 'full' },
          { path: 'lista', component: MesasLista },
          { path: 'crear', component: MesasCrear },
          { path: 'mapa', component: MesasMapa },
        ],
      },
      {
        path: 'pedidos',
        children: [
          { path: '', redirectTo: 'lista', pathMatch: 'full' },
          { path: 'lista', component: Pedidos },
          { path: 'ver', component: PedidosVer },
          { path: 'nuevo', component: PedidosNuevo },
          { path: 'historial', component: PedidosHistorial },
        ],
      },
      {
        path: 'ventas',
        children: [
          { path: '', redirectTo: 'lista', pathMatch: 'full' },
          { path: 'lista', component: Ventas },
          { path: 'ver', component: VentasVer },
          { path: 'detalle', component: VentasDetalle },
        ],
      },
      {
        path: 'caja',
        children: [
          { path: '', redirectTo: 'gestion', pathMatch: 'full' },
          { path: 'gestion', component: CajaComponent },
          { path: 'movimientos', component: CajaMovimientos },
        ],
      },
      {
        path: 'reportes',
        children: [
          { path: '', redirectTo: 'ventas-diarias', pathMatch: 'full' },
          { path: 'ventas-diarias', component: Reportes },
          { path: 'ventas-semanales', component: ReportesSemanales },
          { path: 'ventas-mensuales', component: ReportesMensuales },
          { path: 'productos-vendidos', component: ReportesProductos },
          { path: 'mesas-mas-usadas', component: ReportesMesas },
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

import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms'; 
import { AdminRoutingModule } from './admin-routing.module';
import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { AdminComponent } from './admin.component';
import { SidebarAdminComponent } from '../../shared/components/sidebar-admin/sidebar-admin.component';
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
import { ToastService } from '../../shared/services/toast.service';
import { HttpClientModule } from '@angular/common/http';
import { SharedModule } from '../../shared/shared.module';
import { ProductosCategorias } from './pages/productos/productos-categorias/productos-categorias';
import { ProductosStock } from './pages/productos/productos-stock/productos-stock';
import { MesasCrear } from './pages/mesas/mesas-crear/mesas-crear';
import { MesasMapa } from './pages/mesas/mesas-mapa/mesas-mapa';
import { PedidosVer } from './pages/pedidos/pedidos-ver/pedidos-ver';
import { PedidosNuevo } from './pages/pedidos/pedidos-nuevo/pedidos-nuevo';
import { PedidosHistorial } from './pages/pedidos/pedidos-historial/pedidos-historial';
import { VentasVer } from './pages/ventas/ventas-ver/ventas-ver';
import { VentasDetalle } from './pages/ventas/ventas-detalle/ventas-detalle';
import { CajaMovimientos } from './pages/caja/caja-movimientos/caja-movimientos';
import { ReportesSemanales } from './pages/reportes/reportes-semanales/reportes-semanales';
import { ReportesMensuales } from './pages/reportes/reportes-mensuales/reportes-mensuales';
import { ReportesProductos } from './pages/reportes/reportes-productos/reportes-productos';
import { ReportesMesas } from './pages/reportes/reportes-mesas/reportes-mesas';

@NgModule({
  declarations: [
    AdminComponent,
    DashboardComponent,
    SidebarAdminComponent,
    UsuariosComponent,
    UsuariosListaComponent,
    UsuariosRolesComponent,
    ProductosListaComponent,
    Productos,
    Mesas,
    Pedidos,
    Ventas,
    Reportes,
    CajaComponent,
    ProductosCategorias,
    ProductosStock,
    MesasCrear,
    MesasMapa,
    PedidosVer,
    PedidosNuevo,
    PedidosHistorial,
    VentasVer,
    VentasDetalle,
    CajaMovimientos,
    ReportesSemanales,
    ReportesMensuales,
    ReportesProductos,
    ReportesMesas,
  ],
  imports: [
    CommonModule,
    FormsModule,
    HttpClientModule,
    AdminRoutingModule,
    SharedModule,
  ],
  providers: [
    ToastService,
  ],
})
export class AdminModule {}

import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { AdminRoutingModule } from './admin-routing-module';
import { Dashboard } from './pages/dashboard/dashboard';
import { Usuarios } from './pages/usuarios/usuarios';
import { Productos } from './pages/productos/productos';
import { Mesas } from './pages/mesas/mesas';
import { Pedidos } from './pages/pedidos/pedidos';
import { Ventas } from './pages/ventas/ventas';
import { Reportes } from './pages/reportes/reportes';


@NgModule({
  declarations: [
    Dashboard,
    Usuarios,
    Productos,
    Mesas,
    Pedidos,
    Ventas,
    Reportes
  ],
  imports: [
    CommonModule,
    AdminRoutingModule
  ]
})
export class AdminModule { }

import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

// 1. Asegúrate de importar el componente aquí (ajusta la ruta según tu carpetas)
import { DashboardMozoComponent } from './pages/dashboard-mozo/dashboard-mozo.component';
import { MozoRoutingModule } from './mozo-routing-module';

@NgModule({
  declarations: [
    DashboardMozoComponent 
  ],
  imports: [
    CommonModule,   
    MozoRoutingModule
  ]
})
export class MozoModule { }
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms'; // <--- 1. Importar esto
import { DashboardComponent } from './pages/dashboard/dashboard.component';

@NgModule({
  declarations: [
    DashboardComponent
    // ... otros componentes
  ],
  imports: [
    CommonModule,
    FormsModule, 
  ]
})
export class AdminModule { }

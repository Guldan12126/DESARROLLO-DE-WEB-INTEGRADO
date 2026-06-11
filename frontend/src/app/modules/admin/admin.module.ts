import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms'; 
import { AdminRoutingModule } from './admin-routing.module';
import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { AdminComponent } from './admin.component';
import { SidebarAdminComponent } from '../../shared/components/sidebar-admin/sidebar-admin.component';
import { HeaderComponent } from '../../shared/components/header/header.component';
import { UsuariosComponent } from './pages/usuarios/usuarios'; // Componente para crear/editar
import { UsuariosListaComponent } from './pages/usuarios/usuarios-lista.component'; // Nuevo componente para la lista
import { UsuariosRolesComponent } from './pages/usuarios/usuarios-roles.component'; // Nuevo componente para roles
import { ToastService } from '../../shared/services/toast.service'; // Importar ToastService
import { HttpClientModule } from '@angular/common/http'; // Import HttpClientModule


@NgModule({
  declarations: [
    AdminComponent,    
    DashboardComponent, 
    SidebarAdminComponent, 
    HeaderComponent,
    UsuariosComponent,
    UsuariosListaComponent,
    UsuariosRolesComponent  
  ],
  imports: [
    CommonModule,
    FormsModule,
    HttpClientModule, // Añadir HttpClientModule aquí
    AdminRoutingModule,
  ],
  providers: [
    ToastService // Proveer ToastService
  ]
})
export class AdminModule { }
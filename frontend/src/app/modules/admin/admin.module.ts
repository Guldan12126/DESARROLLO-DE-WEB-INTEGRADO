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
import { ToastService } from '../../shared/services/toast.service'; 
import { HttpClientModule } from '@angular/common/http'; 
import { SharedModule } from '../../shared/shared.module';


@NgModule({
  declarations: [
    AdminComponent,    
    DashboardComponent, 
    SidebarAdminComponent, 
    UsuariosComponent,
    UsuariosListaComponent,
    UsuariosRolesComponent  
  ],
  imports: [
    CommonModule,
    FormsModule,
    HttpClientModule, 
    AdminRoutingModule,
    SharedModule
  ],
  providers: [
    ToastService 
  ]
})
export class AdminModule { }
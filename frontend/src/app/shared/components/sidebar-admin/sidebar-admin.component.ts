import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { ToastService } from '../../services/toast.service';

@Component({
  selector: 'app-sidebar-admin',
  templateUrl: './sidebar-admin.component.html',
  styleUrl: '../../../../scss/_sidebar-admin.scss',
  standalone: false
})
export class SidebarAdminComponent {
  nombreUsuario: string = 'Administrador';
  isOpen: boolean = false;
  
  sections = {
    usuarios: false,
    productos: false,
    mesas: false,
    pedidos: false,
    ventas: false,
    caja: false,
    reportes: false
  };

  constructor(
    private router: Router,
    private toastService: ToastService
  ) {}

  toggleSection(section: keyof typeof this.sections) {
    this.sections[section] = !this.sections[section];
  }

  toggleSidebar() {
    this.isOpen = !this.isOpen;
  }

  closeSidebar() {
    this.isOpen = false;
  }

  onLogout() {
    localStorage.removeItem('token');
    localStorage.removeItem('role');
    this.router.navigate(['/login']);
    this.toastService.info('Sesión cerrada correctamente');
  }
}
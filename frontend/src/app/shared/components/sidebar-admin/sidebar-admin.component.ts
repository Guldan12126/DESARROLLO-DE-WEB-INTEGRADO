import { Component } from '@angular/core';

@Component({
  selector: 'app-sidebar-admin',
  templateUrl: './sidebar-admin.component.html',
  styleUrls: ['./sidebar-admin.component.scss'],
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
    reportes: false
  };

  toggleSection(section: keyof typeof this.sections) {
    this.sections[section] = !this.sections[section];
  }

  toggleSidebar() {
    this.isOpen = !this.isOpen;
  }

  closeSidebar() {
    this.isOpen = false;
  }
}
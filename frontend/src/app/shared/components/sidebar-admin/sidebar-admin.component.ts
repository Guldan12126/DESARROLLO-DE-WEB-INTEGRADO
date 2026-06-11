import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ToastService } from '../../services/toast.service';

@Component({
  selector: 'app-sidebar-admin',
  templateUrl: './sidebar-admin.component.html',
  styleUrl: '../../../../scss/_sidebar-admin.scss',
  standalone: false
})
export class SidebarAdminComponent implements OnInit {
  nombreUsuario: string = 'Administrador';
  isOpen: boolean = false;
  avatarUrl: string = 'assets/Images/default-avatar.png'; 
  
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

  ngOnInit(): void {
    const nombre = localStorage.getItem('nombreUsuario');
    if (nombre) {
      this.nombreUsuario = nombre;
    }
  }

  handleImageError(event: any): void {
    const target = event.target as HTMLImageElement;
    const defaultImg = 'assets/Images/default-avatar.png'; // Ruta corregida: 'Images' con 'I' mayúscula
    if (target.src !== window.location.origin + '/' + defaultImg) {
      target.src = defaultImg;
    }
  }

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
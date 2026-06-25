import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ToastService } from '../../services/toast.service';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-sidebar-cocina',
  templateUrl: './sidebar-cocina.html',
  styleUrl: '../../../../scss/_sidebar-admin.scss',
  standalone: false
})
export class SidebarCocina implements OnInit {
  nombreUsuario: string = 'Andrea Arrunategui';
  rolUsuario: string = 'COCINA';
  isOpen: boolean = false;
  avatarUrl: string = 'assets/Images/default-avatar.png'; 
  
  sections = {
    pedidos: false
  };

  constructor(
    private router: Router,
    private toastService: ToastService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    const nombre = localStorage.getItem('nombreUsuario');
    if (nombre) {
      this.nombreUsuario = nombre;
    }
    const rol = localStorage.getItem('role');
    if (rol) {
      this.rolUsuario = rol;
    }
  }

  handleImageError(event: any): void {
    const target = event.target as HTMLImageElement;
    const defaultImg = 'assets/Images/default-avatar.png';
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
    this.authService.logout();
    this.router.navigate(['/login']);
    this.toastService.info('Sesión cerrada correctamente');
  }
}

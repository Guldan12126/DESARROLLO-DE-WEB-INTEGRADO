import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Subject, takeUntil } from 'rxjs';

import { ToastService } from '../../services/toast.service';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-sidebar-admin',
  templateUrl: './sidebar-admin.component.html',
  styleUrl: '../../../../scss/_sidebar-admin.scss',
  standalone: false,
})
export class SidebarAdminComponent implements OnInit, OnDestroy {
  nombreUsuario: string = 'Administrador';
  rolUsuario: string = 'ADMIN';
  isOpen: boolean = false;
  avatarUrl: string = 'assets/Images/default-avatar.png';
  private readonly destroy$ = new Subject<void>();

  sections = {
    usuarios: false,
    productos: false,
    mesas: false,
    pedidos: false,
    ventas: false,
    caja: false,
    reportes: false,
    configuracion: false
  };

  constructor(
    private router: Router,
    private toastService: ToastService,
    private authService: AuthService,
  ) {}

  ngOnInit(): void {
    this.authService.session$
      .pipe(takeUntil(this.destroy$))
      .subscribe((session) => {
        this.nombreUsuario = session?.nombre ?? 'Administrador';
        this.rolUsuario = session?.rol ?? 'ADMIN';
      });
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
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

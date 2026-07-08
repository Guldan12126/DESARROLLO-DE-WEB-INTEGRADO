import { Component, HostListener, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Subject, takeUntil } from 'rxjs';

import { AuthService } from '../../../core/services/auth.service';
import {
  NotificationItem,
  NotificationService,
} from '../../services/notification.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrl: '../../../../scss/_header.scss',
  standalone: false,
})
export class HeaderComponent implements OnInit, OnDestroy {
  nombreUsuario: string = 'Usuario';
  rolUsuario: string = 'Rol';
  isDropdownOpen: boolean = false;
  isNotificationsOpen: boolean = false;
  isLoadingNotifications: boolean = false;
  unreadCount: number = 0;
  notifications: NotificationItem[] = [];
  avatarUrl: string = 'assets/Images/default-avatar.png';
  private currentRole: string = '';
  private readonly destroy$ = new Subject<void>();

  constructor(
    private router: Router,
    private authService: AuthService,
    private notificationService: NotificationService,
  ) {}

  ngOnInit(): void {
    this.notificationService.notifications$
      .pipe(takeUntil(this.destroy$))
      .subscribe((notifications) => {
        this.notifications = notifications;
      });

    this.notificationService.unreadCount$
      .pipe(takeUntil(this.destroy$))
      .subscribe((count) => {
        this.unreadCount = count;
      });

    this.authService.session$
      .pipe(takeUntil(this.destroy$))
      .subscribe((session) => {
        this.nombreUsuario = session?.nombre ?? 'Usuario';
        this.rolUsuario = session?.rol ?? 'Invitado';

        if (!session?.rol) {
          this.currentRole = '';
          this.notifications = [];
          this.unreadCount = 0;
          return;
        }

        this.currentRole = session.rol;
        this.loadNotifications();
      });
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  @HostListener('document:click')
  closeMenus(): void {
    this.isDropdownOpen = false;
    this.isNotificationsOpen = false;
  }

  toggleDropdown(event: Event): void {
    event.stopPropagation();
    this.isDropdownOpen = !this.isDropdownOpen;
    this.isNotificationsOpen = false;
  }

  toggleNotifications(event: Event): void {
    event.stopPropagation();
    this.isNotificationsOpen = !this.isNotificationsOpen;
    this.isDropdownOpen = false;

    if (this.isNotificationsOpen) {
      this.loadNotifications();
    }
  }

  marcarComoLeidas(event: Event): void {
    event.stopPropagation();
    if (!this.currentRole) {
      return;
    }

    this.isLoadingNotifications = true;
    this.notificationService
      .marcarComoLeidas(this.currentRole)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: () => this.loadNotifications(),
        error: () => {
          this.isLoadingNotifications = false;
        },
      });
  }

  trackByNotification(index: number, notification: NotificationItem): string {
    return notification.id || `${notification.entidad}-${notification.entidadId}-${index}`;
  }

  handleImageError(event: any): void {
    (event.target as HTMLImageElement).src = 'assets/images/default-avatar.png';
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }

  private loadNotifications(): void {
    if (!this.currentRole) {
      return;
    }

    this.isLoadingNotifications = true;
    this.notificationService
      .loadNotifications(this.currentRole)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: () => {
          this.isLoadingNotifications = false;
        },
        error: () => {
          this.isLoadingNotifications = false;
        },
      });
  }
}

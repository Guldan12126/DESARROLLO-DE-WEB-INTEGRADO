import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Subject, takeUntil } from 'rxjs';

import { AuthService } from '../../../core/services/auth.service';

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
  avatarUrl: string = 'assets/Images/default-avatar.png';
  private readonly destroy$ = new Subject<void>();

  constructor(
    private router: Router,
    private authService: AuthService,
  ) {}

  ngOnInit(): void {
    this.authService.session$
      .pipe(takeUntil(this.destroy$))
      .subscribe((session) => {
        this.nombreUsuario = session?.nombre ?? 'Usuario';
        this.rolUsuario = session?.rol ?? 'Invitado';
      });
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  toggleDropdown(): void {
    this.isDropdownOpen = !this.isDropdownOpen;
  }

  handleImageError(event: any): void {
    (event.target as HTMLImageElement).src = 'assets/images/default-avatar.png';
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}

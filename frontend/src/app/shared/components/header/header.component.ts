import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrl: '../../../../scss/_header.scss',
  standalone: false
})
export class HeaderComponent implements OnInit {
  nombreUsuario: string = 'Usuario';
  rolUsuario: string = 'Rol';
  isDropdownOpen: boolean = false;
  avatarUrl: string = 'assets/Images/default-avatar.png';

  constructor(private router: Router) {}

  ngOnInit(): void {
    const nombre = localStorage.getItem('nombreUsuario');
    const rol = localStorage.getItem('role');
    
    if (nombre) {
      this.nombreUsuario = nombre;
    }
    if (rol) {
      this.rolUsuario = rol;
    }
  }

  toggleDropdown(): void {
    this.isDropdownOpen = !this.isDropdownOpen;
  }

  handleImageError(event: any): void {
    (event.target as HTMLImageElement).src = 'assets/images/default-avatar.png';
  }

  logout(): void {
    // Limpiar todos los datos de sesión (token, rol, nombre)
    localStorage.clear();
    // Redirigir a la página de inicio de sesión
    this.router.navigate(['/login']);
  }
}
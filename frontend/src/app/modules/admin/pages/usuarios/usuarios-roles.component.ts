import { Component, OnInit } from '@angular/core';
import { UsuarioService } from '../../../../shared/services/usuario.service';
import { ToastService } from '../../../../shared/services/toast.service';

interface Rol {
  nombre: string;
  descripcion: string;
  permisos: string[];
  colorClass: string;
}

@Component({
  selector: 'app-usuarios-roles',
  standalone: false,
  templateUrl: './usuarios-roles.html',
  styleUrl: '../../../../../scss/_usuarios.scss'
})
export class UsuariosRolesComponent implements OnInit {
  roles: Rol[] = [];
  isLoading: boolean = false;
  isSaving: boolean = false; // Para el estado de guardado

  constructor(
    private usuarioService: UsuarioService,
    private toastService: ToastService
  ) {}

  ngOnInit(): void {
    this.cargarRoles();
  }

  cargarRoles(): void {
    this.isLoading = true;
    this.usuarioService.obtenerRoles().subscribe({
      next: (data) => {
        // Mapeamos los datos del backend y asignamos la clase de color según el nombre
        this.roles = data.map(rol => ({
          ...rol,
          colorClass: this.getRoleColorClass(rol.nombre)
        }));
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Error al cargar roles:', err);
        this.toastService.error('No se pudo conectar con el servidor de roles');
        this.isLoading = false;
      }
    });
  }

  onEditarDescripcion(rol: Rol): void {
    const nuevaDescripcion = prompt(`Editar descripción para el rol ${rol.nombre}:`, rol.descripcion);

    if (nuevaDescripcion !== null && nuevaDescripcion.trim() !== rol.descripcion.trim()) {
      this.isSaving = true;
      this.usuarioService.actualizarRol(rol.nombre, nuevaDescripcion.trim()).subscribe({
        next: (res) => {
          this.toastService.success(`Descripción del rol ${rol.nombre} actualizada.`);
          rol.descripcion = nuevaDescripcion.trim(); // Actualizar la UI directamente
          this.isSaving = false;
        },
        error: (err) => {
          console.error('Error al actualizar rol:', err);
          this.toastService.error(`Error al actualizar la descripción del rol ${rol.nombre}.`);
          this.isSaving = false;
        }
      });
    } else if (nuevaDescripcion !== null && nuevaDescripcion.trim() === rol.descripcion.trim()) {
      this.toastService.info('No se realizaron cambios en la descripción.');
    }
  }

  private getRoleColorClass(nombre: string): string {
    const classes: { [key: string]: string } = {
      'ADMIN': 'admin', 'MOZO': 'mozo', 'COCINA': 'cocina', 'CAJERO': 'cajero'
    };
    return classes[nombre.toUpperCase()] || 'admin'; 
  }
}
import { Component, OnInit, Inject } from '@angular/core';
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
    @Inject(UsuarioService) private usuarioService: UsuarioService,
    private toastService: ToastService
  ) {}

  ngOnInit(): void {
    this.cargarRoles();
  }

  cargarRoles(): void {
    this.isLoading = true;
    this.usuarioService.obtenerRoles().subscribe({
      next: (data) => {
        if (data && data.length > 0) {
          this.roles = data.map(rol => ({
            ...rol,
            colorClass: this.getRoleColorClass(rol.nombre)
          }));
        } else {
          // Si el backend está vacío, cargamos los roles base del sistema
          this.cargarRolesPorDefecto();
        }
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Error al cargar roles:', err);
        this.toastService.error('No se pudo conectar con el servidor de roles');
        this.isLoading = false;
      }
    });
  }

  private cargarRolesPorDefecto(): void {
    const rolesBase = [
      { nombre: 'ADMIN', descripcion: 'Gestión total del sistema, personal, inventario y reportes gerenciales.', permisos: ['Usuarios', 'Productos', 'Ventas', 'Caja', 'Reportes'], colorClass: 'admin' },
      { nombre: 'MOZO', descripcion: 'Atención de mesas, toma de pedidos y seguimiento de estados.', permisos: ['Mesas', 'Pedidos'], colorClass: 'mozo' },
      { nombre: 'CAJERO', descripcion: 'Control de ingresos, cobros presenciales y cierre de caja diario.', permisos: ['Caja', 'Ventas', 'Pedidos'], colorClass: 'cajero' },
      { nombre: 'COCINA', descripcion: 'Visualización de comandas en tiempo real y despacho de platos.', permisos: ['Tablero Cocina', 'Insumos'], colorClass: 'cocina' }
    ];
    
    this.roles = rolesBase;
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
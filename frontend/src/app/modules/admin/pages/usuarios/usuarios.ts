import { Component, OnInit, Inject } from '@angular/core';
import { ToastService } from '../../../../shared/services/toast.service';
import { UsuarioService } from '../../../../shared/services/usuario.service'; 

@Component({
  selector: 'app-usuarios',
  standalone: false,
  templateUrl: './usuarios-crear.html',
  styleUrl: '../../../../../scss/_usuarios.scss', 
})
export class UsuariosComponent implements OnInit {
  nombre: string = '';
  email: string = '';
  password: string = '';
  rol: string = '';

  constructor(
    private usuarioService: UsuarioService,
    private toastService: ToastService
  ) { }

  ngOnInit(): void { }

  onSubmit(): void {
    const usuarioData = {
      nombre: this.nombre,
      email: this.email,
      password: this.password,
      rol: this.rol
    };

    this.usuarioService.registrarUsuario(usuarioData).subscribe({ 
      next: (res) => {
        this.toastService.success('Personal registrado exitosamente');
        this.limpiarFormulario();
      },
      error: (err) => {
        console.error('Error al registrar:', err);
        if (err.status === 409 || (err.error && err.error.message && err.error.message.includes('email'))) {
          this.toastService.error('El correo electrónico ya está registrado en el sistema');
        } else {
          this.toastService.error('Error al procesar el registro. Intente de nuevo.');
        }
      }
    });
  }

  private limpiarFormulario() {
    this.nombre = '';
    this.email = '';
    this.password = '';
    this.rol = '';
  }
}

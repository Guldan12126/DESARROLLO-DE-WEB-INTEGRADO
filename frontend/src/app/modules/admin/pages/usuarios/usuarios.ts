import { Component, OnInit, Inject } from '@angular/core';
import { NgForm } from '@angular/forms';
import { ToastService } from '../../../../shared/services/toast.service';
import { UsuarioService } from '../../../../shared/services/usuario.service'; 
import { NotificationService } from '../../../../shared/services/notification.service';

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
    private toastService: ToastService,
    private notificationService: NotificationService
  ) { }

  ngOnInit(): void { }

  onSubmit(form: NgForm): void {
    const usuarioData = {
      nombre: this.nombre,
      email: this.email,
      password: this.password,
      rol: this.rol,
      activo: true
    };

    this.usuarioService.registrarUsuario(usuarioData).subscribe({ 
      next: (res) => {
        this.toastService.success('Personal registrado exitosamente');
        this.notificationService.actualizarConteo('ADMIN').subscribe();
        form.resetForm({
          rol: '' // Valor por defecto para el select
        });
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
}

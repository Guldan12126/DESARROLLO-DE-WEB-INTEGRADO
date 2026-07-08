import { Component, Inject } from '@angular/core';
import { Router } from '@angular/router';
import { MesaService } from '../../../../../shared/services/mesa.service';
import { ToastService } from '../../../../../shared/services/toast.service';

@Component({
  selector: 'app-mesas-crear',
  standalone: false,
  templateUrl: './mesas-crear.html',
  styleUrls: ['../../../../../../scss/_mesas-crear.scss']
})
export class MesasCrear {
  nuevaMesa: any = {
    numero: null,
    capacidad: 4,
    ubicacion: 'SALON_PRINCIPAL',
    activo: true
  };
  
  formErrors: any = {};
  isSaving: boolean = false;

  constructor(
    @Inject(MesaService) private mesaService: MesaService,
    private toastService: ToastService,
    private router: Router
  ) {}

  validarFormulario(): boolean {
    this.formErrors = {};
    if (!this.nuevaMesa.numero || this.nuevaMesa.numero <= 0) this.formErrors.numero = 'Ingrese un número válido.';
    if (!this.nuevaMesa.capacidad || this.nuevaMesa.capacidad <= 0) this.formErrors.capacidad = 'Ingrese la capacidad.';
    if (!this.nuevaMesa.ubicacion) this.formErrors.ubicacion = 'Seleccione una ubicación.';
    
    return Object.keys(this.formErrors).length === 0;
  }

  crearMesa(): void {
    if (!this.validarFormulario()) return;

    this.isSaving = true;
    this.mesaService.crearMesa(this.nuevaMesa).subscribe({
      next: () => {
        this.toastService.success(`Mesa #${this.nuevaMesa.numero} registrada exitosamente.`);
        this.router.navigate(['/admin/mesas/lista']);
      },
      error: (err) => {
        console.error(err);
        this.toastService.error('Ocurrió un error al registrar la mesa. ¿Quizás el número ya existe?');
        this.isSaving = false;
      }
    });
  }

  cancelar(): void {
    this.router.navigate(['/admin/mesas/lista']);
  }
}

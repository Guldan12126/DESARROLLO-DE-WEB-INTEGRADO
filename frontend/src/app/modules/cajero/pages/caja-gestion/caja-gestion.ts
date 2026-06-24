import { Component, OnInit } from '@angular/core';
import { CajaService } from '../../../../shared/services/caja.service';
import { ToastService } from '../../../../shared/services/toast.service';
import { AuthService } from '../../../../core/services/auth.service';

@Component({
  selector: 'app-caja-gestion',
  templateUrl: './caja-gestion.html',
  standalone: false
})
export class CajaGestionComponent implements OnInit {
  cajaAbierta: any = null;
  montoApertura: number = 0;
  montoCierre: number = 0;
  isLoading: boolean = false;

  constructor(
    private cajaService: CajaService,
    private toastService: ToastService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.verificarCaja();
  }

  verificarCaja(): void {
    this.isLoading = true;
    this.cajaService.obtenerCajaAbierta().subscribe({
      next: (caja) => {
        this.cajaAbierta = caja;
        this.isLoading = false;
      },
      error: () => {
        this.cajaAbierta = null;
        this.isLoading = false;
      }
    });
  }

  abrirCaja(): void {
    if (this.montoApertura <= 0) {
      this.toastService.error('El monto de apertura debe ser mayor a 0');
      return;
    }
    this.isLoading = true;
    const userId = this.authService.getUserId() || 1;
    this.cajaService.abrirCaja(userId, this.montoApertura).subscribe({
      next: (res) => {
        this.toastService.success('Caja abierta correctamente');
        this.verificarCaja();
      },
      error: (err) => {
        this.toastService.error(err.error?.message || 'Error al abrir la caja');
        this.isLoading = false;
      }
    });
  }

  cerrarCaja(): void {
    if (this.montoCierre < 0) {
      this.toastService.error('El monto de cierre no puede ser negativo');
      return;
    }
    this.isLoading = true;
    this.cajaService.cerrarCaja(this.cajaAbierta.id, this.montoCierre).subscribe({
      next: (res) => {
        this.toastService.success('Caja cerrada correctamente');
        this.cajaAbierta = null;
        this.montoApertura = 0;
        this.montoCierre = 0;
        this.isLoading = false;
      },
      error: (err) => {
        this.toastService.error(err.error?.message || 'Error al cerrar la caja');
        this.isLoading = false;
      }
    });
  }
}

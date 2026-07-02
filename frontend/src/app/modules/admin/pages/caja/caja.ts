import { Component, OnInit } from '@angular/core';
import { finalize } from 'rxjs';

import { AuthService } from '../../../../core/services/auth.service';
import { CajaService } from '../../../../shared/services/caja.service';
import { ToastService } from '../../../../shared/services/toast.service';

type MovimientoTipo = 'INGRESO' | 'EGRESO';

@Component({
  selector: 'app-caja',
  standalone: false,
  templateUrl: './caja.html',
  styleUrl: './caja.scss'
})
export class CajaComponent implements OnInit {
  loading = false;

  // Apertura
  montoApertura: number = 0;

  // Caja abierta
  cajaAbierta: any | null = null;
  cajaId: number | null = null;

  // Movimiento
  tipoMovimiento: MovimientoTipo = 'INGRESO';
  montoMovimiento: number = 0;
  descripcionMovimiento: string = '';

  // Cierre
  montoCierre: number = 0;

  // Cobro / calculo vuelto (solo para UI; el guardado real va como Movimiento INGRESO)
  montoCobrar: number = 0;
  montoRecibido: number = 0;
  metodoPago: string = 'Efectivo';

  constructor(
    private authService: AuthService,
    private cajaService: CajaService,
    private toastService: ToastService
  ) {}

  ngOnInit(): void {
    this.cargarCajaAbierta();
    // opcional: iniciar montoApertura/montoCobrar con valores por defecto
    this.montoApertura = 0;
  }

  get esCajero(): boolean {
    return this.authService.hasRole('CAJERO');
  }

  get vuelto(): number {
    return this.montoRecibido >= this.montoCobrar ? this.montoRecibido - this.montoCobrar : 0;
  }

  cargarCajaAbierta(): void {
    this.loading = true;
    this.cajaService.obtenerCajaAbierta().pipe(finalize(() => (this.loading = false))).subscribe({
      next: (caja) => {
        this.cajaAbierta = caja;
        this.cajaId = caja?.id ?? null;
        // saldo calculado esperado para precargar montoCierre (si aplica)
        this.montoCierre = 0;
        this.montoApertura = 0;
      },
      error: (err) => {
        // Si no hay caja abierta, backend retorna 204, Angular lo maneja como error en muchos casos.
        this.cajaAbierta = null;
        this.cajaId = null;
      }
    });
  }

  abrirCaja(): void {
    const usuarioId = this.authService.getUserId();
    if (!usuarioId) {
      this.toastService.error('No hay sesión de usuario.');
      return;
    }
    if (!this.montoApertura || this.montoApertura <= 0) {
      this.toastService.error('Ingrese un monto de apertura válido.');
      return;
    }

    this.loading = true;
    this.cajaService.abrirCaja(usuarioId, this.montoApertura)
      .pipe(finalize(() => (this.loading = false)))
      .subscribe({
        next: () => {
          this.toastService.success('Caja abierta correctamente.');
          this.cargarCajaAbierta();
        },
        error: (err) => {
          const msg = err?.error?.message ?? 'No se pudo abrir la caja.';
          this.toastService.error(msg);
        }
      });
  }

  registrarMovimiento(): void {
    if (!this.cajaId) return;
    if (!this.montoMovimiento || this.montoMovimiento <= 0) {
      this.toastService.error('Ingrese un monto válido.');
      return;
    }
    if (!this.descripcionMovimiento?.trim()) {
      this.toastService.error('Ingrese una descripción.');
      return;
    }

    this.loading = true;
    this.cajaService.registrarMovimiento(
      this.cajaId,
      this.tipoMovimiento,
      this.montoMovimiento,
      this.descripcionMovimiento.trim()
    )
      .pipe(finalize(() => (this.loading = false)))
      .subscribe({
        next: () => {
          this.toastService.success('Movimiento registrado.');
          // limpiar formulario
          this.montoMovimiento = 0;
          this.descripcionMovimiento = '';
        },
        error: (err) => {
          const msg = err?.error?.message ?? 'No se pudo registrar el movimiento.';
          this.toastService.error(msg);
        }
      });
  }

  // Mantengo el botón de “Confirmar Pago” como ingreso a caja
  procesarPago(): void {
    if (!this.cajaId) {
      this.toastService.error('Debe abrir la caja antes de procesar pagos.');
      return;
    }
    if (!this.montoCobrar || this.montoCobrar <= 0) {
      this.toastService.error('Ingrese el total a cobrar.');
      return;
    }
    if (this.metodoPago === 'Efectivo' && (!this.montoRecibido || this.montoRecibido < this.montoCobrar)) {
      this.toastService.error('El monto recibido es menor al monto a cobrar.');
      return;
    }

    // Solo registra el cobro como INGRESO.
    // (vuelto no impacta BD; se refleja en la UI)
    this.tipoMovimiento = 'INGRESO';
    this.montoMovimiento = this.montoCobrar;
    this.descripcionMovimiento = `Cobro (${this.metodoPago})`;
    this.registrarMovimiento();
  }

  generarComprobante(): void {
    this.toastService.info('Generación de comprobante no implementada en este módulo (UI).');
  }

  cerrarCaja(): void {
    if (!this.cajaId) {
      this.toastService.error('No hay caja abierta.');
      return;
    }
    // El backend requiere montoCierre.
    if (!this.montoCierre || this.montoCierre <= 0) {
      this.toastService.error('Ingrese el monto de cierre.');
      return;
    }

    this.loading = true;
    this.cajaService.cerrarCaja(this.cajaId, this.montoCierre)
      .pipe(finalize(() => (this.loading = false)))
      .subscribe({
        next: () => {
          this.toastService.success('Caja cerrada correctamente.');
          this.cajaAbierta = null;
          this.cajaId = null;
          this.cargarCajaAbierta();
        },
        error: (err) => {
          const msg = err?.error?.message ?? 'No se pudo cerrar la caja.';
          this.toastService.error(msg);
        }
      });
  }
}


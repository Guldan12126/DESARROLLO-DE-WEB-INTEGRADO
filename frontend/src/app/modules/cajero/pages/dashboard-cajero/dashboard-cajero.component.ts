import { Component, OnInit } from '@angular/core';
import { PedidoService } from '../../../../shared/services/pedido.service';
import { MesaService } from '../../../../shared/services/mesa.service';
import { ToastService } from '../../../../shared/services/toast.service';
import { VentaService } from '../../../../shared/services/venta.service';
import { CajaService } from '../../../../shared/services/caja.service';
import { AuthService } from '../../../../core/services/auth.service';
import { forkJoin, of } from 'rxjs';
import { catchError } from 'rxjs/operators';

@Component({
  selector: 'app-dashboard-cajero',
  templateUrl: './dashboard-cajero.html',
  styleUrl: '../../../../../scss/_dashboard.scss',
  standalone: false
})
export class DashboardCajeroComponent implements OnInit {
  nombreUsuario: string = 'Cajero';
  mesasPorCobrar: any[] = [];
  
  stats = {
    totalVentasHoy: 0,
    cajaSaldo: 450.00,
    pedidosPendientesCobro: 0
  };

  isLoading: boolean = false;
  
  // Estado del modal de cobro
  showPaymentModal: boolean = false;
  selectedMesa: any = null;
  selectedPedido: any = null;
  metodoPago: string = 'EFECTIVO';
  montoRecibido: number = 0;
  vuelto: number = 0;
  formError: string = '';

  constructor(
    private pedidoService: PedidoService,
    private mesaService: MesaService,
    private toastService: ToastService,
    private ventaService: VentaService,
    private cajaService: CajaService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    const nombre = localStorage.getItem('nombreUsuario');
    if (nombre) {
      this.nombreUsuario = nombre.split(' ')[0];
    }
    this.cargarDatos();
  }

  cargarDatos(): void {
    this.isLoading = true;
    
    this.mesaService.listarPendientes().subscribe({
      next: (mesas) => {
        if (mesas.length === 0) {
          this.mesasPorCobrar = [];
          this.stats.pedidosPendientesCobro = 0;
          this.cargarVentasYCaja();
          return;
        }

        // Para cada mesa pendiente, obtener el detalle de su pedido actual
        const peticionesPedidos = mesas.map(m => {
          if (m.pedidoActualId) {
            return this.pedidoService.obtenerPorId(m.pedidoActualId).pipe(
              catchError(() => of(null))
            );
          }
          return of(null);
        });

        forkJoin(peticionesPedidos).subscribe((pedidosResult) => {
          this.mesasPorCobrar = mesas.map((m, index) => {
            return {
              ...m,
              pedido: pedidosResult[index]
            };
          }).filter(m => m.pedido != null); // Solo mesas con pedido válido
          
          this.stats.pedidosPendientesCobro = this.mesasPorCobrar.length;
          this.cargarVentasYCaja();
        });
      },
      error: (err) => {
        console.error('Error al cargar mesas por cobrar:', err);
        this.toastService.error('Error al cargar datos del servidor.');
        this.isLoading = false;
      }
    });
  }

  cargarVentasYCaja(): void {
    // Obtener saldo de caja real
    this.cajaService.obtenerCajaAbierta().subscribe({
      next: (caja) => {
        if (caja) {
          this.stats.cajaSaldo = caja.montoApertura; 
        }
      },
      error: (err) => {
        console.error('No hay caja abierta', err);
        this.stats.cajaSaldo = 0;
      }
    });

    // Obtener ventas del día
    this.ventaService.obtenerVentasDelDia().subscribe({
      next: (res) => {
        const total = res?.total || 0;
        this.stats.totalVentasHoy = total;
        this.stats.cajaSaldo = this.stats.cajaSaldo === 0 ? total : this.stats.cajaSaldo + total;
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Error al cargar ventas de hoy', err);
        this.stats.totalVentasHoy = 0;
        this.isLoading = false;
      }
    });
  }

  abrirCobro(mesa: any): void {
    this.selectedMesa = mesa;
    this.selectedPedido = mesa.pedido;
    this.metodoPago = 'EFECTIVO';
    this.montoRecibido = Math.ceil(this.selectedPedido.total);
    this.vuelto = 0;
    this.formError = '';
    this.showPaymentModal = true;
    this.calcularVuelto();
  }

  cerrarCobro(): void {
    this.showPaymentModal = false;
    this.selectedMesa = null;
    this.selectedPedido = null;
  }

  calcularVuelto(): void {
    this.formError = '';
    if (this.metodoPago === 'EFECTIVO') {
      if (this.montoRecibido < this.selectedPedido.total) {
        this.vuelto = 0;
        this.formError = 'El monto recibido es menor que el total a pagar.';
      } else {
        this.vuelto = parseFloat((this.montoRecibido - this.selectedPedido.total).toFixed(2));
      }
    } else {
      this.vuelto = 0;
    }
  }

  confirmarPago(): void {
    if (this.metodoPago === 'EFECTIVO' && this.montoRecibido < this.selectedPedido.total) {
      this.formError = 'No se puede procesar el pago. El monto recibido es insuficiente.';
      this.toastService.error('Monto recibido insuficiente.');
      return;
    }

    this.isLoading = true;
    const pedidoId = this.selectedPedido.id;
    const cajeroId = this.authService.getUserId() || 1; // Usar el ID del usuario actual o 1 como fallback temporal
    
    // El monto recibido real (si es tarjeta/yape, se asume exacto al total)
    const montoFinalRecibido = this.metodoPago === 'EFECTIVO' ? this.montoRecibido : this.selectedPedido.total;

    this.ventaService.registrarVenta(pedidoId, this.metodoPago, montoFinalRecibido, cajeroId).subscribe({
      next: (res) => {
        this.toastService.success(`¡Cuenta de Mesa ${this.selectedMesa.numero} cobrada y comprobante generado!`);
        this.cerrarCobro();
        this.cargarDatos(); // Recargar pedidos, caja y total del día
      },
      error: (err) => {
        console.error('Error al procesar pago:', err);
        this.toastService.error(err.error?.message || 'Error al procesar el pago. ¿Hay una caja abierta?');
        this.isLoading = false;
      }
    });
  }

  cargarDatosPrueba(): void {
    // Ya no se usa. Borrado por limpieza.
  }
}

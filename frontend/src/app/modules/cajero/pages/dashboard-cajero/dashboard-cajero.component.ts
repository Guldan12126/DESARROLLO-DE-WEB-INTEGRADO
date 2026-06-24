import { Component, OnInit } from '@angular/core';
import { PedidoService } from '../../../../shared/services/pedido.service';
import { MesaService } from '../../../../shared/services/mesa.service';
import { ToastService } from '../../../../shared/services/toast.service';
import { VentaService } from '../../../../shared/services/venta.service';
import { CajaService } from '../../../../shared/services/caja.service';
import { AuthService } from '../../../../core/services/auth.service';

@Component({
  selector: 'app-dashboard-cajero',
  templateUrl: './dashboard-cajero.html',
  styleUrl: '../../../../../scss/_dashboard.scss',
  standalone: false
})
export class DashboardCajeroComponent implements OnInit {
  nombreUsuario: string = 'Cajero';
  pedidosPorCobrar: any[] = [];
  
  stats = {
    totalVentasHoy: 0,
    cajaSaldo: 450.00,
    pedidosPendientesCobro: 0
  };

  isLoading: boolean = false;
  
  // Estado del modal de cobro
  showPaymentModal: boolean = false;
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
    
    this.pedidoService.listarActivos().subscribe({
      next: (pedidos) => {
        // Pedidos listos o entregados que no han sido pagados
        this.pedidosPorCobrar = pedidos.filter(p => p.estado === 'ENTREGADO' || p.estado === 'LISTO');
        this.stats.pedidosPendientesCobro = this.pedidosPorCobrar.length;
        
        // Obtener saldo de caja real
        this.cajaService.obtenerCajaAbierta().subscribe({
          next: (caja) => {
            if (caja) {
              // Si la caja está abierta, sumamos los movimientos
              this.stats.cajaSaldo = caja.montoApertura; // Necesitamos endpoint de balance o calcularlo en backend
              // Ocultamos un poco esto asumiendo q se calculará, por ahora dejamos el monto base + ventas
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
      },
      error: (err) => {
        console.error('Error al cargar pedidos por cobrar:', err);
        this.toastService.error('Error al cargar datos del servidor.');
        this.isLoading = false;
      }
    });
  }

  abrirCobro(pedido: any): void {
    this.selectedPedido = pedido;
    this.metodoPago = 'EFECTIVO';
    this.montoRecibido = Math.ceil(pedido.total);
    this.vuelto = 0;
    this.formError = '';
    this.showPaymentModal = true;
    this.calcularVuelto();
  }

  cerrarCobro(): void {
    this.showPaymentModal = false;
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
        this.toastService.success(`¡Pedido #${pedidoId} pagado y comprobante generado!`);
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

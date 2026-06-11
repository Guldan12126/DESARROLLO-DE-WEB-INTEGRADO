import { Component, OnInit } from '@angular/core';
import { PedidoService } from '../../../../shared/services/pedido.service';
import { MesaService } from '../../../../shared/services/mesa.service';
import { ToastService } from '../../../../shared/services/toast.service';

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
    private toastService: ToastService
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
        
        // Calcular ventas ficticias del día (o reales si hay pagadas en la base de datos)
        // Por ahora sumamos un valor base
        this.stats.totalVentasHoy = 780.00;
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Error al cargar pedidos por cobrar:', err);
        this.toastService.error('Error al cargar datos del servidor. Usando datos de prueba.');
        this.cargarDatosPrueba();
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
    const mesaId = this.selectedPedido.mesa?.id;

    // 1. Actualizar estado del pedido a PAGADO
    this.pedidoService.actualizarEstado(pedidoId, 'PAGADO').subscribe({
      next: () => {
        // 2. Liberar la mesa correspondiente
        if (mesaId) {
          this.mesaService.liberarMesa(mesaId).subscribe({
            next: () => {
              this.toastService.success(`¡Pedido #${pedidoId} pagado y mesa liberada con éxito!`);
              // Sumar al saldo de caja
              this.stats.cajaSaldo += this.selectedPedido.total;
              this.cerrarCobro();
              this.cargarDatos();
            },
            error: (err) => {
              console.error('Error al liberar mesa:', err);
              this.toastService.success(`¡Pedido #${pedidoId} pagado! (Mesa requiere liberación manual)`);
              this.cerrarCobro();
              this.cargarDatos();
            }
          });
        } else {
          this.toastService.success(`¡Pedido #${pedidoId} pagado con éxito!`);
          this.cerrarCobro();
          this.cargarDatos();
        }
      },
      error: (err) => {
        console.error('Error al procesar pago:', err);
        this.toastService.error('Error al procesar el pago.');
        this.isLoading = false;
      }
    });
  }

  cargarDatosPrueba(): void {
    this.stats = {
      totalVentasHoy: 650.00,
      cajaSaldo: 450.00,
      pedidosPendientesCobro: 2
    };

    this.pedidosPorCobrar = [
      {
        id: 301,
        mesa: { id: 1, numero: 2 },
        estado: 'ENTREGADO',
        detalles: [
          { producto: { nombre: 'Pollo Chijaukay' }, cantidad: 1 },
          { producto: { nombre: 'Arroz Chaufa Pork' }, cantidad: 2 }
        ],
        total: 68.50
      },
      {
        id: 302,
        mesa: { id: 3, numero: 7 },
        estado: 'ENTREGADO',
        detalles: [
          { producto: { nombre: 'Kam Lu Wantan Especial' }, cantidad: 1 },
          { producto: { nombre: 'Chicha Morada Jarra' }, cantidad: 1 }
        ],
        total: 55.00
      }
    ];
  }
}

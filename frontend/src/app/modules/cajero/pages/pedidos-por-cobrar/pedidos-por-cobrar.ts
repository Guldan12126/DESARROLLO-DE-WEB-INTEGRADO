import { Component, OnInit } from '@angular/core';
import { PedidoService } from '../../../../../shared/services/pedido.service';
import { VentaService } from '../../../../../shared/services/venta.service';
import { AuthService } from '../../../../../core/services/auth.service';
import { ToastService } from '../../../../../shared/services/toast.service';

@Component({
  selector: 'app-pedidos-por-cobrar',
  standalone: false,
  templateUrl: './pedidos-por-cobrar.html',
  styleUrl: './pedidos-por-cobrar.scss',
})
export class PedidosPorCobrar implements OnInit {
  pedidos: any[] = [];
  isLoading = false;
  isSaving = false;

  pedidoSeleccionado: any = null;
  metodoPago: string = 'EFECTIVO';
  montoRecibido: number = 0;

  constructor(
    private pedidoService: PedidoService,
    private ventaService: VentaService,
    private authService: AuthService,
    private toastService: ToastService
  ) {}

  ngOnInit() {
    this.cargarPedidos();
  }

  cargarPedidos() {
    this.isLoading = true;
    this.pedidoService.listarTodos().subscribe({
      next: (data) => {
        this.pedidos = data.filter(p => p.estado === 'LISTO' || p.estado === 'ENTREGADO');
        this.isLoading = false;
      },
      error: () => {
        this.toastService.error('Error al cargar pedidos por cobrar');
        this.isLoading = false;
      }
    });
  }

  abrirModalCobro(pedido: any) {
    this.pedidoSeleccionado = pedido;
    this.metodoPago = 'EFECTIVO';
    this.montoRecibido = this.calcularTotal(pedido);
  }

  cerrarModal() {
    this.pedidoSeleccionado = null;
  }

  calcularTotal(pedido: any): number {
    if (!pedido || !pedido.detalles) return 0;
    return pedido.detalles.reduce((acc: number, d: any) => acc + (d.precioUnitario * d.cantidad), 0);
  }

  registrarVenta() {
    if (!this.pedidoSeleccionado) return;

    const total = this.calcularTotal(this.pedidoSeleccionado);
    if (this.montoRecibido < total) {
      this.toastService.error('El monto recibido es menor al total del pedido.');
      return;
    }

    this.isSaving = true;
    const cajeroId = this.authService.getUserId() || 1;

    this.ventaService.registrarVenta(
      this.pedidoSeleccionado.id, 
      this.metodoPago, 
      this.montoRecibido, 
      cajeroId
    ).subscribe({
      next: (res) => {
        const vuelto = this.montoRecibido - total;
        this.toastService.success(`Cobro registrado. Vuelto: S/ ${vuelto.toFixed(2)}`);
        this.isSaving = false;
        this.cerrarModal();
        this.cargarPedidos();
      },
      error: (err) => {
        this.toastService.error(err.error?.message || 'Error al registrar la venta. Asegúrese de tener la caja abierta.');
        this.isSaving = false;
      }
    });
  }
}

import { Component, OnInit } from '@angular/core';
import { MesaService } from '../../../../shared/services/mesa.service';
import { PedidoService } from '../../../../shared/services/pedido.service';
import { ToastService } from '../../../../shared/services/toast.service';

@Component({
  selector: 'app-dashboard-mozo',
  templateUrl: './dashboard-mozo.html',
  styleUrl: '../../../../../scss/_dashboard.scss',
  standalone: false
})
export class DashboardMozoComponent implements OnInit {
  nombreUsuario: string = 'Gael';
  mesasActivas: any[] = [];
  pedidosActivos: any[] = [];
  
  stats = {
    totalMesas: 0,
    mesasOcupadas: 0,
    pedidosPendientes: 0,
    pedidosListos: 0
  };

  isLoading: boolean = false;

  constructor(
    private mesaService: MesaService,
    private pedidoService: PedidoService,
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
    
    // Cargar mesas
    this.mesaService.listarTodas().subscribe({
      next: (mesas) => {
        this.mesasActivas = mesas;
        this.stats.totalMesas = mesas.length;
        this.stats.mesasOcupadas = mesas.filter(m => m.estado === 'OCUPADA' || m.estado === 'PENDIENTE_PAGO').length;
        
        // Cargar pedidos activos
        this.pedidoService.listarActivos().subscribe({
          next: (pedidos) => {
            this.pedidosActivos = pedidos;
            this.stats.pedidosPendientes = pedidos.filter(p => p.estado === 'PENDIENTE' || p.estado === 'EN_PREPARACION').length;
            this.stats.pedidosListos = pedidos.filter(p => p.estado === 'LISTO').length;
            this.isLoading = false;
          },
          error: (err) => {
            console.error('Error al cargar pedidos activos:', err);
            this.toastService.error('Error al cargar pedidos. Usando datos de prueba.');
            this.cargarDatosPrueba();
            this.isLoading = false;
          }
        });
      },
      error: (err) => {
        console.error('Error al cargar mesas:', err);
        this.toastService.error('Error de conexión con el servidor.');
        this.cargarDatosPrueba();
        this.isLoading = false;
      }
    });
  }

  entregarPedido(id: number): void {
    this.pedidoService.actualizarEstado(id, 'ENTREGADO').subscribe({
      next: () => {
        this.toastService.success('¡Pedido entregado con éxito!');
        this.cargarDatos();
      },
      error: (err) => {
        console.error('Error al entregar pedido:', err);
        this.toastService.error('No se pudo actualizar el estado del pedido.');
      }
    });
  }

  cargarDatosPrueba(): void {
    this.stats = {
      totalMesas: 12,
      mesasOcupadas: 4,
      pedidosPendientes: 3,
      pedidosListos: 1
    };
    
    this.mesasActivas = [
      { id: 1, numero: 1, capacidad: 4, estado: 'OCUPADA' },
      { id: 2, numero: 2, capacidad: 2, estado: 'LIBRE' },
      { id: 3, numero: 3, capacidad: 6, estado: 'PENDIENTE_PAGO' },
      { id: 4, numero: 4, capacidad: 4, estado: 'LIBRE' }
    ];

    this.pedidosActivos = [
      {
        id: 101,
        mesa: { numero: 1 },
        estado: 'EN_PREPARACION',
        detalles: [
          { producto: { nombre: 'Arroz Chaufa de Pollo' }, cantidad: 2 },
          { producto: { nombre: 'Lomo Saltado' }, cantidad: 1 }
        ],
        total: 75.00
      },
      {
        id: 102,
        mesa: { numero: 3 },
        estado: 'LISTO',
        detalles: [
          { producto: { nombre: 'Tallarín Saltado de Carne' }, cantidad: 1 },
          { producto: { nombre: 'Chijaukay' }, cantidad: 1 }
        ],
        total: 58.00
      }
    ];
  }
}

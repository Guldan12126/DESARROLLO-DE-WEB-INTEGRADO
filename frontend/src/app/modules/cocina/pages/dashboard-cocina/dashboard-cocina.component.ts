import { Component, OnInit } from '@angular/core';
import { PedidoService } from '../../../../shared/services/pedido.service';
import { ToastService } from '../../../../shared/services/toast.service';

@Component({
  selector: 'app-dashboard-cocina',
  templateUrl: './dashboard-cocina.html',
  styleUrl: '../../../../../scss/_dashboard.scss',
  standalone: false
})
export class DashboardCocinaComponent implements OnInit {
  nombreUsuario: string = 'Andrea';
  pedidosPendientes: any[] = [];
  pedidosPreparando: any[] = [];
  
  stats = {
    pendientes: 0,
    preparando: 0,
    completadosHoy: 0
  };

  isLoading: boolean = false;

  constructor(
    private pedidoService: PedidoService,
    private toastService: ToastService
  ) {}

  ngOnInit(): void {
    const nombre = localStorage.getItem('nombreUsuario');
    if (nombre) {
      this.nombreUsuario = nombre.split(' ')[0];
    }
    this.cargarPedidos();
  }

  cargarPedidos(): void {
    this.isLoading = true;
    
    this.pedidoService.listarActivos().subscribe({
      next: (pedidos) => {
        // Separar por estado
        this.pedidosPendientes = pedidos.filter(p => p.estado === 'PENDIENTE');
        this.pedidosPreparando = pedidos.filter(p => p.estado === 'EN_PREPARACION');
        
        // Actualizar estadísticas
        this.stats.pendientes = this.pedidosPendientes.length;
        this.stats.preparando = this.pedidosPreparando.length;
        
        // Obtener completados (LISTO, ENTREGADO) para contar hoy
        this.stats.completadosHoy = pedidos.filter(p => p.estado === 'LISTO' || p.estado === 'ENTREGADO' || p.estado === 'PAGADO').length;
        
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Error al cargar pedidos en cocina:', err);
        this.toastService.error('Error al conectar con el servidor. Usando datos de prueba.');
        this.cargarDatosPrueba();
        this.isLoading = false;
      }
    });
  }

  iniciarPreparacion(id: number): void {
    this.pedidoService.actualizarEstado(id, 'EN_PREPARACION').subscribe({
      next: () => {
        this.toastService.success('¡Pedido en preparación!');
        this.cargarPedidos();
      },
      error: (err) => {
        console.error('Error al iniciar preparación:', err);
        this.toastService.error('No se pudo actualizar el estado del pedido.');
      }
    });
  }

  terminarPreparacion(id: number): void {
    this.pedidoService.actualizarEstado(id, 'LISTO').subscribe({
      next: () => {
        this.toastService.success('¡Pedido listo para servir!');
        this.cargarPedidos();
      },
      error: (err) => {
        console.error('Error al terminar preparación:', err);
        this.toastService.error('No se pudo actualizar el estado del pedido.');
      }
    });
  }

  cargarDatosPrueba(): void {
    this.stats = {
      pendientes: 2,
      preparando: 1,
      completadosHoy: 5
    };

    this.pedidosPendientes = [
      {
        id: 201,
        mesa: { numero: 4 },
        estado: 'PENDIENTE',
        detalles: [
          { producto: { nombre: 'Kam Lu Wantan' }, cantidad: 1 },
          { producto: { nombre: 'Arroz Chaufa Especial' }, cantidad: 1 }
        ]
      },
      {
        id: 202,
        mesa: { numero: 2 },
        estado: 'PENDIENTE',
        detalles: [
          { producto: { nombre: 'Sopa Wantan Especial' }, cantidad: 2 }
        ]
      }
    ];

    this.pedidosPreparando = [
      {
        id: 200,
        mesa: { numero: 5 },
        estado: 'EN_PREPARACION',
        detalles: [
          { producto: { nombre: 'Chaufa de Chancho Asado' }, cantidad: 1 },
          { producto: { nombre: 'Pollo con Verduras' }, cantidad: 1 }
        ]
      }
    ];
  }
}

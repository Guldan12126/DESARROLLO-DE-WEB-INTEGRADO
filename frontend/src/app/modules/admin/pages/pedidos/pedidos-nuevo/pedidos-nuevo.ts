import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { PedidoService } from '../../../../../shared/services/pedido.service';
import { MesaService } from '../../../../../shared/services/mesa.service';
import { ProductoService } from '../../../../../shared/services/producto.service';
import { ToastService } from '../../../../../shared/services/toast.service';

@Component({
  selector: 'app-pedidos-nuevo',
  standalone: false,
  templateUrl: './pedidos-nuevo.html',
  styleUrl: './pedidos-nuevo.scss'
})
export class PedidosNuevo implements OnInit {
  // Datos maestros
  mesasDisponibles: any[] = [];
  productos: any[] = [];
  productosFiltrados: any[] = [];
  categoriaFiltro: string = 'TODOS';

  // Estado del nuevo pedido
  mesaSeleccionadaId: number | null = null;
  carrito: { producto: any, cantidad: number, subtotal: number }[] = [];
  isSaving: boolean = false;

  constructor(
    private pedidoService: PedidoService,
    private mesaService: MesaService,
    private productoService: ProductoService,
    private toastService: ToastService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.cargarMesas();
    this.cargarProductos();
  }

  cargarMesas(): void {
    // Para un pedido nuevo, normalmente listamos mesas DISPONIBLES
    this.mesaService.listarActivas().subscribe((data: any[]) => {
      // Filtrar solo las que no están ocupadas (o todas si permites agregar a ocupadas)
      this.mesasDisponibles = data.filter(m => m.estado === 'DISPONIBLE' || m.estado === 'PENDIENTE_PAGO');
    });
  }

  cargarProductos(): void {
    this.productoService.listarTodos().subscribe((data: any[]) => {
      // Solo productos activos y con stock > 0
      this.productos = data.filter(p => p.activo && p.stock > 0);
      this.productosFiltrados = [...this.productos];
    });
  }

  filtrarPorCategoria(cat: string): void {
    this.categoriaFiltro = cat;
    if (cat === 'TODOS') {
      this.productosFiltrados = [...this.productos];
    } else {
      this.productosFiltrados = this.productos.filter(p => p.categoria?.nombre === cat);
    }
  }

  agregarAlCarrito(producto: any): void {
    const itemExistente = this.carrito.find(item => item.producto.id === producto.id);
    
    if (itemExistente) {
      if (itemExistente.cantidad < producto.stock) {
        itemExistente.cantidad++;
        itemExistente.subtotal = itemExistente.cantidad * itemExistente.producto.precio;
      } else {
        this.toastService.error(`No hay más stock disponible para ${producto.nombre}.`);
      }
    } else {
      this.carrito.push({
        producto: producto,
        cantidad: 1,
        subtotal: producto.precio
      });
    }
  }

  quitarDelCarrito(index: number): void {
    this.carrito.splice(index, 1);
  }

  get totalCarrito(): number {
    return this.carrito.reduce((acc, item) => acc + item.subtotal, 0);
  }

  // ==== FLUJO DE CREACIÓN EN EL BACKEND ====
  // 1. Crear el Pedido (asignando mesa)
  // 2. Iterar sobre el carrito haciendo POST /detalle
  async confirmarPedido(): Promise<void> {
    if (!this.mesaSeleccionadaId) {
      this.toastService.error('Debe seleccionar una mesa para crear el pedido.');
      return;
    }
    if (this.carrito.length === 0) {
      this.toastService.error('Debe agregar al menos un producto al pedido.');
      return;
    }

    this.isSaving = true;
    
    try {
      // 1. Crear el pedido base
      // Asignamos usuario con id 1 por defecto para satisfacer la restricción not-null de la BD
      const nuevoPedidoPayload = { 
        mesa: { id: this.mesaSeleccionadaId },
        usuario: { id: 1 }
      };
      const pedidoCreado = await this.pedidoService.crearPedido(nuevoPedidoPayload).toPromise();
      
      const pedidoId = pedidoCreado.id;

      // 2. Agregar cada detalle (usando for...of para asegurar que terminen o usar Promise.all)
      for (const item of this.carrito) {
        await this.pedidoService.agregarDetalle(pedidoId, item.producto.id, item.cantidad).toPromise();
      }

      this.toastService.success(`Pedido #${pedidoId} creado exitosamente.`);
      this.router.navigate(['/admin/pedidos/lista']);
      
    } catch (err: any) {
      console.error(err);
      this.toastService.error('Ocurrió un error al procesar el pedido. Revise el inventario.');
      this.isSaving = false;
    }
  }

  cancelar(): void {
    this.router.navigate(['/admin/pedidos/lista']);
  }
}

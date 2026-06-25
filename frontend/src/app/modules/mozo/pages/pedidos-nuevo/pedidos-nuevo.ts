import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ProductoService } from '../../../../shared/services/producto.service';
import { MesaService } from '../../../../shared/services/mesa.service';
import { PedidoService } from '../../../../shared/services/pedido.service';
import { ToastService } from '../../../../shared/services/toast.service';
import { forkJoin, of } from 'rxjs';
import { catchError, map, switchMap } from 'rxjs/operators';

@Component({
  selector: 'app-pedidos-nuevo',
  templateUrl: './pedidos-nuevo.html',
  styleUrl: './pedidos-nuevo.scss',
  standalone: false
})
export class PedidosNuevoComponent implements OnInit {
  mesasLibres: any[] = [];
  mesaSeleccionada: number | null = null;
  pedidoIdSeleccionado: number | null = null;
  
  productos: any[] = [];
  categorias: string[] = [];
  filtroActual: string = 'TODOS';
  
  carrito: any[] = [];
  
  isLoading: boolean = false;
  isSubmitting: boolean = false;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private productoService: ProductoService,
    private mesaService: MesaService,
    private pedidoService: PedidoService,
    private toastService: ToastService
  ) {}

  ngOnInit(): void {
    this.cargarDatosIniciales();
    
    // Ver si venimos desde el mapa con una mesa seleccionada
    this.route.queryParams.subscribe(params => {
      if (params['mesaId']) {
        this.mesaSeleccionada = Number(params['mesaId']);
      } else if (params['mesa']) { // Soporte para links antiguos
        this.mesaSeleccionada = Number(params['mesa']);
      }

      if (params['pedidoId']) {
        this.pedidoIdSeleccionado = Number(params['pedidoId']);
      }
    });
  }

  cargarDatosIniciales(): void {
    this.isLoading = true;
    
    forkJoin({
      mesas: this.mesaService.listarTodas().pipe(catchError(() => of([]))),
      productos: this.productoService.listarActivos().pipe(catchError(() => of([])))
    }).subscribe({
      next: (res: any) => {
        this.mesasLibres = res.mesas.filter((m: any) => m.estado === 'DISPONIBLE');
        this.productos = res.productos;
        this.categorias = Array.from(new Set(res.productos.map((p: any) => p.categoria?.nombre))).filter(c => c) as string[];
        this.isLoading = false;
      },
      error: () => {
        this.toastService.error('Error al cargar datos. Refresque la página.');
        this.isLoading = false;
      }
    });
  }

  get productosFiltrados(): any[] {
    if (this.filtroActual === 'TODOS') return this.productos;
    return this.productos.filter(p => p.categoria?.nombre === this.filtroActual);
  }

  setFiltro(cat: string): void {
    this.filtroActual = cat;
  }

  agregarAlCarrito(producto: any): void {
    if (producto.stock <= 0) {
      this.toastService.warn('Producto sin stock');
      return;
    }
    
    const item = this.carrito.find(i => i.producto.id === producto.id);
    if (item) {
      if (item.cantidad >= producto.stock) {
         this.toastService.warn('Stock máximo alcanzado');
         return;
      }
      item.cantidad++;
      item.subtotal = item.cantidad * producto.precio;
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

  disminuirCantidad(index: number): void {
    const item = this.carrito[index];
    if (item.cantidad > 1) {
      item.cantidad--;
      item.subtotal = item.cantidad * item.producto.precio;
    } else {
      this.quitarDelCarrito(index);
    }
  }

  aumentarCantidad(index: number): void {
    const item = this.carrito[index];
    if (item.cantidad >= item.producto.stock) {
      this.toastService.warn('Stock máximo alcanzado');
      return;
    }
    item.cantidad++;
    item.subtotal = item.cantidad * item.producto.precio;
  }

  get totalCarrito(): number {
    return this.carrito.reduce((acc, item) => acc + item.subtotal, 0);
  }

  enviarComanda(): void {
    if (!this.mesaSeleccionada && !this.pedidoIdSeleccionado) {
      this.toastService.warn('Por favor seleccione una mesa.');
      return;
    }
    if (this.carrito.length === 0) {
      this.toastService.warn('El carrito está vacío.');
      return;
    }

    this.isSubmitting = true;
    const usuarioId = Number(localStorage.getItem('userId')) || 1; 

    // Flujo 1: Agregar a pedido existente
    if (this.pedidoIdSeleccionado) {
      const peticiones = this.carrito.map(item => 
        this.pedidoService.agregarDetalle(this.pedidoIdSeleccionado!, item.producto.id, item.cantidad)
      );
      
      forkJoin(peticiones).subscribe({
        next: () => {
          this.toastService.success('¡Platos añadidos a la mesa exitosamente!');
          this.isSubmitting = false;
          this.router.navigate(['/mozo/mesas/mapa']);
        },
        error: (err) => {
          console.error('Error al agregar detalles', err);
          this.toastService.error('Hubo un error al añadir los platos.');
          this.isSubmitting = false;
        }
      });
      return;
    }

    // Flujo 2: Crear nuevo pedido
    const nuevoPedido = {
      mesa: { id: this.mesaSeleccionada },
      usuario: { id: usuarioId }
    };

    this.pedidoService.crearPedido(nuevoPedido).pipe(
      switchMap(pedidoCreado => {
        const peticiones = this.carrito.map(item => 
          this.pedidoService.agregarDetalle(pedidoCreado.id, item.producto.id, item.cantidad)
        );
        // Además, marcar la mesa como ocupada
        peticiones.push(this.mesaService.ocuparMesa(this.mesaSeleccionada!, pedidoCreado.id));
        return forkJoin(peticiones);
      })
    ).subscribe({
      next: () => {
        this.toastService.success('¡Comanda enviada a cocina exitosamente!');
        this.isSubmitting = false;
        this.router.navigate(['/mozo/mesas/mapa']);
      },
      error: (err) => {
        console.error('Error al procesar comanda', err);
        this.toastService.error('Hubo un error al enviar la comanda.');
        this.isSubmitting = false;
      }
    });
  }
}

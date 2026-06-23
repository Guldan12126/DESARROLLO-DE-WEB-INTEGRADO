import { Component, OnInit, Inject } from '@angular/core';
import { ProductoService } from '../../../../../shared/services/producto.service';
import { ToastService } from '../../../../../shared/services/toast.service';

@Component({
  selector: 'app-productos-stock',
  standalone: false,
  templateUrl: './productos-stock.html',
  styleUrl: './productos-stock.scss',
})
export class ProductosStock implements OnInit {
  productos: any[] = [];
  productosFiltrados: any[] = [];
  isLoading: boolean = false;
  
  // Filtros
  searchTerm: string = '';
  mostrarSoloBajoStock: boolean = false;

  // Estado de edición
  productoEditandoId: number | null = null;
  nuevoStockVal: number = 0;
  isSaving: boolean = false;

  constructor(
    @Inject(ProductoService) private productoService: ProductoService,
    private toastService: ToastService
  ) {}

  ngOnInit(): void {
    this.cargarProductos();
  }

  cargarProductos(): void {
    this.isLoading = true;
    if (this.mostrarSoloBajoStock) {
      // Por defecto limite 20 como lo define el backend
      this.productoService.listarStockBajo(20).subscribe({
        next: (data) => {
          this.productos = data;
          this.aplicarFiltros();
          this.isLoading = false;
        },
        error: (err) => {
          this.manejarErrorCarga(err);
        }
      });
    } else {
      this.productoService.listarTodos().subscribe({
        next: (data) => {
          this.productos = data;
          this.aplicarFiltros();
          this.isLoading = false;
        },
        error: (err) => {
          this.manejarErrorCarga(err);
        }
      });
    }
  }

  manejarErrorCarga(err: any): void {
    console.error('Error al cargar stock:', err);
    this.toastService.error('Error al cargar la lista de productos.');
    this.isLoading = false;
  }

  aplicarFiltros(): void {
    let resultado = [...this.productos];
    if (this.searchTerm.trim()) {
      const term = this.searchTerm.toLowerCase();
      resultado = resultado.filter(p =>
        p.nombre?.toLowerCase().includes(term) ||
        p.categoria?.nombre?.toLowerCase().includes(term)
      );
    }
    this.productosFiltrados = resultado;
  }

  toggleBajoStock(): void {
    this.mostrarSoloBajoStock = !this.mostrarSoloBajoStock;
    this.cargarProductos();
  }

  // ==== FUNCIONES DE EDICIÓN DE STOCK ====

  iniciarEdicion(producto: any): void {
    this.productoEditandoId = producto.id;
    this.nuevoStockVal = producto.stock;
  }

  cancelarEdicion(): void {
    this.productoEditandoId = null;
    this.nuevoStockVal = 0;
  }

  guardarStock(producto: any): void {
    if (this.nuevoStockVal < 0) {
      this.toastService.error('El stock no puede ser negativo.');
      return;
    }

    this.isSaving = true;
    this.productoService.actualizarStock(producto.id, this.nuevoStockVal).subscribe({
      next: (actualizado) => {
        // Actualizamos el objeto localmente
        producto.stock = actualizado.stock;
        this.toastService.success(`Stock de "${producto.nombre}" actualizado a ${actualizado.stock}.`);
        this.productoEditandoId = null;
        this.isSaving = false;
      },
      error: (err) => {
        console.error('Error al actualizar stock:', err);
        this.toastService.error('Ocurrió un error al actualizar el stock.');
        this.isSaving = false;
      }
    });
  }
}

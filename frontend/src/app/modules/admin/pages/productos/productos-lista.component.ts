import { Component, OnInit } from '@angular/core';
import { ProductoService } from '../../../../shared/services/producto.service';
import { ToastService } from '../../../../shared/services/toast.service';

@Component({
  selector: 'app-productos-lista',
  standalone: false,
  templateUrl: './productos-lista.html',
  styleUrl: '../../../../../scss/_productos.scss'
})
export class ProductosListaComponent implements OnInit {
  productos: any[] = [];
  productosFiltrados: any[] = [];
  searchTerm: string = '';
  filtroCategoria: string = '';
  isLoading: boolean = false;

  // Estado Modal Eliminar
  showDeleteModal: boolean = false;
  productoIdToDelete: number | null = null;
  productoNombreToDelete: string = '';

  // Estado Modal Editar
  showEditModal: boolean = false;
  productoEditando: any = null;
  editNombre: string = '';
  editPrecio: number = 0;
  editCategoria: string = '';
  editStock: number = 0;
  editDescripcion: string = '';
  editActivo: boolean = true;
  editErrors: { [key: string]: string } = {};
  isSaving: boolean = false;

  categorias = ['CHAUFA', 'SOPAS', 'TALLARINES', 'CARNES', 'MARISCOS', 'BEBIDAS', 'POSTRES', 'ENTRADAS', 'OTROS'];

  constructor(
    private productoService: ProductoService,
    private toastService: ToastService
  ) {}

  ngOnInit(): void {
    this.cargarProductos();
  }

  cargarProductos(): void {
    this.isLoading = true;
    this.productoService.listarTodos().subscribe({
      next: (data) => {
        this.productos = data;
        this.aplicarFiltros();
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Error al cargar productos:', err);
        this.toastService.error('Error al cargar la lista de productos.');
        this.isLoading = false;
      }
    });
  }

  aplicarFiltros(): void {
    let resultado = [...this.productos];
    if (this.searchTerm.trim()) {
      const term = this.searchTerm.toLowerCase();
      resultado = resultado.filter(p =>
        p.nombre?.toLowerCase().includes(term) ||
        p.categoria?.toLowerCase().includes(term)
      );
    }
    if (this.filtroCategoria) {
      resultado = resultado.filter(p => p.categoria === this.filtroCategoria);
    }
    this.productosFiltrados = resultado;
  }

  abrirEditar(producto: any): void {
    this.productoEditando = { ...producto };
    this.editNombre = producto.nombre || '';
    this.editPrecio = producto.precio || 0;
    this.editCategoria = producto.categoria || '';
    this.editStock = producto.stock || 0;
    this.editDescripcion = producto.descripcion || '';
    this.editActivo = producto.activo !== false;
    this.editErrors = {};
    this.showEditModal = true;
  }

  validarEdicion(): boolean {
    this.editErrors = {};
    if (!this.editNombre.trim()) {
      this.editErrors['nombre'] = 'El nombre es obligatorio.';
    } else if (this.editNombre.trim().length < 3) {
      this.editErrors['nombre'] = 'El nombre debe tener al menos 3 caracteres.';
    }
    if (!this.editPrecio || this.editPrecio <= 0) {
      this.editErrors['precio'] = 'El precio debe ser mayor a S/ 0.00.';
    }
    if (!this.editCategoria) {
      this.editErrors['categoria'] = 'Debe seleccionar una categoría.';
    }
    if (this.editStock < 0) {
      this.editErrors['stock'] = 'El stock no puede ser negativo.';
    }
    return Object.keys(this.editErrors).length === 0;
  }

  guardarEdicion(): void {
    if (!this.validarEdicion()) return;

    this.isSaving = true;
    const datos = {
      nombre: this.editNombre.trim(),
      precio: this.editPrecio,
      categoria: this.editCategoria,
      stock: this.editStock,
      descripcion: this.editDescripcion.trim(),
      activo: this.editActivo
    };

    this.productoService.actualizarProducto(this.productoEditando.id, datos).subscribe({
      next: () => {
        this.toastService.success(`Producto "${this.editNombre}" actualizado correctamente.`);
        this.cerrarEditModal();
        this.cargarProductos();
      },
      error: (err) => {
        console.error('Error al actualizar producto:', err);
        if (err.status === 409) {
          this.editErrors['nombre'] = 'Ya existe un producto con este nombre.';
        } else {
          this.toastService.error('Error al guardar los cambios. Intente de nuevo.');
        }
        this.isSaving = false;
      }
    });
  }

  cerrarEditModal(): void {
    this.showEditModal = false;
    this.productoEditando = null;
    this.isSaving = false;
  }

  confirmarEliminar(producto: any): void {
    this.productoIdToDelete = producto.id;
    this.productoNombreToDelete = producto.nombre;
    this.showDeleteModal = true;
  }

  eliminarProducto(): void {
    if (!this.productoIdToDelete) return;
    this.productoService.eliminarProducto(this.productoIdToDelete).subscribe({
      next: () => {
        this.toastService.success(`"${this.productoNombreToDelete}" eliminado correctamente.`);
        this.cerrarDeleteModal();
        this.cargarProductos();
      },
      error: (err) => {
        console.error('Error al eliminar producto:', err);
        this.toastService.error('No se pudo eliminar el producto. Puede estar en uso en algún pedido.');
        this.cerrarDeleteModal();
      }
    });
  }

  cerrarDeleteModal(): void {
    this.showDeleteModal = false;
    this.productoIdToDelete = null;
    this.productoNombreToDelete = '';
  }
}

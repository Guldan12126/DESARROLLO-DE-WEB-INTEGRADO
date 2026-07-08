import { Component, OnInit, Inject } from '@angular/core';
import { ProductoService } from '../../../../../shared/services/producto.service';
import { CategoriaService } from '../../../../../shared/services/categoria.service';
import { ToastService } from '../../../../../shared/services/toast.service';

@Component({
  selector: 'app-productos-lista',
  standalone: false,
  templateUrl: './productos-lista.html',
  styleUrl: '../../../../../../scss/_productos-lista.scss'
})
export class ProductosListaComponent implements OnInit {
  productos: any[] = [];
  productosFiltrados: any[] = [];
  searchTerm: string = '';
  filtroCategoria: string = ''; // Mantener para el filtro de categoría
  isLoading: boolean = false; // Mantener para el estado de carga de la tabla

  // Estado Modal Eliminar (Mantener)
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

  categorias: any[] = []; // Mantener para el filtro y el selector de categoría en el modal

  constructor(
    @Inject(ProductoService) private productoService: ProductoService,
    @Inject(CategoriaService) private categoriaService: CategoriaService,
    private toastService: ToastService
  ) {}

  ngOnInit(): void {
    this.cargarProductos();
    this.cargarCategorias();
  }

  cargarProductos(): void {
    this.isLoading = true;
    this.productoService.listarTodos().subscribe({
      next: (data: any[]) => {
        this.productos = data;
        this.aplicarFiltros();
        this.isLoading = false;
      },
      error: (err: any) => {
        console.error('Error al cargar productos:', err);
        this.toastService.error('Error al cargar la lista de productos.');
        this.isLoading = false;
      }
    });
  }

  cargarCategorias(): void {
    this.categoriaService.obtenerCategorias().subscribe({
      next: (data: any[]) => {
        this.categorias = data;
      },
      error: (err: any) => {
        console.error('Error al cargar categorías:', err);
      }
    });
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
    if (this.filtroCategoria) {
      resultado = resultado.filter(p => p.categoria?.nombre === this.filtroCategoria);
    }
    this.productosFiltrados = resultado;
  }

  abrirEditar(producto: any): void {
    this.productoEditando = { ...producto };
    this.editNombre = producto.nombre || '';
    this.editPrecio = producto.precio || 0;
    this.editCategoria = producto.categoria?.nombre || '';
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
    }
    if (!this.editPrecio || this.editPrecio <= 0) {
      this.editErrors['precio'] = 'El precio debe ser mayor a 0.';
    }
    if (!this.editCategoria) {
      this.editErrors['categoria'] = 'Debe seleccionar una categoría.';
    }
    return Object.keys(this.editErrors).length === 0;
  }

  guardarEdicion(): void {
    if (!this.validarEdicion()) return;

    this.isSaving = true;

    const datos = {
      nombre: this.editNombre.trim(),
      precio: this.editPrecio,
      categoria: { nombre: this.editCategoria },
      stock: this.editStock,
      descripcion: this.editDescripcion.trim(),
      activo: this.editActivo
    };

    // ✅ Corregido: el backend usa @RequestPart("producto"), requiere multipart/form-data
    // Enviamos el objeto como Blob JSON en la parte 'producto', igual que en el formulario de creación
    const formData = new FormData();
    formData.append('producto', new Blob([JSON.stringify(datos)], {
      type: 'application/json'
    }));

    this.productoService.actualizarProducto(this.productoEditando.id, formData).subscribe({
      next: () => {
        this.toastService.success(`Producto "${this.editNombre}" actualizado correctamente.`);
        this.cerrarEditModal();
        this.cargarProductos();
      },
      error: (err: any) => {
        console.error('Error al actualizar producto:', err);
        this.isSaving = false;
        this.toastService.error('Error al guardar los cambios.');
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
        this.toastService.success('Producto eliminado.');
        this.cerrarDeleteModal();
        this.cargarProductos();
      },
      error: (err: any) => {
        this.toastService.error('No se pudo eliminar el producto.');
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

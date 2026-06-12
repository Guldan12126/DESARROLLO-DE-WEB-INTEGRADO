import { Component, OnInit } from '@angular/core';
import { ToastService } from '../../../../../shared/services/toast.service';
import { CategoriaService } from '../../../../../core/services/categoria.service';

@Component({
  selector: 'app-productos-categorias',
  standalone: false,
  templateUrl: './productos-categorias.html',
  styleUrl: '../../../../../../scss/_productos-categorias.scss',
})
export class ProductosCategorias implements OnInit {
  showModal: boolean = false;
  isEditMode: boolean = false;
  searchTerm: string = '';
  isSaving: boolean = false;
  currentCategory: any = {
    id: null,
    nombre: '',
    descripcion: '',
    activo: true
  };
  formErrors: any = {};

  categorias: any[] = [];

  // Getter para filtrar categorías dinámicamente
  get categoriasFiltradas(): any[] {
    if (!this.searchTerm.trim()) {
      return this.categorias;
    }
    const term = this.searchTerm.toLowerCase();
    return this.categorias.filter(cat => 
      cat.nombre.toLowerCase().includes(term) || 
      (cat.descripcion && cat.descripcion.toLowerCase().includes(term))
    );
  }

  constructor(
    private categoriaService: CategoriaService,
    private toastService: ToastService
  ) {}

  ngOnInit(): void {
    this.cargarCategorias();
  }

  cargarCategorias(): void {
    this.isSaving = true;
    this.categoriaService.obtenerCategorias().subscribe({
      next: (data) => {
        this.categorias = data;
        this.isSaving = false;
      },
      error: (err) => {
        console.error('Error al cargar categorías:', err);
        this.toastService.error('Error al conectar con el servidor.');
        this.isSaving = false;
      }
    });
  }

  openCreateModal(): void {
    this.isEditMode = false;
    this.currentCategory = { id: null, nombre: '', descripcion: '', activo: true };
    this.formErrors = {};
    this.showModal = true;
  }

  openEditModal(categoria: any): void {
    this.isEditMode = true;
    this.currentCategory = { ...categoria }; // Copia para no modificar directamente
    this.formErrors = {};
    this.showModal = true;
  }

  closeModal(): void {
    this.showModal = false;
    this.isSaving = false;
  }

  guardarCategoria(): void {
    this.formErrors = {};
    if (!this.currentCategory.nombre.trim()) {
      this.formErrors.nombre = 'El nombre de la categoría es obligatorio.';
      this.toastService.error('Por favor, complete el nombre de la categoría.');
      return;
    }

    this.isSaving = true;

    const operacion = this.isEditMode 
      ? this.categoriaService.actualizarCategoria(this.currentCategory.id, this.currentCategory)
      : this.categoriaService.crearCategoria(this.currentCategory);

    operacion.subscribe({
      next: () => {
        this.toastService.success(`Categoría "${this.currentCategory.nombre}" guardada.`);
        this.cargarCategorias();
        this.closeModal();
      },
      error: (err) => {
        // Verificamos si el backend indica que el nombre ya existe
        if (err.message && err.message.includes('existe')) {          this.formErrors.nombre = 'Este nombre de categoría ya está registrado.';
        } else {
          console.error('Error al guardar:', err);
          this.toastService.error('No se pudo procesar la solicitud.');
        }
        this.isSaving = false;
      }
    });
  }

  eliminarCategoria(id: number): void {
    if (confirm('¿Está seguro de eliminar esta categoría?')) {
      this.categoriaService.eliminarCategoria(id).subscribe({
        next: () => {
          this.toastService.success('Categoría eliminada.');
          this.cargarCategorias();
        },
        error: () => this.toastService.error('Error: Verifique si tiene productos asociados.')
      });
    }
  }
}
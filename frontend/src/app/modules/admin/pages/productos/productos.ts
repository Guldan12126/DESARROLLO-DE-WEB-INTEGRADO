import { Component, OnInit, Inject } from '@angular/core';
import { Router } from '@angular/router';
import { ProductoService } from '../../../../shared/services/producto.service';
import { CategoriaService } from '../../../../shared/services/categoria.service';
import { ToastService } from '../../../../shared/services/toast.service';

@Component({
  selector: 'app-productos',
  standalone: false,
  templateUrl: './productos.html',
  styleUrl: '../../../../../scss/_productos.scss',
})
export class Productos implements OnInit {
  nuevoProducto: any = {
    nombre: '',
    precio: null,
    categoria: { id: '' },
    stock: 0,
    imagenUrl: '',
    descripcion: '',
    activo: true
  };

  categorias: any[] = [];
  formErrors: any = {};
  selectedFile: File | null = null;
  imagePreview: string | ArrayBuffer | null = null;
  isSaving: boolean = false;

  constructor(
    @Inject(ProductoService) private productoService: ProductoService,
    @Inject(CategoriaService) private categoriaService: CategoriaService,
    private toastService: ToastService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.cargarCategorias();
  }

  cargarCategorias(): void {
    this.categoriaService.obtenerCategorias().subscribe({
      next: (data) => this.categorias = data,
      error: () => this.toastService.error('Error al cargar las categorías.')
    });
  }

  onFileSelected(event: any): void {
    const file: File = event.target.files[0];
    if (file) {
      this.selectedFile = file;
      const reader = new FileReader();
      reader.onload = () => {
        this.imagePreview = reader.result;
      };
      reader.readAsDataURL(file);
    }
  }

  removeImage(): void {
    this.selectedFile = null;
    this.imagePreview = null;
  }

  registrarProducto(): void {
    if (!this.validarFormulario()) return;

    this.isSaving = true;

    // Creamos el FormData para enviar datos + archivo
    const formData = new FormData();
    
    // Agregamos el objeto producto como un Blob JSON (es el estándar para Multipart en Spring Boot)
    formData.append('producto', new Blob([JSON.stringify(this.nuevoProducto)], {
      type: 'application/json'
    }));

    // Si el usuario seleccionó una imagen, la adjuntamos
    if (this.selectedFile) {
      formData.append('imagen', this.selectedFile);
    }

    this.productoService.crearProducto(formData).subscribe({
      next: (res) => {
        this.toastService.success(`Producto "${res.nombre}" registrado exitosamente.`);
        this.router.navigate(['/admin/productos/lista']);
      },
      error: (err) => {
        console.error(err);
        if (err.status === 409) {
          this.formErrors.nombre = 'Este producto ya existe.';
        } else {
          this.toastService.error('Error al registrar el producto.');
        }
        this.isSaving = false;
      }
    });
  }

  validarFormulario(): boolean {
    this.formErrors = {};
    if (!this.nuevoProducto.nombre.trim()) this.formErrors.nombre = 'El nombre es obligatorio.';
    if (!this.nuevoProducto.precio || this.nuevoProducto.precio <= 0) this.formErrors.precio = 'Ingrese un precio válido.';
    if (!this.nuevoProducto.categoria.id) this.formErrors.categoria = 'Seleccione una categoría.';
    if (this.nuevoProducto.stock < 0) this.formErrors.stock = 'El stock no puede ser negativo.';
    
    return Object.keys(this.formErrors).length === 0;
  }

  cancelar(): void {
    this.router.navigate(['/admin/productos/lista']);
  }
}

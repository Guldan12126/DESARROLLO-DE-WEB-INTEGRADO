import { Component, OnInit } from '@angular/core';
import { ProductoService } from '../../../../shared/services/producto.service';

@Component({
  selector: 'app-recetario',
  templateUrl: './recetario.html',
  styleUrl: './recetario.scss',
  standalone: false
})
export class RecetarioComponent implements OnInit {
  productos: any[] = [];
  categorias: string[] = [];
  filtroActual: string = 'TODOS';
  isLoading: boolean = false;

  constructor(private productoService: ProductoService) {}

  ngOnInit(): void {
    this.cargarRecetario();
  }

  cargarRecetario(): void {
    this.isLoading = true;
    this.productoService.listarActivos().subscribe({
      next: (data) => {
        this.productos = data;
        // Extraer categorías únicas
        this.categorias = Array.from(new Set(data.map(p => p.categoria?.nombre))).filter(c => c) as string[];
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Error al cargar recetario', err);
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
}

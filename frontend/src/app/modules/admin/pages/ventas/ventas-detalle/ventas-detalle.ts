import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { VentaService } from '../../../../../shared/services/venta.service';
import { ToastService } from '../../../../../shared/services/toast.service';

@Component({
  selector: 'app-ventas-detalle',
  standalone: false,
  templateUrl: './ventas-detalle.html'
})
export class VentasDetalle implements OnInit {
  ventaId: number | null = null;
  venta: any = null;
  isLoading: boolean = false;
  isAnulando: boolean = false;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private ventaService: VentaService,
    private toastService: ToastService
  ) {}

  ngOnInit(): void {
    // Buscar ID en query params
    this.route.queryParams.subscribe(params => {
      if (params['id']) {
        this.ventaId = +params['id'];
        this.cargarVenta();
      } else {
        // Si no hay ID en la URL, redirigir a lista
        this.toastService.error('Seleccione una venta desde la lista para gestionar.');
        this.router.navigate(['/admin/ventas/lista']);
      }
    });
  }

  cargarVenta(): void {
    if (!this.ventaId) return;
    
    this.isLoading = true;
    this.ventaService.obtenerPorId(this.ventaId).subscribe({
      next: (data: any) => {
        this.venta = data;
        this.isLoading = false;
      },
      error: (err: any) => {
        console.error(err);
        this.toastService.error('Error al cargar la gestión de venta.');
        this.router.navigate(['/admin/ventas/lista']);
      }
    });
  }

  anularVenta(): void {
    if (!this.ventaId) return;

    if (confirm('¿Está absolutamente seguro de ANULAR esta venta? Esta acción no se puede deshacer y los reportes financieros se actualizarán.')) {
      this.isAnulando = true;
      // Simulamos cajeroId 1 para la anulación
      this.ventaService.anularVenta(this.ventaId, 1).subscribe({
        next: () => {
          this.toastService.success('Venta anulada correctamente.');
          this.router.navigate(['/admin/ventas/lista']);
        },
        error: (err: any) => {
          console.error(err);
          this.toastService.error('Error al intentar anular la venta.');
          this.isAnulando = false;
        }
      });
    }
  }

  volver(): void {
    this.router.navigate(['/admin/ventas/lista']);
  }
}

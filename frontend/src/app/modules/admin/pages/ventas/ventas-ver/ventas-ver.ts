import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { VentaService } from '../../../../../shared/services/venta.service';
import { ToastService } from '../../../../../shared/services/toast.service';

@Component({
  selector: 'app-ventas-ver',
  standalone: false,
  templateUrl: './ventas-ver.html',
  styleUrl: './ventas-ver.scss'
})
export class VentasVer implements OnInit {
  ventaId: number | null = null;
  venta: any = null;
  isLoading: boolean = false;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private ventaService: VentaService,
    private toastService: ToastService
  ) {}

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      if (params['id']) {
        this.ventaId = +params['id'];
        this.cargarVenta();
      } else {
        this.toastService.error('No se especificó un ID de venta.');
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
        this.toastService.error('Error al cargar los detalles del comprobante.');
        this.router.navigate(['/admin/ventas/lista']);
      }
    });
  }

  volver(): void {
    this.router.navigate(['/admin/ventas/lista']);
  }
}

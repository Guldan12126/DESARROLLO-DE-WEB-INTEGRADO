import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { PedidoService } from '../../../../../shared/services/pedido.service';
import { ToastService } from '../../../../../shared/services/toast.service';

@Component({
  selector: 'app-pedidos-ver',
  standalone: false,
  templateUrl: './pedidos-ver.html',
  styleUrl: './pedidos-ver.scss'
})
export class PedidosVer implements OnInit {
  pedidoId: number | null = null;
  pedido: any = null;
  isLoading: boolean = false;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private pedidoService: PedidoService,
    private toastService: ToastService
  ) {}

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      if (params['id']) {
        this.pedidoId = +params['id'];
        this.cargarPedido();
      } else {
        this.toastService.error('No se especificó un ID de pedido válido.');
        this.router.navigate(['/admin/pedidos/lista']);
      }
    });
  }

  cargarPedido(): void {
    if (!this.pedidoId) return;
    
    this.isLoading = true;
    this.pedidoService.obtenerPorId(this.pedidoId).subscribe({
      next: (data: any) => {
        this.pedido = data;
        this.isLoading = false;
      },
      error: (err: any) => {
        console.error(err);
        this.toastService.error('Error al cargar los detalles del pedido.');
        this.router.navigate(['/admin/pedidos/lista']);
      }
    });
  }

  volver(): void {
    this.router.navigate(['/admin/pedidos/lista']);
  }
}

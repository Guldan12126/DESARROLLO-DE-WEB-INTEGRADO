import { Component, OnInit } from '@angular/core';
import { ToastService } from '../../../../../shared/services/toast.service';
import { CajaService } from '../../../../../shared/services/caja.service';

@Component({
  selector: 'app-caja-movimientos',
  standalone: false,
  templateUrl: './caja-movimientos.html',
  styleUrl: '../../../../../../scss/_caja-movimientos.scss',
})
export class CajaMovimientos implements OnInit {
  movimientos: any[] = [];
  isLoading: boolean = false;

  constructor(
    private cajaService: CajaService,
    private toastService: ToastService
  ) {}

  ngOnInit(): void {
    this.cargarMovimientos();
  }

  cargarMovimientos(): void {
    this.isLoading = true;
    this.cajaService.obtenerMovimientos().subscribe({
      next: (data) => {
        this.movimientos = data;
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Error al cargar movimientos:', err);
        this.toastService.error('No se pudo conectar con el servidor para obtener los movimientos.');
        this.isLoading = false;
      }
    });
  }

  getMontoClase(tipo: string): string {
    return tipo === 'INGRESO' ? 'pos' : 'neg';
  }
}

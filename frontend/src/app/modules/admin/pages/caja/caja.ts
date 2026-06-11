import { Component } from '@angular/core';

@Component({
  selector: 'app-caja',
  standalone: false,
  templateUrl: './caja.html',
  styleUrl: './caja.scss'
})
export class CajaComponent {
  montoCobrar: number = 150.50;
  montoRecibido: number = 0;
  metodoPago: string = 'Efectivo';
  
  get vuelto(): number {
    return this.montoRecibido >= this.montoCobrar ? this.montoRecibido - this.montoCobrar : 0;
  }

  procesarPago() {
    if (this.montoRecibido >= this.montoCobrar || this.metodoPago !== 'Efectivo') {
      alert('Pago procesado correctamente. Generando comprobante...');
    } else {
      alert('El monto recibido es menor al monto a cobrar.');
    }
  }

  generarComprobante() {
    alert('Generando PDF del comprobante...');
  }

  cierreCaja() {
    alert('Realizando cierre de caja. Ingresos totales calculados.');
  }
}

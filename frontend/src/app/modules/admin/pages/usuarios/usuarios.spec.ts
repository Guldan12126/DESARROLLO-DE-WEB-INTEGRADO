import { ComponentFixture, TestBed } from '@angular/core/testing';
import { UsuariosComponent } from './usuarios'; // Importa el nombre correcto
import { FormsModule } from '@angular/forms';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ToastService } from '../../../../shared/services/toast.service';
import { UsuarioService } from '../../../../shared/services/usuario.service';

describe('UsuariosComponent', () => { 
  let component: UsuariosComponent;
  let fixture: ComponentFixture<UsuariosComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FormsModule, HttpClientTestingModule], 
      declarations: [UsuariosComponent], // Declara el componente
      providers: [ToastService, UsuarioService] // Provee los servicios
    })
    .compileComponents();

    fixture = TestBed.createComponent(UsuariosComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  // Aquí puedes añadir más pruebas para tu componente
  // Por ejemplo, para verificar que el formulario se envía correctamente
  // o que los mensajes de error se muestran.
});
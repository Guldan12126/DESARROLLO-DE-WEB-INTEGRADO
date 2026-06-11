package apf3.ChifaXinYan.Service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import apf3.ChifaXinYan.Model.Proveedor;
import apf3.ChifaXinYan.Repository.ProveedorRepository;

@Service
public class ProveedorService {

    private final ProveedorRepository proveedorRepository;

    public ProveedorService(ProveedorRepository proveedorRepository) {
        this.proveedorRepository = proveedorRepository;
    }

    @Transactional(readOnly = true)
    public List<Proveedor> listarTodos() {
        return proveedorRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Proveedor obtenerPorId(Long id) {
        return proveedorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado con ID: " + id));
    }

    @Transactional(rollbackFor = Exception.class)
    public Proveedor crearProveedor(Proveedor proveedor) {
        if (proveedorRepository.findByRuc(proveedor.getRuc()).isPresent()) {
            throw new RuntimeException("Ya existe un proveedor con el RUC: " + proveedor.getRuc());
        }
        return proveedorRepository.save(proveedor);
    }

    @Transactional(rollbackFor = Exception.class)
    public Proveedor actualizarProveedor(Long id, Proveedor datos) {
        Proveedor existente = obtenerPorId(id);
        existente.setNombre(datos.getNombre());
        existente.setRuc(datos.getRuc());
        existente.setTelefono(datos.getTelefono());
        existente.setEmail(datos.getEmail());
        existente.setDireccion(datos.getDireccion());
        return proveedorRepository.save(existente);
    }

    @Transactional(rollbackFor = Exception.class)
    public void eliminar(Long id) {
        if (!proveedorRepository.existsById(id)) {
            throw new RuntimeException("No se puede eliminar. Proveedor inexistente.");
        }
        proveedorRepository.deleteById(id);
    }
}
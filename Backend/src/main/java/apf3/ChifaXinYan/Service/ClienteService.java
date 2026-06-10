package apf3.ChifaXinYan.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import apf3.ChifaXinYan.Model.Cliente;
import apf3.ChifaXinYan.Repository.ClienteRepository;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Transactional(readOnly = true)
    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Cliente obtenerPorId(Long id) {
        return clienteRepository.findById(id).orElse(null);
    }

    @Transactional(rollbackFor = Exception.class)
    public Cliente crearCliente(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    @Transactional(rollbackFor = Exception.class)
    public Cliente actualizarCliente(Long id, Cliente datosActualizados) {
        Cliente existente = obtenerPorId(id);
        if (existente != null) {
            existente.setNombre(datosActualizados.getNombre());
            existente.setDocumento(datosActualizados.getDocumento());
            existente.setTelefono(datosActualizados.getTelefono());
            existente.setEmail(datosActualizados.getEmail());
            existente.setDireccion(datosActualizados.getDireccion());
            return clienteRepository.save(existente);
        }
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean eliminarCliente(Long id) {
        if (clienteRepository.existsById(id)) {
            clienteRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
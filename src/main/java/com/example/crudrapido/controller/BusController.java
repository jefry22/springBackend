package com.example.crudrapido.controller;

import com.example.crudrapido.dto.BusDTO;
import com.example.crudrapido.dto.MarcaDTO;
import com.example.crudrapido.dto.PagedResponseDTO;
import com.example.crudrapido.model.Bus;
import com.example.crudrapido.model.Marca;
import com.example.crudrapido.repository.BusRepository;
import com.example.crudrapido.repository.MarcaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/bus")
@CrossOrigin("http://localhost:5173/")
public class BusController {
    @Autowired
    private BusRepository busRepository;
    @Autowired
    private MarcaRepository marcaRepository;

    @GetMapping
    public PagedResponseDTO<BusDTO> listarBuses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Bus> busPage = busRepository.findAll(pageable);

        List<BusDTO> busDTOList = busPage.getContent().stream().map(bus -> {
            MarcaDTO marcaDTO = null;
            if (bus.getMarca() != null) {
                marcaDTO = new MarcaDTO(
                        bus.getMarca().getId(),
                        bus.getMarca().getNombre()
                );
            }


            return new BusDTO(
                    bus.getId(),
                    bus.getNumeroBus(),
                    bus.getPlaca(),
                    bus.getFechaCreacion(),
                    bus.getCaracteristica(),
                    bus.getActivo(),
                    marcaDTO
            );
        }).collect(Collectors.toList());

        // Retornamos la estructura con datos paginados
        return new PagedResponseDTO<>(
                busDTOList,
                busPage.getNumber(),
                busPage.getSize(),
                busPage.getTotalElements(),
                busPage.getTotalPages(),
                busPage.isLast()
        );
    }
    @GetMapping("/{id}")
    public ResponseEntity<BusDTO> obtenerBusPorId(@PathVariable Long id) {
        Optional<Bus> busOptional = busRepository.findById(id);

        if (busOptional.isPresent()) {
            Bus bus = busOptional.get();
            MarcaDTO marcaDTO = null;

            if (bus.getMarca() != null) {
                marcaDTO = new MarcaDTO(
                        bus.getMarca().getId(),
                        bus.getMarca().getNombre()
                );
            }

            BusDTO busDTO = new BusDTO(
                    bus.getId(),
                    bus.getNumeroBus(),
                    bus.getPlaca(),
                    bus.getFechaCreacion(),
                    bus.getCaracteristica(),
                    bus.getActivo(),
                    marcaDTO
            );

            return ResponseEntity.ok(busDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
    //Crear Bus con Marca
    @PostMapping
    public ResponseEntity<?> create(@RequestBody BusDTO busDTO) {
        try {
            MarcaDTO marcaDTO = busDTO.getMarca();
            Marca marca = new Marca(marcaDTO.getId(), marcaDTO.getNombre());
            marca = marcaRepository.save(marca);

            Bus bus = new Bus();
            bus.setPlaca(busDTO.getPlaca());
            bus.setNumeroBus(busDTO.getNumeroBus());
            bus.setFechaCreacion(busDTO.getFechaCreacion());
            bus.setCaracteristica(busDTO.getCaracteristica());
            bus.setActivo(busDTO.getActivo());
            bus.setMarca(marca);
            busRepository.save(bus);

            return ResponseEntity.ok(bus);
        } catch (DataIntegrityViolationException e) {
            // Si ocurre una violación de integridad, devolvemos un error apropiado
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Error: La marca con el nombre '" + busDTO.getMarca().getNombre() + "' ya existe.");
        }
    }
}

package com.iasi.iasi.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iasi.iasi.model.Empresa;
import com.iasi.iasi.repository.EmpresaRepository;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class IasiController {
    @Autowired
    EmpresaRepository empresaRepository;

    @GetMapping("/empresas")
    public ResponseEntity<List<Empresa>> getAllEmpresas(@RequestParam(required = false) String nome) {
        try {
            List<Empresa> empresas = new ArrayList<Empresa>();

            if (nome == null)
                empresaRepository.findAll().forEach(empresas::add);
            else
                empresaRepository.findByNameContaining(nome).forEach(empresas::add);

            if (empresas.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            if (empresas.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(empresas, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/empresas/{id}")
    public ResponseEntity<Empresa> getEmpresaById(@PathVariable("id") long id) {
        Empresa empresa = empresaRepository.findById(id);

        if (empresa != null) {
            return new ResponseEntity<>(empresa, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/empresas")
    public ResponseEntity<String> createEmpresa(@RequestBody Empresa empresa) {
        try {
            empresaRepository.save(new Empresa(empresa.getNome(), empresa.getSetorIndustrial(), empresa.getLocalizacao(), empresa.getTipo(), empresa.getConformidadeRegular()));
            return new ResponseEntity<>("Empresa was created successfully.", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/empresas/{id}")
    public ResponseEntity<String> updateEmpresa(@PathVariable("id") long id, @RequestBody Empresa empresa) {
        Empresa _empresa = empresaRepository.findById(id);

        if (_empresa != null) {
            _empresa.setId(id);
            _empresa.setNome(empresa.getNome());
            _empresa.setSetorIndustrial(empresa.getSetorIndustrial());
            _empresa.setLocalizacao(empresa.getLocalizacao());
            _empresa.setTipo(empresa.getTipo());
            _empresa.setConformidadeRegular(empresa.getConformidadeRegular());

            empresaRepository.update(_empresa);
            return new ResponseEntity<>("Empresa was updated successfully.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Cannot find Empresa with id=" + id, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/empresas/{id}")
    public ResponseEntity<String> deleteEmpresa(@PathVariable("id") long id) {
        try {
            int result = empresaRepository.deleteById(id);
            if (result == 0) {
                return new ResponseEntity<>("Cannot find Empresa with id=" + id, HttpStatus.OK);
            }
            return new ResponseEntity<>("Empresa was deleted successfully.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Cannot delete empresa.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/empresas")
    public ResponseEntity<String> deleteAllEmpresas() {
        try {
            int numRows = empresaRepository.deleteAll();
            return new ResponseEntity<>("Deleted " + numRows + " Empresa(s) successfully.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Cannot delete empresas.", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}

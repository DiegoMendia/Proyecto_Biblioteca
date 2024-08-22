package com.egg.controladores;

import com.egg.entities.Autor;
import com.egg.excepciones.MiException;
import com.egg.servicios.AutorServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Controller
@RequestMapping("/autor") // localhost:8080/autor
public class AutorControlador {
    @Autowired
    private AutorServicio autorServicio;

    @GetMapping("/registrar") //localhost:8080/autor/registar
    public String registrar() {
        return "autor_form.html";
    }

    @PostMapping("registro")
    public String registro(@RequestParam String nombre, ModelMap modelo) {
        try {
            autorServicio.crearAutor(nombre);
            modelo.put("exito", "El Autor fue cargado correctamente");
        } catch (MiException ex) {
            modelo.put("error", ex.getMessage());
            Logger.getLogger(AutorControlador.class.getName()).log(Level.SEVERE, null, ex);
            return "autor_form.html";
        }
        return "index.html";
    }

    @GetMapping("/lista")
    public String listar(ModelMap modelo){

        List<Autor> autores = autorServicio.listarAutores();
        modelo.addAttribute("autores", autores);

        return "autor_list.html";
    }

    @GetMapping("/modificar/{id}")
    public String modificar(@PathVariable String id, ModelMap modelo){
        modelo.put("autor", autorServicio.getOne(id));

        return "autor_modificar.html";
    }

    @PostMapping("/modificar/{id}")
    public String modificar(@PathVariable String id, String nombre, ModelMap modelo){
        try {
            autorServicio.modificarAutor(nombre, id);

            return "redirect:../lista";
        } catch (MiException ex) {
           modelo.put("error", ex.getMessage());
            return "autor_modificar.html";
        }
    }
}

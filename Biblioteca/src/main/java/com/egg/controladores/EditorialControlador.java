package com.egg.controladores;

import com.egg.entities.Editorial;
import com.egg.excepciones.MiException;
import com.egg.servicios.EditorialServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Controller
@RequestMapping("/editorial")
public class EditorialControlador {
    @Autowired
    private EditorialServicio editorialServicio;

    @GetMapping("/registrar") //localhost:8080/autor/registar
    public String registrar() {
        return "editorial_form.html";
    }

    @PostMapping("registro")
    public String registro(@RequestParam String nombre, ModelMap modelo) {
        try {
            editorialServicio.crearEditorial(nombre);
            modelo.put("exito", "La editorial fue cargado correctamente");
        } catch (MiException ex) {
            modelo.put("error", ex.getMessage());
            Logger.getLogger(EditorialControlador.class.getName()).log(Level.SEVERE, null, ex);
            return "editorial_form.html";
        }
        return "index.html";
    }

    @GetMapping("/lista")
    public String listar(ModelMap modelo) {

        List<Editorial> editoriales = editorialServicio.listarEditoriales();
        modelo.addAttribute("editoriales", editoriales);

        return "editorial_list.html";
    }

    @GetMapping("/modificar/{id}")
    public String modificar(@PathVariable String id, ModelMap modelo){
        modelo.put("editorial", editorialServicio.getOne(id));

        return "editorial_modificar.html";
    }
    @PostMapping("/modificar/{id}")
    public String modificar(@PathVariable String id, String nombre, ModelMap modelo){
        try {
            editorialServicio.modificarEditorial(nombre, id);

            return "redirect:../lista";
        } catch (MiException ex){
            modelo.put("error", ex.getMessage());
            return "editorial_modificar.html";
        }
    }

}

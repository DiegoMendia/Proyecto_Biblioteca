package com.egg.controladores;

import com.egg.entities.Autor;
import com.egg.entities.Editorial;
import com.egg.entities.Libro;
import com.egg.excepciones.MiException;
import com.egg.servicios.AutorServicio;
import com.egg.servicios.EditorialServicio;
import com.egg.servicios.LibroServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Controller
@RequestMapping("/libro")
public class LibroControlador {

    @Autowired
    private LibroServicio libroServicio;
    @Autowired
    private AutorServicio autorServicio;
    @Autowired
    private EditorialServicio editorialServicio;

    @GetMapping("/registrar") // localhost:8080/libro/registrar
    public String registrar(ModelMap modelo) {

        List<Autor> autores= autorServicio.listarAutores();
        List<Editorial> editoriales=editorialServicio.listarEditoriales();

        modelo.addAttribute("autores", autores);
        modelo.addAttribute("editoriales", editoriales);

        return "libro_form.html";
    }

    @PostMapping("/registro")
    public String registro(@RequestParam(required = false) Long isbn, @RequestParam String titulo, @RequestParam(required = false) Integer ejemplares, @RequestParam String idAutor, @RequestParam String idEditorial, ModelMap modelo) {
        try {

            libroServicio.crearLibro(isbn, titulo, ejemplares, idAutor, idEditorial); // si todo sale bien retornamos al index
            modelo.put("exito", "El libro fue cargado correctamente");

        } catch (MiException ex) {
            List<Autor> autores= autorServicio.listarAutores();
            List<Editorial> editoriales=editorialServicio.listarEditoriales();

            modelo.addAttribute("autores", autores);
            modelo.addAttribute("editoriales", editoriales);

            modelo.put("error", ex.getMessage());
            Logger.getLogger(LibroControlador.class.getName()).log(Level.SEVERE, null, ex);
            return "libro_form.html"; // volvemos a cargar el formulario
        }
        return "index.html";
    }
    @GetMapping("/lista")
    public String listar(ModelMap modelo) {

        List<Libro> libros = libroServicio.listarLibros();
        modelo.addAttribute("libros", libros);

        return "libro_list.html";
    }
    @GetMapping("/modificar/{isbn}")
        public String modificar(@PathVariable Long isbn, ModelMap modelo){
        modelo.put("libro",libroServicio.getOne(isbn));

        return "libro_modificar.html";
    }

    @PostMapping("/modificar/{id}")
    public String modificar(@PathVariable Long isbn, String titulo, Integer ejemplares, String idAutor, String idEditorial, ModelMap modelo){
        try{
            libroServicio.modificarLibro(isbn,titulo,ejemplares,idAutor,idEditorial);

            return "redirect:../lista";
        }catch (MiException ex){
            modelo.put("error",ex.getMessage());
            return "libro_modificar.html";
        }
    }
}

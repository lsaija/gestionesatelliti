package it.prova.gestionesatelliti.web.controller;

import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.prova.gestionesatelliti.model.Satellite;
import it.prova.gestionesatelliti.model.StatoSatellite;
import it.prova.gestionesatelliti.service.SatelliteService;

@Controller
@RequestMapping(value = "/satellite")
public class SatelliteController {

	@Autowired
	private SatelliteService satelliteService;

	@GetMapping
	public ModelAndView listAll() {
		ModelAndView mv = new ModelAndView();
		List<Satellite> results = satelliteService.listAllElements();
		mv.addObject("satellite_list_attribute", results);
		mv.setViewName("satellite/list");
		return mv;
	}

	@GetMapping("/search")
	public String search() {
		return "satellite/search";
	}

	@PostMapping("/list")
	public String listByExample(Satellite example, ModelMap model) {
		List<Satellite> results = satelliteService.findByExample(example);
		model.addAttribute("satellite_list_attribute", results);
		return "satellite/list";
	}

	@GetMapping("/insert")
	public String create(Model model) {
		model.addAttribute("insert_satellite_attr", new Satellite());
		return "satellite/insert";
	}

	@PostMapping("/save")
	public String save(@Valid @ModelAttribute("insert_satellite_attr") Satellite satellite, BindingResult result,
			RedirectAttributes redirectAttrs, Model model) {

		if (satellite.getDataLancio() != null && satellite.getDataRientro() != null
				&& satellite.getDataLancio().after(satellite.getDataRientro())) {
			model.addAttribute("errorMessage", "ATTENZIONE ERRORE DATE");
			return "satellite/insert";
		}

		if (satellite.getDataRientro() != null && satellite.getDataLancio() == null) {
			model.addAttribute("errorMessage", "ATTENZIONE DATA DI RIENTRO DOPO QUELLA DI LANCIO");
			return "satellite/insert";
		}

		if (satellite.getDataLancio() != null && satellite.getDataRientro() != null
				&& satellite.getDataLancio().before(new Date()) && satellite.getDataRientro().before(new Date())
				&& (satellite.getStato() == null || satellite.getStato() != StatoSatellite.DISATTIVATO)) {
			model.addAttribute("errorMessage", "ATTENZIONE LO STATO DEVE ESSERE DEVE ESSERE:DISATTIVATO");
			return "satellite/insert";
		}

		if (satellite.getDataLancio() == null && satellite.getDataRientro() == null && satellite.getStato() != null) {
			model.addAttribute("errorMessage", "ATTENZIONE LO STATO DEVE ESSERE INSERITO DOPO IL LANCIO");
			return "satellite/insert";
		}

		if (satellite.getDataLancio() != null && satellite.getDataLancio().before(new Date())
				&& satellite.getStato() == null) {
			model.addAttribute("errorMessage", "ATTENZIONE INSERIRE UNO STATO DOPO IL LANCIO");
			return "satellite/insert";
		}

		if (satellite.getDataLancio() != null && satellite.getDataLancio().after(new Date())
				&& satellite.getStato() != null) {
			model.addAttribute("errorMessage", "ATTENZIONE LO STATO DEVE ESSERE INSERITO DOPO IL LANCIO");
			return "satellite/insert";
		}

		if (result.hasErrors())
			return "satellite/insert";

		satelliteService.inserisciNuovo(satellite);

		redirectAttrs.addFlashAttribute("successMessage", "Operazione eseguita correttamente");
		return "redirect:/satellite";
	}

	@GetMapping("/show/{idSatellite}")
	public String show(@PathVariable(required = true) Long idSatellite, Model model) {
		model.addAttribute("show_satellite_attr", satelliteService.caricaSingoloElemento(idSatellite));
		return "satellite/show";
	}

	@GetMapping("/delete/{idSatellite}")
	public String delete(@PathVariable(required = true) Long idSatellite, Model model) {
		model.addAttribute("delete_satellite_attr", satelliteService.caricaSingoloElemento(idSatellite));
		return "satellite/delete";
	}

	@PostMapping("/saveDelete")
	public String saveDelete(@RequestParam(name = "idSatellite") Long idSatellite, RedirectAttributes redirectAttrs) {

		satelliteService.rimuovi(idSatellite);

		redirectAttrs.addFlashAttribute("successMessage", "Operazione eseguita correttamente");
		return "redirect:/satellite";
	}

	@GetMapping("/update/{idSatellite}")
	public String update(@PathVariable(required = true) Long idSatellite, Model model) {
		model.addAttribute("update_satellite_attr", satelliteService.caricaSingoloElemento(idSatellite));
		model.addAttribute("todayDate_attr", new Date());
		return "satellite/update";
	}

	@PostMapping("/saveUpdate")
	public String saveUpdate(@Valid @ModelAttribute("update_satellite_attr") Satellite satellite, BindingResult result,
			RedirectAttributes redirectAttrs, Model model) {

		if (satellite.getDataLancio() != null && satellite.getDataRientro() != null
				&& satellite.getDataLancio().after(satellite.getDataRientro())) {
			model.addAttribute("errorMessage", "ATTENZIONE ERRORE DATE");
			return "satellite/update";
		}

		if (satellite.getDataRientro() != null && satellite.getDataLancio() == null) {
			model.addAttribute("errorMessage", "ATTENZIONE INSERIRE DATA DI LANCIO PRIMA DEL RIENTRO");
			return "satellite/update";
		}

		if (satellite.getDataLancio() != null && satellite.getDataRientro() != null
				&& satellite.getDataLancio().before(new Date()) && satellite.getDataRientro().before(new Date())
				&& (satellite.getStato() == null || satellite.getStato() != StatoSatellite.DISATTIVATO)) {
			model.addAttribute("errorMessage",
					"ATTENZIONE INSERIRE STATO DISATTIVATO SE GIA INSERITE ENTRAMBE LE DATE");
			return "satellite/update";
		}

		if (satellite.getDataLancio() == null && satellite.getDataRientro() == null && satellite.getStato() != null) {
			model.addAttribute("errorMessage", "ATTENZIONE INSERIRE STATO DOPO DATA DI LANCIO");
			return "satellite/update";
		}

		if (satellite.getDataLancio() != null && satellite.getDataLancio().before(new Date())
				&& satellite.getStato() == null) {
			model.addAttribute("errorMessage", "ATTENZIONE INSERIRE UNO STATO DOPO IL LANCIO");
			return "satellite/update";
		}

		if (satellite.getDataLancio() != null && satellite.getDataLancio().after(new Date())
				&& satellite.getStato() != null) {
			model.addAttribute("errorMessage", "ATTENZIONE INSERIRE UNO STATO DOPO IL LANCIO");
			return "satellite/update";
		}

		satelliteService.aggiorna(satellite);

		redirectAttrs.addFlashAttribute("successMessage", "Operazione eseguita correttamente");
		return "redirect:/satellite";
	}

	@PostMapping("/lancia")
	public String lancia(@RequestParam(name = "idSatellite") Long idSatellite, ModelMap model) {
		Satellite satellite = satelliteService.caricaSingoloElemento(idSatellite);
		satellite.setDataLancio(new Date());
		satelliteService.aggiorna(satellite);
		model.addAttribute("todayDate_attr", new Date());
		model.addAttribute("satellite_list_attribute", satelliteService.listAllElements());
		return "satellite/list";
	}

	@PostMapping("/rientro")
	public String rientro(@RequestParam(name = "idSatellite") Long idSatellite, ModelMap model) {
		Satellite satellite = satelliteService.caricaSingoloElemento(idSatellite);
		if (satellite.getDataLancio() != null) {
			satellite.setDataRientro(new Date());
			satellite.setStato(StatoSatellite.DISATTIVATO);
			satelliteService.aggiorna(satellite);
		}
		model.addAttribute("todayDate_attr", new Date());
		model.addAttribute("satellite_list_attribute", satelliteService.listAllElements());
		return "satellite/list";
	}
}

package it.prova.gestionesatelliti.web.controller;

import java.util.Calendar;
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
	public ModelAndView listAll(Model model) {
		ModelAndView mv = new ModelAndView();
		List<Satellite> results = satelliteService.listAllElements();
		model.addAttribute("todayDate_attr", new Date());
		mv.addObject("satellite_list_attribute", results);
		mv.setViewName("satellite/list");
		return mv;
	}

	@GetMapping("/search")
	public String search(Model model) {
		model.addAttribute("todayDate_attr", new Date());
		return "satellite/search";
	}

	@PostMapping("/list")
	public String listByExample(Satellite example, ModelMap model) {
		List<Satellite> results = satelliteService.findByExample(example);
		model.addAttribute("satellite_list_attribute", results);
		model.addAttribute("todayDate_attr", new Date());
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
			result.rejectValue("dataLancio","Satellite.dataLancio.mustBe.piuPiccolo");
			result.rejectValue("dataRientro","Satellite.dataRientro.mustBe.piuGrande");
			
			return "satellite/insert";
		}

		if (satellite.getDataRientro() != null && satellite.getDataLancio() == null) {
			result.rejectValue("dataRientro","Satellite.dataRientro.mustBeNUll");			
			return "satellite/insert";
		}

		if (satellite.getDataLancio() != null && satellite.getDataRientro() != null
				&& satellite.getDataLancio().before(new Date()) && satellite.getDataRientro().before(new Date())
				&& (satellite.getStato() == null || satellite.getStato() != StatoSatellite.DISATTIVATO)) {
			result.rejectValue("stato","Satellite.stato.mustBe.DISATTIVATO");
			return "satellite/insert";
		}

		if (satellite.getDataLancio() == null && satellite.getDataRientro() == null && satellite.getStato() != null) {
			result.rejectValue("stato","satellite.stato.mustBe.inseritoDopoLancio");
			return "satellite/insert";
		}

		if (satellite.getDataLancio() != null && satellite.getDataLancio().before(new Date())
				&& satellite.getStato() == null) {
			result.rejectValue("stato","satellite.stato.mustBe.inseritoDopoLancio");
			return "satellite/insert";
		}

		if (satellite.getDataLancio() != null && satellite.getDataLancio().after(new Date())
				&& satellite.getStato() != null) {
			result.rejectValue("stato","satellite.stato.mustBe.inseritoDopoLancio");
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
	public String saveDelete(@RequestParam(name = "idSatellite") Long idSatellite, RedirectAttributes redirectAttrs,Model model,BindingResult result) {
		Satellite satellite = satelliteService.caricaSingoloElemento(idSatellite);
		if(satellite.getDataLancio() != null && satellite.getDataRientro() == null &&satellite.getStato()!=StatoSatellite.DISATTIVATO) {
			result.rejectValue("dataRientro","Satellite.dataRientro.mustBeInsert");
		    return "satellite/update";
		}	
	
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
			result.rejectValue("dataLancio","Satellite.dataLancio.mustBe.piuPiccolo");
			result.rejectValue("dataRientro","Satellite.dataRientro.mustBe.piuGrande");
			return "satellite/update";
		}

		if (satellite.getDataRientro() != null && satellite.getDataLancio() == null) {
			result.rejectValue("dataRientro","Satellite.dataRientro.mustBeNUll");
			return "satellite/update";
		}

		if (satellite.getDataLancio() != null && satellite.getDataRientro() != null
				&& satellite.getDataLancio().before(new Date()) && satellite.getDataRientro().before(new Date())
				&& (satellite.getStato() == null || satellite.getStato() != StatoSatellite.DISATTIVATO)) {
			result.rejectValue("stato","satellite.stato.mustBeInsert");
			return "satellite/update";
		}

		if (satellite.getDataLancio() == null && satellite.getDataRientro() == null && satellite.getStato() != null) {
			result.rejectValue("stato","satellite.stato.mustBe.inseritoDopoLancio");
			return "satellite/update";
		}

		if (satellite.getDataLancio() != null && satellite.getDataLancio().before(new Date())
				&& satellite.getStato() == null) {
			result.rejectValue("stato","satellite.stato.mustBe.inseritoDopoLancio");
			return "satellite/update";
		}

		if (satellite.getDataLancio() != null && satellite.getDataLancio().after(new Date())
				&& satellite.getStato() != null) {
			result.rejectValue("stato","satellite.stato.mustBe.inseritoDopoLancio");
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
		satellite.setStato(StatoSatellite.IN_MOVIMENTO);
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
	
	@GetMapping("/listAllDueAnni")
	public ModelAndView listAllDueAnni() {
		ModelAndView mv = new ModelAndView();
		
		Date dataProva = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dataProva);
		calendar.add(Calendar.YEAR, -2);
		Date dataMenoDue = calendar.getTime();
		List<Satellite> results = satelliteService.cercaTuttiSatellitiDueAnni(dataMenoDue);
		mv.addObject("satellite_list_attribute", results);
		mv.setViewName("satellite/list");
		return mv;
	}
	
	@GetMapping("/listAllDisattivati")
	public ModelAndView listAllDisattivati() {
		ModelAndView mv = new ModelAndView();
		List<Satellite> results = satelliteService.cercaTuttiSatellitiOffDataRientroNull();
		mv.addObject("satellite_list_attribute", results);
		mv.setViewName("satellite/list");
		return mv;
	}
	
	@GetMapping("/listAllDieciAnniOrbita")
	public ModelAndView listAllDieciAnniOrbita() {
		ModelAndView mv = new ModelAndView();
		
		Date dataProva = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dataProva);
		calendar.add(Calendar.YEAR, -10);
		Date dataMenoDue = calendar.getTime();
		List<Satellite> results = satelliteService.cercaTuttiSatellitiDieciAnniOrbita(dataMenoDue);
		mv.addObject("satellite_list_attribute", results);
		mv.setViewName("satellite/list");
		return mv;
	}
	
	@GetMapping("/disabilitaTutti")
	public ModelAndView disabilitaTutti() {
		ModelAndView mv = new ModelAndView();
		List<Satellite> satelliti=satelliteService.listAllElements();
		List<Satellite> results = satelliteService.cercaTuttiSatellitiNonRientrati();
		mv.addObject("numeroSatellite_list_attribute",satelliti.size());
		mv.addObject("numeroSatelliteModificati_list_attribute", results.size());
		mv.setViewName("satellite/disabilita");
		return mv;
	}
	
	@PostMapping("/saveDisabilita")
	public ModelAndView saveDisabilta(RedirectAttributes redirectAttrs) {
		ModelAndView mv = new ModelAndView();		
		List<Satellite> results = satelliteService.cercaTuttiSatellitiNonRientrati();
		
		for(Satellite item: results) {
			item.setDataRientro(new Date());
			item.setStato(StatoSatellite.DISATTIVATO);
			satelliteService.aggiorna(item);
		}
		redirectAttrs.addFlashAttribute("successMessage", "Operazione eseguita correttamente");
		mv.setViewName("redirect:/home");
		return mv;
	}
}

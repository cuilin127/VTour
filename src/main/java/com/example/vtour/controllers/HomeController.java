package com.example.vtour.controllers;

import com.example.vtour.model.*;
import com.example.vtour.repo.AddressReportRepo;
import com.example.vtour.repo.HouseRepo;
import com.example.vtour.repo.PictureRepo;
import com.example.vtour.repo.PublicInfraRepo;
import com.example.vtour.services.LocationService;
import com.google.maps.model.PlacesSearchResponse;
import com.google.maps.model.PlacesSearchResult;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class HomeController {
    private final HouseRepo hrp;
    private final PictureRepo prp;
    private final AddressReportRepo arp;
    private final PublicInfraRepo pirp;
    private final LocationService locationService;
    public HomeController(HouseRepo hrp, PictureRepo prp, AddressReportRepo arp, PublicInfraRepo pirp, LocationService locationService) {
        this.hrp = hrp;
        this.prp = prp;
        this.arp = arp;
        this.pirp = pirp;
        this.locationService = locationService;
    }

    @GetMapping("/")
    public String goHome() {
        return "redirect:https://revstudio.ca/";
    }
    @GetMapping("/admin")
    public String goAdmin(HttpServletRequest request) {
        try{
            if(allowAccess(request)){
                return "redirect:https://revstudio.ca/";
            }
            return "Admin";
        }catch (Exception ex){
            return "redirect:https://revstudio.ca/";
        }

    }

    @GetMapping("/addHousePage")
    public String goAddHousePage(Model model, HttpServletRequest request) {
        try{
            //Check if eligible
            if(allowAccess(request)){
                return "redirect:https://revstudio.ca/";
            }

            model.addAttribute("house", new House());
            return "AddHouse";
        }catch (Exception ex){
            return "redirect:https://revstudio.ca/";
        }

    }
    @GetMapping("/addHouse")
    public String addHousePage(Model model, @ModelAttribute House house, HttpServletRequest request) {
        try{
            //Check if eligible
            if(allowAccess(request)){
                return "redirect:https://revstudio.ca/";
            }
            house.setFullAddress(house.getAddress()+", "+house.getCity()+", "+house.getProvince()+", "+house.getPostCode());
            AddressReport tempReport = new AddressReport();
            //Work for Address report
            //Add Schools
            PlacesSearchResponse tempSchoolSearchResult = locationService.findSchool(house.getAddress());
            for(PlacesSearchResult temp:tempSchoolSearchResult.results){
                PublicInfra tempInfra = new PublicInfra();
                tempInfra.setName(temp.name);
                tempInfra.setType("SCHOOL");
                tempInfra.setAddress(locationService.getFormattedAddress(temp.placeId));
                tempInfra.setDistance(locationService.getDistance(house.getFullAddress(),tempInfra.getAddress()));
                tempInfra.setUrl(locationService.getPlaceWebsite(temp.placeId));
                pirp.save(tempInfra);
                tempReport.getInfraList().add(tempInfra);
            }
            //Add park
            PlacesSearchResponse tempParkSearchResult = locationService.findParks(house.getAddress());
            for(PlacesSearchResult temp:tempParkSearchResult.results){
                PublicInfra tempInfra = new PublicInfra();
                tempInfra.setName(temp.name);
                tempInfra.setType("PARK");
                tempInfra.setAddress(locationService.getFormattedAddress(temp.placeId));
                tempInfra.setDistance(locationService.getDistance(house.getFullAddress(),tempInfra.getAddress()));
                tempInfra.setUrl(locationService.getPlaceWebsite(temp.placeId));
                pirp.save(tempInfra);
                tempReport.getInfraList().add(tempInfra);
            }
            //Add Transits
            PlacesSearchResponse tempTransitSearchResult = locationService.findTransits(house.getAddress());
            for(PlacesSearchResult temp:tempTransitSearchResult.results){
                PublicInfra tempInfra = new PublicInfra();
                tempInfra.setName(temp.name);
                tempInfra.setType("TRANSIT");
                tempInfra.setAddress(locationService.getFormattedAddress(temp.placeId));
                tempInfra.setDistance(locationService.getDistance(house.getFullAddress(),tempInfra.getAddress()));
                tempInfra.setUrl(locationService.getPlaceWebsite(temp.placeId));
                pirp.save(tempInfra);
                tempReport.getInfraList().add(tempInfra);
            }

            arp.save(tempReport);
            house.setAddressReport(tempReport);
            hrp.save(house);
            model.addAttribute("houses", hrp.findAll());
            return "ViewHouses";
        }catch (Exception ex){
            return "redirect:https://revstudio.ca/";
        }

    }

    @GetMapping("/viewHousesPage")
    public String goViewHousesPage(Model model, HttpServletRequest request) {
        try{
            //Check if eligible
            if(allowAccess(request)){
                return "redirect:https://revstudio.ca/";
            }
            model.addAttribute("houses", hrp.findAll());
            return "ViewHouses";
        }catch (Exception ex){
            return "redirect:https://revstudio.ca/";
        }

    }
    @GetMapping("/addPhoto/{id}")
    public String addPhoto(@PathVariable int id, Model model, HttpServletRequest request) {
        try{
            //Check if eligible
            if(allowAccess(request)){
                return "redirect:https://revstudio.ca/";
            }

            AddPhotoDto dto = new AddPhotoDto();
            dto.setHouseId(id);
            model.addAttribute("addPhotoDto",dto);
            return "AddPhoto";
        }catch (Exception ex){
            return "redirect:https://revstudio.ca/";
        }

    }
    @GetMapping("/doAddPhoto")
    public String doAddPhoto(@ModelAttribute AddPhotoDto dto, Model model, HttpServletRequest request) {
        try{
            //Check if eligible
            if(allowAccess(request)){
                return "redirect:https://revstudio.ca/";
            }

            String imageIds = dto.getImageIds();
            String[] items = imageIds.split(",");
            House tempHouse = hrp.findById(dto.getHouseId());
            tempHouse.getPictures().clear();
            for (int i = 0; i < items.length; i++) {
                items[i] = items[i].trim();
                Picture pic = Picture.builder().fileId(items[i]).build();
                pic.setHouse(tempHouse);
                pic.setUrl("https://drive.google.com/thumbnail?id="+pic.getFileId()+"&sz=s4000");
                tempHouse.getPictures().add(pic);
                prp.save(pic);
            }
            hrp.save(tempHouse);

            model.addAttribute("houses", hrp.findAll());
            return "ViewHouses";
        }catch (Exception ex){
            return "redirect:https://revstudio.ca/";
        }

    }
    @GetMapping("/viewProperty/{id}")
    public String doAddPhoto(@PathVariable int id, Model model) {

        House tempHouse = hrp.findById(id);

        model.addAttribute("house", tempHouse);

        List<PublicInfra> schools = tempHouse.getAddressReport().getInfraList().stream().filter(s -> s.getType().equals("SCHOOL")).collect(Collectors.toList());
        List<PublicInfra> parks = tempHouse.getAddressReport().getInfraList().stream().filter(s -> s.getType().equals("PARK")).collect(Collectors.toList());
        List<PublicInfra> transits = tempHouse.getAddressReport().getInfraList().stream().filter(s -> s.getType().equals("TRANSIT")).collect(Collectors.toList());
        model.addAttribute("schools", schools);
        model.addAttribute("parks", parks);
        model.addAttribute("transits", transits);
        return "ViewProperty";
    }

    public boolean allowAccess(HttpServletRequest request) throws UnknownHostException {

        var clientIp = request.getRemoteAddr();
        // Allow localhost IPs
        boolean isLocalhost = "0:0:0:0:0:0:0:1".equals(clientIp);

        // Resolve the domain name to an IP address
        InetAddress address = InetAddress.getByName("amyccmmyy.synology.me");
        String domainIp = address.getHostAddress();

        boolean isAllowedDomain = clientIp.equals(domainIp);

        return !isLocalhost && !isAllowedDomain;
    }
}

package com.example.vtour.controllers;

import com.example.vtour.model.AddPhotoDto;
import com.example.vtour.model.House;
import com.example.vtour.model.Picture;
import com.example.vtour.repo.HouseRepo;
import com.example.vtour.repo.PictureRepo;
import com.example.vtour.services.DropBoxService;
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

@Controller
public class HomeController {
    private final HouseRepo hrp;
    private final PictureRepo prp;
    private final DropBoxService dropBoxService;
    public HomeController(HouseRepo hrp, PictureRepo prp, DropBoxService dropBoxService) {
        this.hrp = hrp;
        this.prp = prp;
        this.dropBoxService = dropBoxService;
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

            String dropBoxUrl = dto.getDropBoxUrl();
            House tempHouse = hrp.findById(dto.getHouseId());
            List<String> imageUrls = dropBoxService.getSharedLinksForFolder(dropBoxUrl);
            tempHouse.getPictures().clear();
            List<Picture> tempPics = new ArrayList<>();
            for (int i = 0; i < imageUrls.size(); i++) {

                Picture tempPic = new Picture();
                tempPic.setHouse(tempHouse);
                tempPic.setUrl(imageUrls.get(i));
                tempHouse.getPictures().add(tempPic);
                tempPics.add(tempPic);
            }
            prp.saveAll(tempPics);
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

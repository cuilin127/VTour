package com.example.vtour.controllers;

import com.example.vtour.model.AddPhotoDto;
import com.example.vtour.model.House;
import com.example.vtour.model.Picture;
import com.example.vtour.model.SetTokenDTO;
import com.example.vtour.repo.HouseRepo;
import com.example.vtour.repo.PictureRepo;
import com.example.vtour.services.DropBoxService;
import com.example.vtour.services.DropBoxTokenService;
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
    private final DropBoxTokenService dropBoxTokenService;

    private String token = null;
    public HomeController(HouseRepo hrp, PictureRepo prp, DropBoxService dropBoxService, DropBoxTokenService dropBoxTokenService) {
        this.hrp = hrp;
        this.prp = prp;
        this.dropBoxService = dropBoxService;
        this.dropBoxTokenService = dropBoxTokenService;
    }

    @GetMapping("/")
    public String goHome() {
        return "redirect:https://revstudio.ca/";
    }
    @GetMapping("/login")
    public String login() {
        return "login";
    }
    @GetMapping("/getCode")
    public String printCode() {
        //System.out.println(dropBoxTokenService.getAccessToken("EmTCYSykox0AAAAAAAACAZuLNpj0VEwq21jfenTVihA"));
        return "redirect:https://www.dropbox.com/oauth2/authorize?client_id=uhqcpb2jg2bpzaw&response_type=code&redirect_uri=https://google.com";
    }
    @GetMapping("/admin")
    public String goAdmin(HttpServletRequest request) {
        try{
            return "Admin";
        }catch (Exception ex){
            return "redirect:https://revstudio.ca/";
        }

    }

    @GetMapping("/addHousePage")
    public String goAddHousePage(Model model, HttpServletRequest request) {
        try{
            //Check if eligible

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
            SetTokenDTO setTokenDTO = new SetTokenDTO();
            setTokenDTO.setHouseId(id);
            //AddPhotoDto dto = new AddPhotoDto();
            //dto.setHouseId(id);
            model.addAttribute("setTokenDTO",setTokenDTO);
            //return "AddPhoto";
            return "SetToken";
        }catch (Exception ex){
            return "redirect:https://revstudio.ca/";
        }
    }
    @GetMapping("/setToken")
    public String setToken(){
        return "SetToken";
    }
    @GetMapping("/doSetToken")
    public String doSetToken(@ModelAttribute SetTokenDTO dto, Model model, HttpServletRequest request) {
        try{
            //Check if eligible
            token = dropBoxTokenService.getAccessToken(dto.getAuCode());
            AddPhotoDto addPhotoDto = new AddPhotoDto();
            addPhotoDto.setHouseId(dto.getHouseId());

            model.addAttribute("addPhotoDto", addPhotoDto);
            return "AddPhoto";
        }catch (Exception ex){
            return "redirect:https://revstudio.ca/";
        }

    }
    @GetMapping("/doAddPhoto")
    public String doAddPhoto(@ModelAttribute AddPhotoDto dto, Model model, HttpServletRequest request) {
        try{
            //Check if eligible

            String dropBoxUrl = dto.getDropBoxUrl();
            House tempHouse = hrp.findById(dto.getHouseId());
            List<String> imageUrls = dropBoxService.getSharedLinksForFolder(dropBoxUrl,token);
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
}

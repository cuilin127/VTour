package com.example.vtour.controllers;

import com.example.vtour.model.AddPhotoDto;
import com.example.vtour.model.House;
import com.example.vtour.model.Picture;
import com.example.vtour.repo.HouseRepo;
import com.example.vtour.repo.PictureRepo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;

@Controller
public class HomeController {
    @Autowired
    private HouseRepo hrp;
    @Autowired
    private PictureRepo prp;

    @GetMapping("/*")
    public void redirectToHttps(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!request.isSecure()) {
            String redirectUrl = "https://" + request.getServerName() + ":" + request.getServerPort() + request.getRequestURI();
            response.sendRedirect(redirectUrl);
        }
    }

    @GetMapping("/")
    public String goHome() {
        return "index";
    }

    @GetMapping("/addHousePage")
    public String goAddHousePage(Model model) {
        model.addAttribute("house", new House());
        return "AddHouse";
    }
    @GetMapping("/addHouse")
    public String addHousePage(Model model, @ModelAttribute House house) {
        house.setFullAddress(house.getAddress()+", "+house.getCity()+", "+house.getProvince()+", "+house.getPostCode());
        hrp.save(house);
        model.addAttribute("houses", hrp.findAll());
        return "ViewHouses";
    }

    @GetMapping("/viewHousesPage")
    public String goViewHousesPage(Model model) {
        model.addAttribute("houses", hrp.findAll());
        return "ViewHouses";
    }
    @GetMapping("/addPhoto/{id}")
    public String addPhoto(@PathVariable int id, Model model) {

        AddPhotoDto dto = new AddPhotoDto();
        dto.setHouseId(id);
        model.addAttribute("addPhotoDto",dto);
        return "AddPhoto";
    }
    @GetMapping("/doAddPhoto")
    public String doAddPhoto(@ModelAttribute AddPhotoDto dto, Model model) {

        String imageIds = dto.getImageIds();
        String[] items = imageIds.split(",");
        House tempHouse = hrp.findById(dto.getHouseId());
        tempHouse.getPictures().clear();
        for (int i = 0; i < items.length; i++) {
            items[i] = items[i].trim();
            Picture pic = Picture.builder().fileId(items[i]).build();
            pic.setHouse(tempHouse);
            pic.setUrl("https://drive.google.com/uc?export=view&id="+pic.getFileId());
            tempHouse.getPictures().add(pic);
            prp.save(pic);
        }
        hrp.save(tempHouse);

        model.addAttribute("houses", hrp.findAll());
        return "ViewHouses";
    }
    @GetMapping("/viewProperty/{id}")
    public String doAddPhoto(@PathVariable int id, Model model) {

        House tempHouse = hrp.findById(id);

        model.addAttribute("house", tempHouse);
        return "ViewProperty";
    }
}

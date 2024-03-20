package com.example.vtour.services;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;
import com.dropbox.core.v2.sharing.CreateSharedLinkWithSettingsErrorException;
import com.dropbox.core.v2.sharing.SharedLinkMetadata;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DropBoxService {
    private final String ACCESS_TOKEN = "sl.BxxN0a4DU4BO8x6rP0Z_WQb0eRSG4gau9i7UKg8_cjD1IrgyzAV7BMOYf31bvAN0KWinNsivzotpjSRgBVk2UhspGC9tpQyJR1X9z_iukmwNXLKHH6q8L18A7o3aJfrHfzfq3A53E2IEjqg";

    public List<String> getSharedLinksForFolder(String folderPath) {
        DbxRequestConfig config = DbxRequestConfig.newBuilder("dropbox/java-tutorial").build();
        DbxClientV2 client = new DbxClientV2(config, ACCESS_TOKEN);

        List<String> urls = new ArrayList<>();
        folderPath = "/VTour/Images/property-" + folderPath;
        try {
            ListFolderResult result = client.files().listFolder(folderPath);
            while (true) {
                for (Metadata metadata : result.getEntries()) {
                    try {
                        List<SharedLinkMetadata> links = client.sharing().listSharedLinksBuilder().withPath(metadata.getPathLower()).start().getLinks();
                        if (!links.isEmpty()) {
                            String directUrl = links.get(0).getUrl().replace("&dl=0", "&raw=1");
                            urls.add(directUrl);

                            System.out.println(urls.size() + ". " + directUrl);
                        }
                    } catch (CreateSharedLinkWithSettingsErrorException e) {
                        System.out.println(e.getMessage());
                    }
                }
                if (!result.getHasMore()) {
                    System.out.println("DONE for add photo!");
                    break;
                }
                result = client.files().listFolderContinue(result.getCursor());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return urls;
    }
}

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
    private static final String ACCESS_TOKEN = "sl.BxwpjnFIhRv8X84AayEest3-q0-W7t-vBj8c09fN6BF4y18bIyynT-BwAMfxSCcw2P_r3u6KrNP64gmi-vmbqmEwH54J2POAgnhJ7cfxBTuki2dXJTfo2ldxUmF4OhaVHTisy1JSgl5s";

    public static List<String> getSharedLinksForFolder(String folderPath) {
        DbxRequestConfig config = DbxRequestConfig.newBuilder("dropbox/java-tutorial").build();
        DbxClientV2 client = new DbxClientV2(config, ACCESS_TOKEN);

        List<String> urls = new ArrayList<>();

        try {
            ListFolderResult result = client.files().listFolder(folderPath);
            while (true) {
                for (Metadata metadata : result.getEntries()) {
                    try {
                        SharedLinkMetadata sharedLink = client.sharing().createSharedLinkWithSettings(metadata.getPathLower());
                        String directUrl = sharedLink.getUrl().replace("?dl=0", "?raw=1");
                        urls.add(directUrl);
                    } catch (CreateSharedLinkWithSettingsErrorException e) {
                        if (e.errorValue.isSharedLinkAlreadyExists()) {
                            List<SharedLinkMetadata> links = client.sharing().listSharedLinksBuilder().withPath(metadata.getPathLower()).start().getLinks();
                            if (!links.isEmpty()) {
                                String directUrl = links.get(0).getUrl().replace("?dl=0", "?raw=1");
                                urls.add(directUrl);
                            }
                        } else {
                            throw e;
                        }
                    }
                }
                if (!result.getHasMore()) {
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

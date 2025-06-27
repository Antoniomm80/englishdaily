package com.anmoma.englishdaily.documentreader;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * Implementation of FolderReader for Google Drive.
 */
@Component
@Primary
public class GoogleDriveFolderReader implements FolderReader {

    private static final Logger log = LoggerFactory.getLogger(GoogleDriveFolderReader.class);

    private final Drive driveService;
    private final String rootFolderId; // Default to My Drive root

    public GoogleDriveFolderReader(Drive driveService, @Value("${google.drive.documents.folder-id}") String rootFolderId) {
        this.driveService = driveService;
        this.rootFolderId = rootFolderId;
    }

    /**
     * Gets filenames from a Google Drive folder.
     *
     * @param path The folder ID in Google Drive
     * @return List of filenames in the folder
     */
    @Override
    public List<String> getFilenamesFromFolder(String path) {
        log.info("Getting filenames from Google Drive folder: {}", path);
        try {
            String folderId = resolveFolderPath(path);// Ensure the folder path is resolved to an ID
            // Query to find all files in the specified folder that are not trashed
            String query = "'" + folderId + "' in parents and trashed = false";

            // Execute the query
            FileList result = driveService.files()
                                          .list()
                                          .setQ(query)
                                          .setFields("files(id, name,mimeType)")
                                          .execute();

            List<File> files = result.getFiles();
            if (files == null || files.isEmpty()) {
                log.debug("No files found in Google Drive folder: {}", path);
                return Collections.emptyList();
            }
            return files.stream()
                        .map(File::getId)
                        .toList();
        } catch (GoogleJsonResponseException e) {
            log.warn("Google Drive API error while getting filenames from folder {}: {}", path, e.getDetails()
                                                                                                 .getMessage());
            return Collections.emptyList(); // Return empty list on error
        } catch (IOException e) {
            throw new RuntimeException("Failed to get filenames from Google Drive folder", e);
        }
    }

    private String resolveFolderPath(String path) throws IOException {
        String[] segments = path.split("/");
        String currentParentId = rootFolderId; // Start at My Drive

        for (String segment : segments) {
            String query = String.format("name = '%s' and mimeType = 'application/vnd.google-apps.folder' and '%s' in parents and trashed = false",
                    segment, currentParentId);

            FileList folderList = driveService.files()
                                              .list()
                                              .setQ(query)
                                              .setFields("files(id, name)")
                                              .execute();

            List<File> folders = folderList.getFiles();
            if (folders.isEmpty()) {
                return null; // Folder not found
            }

            // If multiple folders have same name, we use the first
            currentParentId = folders.getFirst()
                                     .getId();
        }

        return currentParentId; // Final folder ID
    }

    /**
     * Gets a resource from Google Drive.
     *
     * @param resourceId The file ID in Google Drive
     * @return Resource representing the file
     */
    @Override
    public Resource getResource(String resourceId) {
        log.debug("Getting resource from Google Drive: {}", resourceId);

        try {

            // Get file metadata to get the filename
            File file = driveService.files()
                                    .get(resourceId)
                                    .execute();
            String fileName = file.getName();

            // Download the file content
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            driveService.files()
                        .get(resourceId)
                        .executeMediaAndDownloadTo(outputStream);

            // Create a resource from the downloaded content
            final byte[] content = outputStream.toByteArray();
            final String finalFileName = fileName;

            return new ByteArrayResource(content) {
                @Override
                public String getFilename() {
                    return finalFileName;
                }
            };
        } catch (IOException e) {
            throw new RuntimeException("Failed to get resource from Google Drive", e);
        }
    }
}

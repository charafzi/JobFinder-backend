package com.ilisi.jobfinder.service;

import com.ilisi.jobfinder.Enum.DocumentType;
import com.ilisi.jobfinder.dto.DocumentDTO;
import com.ilisi.jobfinder.model.Candidat;
import com.ilisi.jobfinder.model.Document;
import com.ilisi.jobfinder.repository.DocumentRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;

@Repository
@AllArgsConstructor
public class DocumentService {
    private final DocumentRepository documentRepository;
    private final String uploadDir = "src/main/java/com/ilisi/jobfinder/assets/document";
    private final Path storageLocation = Paths.get(System.getProperty("user.dir"), uploadDir);

    public Document saveDocument(Candidat candidat, MultipartFile file, DocumentType type) throws IOException, RuntimeException{
        // Créer l'entité Document
        String filePath = "";
        try {
            filePath = this.saveDocumentLocally(candidat.getId(),file,type);
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de sauvegardement du ficher.");
        }
        catch (RuntimeException e){
            throw e;
        }
        Document document = new Document();
        document.setFilePath(filePath);
        document.setFileType(type);
        document.setUploadDate(LocalDateTime.now());
        document.setCandidat(candidat);
        return documentRepository.save(document);
    }

    public Document getDocumentEntityById(Long id) throws IOException {
        Document doc = documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document introuvable avec l'id: " + id));
        return doc;
    }

    public DocumentDTO getDocumentById(Long id) throws IOException {
        Document doc = documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document introuvable avec l'id: " + id));
        DocumentDTO documentDTO = new DocumentDTO();
        documentDTO.setData(getDocumentLocally(doc));
        documentDTO.setContentType(determineContentType(doc.getFilePath()));
        documentDTO.setFileType(doc.getFileType());

        return documentDTO;
    }

    private String saveDocumentLocally(Long candidatId, MultipartFile file, DocumentType type) throws IOException, RuntimeException {
        //créer le dossier du candidat si il n'existe pas
        Path candidatDirectory = storageLocation.resolve(String.valueOf(candidatId));
        Files.createDirectories(candidatDirectory);

        if (file.isEmpty()) {
            throw new RuntimeException("Le fichier est vide");
        }

        // Valider le type de fichier (PDF ou images JPEG, PNG)
        String contentType = file.getContentType();
        if (contentType == null ||
                !(contentType.equals("application/pdf") ||
                        contentType.equals("image/jpeg") ||
                        contentType.equals("image/png"))) {
            throw new RuntimeException("Format de fichier non pris en charge. Seuls les fichiers PDF, JPEG et PNG sont autorisés.");
        }

        // Construire le chemin du fichier
        String fileUrl = candidatId + "/" + file.getOriginalFilename();
        Path filePath = candidatDirectory.resolve(file.getOriginalFilename());

        try {
            // Sauvegarder le fichier
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            return fileUrl;
        } catch (IOException e) {
            throw new RuntimeException("Échec de la sauvegarde du fichier : " + e.getMessage(), e);
        }
    }

    public byte[] getDocumentLocally(Document document) throws IOException {
        // Vérifier le chemin du fichier
        String filePath = document.getFilePath();
        if (filePath == null || filePath.isEmpty()) {
            throw new RuntimeException("Chemin du fichier non défini pour le document: " + document.getId());
        }

        Path documentPath = storageLocation.resolve(filePath);

        // Vérifier si le fichier existe
        if (!Files.exists(documentPath)) {
            throw new RuntimeException("Fichier non trouvé: " + filePath);
        }

        try {
            return Files.readAllBytes(documentPath);
        } catch (IOException e) {
            throw new IOException("Erreur lors de la lecture du fichier: " + filePath, e);
        }
    }

    private MediaType determineContentType(String fileName) {
        String extension = fileName.toLowerCase();
        if (extension.endsWith(".pdf")) {
            return MediaType.APPLICATION_PDF;
        } else if (extension.endsWith(".jpg") || extension.endsWith(".jpeg")) {
            return MediaType.IMAGE_JPEG;
        } else if (extension.endsWith(".png")) {
            return MediaType.IMAGE_PNG;
        } else {
            return MediaType.APPLICATION_OCTET_STREAM;
        }
    }
}

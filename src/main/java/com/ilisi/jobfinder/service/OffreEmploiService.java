package com.ilisi.jobfinder.service;


import com.ilisi.jobfinder.model.Entreprise;
import com.ilisi.jobfinder.model.OffreEmploi;
import com.ilisi.jobfinder.model.User;
import com.ilisi.jobfinder.repository.OffreEmploiRepository;
import com.ilisi.jobfinder.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class OffreEmploiService {
private final OffreEmploiRepository offremploiRepository;
private final UserRepository userRepository;


    public OffreEmploi create_Offre(OffreEmploi offreEmploi) {
        // Vérifiez si l'ID de l'entreprise est présent dans l'objet
        if (offreEmploi.getEntreprise() == null || offreEmploi.getEntreprise().getId() == 0) {
            throw new RuntimeException("Entreprise ID is required to create an offre.");
        }
        // Récupérez l'utilisateur par son ID
        User user = userRepository.findById(offreEmploi.getEntreprise().getId())
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + offreEmploi.getEntreprise().getId()));

        // Vérifiez que l'utilisateur est une entreprise
        if (!(user instanceof Entreprise)) {
            throw new RuntimeException("The provided user ID does not belong to an entreprise.");
        }
        // Associez l'entreprise à l'offre
        offreEmploi.setEntreprise((Entreprise) user);

        // Sauvegardez l'offre
        return offremploiRepository.save(offreEmploi);
    }
    public List<OffreEmploi> getAllOffres(){
        return offremploiRepository.findAll();
    }
    public Optional<OffreEmploi> getOffreById(int id){
        return offremploiRepository.findById(id);
    }
    public OffreEmploi updateOffre(int id, OffreEmploi updatedOffre) {
        return offremploiRepository.findById(id).map(offre -> {
            offre.setTitre(updatedOffre.getTitre());
            offre.setDescription(updatedOffre.getDescription());
            offre.setPoste(updatedOffre.getPoste());
            offre.setExigences(updatedOffre.getExigences());
            offre.setTypeContrat(updatedOffre.getTypeContrat());
            offre.setSalaire(updatedOffre.getSalaire());
            offre.setDatePublication(updatedOffre.getDatePublication());
            offre.setDateLimite(updatedOffre.getDateLimite());
            offre.setStatusOffre(updatedOffre.getStatusOffre());
            return offremploiRepository.save(offre);
        }).orElseThrow(() -> new RuntimeException("Offre non trouvée avec l'ID : " + id));
    }

    public void deleteOffre(int id){
        //verifier si cette offre existe
        if(offremploiRepository.existsById(id)){
            // si oui supprimez le
            offremploiRepository.deleteById(id);
        }else{
            throw new RuntimeException("Cette Offre d'ID "+id+" n'existe pas !!!");
        }
    }

}

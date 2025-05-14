package entities;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import services.sujetService;

import java.io.IOException;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Pdf {

    public void generateSujetsPdf(String filename) throws IOException, SQLException {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            // Premier contentStream pour la première page
            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                // Configuration de la police et du titre
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 16);
                contentStream.beginText();
                contentStream.newLineAtOffset(100, 750);
                contentStream.showText("Rapport des Sujets");
                contentStream.endText();

                // Style pour le contenu
                contentStream.setFont(PDType1Font.HELVETICA, 12);
                contentStream.beginText();
                contentStream.newLineAtOffset(50, 700);

                // Récupération des données
                sujetService service = new sujetService();
                List<Sujet> sujets = service.getAll();

                // Formatage de date
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

                // Ajout des données au PDF
                int yPosition = 700;
                for (Sujet sujet : sujets) {
                    if (yPosition < 100) { // Nouvelle page si on arrive en bas
                        contentStream.endText();
                        contentStream.close();

                        // Création d'une nouvelle page
                        page = new PDPage();
                        document.addPage(page);

                        // Nouveau contentStream pour la nouvelle page
                        try (PDPageContentStream newContentStream = new PDPageContentStream(document, page)) {
                            newContentStream.setFont(PDType1Font.HELVETICA, 12);
                            newContentStream.beginText();
                            newContentStream.newLineAtOffset(50, 750);

                            // Continuer l'écriture avec le nouveau contentStream
                            yPosition = 750;
                            writeSujet(newContentStream, sujet, dateFormatter, yPosition);
                            yPosition -= 85;
                        }
                        continue;
                    }

                    writeSujet(contentStream, sujet, dateFormatter, yPosition);
                    yPosition -= 85;
                }
                contentStream.endText();
            }

            // Sauvegarde du document
            document.save(filename + ".pdf");
        }

        // Ouverture automatique du PDF
        try {
            Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + filename + ".pdf");
        } catch (IOException e) {
            System.out.println("Impossible d'ouvrir le PDF automatiquement");
        }
    }

    private void writeSujet(PDPageContentStream contentStream, Sujet sujet,
                            DateTimeFormatter dateFormatter, int yPosition) throws IOException {
        contentStream.newLineAtOffset(0, -20);
        contentStream.showText("ID: " + sujet.getId());

        contentStream.newLineAtOffset(0, -15);
        contentStream.showText("Titre: " + sujet.getTitre());

        contentStream.newLineAtOffset(0, -15);
        contentStream.showText("Catégorie: " + sujet.getCategorie());

        contentStream.newLineAtOffset(0, -15);
        contentStream.showText("Date de création: " + sujet.getDateCreation().format(dateFormatter));

        String contenu = sujet.getContenu().length() > 150 ?
                sujet.getContenu().substring(0, 150) + "..." :
                sujet.getContenu();
        contentStream.newLineAtOffset(0, -15);
        contentStream.showText("Contenu: " + contenu);

        contentStream.newLineAtOffset(0, -20);
        contentStream.showText("--------------------------------------------------");
    }
}
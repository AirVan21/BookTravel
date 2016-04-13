package ru.spbau.googleAPI;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.services.books.Books;
import com.google.api.services.books.BooksRequestInitializer;
import com.google.api.services.books.model.Volume;
import com.google.api.services.books.model.Volumes;

import java.net.URLEncoder;

/**
 * Created by airvan21 on 13.04.16.
 */
public class BookSearcher {
    private static final String googleAPICode = "AIzaSyBPGuEnVZcQarLwzByVquiP4D-lmc2Q9OY";
    private static final String APPLICATION_NAME = "BookTravel";

    public static void queryGoogleBooks(JsonFactory jsonFactory, String query) throws Exception {
        // Set up Books client.
        final Books books = new Books.Builder(GoogleNetHttpTransport.newTrustedTransport(), jsonFactory, null)
                .setApplicationName(APPLICATION_NAME)
                .setGoogleClientRequestInitializer(new BooksRequestInitializer(googleAPICode))
                .build();

        // Set query string and filter only Google eBooks.
        System.out.println("Query: [" + query + "]");
        Books.Volumes.List volumesList = books.volumes().list(query);
        volumesList.setMaxResults((long) 5);
        volumesList.setPrintType("books");
        volumesList.setOrderBy("relevance");
        volumesList.setFilter("ebooks");

        // Execute the query.
        Volumes volumes = volumesList.execute();
        if (volumes.getTotalItems() == 0 || volumes.getItems() == null) {
            System.out.println("No matches found.");
            return;
        }

        // Output results.
        for (Volume volume : volumes.getItems()) {
            Volume.VolumeInfo volumeInfo = volume.getVolumeInfo();
            Volume.SaleInfo saleInfo = volume.getSaleInfo();
            System.out.println("==========");
            // Title.
            System.out.println("Title: " + volumeInfo.getTitle());
            // Image link
            System.out.println(volumeInfo.getImageLinks());
            // Author(s).
            java.util.List<String> authors = volumeInfo.getAuthors();
            if (authors != null && !authors.isEmpty()) {
                System.out.print("Author(s): ");
                for (int i = 0; i < authors.size(); ++i) {
                    System.out.print(authors.get(i));
                    if (i < authors.size() - 1) {
                        System.out.print(", ");
                    }
                }
                System.out.println();
            }

            // Description (if any).
            if (volumeInfo.getDescription() != null && volumeInfo.getDescription().length() > 0) {
                System.out.println("Description: " + volumeInfo.getDescription());
            }

            // Ratings (if any).
            if (volumeInfo.getRatingsCount() != null && volumeInfo.getRatingsCount() > 0) {
                System.out.print("User Rating: " + volumeInfo.getAverageRating().doubleValue());
                System.out.println(" (" + volumeInfo.getRatingsCount() + " rating(s))");
            }

            // Link to Google eBooks.
            System.out.println(volumeInfo.getInfoLink());
        }
        System.out.println("==========");
    }
}

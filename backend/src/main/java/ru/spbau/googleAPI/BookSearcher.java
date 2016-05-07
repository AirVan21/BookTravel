package ru.spbau.googleAPI;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.books.Books;
import com.google.api.services.books.BooksRequestInitializer;
import com.google.api.services.books.model.Volume;
import com.google.api.services.books.model.Volumes;
import ru.spbau.database.BookAuthor;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Optional;

/**
 * Created by airvan21 on 13.04.16.
 */
public class BookSearcher {
    public static final String GOOGLE_API_CODE  = "AIzaSyBPGuEnVZcQarLwzByVquiP4D-lmc2Q9OY";
    public static final String APPLICATION_NAME = "BookTravel";

    private static final long MAX_SEARCH_RESULT = 5;
    private final Books bookManager;

    public BookSearcher (Books bookManager) {
        this.bookManager = bookManager;
    }

    public Optional<String> getBookDescription(String title, List<BookAuthor> authors) {
        String query = buildQuery(title, authors);
        Volumes volumes;

        try {
            volumes = executeQuery(query);
        } catch (IOException e) {
            return Optional.empty();
        }

        if (volumes.getTotalItems() == 0 || volumes.getItems() == null) {
            return Optional.empty();
        }

        for (Volume volume : volumes.getItems()) {
            Volume.VolumeInfo volumeInfo = volume.getVolumeInfo();

            if (volumeInfo.getDescription() != null) {
                return Optional.of(volumeInfo.getDescription());
            }
        }

        return Optional.empty();
    }

    public void queryGoogleBooks(String query) throws Exception {
        Books.Volumes.List volumesList = bookManager.volumes().list(query);
        setSearchParameters(volumesList);

        // Execute the query.
        Volumes volumes = volumesList.execute();
        if (volumes.getTotalItems() == 0 || volumes.getItems() == null) {
            System.out.println("No matches found.");
            return;
        }

        // Output results.
        for (Volume volume : volumes.getItems()) {
            Volume.VolumeInfo volumeInfo = volume.getVolumeInfo();

            // Title
            System.out.println("Title: " + volumeInfo.getTitle());
            // Author(s).
            List<String> authors = volumeInfo.getAuthors();

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
        }
    }

    public static Books generateBooksManager(String applicationName, String apiCode) throws GeneralSecurityException, IOException {
        return new Books.Builder(GoogleNetHttpTransport.newTrustedTransport(), new JacksonFactory(), null)
                .setApplicationName(applicationName)
                .setGoogleClientRequestInitializer(new BooksRequestInitializer(apiCode))
                .build();
    }

    private Volumes executeQuery(String query) throws IOException {
        Books.Volumes.List volumesList = bookManager.volumes().list(query);
        setSearchParameters(volumesList);

        return volumesList.execute();
    }

    private static String buildQuery(String title, List<BookAuthor> authors) {
        StringBuilder sb = new StringBuilder();
        sb.append(title);
        authors.forEach(bookAuthor -> { sb.append(" "); sb.append(bookAuthor); });

        return sb.toString();
    }

    /**
     *
     * @param volumesList
     */
    private static void setSearchParameters(Books.Volumes.List volumesList) {
        volumesList.setMaxResults(MAX_SEARCH_RESULT);
        volumesList.setPrintType("books");
        volumesList.setOrderBy("relevance");
    }
}

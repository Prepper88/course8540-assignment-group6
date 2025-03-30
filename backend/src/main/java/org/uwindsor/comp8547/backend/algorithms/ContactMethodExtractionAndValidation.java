package org.uwindsor.comp8547.backend.algorithms;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;
import java.util.stream.Collectors;

public class ContactMethodExtractionAndValidation {

    // Canadian phone number regex pattern
    private static final String CANADA_PHONE_REGEX = "1?\\s?\\(?\\d{3}\\)?\\s?[-]?\\d{3}\\s?-?\\d{4}";
    private static final Pattern PHONE_PATTERN = Pattern.compile(CANADA_PHONE_REGEX);

    // Email regex pattern
    private static final String EMAIL_REGEX = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    /**
     * Method 1: Extract phone numbers from all files in the given directory.
     */
    public static List<String> extractPhoneNumber(String  filePath) {
        return extractMatchesFromFile(filePath, PHONE_PATTERN);
    }

    /**
     * Method 2: Extract email addresses from all files in the given directory.
     */
    public static List<String> extractEmails(String filePath) {
        return extractMatchesFromFile(filePath, EMAIL_PATTERN);
    }

    /**
     * Method 3: Validate if a given string is a valid Canadian phone number.
     */
    public static boolean isValidPhoneNumber(String phoneNumber) {
        return PHONE_PATTERN.matcher(phoneNumber).matches();
    }

    /**
     * Method 4: Validate if a given string is a valid email address.
     */
    public static boolean isValidEmail(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }

    /**
     * Helper method: Extract matches from a single file using the given regex pattern.
     */
    private static List<String> extractMatchesFromFile(String filePath, Pattern pattern) {
        Path file = Paths.get(filePath);
        List<String> matches = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file.toFile()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Matcher matcher = pattern.matcher(line);
                while (matcher.find()) {
                    matches.add(matcher.group());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return matches;
    }
    public static List<String> searchKeywordInFiles(String folderPath, String keyword) {
        List<String> matchResults = new ArrayList<>();
        File folder = new File(folderPath);
        File[] files = folder.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    matchResults.addAll(searchKeywordInFile(file, keyword));
                }
            }
        }
        return matchResults;
    }

    public static List<String> searchKeywordInFile(File file, String keyword) {
        List<String> matches = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains(keyword)) {
                    matches.add(line.trim());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return matches;
    }
    public static String readFirstLine(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String firstLine = reader.readLine();
            if (firstLine != null) {
                String cleanedLine = firstLine.replaceFirst("(?i)^url:", "").trim();
                System.out.println(cleanedLine);
                return cleanedLine;
            } else {
                System.out.println("File is empty or not found.");
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }
}

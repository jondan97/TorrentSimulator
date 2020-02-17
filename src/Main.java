import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {


    private static int hardDiskSizeRemaining;
    private static int numberOfConcurrentUploads;
    private static boolean firstTime = true;
    private static ReferenceBasedList prospectiveDownloads; //sorting purposes, this could have been a queue if we chose option 1 which is FIFO
    private static ArrayList<Movie> downloading; //starting size is known (numItems of prospectiveDownloads)
    private static ReferenceBasedList completed; //sorting purposes
    private static ArrayList<Movie> uploading; //chose ArrayList instead of array because im bored to implement "add" method for simple array :P
    private static ReferenceBasedList prospectiveUploads; //wish i could choose queue but it requires to show content of list and also check if an element exists (linked list is better as an infinite "see-through" queue)

    public static void main(String[] args) throws ListIndexOutOfBoundsException {
        int selection = -1;
        while (selection != 0) {
            selection = mainMenu();
            int innerMenuSelection = -1;
            if (selection == 1 && !firstTime) {
                while (innerMenuSelection != 0) {
                    innerMenuSelection = prospectiveDownloads();
                }
            } else if (selection == 2 && !firstTime) {
                while (innerMenuSelection != 0) {
                    innerMenuSelection = currentDownloads();
                }
            } else if (selection == 3 && !firstTime) {
                while (innerMenuSelection != 0) {
                    innerMenuSelection = completedDownloads();
                }
            } else if (selection == 4 && !firstTime) {
                while (innerMenuSelection != 0) {
                    innerMenuSelection = currentUploads();
                }
            } else if (selection == 5 && !firstTime) {
                while (innerMenuSelection != 0) {
                    innerMenuSelection = prospectiveUploads();
                }
            } else if (selection == 6) {
                initializeProgram();
            }
            System.out.println("-------------------");
            System.out.println("-------------------");
        }

        writeToFile("save.txt");
    }


    public static int mainMenu() {
        Scanner input = new Scanner(System.in);
        System.out.println("Choose from these choices:");
        System.out.println("1 - Prospective Downloads");
        System.out.println("2 - Current Downloads");
        System.out.println("3 - Completed Downloads");
        System.out.println("4 - Current Uploads");
        System.out.println("5 - Prospective Uploads");
        System.out.println("6 - Initialize Program");
        System.out.println("0 - Exit Program");
        return input.nextInt();
    }

    private static int prospectiveDownloads() throws ListIndexOutOfBoundsException {
        Scanner input = new Scanner(System.in);
        int innerMenuSelection;
        System.out.println("Prospective Downloads");
        System.out.println("Select Action:");
        System.out.println("1 - Add movie");
        System.out.println("2 - Delete movie");
        System.out.println("0 - Go back");
        innerMenuSelection = input.nextInt();
        input.nextLine(); //https://stackoverflow.com/questions/13102045/scanner-is-skipping-nextline-after-using-next-or-nextfoo
        if (innerMenuSelection == 1) {
            System.out.println("Name?");
            String name = input.nextLine();
            Movie newMovie = new Movie(name, 0);
            System.out.println("Size?");
            int size = input.nextInt();
            newMovie.setSize(size);
            if (!prospectiveDownloads.exists(newMovie) && !completed.exists(newMovie) && !downloading.contains(newMovie)) {
                prospectiveDownloads.addSortedBySize(newMovie);
                System.out.println("Movie successfully added.");
            } else {
                System.out.println("Movie already exists.");
            }
            prospectiveDownloads.showAll();
        } else if (innerMenuSelection == 2) {
            System.out.println("Choose a movie to delete:");
            prospectiveDownloads.showAll();
            int choice = input.nextInt();
            prospectiveDownloads.remove(choice);
            System.out.println("Movie successfully removed.");
        }
        System.out.println("//////////////////////");
        return innerMenuSelection;
    }

    private static int currentDownloads() throws ListIndexOutOfBoundsException {
        Scanner input = new Scanner(System.in);
        int innerMenuSelection;
        int selection;
        System.out.println("Current Downloads");
        System.out.println("Select Action:");
        System.out.println("1 - Download movie");
        System.out.println("2 - End downloading movie");
        System.out.println("3 - Cancel downloading movie");
        System.out.println("4 - Show all downloading movies");
        System.out.println("0 - Go back");
        innerMenuSelection = input.nextInt();
        if (innerMenuSelection == 1) {
            Movie movie = (Movie) prospectiveDownloads.get(1);
            int sizeAll = 0;
            for (Movie m : downloading) { //for each
                sizeAll += m.getSize();
            }
            if (sizeAll + movie.getSize() <= 99999) { //hardDiskSizeRemaining
                prospectiveDownloads.remove(1);
                downloading.add(movie);
                System.out.println(movie + ": successfully added.");
            }


        } else if (innerMenuSelection == 2) {
            int count = 1;
            System.out.println("Choose a movie to complete downloading:");
            for (Movie m : downloading) {
                System.out.println(count + " - " + m);
                count++;
            }
            int choice = input.nextInt();
            Movie m = downloading.get(choice - 1); //ArrayLists, just like Arrays start indexing from 0
            downloading.remove(choice - 1);
            completed.addSortedAlphabetically(m);
            hardDiskSizeRemaining -= m.getSize();
            System.out.println(m + ": successfully completed downloading.");

        } else if (innerMenuSelection == 3) {
            int count = 1;
            System.out.println("Choose a movie to delete:");
            for (Movie m : downloading) {
                System.out.println(count + " - " + m);
                count++;
            }
            int choice = input.nextInt();
            System.out.println("1 - Add movie back to prospective downloads.");
            System.out.println("2 - Delete movie entirely.");
            selection = input.nextInt();
            Movie m = downloading.get(choice - 1);
            if (selection == 1) {
                downloading.remove(choice - 1);
                prospectiveDownloads.addSortedBySize(m);
                System.out.println(m + ": successfully added.");
            } else if (selection == 2) {
                downloading.remove(choice - 1);
                System.out.println(m + ": successfully deleted.");
            }
        } else if (innerMenuSelection == 4) {
            int count = 1;
            for (Movie m : downloading) {
                System.out.println(count + " - " + m);
                count++;
            }
        }
        System.out.println("//////////////////////");
        return innerMenuSelection;
    }

    private static int completedDownloads() throws ListIndexOutOfBoundsException {
        Scanner input = new Scanner(System.in);
        int innerMenuSelection;
        System.out.println("Completed Downloads");
        System.out.println("Select Action:");
        System.out.println("1 - Show all completed downloads");
        System.out.println("2 - Delete movie");
        System.out.println("0 - Go back");
        innerMenuSelection = input.nextInt();
        if (innerMenuSelection == 1) {
            completed.showAll();
            System.out.println("Size of all movies that are completed:" + completed.sizeAll() + " MB");
            System.out.println("HardDiskSizeRemaining:" + hardDiskSizeRemaining + " MB");
        } else if (innerMenuSelection == 2) {
            System.out.println("Choose a completed movie to delete:");
            completed.showAll();
            int choice = input.nextInt();
            Movie m = (Movie) completed.get(choice);
            if (!prospectiveUploads.exists(m) && !uploading.get(choice - 1).equals(m)) { //i suppose it's a spelling mistake on the coursework and it means uploads? cannot delete a movie that has not yet been downloaded :/ (exists in prospectiveDownloads???)
                completed.remove(choice);
                System.out.println("Movie successfully removed.");
            } else {
                System.out.println("Movie cannot be removed because it exists as a Prospective Upload or as an Upload.");
            }
        }
        System.out.println("//////////////////////");
        return innerMenuSelection;
    }


    private static int currentUploads() throws ListIndexOutOfBoundsException {
        Scanner input = new Scanner(System.in);
        int innerMenuSelection;
        System.out.println("Current Uploads");
        System.out.println("Select Action:");
        System.out.println("1 - Add an upload");
        System.out.println("2 - Complete an upload");
        System.out.println("0 - Go back");
        innerMenuSelection = input.nextInt();
        if (innerMenuSelection == 1) {
            if (uploading.size() >= numberOfConcurrentUploads) {
                System.out.println("Cannot add another upload. Please wait for an upload to finish.");
            } else {
                System.out.println("Choose a movie to upload:");
                completed.showAll();
                int choice = input.nextInt();
                Movie m = (Movie) completed.get(choice);
                if (uploading.contains(m)) {
                    m = new Movie(m.getName(), m.getSize());
                    ArrayList<Integer> idsIncluded = new ArrayList<>(numberOfConcurrentUploads);
                    for (int i = 0; i < uploading.size(); i++) { //so that it's unique every time
                        if (m.equals(uploading.get(i))) {
                            idsIncluded.add(uploading.get(i).getId());
                            if (!(m.getId() == uploading.get(i).getId()) && !(idsIncluded.contains(m.getId()))) {
                                m.setId(m.getId());
                                break;
                            } else {
                                m.setId(idsIncluded.get(idsIncluded.size() - 1) + 50); //assumed that the number of current uploads is never that big (50+)
                            }
                        }

                    }
                    uploading.add(m);
                    System.out.println(m + ": successfully added.(" + m.getId() + ")");
                } else {
                    uploading.add(m);
                    System.out.println(m + ": successfully added.(" + m.getId() + ")");
                }

            }
        } else if (innerMenuSelection == 2) {
            System.out.println("Choose a movie to complete uploading:");
            int count = 0;
            for (Movie m : uploading) {
                System.out.println(count + 1 + " - " + m + " (" + m.getId() + ")");
                count++;
            }
            int choice = input.nextInt();
            uploading.remove(choice - 1);
            System.out.println("Movie successfully finished uploading");
            if (!prospectiveUploads.isEmpty()) {
                Movie m = (Movie) prospectiveUploads.get(1);
                prospectiveUploads.remove(1);
                uploading.add(m);
                System.out.println("Movie: " + m + " added from queue.");
            }
        }
        System.out.println("//////////////////////");
        return innerMenuSelection;
    }

    private static int prospectiveUploads() throws ListIndexOutOfBoundsException {
        Scanner input = new Scanner(System.in);
        int innerMenuSelection;
        System.out.println("Prospective Uploads");
        System.out.println("Select Action:");
        System.out.println("1 - Add movie to upload");
        System.out.println("2 - Show all queued uploads");
        System.out.println("0 - Go back");
        innerMenuSelection = input.nextInt();
        if (innerMenuSelection == 1) {
            System.out.println("Choose a movie to upload:");
            completed.showAll();
            int choice = input.nextInt();
            Movie m = (Movie) completed.get(choice);
            if (uploading.size() >= numberOfConcurrentUploads) {
                if (prospectiveUploads.exists(m) || uploading.contains(m)) {
                    m = new Movie(m.getName(), m.getSize());
                    m.setId((int) (Math.random() * 1000 + 500)); //unique identifier for every remote user (it is assumed that there will never be more than 500 concurrent uploads from a single user)
                    prospectiveUploads.add(prospectiveUploads.size() + 1, m); //added last, goes through everything, array-based list could be used here but since it's a queue, there's always items being added and removed which requires shifting, my guess is that both(arraylist/referencebased) have similar trade-offs
                    System.out.println(m + ": successfully added in queue.");
                } else {
                    prospectiveUploads.add(prospectiveUploads.size() + 1, m);
                    System.out.println(m + ": successfully added in queue.");
                }
            } else {
                uploading.add(m);
                System.out.println(m + ": successfully added.");
            }

        } else if (innerMenuSelection == 2) {
            prospectiveUploads.showAll();
        }
        return innerMenuSelection;
    }

    private static void initializeProgram() {
        FileReader theFile = null;
        try {
            theFile = new FileReader("save.txt");
            BufferedReader fileIn = new BufferedReader(theFile);
            firstTime = Boolean.parseBoolean(fileIn.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (firstTime) {
            Scanner input = new Scanner(System.in);
            System.out.println("Input hardDiskSizeRemaining");
            hardDiskSizeRemaining = input.nextInt();
            System.out.println("Input numberOfConcurrentUploads");
            numberOfConcurrentUploads = input.nextInt();
            prospectiveDownloads = new ReferenceBasedList();
            completed = new ReferenceBasedList();
            prospectiveUploads = new ReferenceBasedList();
            downloading = new ArrayList<Movie>();
            uploading = new ArrayList<Movie>(numberOfConcurrentUploads);
            firstTime = false;
            System.out.println("Program Initialized.");
        } else if (!firstTime) {
            prospectiveDownloads = new ReferenceBasedList();
            completed = new ReferenceBasedList();
            prospectiveUploads = new ReferenceBasedList();
            downloading = new ArrayList<Movie>(prospectiveDownloads.size());
            uploading = new ArrayList<Movie>(numberOfConcurrentUploads);
            readFromFile("save.txt");
            System.out.println("Program Restored.");
        }
    }

    public static void writeToFile(String fileName) { //creates a unique file with everything in it
        int count = 0;
        try {
            FileWriter theFile = new FileWriter(fileName);
            PrintWriter fileOut = new PrintWriter(theFile);
            if (fileName.equals("save.txt")) {
                fileOut.println(firstTime);
                fileOut.println(hardDiskSizeRemaining);
                fileOut.println(numberOfConcurrentUploads);
                fileOut.println(prospectiveDownloads.size());
                for (int i = 1; i <= prospectiveDownloads.size(); i++) {
                    fileOut.println(((Movie) prospectiveDownloads.get(i)).getName());
                    fileOut.println(((Movie) prospectiveDownloads.get(i)).getSize());
                }
                fileOut.println(downloading.size());
                for (Movie m : downloading) {
                    fileOut.println(((Movie) downloading.get(count)).getName());
                    fileOut.println(((Movie) downloading.get(count)).getSize());
                    count++;
                }
                fileOut.println(completed.size());
                for (int i = 1; i <= completed.size(); i++) {
                    fileOut.println(((Movie) completed.get(i)).getName());
                    fileOut.println(((Movie) completed.get(i)).getSize());
                }
                count = 0;
                fileOut.println(uploading.size());
                for (Movie m : uploading) {
                    fileOut.println(((Movie) uploading.get(count)).getName());
                    fileOut.println(((Movie) uploading.get(count)).getSize());
                    fileOut.println(((Movie) uploading.get(count)).getId());
                    count++;
                }
                fileOut.println(prospectiveUploads.size());
                for (int i = 1; i <= prospectiveUploads.size(); i++) {
                    fileOut.println(((Movie) prospectiveUploads.get(i)).getName());
                    fileOut.println(((Movie) prospectiveUploads.get(i)).getSize());
                    fileOut.println(((Movie) prospectiveUploads.get(i)).getId());
                }
                //-------------------------------------------------
            }
            fileOut.close();
        } catch (IOException e) {
            System.out.println("Problem writing to the file");
        } catch (ListIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    public static void readFromFile(String fileName) { //reads from a single file named saved.txt
        String name;
        int size;
        int id;
        Movie m;
        int count = 1;

        try {
            FileReader theFile = new FileReader(fileName);
            BufferedReader fileIn = new BufferedReader(theFile);
            if (fileName.equals("save.txt")) {
                firstTime = Boolean.parseBoolean(fileIn.readLine());
                hardDiskSizeRemaining = Integer.parseInt(fileIn.readLine());
                numberOfConcurrentUploads = Integer.parseInt(fileIn.readLine());
                int sizeFromFile = Integer.parseInt(fileIn.readLine());
                if (sizeFromFile != 0) {
                    for (int i = 1; i <= sizeFromFile; i++) {
                        name = fileIn.readLine();
                        size = Integer.parseInt(fileIn.readLine());
                        m = new Movie(name, size);
                        prospectiveDownloads.add(i, m);
                    }
                }
                sizeFromFile = Integer.parseInt(fileIn.readLine());
                if (sizeFromFile != 0) {
                    for (int i = 0; i < sizeFromFile; i++) {
                        name = fileIn.readLine();
                        size = Integer.parseInt(fileIn.readLine());
                        m = new Movie(name, size);
                        downloading.add(m);
                    }
                }
                sizeFromFile = Integer.parseInt(fileIn.readLine());
                if (sizeFromFile != 0) {
                    for (int i = 1; i <= sizeFromFile; i++) {
                        name = fileIn.readLine();
                        size = Integer.parseInt(fileIn.readLine());
                        m = new Movie(name, size);
                        completed.add(i, m);
                    }
                }
                sizeFromFile = Integer.parseInt(fileIn.readLine());
                if (sizeFromFile != 0) {
                    for (int i = 0; i < sizeFromFile; i++) {
                        name = fileIn.readLine();
                        size = Integer.parseInt(fileIn.readLine());
                        m = new Movie(name, size);
                        id = Integer.parseInt(fileIn.readLine());
                        m.setId(id);
                        uploading.add(m);
                    }
                }
                sizeFromFile = Integer.parseInt(fileIn.readLine());
                if (sizeFromFile != 0) {
                    for (int i = 1; i <= sizeFromFile; i++) {
                        name = fileIn.readLine();
                        size = Integer.parseInt(fileIn.readLine());
                        m = new Movie(name, size);
                        id = Integer.parseInt(fileIn.readLine());
                        m.setId(id);
                        prospectiveUploads.add(i, m);
                    }
                }
            }
            fileIn.close();
        } catch (FileNotFoundException e) {
            System.out.println("Unable to locate the file: " + fileName);
        } catch (IOException | ListIndexOutOfBoundsException e) {
            System.out.println("There was a problem reading the file: " + fileName);
        }
    }
}

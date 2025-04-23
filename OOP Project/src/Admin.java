import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.Date;

public class Admin extends User implements CRUD
{
    private String role;
    private int workingHours;

    public Admin(String username, String password, Date dateOfBirth, String role, int workingHours) {
        super(username, password, dateOfBirth);
        this.role = role;
        this.workingHours = workingHours;
    }

    Scanner scanner = new Scanner(System.in);
    public void adminDashboard()
    {
        System.out.println("\n---Welcome Admin Menu---");
        System.out.println("--Please select an option--");
        System.out.println("1) Show all rooms");
        System.out.println("2) Show all events");
        System.out.println("3) Show all attendees");
        System.out.println("4) Show all organizers");
        System.out.println("5) Add a room");
        System.out.println("6) Category Dashboard");
        System.out.println("7) Logout");
        System.out.println("Enter your choice: ");
        int x = scanner.nextInt();
        scanner.nextLine();
        switch(x)
        {
            case 1:
                viewAllRooms();
                adminDashboard();
                break;
            case 2:
                viewAllEvents();
                adminDashboard();
                break;
            case 3:
                viewAllAttendees();
                adminDashboard();
                break;
            case 4:
                viewAllOrganizers();
                adminDashboard();
                break;
            case 5:
                addRoom();
                adminDashboard();
                break;
            case 6:
                categoryDashboard();
                adminDashboard();
                break;
            case 7:
                logout();
                break;
            default:
                System.out.println("Invalid choice.");
                adminDashboard();
                break;
        }
    }
    public void viewAllRooms(){
        System.out.println("\n---All rooms:---");
        Database.getRooms().forEach(System.out::println);
    }
    public void create()
    {
        System.out.println("\nPlease enter the name of the category:");
        String name = scanner.nextLine();
        Category category = new Category(name);
        Database.getCategories().add(category);
        System.out.println("Category created successfully!");
        Category.noOfCategories++;
    }
    public void update()
    {
        read();
        System.out.println("\nPlease enter the name of the category:");
        String name = scanner.nextLine();
        boolean found = false;
        for (Category category : Database.getCategories()) {
            if (category.getName().equals(name)) {
                System.out.println("Please enter the new name of the category:");
                String newName = scanner.nextLine();
                category.setName(newName);
                System.out.println("Category updated successfully!");
                found = true;
            }
        }
        if (!found) {
            System.out.println("Category not found!");
        }
    }
    public void delete() {
        read();
        System.out.println("\nPlease enter the name of the category:");
        String name = scanner.nextLine();
        boolean found = false;
        for (Category category : Database.getCategories()) {
            if (category.getName().equalsIgnoreCase(name)) {
                Database.getCategories().remove(category);
                System.out.println("Category deleted successfully!");
                Category.noOfCategories--;
                found = true;
                break;
            }
        }
        if(!found){
            System.out.println("Category not found!");
        }
    }

    public void read(){
        int i = 1;
        System.out.println("\nExisting categories: ");
        for (Category category : Database.getCategories())
        {
            System.out.println(i+") "+category.toString());
            i++;
        }
    }

    public void viewAllEvents()
    {
        System.out.println("\n---All Events---");
        Database.getEvents().forEach(System.out::println);
    }
    public void viewAllAttendees()
    {
        System.out.println("\n---All Attendees---");
        Database.getAttendees().forEach(System.out::println);
    }
    public void viewAllOrganizers()
    {
        System.out.println("\n---All Organizers---");
        Database.getOrganizers().forEach(System.out::println);
    }

    public void categoryDashboard()
    {
            while (true)
            {
                try {
                    System.out.println("--- Category Dashboard ---");
                    System.out.println("\nPlease select an option:");
                    System.out.println("1) Create new category");
                    System.out.println("2) Update an existing category");
                    System.out.println("3) Read existing categories");
                    System.out.println("4) Delete an existing category");
                    System.out.println("5) Get total number of categories");
                    System.out.println("6) Return to admin dashboard");
                    System.out.print("Enter your choice: ");

                    int x = scanner.nextInt();
                    scanner.nextLine(); // consume newline

                    switch (x) {
                        case 1:
                            create();
                            break;
                        case 2:
                            update();
                            break;
                        case 3:
                            read();
                            break;
                        case 4:
                            delete();
                            break;
                        case 5:
                            System.out.println("Total Categories: " + Category.getNoOfCategories());
                            break;
                        case 6:
                            adminDashboard();
                            return; // return to exit the method
                        default:
                            System.out.println("Invalid choice. Please select a number from 1 to 6.");
                    }

                } catch (Exception e) {
                    System.out.println("Invalid input. Please enter a numeric value.");
                    scanner.nextLine(); // clear the invalid input
                }
            }
    }



    public void addRoom()
    {
        System.out.println("\nEnter room name: ");
        String roomName = scanner.nextLine();
        System.out.println("Enter available time for the room: (HH:mm)");
        System.out.print("Available time: ");
        LocalTime time = null;
        while (time == null) {
            try {
                String availabletime = scanner.nextLine();
                time = LocalTime.parse(availabletime, DateTimeFormatter.ofPattern("HH:mm"));
                time = time.withSecond(0).withNano(0);
            } catch (Exception e) {
                System.out.println("Invalid time format. Please enter the time in HH:mm format.");
                System.out.print("Available time: ");
            }
        }
        Room newroom = new Room(roomName,time);
        newroom.addAvailableTime(time);
        Database.getRooms().add(newroom);
        System.out.println("Room added successfully At Time "+time);
    }

    @Override
    public String toString()
    {
        return "\nUsername:  " + username + "  ,Date of Birth:  " + dateOfBirth + "  ,Role:  " + role + "  ,Working Hours:  " + workingHours ;
    }

}

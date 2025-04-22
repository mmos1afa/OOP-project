import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.Date;

public class Admin extends User implements CRUD{
    private String role;
    private int workingHours;

    public Admin(String username, String password, Date dateOfBirth, String role, int workingHours) {
        super(username, password, dateOfBirth);
        this.role = role;
        this.workingHours = workingHours;
    }

    Scanner scanner = new Scanner(System.in);
    public void adminDashboard(){
        System.out.println("\nWelcome to the Admin Dashboard");
        System.out.println("Please select an option");
        System.out.println("1. Show all rooms");
        System.out.println("2. Show all events");
        System.out.println("3. Show all attendees");
        System.out.println("4. Show all organizers");
        System.out.println("5. Add a room");
        System.out.println("6. Category Dashboard");
        System.out.println("7. Logout");
        System.out.println("Enter your choice: ");
        int x = scanner.nextInt();
        scanner.nextLine();
        switch(x){
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
        System.out.println("\nAll rooms:");
        Database.getRooms().forEach(System.out::println);
    }
    public void create(){
        System.out.println("\nPlease enter the name of the category:");
        String name = scanner.nextLine();
        Category category = new Category(name);
        Database.getCategories().add(category);
        System.out.println("Category created successfully!");
        Category.noOfCategories++;
    }
    public void update(){
        read();
        System.out.println("\nPlease enter the name of the category:");
        String name = scanner.nextLine();
        for (Category category : Database.getCategories()) {
            if (category.getName().equals(name)) {
                System.out.println("Please enter the new name of the category:");
                String newName = scanner.nextLine();
                category.setName(newName);
                System.out.println("Category updated successfully!");
            }
            else {
                System.out.println("Category not found!");
            }
        }
    }
    public void delete(){
        read();
        System.out.println("\nPlease enter the name of the category:");
        String name = scanner.nextLine();
        for (Category category : Database.getCategories())
            if (category.getName().equalsIgnoreCase(name)) {
                Database.getCategories().remove(category);
                System.out.println("Category deleted successfully!");
                Category.noOfCategories--;
                break;
            } else {
                System.out.println("Category not found!");
            }
    }

    public void read(){
        int i = 1;
        System.out.println("\nExisting categories: ");
        for (Category category : Database.getCategories()) {
            System.out.println(i+") "+category.toString());
            i++;
        }
    }

    public void viewAllEvents(){
        System.out.println("\nAll events:");
        Database.getEvents().forEach(System.out::println);
    }
    public void viewAllAttendees(){
        System.out.println("\nAll attendees:");
        Database.getAttendees().forEach(System.out::println);
    }
    public void viewAllOrganizers(){
        System.out.println("\nAll organizers:");
        Database.getOrganizers().forEach(System.out::println);
    }

    public void categoryDashboard(){
        System.out.println("\nPlease select an option");
        System.out.println("1. Create new category");
        System.out.println("2. Update an existing category");
        System.out.println("3. Read existing categories");
        System.out.println("4. Delete an existing category");
        System.out.println("5. Get total number of categories");
        System.out.println("6. Return to admin dashboard");
        System.out.println("Enter your choice: ");
        int x = scanner.nextInt();
        scanner.nextLine();
        switch(x){
            case 1:
                create();
                categoryDashboard();
                break;
            case 2:
                update();
                categoryDashboard();
                break;
            case 3:
                read();
                categoryDashboard();
                break;
            case 4:
                delete();
                categoryDashboard();
                break;
            case 5:
                System.out.println(Category.getNoOfCategories());
                categoryDashboard();
                break;
            case 6:
                adminDashboard();
                break;
            default:
                System.out.println("Invalid choice.");
                categoryDashboard();
                break;
        }
        scanner.close();
    }

    public void addRoom(){
        System.out.println("\nEnter room name: ");
        String roomName = scanner.nextLine();
        System.out.println("Enter available time for the room: (HH:mm)");
        System.out.print("Available time: ");
        Scanner scannerx = new Scanner(System.in);
        String availabletime = scannerx.nextLine();
        LocalTime time = LocalTime.parse(availabletime, DateTimeFormatter.ofPattern("HH:mm"));
        time = time.withSecond(0).withNano(0);
        Room newroom = new Room(roomName,time);
        newroom.addAvailableTime(time);
        Database.getRooms().add(newroom);
        System.out.println("Room added successfully At Time "+time);
    }

    @Override
    public String toString() {
        return "\nUsername:  " + username + "  ,Date of Birth:  " + dateOfBirth + "  ,Role:  " + role + "  ,Working Hours:  " + workingHours ;
    }
}

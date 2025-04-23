import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.Date;

public class Organizer extends User implements CRUD {
    private final Wallet wallet;

    public Organizer(String username, String password, Date dateOfBirth)
    {
        super(username, password, dateOfBirth);
        this.wallet = new Wallet(0.0);
    }
    public Wallet getWallet()
    {
        return wallet;
    }

    Scanner scanner = new Scanner(System.in);

    public void organizerDashboard()
    {
        while (true)
        {
            try {
                System.out.println("-- Please select an option --");
                System.out.println("1) Event dashboard");
                System.out.println("2) View my attendees");
                System.out.println("3) View all available rooms");
                System.out.println("4) Show my balance");
                System.out.println("5) Statistics");
                System.out.println("6) Logout");

                int x = scanner.nextInt();
                scanner.nextLine(); // consume newline

                switch (x)
                {
                    case 1:
                        eventDashboard();
                        break;
                    case 2:
                        viewMyAttendees();
                        break;
                    case 3:
                        viewAvailableRooms();
                        break;
                    case 4:
                        viewBalance();
                        break;
                    case 5:
                        Earnings();
                        break;
                    case 6:
                        logout();
                        return;
                    default:
                        System.out.println("Invalid choice. Please select from 1 to 6.");
                }
            } catch (Exception e)
            {
                System.out.println("Invalid input. Please enter a number from 1 to 6.");
                scanner.nextLine();
            }
        }
    }



    public void viewMyAttendees()
    {
        System.out.println("\n---Your Attendees---");
        for (Event event : Database.getEvents())
        {
            if (event.getOrganizer() != null && event.getOrganizer().equals(this))
            {
                if (event.getAttendees().isEmpty())
                {
                    System.out.println("  No attendees yet!");
                }
                else
                {
                    for (Attendee attendee : event.getAttendees())
                    {
                        System.out.println(event.getTitle() + "Attendees: ");
                        System.out.println("  - " + attendee.getUsername());
                    }
                }
            }
        }
    }

    public LocalTime viewAvailableRooms()
    {
        System.out.println("\nEnter Time you want to check available rooms in: (HH:mm)");
        Scanner etime = new Scanner(System.in);
        String eventtime = etime.nextLine();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime time = LocalTime.parse(eventtime, formatter);
        List<Room> availablerooms = Database.getRooms().stream().filter(r -> r.isAvailable(time)).toList();
        if (availablerooms.isEmpty()) {
            System.out.println("No rooms available at " + time + ".");
            viewAvailableRooms();
        }
        else
        {
            System.out.println("Available rooms at " + time + ":");
            for (Room room : availablerooms)
            {
                System.out.println("  - " + room);
            }
        }
        return time;
    }

    public void viewBalance()
    {
        System.out.println("\nYour Balance: " + wallet.getBalance());
    }

    public void read() {
        System.out.println("\n---Your Organized Events---");
        for (Event event : Database.getEvents())
        {
            if (event.getOrganizer() != null && event.getOrganizer().equals(this))
            {
                System.out.println(event);
            }
        }
    }

    public Room bookRoom(String roomName,LocalTime time) {
        Room selectedRoom = null;

        for (Room r : Database.getRooms()) {
            if (r.getRoomName().equals(roomName))
            {
                selectedRoom = r;
                break;
            }
        }
        if (selectedRoom == null) {
            System.out.println("Room not found or already booked!");
            organizerDashboard();
        }
        selectedRoom.book();
        selectedRoom.removeAvailableTime(time);
        return selectedRoom;
    }

    public void create() {
        System.out.println("---Create Your Event---");

        System.out.print("\nEnter event name: ");
        String name = scanner.nextLine();

        System.out.print("Enter ticket price: ");
        double price;
        try {
            price = scanner.nextDouble();
        } catch (InputMismatchException e) {
            System.out.println("Invalid price entered.");
            scanner.nextLine();
            return;
        }
        scanner.nextLine();

        System.out.println("Enter event category number you want to choose:");
        List<Category> categories = Database.getCategories();
        for (int i = 0; i < categories.size(); i++) {
            System.out.println((i + 1) + ". " + categories.get(i));
        }

        int categoryChoice = scanner.nextInt();
        scanner.nextLine();

        if (categoryChoice < 1 || categoryChoice > categories.size()) {
            System.out.println("Invalid category choice.");
            organizerDashboard();
            return;
        }

        Category category = categories.get(categoryChoice - 1);

        LocalTime time = viewAvailableRooms();
        System.out.print("Enter the room name you want to book: ");
        String roomName = scanner.nextLine();
        Room selectedRoom = bookRoom(roomName,time);


        Event event = new Event(selectedRoom, name, price, category);
        event.setOrganizer(this);
        Database.getEvents().add(event);

        System.out.println("Event created successfully!");
    }

    public void update()
    {
        System.out.println("---Update Your Event---");

        read();
        System.out.print("\nEnter event name you want to update: ");
        String name = scanner.nextLine();
        boolean found = false;

        for(Event event:Database.getEvents())
        {
            if(event != null && event.getTitle().equalsIgnoreCase(name))
            {
                System.out.println("Please enter the new name of the event:");
                String newName = scanner.nextLine();
                event.setTitle(newName);

                LocalTime time = viewAvailableRooms();
                System.out.print("Enter the new room name you want to book: ");
                String roomName = scanner.nextLine();
                Room selectedRoom = bookRoom(roomName,time);
                event.setRoom(selectedRoom);


                System.out.println("Please enter the new price of the event:");
                double newPrice = scanner.nextDouble();
                scanner.nextLine();
                event.setPrice(newPrice);

                System.out.print("Available categories: \n");
                Database.getCategories().forEach(System.out::println);
                System.out.println("Please enter the new category of the event:");
                String newCategory = scanner.nextLine();
                for(Event event1:Database.getEvents())
                {
                    if(event1.getCategory().getName().equals(newCategory))
                    {
                        event.setCategory(event1.getCategory());
                    }
                    else{
                        System.out.println("Category not found!");
                    }
                    found = true;
                }
            }
        }
        if(!found)
        {
            System.out.println("Event not found!");
        }
    }
    public void delete()
    {
        System.out.println("---Delete Your Event---");

        read();
        System.out.print("\nEnter event name you want to delete: ");
        String name = scanner.nextLine();
        for(Event event:Database.getEvents()){
            if(event.getTitle().equals(name)){
                Database.getEvents().remove(event);
                System.out.println("Event deleted successfully!");
            }
            else
            {
                System.out.println("Event not found!");
            }
        }
    }


    @Override
    public String toString()
    {
        return "\nUsername: " + username + "  ,Date of Birth: " + dateOfBirth ;
    }

    public void eventDashboard()
    {
        while (true)
        {
            try {
                System.out.println("--- Welcome To Event Menu ---");
                System.out.println("\nPlease select an option:");
                System.out.println("1) Create new event");
                System.out.println("2) Update an existing event");
                System.out.println("3) View my events");
                System.out.println("4) Delete an existing event");
                System.out.println("5) Return to organizer dashboard");
                System.out.print("Enter your choice: ");

                int x = scanner.nextInt();
                scanner.nextLine(); // Clear newline

                switch (x)
                {
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
                        organizerDashboard();
                        return;
                    default:
                        System.out.println("Invalid choice! Please enter a number between 1 and 5.");
                }
            }
            catch (Exception e)
            {
                System.out.println("Invalid input! Please enter a numeric value.");
                scanner.nextLine();
            }
        }
    }


    private void Earnings()
    {
        System.out.println("\n---View Earnings---");
        double totalearnings = wallet.getBalance();
        System.out.println("Total Earnings: " + totalearnings);
        System.out.println("Earnings By Event: ");
        for (Event event : Database.getEvents())
        {
            if (event.getOrganizer() != null && event.getOrganizer().equals(this))
            {
                System.out.println(event+": "+event.getTicketPrice() * (event.getAttendees().size()-1));
            }
        }
    }

}

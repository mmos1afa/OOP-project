import java.util.Scanner;
import java.util.Date;

public class User {
    protected String username;
    protected String password;
    protected Date dateOfBirth;

    public User(String username, String password, Date dateOfBirth) {
        this.username = username;
        this.password = password;
        this.dateOfBirth = dateOfBirth;
    }

    public String getUsername() {
        return username;
    }

    private static Scanner scanner = new Scanner(System.in);

    public static void mainDashboard() {
        while (true) {
            try {
                System.out.println("---Welcome to the Event Management System---");
                System.out.println("--Please select an option--");
                System.out.println("1) Register \n2) Login \n3) Exit");

                int x = scanner.nextInt();
                scanner.nextLine();

                switch (x) {
                    case 1:
                        User.register();
                        break;
                    case 2:
                        User.login();
                        break;
                    case 3:
                        System.out.println("Exiting...");
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Invalid choice. Please select 1, 2, or 3.");
                }
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // clear the invalid input
            }
        }
    }

    public static void register() {
        System.out.println("Register as: \n1) Admin \n2) Organizer \n3) Attendee");

        int choice = -1;
        boolean validchoice = false;
        while (!validchoice){
        try {
            choice = scanner.nextInt();
            scanner.nextLine();
            if (choice >= 1 && choice <= 3) {
                validchoice = true;
            } else {
                System.out.println("Invalid choice. Please Try Again");
            }
        }catch(Exception e){
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine();
                register();
                return;
            }
        }
        System.out.print("Username: ");
        String username = scanner.nextLine();
        boolean usernameTaken = false;
        switch (choice) {
            case 1:
                for (Admin a : Database.getAdmins()) {
                    if (a.getUsername().equals(username)) {
                        usernameTaken = true;
                        break;
                    }
                }
                break;
            case 2:
                for (Organizer o : Database.getOrganizers()) {
                    if (o.getUsername().equals(username)) {
                        usernameTaken = true;
                        break;
                    }
                }
                break;
            case 3:
                for (Attendee at : Database.getAttendees()) {
                    if (at.getUsername().equals(username)) {
                        usernameTaken = true;
                        break;
                    }
                }
                break;
        }

        if (usernameTaken) {
            System.out.println("Username already taken. Please try again.");
            register();
            return;
        }

        System.out.print("Password: ");
        String password = scanner.nextLine();

        Date dob;
        while (true) {
            System.out.print("Date of Birth (YYYY-MM-DD): ");
            try {
                dob = java.sql.Date.valueOf(scanner.nextLine());
                break;
            } catch (Exception e) {
                System.out.println("Invalid date format. Please try again.");
            }
        }

        switch (choice) {
            case 1:
                System.out.print("Role: ");
                String role = scanner.nextLine();
                System.out.print("Working Hours: ");
                int hours = scanner.nextInt();
                scanner.nextLine();
                Admin admin = new Admin(username, password, dob, role, hours);
                Database.getAdmins().add(admin);
                System.out.println("admin registered.");
                break;

            case 2:
                Organizer organizer = new Organizer(username, password, dob);
                Database.getOrganizers().add(organizer);
                System.out.println("Organizer registered.");
                break;

            case 3:
                double balance = 0;
                boolean validBalance = false;
                while (!validBalance) {
                    try {
                        System.out.print("Initial Wallet Balance: ");
                        balance = scanner.nextDouble();
                        scanner.nextLine();
                        validBalance = true;
                    } catch (Exception ex) {
                        System.out.println("Invalid input. Please enter a numeric value.");
                        scanner.nextLine();
                    }
                }

                System.out.print("Address: ");
                String address = scanner.nextLine();

                Gender gender = null;
                boolean validGender = false;
                while (!validGender) {
                    try {
                        System.out.print("Gender (MALE/FEMALE): ");
                        gender = Gender.valueOf(scanner.nextLine().trim().toUpperCase());
                        validGender = true;
                    } catch (Exception ex) {
                        System.out.println("Invalid gender. Please enter MALE or FEMALE.");
                    }
                }

                System.out.print("Interests: ");
                String interests = scanner.nextLine();

                Attendee attendee = new Attendee(username, password, dob, balance, address, gender, interests);
                Database.getAttendees().add(attendee);
                System.out.println(" Attendee registered.");
                break;
        }

        mainDashboard();
    }

    public boolean loginCheck(String inputUsername, String inputPassword) {
        boolean isvalid = this.username.equals(inputUsername) && this.password.equals(inputPassword);
        if(!isvalid){
            System.out.println("Invalid Login For: "+ username);
        }
        return isvalid;
    }

    public static void login() {

        System.out.println("Login as: \n 1) Admin \n 2) Organizer \n 3) Attendee");
        int choice = scanner.nextInt();
        scanner.nextLine();
        if(choice>3||choice<1)
        {
            System.out.println("invalid choice. Please try again.");
            login();
            return;
        }

        System.out.print("Username: ");
        String username = scanner.next().trim();
        System.out.print("Password: ");
        String password = scanner.next().trim();

        boolean found = false;

        switch (choice) {
            case 1:
                for (Admin a : Database.getAdmins()) {
                    if (a.loginCheck(username, password)) {
                        System.out.println(" Admin login successful.");
                        a.adminDashboard();
                        found = true;
                        break;
                    }
                }
                break;
            case 2:
                for (Organizer o : Database.getOrganizers()) {
                    if (o.loginCheck(username, password)) {
                        System.out.println(" Organizer login successful.");
                        o.organizerDashboard();
                        found = true;
                        break;
                    }
                }
                break;
            case 3:
                for (Attendee at : Database.getAttendees()) {
                    if (at.loginCheck(username, password)) {
                        System.out.println(" Attendee login successful.");
                        at.attendeeMenu();
                        found = true;
                        break;
                    }
                }
                break;
            default:
                System.out.println(" Invalid user type.");
                login();
                break;
        }
        if (!found) {
            System.out.println(" Login failed. Check username/password.");
            login();
        }
    }

    public void logout() {
        System.out.println("Logging out....");
        mainDashboard();
    }
}

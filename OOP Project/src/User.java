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

    public static void mainDashboard(){
        System.out.println("---Welcome to the Event Management System---");
        System.out.println("Please select an option");
        Scanner scanner = new Scanner(System.in);
        System.out.println("1.Register \n2.Login \n3.Exit");
        int x = scanner.nextInt();
        switch(x)
        {
            case 1:
                User.register();
            case 2:
                User.login();
                break;
            case 3:
                System.out.println("Exiting...");
                System.exit(0);

            default:
                System.out.println("Invalid choice");
                mainDashboard();
                break;
        }
        scanner.close();
    }

    public static void register() {
        System.out.println("Register as: \n1) Admin \n2) Organizer \n3) Attendee");
        int choice = scanner.nextInt();
        scanner.nextLine();
        Date dob = null;
        String username = null;
        switch(choice) {
            case 1:
                System.out.print("Username: ");
                username = scanner.nextLine();
                for (Admin a : Database.getAdmins()) {
                    if (a.getUsername().equals(username)) {
                        System.out.println("Username already taken. Please try again.");
                        register();
                        return;
                    }
                    break;
                }


            case 2:
                System.out.print("Username: ");
                username = scanner.nextLine();
                for (Organizer o : Database.getOrganizers()) {
                    if (o.getUsername().equals(username)) {
                        System.out.println("Username already taken. Please try again.");
                        register();
                        return;
                    }
                }
                break;
            case 3:
                System.out.print("Username: ");
                username = scanner.nextLine();
                for (Attendee at : Database.getAttendees())
                {
                    if (at.getUsername().equals(username)) {
                        System.out.println("Username already taken. Please try again.");
                        register();
                        return;
                    }
                }
                break;
            default:
                System.out.println("invalid choice");
                register();
        }

        System.out.print("Password: ");
        String password = scanner.nextLine();
        System.out.print("Date of Birth (yyyy-mm-dd): ");
        try{
            dob = java.sql.Date.valueOf(scanner.nextLine());
        }
        catch (Exception e) {
            System.out.println("Invalid date format. Please try again.");
            register();
            return;
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
                System.out.println(" Admin registered.");
                mainDashboard();

                break;
            case 2:
                Organizer organizer = new Organizer(username, password, dob);
                Database.getOrganizers().add(organizer);
                System.out.println(" Organizer registered.");
                mainDashboard();

                break;
            case 3:
                System.out.print("Initial Wallet Balance: ");
                double balance = scanner.nextDouble();
                scanner.nextLine();
                System.out.print("Address: ");
                String address = scanner.nextLine();
                System.out.print("Gender (MALE/FEMALE): ");
                Gender gender = Gender.valueOf(scanner.nextLine().trim().toUpperCase());
                System.out.print("Interests: ");
                String interests = scanner.next();
                Attendee attendee = new Attendee(username, password, dob, balance, address, gender, interests);
                Database.getAttendees().add(attendee);
                System.out.println(" Attendee registered.");
                mainDashboard();

                break;
            default:
                System.out.println(" Invalid user type.");
                register();
                break;
        }
        scanner.close();
    }

    public boolean loginCheck(String inputUsername, String inputPassword) {
        return this.username.equals(inputUsername) && this.password.equals(inputPassword);
    }

    public static void login() {
        System.out.println("Login as: \n1) Admin \n2) Organizer \n3) Attendee");
        int choice = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Username: ");
        String username = scanner.next().trim();
        System.out.print("Password: ");
        String password = scanner.next().trim();


        boolean found = false;

        switch (choice) {
            case 1 :
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
    public void logout(){
        System.out.println("Logging out....");
        mainDashboard();
    }

}
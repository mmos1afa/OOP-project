import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Attendee extends User{
    private Gender gender;
    private double balance;
    private String address;
    Wallet wallet;
    private List<Event>registeredevents;
    private String interests;


    public Attendee(String username, String password, Date dateOfBirth, double intialBalance, String address, Gender gender, String interests) {
        super(username, password, dateOfBirth);
        this.gender = gender;
        this.balance= intialBalance;
        this.address=address;
        this.wallet=new Wallet(intialBalance);
        this.interests=interests;
        this.registeredevents = new ArrayList<>();
    }
    Scanner input= new Scanner(System.in);

    void attendeeMenu(){
        System.out.println("\n---Welcome to Attendee Menu---");
        System.out.println("--Please select an option--");
        System.out.println("1) View Profile\n"+"2) View All Events\n"+"3) Search for events\n"+"4) View My events\n"+"5) Buy Ticket\n"+"6) View Balance\n"+"7) Add balance\n"+"8) Logout\n");

        int x= input.nextInt();
        input.nextLine();

        switch (x){
            case 1:
                viewProfile();
                attendeeMenu();
                break;
            case 2:
                viewAllEvents();
                attendeeMenu();
                break;
            case 3:
                searchEvents();
                attendeeMenu();
                break;
            case 4:
                viewMyEvents();
                attendeeMenu();
                break;
            case 5:
                registerEvent();
                attendeeMenu();
                break;
            case 6:
                viewBalance();
                attendeeMenu();
                break;
            case 7:
                AddBalance();
                attendeeMenu();
                break;
            case 8:
                logout();
                break;

        }
    }
    public List<Event> getRegisteredEvents(){
        return registeredevents;
    }

    public void viewMyEvents(){
        for(Attendee a : Database.getAttendees()){
            if(a.getRegisteredEvents().isEmpty()){
                System.out.println("\nNo Events Registered!");
            }else {
                System.out.println("\n---Your Registered Events---");
                a.getRegisteredEvents().forEach(System.out::println);
            }
        }
    }
    public void registerEvent() {
        if (Database.getEvents().isEmpty()) {
            System.out.println("\nNo events available at the moment!");
        }
        else {
            System.out.println("\nEnter the name of event you'd like to register for:");
            Database.getEvents().forEach(System.out::println);

            String choice = input.next();
            boolean found = false;
            for (Event e : Database.getEvents()) {
                if (e.getTitle().equalsIgnoreCase(choice)) {
                    if (wallet.getBalance() < e.getPrice()) {
                        System.out.println("Insufficient funds to register for this event.");
                    } else {
                        double newBalance = wallet.withDraw(e.getPrice());
                        e.addAttendee(this);
                        System.out.println("Remaining Balance: $" + newBalance);
                        e.getOrganizer().getWallet().deposit(e.getPrice());
                        this.getRegisteredEvents().add(e);
                    }
                    found = true;
                }
            }
            if (!found) {
                System.out.println("Invalid event name entered.");
            }
        }
    }


    public void viewAllEvents(){
        System.out.println("\n---All Events---");
        Database.getEvents().forEach(System.out::println);
    }


    public void viewProfile() {
        System.out.println("\n---Profile Details---");
        for (Attendee a1 : Database.getAttendees()) {
            if (a1.username.equals(this.username)) {
                System.out.println("User Name: "+this.username + " " + " Password: "+ this.password + " " + "Date of Birth : "+ this.dateOfBirth + " " + "Gender: "+ this.gender+" "+"Registered events:"+this.getRegisteredEvents());
            }
        }

    }

    public void viewBalance(){
        System.out.println( "\nThe balance in your wallet is $" +wallet.getBalance());
    }

    public void AddBalance(){
        System.out.println("\nAmount Deposited:");
        double amount=input.nextInt();
        System.out.println( "Total Balance after the Deposit " + wallet.deposit(amount));
    }
    @Override
    public String toString() {
        return "Username: " + username + "  ,Date of Birth: " + dateOfBirth + "  ,Gender: " + gender + "  ,Balance: " + balance + "  ,Address: " + address + "  ,Interests: " + interests;
    }

    public void searchEvents(){
        System.out.println("Please select an option: ");
        System.out.println("1: Search By Name");
        System.out.println("2: Search By Category");
        System.out.println("3: Return to Admin Menu");
        Scanner sc = new Scanner(System.in);
        Scanner x = new Scanner(System.in);
        int choice = sc.nextInt();
        switch(choice){
            case 1:
                System.out.println("Enter Event Name:");
                String name = x.nextLine();
                Database.getEvents().stream().filter(e -> e.getEventName().equalsIgnoreCase(name)).forEach(System.out::println);
                break;
            case 2:
                System.out.println("Enter Event Category:");
                String category = x.nextLine();
                Database.getEvents().stream().filter(e -> e.getCategoryName().equalsIgnoreCase(category)).forEach(System.out::println);
                break;
            case 3:
                attendeeMenu();
                break;
        }
    }
}


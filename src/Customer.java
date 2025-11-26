public class Customer {
    private String name;
    private String email;
    private String phoneNumber;
    private static  int customerCount = 0;
    public  Customer(String name,String email, String phoneNumber){
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        Customer.customerCount ++;
    }

    public  String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public static int getCustomerCount() {
        return customerCount;
    }

}

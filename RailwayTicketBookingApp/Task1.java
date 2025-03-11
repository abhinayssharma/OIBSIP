package  RailwayTicketBookingApp;

import java.sql.*;
import java.util.Random;
import java.util.Scanner;

public class Task1 {

        public static final int min = 1000;
        public static final int max = 9999;

        public static class user {
            public String username;
            public String password;

            Scanner sc = new Scanner(System.in);

            public user() {
            }

            public String getUsername() {
                System.out.println("Enter Username: ");
                username = sc.nextLine();
                return username;
            }

            public String getPassword() {
                System.out.println("Enter Password: ");
                password = sc.nextLine();
                return password;
            }
        }
            public static class PnrRecord{
                private int pnrNumber;
                private String passengerName;
                private String trainNumber;
                private String classType;
                private String journeyDate;
                private String from;
                private String to;

                Scanner sc = new Scanner(System.in);

                public int getPnrNumber() {
                    Random random = new Random();
                    pnrNumber = random.nextInt(max) + min;
                    return pnrNumber;
                }

                public String getPassengerName() {
                    System.out.println("Enter the passenger Name: ");
                    passengerName = sc.nextLine();
                    return passengerName;
                }

                public String getTrainNumber() {
                    System.out.println("Enter the train Number: ");
                    trainNumber = sc.nextLine();
                    return trainNumber;
                }

                public String getClassType() {
                    System.out.println("Enter the class Type: ");
                    classType = sc.nextLine();
                    return classType;
                }
                public String getJourneyDate() {
                    System.out.println("Enter the journey Date as 'YYYY-MM-DD' format: ");
                    journeyDate = sc.nextLine();
                    return journeyDate;
                }
                public String getFrom() {
                    System.out.println("Enter the starting place: ");
                    from = sc.nextLine();
                    return from;
                }
                public String getTo() {
                    System.out.println("Enter the destination place: ");
                    to = sc.nextLine();
                    return to;
                }
            }

            public static void main(String[] args) {

                Scanner sc = new Scanner(System.in);

                user u1 = new user();
                String userName = u1.getUsername();
                String password = u1.getPassword();

                String url = "jdbc:mysql://localhost:3307/railwayApp";


                try {
                    Class.forName("com.mysql.cj.jdbc.Driver");

                    try (Connection connection = DriverManager.getConnection(url, userName, password)){
                        System.out.println("User Connection Granted.\n");

                        while (true) {
                            String insertQuery = "Insert into reservations values (?,?,?,?,?,?,?)";
                            String deleteQuery = "Delete FROM reservations WHERE pnr_number = ?";
                            String showQuery = "Select * from reservations";

                            System.out.println("\nEnter the choice:");
                            System.out.println("1. Insert Record");
                            System.out.println("2. Delete Record");
                            System.out.println("3. Show all Record");
                            System.out.println("4. Exit");

                            System.out.print("Command: ");
                            int choice = sc.nextInt();

                            if (choice == 1) {
                                PnrRecord pnrRecord = new PnrRecord();
                                int pnr_Number = pnrRecord.getPnrNumber();
                                String passengerName = pnrRecord.getPassengerName();
                                String trainNumber = pnrRecord.getTrainNumber();
                                String classType = pnrRecord.getClassType();
                                String journeyDate = pnrRecord.getJourneyDate();
                                String getFrom = pnrRecord.getFrom();
                                String getTo = pnrRecord.getTo();

                                try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                                    preparedStatement.setInt(1, pnr_Number);
                                    preparedStatement.setString(2, passengerName);
                                    preparedStatement.setString(3, trainNumber);
                                    preparedStatement.setString(4, classType);
                                    preparedStatement.setString(5, journeyDate);
                                    preparedStatement.setString(6, getFrom);
                                    preparedStatement.setString(7, getTo);

                                    int rowsAffected = preparedStatement.executeUpdate();
                                    if (rowsAffected > 0) {
                                        System.out.println("Record added successfully.");
                                    } else {
                                        System.out.println("No record were added.");
                                    }
                                }catch (SQLException e) {
                                    System.err.println("SQLException: " + e.getMessage()); // ✅ Correct (prints error)
                                }


                            } else if (choice == 2) {
                                System.out.println("Enter the PNR Number to delete the record");
                                int pnr_Number = sc.nextInt();

                                try (PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {
                                    preparedStatement.setInt(1, pnr_Number);

                                    int rowsAffected = preparedStatement.executeUpdate();

                                    if (rowsAffected > 0) {
                                        System.out.println("Record deleted Successfully");
                                    } else {
                                        System.out.println("No record were deleted");
                                    }
                                }catch (SQLException e) {
                                    System.err.println("SQLException: " + e.getMessage()); // ✅ Correct (prints error)
                                }

                            } else if (choice == 3) {
                                try (PreparedStatement preparedStatement = connection.prepareStatement(showQuery)) {
                                    ResultSet resultSet = preparedStatement.executeQuery();

                                    System.out.println("\nAll Records printing\n");

                                    while (resultSet.next()) {
                                        String pnrNumber = resultSet.getString("pnr_number");
                                        String passengerName = resultSet.getString("passenger_name");
                                        String trainNumber = resultSet.getString("train_number");
                                        String classType = resultSet.getString("class_type");
                                        String journeyDate = resultSet.getString("journey_date");
                                        String fromLocation = resultSet.getString("from_location");
                                        String toLocation = resultSet.getString("to_location");

                                        System.out.println("PNR Number: " + pnrNumber);
                                        System.out.println("Passenger Number: " + passengerName);
                                        System.out.println("Train Number: " + trainNumber);
                                        System.out.println("Class type: " + classType);
                                        System.out.println("Journey Date: " + journeyDate);
                                        System.out.println("From Location: " + fromLocation);
                                        System.out.println("To Location: " + toLocation);
                                    }
                                }catch (SQLException e) {
                                    System.err.println("SQLException: " + e.getMessage()); // ✅ Correct (prints error)
                                }

                            } else if (choice == 4) {
                                System.out.println("Exiting the program.\n");
                                break;
                            } else {
                                System.out.println("Invalid choice Entered.\n");
                            }
                        }

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                catch (ClassNotFoundException e){
                    System.err.println("Error loading JDBC Driver: " +e.getMessage());
                }
                sc.close();
            }
        }

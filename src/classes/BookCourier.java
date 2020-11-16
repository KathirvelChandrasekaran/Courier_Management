package classes;

import services.DBConnection;
import services.Mailer;

import java.sql.*;

public class BookCourier implements interfaces.BookCourier {
    int userID;

    public BookCourier() {
    }

    public BookCourier(int userID) {
        this.userID = userID;
    }

    public Double calculateBookingCharge(Double parcelWeight) {
        Double bookingCharges;
        if (parcelWeight < 20)
            bookingCharges = 40.0;
        else if (parcelWeight > 20 && parcelWeight <= 50) {
            parcelWeight -= 20;
            bookingCharges = 40.0 + (0.75 * parcelWeight);
        } else if (parcelWeight > 50 && parcelWeight <= 80) {
            parcelWeight -= 20;
            bookingCharges = 40.0 + (1.15 * parcelWeight);
        } else {
            parcelWeight -= 20;
            bookingCharges = 40.0 + (1.50 * parcelWeight);
        }
        return bookingCharges;
    }

    @Override
    public void bookCourier(CourierDetails courierDetails, int userID) {
        try {
            String query = "insert into courier.bookingDetails values (default , ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            Connection connection = DBConnection.db_connection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, userID);
            preparedStatement.setDouble(2, courierDetails.parcelWeight);
            preparedStatement.setString(3, courierDetails.toAddress);
            preparedStatement.setString(4, courierDetails.fromAddress);
            preparedStatement.setString(5, courierDetails.senderName);
            preparedStatement.setString(6, courierDetails.receiverName);
            preparedStatement.setString(7, courierDetails.bookingDate);
            preparedStatement.setDouble(8, calculateBookingCharge(courierDetails.parcelWeight));
            preparedStatement.setString(9, courierDetails.senderEmail);
            preparedStatement.setString(10, "Pending");
            preparedStatement.execute();
            System.out.println("Parcel has been booked!!!");
            Mailer mailer = new Mailer();
            mailer.sendInvoicwMail(courierDetails.senderEmail, userID, courierDetails.parcelWeight,
                    courierDetails.toAddress, courierDetails.fromAddress, courierDetails.senderName,
                    courierDetails.receiverName, courierDetails.bookingDate);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void bookingHistory(int userID) {
        try {
            Connection connection = DBConnection.db_connection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement
                    .executeQuery("Select * from courier.bookingDetails where user_id= '" + userID + "'");
            System.out.println("Looking for history...");
            System.out.println(" Parcel ID | User ID | Parcel Weight | To Address | From Address "
                    + "| Sender's Name | Receiver's Name | Booking Date |"
                    + " Booking Charges | Sender's Email | Status");
            System.out.println(" ---------- | --------| --------- | --------- | -------------- |"
                    + " -------------- | -------------- | -------------------- | --------- | --------- ");
            while (resultSet.next()) {
                System.out.println(" " + resultSet.getInt(1) + " | " + resultSet.getInt(2) + " | "
                        + resultSet.getDouble(3) + " | " + resultSet.getString(4) + " | " + resultSet.getString(5)
                        + " | " + resultSet.getString(6) + " | " + resultSet.getString(7) + " | "
                        + resultSet.getString(8) + " | " + resultSet.getDouble(9) + " | " + resultSet.getString(10)
                        + " | " + resultSet.getString(11));
            }
            System.out.println();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

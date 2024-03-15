package Controller;

import javafx.fxml.FXML;

import javafx.scene.control.Button;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import ConnectDB.AdminManage;
import application.Feedback;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TableColumn;

public class FormFeedbackController {
	AdminManage admin_manage = new AdminManage();
    @FXML
    private TableColumn<Feedback, String> Column_Content;

    @FXML
    private TableColumn<Feedback, String> Column_Email;

    @FXML
    private TableColumn<Feedback, String> Column_ID;

    @FXML
    private Button btnRespone;

    @FXML
    private Button btnShowFeedback;

    @FXML
    private TableView tbShowFeedback;

    @FXML
    void btnClicked_Respone(ActionEvent event) {
    	List<String> list = new ArrayList<>();
    	list = admin_manage.getFeedback();
    	for(String value : list) {
    		String email = value;
            String randomString = generateRandomString(8);
            String password = randomString;
            sendPassUseGmail(email, password);
            admin_manage.Update_Password(email, password);
            admin_manage.DeleteFeedback(email);
    	}
    }
    
    @FXML
    void btnClicked_ShowFeed(ActionEvent event) {
	    ObservableList<Feedback> ListFeedback = FXCollections.observableArrayList();
    	List<String> list = new ArrayList<>();
    	list = admin_manage.getFeedback();
    	for(String value : list) {
    		String email = value;
    		String id_user = admin_manage.getID_By_Email(email);
    		String content = "Change Password";
    		Feedback feedback = new Feedback(id_user, content, email);
    		ListFeedback.add(feedback);
    	}
    	
	    // Set data to TableView
    	tbShowFeedback.setItems(ListFeedback);

	    // Set PropertyValueFactory
	    Column_ID.setCellValueFactory(new PropertyValueFactory<>("id_user"));
	    Column_Email.setCellValueFactory(new PropertyValueFactory<>("email"));
	    Column_Content.setCellValueFactory(new PropertyValueFactory<>("content"));
    }
    
    public void sendPassUseGmail(String to, String  password_user) {
        String from = "102210208@sv1.dut.udn.vn";
        String host = "smtp-mail.outlook.com"; // Máy chủ SMTP của Outlook

        // Cấu hình thông tin đăng nhập
        final String username = "102210208@sv1.dut.udn.vn";
        final String password = "67722003@@nth"; // Mật khẩu ứng dụng hoặc mật khẩu tài khoản

        // Thiết lập cấu hình properties
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "587");

        // Tạo phiên làm việc
        Session session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            // Tạo đối tượng MimeMessage
            MimeMessage message = new MimeMessage(session);

            // Đặt thông tin người gửi và người nhận
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            // Đặt chủ đề và nội dung
            message.setSubject("Login password");
            message.setText(password_user);

            // Gửi email
            Transport.send(message);

            System.out.println("Email sent successfully...");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }
    
    private static String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@#&";
        List<Character> availableCharacters = new ArrayList<>();
        for (char c : characters.toCharArray()) {
            availableCharacters.add(c);
        }

        StringBuilder randomString = new StringBuilder();

        SecureRandom secureRandom = new SecureRandom();
        for (int i = 0; i < length; i++) {
            int randomIndex = secureRandom.nextInt(availableCharacters.size());
            char randomChar = availableCharacters.remove(randomIndex);
            randomString.append(randomChar);
        }

        return randomString.toString();
    }
}

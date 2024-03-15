package Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.File;
import java.io.FileInputStream;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.JFileChooser;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import ConnectDB.AdminManage;
import application.DataUser;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import javafx.scene.control.TableView;

public class FormAddUserController {
	AdminManage admin_manage = new AdminManage();
    @FXML
    private TableColumn<DataUser, String> Column_Email;
    @FXML
    private TableColumn<DataUser, String> Column_Gender;
    @FXML
    private TableColumn<DataUser, String> Column_ID;
    @FXML
    private TableColumn<DataUser, String> Column_Password;
    @FXML
    private TableColumn<DataUser, String> Column_PhoneNumber;
    @FXML
    private TableColumn<DataUser, String> Column_Role;
	@FXML
	private TableView tbShowInfoUser;
	@FXML
	private Button btnSelectFile;
	@FXML
	private Button btnAddUser;
	@FXML
	private Button btnShow;
	@FXML
	private TextField txtShowDirectionFile;

	// Event Listener on Button[#btnSelectFile].onAction
	@FXML
	public void btnClicked_SelectFile(ActionEvent event) {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(null);
        
        if (result == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getAbsolutePath();
            txtShowDirectionFile.setText(filePath);
        }
	}
	// Event Listener on Button[#btnAddUser].onAction
	@FXML
	public void btnClicked_AddUser(ActionEvent event) {
	    String id_user, email, password, phone_number, gender, role, name_user;
	    boolean bit;

	    String filePath = txtShowDirectionFile.getText();
	    try (FileInputStream file = new FileInputStream(new File(filePath));
	         Workbook workbook = new XSSFWorkbook(file)) {

	        DataFormatter dataFormatter = new DataFormatter();
	        Iterator<Sheet> sheets = workbook.sheetIterator();
	        while (sheets.hasNext()) {
	            Sheet sh = sheets.next();
	            System.out.println("Sheet name is " + sh.getSheetName());
	            System.out.println("---------");

	            boolean isFirstRow = true;

	            Iterator<Row> iterator = sh.iterator();
	            while (iterator.hasNext()) {
	                Row row = iterator.next();
	                Iterator<Cell> cellIterator = row.iterator();
	                String data = ""; // Reset data for each row
	                while (cellIterator.hasNext()) {
	                    Cell cell = cellIterator.next();
	                    String cellValue = dataFormatter.formatCellValue(cell);
	                    // Bỏ qua in tên cột nếu là dòng đầu tiên
	                    if (!isFirstRow) {
	                        data += cellValue + " ";
	                        System.out.print(cellValue + "\t");
	                    }
	                }
	                System.out.println();

	                if (!isFirstRow) {
	                    String[] value = data.split(" ");
	                    if(value[6].equals("SinhVien")) {
		                    id_user = value[0] + value[1] + value[2];
		                    email = value[3];
		                    String randomString = generateRandomString(8);
		                    password = randomString;
		                    phone_number = value[4];
		                    gender = value[5];
		                    role = value[6];
		                    
		            		StringBuilder fullNameBuilder = new StringBuilder();
		            		for (int i = 7; i < value.length; i++) {
		            		    fullNameBuilder.append(value[i]).append(" ");
		            		}
		            		name_user = fullNameBuilder.toString().trim();
		            		
		                    if(gender.equals("Nam")) bit = true;
		                    else bit = false;
	                    } else {
		                    id_user = value[0] + value[1];
		                    email = value[2];
		                    String randomString = generateRandomString(8);
		                    password = randomString;
		                    phone_number = value[3];
		                    gender = value[4];
		                    role = value[5];
		                    
		            		StringBuilder fullNameBuilder = new StringBuilder();
		            		for (int i = 6; i < value.length; i++) {
		            		    fullNameBuilder.append(value[i]).append(" ");
		            		}
		            		name_user = fullNameBuilder.toString().trim();
		            		
		                    if(gender.equals("Nam")) bit = true;
		                    else bit = false;
	                    }
	                    admin_manage.InsertUserAndAccount(id_user, email, password, phone_number, bit, role, name_user);
	                    sendPassUseGmail(email, password);
	                }
	                isFirstRow = false;
	            }
	        }
			Platform.runLater(() -> {
				Alert _alert = new Alert(Alert.AlertType.INFORMATION);
				_alert.setTitle("Thông báo");
				_alert.setHeaderText(null);
				_alert.setContentText("Thêm tài khoản thành công");
				_alert.showAndWait(); 
			});
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	@FXML
	void btnClicked_Show(ActionEvent event) {
	    ObservableList<DataUser> ListUser = FXCollections.observableArrayList();
	    String id_user, email, password, phone_number, gender, role, name_user;

	    String filePath = txtShowDirectionFile.getText();
	    try (FileInputStream file = new FileInputStream(new File(filePath));
	         Workbook workbook = new XSSFWorkbook(file)) {

	        DataFormatter dataFormatter = new DataFormatter();
	        Iterator<Sheet> sheets = workbook.sheetIterator();
	        while (sheets.hasNext()) {
	            Sheet sh = sheets.next();
	            System.out.println("Sheet name is " + sh.getSheetName());
	            System.out.println("---------");

	            boolean isFirstRow = true;

	            Iterator<Row> iterator = sh.iterator();
	            while (iterator.hasNext()) {
	                Row row = iterator.next();
	                Iterator<Cell> cellIterator = row.iterator();
	                String data = ""; // Reset data for each row
	                while (cellIterator.hasNext()) {
	                    Cell cell = cellIterator.next();
	                    String cellValue = dataFormatter.formatCellValue(cell);
	                    // Bỏ qua in tên cột nếu là dòng đầu tiên
	                    if (!isFirstRow) {
	                        data += cellValue + " ";
	                        System.out.print(cellValue + "\t");
	                    }
	                }
	                System.out.println();

	                if (!isFirstRow) {
	                    String[] value = data.split(" ");
	                    if(value[6].equals("SinhVien")) {
		                    id_user = value[0] + value[1] + value[2];
		                    email = value[3];
		                    String randomString = generateRandomString(8);
		                    password = randomString;
		                    phone_number = value[4];
		                    gender = value[5];
		                    role = value[6];
		                    
		            		StringBuilder fullNameBuilder = new StringBuilder();
		            		for (int i = 7; i < value.length; i++) {
		            		    fullNameBuilder.append(value[i]).append(" ");
		            		}
		            		name_user = fullNameBuilder.toString().trim();
	                    } else {
		                    id_user = value[0] + value[1];
		                    email = value[2];
		                    String randomString = generateRandomString(8);
		                    password = randomString;
		                    phone_number = value[3];
		                    gender = value[4];
		                    role = value[5];
		                    
		            		StringBuilder fullNameBuilder = new StringBuilder();
		            		for (int i = 6; i < value.length; i++) {
		            		    fullNameBuilder.append(value[i]).append(" ");
		            		}
		            		name_user = fullNameBuilder.toString().trim();
	                    }
	            		
	                    DataUser user = new DataUser(id_user, email, password, phone_number, gender, role);
	                    ListUser.add(user);
	                }
	                isFirstRow = false;
	            }
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    // Set data to TableView
	    tbShowInfoUser.setItems(ListUser);

	    // Set PropertyValueFactory
	    Column_ID.setCellValueFactory(new PropertyValueFactory<>("id_user"));
	    Column_Email.setCellValueFactory(new PropertyValueFactory<>("email"));
	    Column_Password.setCellValueFactory(new PropertyValueFactory<>("password"));
	    Column_PhoneNumber.setCellValueFactory(new PropertyValueFactory<>("phone_number"));
	    Column_Gender.setCellValueFactory(new PropertyValueFactory<>("gender"));
	    Column_Role.setCellValueFactory(new PropertyValueFactory<>("role"));
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
}

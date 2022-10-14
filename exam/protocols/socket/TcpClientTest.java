import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.Socket;
 
 
public class TcpClientTest {
    public static void main(String[] args) {
        try {
            String serverIP = "127.0.0.1"; // 127.0.0.1 & localhost ����
            System.out.println("������ �������Դϴ�. ���� IP : " + serverIP);
             
            // ������ �����Ͽ� ������ ��û�Ѵ�.
            Socket socket = new Socket(serverIP, 5000);
             
            // ������ �Է½�Ʈ���� ��´�.
            InputStream in = socket.getInputStream();
            DataInputStream dis = new DataInputStream(in);  // �⺻�� ������ ó���ϴ� ������Ʈ��
             
            // �������� ���� ���� �����͸� ����Ѵ�.
            System.out.println("�����κ��� ���� �޼��� : " + dis.readUTF());
            System.out.println("������ �����մϴ�.");
             
            // ��Ʈ���� ������ �ݴ´�.
            dis.close();
            socket.close();
        } catch (ConnectException ce) {
            ce.printStackTrace();
        } catch (IOException ie) {
            ie.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } // try - catch
    } // main
}

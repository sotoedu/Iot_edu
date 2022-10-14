import java.net.*;
import java.io.*;
// FtpClient Ŭ����
// CtrlListen Ŭ����
public class FtpClient {
    private static String serverAddr = "166.104.115.201";
    
    // ������ �غ�
    Socket ctrlSocket;//    ����� ����
    public PrintWriter ctrlOutput;//  ���� ��¿� ��Ʈ��
    public BufferedReader ctrlInput;//  ���� �Է¿� ��Ʈ��
    final int CTRLPORT = 21 ;//   ftp ����� ��Ʈ
    // openConnection  �޼ҵ�
    // �ּҿ� ��Ʈ ��ȣ�κ��� ������ ����� ����� ��Ʈ���� �ۼ��Ѵ�.
    public void openConnection(String host)
        throws IOException,UnknownHostException
    {
        ctrlSocket = new Socket(host, CTRLPORT);
        ctrlOutput = new PrintWriter(ctrlSocket.getOutputStream());
        ctrlInput 
          = new BufferedReader(new InputStreamReader(ctrlSocket.getInputStream()));
    }
    // closeConnection  �޼ҵ�
    // ����� ������ �ݴ´�.
    public void closeConnection()
        throws IOException
    {
        ctrlSocket.close() ;
    }

    // showMenu �޼ҵ�
    // Ftp�� ��� �޴��� ����Ѵ�.
    public void showMenu()
    {
        System.out.println(">Command?") ;
        System.out.print("2 ls") ;
        System.out.print("    3 cd") ;
        System.out.print("    4 get") ;
        System.out.print("    5 put") ;
        System.out.print("    6 ascii") ;
        System.out.print("    7 binary") ;
        System.out.println("    9 quit") ;
    }
    // getCommand �޼ҵ�
    // �̿��ڰ� ������ ��� ��ȣ�� �о� ó���Ѵ�.
    public String getCommand()
    {
       String buf = "" ;
       BufferedReader lineread
         = new BufferedReader(new InputStreamReader(System.in)) ;
        
       while(buf.length() != 1){// 1 ������ �Է��� ���� ������ �ݺ��Ѵ�.
         try{
           buf = lineread.readLine() ;
         }catch(Exception e)
         { 
          e.printStackTrace();
          System.exit(1);
         }
       }
       return (buf) ;
     }
    // doLogin �޼ҵ�
    // ftp ������ �α�-�� �Ѵ�.
    public void doLogin()
    {
       String loginName = "" ;
       String password = "" ;
       BufferedReader lineread
          = new BufferedReader(new InputStreamReader(System.in)) ;
       try{
            System.out.println("�α��� �̸��� �Է��ϼ��� : ") ;
            loginName = lineread.readLine() ;
            // USER ��ɿ� ���� �α���
            ctrlOutput.println("USER " + loginName) ;
            ctrlOutput.flush() ;
            // PASS ��ɿ� ���� �н������� �Է�
            System.out.println("�н����带 �Է��ϼ��� : ") ;
            password = lineread.readLine() ;
            ctrlOutput.println("PASS " + password) ;
            ctrlOutput.flush() ;
       }catch(Exception e)
       {
            e.printStackTrace();
            System.exit(1);
       }
    }
    // doQuit �޼ҵ�
    // ftp �����κ��� �α� �ƿ��Ѵ�
    public void doQuit()
    {
       try{
            ctrlOutput.println("QUIT ") ;//  QUIT ����� �۽�
            ctrlOutput.flush() ;
       }catch(Exception e)
       {
            e.printStackTrace();
            System.exit(1);
       }
    }
    // doCd  �޼ҵ�
    // ���丮�� �����Ѵ�.
    public void doCd()
    {
       String dirName = "" ;
       BufferedReader lineread
          = new BufferedReader(new InputStreamReader(System.in)) ;
       try{
            System.out.println("���丮 �̸��� �Է��ϼ��� : ") ;
            dirName = lineread.readLine() ;
            ctrlOutput.println("CWD " + dirName) ;// CWD ���
            ctrlOutput.flush() ;
       }catch(Exception e)
       {
            e.printStackTrace();
            System.exit(1);
       }
    }
    // doLs �޼ҵ�
    // ���丮 ������ ��´�.
    public void doLs()
    {
       try{
            int n ;
            byte[] buff = new byte[1024] ;
            // �����Ϳ� ����(connection)�� �����.
            Socket dataSocket = dataConnection("LIST") ;
            //  �����͸� �о� ó���ϴ� ��Ʈ���� ����Ѵ�.
            BufferedInputStream dataInput
              = new BufferedInputStream(dataSocket.getInputStream()) ;
            //  ���丮 ������ �а� ó���Ѵ�.
            while((n = dataInput.read(buff)) > 0){
                System.out.write(buff,0,n)  ;
            }
            dataSocket.close() ;
       }catch(Exception e)
       {
            e.printStackTrace();
            System.exit(1);
       }
    }
    // dataConnection �޼ҵ�
    // �������� ������ ��ȯ�� ������ �����.
    // ����, �������� port ������� ��Ʈ�� �˸���.
    public Socket dataConnection(String ctrlcmd)
    {
       String cmd = "PORT " ; //PORT ������� �۽��� ������ ���� ����
       int i ;
       Socket dataSocket = null ;//  ������ ���ۿ� ����
       try{
             //  �ڽ��� �ּҸ� ��´�.
             byte[] address = InetAddress.getLocalHost().getAddress() ;
             //  ������ ��Ʈ ��ȣ�� ���� ������ �����.
             ServerSocket serverDataSocket = new ServerSocket(0,1) ;
             // PORT ��ɿ��� �۽� �����͸� �̿��Ѵ�.
             for(i = 0; i < 4; ++i)
                 cmd = cmd +  (address[i] & 0xff) + "," ;
             cmd = cmd + (((serverDataSocket.getLocalPort()) / 256) & 0xff)
                       + ","
                       + (serverDataSocket.getLocalPort() & 0xff) ;
             // PORT ����� ����� ��Ʈ���� ���� �����Ѵ�.
             ctrlOutput.println(cmd) ;
             ctrlOutput.flush() ;
             // ó�� ��� ��� (LIST, RETR, STOR)�� ������ ������.
             ctrlOutput.println(ctrlcmd) ;
             ctrlOutput.flush() ;
             // �����κ��� ������ �޴´�.
             dataSocket = serverDataSocket.accept() ;
             serverDataSocket.close() ;
       }catch(Exception e)
       {
            e.printStackTrace();
            System.exit(1);
       }
       return  dataSocket ;
    }
    // doAscii �޼ҵ�
    // �ؽ�Ʈ ���� ���� �����Ѵ�.
    public void doAscii()
    {
       try{
            ctrlOutput.println("TYPE A") ;// A ���
            ctrlOutput.flush() ;
       }catch(Exception e)
       {
            e.printStackTrace();
            System.exit(1);
       }
    }
    // doBinary �޼ҵ�
    // ���� ���� ���� �����Ѵ�.
    public void doBinary()
    {
       try{
            ctrlOutput.println("TYPE I") ;// I ���
            ctrlOutput.flush() ;
       }catch(Exception e)
       {
            e.printStackTrace();
            System.exit(1);
       }
    }
    // doGet �޼ҵ�
    // �������� ������ �����´�.
    public void doGet()
    {
       String fileName = "" ;
       BufferedReader lineread
          = new BufferedReader(new InputStreamReader(System.in)) ;
       try{
            int n ;
            byte[] buff = new byte[1024] ;
            // �������� ������ �̸��� �����Ѵ�.
            System.out.println("���� �̸��� �Է��ϼ��� : ") ;
            fileName = lineread.readLine() ;
            // Ŭ���̾�Ʈ�� ���ſ� ������ �غ��Ѵ�.
            FileOutputStream outfile =  new FileOutputStream(fileName) ;
            // ���� ���ۿ� ������ ��Ʈ���� �ۼ��Ѵ�.
            Socket dataSocket = dataConnection("RETR " + fileName) ;
            BufferedInputStream dataInput
              = new BufferedInputStream(dataSocket.getInputStream()) ;
            // �����κ��� �����͸� �޾� ���Ϸ� �����Ѵ�.
            while((n = dataInput.read(buff)) > 0){
                outfile.write(buff,0,n) ;
            }
            dataSocket.close() ;
            outfile.close() ;
       }catch(Exception e)
       {
            e.printStackTrace();
            System.exit(1);
       }
    }
    // doPut �޼ҵ�
    // ������ ������ �����Ѵ�.
    public void doPut()
    {
       String fileName = "" ;
       BufferedReader lineread
          = new BufferedReader(new InputStreamReader(System.in)) ;
       try{
            int n ;
            byte[] buff = new byte[1024] ;
            FileInputStream sendfile = null ;
            // ���� �̸��� �����Ѵ�.
            System.out.println("���ϸ��� �Է��ϼ��� : ") ;
            fileName = lineread.readLine() ;
            // Ŭ���̾�Ʈ���� ������ �о� ���� �غ� �Ѵ�.
            try{
                sendfile =  new FileInputStream(fileName) ;
            }catch(Exception e){
                System.out.println("&#44594;?&#44541;&#44625;&#44437;&#44423;&#44511;&#44495;&#44455;&#44522;") ;
                return ;
            }
            // ���ۿ� ������ ��Ʈ���� ����Ѵ�.
            Socket dataSocket = dataConnection("STOR " + fileName) ;
            OutputStream outstr =  dataSocket.getOutputStream() ;
            // ������ �о� ��Ʈ��ũ�� �����Ͽ� ������ ������.
            while((n = sendfile.read(buff)) > 0){
                outstr.write(buff,0,n) ;
            }
            dataSocket.close() ;
            sendfile.close() ;
       }catch(Exception e)
       {
            e.printStackTrace();
            System.exit(1);
       }
    }

    // execCommand �޼ҵ�
    // ��ɿ� �����ϴ� �� ó���� ȣ���Ѵ�.
    public boolean execCommand(String command)
    {
          boolean cont = true ;
          switch(Integer.parseInt(command)){
          case 2 : //  ������ ���丮 ǥ�� ó��
              doLs() ;
              break ;
          case 3 : //  ������ �۾� ���丮 ���� ǥ��
              doCd() ;
              break ;
          case 4 : //  �����κ����� ���� ��� ó��
              doGet() ;
              break ;
          case 5 : //  ������ ���� ���� ó��
              doPut() ;
              break ;
          case 6 : //  �ؽ�Ʈ ���� ���
              doAscii() ;
              break ;
          case 7 : //  ���̳ʸ� ���� ���
              doBinary() ;
              break ;
          case 9 : //  ó�� ����
              doQuit() ;
              cont = false ;
              break ;
          default : // �� �̿��� �Է� ó��
              System.out.println("�޴��� �����ϼ��� : ") ;
          }
          return(cont) ;
    }
    // main_proc �޼ҵ�
    // Ftp�� ��� �޴��� ����Ͽ� �ش�Ǵ� ó���� ȣ���Ѵ�.
    public void main_proc()
        throws IOException
    {
         boolean cont = true ;
        try {
            //  �α��� ó���� �Ѵ�.
            doLogin() ;
            while(cont){
                //  �޴��� ����Ѵ�.
                showMenu() ;
                //  ����� �޾Ƽ� ó���Ѵ�.
                 cont = execCommand(getCommand()) ;
             }
        }
        catch(Exception e){
            System.err.print(e);
            System.exit(1);
        }
    }
    // getMsgs �޼ҵ�
    // ���� ��Ʈ���� ���� ������ �����Ѵ�.
    public void getMsgs(){
        try {
            ListenThread lt = new ListenThread(ctrlInput) ;
            Thread listenerthread = new Thread(lt) ;
            listenerthread.start() ;
        }catch(Exception e){
            e.printStackTrace() ;
            System.exit(1) ;
        }
    }
    // main �޼ҵ�
    // TCP ����(connection)�� ��� ó���� �����Ѵ�.
    public static void main(String[] arg){
        try {
            FtpClient f = new FtpClient();
            f.openConnection(serverAddr); //  ����� ������ ����
            f.getMsgs() ;             //  ���� ������ ����
            f.main_proc();            // ftp ó��
            f.closeConnection() ;     //  ���� �ݱ�
            System.exit(0) ;          //  ���α׷� ����
        }catch(Exception e){
            e.printStackTrace();
            System.exit(1);
        }
    }
}

class ListenThread extends Thread{
        BufferedReader ctrlInput = null ;
        //  constructor �а� ó���ϱ� ���� ���� ����
        public ListenThread(BufferedReader in){
            ctrlInput = in ;
        }
        public void run(){
            while(true){
                try{ //   
                    System.out.println(ctrlInput.readLine()) ;
                } catch (Exception e){
                    e.printStackTrace() ;
                    System.exit(1) ;
                }
            }
        }
}

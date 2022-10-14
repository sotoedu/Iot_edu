import java.net.*;
import java.io.*;
// FtpClient 클래스
// CtrlListen 클래스
public class FtpClient {
    private static String serverAddr = "166.104.115.201";
    
    // 소켓의 준비
    Socket ctrlSocket;//    제어용 소켓
    public PrintWriter ctrlOutput;//  제어 출력용 스트림
    public BufferedReader ctrlInput;//  제어 입력용 스트림
    final int CTRLPORT = 21 ;//   ftp 제어용 포트
    // openConnection  메소드
    // 주소와 포트 번호로부터 소켓을 만들고 제어용 스트림을 작성한다.
    public void openConnection(String host)
        throws IOException,UnknownHostException
    {
        ctrlSocket = new Socket(host, CTRLPORT);
        ctrlOutput = new PrintWriter(ctrlSocket.getOutputStream());
        ctrlInput 
          = new BufferedReader(new InputStreamReader(ctrlSocket.getInputStream()));
    }
    // closeConnection  메소드
    // 제어용 소켓을 닫는다.
    public void closeConnection()
        throws IOException
    {
        ctrlSocket.close() ;
    }

    // showMenu 메소드
    // Ftp의 명령 메뉴를 출력한다.
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
    // getCommand 메소드
    // 이용자가 지정한 명령 번호를 읽어 처리한다.
    public String getCommand()
    {
       String buf = "" ;
       BufferedReader lineread
         = new BufferedReader(new InputStreamReader(System.in)) ;
        
       while(buf.length() != 1){// 1 문자의 입력을 받을 때까지 반복한다.
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
    // doLogin 메소드
    // ftp 서버에 로그-인 한다.
    public void doLogin()
    {
       String loginName = "" ;
       String password = "" ;
       BufferedReader lineread
          = new BufferedReader(new InputStreamReader(System.in)) ;
       try{
            System.out.println("로그인 이름을 입력하세요 : ") ;
            loginName = lineread.readLine() ;
            // USER 명령에 의한 로그인
            ctrlOutput.println("USER " + loginName) ;
            ctrlOutput.flush() ;
            // PASS 명령에 의한 패스워드의 입력
            System.out.println("패스워드를 입력하세요 : ") ;
            password = lineread.readLine() ;
            ctrlOutput.println("PASS " + password) ;
            ctrlOutput.flush() ;
       }catch(Exception e)
       {
            e.printStackTrace();
            System.exit(1);
       }
    }
    // doQuit 메소드
    // ftp 서버로부터 로그 아웃한다
    public void doQuit()
    {
       try{
            ctrlOutput.println("QUIT ") ;//  QUIT 명령의 송신
            ctrlOutput.flush() ;
       }catch(Exception e)
       {
            e.printStackTrace();
            System.exit(1);
       }
    }
    // doCd  메소드
    // 디렉토리를 변경한다.
    public void doCd()
    {
       String dirName = "" ;
       BufferedReader lineread
          = new BufferedReader(new InputStreamReader(System.in)) ;
       try{
            System.out.println("디렉토리 이름을 입력하세요 : ") ;
            dirName = lineread.readLine() ;
            ctrlOutput.println("CWD " + dirName) ;// CWD 명령
            ctrlOutput.flush() ;
       }catch(Exception e)
       {
            e.printStackTrace();
            System.exit(1);
       }
    }
    // doLs 메소드
    // 디렉토리 정보를 얻는다.
    public void doLs()
    {
       try{
            int n ;
            byte[] buff = new byte[1024] ;
            // 데이터용 연결(connection)을 만든다.
            Socket dataSocket = dataConnection("LIST") ;
            //  데이터를 읽어 처리하는 스트림을 사용한다.
            BufferedInputStream dataInput
              = new BufferedInputStream(dataSocket.getInputStream()) ;
            //  디렉토리 정보를 읽고 처리한다.
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
    // dataConnection 메소드
    // 서버와의 데이터 교환용 소켓을 만든다.
    // 또한, 서버에게 port 명령으로 포트를 알린다.
    public Socket dataConnection(String ctrlcmd)
    {
       String cmd = "PORT " ; //PORT 명령으로 송신할 데이터 저장 변수
       int i ;
       Socket dataSocket = null ;//  데이터 전송용 소켓
       try{
             //  자신의 주소를 얻는다.
             byte[] address = InetAddress.getLocalHost().getAddress() ;
             //  적당한 포트 번호의 서버 소켓을 만든다.
             ServerSocket serverDataSocket = new ServerSocket(0,1) ;
             // PORT 명령용의 송신 데이터를 이용한다.
             for(i = 0; i < 4; ++i)
                 cmd = cmd +  (address[i] & 0xff) + "," ;
             cmd = cmd + (((serverDataSocket.getLocalPort()) / 256) & 0xff)
                       + ","
                       + (serverDataSocket.getLocalPort() & 0xff) ;
             // PORT 명령을 제어용 스트림을 통해 전송한다.
             ctrlOutput.println(cmd) ;
             ctrlOutput.flush() ;
             // 처리 대상 명령 (LIST, RETR, STOR)을 서버로 보낸다.
             ctrlOutput.println(ctrlcmd) ;
             ctrlOutput.flush() ;
             // 서버로부터 접속을 받는다.
             dataSocket = serverDataSocket.accept() ;
             serverDataSocket.close() ;
       }catch(Exception e)
       {
            e.printStackTrace();
            System.exit(1);
       }
       return  dataSocket ;
    }
    // doAscii 메소드
    // 텍스트 전송 모드로 셋팅한다.
    public void doAscii()
    {
       try{
            ctrlOutput.println("TYPE A") ;// A 모드
            ctrlOutput.flush() ;
       }catch(Exception e)
       {
            e.printStackTrace();
            System.exit(1);
       }
    }
    // doBinary 메소드
    // 이진 전송 모드로 셋팅한다.
    public void doBinary()
    {
       try{
            ctrlOutput.println("TYPE I") ;// I 모드
            ctrlOutput.flush() ;
       }catch(Exception e)
       {
            e.printStackTrace();
            System.exit(1);
       }
    }
    // doGet 메소드
    // 서버상의 파일을 가져온다.
    public void doGet()
    {
       String fileName = "" ;
       BufferedReader lineread
          = new BufferedReader(new InputStreamReader(System.in)) ;
       try{
            int n ;
            byte[] buff = new byte[1024] ;
            // 서버상의 파일의 이름을 지정한다.
            System.out.println("파일 이름을 입력하세요 : ") ;
            fileName = lineread.readLine() ;
            // 클라이언트상에 수신용 파일을 준비한다.
            FileOutputStream outfile =  new FileOutputStream(fileName) ;
            // 파일 전송용 데이터 스트림을 작성한다.
            Socket dataSocket = dataConnection("RETR " + fileName) ;
            BufferedInputStream dataInput
              = new BufferedInputStream(dataSocket.getInputStream()) ;
            // 서버로부터 데이터를 받아 파일로 저장한다.
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
    // doPut 메소드
    // 서버에 파일을 전송한다.
    public void doPut()
    {
       String fileName = "" ;
       BufferedReader lineread
          = new BufferedReader(new InputStreamReader(System.in)) ;
       try{
            int n ;
            byte[] buff = new byte[1024] ;
            FileInputStream sendfile = null ;
            // 파일 이름을 지정한다.
            System.out.println("파일명을 입력하세요 : ") ;
            fileName = lineread.readLine() ;
            // 클라이언트상의 파일을 읽어 보낼 준비를 한다.
            try{
                sendfile =  new FileInputStream(fileName) ;
            }catch(Exception e){
                System.out.println("&#44594;?&#44541;&#44625;&#44437;&#44423;&#44511;&#44495;&#44455;&#44522;") ;
                return ;
            }
            // 전송용 데이터 스트림을 사용한다.
            Socket dataSocket = dataConnection("STOR " + fileName) ;
            OutputStream outstr =  dataSocket.getOutputStream() ;
            // 파일을 읽어 네트워크를 경유하여 서버로 보낸다.
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

    // execCommand 메소드
    // 명령에 대응하는 각 처리를 호출한다.
    public boolean execCommand(String command)
    {
          boolean cont = true ;
          switch(Integer.parseInt(command)){
          case 2 : //  서버의 디렉토리 표시 처리
              doLs() ;
              break ;
          case 3 : //  서버의 작업 디렉토리 변경 표시
              doCd() ;
              break ;
          case 4 : //  서버로부터의 파일 얻기 처리
              doGet() ;
              break ;
          case 5 : //  서버로 파일 전송 처리
              doPut() ;
              break ;
          case 6 : //  텍스트 전송 모드
              doAscii() ;
              break ;
          case 7 : //  바이너리 전송 모드
              doBinary() ;
              break ;
          case 9 : //  처리 종료
              doQuit() ;
              cont = false ;
              break ;
          default : // 그 이외의 입력 처리
              System.out.println("메뉴를 선택하세요 : ") ;
          }
          return(cont) ;
    }
    // main_proc 메소드
    // Ftp의 명령 메뉴를 출력하여 해당되는 처리를 호출한다.
    public void main_proc()
        throws IOException
    {
         boolean cont = true ;
        try {
            //  로그인 처리를 한다.
            doLogin() ;
            while(cont){
                //  메뉴를 출력한다.
                showMenu() ;
                //  명령을 받아서 처리한다.
                 cont = execCommand(getCommand()) ;
             }
        }
        catch(Exception e){
            System.err.print(e);
            System.exit(1);
        }
    }
    // getMsgs 메소드
    // 제어 스트림의 수신 슬롯을 개시한다.
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
    // main 메소드
    // TCP 연결(connection)을 열어서 처리를 개시한다.
    public static void main(String[] arg){
        try {
            FtpClient f = new FtpClient();
            f.openConnection(serverAddr); //  제어용 연결의 설정
            f.getMsgs() ;             //  수신 슬롯의 개시
            f.main_proc();            // ftp 처리
            f.closeConnection() ;     //  연결 닫기
            System.exit(0) ;          //  프로그램 종료
        }catch(Exception e){
            e.printStackTrace();
            System.exit(1);
        }
    }
}

class ListenThread extends Thread{
        BufferedReader ctrlInput = null ;
        //  constructor 읽고 처리하기 위한 상대방 지정
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

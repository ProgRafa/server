/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.Scanner;

/**
 *
 * @author Eduardo
 */
public class Conexao {

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        ServerSocket port;
        Socket tunnel;
        Date data = new Date();
        PrintWriter saida;
        BufferedReader entrada;
        
        String absolutUrl="C:\\Users\\Rafael\\Desktop\\launchpad";
        
        Conexao callerMethodsToCennect = new Conexao();
        
        port = new ServerSocket(8080);
        System.out.println("Porta 8080 Aberta!!!");
        
        while(true){
            tunnel = port.accept(); 
            
            entrada = new BufferedReader(new InputStreamReader(tunnel.getInputStream())) ;
            saida = new PrintWriter(tunnel.getOutputStream(), true);
            
            
            Scanner getContentRequest=new Scanner(tunnel.getInputStream());
            String header=getContentRequest.nextLine();
                
            System.out.println(header); 
                        
            String archiveName = callerMethodsToCennect.parseToFindArchiveName(header);
            System.out.println(absolutUrl+archiveName);
            try(FileReader invokeArchive = new FileReader(absolutUrl+archiveName);){
                saida.write(callerMethodsToCennect.createHeaderServer());

                System.out.println("Arquivo Solicitado: " +archiveName);                        

                String response = printArchiveRequest(invokeArchive); 

                saida.println(response);//printa a primeira linha para o cliente
            }catch(FileNotFoundException e){
                saida.write("HTTP/1.1 404 Not Found\r\n\n<H1>404 NOT FOUND</H1>");
                saida.close();
            }
        }
    }

    /**
     *
     * @param header
     * @return findMyTitle
     */
    public String parseToFindArchiveName (String header){

        //////////Pega a palavra entre o GET HTTP/1.0 e 200 Ok///////////// 
        String[] splitInWords = header.split(" ");
        String[] findMyTitle = splitInWords[1].split("/");
        String url="";
        for(String i: findMyTitle)
        {
            url += i+"\\";
        }
        
        System.out.println(url);
        return url;
    }
    
    public String createHeaderServer()
    {
        Date data = new Date();
        String header = "HTTP/1.0 200 OK\r\n"+
                        data+"\r\n"+
                        "Server: Apache\r\n"+
                        "Content-Type: text/html; charset=UTF-8 \r\n"+
                        "Expires: Sat, 01 Jan 2000 00:59:59 GMT\r\n"+
                        "Last-modified: Fri, 09 Aug 1996 14:21:40 GMT\r\n"+
                        "\r\n"+
                        "<TITLE>Servidor</TITLE>"+
                        "<P>  \n</P>";
        
        return header;
    }
    
    /**
     *
     * @param name
     * @return response
     * @throws IOException
     */
    public static String printArchiveRequest(FileReader name) throws IOException
    {
        BufferedReader stringFirstLineHeader = new BufferedReader(name);
        String linha = stringFirstLineHeader.readLine();
        String response="";
        
        while(linha!=null)
        {
            response += linha+"\n"; 
            System.out.println(linha);
            linha = stringFirstLineHeader.readLine();
        } 
            
        return response;
    }
}
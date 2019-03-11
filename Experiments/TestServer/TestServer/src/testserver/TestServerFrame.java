/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testserver;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import org.json.JSONException;
import org.json.JSONObject;
import java.time.format.DateTimeFormatter;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;

/**
 *
 * @author Aaron
 */
public class TestServerFrame extends javax.swing.JFrame {

    static Socket s;
    static ServerSocket ss;
    static ObjectInputStream in;
    static ObjectOutputStream out;
    static BufferedReader br;
    static String message;
    static InetAddress ina;
    /**
     * Creates new form TestServerFrame
     */
    public TestServerFrame() {
        //initComponents();
    }

    @SuppressWarnings("unchecked")/*
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
*/
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(TestServerFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TestServerFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TestServerFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TestServerFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /*Create and display the form*/
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new TestServerFrame().setVisible(true);
            }
        });
        
        JSONObject tile0 = new JSONObject();
        try
        {
            tile0.put("x", 0);
            tile0.put("y", 0);
            tile0.put("terrainType", "genericGrass1");
            tile0.put("object", "genericBarrier1");
            tile0.put("character", "");
            tile0.put("walkable", false);
        }
        catch(JSONException e)
        {
        }
        JSONObject tile1 = new JSONObject();
        try
        {
            tile1.put("x", 0);
            tile1.put("y", 1);
            tile1.put("terrainType", "genericGrass1");
            tile1.put("object", "genericBarrier1");
            tile1.put("character", "");
            tile1.put("walkable", false);
        }
        catch(JSONException e)
        {
        }
        JSONObject tile2 = new JSONObject();
        try
        {
            tile2.put("x", 0);
            tile2.put("y", 2);
            tile2.put("terrainType", "genericGrass1");
            tile2.put("object", "genericBarrier1");
            tile2.put("character", "");
            tile2.put("walkable", false);
        }
        catch(JSONException e)
        {
        }
        JSONObject tile3 = new JSONObject();
        try
        {
            tile3.put("x", 0);
            tile3.put("y", 3);
            tile3.put("terrainType", "genericGrass1");
            tile3.put("object", "");
            tile3.put("character", "");
            tile3.put("walkable", true);
        }
        catch(JSONException e)
        {
        }
        JSONObject tile4 = new JSONObject();
        try
        {
            tile4.put("x", 0);
            tile4.put("y", 4);
            tile4.put("terrainType", "genericGrass1");
            tile4.put("object", "");
            tile4.put("character", "");
            tile4.put("walkable", true);
        }
        catch(JSONException e)
        {
        }
        JSONObject tile5 = new JSONObject();
        try
        {
            tile5.put("x", 0);
            tile5.put("y", 5);
            tile5.put("terrainType", "genericGrass1");
            tile5.put("object", "");
            tile5.put("character", "");
            tile5.put("walkable", true);
        }
        catch(JSONException e)
        {
        }
        JSONObject tile6 = new JSONObject();
        try
        {
            tile6.put("x", 0);
            tile6.put("y", 6);
            tile6.put("terrainType", "genericGrass1");
            tile6.put("object", "genericBarrier1");
            tile6.put("character", "");
            tile6.put("walkable", false);
        }
        catch(JSONException e)
        {
        }
        JSONObject tile7 = new JSONObject();
        try
        {
            tile7.put("x", 0);
            tile7.put("y", 7);
            tile7.put("terrainType", "genericGrass1");
            tile7.put("object", "genericBarrier1");
            tile7.put("character", "");
            tile7.put("walkable", false);
        }
        catch(JSONException e)
        {
        }
        JSONObject tile8 = new JSONObject();
        try
        {
            tile8.put("x", 0);
            tile8.put("y", 8);
            tile8.put("terrainType", "genericGrass1");
            tile8.put("object", "genericBarrier1");
            tile8.put("character", "");
            tile8.put("walkable", false);
        }
        catch(JSONException e)
        {
        }
        JSONObject tile9 = new JSONObject();
        try
        {
            tile9.put("x", 1);
            tile9.put("y", 0);
            tile9.put("terrainType", "genericGrass1");
            tile9.put("object", "genericBarrier1");
            tile9.put("character", "");
            tile9.put("walkable", false);
        }
        catch(JSONException e)
        {
        }
        JSONObject tile10 = new JSONObject();
        try
        {
            tile10.put("x", 1);
            tile10.put("y", 1);
            tile10.put("terrainType", "genericGrass1");
            tile10.put("object", "genericBarrier1");
            tile10.put("character", "");
            tile10.put("walkable", false);
        }
        catch(JSONException e)
        {
        }
        JSONObject tile11 = new JSONObject();
        try
        {
            tile11.put("x", 1);
            tile11.put("y", 2);
            tile11.put("terrainType", "genericGrass1");
            tile11.put("object", "genericBarrier1");
            tile11.put("character", "");
            tile11.put("walkable", false);
        }
        catch(JSONException e)
        {
        }
        JSONObject tile12 = new JSONObject();
        try
        {
            tile12.put("x", 1);
            tile12.put("y", 3);
            tile12.put("terrainType", "genericGrass1");
            tile12.put("object", "");
            tile12.put("character", "");
            tile12.put("walkable", true);
        }
        catch(JSONException e)
        {
        }
        JSONObject tile13 = new JSONObject();
        try
        {
            tile13.put("x", 1);
            tile13.put("y", 4);
            tile13.put("terrainType", "genericGrass1");
            tile13.put("object", "");
            tile13.put("character", "");
            tile13.put("walkable", true);
        }
        catch(JSONException e)
        {
        }
        JSONObject tile14 = new JSONObject();
        try
        {
            tile14.put("x", 1);
            tile14.put("y", 5);
            tile14.put("terrainType", "genericGrass1");
            tile14.put("object", "");
            tile14.put("character", "Aaron");
            tile14.put("walkable", true);
        }
        catch(JSONException e)
        {
        }
        JSONObject tile15 = new JSONObject();
        try
        {
            tile15.put("x", 1);
            tile15.put("y", 6);
            tile15.put("terrainType", "genericGrass1");
            tile15.put("object", "genericBarrier1");
            tile15.put("character", "");
            tile15.put("walkable", false);
        }
        catch(JSONException e)
        {
        }
        JSONObject tile16 = new JSONObject();
        try
        {
            tile16.put("x", 1);
            tile16.put("y", 7);
            tile16.put("terrainType", "genericGrass1");
            tile16.put("object", "genericBarrier1");
            tile16.put("character", "");
            tile16.put("walkable", false);
        }
        catch(JSONException e)
        {
        }
        JSONObject tile17 = new JSONObject();
        try
        {
            tile17.put("x", 1);
            tile17.put("y", 8);
            tile17.put("terrainType", "genericGrass1");
            tile17.put("object", "genericBarrier1");
            tile17.put("character", "");
            tile17.put("walkable", false);
        }
        catch(JSONException e)
        {
        }
        JSONObject tile18 = new JSONObject();
        try
        {
            tile18.put("x", 2);
            tile18.put("y", 0);
            tile18.put("terrainType", "genericGrass1");
            tile18.put("object", "genericBarrier1");
            tile18.put("character", "");
            tile18.put("walkable", false);
        }
        catch(JSONException e)
        {
        }
        JSONObject tile19 = new JSONObject();
        try
        {
            tile19.put("x", 2);
            tile19.put("y", 1);
            tile19.put("terrainType", "genericGrass1");
            tile19.put("object", "genericBarrier1");
            tile19.put("character", "");
            tile19.put("walkable", false);
        }
        catch(JSONException e)
        {
        }
        JSONObject tile20 = new JSONObject();
        try
        {
            tile20.put("x", 2);
            tile20.put("y", 2);
            tile20.put("terrainType", "genericGrass1");
            tile20.put("object", "genericBarrier1");
            tile20.put("character", "");
            tile20.put("walkable", false);
        }
        catch(JSONException e)
        {
        }
        JSONObject tile21 = new JSONObject();
        try
        {
            tile21.put("x", 2);
            tile21.put("y", 3);
            tile21.put("terrainType", "genericGrass1");
            tile21.put("object", "");
            tile21.put("character", "");
            tile21.put("walkable", true);
        }
        catch(JSONException e)
        {
        }
        JSONObject tile22 = new JSONObject();
        try
        {
            tile22.put("x", 2);
            tile22.put("y", 4);
            tile22.put("terrainType", "genericGrass1");
            tile22.put("object", "");
            tile22.put("character", "");
            tile22.put("walkable", true);
        }
        catch(JSONException e)
        {
        }
        JSONObject tile23 = new JSONObject();
        try
        {
            tile23.put("x", 2);
            tile23.put("y", 5);
            tile23.put("terrainType", "genericGrass1");
            tile23.put("object", "");
            tile23.put("character", "");
            tile23.put("walkable", true);
        }
        catch(JSONException e)
        {
        }
        JSONObject tile24 = new JSONObject();
        try
        {
            tile24.put("x", 2);
            tile24.put("y", 6);
            tile24.put("terrainType", "genericGrass1");
            tile24.put("object", "genericBarrier1");
            tile24.put("character", "");
            tile24.put("walkable", false);
        }
        catch(JSONException e)
        {
        }
        JSONObject tile25 = new JSONObject();
        try
        {
            tile25.put("x", 2);
            tile25.put("y", 7);
            tile25.put("terrainType", "genericGrass1");
            tile25.put("object", "genericBarrier1");
            tile25.put("character", "");
            tile25.put("walkable", false);
        }
        catch(JSONException e)
        {
        }
        JSONObject tile26 = new JSONObject();
        try
        {
            tile26.put("x", 2);
            tile26.put("y", 8);
            tile26.put("terrainType", "genericGrass1");
            tile26.put("object", "genericBarrier1");
            tile26.put("character", "");
            tile26.put("walkable", false);
        }
        catch(JSONException e)
        {
        }
        JSONObject tile27 = new JSONObject();
        try
        {
            tile27.put("x", 3);
            tile27.put("y", 0);
            tile27.put("terrainType", "genericGrass1");
            tile27.put("object", "genericBarrier1");
            tile27.put("character", "");
            tile27.put("walkable", false);
        }
        catch(JSONException e)
        {
        }
        JSONObject tile28 = new JSONObject();
        try
        {
            tile28.put("x", 3);
            tile28.put("y", 1);
            tile28.put("terrainType", "genericGrass1");
            tile28.put("object", "genericBarrier1");
            tile28.put("character", "");
            tile28.put("walkable", false);
        }
        catch(JSONException e)
        {
        }
        JSONObject tile29 = new JSONObject();
        try
        {
            tile29.put("x", 3);
            tile29.put("y", 2);
            tile29.put("terrainType", "genericGrass1");
            tile29.put("object", "genericBarrier1");
            tile29.put("character", "");
            tile29.put("walkable", false);
        }
        catch(JSONException e)
        {
        }
        JSONObject tile30 = new JSONObject();
        try
        {
            tile30.put("x", 3);
            tile30.put("y", 3);
            tile30.put("terrainType", "genericGrass1");
            tile30.put("object", "");
            tile30.put("character", "");
            tile30.put("walkable", true);
        }
        catch(JSONException e)
        {
        }
        JSONObject tile31 = new JSONObject();
        try
        {
            tile31.put("x", 3);
            tile31.put("y", 4);
            tile31.put("terrainType", "genericGrass1");
            tile31.put("object", "");
            tile31.put("character", "");
            tile31.put("walkable", true);
        }
        catch(JSONException e)
        {
        }
        JSONObject tile32 = new JSONObject();
        try
        {
            tile32.put("x", 3);
            tile32.put("y", 5);
            tile32.put("terrainType", "genericGrass1");
            tile32.put("object", "");
            tile32.put("character", "");
            tile32.put("walkable", true);
        }
        catch(JSONException e)
        {
        }
        JSONObject tile33 = new JSONObject();
        try
        {
            tile33.put("x", 3);
            tile33.put("y", 6);
            tile33.put("terrainType", "genericGrass1");
            tile33.put("object", "genericBarrier1");
            tile33.put("character", "");
            tile33.put("walkable", false);
        }
        catch(JSONException e)
        {
        }
        JSONObject tile34 = new JSONObject();
        try
        {
            tile34.put("x", 3);
            tile34.put("y", 7);
            tile34.put("terrainType", "genericGrass1");
            tile34.put("object", "genericBarrier1");
            tile34.put("character", "");
            tile34.put("walkable", false);
        }
        catch(JSONException e)
        {
        }
        JSONObject tile35 = new JSONObject();
        try
        {
            tile35.put("x", 3);
            tile35.put("y", 8);
            tile35.put("terrainType", "genericGrass1");
            tile35.put("object", "genericBarrier1");
            tile35.put("character", "");
            tile35.put("walkable", false);
        }
        catch(JSONException e)
        {
        }
        JSONObject tile36 = new JSONObject();
        try
        {
            tile36.put("x", 4);
            tile36.put("y", 0);
            tile36.put("terrainType", "genericGrass1");
            tile36.put("object", "genericBarrier1");
            tile36.put("character", "");
            tile36.put("walkable", false);
        }
        catch(JSONException e)
        {
        }
        JSONObject tile37 = new JSONObject();
        try
        {
            tile37.put("x", 4);
            tile37.put("y", 1);
            tile37.put("terrainType", "genericGrass1");
            tile37.put("object", "genericBarrier1");
            tile37.put("character", "");
            tile37.put("walkable", false);
        }
        catch(JSONException e)
        {
        }
        JSONObject tile38 = new JSONObject();
        try
        {
            tile38.put("x", 4);
            tile38.put("y", 2);
            tile38.put("terrainType", "genericGrass1");
            tile38.put("object", "genericBarrier1");
            tile38.put("character", "");
            tile38.put("walkable", false);
        }
        catch(JSONException e)
        {
        }
        JSONObject tile39 = new JSONObject();
        try
        {
            tile39.put("x", 4);
            tile39.put("y", 3);
            tile39.put("terrainType", "genericGrass1");
            tile39.put("object", "");
            tile39.put("character", "");
            tile39.put("walkable", true);
        }
        catch(JSONException e)
        {
        }
        JSONObject tile40 = new JSONObject();
        try
        {
            tile40.put("x", 4);
            tile40.put("y", 4);
            tile40.put("terrainType", "genericGrass1");
            tile40.put("object", "");
            tile40.put("character", "");
            tile40.put("walkable", true);
        }
        catch(JSONException e)
        {
        }
        JSONObject tile41 = new JSONObject();
        try
        {
            tile41.put("x", 4);
            tile41.put("y", 5);
            tile41.put("terrainType", "genericGrass1");
            tile41.put("object", "");
            tile41.put("character", "");
            tile41.put("walkable", true);
        }
        catch(JSONException e)
        {
        }
        JSONObject tile42 = new JSONObject();
        try
        {
            tile42.put("x", 4);
            tile42.put("y", 6);
            tile42.put("terrainType", "genericGrass1");
            tile42.put("object", "genericBarrier1");
            tile42.put("character", "");
            tile42.put("walkable", false);
        }
        catch(JSONException e)
        {
        }
        JSONObject tile43 = new JSONObject();
        try
        {
            tile43.put("x", 4);
            tile43.put("y", 7);
            tile43.put("terrainType", "genericGrass1");
            tile43.put("object", "genericBarrier1");
            tile43.put("character", "");
            tile43.put("walkable", false);
        }
        catch(JSONException e)
        {
        }
        JSONObject tile44 = new JSONObject();
        try
        {
            tile44.put("x", 4);
            tile44.put("y", 8);
            tile44.put("terrainType", "genericGrass1");
            tile44.put("object", "genericBarrier1");
            tile44.put("character", "");
            tile44.put("walkable", false);
        }
        catch(JSONException e)
        {
        }
        JSONArray tiles = new JSONArray();
        tiles.put(tile0);
        tiles.put(tile1);
        tiles.put(tile2);
        tiles.put(tile3);
        tiles.put(tile4);
        tiles.put(tile5);
        tiles.put(tile6);
        tiles.put(tile7);
        tiles.put(tile8);
        tiles.put(tile9);
        tiles.put(tile10);
        tiles.put(tile11);
        tiles.put(tile12);
        tiles.put(tile13);
        tiles.put(tile14);
        tiles.put(tile15);
        tiles.put(tile16);
        tiles.put(tile17);
        tiles.put(tile18);
        tiles.put(tile19);
        tiles.put(tile20);
        tiles.put(tile21);
        tiles.put(tile22);
        tiles.put(tile23);
        tiles.put(tile24);
        tiles.put(tile25);
        tiles.put(tile26);
        tiles.put(tile27);
        tiles.put(tile28);
        tiles.put(tile29);
        tiles.put(tile30);
        tiles.put(tile31);
        tiles.put(tile32);
        tiles.put(tile33);
        tiles.put(tile34);
        tiles.put(tile35);
        tiles.put(tile36);
        tiles.put(tile37);
        tiles.put(tile38);
        tiles.put(tile39);
        tiles.put(tile40);
        tiles.put(tile41);
        tiles.put(tile42);
        tiles.put(tile43);
        tiles.put(tile44);
        try
        {
            ss = new ServerSocket(10044);  
            s = ss.accept();
            System.out.println("client connected: " + s.isConnected());
            ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
            out.flush();
            in = new ObjectInputStream(s.getInputStream());
            while(true)
            {    
                message = readMessage();
                System.out.println("message: " + message);
                JSONObject server = new JSONObject();
                JSONObject c = new JSONObject(message);
                byte[] bytes;
                if(c.has("username") && c.has("password"))
                {
                    if(c.get("password").equals("Colts") && c.get("username").equals("Aaron"))
                    {
                        server.put("loginSuccess", "true");
                    }
                    else
                    {
                        server.put("loginSuccess", "false");
                    }
                }
                else if(c.has("requestType"))
                {
                    if(c.get("requestType").equals("subscription"))
                    {
                        server.put("newDungeon", "true");
                        server.put("xL", "0");
                        server.put("xR", "15");
                        server.put("yU", "15");
                        server.put("yL", "0");
                        server.put("tiles", tiles);
                    }
                }
                else
                {
                    
                }
                bytes = server.toString().getBytes(StandardCharsets.US_ASCII);
                /*jTextArea1.setText(jTextArea1.getText() + message + "\n");
                jTextArea1.setText(jTextArea1.getText() + server.toString() + "\n");*/
                out.writeObject(Arrays.toString(bytes));
                out.flush();
            }
        }
        catch(ClassNotFoundException | IOException e)
        {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
            LocalTime time =  LocalTime.now();
            System.out.println(dtf.format(time));
        } catch (JSONException ex) {
            Logger.getLogger(TestServerFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    public static String readMessage() throws IOException, ClassNotFoundException
    {
        String messageIn = (String) in.readObject();
        String temp = messageIn.replaceAll("[\\[\\]\\s*]", "");
        String[] ascii = temp.split(",");
        int[] asciiValues = new int[ascii.length];
        String serverMessage = "";
        for(int i = 0; i < asciiValues.length; i++)
        {
            asciiValues[i] = Integer.parseInt(ascii[i]);
            serverMessage += (char) asciiValues[i];
        }
        return serverMessage;
    }
/*
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JScrollPane jScrollPane1;
    public static javax.swing.JTextArea jTextArea1;
    // End of variables declaration//GEN-END:variables
*/}

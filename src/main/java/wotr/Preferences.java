package wotr;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/* renamed from: Preferences */
public class Preferences {
    String addr;
    int boardfontsize;
    int cardsHeight;
    int cardsWidth;
    int chatHeight;
    int chatWidth;
    int diceFPHeight;
    int diceFPWidth;
    int diceSAHeight;
    int diceSAWidth;
    boolean dicesorted;
    int flashMandatory;
    int flashOptional;
    String font;
    int fontsize;
    int highlightHeight;
    int highlightWidth;
    int horizontalSpacing;
    boolean iponstart;
    String language;
    boolean largeControlMarkers;
    String lastGame = "";
    String lastPasswordUsed = "";
    String myActionColour;
    String myChatColour;
    String nick;
    boolean noBorder;
    boolean noPassword;
    String opponentActionColour;
    String opponentChatColour;
    int port;
    boolean shortGroupDesc;
    boolean showLeadership;
    String translation;
    int verticalSpacing;
    int winx;
    int winy;
    int zoom;
    String zoomLevels;
    boolean disableDatabase;

    public static Preferences loadPrefs(File f) {
        Preferences p = new Preferences();
        p.zoom = 100;
        p.winx = 1024;
        p.winy = 738;
        p.nick = "Newbie";
        p.port = 4747;
        p.addr = "127.0.0.1";
        p.font = "SansSerif";
        p.fontsize = 10;
        p.cardsWidth = 210;
        p.cardsHeight = 121;
        p.diceFPWidth = 210;
        p.diceFPHeight = 38;
        p.diceSAWidth = 210;
        p.diceSAHeight = 72;
        p.highlightWidth = 210;
        p.highlightHeight = 212;
        p.chatWidth = 210;
        p.chatHeight = 112;
        p.showLeadership = true;
        p.dicesorted = false;
        p.language = "";
        p.flashMandatory = 0;
        p.flashOptional = -1;
        p.translation = "";
        p.zoomLevels = "80,100,130,150,200";
        p.shortGroupDesc = false;
        p.lastPasswordUsed = "";
        p.lastGame = "base2";
        p.disableDatabase = false;
        p.horizontalSpacing = 2;
        p.verticalSpacing = 2;
        p.myChatColour = "0,0,255";
        p.opponentChatColour = "255,0,0";
        p.myActionColour = "0,0,150";
        p.opponentActionColour = "150,0,0";
        p.noBorder = false;
        p.iponstart = true;
        p.noPassword = false;
        p.boardfontsize = 11;
        try {
            BufferedReader br = new BufferedReader(new FileReader(f));
            while (br.ready()) {
                p.setVariable(br.readLine());
            }
            br.close();
        } catch (IOException e) {
        }
        return p;
    }

    public void save(File f) {
        String fileComments = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(f));
            while (br.ready()) {
                String fileLine = br.readLine();
                if (fileLine.startsWith("nick:")) {
                    break;
                }
                fileComments = String.valueOf(fileComments) + fileLine + "\n";
            }
            br.close();
            f.delete();
            FileWriter fw = new FileWriter(f);
            fw.write(fileComments);
            fw.write("nick:" + this.nick + "\n");
            fw.write("zoom:" + this.zoom + "\n");
            fw.write("zoomLevels:" + this.zoomLevels + "\n");
            fw.write("winx:" + this.winx + "\n");
            fw.write("winy:" + this.winy + "\n");
            fw.write("port:" + this.port + "\n");
            fw.write("addr:" + this.addr + "\n");
            fw.write("font:" + this.font + "\n");
            fw.write("fontsize:" + this.fontsize + "\n");
            fw.write("cardsWidth:" + this.cardsWidth + "\n");
            fw.write("cardsHeight:" + this.cardsHeight + "\n");
            fw.write("diceFPWidth:" + this.diceFPWidth + "\n");
            fw.write("diceFPHeight:" + this.diceFPHeight + "\n");
            fw.write("diceSAWidth:" + this.diceSAWidth + "\n");
            fw.write("diceSAHeight:" + this.diceSAHeight + "\n");
            fw.write("highlightWidth:" + this.highlightWidth + "\n");
            fw.write("highlightHeight:" + this.highlightHeight + "\n");
            fw.write("chatWidth:" + this.chatWidth + "\n");
            fw.write("chatHeight:" + this.chatHeight + "\n");
            fw.write("nopassword:" + this.noPassword + "\n");
            fw.write("showLeadership:" + this.showLeadership + "\n");
            fw.write("dicesorted:" + this.dicesorted + "\n");
            fw.write("language:" + this.language + "\n");
            fw.write("flashMandatory:" + this.flashMandatory + "\n");
            fw.write("flashOptional:" + this.flashOptional + "\n");
            fw.write("translation:" + this.translation + "\n");
            fw.write("shortGroupDesc:" + this.shortGroupDesc + "\n");
            fw.write("lastPasswordUsed:" + this.lastPasswordUsed + "\n");
            fw.write("lastGame:" + this.lastGame + "\n");
            fw.write("horizontalSpacing:" + this.horizontalSpacing + "\n");
            fw.write("verticalSpacing:" + this.verticalSpacing + "\n");
            fw.write("myChatColour:" + this.myChatColour + "\n");
            fw.write("opponentChatColour:" + this.opponentChatColour + "\n");
            fw.write("myActionColour:" + this.myActionColour + "\n");
            fw.write("opponentActionColour:" + this.opponentActionColour + "\n");
            fw.write("noBorder:" + this.noBorder + "\n");
            fw.write("showIPonstart:" + this.iponstart + "\n");
            fw.write("boardfontsize:" + this.boardfontsize + "\n");
            fw.write("disableDatabase:" + this.disableDatabase + "\n");
            fw.flush();
            fw.close();
        } catch (IOException e) {
        }
    }

    private void setVariable(String inputLine) {
        String line = inputLine.trim();
        try {
            if (line.length() > 4 && line.substring(0, 5).equals("nick:")) {
                setNick(line.substring(5));
            } else if (line.length() > 4 && line.substring(0, 5).equals("zoom:")) {
                this.zoom = Integer.parseInt(line.substring(5));
            } else if (line.length() > 4 && line.substring(0, 5).equals("winx:")) {
                this.winx = Integer.parseInt(line.substring(5));
            } else if (line.length() > 4 && line.substring(0, 5).equals("winy:")) {
                this.winy = Integer.parseInt(line.substring(5));
            } else if (line.length() > 4 && line.substring(0, 5).equals("port:")) {
                this.port = Integer.parseInt(line.substring(5));
            } else if (line.length() > 4 && line.substring(0, 5).equals("addr:")) {
                this.addr = line.substring(5);
            } else if (line.length() > 4 && line.substring(0, 5).equals("font:")) {
                this.font = line.substring(5);
            } else if (line.length() > 8 && line.substring(0, 9).equals("fontsize:")) {
                this.fontsize = Integer.parseInt(line.substring(9));
            } else if (line.length() > 10 && line.substring(0, 11).equals("cardsWidth:")) {
                this.cardsWidth = Integer.parseInt(line.substring(11));
            } else if (line.length() > 11 && line.substring(0, 12).equals("cardsHeight:")) {
                this.cardsHeight = Integer.parseInt(line.substring(12));
            } else if (line.length() > 11 && line.substring(0, 12).equals("diceFPWidth:")) {
                this.diceFPWidth = Integer.parseInt(line.substring(12));
            } else if (line.length() > 12 && line.substring(0, 13).equals("diceFPHeight:")) {
                this.diceFPHeight = Integer.parseInt(line.substring(13));
            } else if (line.length() > 11 && line.substring(0, 12).equals("diceSAWidth:")) {
                this.diceSAWidth = Integer.parseInt(line.substring(12));
            } else if (line.length() > 12 && line.substring(0, 13).equals("diceSAHeight:")) {
                this.diceSAHeight = Integer.parseInt(line.substring(13));
            } else if (line.length() > 14 && line.substring(0, 15).equals("highlightWidth:")) {
                this.highlightWidth = Integer.parseInt(line.substring(15));
            } else if (line.length() > 15 && line.substring(0, 16).equals("highlightHeight:")) {
                this.highlightHeight = Integer.parseInt(line.substring(16));
            } else if (line.length() > 9 && line.substring(0, 10).equals("chatWidth:")) {
                this.chatWidth = Integer.parseInt(line.substring(10));
            } else if (line.length() > 10 && line.substring(0, 11).equals("chatHeight:")) {
                this.chatHeight = Integer.parseInt(line.substring(11));
            } else if (line.length() > 10 && line.substring(0, 11).equals("nopassword:")) {
                this.noPassword = Boolean.parseBoolean(line.substring(11));
            } else if (line.length() > 14 && line.substring(0, 15).equals("showLeadership:")) {
                this.showLeadership = Boolean.parseBoolean(line.substring(15));
            } else if (line.length() > 10 && line.substring(0, 11).equals("dicesorted:")) {
                this.dicesorted = Boolean.parseBoolean(line.substring(11));
            } else if (line.length() > 9 && line.substring(0, 9).equals("language:")) {
                this.language = line.substring(9);
            } else if (line.length() > 14 && line.substring(0, 15).equals("flashMandatory:")) {
                this.flashMandatory = Integer.parseInt(line.substring(15));
            } else if (line.length() > 13 && line.substring(0, 14).equals("flashOptional:")) {
                this.flashOptional = Integer.parseInt(line.substring(14));
            } else if (line.length() > 12 && line.substring(0, 12).equals("translation:")) {
                this.translation = line.substring(12);
            } else if (line.length() > 11 && line.substring(0, 11).equals("zoomLevels:")) {
                this.zoomLevels = line.substring(11);
            } else if (line.length() > 14 && line.substring(0, 15).equals("shortGroupDesc:")) {
                this.shortGroupDesc = Boolean.parseBoolean(line.substring(15));
            } else if (line.length() > 16 && line.substring(0, 17).equals("lastPasswordUsed:")) {
                this.lastPasswordUsed = line.substring(17);
            } else if (line.length() > 8 && line.substring(0, 9).equals("lastGame:")) {
                this.lastGame = line.substring(9);
            } else if (line.length() > 17 && line.substring(0, 18).equals("horizontalSpacing:")) {
                this.horizontalSpacing = Integer.parseInt(line.substring(18));
            } else if (line.length() > 15 && line.substring(0, 16).equals("verticalSpacing:")) {
                this.verticalSpacing = Integer.parseInt(line.substring(16));
            } else if (line.length() > 12 && line.substring(0, 13).equals("myChatColour:")) {
                this.myChatColour = line.substring(13);
            } else if (line.length() > 18 && line.substring(0, 19).equals("opponentChatColour:")) {
                this.opponentChatColour = line.substring(19);
            } else if (line.length() > 14 && line.substring(0, 15).equals("myActionColour:")) {
                this.myActionColour = line.substring(15);
            } else if (line.length() > 20 && line.substring(0, 21).equals("opponentActionColour:")) {
                this.opponentActionColour = line.substring(21);
            } else if (line.length() > 8 && line.substring(0, 9).equals("noBorder:")) {
                this.noBorder = Boolean.parseBoolean(line.substring(9));
            } else if (line.length() > 13 && line.substring(0, 14).equals("showIPonstart:")) {
                this.iponstart = Boolean.parseBoolean(line.substring(14));
            } else if (line.length() > 8 && line.substring(0, 14).equals("boardfontsize:")) {
                this.boardfontsize = Integer.parseInt(line.substring(14));
            } else if (line.length() > 15 && line.substring(0, 16).equals("disableDatabase:")) {
                this.disableDatabase = Boolean.parseBoolean(line.substring(16));
            }
        } catch (Throwable th) {
        }
    }

    public void setNick(String inputNick) {
        String newNick = inputNick.replaceAll(" ", "_");
        if (newNick.equals("game")) {
            newNick = "game_";
        }
        this.nick = newNick;
    }
}

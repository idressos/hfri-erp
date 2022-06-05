package gr.ntua.ece.db.hfri.erp;

import java.net.URL;

import java.util.Date;
import java.util.Calendar;

import java.io.IOException;

import java.text.Normalizer;
import java.text.Normalizer.Form;

import java.awt.Color;
import java.awt.Image;
import java.awt.Graphics2D;
import java.awt.AlphaComposite;
import java.awt.RenderingHints;

import java.awt.image.BufferedImage;

import java.awt.geom.RoundRectangle2D;

import javax.imageio.ImageIO;

import org.json.JSONObject;
import org.json.JSONException;

import gr.ntua.ece.http.HTTPRequests;
import gr.ntua.ece.http.HTTPResponse;

import gr.ntua.ece.db.hfri.types.ResearchWorker;

public class Utils {
	
	public static BufferedImage roundImage(Image image, int cornerRadius) {
		int w = image.getWidth(null);
		int h = image.getHeight(null);
		
		BufferedImage output = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		
		Graphics2D g2 = output.createGraphics();
		
		g2.setComposite(AlphaComposite.Src);
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(Color.WHITE);
		g2.fill(new RoundRectangle2D.Float(0, 0, w, h, cornerRadius, cornerRadius));
		
		g2.setComposite(AlphaComposite.SrcIn);
		g2.drawImage(image, 0, 0, null);
		
		g2.dispose();
		
		return output;
	}
	
	public static BufferedImage getResearchWorkerAvatar(ResearchWorker researchWorker, int windowWidth) throws IOException {
		URL avatar = Utils.class.getResource("/img/avatar.png");
		
		if(Settings.getBoolean("miscellaneous.random-avatars")) {
			try {
				HTTPResponse apiResponse = HTTPRequests.get(null, "https://randomuser.me/api/?inc=picture&gender=" + researchWorker.getSex().toString().toLowerCase() + "&noinfo");
				
				if(apiResponse.getCode() == 200) {
					JSONObject responseData = new JSONObject(apiResponse.getBody());
					
					avatar = new URL(responseData.getJSONArray("results").getJSONObject(0).getJSONObject("picture").getString("large"));
				}
			} catch(IOException | JSONException ex) {}
		}
		
		int width = Math.min(windowWidth / 6, 104);
		int cornerRadius = (width * 78) / 100;
		
		return Utils.roundImage(ImageIO.read(avatar).getScaledInstance(width, -1, Image.SCALE_SMOOTH), cornerRadius);
	}
	
	public static int getDateDiffYears(Date a, Date b) {
		Calendar ca = getCalendar(a);
		Calendar cb = getCalendar(b);
		
		int diff = cb.get(Calendar.YEAR) - ca.get(Calendar.YEAR);
		
		if(ca.get(Calendar.MONTH) > cb.get(Calendar.MONTH) ||
			(ca.get(Calendar.MONTH) == cb.get(Calendar.MONTH) && ca.get(Calendar.DATE) > cb.get(Calendar.DATE))) {
			
			diff--;
	    }
		
		return diff;
	}
	
	public static Calendar getCalendar(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		
		return cal;
	}
	
	public static String removeDiacriticalMarks(String string) {
	    return Normalizer.normalize(string, Form.NFD)
	        .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
	}
	
}
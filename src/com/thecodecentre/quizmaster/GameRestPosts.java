package com.thecodecentre.quizmaster;

import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GameRestPosts {
	
  	private static final Logger Log = Logger.getLogger(GameRestPosts.class.getName());

	public static String RestPostsFromPath(QMaster qm, HttpServletRequest req) throws TCCException
	{
		String jsonrsp = null;
	    Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		String path = req.getPathInfo();

		//	/qmgames/*
		if(path != null && path.length() > 1)	// nothing or just a /
		{
			String[] p = path.split("/");
			// first field will always be blank after the split
			long gameid = 0;
			QMGame game = null;
			
			try		//	/qmgames/<long game id>
			{
				gameid = Long.parseLong(p[1]);
				game = MPGMethods.GetGameFromId(gameid);
			}
			catch (NumberFormatException nfe)
			{
				throw new TCCException("Game id is missing");					
			}

			if(p.length == 2)	// just the game id?
			{
				// TODO
			}
			else if(p.length == 3 || p.length == 4)	// next param
			{
			
				if(p[2].equals("contestant"))	// /qmgame/nnnnnnn/contestant
				{
					QMContestant con = MPGMethods.AddContestant(game, req);
					jsonrsp = gson.toJson(con);	
				}
				else if(p[2].equals("questions"))	// /qmgame/nnnnnnn/questions
				{
					Log.info("Add question to game "+gameid);
					String qidstr;
					// check put data contains valid question id
					if((qidstr = req.getParameter("questionid")) == null)
						throw new TCCException("Request Invalid, question id missing");
					int newqid = Integer.parseInt(qidstr);
					QMQuestion qmq = QMQuestion.getQMQuestionFromId(newqid);
					game.addNewQuestion(newqid);
					jsonrsp = gson.toJson(qmq);
				}
				else
				{
					throw new TCCException("This request is invalid");
				}
			}
		}
		else
		{
			QMGame newgame = MPGMethods.AddGame(qm, req);
			jsonrsp = gson.toJson(newgame);
		}

		return jsonrsp;
	}

	private static void SendStartNotifications(QMGame game) 
	{
		// TODO Auto-generated method stub
		
	}

}

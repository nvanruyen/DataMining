package ti2736c;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class main {

	public static void main(String[] args) {
		Locale.setDefault(Locale.US);
		// Create user list, movie list, and list of ratings
		UserList userList = new UserList();
		userList.readFile("data/users.csv");
		MovieList movieList = new MovieList();
		movieList.readFile("data/movies.csv");
		RatingList ratings = new RatingList();
		ratings.readFile("data/ratings.csv", userList, movieList);

		// Read list of ratings we need to predict
		RatingList predRatings = new RatingList();
		predRatings.readFile("data/predictions.csv", userList, movieList);

		// Perform rating predictions
		predictRatings(userList, movieList, ratings, predRatings);

		// Write result file
		predRatings.writeResultsFile("submission.csv");
	}

	public static RatingList predictRatings(UserList userList,
			MovieList movieList, RatingList ratingList, RatingList predRatings) {
		
		HashMap<Movie, ArrayList<Rating>> MovieMap = mapMovie(ratingList);
		HashMap<User, ArrayList<Rating>> UserMap = mapUser(ratingList);
		
		//Get the Rating we want to predict
		for(int i = 0; i< predRatings.size(); i++){
			Rating r = predRatings.get(i);
			User ur = r.getUser();
			Movie mr = r.getMovie();
			ArrayList<Rating> ar = new ArrayList<Rating>();
			ArrayList<Rating> movier = new ArrayList<Rating>();
			ArrayList<Movie> am = new ArrayList<Movie>();
			double sum = 0.0;
			double sum2 = 0.0;

			// Arraylist with the ratings of the user
			ar = UserMap.get(ur);
//			HashMap<Movie, Double> RatingMap = mapRating(ar);
			
			//Get average of users ratings
			if(ar.isEmpty()){
				sum = 0.0;
			}
			else{
				for(int n = 0; n<ar.size(); n++){
					sum = sum + ar.get(n).getRating();
				}
				sum = sum/ar.size();
			}
			
			//Get average of other users rating of the movie
			if(MovieMap.containsKey(mr)){
				movier = MovieMap.get(mr);
				for(int o = 0; o<movier.size(); o++){
					sum2 = sum2 + movier.get(o).getRating();
				}
				sum2 = sum2 / movier.size();
			}
			
				predRatings.get(i).setRating((sum + sum2)/2);
			
//			for(int k = 0; k<ar.size(); k++){
//				am.add(ar.get(k).getMovie());
//			}
//			
//			//Search for similar users who have rated mr
//			//An ArrayList with all the users who have rated mr
//			ArrayList<Rating> amr = new ArrayList<Rating>();
//			if(MovieMap.containsKey(mr))
//				amr = MovieMap.get(mr);
//			
//			//Find the user with the most similar ratings
//			double sim = Integer.MAX_VALUE;
//			User u = null;
//			ArrayList<Rating> tempu = null;
//			if(amr.isEmpty()){
//				predRatings.get(i).setRating(3.0);
//			}
//			else {
//				for(int v = 0; v<amr.size(); v++){
//					ArrayList<Rating> temp = new ArrayList<Rating>();
//					if(!UserMap.containsKey(amr.get(v).getUser()))
//						predRatings.get(i).setRating(3.0);
//					else{
//						temp = UserMap.get(amr.get(v).getUser());
//						ArrayList<Rating> same = new ArrayList<Rating>();
//						for(int o = 0; o<temp.size(); o++){
//							if(am.contains(temp.get(o).getMovie()))
//								same.add(temp.get(o));
//						}
//						double s = 0;
//						for(int m = 0; m<same.size(); m++){
//							s = s + Math.pow((same.get(m).getRating() - RatingMap.get(same.get(m).getMovie())), 2);
//						}
//						s = (Math.sqrt(s))/same.size();
//						if(s < sim){
//							sim = s;
//							u = amr.get(v).getUser();
//							tempu = temp;
//						}
//					}
//				}
//				
//					System.out.println(i);
//					if(tempu == null){
//						predRatings.get(i).setRating(3.0);
//					}
//					else
//						predRatings.get(i).setRating(getMovieRating(tempu, mr));
//					}
		}
			
			
		
		
		// Compute mean of ratings
//		double mean = ratingList.get(0).getRating();
//		for (int i = 1; i < ratingList.size(); i++) {
//			mean = ((double) i / ((double) i + 1.0)) * mean
//					+ (1.0 / ((double) i + 1.0))
//					* ratingList.get(i).getRating();
//		}
//
//		// Predict mean everywhere
//		for (int i = 0; i < predRatings.size(); i++) {
//			predRatings.get(i).setRating(mean);
//		}

		return predRatings;
	}
	
	public static HashMap<Movie, Double> mapRating(ArrayList<Rating> r){
		HashMap<Movie, Double> hm = new HashMap<Movie, Double>();
		for(int i = 0; i<r.size(); i++){
			hm.put(r.get(i).getMovie(), r.get(i).getRating());
		}
		return hm;
	}
	
	public static HashMap<Movie, ArrayList<Rating>> mapMovie(RatingList r){
		HashMap<Movie, ArrayList<Rating>> hm = new HashMap<Movie, ArrayList<Rating>>();
		
		for(int i = 0; i<r.size(); i++){
			if(hm.containsKey(r.get(i).getMovie())){
				hm.get(r.get(i).getMovie()).add(r.get(i));
			}
			else{
				ArrayList<Rating> temp = new ArrayList<Rating>();
				temp.add(r.get(i));
				hm.put(r.get(i).getMovie(), temp);
			}
		}
		return hm;
	}
	
	public static HashMap<User, ArrayList<Rating>> mapUser(RatingList r){
		HashMap<User, ArrayList<Rating>> hm = new HashMap<User, ArrayList<Rating>>();
		
		for(int i = 0; i<r.size(); i++){
			if(hm.containsKey(r.get(i).getUser())){
				hm.get(r.get(i).getUser()).add(r.get(i));
			}
			else{
				ArrayList<Rating> temp = new ArrayList<Rating>();
				temp.add(r.get(i));
				hm.put(r.get(i).getUser(), temp);
			}
		}
		
		return hm;
	}
	
	public static double getMovieRating(ArrayList<Rating> ar, Movie m){
		if(ar.size() == 0){
			return 0;
		}else{
		for(int i = 0; i<ar.size(); i++){
			if(ar.get(i).getMovie() == m)
				return ar.get(i).getRating();
		}
		return 0;
		}
	}
}

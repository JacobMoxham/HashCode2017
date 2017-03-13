import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Algorithm {

	public static void main(String[] args) {
		DataInput input = new DataInput();
		try {
			input.readInData(Paths.get("/home/jacob/Downloads/videos_worth_spreading.in"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		HashMap<Cache,Map<Video,Integer>> latencyDrops = new HashMap<>();
		Integer value;
		//Iterate over caches
		for(Cache c: input.cacheMap.values()){
			//For each video
			Map<Video, Integer> drops = new HashMap<Video,Integer>();
			for(Video v: input.videoMap.values()){
				//Calculate drop in latency from adding it
				value = 0;
				if(input.requestMap.get(v) != null){
					for(Request r : input.requestMap.get(v)){
						if(r.endpoint.latencies.containsKey(c)){
		
							value += r.number *(r.endpoint.serverLatency - r.endpoint.latencies.get(c));
							
						}
					}
				}
				drops.put(v, value/v.size);
				
				latencyDrops.put(c,drops);
				
			}
		
		}
		
		//Add biggest drop video if possible, if not next biggest until no more can be added to any cache
		boolean canAdd = true;
		
		//create cache video hashmap
		HashMap<Cache,List<Video>>cacheVideosToPut = new HashMap<>();
		for(Cache c: latencyDrops.keySet()){
			ArrayList<Video> videos = new ArrayList<>();
			for(Video v: input.videoMap.values()){
				videos.add(v);
			}
			cacheVideosToPut.put(c, videos);
		}
		//int count = 0;
		while(canAdd){
			//Calculate best video to add
			int maxDrop = 0;
			Cache cacheToAddTo = null;
			Video videoToAdd = null;
			
			
			for(Cache c: latencyDrops.keySet()){
				Map<Video,Integer> videoDrops = latencyDrops.get(c);
				for(Video v: cacheVideosToPut.get(c)){
					
					//System.out.println(videoDrops.get(v));
					
					if (videoDrops.get(v) > maxDrop){
						maxDrop = videoDrops.get(v);
						videoToAdd = v;
						cacheToAddTo = c;
					}
				}
			}
			//Add best video if possible
			if(cacheToAddTo == null){
				canAdd=false;
			}else{
				cacheToAddTo.videos.add(videoToAdd);
				cacheToAddTo.sizeRemaining -= videoToAdd.size;
				
				//remove videos which can't be added to a cache
				/*for(Cache c: input.cacheMap.values()){
					cacheVideosToPut.get(c).remove(videoToAdd);
				}*/
				cacheVideosToPut.get(cacheToAddTo).remove(videoToAdd);
				
				for(Cache c: latencyDrops.keySet()){
					ArrayList<Video> videosToRemove = new ArrayList<>();
					for(Video v: cacheVideosToPut.get(c)){
						if (v.size > c.sizeRemaining){
							videosToRemove.add(v);
						}
					}
					for(Video v: videosToRemove){
						cacheVideosToPut.get(c).remove(v);
					}
				}
			}
			
		}	
		
		//Test Output
		input.dataOutput();
		
	}
}



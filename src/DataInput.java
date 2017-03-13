
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class DataInput {
	
	public HashMap<Integer , Video> videoMap = new HashMap<>();
	public HashMap<Integer, Endpoint> endpointMap = new HashMap<>();
	public HashMap<Integer, Cache> cacheMap = new HashMap<>();
	public HashMap<Video, List<Request>> requestMap = new HashMap<>();
	
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		DataInput d = new DataInput();
		
		try {
			d.readInData(Paths.get("/home/jacob/Downloads/kittens.in"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	public void readInData(Path p) throws IOException {
		
		BufferedReader br = new BufferedReader(new FileReader(p.toFile()));
		
		
		//find the values of the first line
		int endPointNumber = 0;
		int cacheCapacity = 0;
		
		String firstLine = br.readLine();
		
		String[] seperatedData = firstLine.split(" ");
		
		endPointNumber = Integer.valueOf(seperatedData[1]);
		cacheCapacity = Integer.valueOf(seperatedData[4]);
		
		
		
		//create video list
		String videoSizesInputLine = br.readLine();
		
		seperatedData = videoSizesInputLine.split(" ");
		
		
		for (int i = 0; i < seperatedData.length; i++) {
			videoMap.put(i , new Video(i, Integer.valueOf(seperatedData[i])));
		}
		
		//collect endPoint data
		
		for (int endPointID = 0; endPointID < endPointNumber; endPointID++) {
			
			
			int endPointLatency = 0;
			int connectedCacheNumber = 0;
			
			String endPointData = br.readLine();
			
			seperatedData = endPointData.split(" ");
			
			endPointLatency = Integer.valueOf(seperatedData[0]);
			connectedCacheNumber = Integer.valueOf(seperatedData[1]);
			
			HashMap<Cache,Integer> connectedCaches = new HashMap<>();
			
			for (int i = 0; i < connectedCacheNumber; i++) {
				endPointData = br.readLine();
				
				seperatedData = endPointData.split(" ");
				int cacheID = Integer.valueOf(seperatedData[0]);
				int cacheLatency = Integer.valueOf(seperatedData[1]);
				
				if (cacheMap.containsKey(cacheID)) {
					connectedCaches.put(cacheMap.get(cacheID), cacheLatency);
				}else{
					Cache newCache = new Cache(cacheID, cacheCapacity);
					connectedCaches.put(newCache, cacheLatency);
					cacheMap.put(cacheID, newCache);
				}
				
			}
			
			endpointMap.put(endPointID, new Endpoint(endPointID, endPointLatency, connectedCaches));
			
		}
		
		String requestData;
		
		while((requestData = br.readLine()) != null) {
			seperatedData = requestData.split(" ");
			
			int videoID = Integer.valueOf(seperatedData[0]);
			int endpointID = Integer.valueOf(seperatedData[1]);
			int requestNumber = Integer.valueOf(seperatedData[2]);
			
			Video requestedVideo = videoMap.get(videoID);
			Endpoint requestedEndpoint = endpointMap.get(endpointID);
			
			if (requestMap.containsKey(requestedVideo)) {
				List<Request> requestList = requestMap.get(requestedVideo);
				requestList.add(new Request(requestedEndpoint, requestedVideo, requestNumber));
			}else{
				List<Request> requestList = new ArrayList<>();
				requestList.add(new Request(requestedEndpoint, requestedVideo, requestNumber));
				requestMap.put(requestedVideo, requestList);
			}
			
		}
		
		
		br.close();
		
		for(Cache c : cacheMap.values()) {
			ArrayList<Endpoint> endpointList = new ArrayList<>();
			
			
			for (Endpoint e : endpointMap.values()) {
				if (e.latencies.containsKey(c)) {
					endpointList.add(e);
				}
			}
			
			c.endpointList = endpointList;
			
		}
		
		
		
	}
	public void dataOutput() {
		
		int totalCacheInUse = 0;
		
		
		for (Cache c : cacheMap.values()) {
			if (c.videos.size() != 0) {
				totalCacheInUse++;
				System.out.print(c.id + " ");
				
				for (Video v : c.videos) {
					System.out.print(v.id + " ");
				}
				System.out.println();
			}
		}
		
		System.out.println(totalCacheInUse);
	}

}

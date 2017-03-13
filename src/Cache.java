import java.util.ArrayList;

public class Cache {
	int id;
	ArrayList<Video> videos;
	ArrayList<Endpoint> endpointList;
	int sizeRemaining;
	
	public Cache(int id, int startSize){
		this.id = id;
		this.videos = new ArrayList<Video>();
		sizeRemaining = startSize;
		
	}
}

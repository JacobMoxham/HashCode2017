import java.util.HashMap;

public class Endpoint {
	int id;
	int serverLatency;
	HashMap<Cache,Integer> latencies;
	public Endpoint(int id,int serverLatency, HashMap<Cache,Integer> latencies){
		this.id=id;
		this.serverLatency = serverLatency;
		this.latencies = latencies;
	}
}

package pcap.master.decorator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import pcap.master.core.ISocketDecoratorStrategy;
import pcap.master.core.SocketDetails;

public class TracerouteDecoratorStrategy implements ISocketDecoratorStrategy {

	@Override
	public void decorate(SocketDetails socketDetails) throws Exception {
		ProcessBuilder pb = new ProcessBuilder();
		pb.command("traceroute " + socketDetails.getIpAddress());
		
		Process process = pb.start();
		process.wait();
		
		String traceRouteOutput = getProcessOutput(process);
		
		socketDetails.setTraceRoute(traceRouteOutput);
	}

	private String getProcessOutput(Process process) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
		StringBuilder outputBuilder = new StringBuilder();
		String line = null;
		while((line = reader.readLine()) != null) {
			outputBuilder.append(line);
			outputBuilder.append("\n");
		}
		
		return outputBuilder.toString();
	}

}

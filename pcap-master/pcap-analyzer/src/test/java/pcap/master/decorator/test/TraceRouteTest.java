package pcap.master.decorator.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.junit.Test;

public class TraceRouteTest {
	@Test
	public void ProcessExecution() throws IOException {
		ProcessBuilder pb = new ProcessBuilder();
		pb.command("traceroute 192.168.178.110");
		
		Process process = pb.start();
		
		//process.getOutputStream()
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
		StringBuilder outputBuilder = new StringBuilder();
		String line = null;
		while((line = reader.readLine()) != null) {
			outputBuilder.append(line);
			outputBuilder.append("\n");
		}
		
		System.out.println(outputBuilder.toString());
	}
}

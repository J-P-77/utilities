/*
 * The MIT License (MIT)
 * 
 * Copyright (c) 2014 Justin Palinkas
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package utillib.net;

import utillib.arrays.ResizingArray;

import utillib.debug.DebugLogger;
import utillib.debug.LogManager;

import utillib.interfaces.IMyOutputStream;

import utillib.strings.MyStringBuffer;
import utillib.strings.StringUtil;

import utillib.utilities.Convert;
import utillib.utilities.RandomUtil;

import java.util.Enumeration;
import java.util.List;

import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;

/**
 * 
 * @author Justin Palinkas
 */
public class NetUtil {
	private static final DebugLogger _LOG_ = LogManager.getInstance().getLogger(NetUtil.class);

	//UDP IPv4 protocol is 65,507 bytes (65,535 - 8 byte UDP header - 20 byte IP header
	public static final int _MAX_UDP_PACKET_LENGTH_ = 65507;

	public static final int _MIN_PORT_ = 0;
	public static final int _MAX_PORT_ = 65535;

	public static final int _MAX_ETHERNET_PACKET_SIZE_ = 1500;

	public static final int _IN_USE = 1;
	public static final int _IN_NOT_USE = 2;

	public static final InetAddress _LOOPBACK_ADDRESS_;

	static {
		InetAddress Address = null;
		try {
			Address = InetAddress.getByName(null);
		} catch(Exception e) {}

		_LOOPBACK_ADDRESS_ = Address;
	}

	public static InetAddress getLoopbackAddress() {
		return _LOOPBACK_ADDRESS_;
	}

	public static String toString(InetAddress address, int port) {
		if(address == null) {
			return ":" + port;
		}

		return address.getHostAddress() + ":" + port;
	}

	public static boolean isReachable(InetAddress address, int timeout) {
		if(address == null) {
			throw new RuntimeException("Variable[address] - Is Null");
		}

		if(address.equals(_LOOPBACK_ADDRESS_)) {
			return true;
		}

		try {
			return address.isReachable(timeout);
		} catch(Exception e) {}

		return false;
	}

	public static boolean isReachable(InetAddress address, NetworkInterface ninterface, int ttl, int timeout) {
		if(address == null) {
			throw new RuntimeException("Variable[address] - Is Null");
		}

		if(ninterface == null) {
			throw new RuntimeException("Variable[ninterface] - Is Null");
		}

//		if(address.equals(_LOOPBACK_ADDRESS_)) {
//			return true;
//		}

		try {
			return address.isReachable(ninterface, ttl, timeout);
		} catch(Exception e) {}

		return false;
	}

	public static int getPort(String str) {
		if(StringUtil.isWholeNumber(str)) {
			final int PORT = Integer.parseInt(str);

			if(validPort(PORT)) {
				return PORT;
			}
		}

		return -1;
	}

	public static InetAddress getAddress(String str) {
		try {
			return InetAddress.getByName(str);
		} catch(Exception e) {}

		return null;
	}

	public static InetAddress[] getAddresses(String str) {
		try {
			return InetAddress.getAllByName(str);
		} catch(Exception e) {}

		return null;
	}

	public static boolean validPort(int port) {
		if(port < _MIN_PORT_) {
			return false;
		}

		if(port > _MAX_PORT_) {
			return false;
		}

		return true;
	}

	public static void printAvaiablePorts(int lowerport, int upperport, IMyOutputStream ostream, int print) {
		if(lowerport < _MIN_PORT_) {
			throw new RuntimeException("Variable[lowerport] - Invalid Port Number");
		}

		if(upperport > _MAX_PORT_) {
			throw new RuntimeException("Variable[upperport] - Invalid Port Number");
		}

		if(lowerport > upperport) {
			throw new RuntimeException("Variable[lowerport, upperport] - Lower Port Can't Be Greater Than Upper Port");
		}

		if((print != _IN_USE && print != _IN_NOT_USE) && (print != (_IN_USE | _IN_NOT_USE))) {
			throw new RuntimeException("Variable[print] - Invalid Print Options");
		}

		if(ostream == null) {
			throw new RuntimeException("Variable[ostream] - Is Null");
		}

		for(int X = lowerport; X < upperport; X++) {
			ServerSocket SS = null;
			try {
				SS = new ServerSocket(X);

				if((print & _IN_NOT_USE) != 0) {
					try {
						ostream.writeStringln("Port :" + X + " NOT In Use");
					} catch(Exception e1) {}
				}
			} catch(Exception e) {
				if((print & _IN_USE) != 0) {
					try {
						ostream.writeStringln("Port :" + X + " In Use");
					} catch(Exception e1) {}
				}
			} finally {
				if(SS != null) {
					try {
						SS.close();
					} catch(Exception e) {}
					SS = null;
				}
			}
		}
	}

	public static int getRandomPort() {
		final int MIN = RandomUtil.secureRandomInt(1024, 29999);
		final int MAX = RandomUtil.secureRandomInt(30000, _MAX_PORT_);

		return getAvaiablePort(MIN, MAX);
	}

	public static int getAvaiablePort(int lowerport, int upperport) {
		if(lowerport < _MIN_PORT_) {
			throw new RuntimeException("Variable[lowerport] - Invalid Port Number");
		}

		if(upperport > _MAX_PORT_) {
			throw new RuntimeException("Variable[upperport] - Invalid Port Number");
		}

		if(lowerport > upperport) {
			throw new RuntimeException("Variable[lowerport, upperport] - Lower Port Can't Be Greater Than Upper Port");
		}

		for(int X = lowerport; X < upperport; X++) {
			ServerSocket SS = null;
			try {
				SS = new ServerSocket(X);

				return X;
			} catch(Exception e) {

			} finally {
				try {
					if(SS != null) {
						SS.close();
						SS = null;
					}
				} catch(Exception e) {}
			}
		}

		return -1;
	}

	public static boolean isPrivateIp(String ip) {
		if(ip == null || ip.length() == 0) {
			throw new RuntimeException("Variable[ip] - Is Null");
		}

		final String[] STR_BYTES = StringUtil.split(ip, '.');

		if(STR_BYTES.length == 4/* || BYTES.length == 6 */) {
			final int[] BYTES = new int[4];

			for(int X = 0; X < BYTES.length; X++) {
				try {
					BYTES[X] = Integer.parseInt(STR_BYTES[X]);
				} catch(Exception e) {
					throw new RuntimeException("Variable[ip] - Invalid Ip: " + STR_BYTES[X]);
				}
			}
//    		
			return isPrivateIp(BYTES);
		} else {
			throw new RuntimeException("Variable[ip] - Invalid Ip Length");
		}
	}

//	10.0.0.0 – 10.255.255.255
//	172.16.0.0 – 172.31.255.255
//	192.168.0.0 – 192.168.255.255
	public static boolean isPrivateIp(int[] ip) {
		if(ip == null) {
			throw new RuntimeException("Variable[ip] - Is Null");
		}

		if(ip.length != 4) {
			throw new RuntimeException("Variable[ip] - Invalid Ip Length");
		}

		for(byte X = 0; X < ip.length; X++) {
			if(ip[X] < 0 || ip[X] > 255) {
				throw new RuntimeException("Variable[ip] - Invalid Ip: " + ip[X]);
			}
		}

		if(ip[0] == 10) {
			return true;
		} else if(ip[0] == 172) {
			if(ip[1] >= 16 && ip[1] <= 32) {
				return true;
			}
		} else if(ip[0] == 192) {
			if(ip[1] == 168) {
				return true;
			}
		}

		return false;
	}

	public static InetAddress getDefaultLocalIp() {
		try {
			final NetworkInterface NET = getDefaultNetworkInterface();

			if(NET != null) {
				final ResizingArray<InetAddress> IPS = new ResizingArray<InetAddress>(1);

				getNetworkInterfaceIps(NET, IPS);

				if(IPS.length() > 0) {
					return IPS.getAt(0);
				}
			}
		} catch(Exception e) {
			_LOG_.printError(e);
		}

		return null;
	}

	public static InetAddress[] getLocalIPs() {
		return getLocalIPs(false);
	}

	public static InetAddress[] getLocalIPs(boolean includeloopback) {
		final ResizingArray<InetAddress> IPS = new ResizingArray<InetAddress>(1);

		getLocalIPs(IPS, true);

		return IPS.toArray(new InetAddress[IPS.length()]);
	}

	public static InetAddress getInetAddressByAddress(String address) {
		if(address == null) {
			return _LOOPBACK_ADDRESS_;
		}

		final ResizingArray<InetAddress> IPS = new ResizingArray<InetAddress>(1);

		getLocalIPs(IPS, true);

		for(int X = 0; X < IPS.length(); X++) {
			final String HOST = IPS.getAt(X).getHostAddress();

			if(HOST.equals(address)) {
				return IPS.getAt(X);
			}
		}

		return null;
	}

	public static void getLocalIPs(ResizingArray<InetAddress> inetaddresses) {
		getLocalIPs(inetaddresses, false);
	}

	/**
	 * Skips Interfaces that are not up
	 * 
	 * @param inetaddresses
	 */
	public static void getLocalIPs(ResizingArray<InetAddress> inetaddresses, boolean includeloopback) {
		Enumeration<NetworkInterface> Interfaces = null;
		try {
			Interfaces = NetworkInterface.getNetworkInterfaces();

			while(Interfaces.hasMoreElements()) {
				final NetworkInterface NET = Interfaces.nextElement();

				if(NET.isUp() || (NET.isLoopback() && includeloopback)) {
					getNetworkInterfaceIps(NET, inetaddresses);
				}
			}
		} catch(Exception e) {
			_LOG_.printError("Geting Network Interfaces");
			_LOG_.printError(e);
		}
	}

	public static String getMACAddress(NetworkInterface networkinterface) {
		return getMACAddress(networkinterface, ':');
	}

	public static String getMACAddress(NetworkInterface networkinterface, char separator) {
		if(networkinterface == null) {
			throw new RuntimeException("Variable[networkinterface] - Is Null");
		}

		try {
//			final BigInteger TO_HEX = new BigInteger(networkinterface.getHardwareAddress());

			String Temp = Convert.toHex(networkinterface.getHardwareAddress());//TO_HEX.toString(16)

			if(Temp != null) {
				final MyStringBuffer BUFFER = new MyStringBuffer(12 + 5);

				for(int X = 0; X < (12 - Temp.length()); X++) {
					BUFFER.append('0');
				}
				BUFFER.append(Temp);

				Temp = BUFFER.toString(true);

				if(Temp.length() == 12) {
					for(int X = 0; X < 12; X++) {
						if(X != 0 && X % 2 == 0) {
							BUFFER.append(separator);
						}

						BUFFER.append(Temp.charAt(X));
					}

					return BUFFER.toString();
				}
			}
		} catch(Exception e) {}

		return null;
	}

	public static NetworkInterface getDefaultNetworkInterface() {
		try {
			return NetworkInterface.getByName("eth0");
		} catch(Exception e) {
			_LOG_.printError(e);
		}

		return null;
	}

	public static NetworkInterface getNetworkInterfaceByName(String name) {
		if(name == null) {
			throw new RuntimeException("Variable[name] - Is Null");
		}

		try {
			return NetworkInterface.getByName(name);
		} catch(Exception e) {
			_LOG_.printError(e);
		}

		return null;
	}

	public static InetAddress[] getNetworkInterfaceIps() {
		final ResizingArray<InetAddress> IPS = new ResizingArray<InetAddress>(1);

		final NetworkInterface NET = getDefaultNetworkInterface();

		if(NET != null) {
			getNetworkInterfaceIps(NET, IPS);

			return IPS.toArray(new InetAddress[IPS.length()]);
		}

		return null;
	}

	public static InetAddress[] getNetworkInterfaceIps(NetworkInterface networkinterface) {
		final ResizingArray<InetAddress> IPS = new ResizingArray<InetAddress>(1);

		getNetworkInterfaceIps(networkinterface, IPS);

		return IPS.toArray(new InetAddress[IPS.length()]);
	}

	public static void getNetworkInterfaceIps(NetworkInterface networkinterface, ResizingArray<InetAddress> inetaddresses) {
		try {
			final Enumeration<InetAddress> ADDRESSES = networkinterface.getInetAddresses();

			while(ADDRESSES.hasMoreElements()) {
				final InetAddress ADDRESS = ADDRESSES.nextElement();

				inetaddresses.put(ADDRESS);
			}
		} catch(Exception e) {
			_LOG_.printError(e);
		}
	}

	public static NetworkInterface[] getNetworkInterfaces() {
		return getNetworkInterfaces(false, true);
	}

	public static NetworkInterface[] getNetworkInterfaces(boolean onlyisup, boolean includeloopback) {
		final ResizingArray<NetworkInterface> IPS = new ResizingArray<NetworkInterface>(1);

		getNetworkInterfaces(IPS, onlyisup, includeloopback);

		return IPS.toArray(new NetworkInterface[IPS.length()]);
	}

	public static void getNetworkInterfaces(ResizingArray<NetworkInterface> networkinterfaces) {
		getNetworkInterfaces(networkinterfaces, false, true);
	}

	public static void getNetworkInterfaces(ResizingArray<NetworkInterface> networkinterfaces, boolean onlyisup, boolean includeloopback) {
		Enumeration<NetworkInterface> Interfaces = null;
		try {
			Interfaces = NetworkInterface.getNetworkInterfaces();

			while(Interfaces.hasMoreElements()) {
				final NetworkInterface NET = Interfaces.nextElement();

				if((!onlyisup || (NET.isUp() && onlyisup)) || (NET.isLoopback() && includeloopback)) {
					networkinterfaces.put(NET);
				}
			}
		} catch(Exception e) {
			System.out.println("!!!ERROR!!! - Geting Network Interfaces");
		}
	}

	public static String getSubnetMask(NetworkInterface netinterface) {
		final List<InterfaceAddress> LIST = netinterface.getInterfaceAddresses();

		if(LIST.size() > 0) {
			return getSubnetMask(LIST.get(0));
		}

		return null;
	}

	public static String getSubnetMask(InterfaceAddress interfaceaddress) {
		switch(interfaceaddress.getNetworkPrefixLength()) {
			case 8:
				return "255.0.0.0";
			case 16:
				return "255.255.0.0";
			case 24:
				return "255.255.255.0";
			case 25:
				return "255.255.255.128";
			case 26:
				return "255.255.255.192";
			case 27:
				return "255.255.255.224";
			case 28:
				return "255.255.255.240";
			case 29:
				return "255.255.255.248";
			case 30:
				return "255.255.255.252";
			case 31:
				return "255.255.255.254";
			case 32:
				return "255.255.255.255";
		}

		return null;
	}

	public static void checkPort(int port) {
		if(!NetUtil.validPort(port)) {
			throw new RuntimeException("Invalid Port: " + port);
		}
	}

	/**
	 * context = SSLContext.getInstance("SSL"); if(trustManagers == null ||
	 * trustManagers.length == 0) trustManagers = new TrustManager[] { new
	 * EasyX509TrustManager(null) }; context.init(null, trustManagers, null);
	 * context.getClientSessionContext().setSessionTimeout(0);
	 * context.getServerSessionContext().setSessionTimeout(0); factory =
	 * context.getSocketFactory();
	 */
}

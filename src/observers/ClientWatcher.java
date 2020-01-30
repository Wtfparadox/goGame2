package observers;

import goServer.GoClientHandler;

public interface ClientWatcher {

	void notifyRunnableEnd(GoClientHandler client, char reason, int number);
}

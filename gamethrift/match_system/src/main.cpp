// This autogenerated skeleton file illustrates how to build a server.
// You should copy it to another filename to avoid overwriting it.

#include "match_server/Match.h"
#include "save_server/Save.h"
#include <thrift/concurrency/ThreadManager.h>
#include <thrift/concurrency/ThreadFactory.h>
#include <thrift/protocol/TBinaryProtocol.h>
#include <thrift/server/TSimpleServer.h>
#include <thrift/server/TThreadPoolServer.h>
#include <thrift/server/TThreadedServer.h>
#include <thrift/transport/TServerSocket.h>
#include <thrift/transport/TSocket.h>
#include <thrift/transport/TTransportUtils.h>
#include <thrift/TToString.h>
#include <iostream>
#include <thread>
#include <condition_variable>
#include <queue>
#include <mutex>
#include <unistd.h>
#include <stdexcept>
#include <sstream>
using namespace std;


using namespace ::apache::thrift;
using namespace apache::thrift::concurrency;
using namespace ::apache::thrift::protocol;
using namespace ::apache::thrift::transport;
using namespace ::apache::thrift::server;

using namespace ::match_service;
using namespace ::save_service;

struct Task {
	match_service::User user;
	string type;
};

struct MessageQueue {
	queue<Task> q;
	mutex m;
	condition_variable cv;
}message_queue;


class Pool {
	public:
		void save_result(User a, User b) {
			printf("Match Result: %d %d\n", a.id, b.id);

			std::shared_ptr<TTransport> socket(new TSocket("127.0.0.1", 8000));
			std::shared_ptr<TTransport> transport(new TBufferedTransport(socket));
			std::shared_ptr<TProtocol> protocol(new TBinaryProtocol(transport));
			SaveClient client(protocol);

			try {
				transport->open();

				int res = client.save_data("root", "xxxxxxxx", a, b);  // md5 进行解密操作
				if (!res) puts("success");
				else puts("failed");

				transport->close();
			} catch (TException& tx) {
				cout << "ERROR: " << tx.what() << endl;
			}
		}
        bool check_match(uint32_t i, uint32_t j) {
            auto a = users[i], b = users[j];
            int dt = abs(a.score - b.score);
            int a_max_dif = wt[i] * 10;
            int b_max_dif = wt[j] * 10;
            return dt <= a_max_dif && dt <= b_max_dif;
        }
		void match() {
            for (uint32_t i = 0; i < wt.size(); i ++) wt[i] ++;
            
			while (users.size() > 1) {
				bool flag = true;
				for (uint32_t i = 0; i < users.size(); i ++) {
                    for (uint32_t j = i + 1; j < users.size(); j ++) {
                        if (check_match(i, j)) {
                            save_result(users[i], users[j]);

                            users.erase(users.begin() + j);
                            users.erase(users.begin() + i);

                            wt.erase(wt.begin() + j);
                            wt.erase(wt.begin() + i);
                            flag = false;
                            break;
                        }
                    }
					if (!flag) break;

				}
                if (flag) break;
			}
		}
		void add(User user) {
			users.push_back(user);
			wt.push_back(0);
		}
		void remove(User user) {
			for (uint32_t i = 0; i < users.size(); i ++) {
				if (users[i].id == user.id) {
					users.erase(users.begin() + i);
					wt.erase(wt.begin() + i);
					break;
				}
			}
		}
	private:
		vector<User> users;
		vector<int> wt;
}pool;

class MatchHandler : virtual public MatchIf {
	public:
		MatchHandler() {
			// Your initialization goes here
		}

		int32_t add_user(const User& user, const std::string& info) {
			// Your implementation goes here
			printf("add_user\n");

			unique_lock<mutex> lck(message_queue.m);
			message_queue.q.push({user, "add"});
			message_queue.cv.notify_all();
			return 0;
		}

		int32_t remove_user(const User& user, const std::string& info) {
			// Your implementation goes here
			printf("remove_user\n");
			unique_lock<mutex> lck(message_queue.m);
			message_queue.q.push({user, "remove"});
			message_queue.cv.notify_all();
			return 0;
		}
};


class MatchCloneFactory : virtual public MatchIfFactory {
	public:
		~MatchCloneFactory() override = default;
		MatchIf* getHandler(const ::apache::thrift::TConnectionInfo& connInfo) override
		{
			std::shared_ptr<TSocket> sock = std::dynamic_pointer_cast<TSocket>(connInfo.transport);
			return new MatchHandler;
		}
		void releaseHandler(MatchIf* handler) override {
			delete handler;
		}
};

void consume_task() {
	while (true) {
		unique_lock<mutex> lck(message_queue.m);
		if (message_queue.q.empty()) {
			pool.match();
            message_queue.cv.wait_for(lck, chrono::seconds(1), [] { return !message_queue.q.empty(); });
		} else {
			auto task = message_queue.q.front();
			message_queue.q.pop();
			lck.unlock();
			// task
			if (task.type == "add") pool.add(task.user);
            else if (task.type == "remove") pool.remove(task.user);
		}
	}
}

int main(int argc, char **argv) {
	TThreadedServer server(
			std::make_shared<MatchProcessorFactory>(std::make_shared<MatchCloneFactory>()),
			std::make_shared<TServerSocket>(9090), //port
			std::make_shared<TBufferedTransportFactory>(),
			std::make_shared<TBinaryProtocolFactory>());

	cout << "Start the Server:" << endl;
	thread matching_thread(consume_task);
	server.serve();
	return 0;
}


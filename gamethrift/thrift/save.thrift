include "match.thrift"

namespace cpp save_service 

service Save {
    i32 save_data(1: string username, 2: string password, 3: match.User user1, 4: match.User user2);
}
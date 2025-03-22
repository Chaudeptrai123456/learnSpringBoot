package se.chau.microservices.core.message.Service;

import com.google.firebase.database.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import se.chau.microservices.api.core.Message.MessageText;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
public class FirebaseService {
    @Autowired
    private DatabaseReference databaseReference;
    private static final Logger log = LoggerFactory.getLogger(FirebaseService.class);


    public FirebaseService(DatabaseReference databaseReference) {
        this.databaseReference = databaseReference;
    }

    public Mono<Void> sendMessage(String roomId, String sender, String message, String type) {
        log.info("test " + Boolean.TRUE.equals(this.isUserInRoom(roomId, sender).block()));
        if (Boolean.TRUE.equals(this.isUserInRoom(roomId, sender).block())) {
            String messageId = databaseReference.child(roomId).push().getKey();
            if (messageId == null) return Mono.empty();

            Map<String, Object> messageData = new HashMap<>();
            messageData.put("sender", sender);
            messageData.put("message", message);
            messageData.put("timestamp", System.currentTimeMillis());
            messageData.put("type", type); // text, image, file
            messageData.put("seenBy", new HashMap<>());

            return Mono.create(sink -> {
                databaseReference.child(roomId).child(messageId)
                        .setValue(messageData, (error, ref) -> {
                            if (error != null) {
                                sink.error(new RuntimeException("Lỗi khi lưu tin nhắn: " + error.getMessage()));
                            } else {
                                sink.success();
                            }
                        });
            });
        } else {
            return null;
        }

    }
    public Mono<Void> createRoom(String roomId, String username) {
        return Mono.create(sink -> {
            Map<String, Object> roomData = new HashMap<>();
            roomData.put("createdAt", System.currentTimeMillis());
            roomData.put("host", username); // Chủ phòng
            roomData.put("guests", new HashMap<>()); // Danh sách khách ban đầu trống

            log.info("Creating room with ID: " + roomId);

            databaseReference.child(roomId)
                    .setValue(roomData, (error, ref) -> {
                        if (error == null) {
                            sink.success();
                        } else {
                            sink.error(new RuntimeException("Failed to create room: " + error.getMessage()));
                        }
                    });
        });
    }
    public Mono<Boolean> addParticipant(String roomId, String hostId, String newUserId) {
        return isUserHost(roomId, hostId) // Kiểm tra nếu hostId có phải host không
                .flatMap(isHost -> {
                    if (isHost) {
                        return Mono.create(sink -> {
                            databaseReference.child(roomId).child("participants").child(newUserId)
                                    .setValueAsync(true);
                        });
                    } else {
                        return Mono.error(new RuntimeException("Bạn không phải host, không thể thêm participant!"));
                    }
                });
    }
    public Mono<Boolean> isUserHost(String roomId, String userId) {
        return Mono.create(sink -> {
            databaseReference.child(roomId).child("host")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            if (snapshot.exists() && userId.equals(snapshot.getValue(String.class))) {
                                sink.success(true); // Là host
                            } else {
                                sink.success(false); // Không phải host
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            sink.error(new RuntimeException("Firebase error: " + error.getMessage()));
                        }
                    });
        });
    }
    public Mono<List<MessageText>> getLast20Messages(String roomId,String userId) {
        if (Boolean.TRUE.equals(this.isUserInRoom(roomId, userId).block())) {
            return Mono.create(sink -> {
                databaseReference.child(roomId)
                        .orderByChild("timestamp")
                        .limitToLast(20)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot snapshot) {
                                List<MessageText> messages = new ArrayList<>();
                                for (DataSnapshot data : snapshot.getChildren()) {
                                    MessageText message = data.getValue(MessageText.class);
                                    if (message != null) {
                                        messages.add(message);
                                    }
                                }
                                sink.success(messages);
                            }

                            @Override
                            public void onCancelled(DatabaseError error) {
                                sink.error(new RuntimeException("Failed to fetch messages: " + error.getMessage()));
                            }
                        });
            });
        } else {
            return null;
        }

    }

    public Mono<Query> searchMessages(String roomId, String keyword) {
        return Mono.just(databaseReference.child(roomId).orderByChild("message").startAt(keyword).endAt(keyword + "\uf8ff"));
    }

    public Mono<List<MessageText>> getMessagesByDate(String roomId, String userId) {
        return isUserInRoom(roomId, userId)
                .flatMap(isInRoom -> {
                    if (Boolean.TRUE.equals(isInRoom)) {
                        return Mono.create(sink -> {
                            databaseReference.child(roomId)
                                    .orderByChild("timestamp")
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot snapshot) {
                                            List<MessageText> messages = new ArrayList<>();
                                            for (DataSnapshot data : snapshot.getChildren()) {
                                                MessageText message = data.getValue(MessageText.class);
                                                messages.add(message);
                                            }
                                            sink.success(messages);
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError error) {
                                            sink.error(new RuntimeException("Lỗi Firebase: " + error.getMessage()));
                                        }
                                    });
                        });
                    } else {
                        return Mono.error(new RuntimeException("Người dùng không có quyền truy cập phòng này!"));
                    }
                });
    }



    public long pareseDate(int year,int month,int day,int h,int min){
        return  LocalDateTime.of(year, month, day, h, min) // 20/03/2025 15:30
                .atZone(ZoneId.systemDefault()) // Chuyển về múi giờ hệ thống
                .toInstant()
                .toEpochMilli();
    }
    public Mono<Boolean> isUserInRoom(String roomId, String userId) {
        return Mono.create(sink -> {
            databaseReference.child(roomId).child("host") // Lấy host của phòng
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            if (snapshot.exists() && userId.equals(snapshot.getValue(String.class))) {
                                sink.success(true); // Nếu user là host => Được gửi tin
                            } else {
                                // Nếu không phải host, kiểm tra participants
                                databaseReference.child(roomId).child("participants").child(userId)
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot participantSnapshot) {
                                                if (participantSnapshot.exists()) {
                                                    sink.success(true); // Nếu user có trong participants => Được gửi tin
                                                } else {
                                                    sink.success(false); // Không phải host, không phải participant => Không được gửi tin
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError error) {
                                                sink.error(new RuntimeException("Firebase error: " + error.getMessage()));
                                            }
                                        });
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            sink.error(new RuntimeException("Firebase error: " + error.getMessage()));
                        }
                    });
        });
    }


    public Mono<Void> markAsRead(String roomId, String messageId, String userId) {
        return Mono.fromRunnable(() -> databaseReference.child(roomId).child(messageId).child("seenBy").child(userId).setValueAsync(true));
    }
}

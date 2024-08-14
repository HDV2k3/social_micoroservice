package com.example.connect_service.Dto;


import com.example.connect_service.Entity.Enum.ConnectStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConnectResponseRequest {
    String connectId;       // ID của kết nối cần cập nhật
    ConnectStatus status;   // Trạng thái mới
}
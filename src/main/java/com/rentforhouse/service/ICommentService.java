package com.rentforhouse.service;

import com.rentforhouse.dto.CommentDto;
import com.rentforhouse.payload.request.CommentRequest;
import com.rentforhouse.payload.response.CommentResponse;

public interface ICommentService {
	CommentDto save(CommentRequest request);
	Boolean delete(Long id);
}

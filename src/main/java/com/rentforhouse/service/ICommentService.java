package com.rentforhouse.service;

import com.rentforhouse.payload.request.CommentRequest;
import com.rentforhouse.payload.response.CommentResponse;

public interface ICommentService {
	CommentResponse save(CommentRequest request);
	Boolean delete(Long id);
}

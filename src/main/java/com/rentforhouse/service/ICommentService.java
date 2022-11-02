package com.rentforhouse.service;

import org.springframework.stereotype.Service;

import com.rentforhouse.payload.request.CommentRequest;
import com.rentforhouse.payload.response.CommentResponse;

@Service
public interface ICommentService {
	CommentResponse save(CommentRequest request);
}

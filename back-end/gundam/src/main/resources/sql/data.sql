INSERT INTO t_authority (`authority_name`) values ('ROLE_USER');
INSERT INTO t_authority (`authority_name`) VALUES ('ROLE_MANAGER');
INSERT INTO t_authority (`authority_name`) values ('ROLE_ADMIN');


INSERT INTO t_user (`user_name`, `password`) VALUES ('customer@naver.com', '$2a$10$rVT/tNSwxPt7j4eAI1NFrO8NA3JRnhvhyIIHQSdo.vusLrIBXFZ.6');
INSERT INTO t_user (`user_name`, `password`) VALUES ('admin@naver.com', '$2a$10$rVT/tNSwxPt7j4eAI1NFrO8NA3JRnhvhyIIHQSdo.vusLrIBXFZ.6');
INSERT INTO t_user (`user_name`, `password`) VALUES ('manager@naver.com', '$2a$10$rVT/tNSwxPt7j4eAI1NFrO8NA3JRnhvhyIIHQSdo.vusLrIBXFZ.6');


INSERT INTO t_user_authority (`user_no`, `authority_name`) VALUES (1, 'ROLE_USER');
INSERT INTO t_user_authority (`user_no`, `authority_name`) VALUES (2, 'ROLE_USER');
INSERT INTO t_user_authority (`user_no`, `authority_name`) VALUES (2, 'ROLE_MANAGER');
INSERT INTO t_user_authority (`user_no`, `authority_name`) VALUES (3, 'ROLE_USER');
INSERT INTO t_user_authority (`user_no`, `authority_name`) VALUES (3, 'ROLE_MANAGER');
INSERT INTO t_user_authority (`user_no`, `authority_name`) VALUES (3, 'ROLE_ADMIN');
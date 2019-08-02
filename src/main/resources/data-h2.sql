INSERT INTO editor(id, first_name, last_name, email, password, role, phone, active, version, created_at, updated_at) 
	VALUES(1, 'Michał', 'Niewiadomy', 'niewiadomy@test.pl', 'test', 'EDITOR', '', true, 1, now(), default);
INSERT INTO editor(id, first_name, last_name, email, password, role, phone, active, version, created_at, updated_at) 
	VALUES(2, 'Ewelina', 'Test', 'etest@test.pl', 'test', 'EDITOR', '', true, 1, now(), default);
INSERT INTO editor(id, first_name, last_name, email, password, role, phone, active, version, created_at, updated_at) 
	VALUES(3, 'Aaron', 'Rodgers', 'rodgers@packers.com', 'test', 'EDITOR', '', true, 1, now(), default);
INSERT INTO editor(id, first_name, last_name, email, password, role, phone, active, version, created_at, updated_at) 
	VALUES(4, 'admin', 'Michal', 'admin@test.pl', 'test', 'ADMIN', '', true, 1, now(), default);
	
INSERT INTO article(id, title, link, display_position, main_menu, parent_id, version, created_at, updated_at)
	VALUES(1, 'Lorem ipsum dolor sit amet', 'strona-glowna', 1, true, null, 1, now(), default);
INSERT INTO article(id, title, link, display_position, main_menu, parent_id, version, created_at, updated_at)
	VALUES(2, 'Artykuł Rodzic', 'artykul-rodzic', 3, true, null, 1, now(), default);
INSERT INTO article(id, title, link, display_position, main_menu, parent_id, version, created_at, updated_at)
	VALUES(3, 'Dziecko nr 1', 'dziecko-nr-1', 2, false, 2, 1, '2019-04-26T07:15:55.124', default);
INSERT INTO article(id, title, link, display_position, main_menu, parent_id, version, created_at, updated_at)
	VALUES(4, 'Dziecko nr 2', 'dziecko-nr-2', 1, false, 2, 1, now(), default);
INSERT INTO article(id, title, link, display_position, main_menu, parent_id, version, created_at, updated_at)
	VALUES(5, 'Artykuł bez dzieci', 'artykul-bez-dzieci', 2, true, null, 1, now(), default);
INSERT INTO article(id, title, link, display_position, main_menu, parent_id, version, created_at, updated_at)
	VALUES(6, 'Dziecko nr 2_1', 'dziecko-nr-2-1', 1, false, 4, 1, now(), default);
INSERT INTO article(id, title, link, display_position, main_menu, parent_id, version, created_at, updated_at)
	VALUES(7, 'Polityka prywatności', 'polityka-prywatnosci', 99, false, null, 1, now(), default);
INSERT INTO article(id, title, link, display_position, main_menu, parent_id, version, created_at, updated_at)
	VALUES(8, 'Polityka cookies', 'polityka-cookies', 100, false, null, 1, now(), default);
	
INSERT INTO content_history(id, article_id, content, editor_id, version, created_at, updated_at)
	VALUES(1, 1, 'Treść strony głównej', 1, 1, now(), default);
INSERT INTO content_history(id, article_id, content, editor_id, version, created_at, updated_at)
	VALUES(2, 1, 'Treść strony głównej po edycji', 2, 1, now(), default);
INSERT INTO content_history(id, article_id, content, editor_id, version, created_at, updated_at)
	VALUES(3, 2, '', 2, 1, now(), default);
INSERT INTO content_history(id, article_id, content, editor_id, version, created_at, updated_at)
	VALUES(4, 3, 'Artykuł dziecko 1 v1', 1, 1, '2019-04-26T07:15:55.124', default);
INSERT INTO content_history(id, article_id, content, editor_id, version, created_at, updated_at)
	VALUES(5, 3, 'Artykuł dziecko 1 v2', 1, 1, '2019-04-26T07:16:55.124', default);
INSERT INTO content_history(id, article_id, content, editor_id, version, created_at, updated_at)
	VALUES(6, 4, 'Artykuł dziecko 2 v1', 3, 1, now(), default);
INSERT INTO content_history(id, article_id, content, editor_id, version, created_at, updated_at)
	VALUES(7, 5, 'Artykuł bez dzieci v1', 2, 1, now(), default);	
INSERT INTO content_history(id, article_id, content, editor_id, version, created_at, updated_at)
	VALUES(8, 5, 'Artykuł bez dzieci v2', 3, 1, now(), default);	
INSERT INTO content_history(id, article_id, content, editor_id, version, created_at, updated_at)
	VALUES(9, 5, 'Artykuł bez dzieci v3', 1, 1, now(), default);
INSERT INTO content_history(id, article_id, content, editor_id, version, created_at, updated_at)
	VALUES(10, 6, 'Artykuł dziecko 2_1 v1', 2, 1, now(), default);
INSERT INTO content_history(id, article_id, content, editor_id, version, created_at, updated_at)
	VALUES(12, 7, '<p>Standardowa formuła polityki prywatności.</p>', 1, 1, '2019-08-02T10:53:55.124', default);
INSERT INTO content_history(id, article_id, content, editor_id, version, created_at, updated_at)
	VALUES(13, 8, '<p>Standardowa formuła polityki cookies.</p>', 1, 1, '2019-08-02T10:54:11.124', default);
	
INSERT INTO attachment(id, article_id, file_name, display_name, extension, size, display_position, added_by, version, created_at, updated_at)
	VALUES(1, 2, 'zal_nr_1.pdf', 'zał nr 1', 'pdf', 321, 2, 1, 1, now(), default);
INSERT INTO attachment(id, article_id, file_name, display_name, extension, size, display_position, added_by, version, created_at, updated_at)
	VALUES(2, 2, 'zal_nr_2.pdf', 'zał nr 2', 'pdf', 4321, 1, 1, 1, now(), default);
INSERT INTO attachment(id, article_id, file_name, display_name, extension, size, display_position, added_by, version, created_at, updated_at)
	VALUES(3, 5, 'zal_art_bez_dzieci_1.odt', 'wniosek - grupa A', 'odt', 4321, 1, 2, 1, now(), default);
INSERT INTO attachment(id, article_id, file_name, display_name, extension, size, display_position, added_by, version, created_at, updated_at)
	VALUES(4, 5, 'zal_art_bez_dzieci_2.odt', 'wniosek - grupa B', 'odt', 4321, 2, 2, 1, now(), default);
INSERT INTO attachment(id, article_id, file_name, display_name, extension, size, display_position, added_by, version, created_at, updated_at)
	VALUES(5, 5, 'zal_art_bez_dzieci_3.odt', 'wniosek - grupa C', 'odt', 4321, 3, 2, 1, now(), default);
	
INSERT INTO attachment_history(id, article_id, editor_id, log, version, created_at, updated_at)
	VALUES(1, 5, 2, 'dodano wniosek grupa A', 1, '2019-04-26T07:16:55.124', default);
INSERT INTO attachment_history(id, article_id, editor_id, log, version, created_at, updated_at)
	VALUES(2, 5, 2, 'dodano wniosek grupa B', 1, '2019-04-26T07:16:50.124', default);
INSERT INTO attachment_history(id, article_id, editor_id, log, version, created_at, updated_at)
	VALUES(3, 5, 1, 'usunięto wniosek grupa B', 1, '2019-04-26T07:16:52.124', default);
INSERT INTO attachment_history(id, article_id, editor_id, log, version, created_at, updated_at)
	VALUES(4, 5, 1, 'dodano wniosek grupa B', 1, '2019-04-26T07:16:57.124', default);
INSERT INTO attachment_history(id, article_id, editor_id, log, version, created_at, updated_at)
	VALUES(5, 5, 1, 'dodano wniosek grupa C', 1, '2019-04-26T07:16:59.124', default);
	
	
	
package net.jewczuk.openbip.constants;

public class ExceptionsMessages {
	
	public static final String LINK_EXISTS = "Podany link już istnieje w bazie danych";
	public static final String NO_TITLE = "Tytuł nie może być pusty!";
	public static final String LINK_MIN_LENGTH = "Minimalna długość linku utworzonego na podstawie tytułu to: " 
													+ ApplicationProperties.MIN_LINK_LENGHT;
	
	public static final String INVALID_EDITOR_ID = "Podany redaktor nie istnieje!";
	
	public static final String PINNING_TO_SELF = "Nie można przypiąć artykułu do siebie!";
	public static final String PINNED_TO_ANOTHER = "Artykuł jest już przypięty!";
	
	public static final String ATTACHMENT_EXISTS = "Plik o podanej nazwie już istnieje w bazie danych";
	public static final String ATTACHMENT_NOT_EXISTS = "Plik o podanej nazwie nie istnieje w bazie danych";
	public static final String ATTACHMENT_INVALID_TITLE = "Minimalna długość wyświetlanej nazwy to: "
													+ ApplicationProperties.MIN_FILENAME_LENGHT;
	public static final String ATTACHMENT_NO_FILE = "Nie wybrano pliku!";
	public static final String ATTACHMENT_INVALID_EXTENSION = "Plik nie posiada rozszerzenia";
	public static final String ATTACHMENT_ERROR_COPING = "Wystąpił błąd podczas kopiowania pliku";
	public static final String ATTACHMENT_ERROR_DELETING = "Wystąpił błąd podczas usuwania pliku";
	public static final String TITLE_MIN_LENGTH = "Minimalna długość tytułu to: " 
													+ ApplicationProperties.MIN_TITLE_LENGHT;
	
}

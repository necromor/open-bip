package net.jewczuk.openbip.constants;

public class ExceptionsMessages {
	
	public static final String LINK_EXISTS = "Podany link już istnieje w bazie danych";
	public static final String NO_TITLE = "Tytuł nie może być pusty!";
	public static final String LINK_MIN_LENGTH = "Link utworzony na podstawie tytułu musi mieć przynajmniej " 
													+ ApplicationProperties.MIN_LINK_LENGHT + " znaki!";
	
	public static final String INVALID_EDITOR_ID = "Podany redaktor nie istnieje!";
	
	public static final String PINNING_TO_SELF = "Nie można przypiąć artykułu do siebie!";
	public static final String PINNED_TO_ANOTHER = "Artykuł jest już przypięty!";
	
}

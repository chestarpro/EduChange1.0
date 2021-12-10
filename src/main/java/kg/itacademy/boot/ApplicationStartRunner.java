package kg.itacademy.boot;

import kg.itacademy.entity.*;
import kg.itacademy.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class ApplicationStartRunner implements CommandLineRunner {

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserBalanceRepository userBalanceRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private LessonRepository lessonRepository;

    @Override
    public void run(String... args) throws Exception {
        User adminChyngyz = new User();
        adminChyngyz.setFullName("Chyngyz");
        adminChyngyz.setUsername("admin");
        adminChyngyz.setEmail("admin@gmail.com");
        adminChyngyz.setPassword("admin");
        adminChyngyz.setIsActive(1L);
        String encodedPassword = passwordEncoder.encode(adminChyngyz.getPassword());
        adminChyngyz.setPassword(encodedPassword);
        userRepository.save(adminChyngyz);

        UserRole roleAdmin = new UserRole();
        roleAdmin.setRoleName("ROLE_ADMIN");
        roleAdmin.setUser(adminChyngyz);
        userRoleRepository.save(roleAdmin);

        userBalanceRepository.save(UserBalance.builder()
                .user(adminChyngyz)
                .balance(new BigDecimal(1000_000))
                .build());

        User userAlinur = new User();
        userAlinur.setFullName("Alinur");
        userAlinur.setUsername("alinur05");
        userAlinur.setEmail("dubaiprinc247@gmailcom");
        userAlinur.setPassword("123456");
        userAlinur.setIsActive(1L);
        String encodedPasswordUser1 = passwordEncoder.encode(userAlinur.getPassword());
        userAlinur.setPassword(encodedPasswordUser1);
        userRepository.save(userAlinur);

        UserRole roleUser1 = new UserRole();
        roleUser1.setRoleName("ROLE_USER");
        roleUser1.setUser(userAlinur);
        userRoleRepository.save(roleUser1);

        userBalanceRepository.save(UserBalance.builder()
                .user(userAlinur)
                .balance(new BigDecimal(1000_000))
                .build());

        List<Category> categories = new ArrayList<>();
        categories.add(Category.builder().categoryName("sport").build());
        categories.add(Category.builder().categoryName("art").build());
        categories.add(Category.builder().categoryName("programming").build());
        categories.add(Category.builder().categoryName("driving").build());
        categories.add(Category.builder().categoryName("languages").build());

        categoryRepository.saveAll(categories);

        List<Course> courses = new ArrayList<>();
        courses.add(Course.builder()
                .category(categories.get(2))
                .courseName("java-разработчик")
                .email("hello@it-academy.kg")
                .phoneNumber("+996 707 745 232")
                .courseShortInfo("\n" +
                        "Получи профессию\n" +
                        "Java-разработчика\n" +
                        "за 9 месяцев.\n" +
                        "С нуля.")
                .courseInfoTitle("Что такое Java?")
                .courseInfo(getJavaCourseInfo())
                .courseInfoUrl("http://itacademy.tilda.ws/java")
                .price(new BigDecimal(10000))
                .user(adminChyngyz)
                .build());

        courses.add(Course.builder()
                .category(categories.get(2))
                .courseName("frontend-разработчик")
                .email("hello@it-academy.kg")
                .phoneNumber("+996 707 745 232")
                .courseShortInfo("\n" +
                        "Получи профессию\n" +
                        "Frontend-разработчика\n" +
                        "за 9 месяцев.\n" +
                        "С нуля.")
                .courseInfoTitle("Что такое Frontend?")
                .courseInfo(getFrontendCourseInfo())
                .courseInfoUrl("http://itacademy.tilda.ws/frontend")
                .price(new BigDecimal(10000))
                .user(userAlinur)
                .build());

        courses.add(Course.builder()
                .category(categories.get(1))
                .courseName("базовый курс: рисунок и живопись 2.0")
                .email("art@gmailcom")
                .phoneNumber("+996 707 223 327")
                .courseShortInfo("Вся основная теория и всего две техники! Обновленная версия курса!")
                .courseInfoTitle("Курс для всех, кто хочет")
                .courseInfo(getArtCourseInfo())
                .courseInfoUrl("https://www.hudozhnik.online/basic_course?_ga=2.99022302.1512450000.1637175476-2057833546.1637175476")
                .price(new BigDecimal(3000))
                .user(adminChyngyz)
                .build());

        courseRepository.saveAll(courses);

        lessonRepository.saveAll(getStartJavaLessons(courses.get(0)));
        lessonRepository.saveAll(getStartFrontendLessons(courses.get(1)));
        lessonRepository.saveAll(getStartArtLessons(courses.get(2)));
    }

    private List<Lesson> getStartArtLessons(Course art) {
        List<Lesson> artLessons = new ArrayList<>();

        artLessons.add(Lesson.builder()
                .lessonInfo("Как рисовать (нарисовать) глаза карандашом - обучающий урок (основы + такой глаз).")
                .lessonUrl("https://www.youtube.com/watch?v=2lS6f9N0nKs&list=PLMjDNhoE9yya9L8Rca-YSsFKUzU9InkCP&index=1")
                .course(art)
                .build());

        artLessons.add(Lesson.builder()
                .lessonInfo("Как выбрать карандаш для рисования (какие карандаши нужны).")
                .lessonUrl("https://www.youtube.com/watch?v=ycMmuQvkMtU&list=PLMjDNhoE9yya9L8Rca-YSsFKUzU9InkCP&index=2")
                .course(art)
                .build());

        artLessons.add(Lesson.builder()
                .lessonInfo("Какую бумагу выбрать для рисования (виды, свойства, рекомендации).")
                .lessonUrl("https://www.youtube.com/watch?v=AsdIkLSyolg&list=PLMjDNhoE9yya9L8Rca-YSsFKUzU9InkCP&index=3")
                .course(art)
                .build());

        artLessons.add(Lesson.builder()
                .lessonInfo("Материалы для рисования карандашом - как их выбрать (часть 3).")
                .lessonUrl("https://www.youtube.com/watch?v=YjUpZ7I2C6U&list=PLMjDNhoE9yya9L8Rca-YSsFKUzU9InkCP&index=4")
                .course(art)
                .build());

        artLessons.add(Lesson.builder()
                .lessonInfo("Как делать штриховку карандашом - теоретические основы.")
                .lessonUrl("https://www.youtube.com/watch?v=3SV4PRhgRA8&list=PLMjDNhoE9yya9L8Rca-YSsFKUzU9InkCP&index=5")
                .course(art)
                .build());

        artLessons.add(Lesson.builder()
                .lessonInfo("ШТРИХОВКА, ТУШЕВКА И РАСТУШЕВКА - практический обучающий урок.")
                .lessonUrl("https://www.youtube.com/watch?v=WkOQ5UX_66w&list=PLMjDNhoE9yya9L8Rca-YSsFKUzU9InkCP&index=6")
                .course(art)
                .build());

        artLessons.add(Lesson.builder()
                .lessonInfo("Как нарисовать объем! Основы светотени в рисунке карандашом.")
                .lessonUrl("https://www.youtube.com/watch?v=hg_RC4LrUiw&list=PLMjDNhoE9yya9L8Rca-YSsFKUzU9InkCP&index=7")
                .course(art)
                .build());

        artLessons.add(Lesson.builder()
                .lessonInfo("Как рисовать (нарисовать) губы карандашом - обучающий урок.")
                .lessonUrl("https://www.youtube.com/watch?v=nOYwEZ8vqT4&list=PLMjDNhoE9yya9L8Rca-YSsFKUzU9InkCP&index=8")
                .course(art)
                .build());

        artLessons.add(Lesson.builder()
                .lessonInfo("Как рисовать (нарисовать) нос карандашом - обучающий урок.")
                .lessonUrl("https://www.youtube.com/watch?v=2YlONB8iH4g&list=PLMjDNhoE9yya9L8Rca-YSsFKUzU9InkCP&index=9")
                .course(art)
                .build());

        artLessons.add(Lesson.builder()
                .lessonInfo("Как нарисовать такое ухо. Как рисовать уши карандашом - видео урок.")
                .lessonUrl("https://www.youtube.com/watch?v=5IXnYbwtacE&list=PLMjDNhoE9yya9L8Rca-YSsFKUzU9InkCP&index=10")
                .course(art)
                .build());

        artLessons.add(Lesson.builder()
                .lessonInfo("Как рисовать (нарисовать) волосы карандашом - обучающий урок.")
                .lessonUrl("https://www.youtube.com/watch?v=2-dZToxwcoc&list=PLMjDNhoE9yya9L8Rca-YSsFKUzU9InkCP&index=11")
                .course(art)
                .build());

        artLessons.add(Lesson.builder()
                .lessonInfo("Как нарисовать лицо человека. Построение и пропорции лица.")
                .lessonUrl("https://www.youtube.com/watch?v=q9Ha00E0Sqg&list=PLMjDNhoE9yya9L8Rca-YSsFKUzU9InkCP&index=12")
                .course(art)
                .build());

        return artLessons;
    }

    private List<Lesson> getStartFrontendLessons(Course frontend) {
        List<Lesson> frontendLessons = new ArrayList<>();

        frontendLessons.add(Lesson.builder()
                .lessonInfo("Введение в HTML. Урок 1 Введение")
                .lessonUrl("https://www.youtube.com/watch?v=AUQMy9-M7gg&list=PL_z4rXo1im3obzKry-jKZiUpwxf4n0Nrf")
                .course(frontend)
                .build());

        frontendLessons.add(Lesson.builder()
                .lessonInfo("Введение в HTML. Урок 2 Раскройка шаблона")
                .lessonUrl("https://www.youtube.com/watch?v=Bt6QsD_-U8U&list=PL_z4rXo1im3obzKry-jKZiUpwxf4n0Nrf&index=2")
                .course(frontend)
                .build());

        frontendLessons.add(Lesson.builder()
                .lessonInfo("Введение в HTML. Урок 3 Структура страницы")
                .lessonUrl("https://www.youtube.com/watch?v=cI4WAUt4BeQ&list=PL_z4rXo1im3obzKry-jKZiUpwxf4n0Nrf&index=3")
                .course(frontend)
                .build());

        frontendLessons.add(Lesson.builder()
                .lessonInfo("Введение в HTML. Урок 4 Первая web страница")
                .lessonUrl("https://www.youtube.com/watch?v=iWuPx5l7NyQ&list=PL_z4rXo1im3obzKry-jKZiUpwxf4n0Nrf&index=4")
                .course(frontend)
                .build());

        frontendLessons.add(Lesson.builder()
                .lessonInfo("Введение в HTML. Урок 5 Создание HTML списков")
                .lessonUrl("https://www.youtube.com/watch?v=PodgGBEP-r8&list=PL_z4rXo1im3obzKry-jKZiUpwxf4n0Nrf&index=5")
                .course(frontend)
                .build());

        frontendLessons.add(Lesson.builder()
                .lessonInfo("Введение в HTML. Урок 6 Создание HTML ссылок")
                .lessonUrl("https://www.youtube.com/watch?v=IcRA3AQCi8A&list=PL_z4rXo1im3obzKry-jKZiUpwxf4n0Nrf&index=6")
                .course(frontend)
                .build());

        frontendLessons.add(Lesson.builder()
                .lessonInfo("Введение в HTML. Урок 7 Добавление изображений на сайт")
                .lessonUrl("https://www.youtube.com/watch?v=uZKocgTHg6k&list=PL_z4rXo1im3obzKry-jKZiUpwxf4n0Nrf&index=7")
                .course(frontend)
                .build());

        frontendLessons.add(Lesson.builder()
                .lessonInfo("Введение в HTML. Урок 8 Создание HTML таблицы")
                .lessonUrl("https://www.youtube.com/watch?v=MyE86D91w7g&list=PL_z4rXo1im3obzKry-jKZiUpwxf4n0Nrf&index=8")
                .course(frontend)
                .build());

        frontendLessons.add(Lesson.builder()
                .lessonInfo("Введение в HTML. Урок 9 Форматирование текста с помощью HTML тегов")
                .lessonUrl("https://www.youtube.com/watch?v=mW7b8GSVVqw&list=PL_z4rXo1im3obzKry-jKZiUpwxf4n0Nrf&index=9")
                .course(frontend)
                .build());

        frontendLessons.add(Lesson.builder()
                .lessonInfo("Введение в HTML. Урок 10 HTML Формы")
                .lessonUrl("https://www.youtube.com/watch?v=aitJXkc8PeI&list=PL_z4rXo1im3obzKry-jKZiUpwxf4n0Nrf&index=10")
                .course(frontend)
                .build());

        return frontendLessons;
    }

    private List<Lesson> getStartJavaLessons(Course java) {
        List<Lesson> javaLessons = new ArrayList<>();

        javaLessons.add(Lesson.builder()
                .lessonInfo("Уроки Java для начинающих | #1 - Программирование на Java")
                .lessonUrl("https://www.youtube.com/watch?v=Zxpz5tRrUvU&list=PL0lO_mIqDDFW2xXiWSfjT7hEdOUZHVNbK")
                .course(java)
                .build());

        javaLessons.add(Lesson.builder()
                .lessonInfo("Уроки Java для начинающих | #2 - Установка Java JDK и IntelliJ IDEA")
                .lessonUrl("https://www.youtube.com/watch?v=AM_WxaR6Spc&list=PL0lO_mIqDDFW2xXiWSfjT7hEdOUZHVNbK&index=2")
                .course(java)
                .build());

        javaLessons.add(Lesson.builder()
                .lessonInfo("Уроки Java для начинающих | #3 - Hello World!")
                .lessonUrl("https://www.youtube.com/watch?v=IQTEziR82so&list=PL0lO_mIqDDFW2xXiWSfjT7hEdOUZHVNbK&index=3")
                .course(java)
                .build());

        javaLessons.add(Lesson.builder()
                .lessonInfo("Уроки Java для начинающих | #4 - Переменные")
                .lessonUrl("https://www.youtube.com/watch?v=Y__Ns7FS5lA&list=PL0lO_mIqDDFW2xXiWSfjT7hEdOUZHVNbK&index=4")
                .course(java)
                .build());

        javaLessons.add(Lesson.builder()
                .lessonInfo("Уроки Java для начинающих | #5 - Данные от пользователя")
                .lessonUrl("https://www.youtube.com/watch?v=kD5ZDwdtJ10&list=PL0lO_mIqDDFW2xXiWSfjT7hEdOUZHVNbK&index=5")
                .course(java)
                .build());

        javaLessons.add(Lesson.builder()
                .lessonInfo("Уроки Java для начинающих | #6 - Математические операции")
                .lessonUrl("https://www.youtube.com/watch?v=W6A4DEr7XW4&list=PL0lO_mIqDDFW2xXiWSfjT7hEdOUZHVNbK&index=6")
                .course(java)
                .build());

        javaLessons.add(Lesson.builder()
                .lessonInfo("Уроки Java для начинающих | #7 - Условные операторы")
                .lessonUrl("https://www.youtube.com/watch?v=Eao7VNpv1f0&list=PL0lO_mIqDDFW2xXiWSfjT7hEdOUZHVNbK&index=7")
                .course(java)
                .build());

        javaLessons.add(Lesson.builder()
                .lessonInfo("Уроки Java для начинающих | #8 - Циклы (For, While, Do while)")
                .lessonUrl("https://www.youtube.com/watch?v=y3Xu5o6Pxfg&list=PL0lO_mIqDDFW2xXiWSfjT7hEdOUZHVNbK&index=8")
                .course(java)
                .build());

        javaLessons.add(Lesson.builder()
                .lessonInfo("Уроки Java для начинающих | #9 - Массивы")
                .lessonUrl("https://www.youtube.com/watch?v=qiUfLIbbedw&list=PL0lO_mIqDDFW2xXiWSfjT7hEdOUZHVNbK&index=9")
                .course(java)
                .build());

        javaLessons.add(Lesson.builder()
                .lessonInfo("Уроки Java для начинающих | #10 - Многомерные массивы")
                .lessonUrl("https://www.youtube.com/watch?v=jzhetb1wJeM&list=PL0lO_mIqDDFW2xXiWSfjT7hEdOUZHVNbK&index=10")
                .course(java)
                .build());

        javaLessons.add(Lesson.builder()
                .lessonInfo("Уроки Java для начинающих | #11 - Функции")
                .lessonUrl("https://www.youtube.com/watch?v=ROomaUIke2c&list=PL0lO_mIqDDFW2xXiWSfjT7hEdOUZHVNbK&index=11")
                .course(java)
                .build());

        javaLessons.add(Lesson.builder()
                .lessonInfo("Уроки Java для начинающих | #12 - Основы ООП")
                .lessonUrl("https://www.youtube.com/watch?v=ArERhPCnpIM&list=PL0lO_mIqDDFW2xXiWSfjT7hEdOUZHVNbK&index=12")
                .course(java)
                .build());

        javaLessons.add(Lesson.builder()
                .lessonInfo("Уроки Java для начинающих | #13 - Создание класса и объекта")
                .lessonUrl("https://www.youtube.com/watch?v=_GLnOwDEE_A&list=PL0lO_mIqDDFW2xXiWSfjT7hEdOUZHVNbK&index=13")
                .course(java)
                .build());

        javaLessons.add(Lesson.builder()
                .lessonInfo("Уроки Java для начинающих | #14 - Конструкторы")
                .lessonUrl("https://www.youtube.com/watch?v=6jc-E52hIks&list=PL0lO_mIqDDFW2xXiWSfjT7hEdOUZHVNbK&index=14")
                .course(java)
                .build());

        javaLessons.add(Lesson.builder()
                .lessonInfo("Уроки Java для начинающих | #15 - Наследование")
                .lessonUrl("https://www.youtube.com/watch?v=c8oUHKKwZtU&list=PL0lO_mIqDDFW2xXiWSfjT7hEdOUZHVNbK&index=15")
                .course(java)
                .build());

        javaLessons.add(Lesson.builder()
                .lessonInfo("Уроки Java для начинающих | #16 - Полиморфизм")
                .lessonUrl("https://www.youtube.com/watch?v=N-JddKgEfa8&list=PL0lO_mIqDDFW2xXiWSfjT7hEdOUZHVNbK&index=16")
                .course(java)
                .build());

        return javaLessons;
    }


    private String getJavaCourseInfo() {
        return "Это язык программирования, который годами не спускается ниже третьего места в мировом рейтинге популярности. " +
                "По данным компании Oracle, программы на Java запускаются на 3+ млрд девайсов.\n" +
                "\n" +
                "Крупные компании и банки используют именно Java. Все потому, что он славится требованиями к качеству кода, " +
                "универсальностью и высоким уровнем безопасности.\n" +
                "\n" +
                "На нем создают серьезное программное обеспечение, приложения на Android, игры и даже такие обыденные " +
                "сервисы как чатботы.";
    }

    private String getFrontendCourseInfo() {
        return "Это всё, что ты видишь и кликаешь на сайте или в приложении - картинки, кнопочки, анимации и даже бесячая реклама.\n" +
                "\n" +
                "Самые главные инструменты frontend разработки сайтов это JavaScript, HTML и CSS.\n" +
                "\n" +
                "В Кыргызстане фронтенд-разработчики востребованы практически в каждой IT-компании. Это также одно из самых " +
                "популярных направлений для фриланса и удаленной работы. Все потому, что в современном мире сайт нужен каждому";
    }

    private String getArtCourseInfo() {
        return "- Рисовать с нуля или повысить свой уровень\n" +
                "- Учиться у двух ведущих преподавателей Художник Online\n" +
                "- Работать акварельными красками, простыми карандашами и мягкими графическими материалами\n" +
                "- Создать больше 40 работ: натюрмортов и пейзажей\n" +
                "- Узнать и освоить всю необходимую художественную теорию\n" +
                "- Познакомиться с приемами и техниками, которые используются в живописи и графике";
    }
}
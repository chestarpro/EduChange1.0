//package kg.itacademy.boot;
//
//import kg.itacademy.entity.*;
//import kg.itacademy.repository.*;
//import lombok.RequiredArgsConstructor;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Component;
//
//import java.math.BigDecimal;
//import java.util.ArrayList;
//import java.util.List;
//
//@Component
//@RequiredArgsConstructor
//public class ApplicationStartRunner implements CommandLineRunner {
//
//    private final UserRoleRepository userRoleRepository;
//    private final UserRepository userRepository;
//    private final PasswordEncoder passwordEncoder;
//    private final CategoryRepository categoryRepository;
//    private final UserBalanceRepository userBalanceRepository;
//    private final CourseRepository courseRepository;
//    private final LessonRepository lessonRepository;
//
//    @Override
//    public void run(String... args) throws Exception {
//        User adminChyngyz = new User();
//        adminChyngyz.setFullName("Chyngyz");
//        adminChyngyz.setUsername("admin");
//        adminChyngyz.setEmail("admin@gmail.com");
//        adminChyngyz.setPassword("admin");
//        adminChyngyz.setIsActive(1L);
//        String encodedPassword = passwordEncoder.encode(adminChyngyz.getPassword());
//        adminChyngyz.setPassword(encodedPassword);
//        userRepository.save(adminChyngyz);
//
//        UserRole roleAdmin = new UserRole();
//        roleAdmin.setRoleName("ROLE_ADMIN");
//        roleAdmin.setUser(adminChyngyz);
//        userRoleRepository.save(roleAdmin);
//
//        userBalanceRepository.save(UserBalance.builder()
//                .user(adminChyngyz)
//                .balance(new BigDecimal(1000_000))
//                .build());
//
//        User userAlinur = new User();
//        userAlinur.setFullName("Alinur");
//        userAlinur.setUsername("alinur05");
//        userAlinur.setEmail("dubaiprinc247@gmailcom");
//        userAlinur.setPassword("123456");
//        userAlinur.setIsActive(1L);
//        String encodedPasswordUser1 = passwordEncoder.encode(userAlinur.getPassword());
//        userAlinur.setPassword(encodedPasswordUser1);
//        userRepository.save(userAlinur);
//
//        UserRole roleUser1 = new UserRole();
//        roleUser1.setRoleName("ROLE_USER");
//        roleUser1.setUser(userAlinur);
//        userRoleRepository.save(roleUser1);
//
//        userBalanceRepository.save(UserBalance.builder()
//                .user(userAlinur)
//                .balance(new BigDecimal(1000_000))
//                .build());
//
//        List<Category> categories = new ArrayList<>();
//        categories.add(Category.builder().categoryName("it").build());
//        categories.add(Category.builder().categoryName("бизнес").build());
//        categories.add(Category.builder().categoryName("маркетинг").build());
//        categories.add(Category.builder().categoryName("спорт").build());
//        categories.add(Category.builder().categoryName("искусство").build());
//        categories.add(Category.builder().categoryName("дизайн").build());
//        categories.add(Category.builder().categoryName("музыка").build());
//
//        categoryRepository.saveAll(categories);
//
//        List<Course> courses = new ArrayList<>();
//        courses.add(Course.builder()
//                .category(categories.get(0))
//                .courseName("java-разработчик")
//                .email("hello@it-academy.kg")
//                .phoneNumber("+996 707 745 232")
//                .courseShortInfo("\n" +
//                        "Получи профессию\n" +
//                        "Java-разработчика\n" +
//                        "за 9 месяцев.\n" +
//                        "С нуля.")
//                .courseInfoTitle("Что такое Java?")
//                .courseInfo(getJavaCourseInfo())
//                .courseInfoUrl("http://itacademy.tilda.ws/java")
//                .price(new BigDecimal(10000))
//                .user(adminChyngyz)
//                .build());
//
//        courses.add(Course.builder()
//                .category(categories.get(0))
//                .courseName("frontend-разработчик")
//                .email("hello@it-academy.kg")
//                .phoneNumber("+996 707 745 232")
//                .courseShortInfo("\n" +
//                        "Получи профессию\n" +
//                        "Frontend-разработчика\n" +
//                        "за 9 месяцев.\n" +
//                        "С нуля.")
//                .courseInfoTitle("Что такое Frontend?")
//                .courseInfo(getFrontendCourseInfo())
//                .courseInfoUrl("http://itacademy.tilda.ws/frontend")
//                .price(new BigDecimal(10000))
//                .user(userAlinur)
//                .build());
//
//        courses.add(Course.builder()
//                .category(categories.get(4))
//                .courseName("базовый курс: рисунок и живопись 2.0")
//                .email("art@gmailcom")
//                .phoneNumber("+996 707 223 327")
//                .courseShortInfo("Вся основная теория и всего две техники! Обновленная версия курса!")
//                .courseInfoTitle("Курс для всех, кто хочет")
//                .courseInfo(getArtCourseInfo())
//                .courseInfoUrl("https://www.hudozhnik.online/basic_course?_ga=2.99022302.1512450000.1637175476-2057833546.1637175476")
//                .price(new BigDecimal(3000))
//                .user(adminChyngyz)
//                .build());
//
//        courseRepository.saveAll(courses);
//
//        lessonRepository.saveAll(getStartJavaLessons(courses.get(0)));
//        lessonRepository.saveAll(getStartFrontendLessons(courses.get(1)));
//        lessonRepository.saveAll(getStartArtLessons(courses.get(2)));
//    }
//
//    private List<Lesson> getStartArtLessons(Course art) {
//        List<Lesson> artLessons = new ArrayList<>();
//
//        artLessons.add(Lesson.builder()
//                .lessonInfo("Как рисовать (нарисовать) глаза карандашом - обучающий урок (основы + такой глаз).")
//                .lessonUrl("https://www.youtube.com/embed/2lS6f9N0nKs")
//                .course(art)
//                .isVisible(false)
//                .build());
//
//        artLessons.add(Lesson.builder()
//                .lessonInfo("Как выбрать карандаш для рисования (какие карандаши нужны).")
//                .lessonUrl("https://www.youtube.com/embed/ycMmuQvkMtU")
//                .course(art)
//                .isVisible(false)
//                .build());
//
//        artLessons.add(Lesson.builder()
//                .lessonInfo("Какую бумагу выбрать для рисования (виды, свойства, рекомендации).")
//                .lessonUrl("https://www.youtube.com/embed/AsdIkLSyolg")
//                .course(art)
//                .isVisible(false)
//                .build());
//
//        artLessons.add(Lesson.builder()
//                .lessonInfo("Материалы для рисования карандашом - как их выбрать (часть 3).")
//                .lessonUrl("https://www.youtube.com/embed/YjUpZ7I2C6U")
//                .course(art)
//                .isVisible(false)
//                .build());
//
//        artLessons.add(Lesson.builder()
//                .lessonInfo("Как делать штриховку карандашом - теоретические основы.")
//                .lessonUrl("https://www.youtube.com/embed/3SV4PRhgRA8")
//                .course(art)
//                .isVisible(false)
//                .build());
//
//        artLessons.add(Lesson.builder()
//                .lessonInfo("ШТРИХОВКА, ТУШЕВКА И РАСТУШЕВКА - практический обучающий урок.")
//                .lessonUrl("https://www.youtube.com/embed/WkOQ5UX_66w")
//                .course(art)
//                .isVisible(false)
//                .build());
//
//        artLessons.add(Lesson.builder()
//                .lessonInfo("Как нарисовать объем! Основы светотени в рисунке карандашом.")
//                .lessonUrl("https://www.youtube.com/embed/hg_RC4LrUiw")
//                .course(art)
//                .isVisible(false)
//                .build());
//
//        artLessons.add(Lesson.builder()
//                .lessonInfo("Как рисовать (нарисовать) губы карандашом - обучающий урок.")
//                .lessonUrl("https://www.youtube.com/embed/nOYwEZ8vqT4")
//                .course(art)
//                .isVisible(false)
//                .build());
//
//        artLessons.add(Lesson.builder()
//                .lessonInfo("Как рисовать (нарисовать) нос карандашом - обучающий урок.")
//                .lessonUrl("https://www.youtube.com/embed/2YlONB8iH4g")
//                .course(art)
//                .isVisible(false)
//                .build());
//
//        artLessons.add(Lesson.builder()
//                .lessonInfo("Как нарисовать такое ухо. Как рисовать уши карандашом - видео урок.")
//                .lessonUrl("https://www.youtube.com/embed/5IXnYbwtacE")
//                .course(art)
//                .isVisible(false)
//                .build());
//
//        artLessons.add(Lesson.builder()
//                .lessonInfo("Как рисовать (нарисовать) волосы карандашом - обучающий урок.")
//                .lessonUrl("https://www.youtube.com/embed/2-dZToxwcoc")
//                .course(art)
//                .isVisible(false)
//                .build());
//
//        artLessons.add(Lesson.builder()
//                .lessonInfo("Как нарисовать лицо человека. Построение и пропорции лица.")
//                .lessonUrl("https://www.youtube.com/embed/q9Ha00E0Sqg")
//                .course(art)
//                .isVisible(false)
//                .build());
//
//        return artLessons;
//    }
//
//    private List<Lesson> getStartFrontendLessons(Course frontend) {
//        List<Lesson> frontendLessons = new ArrayList<>();
//
//        frontendLessons.add(Lesson.builder()
//                .lessonInfo("Введение в HTML. Урок 1 Введение")
//                .lessonUrl("https://www.youtube.com/embed/AUQMy9-M7gg")
//                .isVisible(false)
//                .course(frontend)
//                .build());
//
//        frontendLessons.add(Lesson.builder()
//                .lessonInfo("Введение в HTML. Урок 2 Раскройка шаблона")
//                .lessonUrl("https://www.youtube.com/embed/Bt6QsD_-U8U")
//                .course(frontend)
//                .isVisible(false)
//                .build());
//
//        frontendLessons.add(Lesson.builder()
//                .lessonInfo("Введение в HTML. Урок 3 Структура страницы")
//                .lessonUrl("https://www.youtube.com/embed/cI4WAUt4BeQ")
//                .course(frontend)
//                .isVisible(false)
//                .build());
//
//        frontendLessons.add(Lesson.builder()
//                .lessonInfo("Введение в HTML. Урок 4 Первая web страница")
//                .lessonUrl("https://www.youtube.com/embed/iWuPx5l7NyQ")
//                .course(frontend)
//                .isVisible(false)
//                .build());
//
//        frontendLessons.add(Lesson.builder()
//                .lessonInfo("Введение в HTML. Урок 5 Создание HTML списков")
//                .lessonUrl("https://www.youtube.com/embed/PodgGBEP-r8")
//                .course(frontend)
//                .isVisible(false)
//                .build());
//
//        frontendLessons.add(Lesson.builder()
//                .lessonInfo("Введение в HTML. Урок 6 Создание HTML ссылок")
//                .lessonUrl("https://www.youtube.com/embed/IcRA3AQCi8A")
//                .course(frontend)
//                .isVisible(false)
//                .build());
//
//        frontendLessons.add(Lesson.builder()
//                .lessonInfo("Введение в HTML. Урок 7 Добавление изображений на сайт")
//                .lessonUrl("https://www.youtube.com/embed/uZKocgTHg6k")
//                .course(frontend)
//                .isVisible(false)
//                .build());
//
//        frontendLessons.add(Lesson.builder()
//                .lessonInfo("Введение в HTML. Урок 8 Создание HTML таблицы")
//                .lessonUrl("https://www.youtube.com/embed/MyE86D91w7g")
//                .course(frontend)
//                .isVisible(false)
//                .build());
//
//        frontendLessons.add(Lesson.builder()
//                .lessonInfo("Введение в HTML. Урок 9 Форматирование текста с помощью HTML тегов")
//                .lessonUrl("https://www.youtube.com/embed/mW7b8GSVVqw")
//                .course(frontend)
//                .isVisible(false)
//                .build());
//
//        frontendLessons.add(Lesson.builder()
//                .lessonInfo("Введение в HTML. Урок 10 HTML Формы")
//                .lessonUrl("https://www.youtube.com/embed/aitJXkc8PeI")
//                .course(frontend)
//                .isVisible(false)
//                .build());
//
//        return frontendLessons;
//    }
//
//    private List<Lesson> getStartJavaLessons(Course java) {
//        List<Lesson> javaLessons = new ArrayList<>();
//
//        javaLessons.add(Lesson.builder()
//                .lessonInfo("Уроки Java для начинающих | #1 - Программирование на Java")
//                .lessonUrl("https://www.youtube.com/embed/Zxpz5tRrUvU")
//                .course(java)
//                .isVisible(false)
//                .build());
//
//        javaLessons.add(Lesson.builder()
//                .lessonInfo("Уроки Java для начинающих | #2 - Установка Java JDK и IntelliJ IDEA")
//                .lessonUrl("https://www.youtube.com/embed/AM_WxaR6Spc")
//                .course(java)
//                .isVisible(false)
//                .build());
//
//        javaLessons.add(Lesson.builder()
//                .lessonInfo("Уроки Java для начинающих | #3 - Hello World!")
//                .lessonUrl("https://www.youtube.com/embed/IQTEziR82so")
//                .course(java)
//                .isVisible(false)
//                .build());
//
//        javaLessons.add(Lesson.builder()
//                .lessonInfo("Уроки Java для начинающих | #4 - Переменные")
//                .lessonUrl("https://www.youtube.com/watch?v=Y__Ns7FS5lA&list=PL0lO_mIqDDFW2xXiWSfjT7hEdOUZHVNbK&index=4")
//                .course(java)
//                .isVisible(false)
//                .build());
//
//        javaLessons.add(Lesson.builder()
//                .lessonInfo("Уроки Java для начинающих | #5 - Данные от пользователя")
//                .lessonUrl("https://www.youtube.com/watch?v=kD5ZDwdtJ10&list=PL0lO_mIqDDFW2xXiWSfjT7hEdOUZHVNbK&index=5")
//                .course(java)
//                .isVisible(false)
//                .build());
//
//        javaLessons.add(Lesson.builder()
//                .lessonInfo("Уроки Java для начинающих | #6 - Математические операции")
//                .lessonUrl("https://www.youtube.com/watch?v=W6A4DEr7XW4&list=PL0lO_mIqDDFW2xXiWSfjT7hEdOUZHVNbK&index=6")
//                .course(java)
//                .isVisible(false)
//                .build());
//
//        javaLessons.add(Lesson.builder()
//                .lessonInfo("Уроки Java для начинающих | #7 - Условные операторы")
//                .lessonUrl("https://www.youtube.com/watch?v=Eao7VNpv1f0&list=PL0lO_mIqDDFW2xXiWSfjT7hEdOUZHVNbK&index=7")
//                .course(java)
//                .isVisible(false)
//                .build());
//
//        javaLessons.add(Lesson.builder()
//                .lessonInfo("Уроки Java для начинающих | #8 - Циклы (For, While, Do while)")
//                .lessonUrl("https://www.youtube.com/watch?v=y3Xu5o6Pxfg&list=PL0lO_mIqDDFW2xXiWSfjT7hEdOUZHVNbK&index=8")
//                .course(java)
//                .isVisible(false)
//                .build());
//
//        javaLessons.add(Lesson.builder()
//                .lessonInfo("Уроки Java для начинающих | #9 - Массивы")
//                .lessonUrl("https://www.youtube.com/watch?v=qiUfLIbbedw&list=PL0lO_mIqDDFW2xXiWSfjT7hEdOUZHVNbK&index=9")
//                .course(java)
//                .isVisible(false)
//                .build());
//
//        javaLessons.add(Lesson.builder()
//                .lessonInfo("Уроки Java для начинающих | #10 - Многомерные массивы")
//                .lessonUrl("https://www.youtube.com/watch?v=jzhetb1wJeM&list=PL0lO_mIqDDFW2xXiWSfjT7hEdOUZHVNbK&index=10")
//                .course(java)
//                .isVisible(false)
//                .build());
//
//        javaLessons.add(Lesson.builder()
//                .lessonInfo("Уроки Java для начинающих | #11 - Функции")
//                .lessonUrl("https://www.youtube.com/watch?v=ROomaUIke2c&list=PL0lO_mIqDDFW2xXiWSfjT7hEdOUZHVNbK&index=11")
//                .course(java)
//                .isVisible(false)
//                .build());
//
//        javaLessons.add(Lesson.builder()
//                .lessonInfo("Уроки Java для начинающих | #12 - Основы ООП")
//                .lessonUrl("https://www.youtube.com/watch?v=ArERhPCnpIM&list=PL0lO_mIqDDFW2xXiWSfjT7hEdOUZHVNbK&index=12")
//                .course(java)
//                .isVisible(false)
//                .build());
//
//        javaLessons.add(Lesson.builder()
//                .lessonInfo("Уроки Java для начинающих | #13 - Создание класса и объекта")
//                .lessonUrl("https://www.youtube.com/watch?v=_GLnOwDEE_A&list=PL0lO_mIqDDFW2xXiWSfjT7hEdOUZHVNbK&index=13")
//                .course(java)
//                .isVisible(false)
//                .build());
//
//        javaLessons.add(Lesson.builder()
//                .lessonInfo("Уроки Java для начинающих | #14 - Конструкторы")
//                .lessonUrl("https://www.youtube.com/watch?v=6jc-E52hIks&list=PL0lO_mIqDDFW2xXiWSfjT7hEdOUZHVNbK&index=14")
//                .course(java)
//                .isVisible(false)
//                .build());
//
//        javaLessons.add(Lesson.builder()
//                .lessonInfo("Уроки Java для начинающих | #15 - Наследование")
//                .lessonUrl("https://www.youtube.com/watch?v=c8oUHKKwZtU&list=PL0lO_mIqDDFW2xXiWSfjT7hEdOUZHVNbK&index=15")
//                .course(java)
//                .isVisible(false)
//                .build());
//
//        javaLessons.add(Lesson.builder()
//                .lessonInfo("Уроки Java для начинающих | #16 - Полиморфизм")
//                .lessonUrl("https://www.youtube.com/watch?v=N-JddKgEfa8&list=PL0lO_mIqDDFW2xXiWSfjT7hEdOUZHVNbK&index=16")
//                .course(java)
//                .isVisible(false)
//                .build());
//
//        return javaLessons;
//    }
//
//
//    private String getJavaCourseInfo() {
//        return "Это язык программирования, который годами не спускается ниже третьего места в мировом рейтинге популярности. " +
//                "По данным компании Oracle, программы на Java запускаются на 3+ млрд девайсов.\n" +
//                "\n" +
//                "Крупные компании и банки используют именно Java. Все потому, что он славится требованиями к качеству кода, " +
//                "универсальностью и высоким уровнем безопасности.\n" +
//                "\n" +
//                "На нем создают серьезное программное обеспечение, приложения на Android, игры и даже такие обыденные " +
//                "сервисы как чатботы.";
//    }
//
//    private String getFrontendCourseInfo() {
//        return "Это всё, что ты видишь и кликаешь на сайте или в приложении - картинки, кнопочки, анимации и даже бесячая реклама.\n" +
//                "\n" +
//                "Самые главные инструменты frontend разработки сайтов это JavaScript, HTML и CSS.\n" +
//                "\n" +
//                "В Кыргызстане фронтенд-разработчики востребованы практически в каждой IT-компании. Это также одно из самых " +
//                "популярных направлений для фриланса и удаленной работы. Все потому, что в современном мире сайт нужен каждому";
//    }
//
//    private String getArtCourseInfo() {
//        return "- Рисовать с нуля или повысить свой уровень\n" +
//                "- Учиться у двух ведущих преподавателей Художник Online\n" +
//                "- Работать акварельными красками, простыми карандашами и мягкими графическими материалами\n" +
//                "- Создать больше 40 работ: натюрмортов и пейзажей\n" +
//                "- Узнать и освоить всю необходимую художественную теорию\n" +
//                "- Познакомиться с приемами и техниками, которые используются в живописи и графике";
//    }
//}
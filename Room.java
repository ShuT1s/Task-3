import java.util.Random;

// Базовый класс Room
class Room
{
    public static void main(String[] args)
    {
        // Создание объектов комнат
        Room economyRoom = new EconomyRoom.RoomBuilder(101, 2, 50.0).build();
        StandardRoom standardRoom = new StandardRoom(102, 3, 75.0);
        LuxRoom luxRoom = new LuxRoom(103, 4, 100.0);
        UltraLuxRoom ultraLuxRoom = new UltraLuxRoom(104, 6, 150.0);

        // Создание сервиса для комнат
        RoomService<Room> roomService = new RoomServiceImpl<>();

        // Тестирование сервиса
        try
        {
            roomService.reserve(economyRoom);
            roomService.clean(economyRoom);
            roomService.free(economyRoom);

            // Попытка забронировать уже забронированную комнату
            roomService.reserve(economyRoom); // Вызовет исключение
        }
        catch (RoomAlreadyReservedException e)
        {
            System.out.println("Ошибка: " + e.getMessage());
        }
        
    }
    private int roomNumber;
    private int maxPeople;
    private double pricePerNight;
    private boolean isSanitized;

    public Room(int roomNumber, int maxPeople, double pricePerNight)
    {
        this.roomNumber = roomNumber;
        this.maxPeople = maxPeople;
        this.pricePerNight = pricePerNight;
        this.isSanitized = false; // Изначально комната не продезинфицирована
    }

    public int getRoomNumber()
    {
        return roomNumber;
    }

    public int getMaxPeople()
    {
        return maxPeople;
    }

    public double getPricePerNight()
    {
        return pricePerNight;
    }

    public boolean isSanitized()
    {
        return isSanitized;
    }

    public void setSanitized(boolean sanitized)
    {
        this.isSanitized = sanitized;
    }
    
    // Конструктор не может быть вызван напрямую (final)
    final public static class RoomBuilder
    {
        private int roomNumber;
        private int maxPeople;
        private double pricePerNight;

        public RoomBuilder(int roomNumber, int maxPeople, double pricePerNight)
        {
            this.roomNumber = roomNumber;
            this.maxPeople = maxPeople;
            this.pricePerNight = pricePerNight;
        }

        public Room build()
        {
            return new Room(roomNumber, maxPeople, pricePerNight);
        }
    }
    
    // Запрет на создание объектов типа Room (private constructor)
    private Room(RoomBuilder builder)
    {
        this.roomNumber = builder.roomNumber;
        this.maxPeople = builder.maxPeople;
        this.pricePerNight = builder.pricePerNight;
    }

    @Override
    public String toString()
    {
        return "Room{" +
                "roomNumber=" + roomNumber +
                ", maxPeople=" + maxPeople +
                ", pricePerNight=" + pricePerNight +
                ", isSanitized=" + isSanitized +
                '}';
    }
}

// Класс EconomyRoom
class EconomyRoom extends Room
{
    public EconomyRoom(int roomNumber, int maxPeople, double pricePerNight)
    {
        super(roomNumber, maxPeople, pricePerNight);
    }
}

// Класс ProRoom (нельзя создавать объекты)
final class ProRoom extends Room
{
    private ProRoom(int roomNumber, int maxPeople, double pricePerNight)
    {
        super(roomNumber, maxPeople, pricePerNight);
    }
}

// Класс StandardRoom
class StandardRoom extends EconomyRoom
{
    public StandardRoom(int roomNumber, int maxPeople, double pricePerNight)
    {
        super(roomNumber, maxPeople, pricePerNight);
    }
}

// Класс LuxRoom
class LuxRoom extends Room
{
    public LuxRoom(int roomNumber, int maxPeople, double pricePerNight)
    {
        super(roomNumber, maxPeople, pricePerNight);
    }
}

// Класс UltraLuxRoom
class UltraLuxRoom extends LuxRoom {
    public UltraLuxRoom(int roomNumber, int maxPeople, double pricePerNight)
    {
        super(roomNumber, maxPeople, pricePerNight);
    }
}

// Интерфейс RoomService
interface RoomService<T extends Room>
{
    void clean(T room);
    boolean reserve(T room);
    void free(T room);
}

// Класс RoomServiceImpl, реализующий интерфейс RoomService
class RoomServiceImpl<T extends Room> implements RoomService<T>
{
    @Override
    public void clean(T room)
    {
        room.setSanitized(true);
        System.out.println("Комната " + room.getRoomNumber() + " помыта.");
    }

    @Override
    public boolean reserve(T room)
    {
        if (room.isSanitized())
        {
            System.out.println("Комната " + room.getRoomNumber() + " забронирована.");
            room.setSanitized(false); // После бронирования комната больше не продезинфицирована
            return true;
        }
        else
        {
            throw new RoomAlreadyReservedException("Комната " + room.getRoomNumber() + " уже забронирована.");
        }
    }

    @Override
    public void free(T room)
    {
        if (room.isSanitized())
        {
            System.out.println("Комната " + room.getRoomNumber() + " освобождена.");
        }
        else
        {
            System.out.println("Комната " + room.getRoomNumber() + " уже освобождена.");
        }
    }
}

// Кастомное исключение для забронированной комнаты
class RoomAlreadyReservedException extends RuntimeException
{
    public RoomAlreadyReservedException(String message)
    {
        super(message);
    }
}

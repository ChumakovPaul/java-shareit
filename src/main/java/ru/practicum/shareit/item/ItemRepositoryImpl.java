package ru.practicum.shareit.item;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.DataNotFoundException;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserRepository;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
@Repository
public class ItemRepositoryImpl implements ItemRepository {
    private long itemCounter = 1;
    private final HashMap<Long, Item> itemStorage;

    @Autowired
    private final ItemMapper itemMapper;
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final UserMapper userMapper;


    @Override
    public ItemDto createItem(ItemCreateDto itemCreateDto) {
        User user;
        try {
            user = userMapper.toUser(userRepository.getUser(itemCreateDto.getOwnerId()));
        } catch (NullPointerException e) {
            throw new DataNotFoundException("Такого пользователя нет базе");
        }
        Item item = itemMapper.toItem(itemCreateDto, user);
        item.setId(itemCounter);
        itemCounter++;
        itemStorage.put(item.getId(), item);
        return itemMapper.toItemDto(item);
    }

    @Override
    public ItemDto updateItem(ItemUpdateDto itemUpdateDto) {
        if (!itemStorage.containsKey(itemUpdateDto.getId())) {
            throw new DataNotFoundException("Такой вещи нет в базе");
        } else if (itemStorage.get(itemUpdateDto.getId()).getOwner().getId() != itemUpdateDto.getOwnerId()) {
            throw new DataNotFoundException("Изменить запись вещи  может только её владелец");
        }
        Item item = itemStorage.get(itemUpdateDto.getId());
        if (itemUpdateDto.getName() != null) {
            item.setName(itemUpdateDto.getName());
        }
        if (itemUpdateDto.getDescription() != null) {
            item.setDescription(itemUpdateDto.getDescription());
        }
        if (itemUpdateDto.getAvailable() != null) {
            item.setAvailable(itemUpdateDto.getAvailable());
        }
        System.out.println(item);
        itemStorage.put(item.getId(), item);
        return itemMapper.toItemDto(item);
    }

    @Override
    public ItemDto getItem(Long itemId) {
        return itemMapper.toItemDto(itemStorage.get(itemId));
    }

    @Override
    public List<ItemDto> getUserItems(Long ownerId) {
        return itemStorage.values().stream()
                .filter(item -> item.getOwner().getId() == ownerId)
                .map(itemMapper::toItemDto)
                .toList();
    }

    @Override
    public List<ItemDto> getSearchItems(String text) {
        if (text.isBlank()) {
            return List.of();
        }
        List<ItemDto> nameSearchItems = itemStorage.values()
                .stream()
                .filter(item -> item.getName().toLowerCase().contains(text.toLowerCase()))
                .filter(item -> item.isAvailable())
                .map(itemMapper::toItemDto)
                .toList();
        List<ItemDto> descriptionSearchItems = itemStorage.values()
                .stream()
                .filter(item -> item.getDescription().toLowerCase().contains(text.toLowerCase()))
                .filter(item -> item.isAvailable())
                .map(itemMapper::toItemDto)
                .toList();
        return Stream.concat(nameSearchItems.stream(), descriptionSearchItems.stream())
                .distinct()
                .collect(Collectors.toList());
    }
}
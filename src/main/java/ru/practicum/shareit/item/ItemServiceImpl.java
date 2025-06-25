package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    @Autowired
    private final ItemRepository itemRepository;


    @Override
    public ItemDto createItem(ItemCreateDto itemCreateDto) {
        return itemRepository.createItem(itemCreateDto);
    }

    @Override
    public ItemDto updateItem(ItemUpdateDto itemUpdateDto) {
        return itemRepository.updateItem(itemUpdateDto);
    }

    @Override
    public ItemDto getItem(Long itemId) {
        return itemRepository.getItem(itemId);
    }

    @Override
    public List<ItemDto> getUserItems(Long ownerId) {
        return itemRepository.getUserItems(ownerId);
    }

    @Override
    public List<ItemDto> getSearchItems(String text) {
        return itemRepository.getSearchItems(text);
    }
}
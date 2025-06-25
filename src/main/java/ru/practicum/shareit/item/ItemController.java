package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */

@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/items")
public class ItemController {

    @Autowired
    private final ItemService itemService;

    @PostMapping
    public ItemDto createItem(@RequestHeader("X-Sharer-User-Id") Long ownerId, @Valid @RequestBody ItemCreateDto itemCreateDto) {
        log.info("==> Creating item: {}", itemCreateDto);
        itemCreateDto.setOwnerId(ownerId);
        ItemDto item = itemService.createItem(itemCreateDto);
        log.info("<== Creating item: {}", itemCreateDto);
        return item;
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") Long ownerId, @PathVariable Long itemId, @Valid @RequestBody ItemUpdateDto itemUpdateDto) {
        log.info("==> Updating item: {}", itemUpdateDto);
        itemUpdateDto.setOwnerId(ownerId);
        itemUpdateDto.setId(itemId);
        ItemDto item = itemService.updateItem(itemUpdateDto);
        log.info("<== Updating item: {}", itemUpdateDto);
        return item;
    }

    @GetMapping("/{itemId}")
    public ItemDto getItem(@PathVariable Long itemId) {
        log.info("==> Getting item: {}", itemId);
        ItemDto item = itemService.getItem(itemId);
        log.info("<== Getting item: {}", itemId);
        return item;
    }

    @GetMapping
    public List<ItemDto> getUserItems(@RequestHeader("X-Sharer-User-Id") Long ownerId) {
        log.info("==> Getting user {} items}", ownerId);
        List<ItemDto> userItems = itemService.getUserItems(ownerId);
        log.info("<== Getting user {} items}", ownerId);
        return userItems;
    }

    @GetMapping("/search")
    public List<ItemDto> getSearchItems(@RequestParam("text") String text) {
        log.info("==> Getting items with words {}}", text);
        List<ItemDto> searchItems = itemService.getSearchItems(text);
        log.info("<== Getting items with words {}}", text);
        return searchItems;

    }
}
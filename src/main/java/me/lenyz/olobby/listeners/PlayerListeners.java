package me.lenyz.olobby.listeners;

import de.tr7zw.nbtapi.NBT;
import de.tr7zw.nbtapi.NBTItem;
import me.lenyz.olobby.inventories.ServerInventory;
import me.lenyz.olobby.managers.LobbyManager;
import me.lenyz.olobby.objects.User;
import me.lenyz.olobby.storages.ServerStorage;
import me.lenyz.olobby.storages.UserStorage;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class PlayerListeners implements Listener {

    private UserStorage userStorage;
    private ServerStorage serverStorage;
    private LobbyManager lobbyManager;

    public PlayerListeners(LobbyManager lobbyManager, UserStorage userStorage, ServerStorage serverStorage) {
        this.lobbyManager = lobbyManager;
        this.userStorage = userStorage;
        this.serverStorage = serverStorage;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.setJoinMessage(null);
        giveLobbyItems(event.getPlayer());

        event.getPlayer().teleport(lobbyManager.getLobbyLocation());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        event.setQuitMessage(null);
        if(userStorage.containsUser(event.getPlayer().getUniqueId())) {
            userStorage.removeUser(event.getPlayer().getUniqueId());
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPlayerMove(PlayerMoveEvent event) {
        if(event.getTo().getBlockY() < 0) {
            event.getPlayer().teleport(lobbyManager.getLobbyLocation());
        }
    }

    @EventHandler
    public void onPlayerHungry(FoodLevelChangeEvent event) {
        if(event.getFoodLevel() >= 20) return;
        event.setFoodLevel(20);
    }

    @EventHandler
    public void onPlayerBreak(BlockBreakEvent event) {
        if(event.getPlayer().getGameMode().equals(GameMode.CREATIVE)) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerPlace(BlockPlaceEvent event) {
        if(event.getPlayer().getGameMode().equals(GameMode.CREATIVE)) return;
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if(event.getPlayer().getGameMode().equals(GameMode.CREATIVE)) return;

        if(event.getItem() == null) return;
        if(event.getItem().getType().equals(Material.GOLDEN_APPLE)) return;
        event.setCancelled(true);

        if(event.getItem().getItemMeta() == null) return;
        NBTItem nbtItem = new NBTItem(event.getItem());
        Player player = event.getPlayer();

        if(nbtItem.hasKey("olobby_selector")) {
            ServerInventory.getInventory(lobbyManager, serverStorage).open(player);
            player.playSound(player.getLocation(), Sound.CHEST_OPEN, 1, 1);
            return;
        }

        if(nbtItem.hasKey("olobby_pvp")) {
            User user = userStorage.getUser(player.getUniqueId());
            if(user.isInArena()){
                user.setInArena(false);
                giveLobbyItems(player);
            }else{
                user.setInArena(true);
                giveArenaItems(player);
            }
            return;
        }
    }

    @EventHandler
    public void onPlayerDrop(PlayerDropItemEvent event) {
        if(event.getPlayer().getGameMode().equals(GameMode.CREATIVE)) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerClick(InventoryClickEvent event) {
        if(event.getWhoClicked().getGameMode().equals(GameMode.CREATIVE)) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerDamaged(EntityDamageEvent event) {
        if(event.getEntity() instanceof Player) {
            if(event.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK) || event.getCause().equals(EntityDamageEvent.DamageCause.SUICIDE)) return;
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent event) {
        if(!(event.getEntity() instanceof Player)) {
            event.setCancelled(true);
        }
        if(!(event.getDamager() instanceof Player)) {
            event.setCancelled(true);
        }


        User attackerUser = userStorage.getUser(event.getDamager().getUniqueId());
        if(!attackerUser.isInArena()){
            event.setCancelled(true);
            return;
        }

        User victimUser = userStorage.getUser(event.getEntity().getUniqueId());
        if(!victimUser.isInArena()){
            event.setCancelled(true);
            return;
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        event.setDeathMessage(null);
        event.getDrops().clear();
        event.setDroppedExp(0);

        User user = userStorage.getUser(event.getEntity().getUniqueId());
        user.setInArena(false);
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        event.setRespawnLocation(lobbyManager.getLobbyLocation());
        giveLobbyItems(event.getPlayer());
    }

    private void giveLobbyItems(Player player){
        PlayerInventory inventory = player.getInventory();
        player.getActivePotionEffects().clear();

        inventory.clear();
        inventory.setItem(3, lobbyManager.getServerSelector());
        inventory.setItem(5, lobbyManager.getEnablePVP());

        inventory.setHelmet(null);
        inventory.setChestplate(null);
        inventory.setLeggings(null);
        inventory.setBoots(null);

        player.updateInventory();
    }

    private void giveArenaItems(Player player){
        PlayerInventory inventory = player.getInventory();
        inventory.clear();
        inventory.setItem(8, lobbyManager.getDisablePVP());

        inventory.setItem(0, new ItemStack(Material.DIAMOND_SWORD));
        inventory.setItem(1, new ItemStack(Material.GOLDEN_APPLE, 16));

        inventory.setHelmet(new ItemStack(Material.DIAMOND_HELMET));
        inventory.setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE));
        inventory.setLeggings(new ItemStack(Material.DIAMOND_LEGGINGS));
        inventory.setBoots(new ItemStack(Material.DIAMOND_BOOTS));

        player.updateInventory();
    }
}

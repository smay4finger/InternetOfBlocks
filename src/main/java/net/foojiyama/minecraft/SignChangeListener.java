package net.foojiyama.minecraft;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class SignChangeListener implements Listener {

    private InternetOfBlocks plugin;

    public SignChangeListener(InternetOfBlocks parent) {
        this.plugin = parent;
    }

    @EventHandler
    public void onSignChange(SignChangeEvent event) {
        try {
            if (isPublishSign(event)) {
                String topic = parameterTopic(event);

                plugin.getConfig().set(blockPath("publishers", event), topic);
                plugin.saveConfig();
            }
            else if (isSubscribeSign(event)) {
                String topic = parameterTopic(event);

                plugin.getConfig().set(blockPath("subscribers", event), topic);
                plugin.saveConfig();
            }
        }
        catch (IllegalArgumentException e) {
            event.setCancelled(true);
            event.getBlock().breakNaturally();
            event.getPlayer().sendMessage(e.getMessage());
        }
    }
    

    private String blockPath(String prefix, SignChangeEvent event) {
        Block block = event.getBlock();
        Location location = block.getLocation();

        String locationName = location.getBlockX() + "/" + location.getBlockY() + "/" + location.getBlockZ();
        String worldName = block.getWorld().getName();
        
        return prefix + "." + worldName + "." + locationName;
    }

    private String parameterTopic(SignChangeEvent event) {
        String line = event.getLine(1);
        if (!line.trim().isEmpty()) {
            return line;
        }
        else {
            throw new IllegalArgumentException("you have to have a topic");
        }
    }

    private boolean isSubscribeSign(SignChangeEvent event) {
        return event.getLine(0).equalsIgnoreCase("[mqtt.subscribe]");
    }

    private boolean isPublishSign(SignChangeEvent event) {
        return event.getLine(0).equalsIgnoreCase("[mqtt.publish]");
    }
}

package com.xiaoace.kooksrv.command;

import com.xiaoace.kooksrv.database.dao.UserDao;
import com.xiaoace.kooksrv.utils.CacheTools;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.inject.Inject;
import jdk.jfr.Description;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;

import java.util.Objects;

/**
 * 2024/8/26<br>
 * KookSRV<br>
 *
 * @author huanmeng_qwq
 */

@Command(name = "kooksrv")
@Description("KookSRV main command")
public class LiteMinecraftCommand {
    private final UserDao userDao;
    private final CacheTools cacheTools;

    @Inject
    public LiteMinecraftCommand(UserDao userDao, CacheTools cacheTools) {
        this.userDao = userDao;
        this.cacheTools = cacheTools;
    }

    @Execute
    public String help() {
        return "使用方法: /kooksrv link|unlink  把MC账号绑定到Kook或是取消绑定.";
    }

    @Execute(name = "link")
    public String link(@Context Player player) {
        String uuid = String.valueOf(player.getUniqueId());

        if (Objects.equals(userDao.selectUserByUUID(uuid).getUuid(), uuid)) {
            return "您已经绑定过KOOK账号了噢";
        } else {
            String code = cacheTools.createNewCache(uuid);
            return "您的绑定码为: " + code;
        }
    }

    @Execute(name = "unlink")
    public String unlink(@Context Player player) {
        String uuid = String.valueOf(player.getUniqueId());
        userDao.deleteUserByUUID(uuid);
        return "已成功解绑!";
    }

    @Execute(name = "getMap")
    public void getMap(@Context Player player, @Arg("mapId") int mapId) {
        ItemStack map = new ItemStack(Material.FILLED_MAP, 1);
        MapMeta mapMeta = (MapMeta) map.getItemMeta();
        mapMeta.setMapId(mapId);
        map.setItemMeta(mapMeta);

        if (!player.getInventory().addItem(map).isEmpty()) {
            player.getWorld().dropItem(player.getLocation(), map);
        }
    }
}

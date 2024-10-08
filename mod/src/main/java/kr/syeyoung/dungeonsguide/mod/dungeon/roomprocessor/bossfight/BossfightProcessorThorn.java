/*
 * Dungeons Guide - The most intelligent Hypixel Skyblock Dungeons Mod
 * Copyright (C) 2021  cyoung06
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package kr.syeyoung.dungeonsguide.mod.dungeon.roomprocessor.bossfight;

import kr.syeyoung.dungeonsguide.mod.DungeonsGuide;
import kr.syeyoung.dungeonsguide.mod.features.FeatureRegistry;
import kr.syeyoung.dungeonsguide.mod.features.impl.etc.FeatureCollectDiagnostics;
import kr.syeyoung.dungeonsguide.mod.utils.RenderUtils;
import net.minecraft.block.Block;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.BossStatus;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.passive.*;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BossfightProcessorThorn extends GeneralBossfightProcessor {


    public BossfightProcessorThorn(boolean isMasterMode) {
        super(isMasterMode ? "MASTERMODE_CATACOMBS_FLOOR_FOUR" : "CATACOMBS_FLOOR_FOUR");
        addPhase(GeneralBossfightProcessor.PhaseData.builder()
                .phase("fight").build()
        );
        w= DungeonsGuide.getDungeonsGuide().getDungeonFacade().getContext().getWorld();
        this.isMasterMode = isMasterMode;
    }
    private final Set<BlockPos> progressBar = new HashSet<BlockPos>();
    private final World w;

    private int ticksPassed = 0;

    @Override
    public void tick() {
        ticksPassed ++;
        if (ticksPassed == 20) {
            progressBar.clear();
            for (int x = -30; x <= 30; x++) {
                for (int y = -30; y <= 30; y++) {
                    BlockPos newPos = new BlockPos(5 + x, 77, 5 + y);
                    Block b = w.getBlockState(newPos).getBlock();
                    if ((b == Blocks.coal_block || b == Blocks.sea_lantern) && w.getBlockState(newPos.add(0, 1, 0)).getBlock() != Blocks.carpet)
                        progressBar.add(newPos);
                }
            }
        }
    }
    private boolean isMasterMode;

    @Override
    public List<HealthData> getHealths() {
        List<HealthData> healths = new ArrayList<HealthData>();
        healths.add(new HealthData("Thorn", Math.round(BossStatus.healthScale * (isMasterMode ? 6 : 4)),isMasterMode ? 6 : 4, true));
        return healths;
    }

    @Override
    public String getBossName() {
        return "Thorn";
    }

    public double calculatePercentage() {
        int total = progressBar.size(), lit = 0;
        if (total == 0) return 0;
        for (BlockPos pos : progressBar) {
            if (w.getBlockState(pos).getBlock() == Blocks.sea_lantern ) lit++;
        }

        return lit / (double)total;
    }

    @Override
    public void drawWorld(float partialTicks) {
        super.drawWorld(partialTicks);
        if (!FeatureRegistry.DEBUG.isEnabled()) return;
        try {
            BlockPos pos = new BlockPos(205,77, 205);
            RenderUtils.highlightBlock(pos, new Color(0, 255, 255, 50), partialTicks, false);
            for (BlockPos pos2 : progressBar) {
                RenderUtils.highlightBlock(pos2, w.getBlockState(pos2).getBlock() == Blocks.sea_lantern ?
                            new Color(0, 255, 0, 50) : new Color(255,0,0, 50), partialTicks, false);
            }
        } catch (Exception e) {
            FeatureCollectDiagnostics.queueSendLogAsync(e);
            e.printStackTrace();
        }
    }

    @Override
    public MarkerData convertToMarker(Entity entity) {
        if (entity.isInvisible()) return null;
        if (entity instanceof EntityBat) {
            return MarkerData.fromEntity(entity, MarkerData.MobType.ANIMALS, 28);
        } else if (entity instanceof EntitySheep) {
            return MarkerData.fromEntity(entity, MarkerData.MobType.ANIMALS, 30);
        } else if (entity instanceof EntityCow) {
            return MarkerData.fromEntity(entity, MarkerData.MobType.ANIMALS, 26);
        } else if (entity instanceof EntityChicken) {
            return MarkerData.fromEntity(entity, MarkerData.MobType.ANIMALS, 31);
        } else if (entity instanceof EntityWolf) {
            return MarkerData.fromEntity(entity, MarkerData.MobType.ANIMALS, 29);
        } else if (entity instanceof EntityRabbit) {
            return MarkerData.fromEntity(entity, MarkerData.MobType.ANIMALS, 27);
        } else if (entity instanceof EntityGhast) {
            return MarkerData.fromEntity(entity, MarkerData.MobType.BOSS, 25);
        } else if (entity instanceof EntityOtherPlayerMP) {
            if (entity.getName().equals("Spirit Bear"))
                return MarkerData.fromEntity(entity, MarkerData.MobType.MINIBOSS, 24);
        }
        return null;
    }
}

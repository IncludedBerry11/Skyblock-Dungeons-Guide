/*
 * Dungeons Guide - The most intelligent Hypixel Skyblock Dungeons Mod
 * Copyright (C) 2023  cyoung06 (syeyoung)
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

package kr.syeyoung.dungeonsguide.mod.config.guiconfig.configv3;

import kr.syeyoung.dungeonsguide.mod.features.AbstractFeature;
import kr.syeyoung.dungeonsguide.mod.guiv2.BindableAttribute;
import kr.syeyoung.dungeonsguide.mod.guiv2.Widget;
import kr.syeyoung.dungeonsguide.mod.guiv2.elements.Navigator;
import kr.syeyoung.dungeonsguide.mod.guiv2.xml.AnnotatedImportOnlyWidget;
import kr.syeyoung.dungeonsguide.mod.guiv2.xml.annotations.Bind;
import kr.syeyoung.dungeonsguide.mod.guiv2.xml.annotations.On;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;
import java.util.function.Supplier;

public class CategoryItem extends AnnotatedImportOnlyWidget {

    @Bind(variableName = "name")
    public final BindableAttribute<String> name = new BindableAttribute<>(String.class);
    @Bind(variableName = "description")
    public final BindableAttribute<String> description = new BindableAttribute<>(String.class);
    @Bind(variableName = "icon")
    public final BindableAttribute<String> icon = new BindableAttribute<>(String.class, "dungeonsguide:textures/dglogox128.png");
    private Supplier<Widget> pageCreator;
    private boolean triggerSidemenu = false;

    public CategoryItem(Supplier<Widget> pageCreator, String category, String description) {
        super(new ResourceLocation("dungeonsguide:gui/config/categoryitem.gui"));
        this.pageCreator = pageCreator;

        this.name.setValue(category);
        this.description.setValue(description);
        try {
            String target = "dungeonsguide:textures/config/categoryIcon/"+category.toLowerCase()
                    .replace("&","").replace(" ","_")+".png";
            if (Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation(target)) != null)
                icon.setValue(target);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public CategoryItem triggerSidemenu(){
        this.triggerSidemenu = true;
        return this;
    }

    @On(functionName = "click")
    public void openPage() {
        Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
        Navigator.getNavigator(getDomElement()).openPage(pageCreator.get());

        MainConfigWidget mainConfigWidget = (MainConfigWidget) getDomElement().getContext().CONTEXT.get("mainconfig");
        if (mainConfigWidget != null && triggerSidemenu) mainConfigWidget.sidebar.setValue("show");
    }
}

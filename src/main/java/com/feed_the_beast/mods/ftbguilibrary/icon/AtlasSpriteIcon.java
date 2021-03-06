package com.feed_the_beast.mods.ftbguilibrary.icon;

import com.feed_the_beast.mods.ftbguilibrary.utils.IPixelBuffer;
import com.feed_the_beast.mods.ftbguilibrary.utils.PixelBuffer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldVertexBufferUploader;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

/**
 * @author LatvianModder
 */
public class AtlasSpriteIcon extends Icon
{
	public final ResourceLocation id;
	public Color4I color;

	AtlasSpriteIcon(ResourceLocation n)
	{
		id = n;
		color = Color4I.WHITE;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void draw(int x, int y, int w, int h)
	{
		TextureAtlasSprite sprite = Minecraft.getInstance().getModelManager().getAtlasTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE).getSprite(id);

		if (sprite == null)
		{
			return;
		}

		int r = color.redi();
		int g = color.greeni();
		int b = color.bluei();
		int a = color.alphai();

		float minU = sprite.getMinU();
		float minV = sprite.getMinV();
		float maxU = sprite.getMaxU();
		float maxV = sprite.getMaxV();

		sprite.getAtlasTexture().bindTexture();
		BufferBuilder buffer = Tessellator.getInstance().getBuffer();
		buffer.begin(7, DefaultVertexFormats.POSITION_COLOR_TEX);
		buffer.pos(x, y, 0D).color(r, g, b, a).tex(minU, minV).endVertex();
		buffer.pos(x, y + h, 0D).color(r, g, b, a).tex(minU, maxV).endVertex();
		buffer.pos(x + w, y + h, 0D).color(r, g, b, a).tex(maxU, maxV).endVertex();
		buffer.pos(x + w, y, 0D).color(r, g, b, a).tex(maxU, minV).endVertex();
		buffer.finishDrawing();
		WorldVertexBufferUploader.draw(buffer);
	}

	@Override
	public String toString()
	{
		return id.toString();
	}

	@Override
	public boolean hasPixelBuffer()
	{
		return true;
	}

	@Override
	@Nullable
	public IPixelBuffer createPixelBuffer()
	{
		try
		{
			return PixelBuffer.from(Minecraft.getInstance().getResourceManager().getResource(new ResourceLocation(id.getNamespace(), "textures/" + id.getPath() + ".png")).getInputStream());
		}
		catch (Exception ex)
		{
			return null;
		}
	}

	@Override
	public AtlasSpriteIcon copy()
	{
		return new AtlasSpriteIcon(id);
	}

	@Override
	public AtlasSpriteIcon withColor(Color4I color)
	{
		AtlasSpriteIcon icon = copy();
		icon.color = color;
		return icon;
	}

	@Override
	public AtlasSpriteIcon withTint(Color4I c)
	{
		return withColor(color.withTint(c));
	}
}
package me.antileaf.signature.card;

import basemod.ReflectionHacks;
import basemod.abstracts.CustomCard;
import basemod.patches.com.megacrit.cardcrawl.cards.AbstractCard.MultiCardPreview;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.modthespire.lib.SpireOverride;
import com.evacipated.cardcrawl.modthespire.lib.SpireSuper;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import me.antileaf.signature.utils.MiscHelper;
import me.antileaf.signature.utils.SignatureHelper;

public abstract class AbstractSignatureCard extends CustomCard {
	private TextureAtlas.AtlasRegion signaturePortrait = null;
	public boolean hasSignature = false;

	public SignatureHelper.Style style = SignatureHelper.DEFAULT_STYLE;

	private boolean signatureHovered = false; // 矢野你诗人？
	public float signatureHoveredTimer = 0.0F;
	public float forcedTimer = 0.0F;

	public float previewTransparency = -1.0F;
	public boolean dontAvoidSCVPanel = false;
	
	public AbstractSignatureCard(
			String id,
			String name,
			String img,
			int cost,
			String rawDescription,
			AbstractCard.CardType type,
			AbstractCard.CardColor color,
			AbstractCard.CardRarity rarity,
			AbstractCard.CardTarget target
	) {
		super(
				id,
				name,
				img,
				cost,
				rawDescription,
				type,
				color,
				rarity,
				target
		);

		this.signaturePortrait = SignatureHelper.load(this.getSignatureImgPath());
		if (this.signaturePortrait != null)
			this.hasSignature = true;
	}

	public String getSignatureImgPath() {
		if (this.textureImg.contains("/cards/"))
			return this.textureImg.replace(".png", "_s.png")
					.replace("/cards/", "/signature/");
		else if (this.textureImg.contains("/card/"))
			return this.textureImg.replace(".png", "_s.png")
					.replace("/card/", "/signature/");
		else
			return this.textureImg.replace(".png", "_s.png");
	}

	public String getSignaturePortraitImgPath() {
		return this.getSignatureImgPath().replace(".png", "_p.png");
	}

	@Override
	public void update() {
		super.update();

		if (this.signatureHovered || (MiscHelper.isInBattle() && this.isHoveredInHand(1.0F))) {
			this.signatureHoveredTimer += Gdx.graphics.getDeltaTime();
			if (this.signatureHoveredTimer >= SignatureHelper.FADE_DURATION)
				this.signatureHoveredTimer = SignatureHelper.FADE_DURATION;
		}
		else {
			this.signatureHoveredTimer -= Gdx.graphics.getDeltaTime();
			if (this.signatureHoveredTimer <= 0.0F)
				this.signatureHoveredTimer = 0.0F;
		}

		if (this.forcedTimer > 0.0F) {
			this.forcedTimer -= Gdx.graphics.getDeltaTime();
			if (this.forcedTimer <= 0.0F)
				this.forcedTimer = 0.0F;
		}
	}

	public void forceToShowDescription() {
		this.signatureHoveredTimer = SignatureHelper.FORCED_FADE_DURATION;
	}

	private float getSignatureTransparency() {
		if (this.previewTransparency >= 0.0F)
			return this.previewTransparency;

		float ret = Math.max(this.signatureHoveredTimer, this.forcedTimer) / SignatureHelper.FADE_DURATION;
		if (ret > 1.0F)
			ret = 1.0F;
		return ret;
	}

	@Override
	public void hover() {
		super.hover();
		this.signatureHovered = true;
	}

	@Override
	public void unhover() {
		super.unhover();
		this.signatureHovered = false;
	}

	@Override
	public void renderCardTip(SpriteBatch sb) {
		super.renderCardTip(sb);

		float transparency = SignatureHelper.shouldUseSignature(this.cardID) ?
				this.getSignatureTransparency() : 1.0F;

		if (this.cardsToPreview instanceof AbstractSignatureCard &&
				SignatureHelper.shouldUseSignature(this.cardsToPreview.cardID))
			((AbstractSignatureCard) this.cardsToPreview).previewTransparency = transparency;

		if (MultiCardPreview.multiCardPreview.get(this) != null) {
			for (AbstractCard c : MultiCardPreview.multiCardPreview.get(this)) {
				if (c instanceof AbstractSignatureCard &&
						SignatureHelper.shouldUseSignature(c.cardID))
					((AbstractSignatureCard) c).previewTransparency = transparency;
			}
		}
	}

	@SpireOverride
	protected void renderDescription(SpriteBatch sb) {
		if (!SignatureHelper.shouldUseSignature(this.cardID)) {
			SpireSuper.call(sb);
			return;
		}

		if (this.getSignatureTransparency() > 0.0F) {
			Color textColor = ReflectionHacks.getPrivate(this, AbstractCard.class, "textColor");
			Color goldColor = ReflectionHacks.getPrivate(this, AbstractCard.class, "goldColor");
			float textColorAlpha = textColor.a, goldColorAlpha = goldColor.a;

			textColor.a *= this.getSignatureTransparency();
			goldColor.a *= this.getSignatureTransparency();
			SpireSuper.call(sb);

			textColor.a = textColorAlpha;
			goldColor.a = goldColorAlpha;
		}
	}

	@SpireOverride
	protected void renderDescriptionCN(SpriteBatch sb) {
		if (!SignatureHelper.shouldUseSignature(this.cardID)) {
			SpireSuper.call(sb);
			return;
		}

		if (this.getSignatureTransparency() > 0.0F) {
			Color textColor = ReflectionHacks.getPrivate(this, AbstractCard.class, "textColor");
			Color goldColor = ReflectionHacks.getPrivate(this, AbstractCard.class, "goldColor");
			float textColorAlpha = textColor.a, goldColorAlpha = goldColor.a;

			textColor.a *= this.getSignatureTransparency();
			goldColor.a *= this.getSignatureTransparency();
			SpireSuper.call(sb);

			textColor.a = textColorAlpha;
			goldColor.a = goldColorAlpha;
		}
	}

	private Color getRenderColor() {
		return ReflectionHacks.getPrivate(this, AbstractCard.class, "renderColor");
	}

	private Color getTypeColor() {
		return ReflectionHacks.getPrivate(this, AbstractCard.class, "typeColor");
	}

	private void renderHelper(SpriteBatch sb, Color color, TextureAtlas.AtlasRegion img,
							  float drawX, float drawY) {
		ReflectionHacks.privateMethod(AbstractCard.class, "renderHelper",
				SpriteBatch.class, Color.class, TextureAtlas.AtlasRegion.class, float.class, float.class)
				.invoke(this, sb, color, img, drawX, drawY);
	}

	@Override
	public void renderSmallEnergy(SpriteBatch sb, TextureAtlas.AtlasRegion region, float x, float y) {
		if (SignatureHelper.shouldUseSignature(this.cardID)) {
			Color renderColor = this.getRenderColor();

			float alpha = renderColor.a;
			renderColor.a *= this.getSignatureTransparency();

			super.renderSmallEnergy(sb, region, x, y);

			renderColor.a = alpha;
		}
		else
			super.renderSmallEnergy(sb, region, x, y);
	}

	@SpireOverride
	protected void renderImage(SpriteBatch sb, boolean hovered, boolean selected) {
		SpireSuper.call(sb, hovered, selected);

		if (SignatureHelper.shouldUseSignature(this.cardID) && this.getSignatureTransparency() > 0.0F) {
			Color renderColor = this.getRenderColor();

			float alpha = renderColor.a;
			renderColor.a *= this.getSignatureTransparency();

			TextureAtlas.AtlasRegion shadow = this.description.size() >= 4 || this.style.descShadowSmall == null ?
					this.style.descShadow : this.style.descShadowSmall;

			if (shadow != null)
				this.renderHelper(sb, renderColor, shadow,
						this.current_x, this.current_y);

			renderColor.a = alpha;
		}
	}

	@SpireOverride
	protected void renderCardBg(SpriteBatch sb, float x, float y) {
		if (!SignatureHelper.shouldUseSignature(this.cardID))
			SpireSuper.call(sb, x, y);
	}

	@SpireOverride
	protected void renderPortrait(SpriteBatch sb) {
		if (SignatureHelper.shouldUseSignature(this.cardID)) {
			sb.setColor(this.getRenderColor());
			sb.draw(this.signaturePortrait,
					this.current_x - 256.0F,
					this.current_y - 256.0F,
					256.0F, 256.0F, 512.0F, 512.0F,
					this.drawScale * Settings.scale,
					this.drawScale * Settings.scale,
					this.angle);
		}
		else
			SpireSuper.call(sb);
	}

	@SpireOverride
	protected void renderJokePortrait(SpriteBatch sb) {
		if (SignatureHelper.shouldUseSignature(this.cardID))
			this.renderPortrait(sb);
		else
			SpireSuper.call(sb);
	}

	@SpireOverride
	protected void renderPortraitFrame(SpriteBatch sb, float x, float y) {
		if (SignatureHelper.shouldUseSignature(this.cardID)) {
			TextureAtlas.AtlasRegion frame;

			if (this.type == AbstractCard.CardType.ATTACK) {
				if (this.rarity == AbstractCard.CardRarity.RARE)
					frame = this.style.cardTypeAttackRare;
				else if (this.rarity == AbstractCard.CardRarity.UNCOMMON)
					frame = this.style.cardTypeAttackUncommon;
				else
					frame = this.style.cardTypeAttackCommon;
			}
			else if (this.type == AbstractCard.CardType.POWER) {
				if (this.rarity == AbstractCard.CardRarity.RARE)
					frame = this.style.cardTypePowerRare;
				else if (this.rarity == AbstractCard.CardRarity.UNCOMMON)
					frame = this.style.cardTypePowerUncommon;
				else
					frame = this.style.cardTypePowerCommon;
			}
			else {
				if (this.rarity == AbstractCard.CardRarity.RARE)
					frame = this.style.cardTypeSkillRare;
				else if (this.rarity == AbstractCard.CardRarity.UNCOMMON)
					frame = this.style.cardTypeSkillUncommon;
				else
					frame = this.style.cardTypeSkillCommon;
			}

			if (frame != null)
				this.renderHelper(sb, this.getRenderColor(), frame, x, y);
		}
		else
			SpireSuper.call(sb, x, y);
	}

	@SpireOverride
	protected void renderBannerImage(SpriteBatch sb, float x, float y) {
		if (!SignatureHelper.shouldUseSignature(this.cardID))
			SpireSuper.call(sb, x, y);
	}

	@SpireOverride
	protected void renderType(SpriteBatch sb) {
		if (!SignatureHelper.shouldUseSignature(this.cardID)) {
			SpireSuper.call(sb);
			return;
		}

		String text;
		if (this.type == AbstractCard.CardType.ATTACK)
			text = AbstractCard.TEXT[0];
		else if (this.type == AbstractCard.CardType.SKILL)
			text = AbstractCard.TEXT[1];
		else if (this.type == AbstractCard.CardType.POWER)
			text = AbstractCard.TEXT[2];
		else if (this.type == AbstractCard.CardType.CURSE)
			text = AbstractCard.TEXT[3];
		else if (this.type == AbstractCard.CardType.STATUS)
			text = AbstractCard.TEXT[7];
		else
			text = AbstractCard.TEXT[5];

		BitmapFont font = FontHelper.cardTypeFont;
		font.getData().setScale(this.drawScale);
		this.getTypeColor().a = this.getRenderColor().a;
		FontHelper.renderRotatedText(sb, font, text, this.current_x,
				this.current_y - 195.0F * this.drawScale * Settings.scale,
				0.0F,
				-1.0F * this.drawScale * Settings.scale,
				this.angle, false, this.getTypeColor());
	}
}

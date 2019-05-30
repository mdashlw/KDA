package ru.mdashlw.kda.builder.impl

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.EmbedType
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.entities.Role
import ru.mdashlw.kda.build
import ru.mdashlw.kda.builder.Builder
import ru.mdashlw.kda.dsl.KdaDslMarker
import java.awt.Color
import java.time.OffsetDateTime

@KdaDslMarker
class EmbedBuilder(parent: MessageEmbed? = null) : Builder<MessageEmbed>() {
    var url: String? = parent?.url
    var title: String? = parent?.title
    var description: String? = parent?.description
    var timestamp: OffsetDateTime? = parent?.timestamp
    var color: Color? = parent?.color
    var thumbnail: MessageEmbed.Thumbnail? = parent?.thumbnail
    var author: MessageEmbed.AuthorInfo? = parent?.author
    var footer: MessageEmbed.Footer? = parent?.footer
    var image: MessageEmbed.ImageInfo? = parent?.image
    var fields: MutableList<MessageEmbed.Field> = parent?.fields?.toMutableList() ?: mutableListOf()

    inline fun thumbnail(block: ThumbnailBuilder.() -> Unit) {
        thumbnail = build(ThumbnailBuilder(), block)
    }

    inline fun author(block: AuthorBuilder.() -> Unit) {
        author = build(AuthorBuilder(), block)
    }

    inline fun footer(block: FooterBuilder.() -> Unit) {
        footer = build(FooterBuilder(), block)
    }

    inline fun image(block: ImageBuilder.() -> Unit) {
        image = build(ImageBuilder(), block)
    }

    inline fun field(block: FieldBuilder.() -> Unit) {
        fields.add(build(FieldBuilder(), block))
    }

    override fun build(): MessageEmbed =
        MessageEmbed(
            url,
            title,
            description,
            EmbedType.RICH,
            timestamp,
            color?.rgb ?: Role.DEFAULT_COLOR_RAW,
            thumbnail,
            null,
            author,
            null,
            footer,
            image,
            fields
        )

    @KdaDslMarker
    class ThumbnailBuilder : Builder<MessageEmbed.Thumbnail>() {
        var url: String? = null

        override fun build(): MessageEmbed.Thumbnail =
            MessageEmbed.Thumbnail(url, null, 0, 0)
    }

    @KdaDslMarker
    class AuthorBuilder : Builder<MessageEmbed.AuthorInfo>() {
        var name: String? = null
        var url: String? = null
        var icon: String? = null

        override fun build(): MessageEmbed.AuthorInfo =
            MessageEmbed.AuthorInfo(name, url, icon, null)
    }

    @KdaDslMarker
    class FooterBuilder : Builder<MessageEmbed.Footer>() {
        var text: String? = null
        var icon: String? = null

        override fun build(): MessageEmbed.Footer =
            MessageEmbed.Footer(text, icon, null)
    }

    @KdaDslMarker
    class ImageBuilder : Builder<MessageEmbed.ImageInfo>() {
        var url: String? = null

        override fun build(): MessageEmbed.ImageInfo =
            MessageEmbed.ImageInfo(url, null, 0, 0)
    }

    @KdaDslMarker
    class FieldBuilder : Builder<MessageEmbed.Field>() {
        var name: String = EmbedBuilder.ZERO_WIDTH_SPACE
        var value: String = EmbedBuilder.ZERO_WIDTH_SPACE
        var inline: Boolean = true

        override fun build(): MessageEmbed.Field =
            MessageEmbed.Field(name, value, inline)
    }

    companion object {
        inline fun thumbnail(block: ThumbnailBuilder.() -> Unit): MessageEmbed.Thumbnail =
            build(ThumbnailBuilder(), block)

        inline fun author(block: AuthorBuilder.() -> Unit): MessageEmbed.AuthorInfo =
            build(AuthorBuilder(), block)

        inline fun footer(block: FooterBuilder.() -> Unit): MessageEmbed.Footer =
            build(FooterBuilder(), block)

        inline fun image(block: ImageBuilder.() -> Unit): MessageEmbed.ImageInfo =
            build(ImageBuilder(), block)

        inline fun field(block: FieldBuilder.() -> Unit): MessageEmbed.Field =
            build(FieldBuilder(), block)
    }
}

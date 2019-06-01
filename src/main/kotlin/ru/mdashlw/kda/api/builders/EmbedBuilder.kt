package ru.mdashlw.kda.api.builders

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.*
import ru.mdashlw.kda.api.Builder
import ru.mdashlw.kda.api.build
import ru.mdashlw.kda.internal.dsl.KdaDslMarker
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

    fun thumbnail(user: User) {
        thumbnail {
            url = user.effectiveAvatarUrl
        }
    }

    fun thumbnail(member: Member) {
        thumbnail(member.user)
    }

    fun author(user: User) {
        author {
            name = user.name
            icon = user.effectiveAvatarUrl
        }
    }

    fun author(member: Member) {
        author {
            name = member.effectiveName
            icon = member.user.effectiveAvatarUrl
        }
    }

    fun footer(user: User) {
        footer {
            text = user.name
            icon = user.effectiveAvatarUrl
        }
    }

    fun footer(member: Member) {
        footer {
            text = member.effectiveName
            icon = member.user.effectiveAvatarUrl
        }
    }

    fun image(user: User) {
        image {
            url = user.effectiveAvatarUrl
        }
    }

    fun image(member: Member) {
        image(member.user)
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

package ru.firix.command

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.bots.AbsSender
import ru.firix.exception.PlanningPokerBotException
import ru.firix.service.VotingService
import ru.firix.util.sendMessage

@Component
class VoteStatusCommand @Autowired constructor(private val votingService: VotingService):
        BotCommand("/voteStatus", "status of current voting") {
    private val logger = LoggerFactory.getLogger(javaClass)

    override fun execute(sender: AbsSender, user: User, chat: Chat, arguments: Array<out String>) {
        logger.debug("Try to handle '/voteStatus' from user: $user, and chat: $chat. Arguments: $arguments")
        val chatId = chat.id
        if (!chat.isGroupChat) {
            sendMessage(sender, chatId, "/votestatus command available only in group chat, use /help.")
            return
        }

        try {
            sendMessage(sender, chatId, votingService.getActiveVote(chatId).toString())
        } catch(ex: PlanningPokerBotException) {
            sendMessage(sender, chatId, ex.message)
        }
    }
}
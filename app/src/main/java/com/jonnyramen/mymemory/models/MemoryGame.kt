package com.jonnyramen.mymemory.models

import com.jonnyramen.mymemory.utils.DEFAULT_ICONS

class MemoryGame(private val boardSize: BoardSize) {

    val cards: List<MemoryCard>;
    var numPairsFound = 0;

    private var indexOfSingleSelectedCard: Int? = null;

    init {
        val chosenImages = DEFAULT_ICONS.shuffled().take(boardSize.getNumPairs());
        val randomizedImages = (chosenImages+chosenImages).shuffled();
        cards = randomizedImages.map { MemoryCard(it) }
    }

    fun flipCard(position: Int):Boolean {
        val card = cards[position];
        var foundMatch = false;
        if (indexOfSingleSelectedCard == null) {
            // 0-2 cards previously flipped over => restore(maybe no-op) flip over
            restoreCards();
            indexOfSingleSelectedCard = position;
        } else {
            // 1 card previously flipped over => flip over selected + check if images match
            foundMatch = checkForMatch(indexOfSingleSelectedCard!!, position);
            indexOfSingleSelectedCard = null;
        }

        card.isFaceUp = !card.isFaceUp;
        return foundMatch;
    }

    private fun checkForMatch(position1: Int, position2: Int): Boolean {
        if (cards[position1].identifier != cards[position2].identifier) {
            return false;
        }
        // Else we had a match
        cards[position1].isMatched = true;
        cards[position2].isMatched = true;
        numPairsFound++;
        return true;
    }

    private fun restoreCards() {
        for (card in cards) {
            if (!card.isMatched) {
                card.isFaceUp = false;
            }
        }
    }

    fun haveWonGame(): Boolean {
        return numPairsFound == boardSize.getNumPairs();
    }

    fun isCardFaceUp(position: Int): Boolean {
        return cards[position].isFaceUp;
    }
}